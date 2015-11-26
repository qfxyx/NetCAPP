package com.example.jinanuniversity.Activity;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.data.bo.BoBaseActivity;
import com.example.jinanuniversity.json.Base;
import com.example.jinanuniversity.util.ActivityCollector;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

public class LandingActivity extends BoBaseActivity implements OnClickListener {
	
	private static final String TAG = "Activity.LandingActivity";
	
	private EditText user_account, user_password;
	private ImageView bt_landing, bt_bowout;
	private String account, password, name, groupname;
	private CheckBox cb_save;
	private boolean isSave;
	private String guestInfo;
	private PreferencesHelper preferencesHelper;
	private ProgressDialog mProgressDialog;
	
	private String timestamp, address, buildingList;
	private String troubleReasonList;
	private String dealResultList;
	private String delayReasonList;
	private String passReasonList;
	private String templateList;
	private String groupList;
	private String property;
    private ParamsManager paramsManager;
	
	public static final String ACTION_INTENT_TEST = "com.terry.broadcast.test";

	// 用于判断是否使用wifi连接发送数据的字段
	private boolean canSendDataBywifi = false;
	
	// judge wifi and data switch open or close
	private boolean wifiConnectState = false, mobileConnectState = false;

	private WifiManager wifiManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Just Test
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate start");
		setContentView(R.layout.landing);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),PreferencesHelper.LOGININFO);
		isSave = preferencesHelper.getBoolean("isSave", false);
		Log.i(TAG, "isSave = " + isSave);
		initViews();
		ActivityCollector.addActivity(this);
		Log.i(TAG, "onCreate end");
	}

	/** 实例化 */
	private void initViews() {
		user_account = (EditText) findViewById(R.id.ed_account);
		user_password = (EditText) findViewById(R.id.ed_password);
		bt_bowout = (ImageView) findViewById(R.id.bt_bowout);
		bt_landing = (ImageView) findViewById(R.id.bt_landing);
		cb_save = (CheckBox) findViewById(R.id.enrollments);
		cb_save.setChecked(isSave);
		bt_bowout.setOnClickListener(this);
		bt_landing.setOnClickListener(this);
		paramsManager=(ParamsManager)getApplication();
		// TODO DELETE IT WHEN DEPLOY TO USER
//		user_account.setText("ffw");
//		user_password.setText("feiwen");
		user_account.setText("");
		user_password.setText("");
		if (isSave) {
			String usernames = preferencesHelper.getString("account", "");
			String userpsws = preferencesHelper.getString("password", "");
			user_password.setText(userpsws);
			user_account.setText(usernames);
		} else {
			// do nothing
		}
	}

	/**
	 * @author luxun 登陆
	 */
	private class login extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			timestamp = ParamsManager.getTime();
			String sign = "";
			paramsManager.setStorePassword(password);
			password = ParamsManager.enCode(ParamsManager.getMd5sign(password));
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + password);
			IEasyHttpApiV1 httApi = new IEasyHttpApiV1();
			IEasy ieasy = new IEasy(httApi);
			guestInfo = ieasy.invitationCodeLogin(Config.APPKEY, timestamp, sign, Config.VER, account, password);
			Log.i(TAG, "login doInBackground end");
			return guestInfo;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			if (guestInfo.equals("noEffect")) {
				Toast.makeText(LandingActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
			} else {
				Base guestInfo = new Base(result);
				guestInfo.praser();
				String info = guestInfo.getInfo();
				int code = guestInfo.getCode();
				buildingList = guestInfo.getBuildingList();
				troubleReasonList = guestInfo.getTroubleReasonList();
				dealResultList = guestInfo.getDealResultList();
				delayReasonList = guestInfo.getDelayReasonList();
				passReasonList = guestInfo.getPassReasonList();
				templateList = guestInfo.getTemplateList();
				groupList=guestInfo.getGroupList();
				property = guestInfo.getMtVwUser().getProperty();
				if (code == 0) {
					loginSuccess(guestInfo);
				} else if (code == 1) {
					Toast.makeText(LandingActivity.this, info,
							Toast.LENGTH_SHORT).show();
				}
			}
		}

		@Override
		protected void onPreExecute() {
			System.out.println("登陆中...");
			showProgressDialog();
		}
	}

	/** 提示是否登陆成功 */
	//传递对象 Base类需实现serializable接口
	private void loginSuccess(Base guestInfo) {
		try {
			this.address = (guestInfo.getMtVwUser().getAddress() == null ? " ": guestInfo.getMtVwUser().getAddress());
			this.account = (guestInfo.getMtVwUser().getAccount() == null ? " ": guestInfo.getMtVwUser().getAccount());
			this.name = (guestInfo.getMtVwUser().getName() == null ? " ": guestInfo.getMtVwUser().getName());
			this.groupname = (guestInfo.getMtVwUser().getGroupname() == null ? " ": guestInfo.getMtVwUser().getGroupname());
			this.buildingList = guestInfo.getBuildingList();

			preferencesHelper.setString("address", this.address);
			preferencesHelper.setString("account", this.account);
			preferencesHelper.setString("name", this.name);
			preferencesHelper.setString("groupname", this.groupname);
			preferencesHelper.setString("buildingList", this.buildingList);
			preferencesHelper.setString("troubleReasonList",this.troubleReasonList);
			preferencesHelper.setString("dealResultList", this.dealResultList);
			preferencesHelper.setString("delayReasonList", this.delayReasonList);
			preferencesHelper.setString("passReasonList", this.passReasonList);
			preferencesHelper.setString("templateList", this.templateList);
			preferencesHelper.setString("groupList", this.groupList);
			preferencesHelper.setString("property", this.property);

			isSave = cb_save.isChecked();
			preferencesHelper.setBoolean("isSave", isSave);
			if (isSave) {
				preferencesHelper.setString("password", user_password.getText().toString().trim());
			}
			preferencesHelper.setString("loginServer", user_password.getText().toString().trim());
			Toast.makeText(LandingActivity.this, "恭喜你登陆成功！", Toast.LENGTH_SHORT).show();
			startActivity(new Intent(LandingActivity.this, MenuActivity.class));
			// you should set mProgressDialog=null before you close this
			// activity
			finish();
		} catch (Exception e) {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_landing:
			account = user_account.getText().toString().trim();
			password = user_password.getText().toString().trim();
			if (true == checkNet()) {
				if (senseNull()) {
					new login().execute();
				}
			} else {
				if (mobileConnectState == false && wifiConnectState == false) {
					Toast.makeText(LandingActivity.this, "您当前的网络状态不佳，请稍后再试！",
							Toast.LENGTH_SHORT).show();
				}

			}
			setAllConnectStateToFalse();
			Intent intent = new Intent(ACTION_INTENT_TEST);
			sendBroadcast(intent);
			break;

		case R.id.bt_bowout:
			System.exit(0);
			break;
		}
	}

	private void setAllConnectStateToFalse() {
		wifiConnectState = false;
		mobileConnectState = false;
		canSendDataBywifi = false;
	}

	/** 账号密码判断 */
	public boolean senseNull() {
		Log.i(TAG, "senseNull start");
		if (account.length() == 0 || password.length() == 0) {
			Toast.makeText(LandingActivity.this, "不能有一个或一个以上的编辑框为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (password.length() < 6) {
			Toast.makeText(LandingActivity.this, "格式错误!密码长度不能少于六位数！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		Log.i(TAG, "senseNull end");
		return true;
	}

	private class CheckNetworkConnect extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			try {
				String url = "http://www.baidu.com";
				Connection myMtime = Jsoup.connect(url);
				Response response = myMtime
						.method(Method.GET)
						.header("Accept",
								"text/html, application/xhtml+xml, */*")
						.header("Accept-Language", "zh-CN")
						.header("User-Agent",
								"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)")
						.header("Connection", "Keep-Alive").execute();
				Document document = response.parse();
				String title = document.title();
				// Log.i(TAG, "返回的验证html代码\n" + document.toString());
				if (title.contains("百度一下")) {
					canSendDataBywifi = true;
				} else {
					canSendDataBywifi = false;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * 判断是否打开网络
	 * 
	 * @return
	 */
	public boolean checkNet() {
		Log.i(TAG, "checkNet() start");
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		// 首先判断wifi是否能够发送数据
		if (wifi == State.CONNECTED) {
			wifiConnectState = true;
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			Log.i(TAG, "WifiInfo =" + wifiInfo.toString());
			Log.i(TAG, "wifi  connected but not send data");
			try {
				CheckNetworkConnect checkNetworkConnect = new CheckNetworkConnect();
				checkNetworkConnect.execute();
				// 这一步是为了使认证网络连接和认证网络连接的过程具有先后顺序
				checkNetworkConnect.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			// 表示已经通过wifi连接到Internet
			if (true == canSendDataBywifi) {
				Log.i(TAG, "wifi return true");
				return true;
			} else {
				Toast.makeText(LandingActivity.this,
						wifiInfo.getSSID() + "已连接，需要登录验证", Toast.LENGTH_SHORT)
						.show();
				return false;
			}
		}
		// wifi连接不成功再使用数据连接
		else {
			wifiConnectState = false;
			Log.i(TAG, "wifi  not connected ");
			// 判断是否可以使用数据连接
			State moblie = manager.getNetworkInfo(
					ConnectivityManager.TYPE_MOBILE).getState();
			if (moblie == State.CONNECTED) {
				mobileConnectState = true;
				Log.i(TAG, "moblie connected ");
				Log.i(TAG, "moblie return true");
				return true;
			} else {
				mobileConnectState = false;
				Log.i(TAG, "moblie not connected ");
				Log.i(TAG, "moblie return flase");
				return false;
			}
		}
	}

	/**
	 * 
	 * @return 登陆提示
	 */
	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("登录中，请稍候...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mProgressDialog = dialog;
		}
		mProgressDialog.show();
		return mProgressDialog;
	}

	private void dismissProgressDialog() {
		try {
			mProgressDialog.dismiss();
		} catch (IllegalArgumentException e) {
		}
	}
	@Override
	protected void onDestroy(){
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

}
