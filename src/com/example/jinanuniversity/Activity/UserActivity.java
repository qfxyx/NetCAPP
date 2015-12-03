package com.example.jinanuniversity.Activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.UserListParser;
import com.example.jinanuniversity.json.testMapToJSON;
import com.example.jinanuniversity.types.NetcUser;
import com.example.jinanuniversity.util.ActivityCollector;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.google.gson.Gson;

/** 用户 */
public class UserActivity extends Activity implements OnClickListener {
	private static final String TAG = "Activity.UserActivity";
	private CheckBox tx_number, tx_name, tx_ip, tv_mac, tv_address, cb_active;
	private EditText et_number, et_name, spinner_ip, spinner_mac, ed_psw,
			ed_room;
	private TextView txt_address;
	private Button bt_inquire;
	private ImageView img_address;
	private ProgressDialog mProgressDialog;
	private String account, ruleList, password;
	private int code = 1, pageNo = 1;
	private testMapToJSON ruleListJson = new testMapToJSON();
	private int requestCode = 1;
	private PreferencesHelper preferencesHelper;
	ParamsManager paramsManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user);
		paramsManager=(ParamsManager)getApplication();
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		initView();
		ActivityCollector.addActivity(this);
		Log.i(TAG, "onCreate end");
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume start");
		super.onResume();
		UpdateSession();
		Log.i(TAG, "onResume end");
	}

	private void initView() {
		tx_number = (CheckBox) findViewById(R.id.tx_number);
		tx_name = (CheckBox) findViewById(R.id.tx_name);
		tx_ip = (CheckBox) findViewById(R.id.tx_ip);
		tv_mac = (CheckBox) findViewById(R.id.tv_mac);
		tv_address = (CheckBox) findViewById(R.id.tv_address);
		cb_active = (CheckBox) findViewById(R.id.cb_active);
		et_number = (EditText) findViewById(R.id.et_number);
		et_name = (EditText) findViewById(R.id.et_name);
		spinner_ip = (EditText) findViewById(R.id.spinner_ip);
		spinner_mac = (EditText) findViewById(R.id.spinner_mac);
		ed_psw = (EditText) findViewById(R.id.ed_psw);
		ed_room = (EditText) findViewById(R.id.ed_room);
		txt_address = (TextView) findViewById(R.id.txt_address);
		bt_inquire = (Button) findViewById(R.id.bt_inquire);
		img_address = (ImageView) findViewById(R.id.img_address);

		tx_number.setOnClickListener(this);
		tx_name.setOnClickListener(this);
		tx_ip.setOnClickListener(this);
		tv_mac.setOnClickListener(this);
		tv_address.setOnClickListener(this);
		cb_active.setOnClickListener(this);
		bt_inquire.setOnClickListener(this);
		txt_address.setOnClickListener(this);
		img_address.setOnClickListener(this);

		// TODO TO DELETE IT WHEN IT IS NECESSAY
		tx_number.setSelected(true);
		//注释掉下面这一句，否则会引起姓名查询异常
		//ruleListJson.setUserIdSelect("yes");

		et_number.setText("");
		ed_psw.setText("");
	}

	/** 加载列表数据 */
	private class getInfo extends AsyncTask<String, Void, Integer> {

		//List<NetcUser> list;

		@Override
		protected Integer doInBackground(String... params) {
			Log.i(TAG, "getInfo doInBackground start");
			String timestamp = ParamsManager.getTime();
			String pwd = ParamsManager.getMd5sign(ParamsManager
					.enCode(password));
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account
					+ ruleList + pageNo + pwd);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = ieasy.getUserList(sign, timestamp, account, ruleList,
					String.valueOf(pageNo), pwd);
			Log.i(TAG, "getInfo doInBackground re = " + re);
			//list = getListFromJosn(re);
			getListFromJosn(re);
			Log.i(TAG, "getInfo doInBackground end");
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dismissProgressDialog();
			if (result == 0) {
				Intent intent = new Intent(UserActivity.this,
						UserListActivity.class);
				intent.putExtra("pwd", password);
				intent.putExtra("ruleListJson", ruleList);
				startActivity(intent);
			} else if (result == 1) {
				Toast.makeText(getApplicationContext(), "密码错误",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}
	}

	public List<NetcUser> getListFromJosn(String json) {
		Log.i(TAG, "getListFromJosn start");
		UserListParser parser = new UserListParser(json);
		code = parser.getCode();
		Log.i(TAG, "getListFromJosn end");
		return parser.praserList();
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tx_number:

			setOnClick(tx_number.isChecked(), et_number.getId());

			break;

		case R.id.tx_name:
			if (tx_name.isChecked()) {
				ruleListJson.setUserNameSelect("yes");
			} else {
				ruleListJson.setUserNameSelect("no");
			}
			setOnClick(tx_name.isChecked(), et_name.getId());
			break;

		case R.id.tx_ip:
			if (tx_ip.isChecked()) {
				ruleListJson.setIpSelect("yes");
			} else {
				ruleListJson.setIpSelect("no");
			}
			setOnClick(tx_ip.isChecked(), spinner_ip.getId());
			break;

		case R.id.tv_mac:
			if (tv_mac.isChecked()) {
				ruleListJson.setMacSelect("yes");
			} else {
				ruleListJson.setMacSelect("no");
			}
			setOnClick(tv_mac.isChecked(), spinner_mac.getId());
			break;

		case R.id.tv_address:
			if (tv_address.isChecked()) {
				ruleListJson.setAddressSelect("yes");
			} else {
				ruleListJson.setAddressSelect("no");
			}
			setOnClick(tv_address.isChecked(), ed_room.getId());
			break;

		case R.id.cb_active:
			if (cb_active.isChecked()) {
				ruleListJson.setActiveSelect("yes");
			} else {
				ruleListJson.setActiveSelect("no");
			}
			break;

		case R.id.bt_inquire:
			password = ed_psw.getText().toString().trim();
			if (password.length() < 6) {
				Toast.makeText(getApplicationContext(), "密码最少6位数",
						Toast.LENGTH_SHORT).show();
				break;
			}
			//经测试有个id优先原则
			if (tx_number.isChecked()&&!et_number.getText().toString().isEmpty()) {
				ruleListJson.setUserIdSelect("yes");
			} else {
				ruleListJson.setUserIdSelect("no");
			}
			ruleListJson.setUserId(et_number.getText().toString().trim());
			ruleListJson.setUserName(et_name.getText().toString().trim());
			ruleListJson.setIp(spinner_ip.getText().toString().trim());
			// 使mac地址的所有字母为大写字母
			ruleListJson.setMac(spinner_mac.getText().toString().toUpperCase()
					.trim());
			ruleListJson.setBuilding(txt_address.getText().toString().trim());
			ruleListJson.setRoom(ed_room.getText().toString().trim());
			Gson gson = new Gson();
			String json = gson.toJson(ruleListJson);
			ruleList = json;
			// Intent intent = new Intent(UserActivity.this,
			// UserListActivity.class);
			// intent.putExtra("pwd", pwd);
			// intent.putExtra("ruleListJson", ruleList);
			// startActivity(intent);
			// System.out.println("ruleList=="+ruleList);
			Log.i(TAG, "mac address = " + ruleListJson.getMac());
			new getInfo().execute();
			break;

		case R.id.txt_address:

			break;

		case R.id.img_address:
			startActivityForResult(new Intent(this, BuildingList.class),
					requestCode);
			break;

		default:
			break;
		}
	}

	/** 设置EditText 是否可输入 */
	private void setOnClick(Boolean isClick, int id) {
		EditText editText = (EditText) findViewById(id);
		if (isClick) {
			editText.setEnabled(true);
		} else {
			editText.setEnabled(false);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			String building = data.getStringExtra("building") == null ? ""
					: data.getStringExtra("building");
			if (building != "") {
				txt_address.setText(building);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("查询中，请稍候...");
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
	private void UpdateSession(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("update session start ");
				 String timestamp = ParamsManager.getTime();
				String sign = "";
				String password = preferencesHelper.getString("store_password", "");
				password = ParamsManager.enCode(ParamsManager.getMd5sign(password));
				sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + password);
				IEasyHttpApiV1 httApi = new IEasyHttpApiV1();
				IEasy ieasy = new IEasy(httApi);
				String guestInfo;
				guestInfo = ieasy.invitationCodeLogin(Config.APPKEY, timestamp, sign, Config.VER, account, password);
				System.out.println("update session end ");
				if (guestInfo.equals("noEffect")){
					System.out.println("update session failed ");
				}else {
					System.out.println("update session succeed ");
				}
			}
		}).start();
	}

}
