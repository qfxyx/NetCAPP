package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.adapter.MaintainAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MaintainListParser;
import com.example.jinanuniversity.json.testMapToJSON;
import com.example.jinanuniversity.types.MaintainItemType;
import com.example.jinanuniversity.util.ActivityCollector;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.example.jinanuniversity.view.PullToRefreshListView;
import com.example.jinanuniversity.view.PullToRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

/** 维护 */
public class MaintainActivity extends Activity implements OnClickListener {

	// 用于日志输入
	private static final String TAG = "Activity.MaintainActivity";

	private PullToRefreshListView mListView;
	private MaintainAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private LayoutInflater mInflater;
	private LinearLayout mMoreView;
	private TextView footerMoreTxt;
	private ProgressBar footerMoreProgress;
	private Button bt_rule;
	private ProgressBar message_refresh_progress;
	PreferencesHelper preferencesHelper;
	private String account, timestamp, name, ruleList, groupname;
	private String timeSelect, groupSelect, jobOrder, MyResponseSelect,
			UnCloseJobSelect, CloseJobSelect, UnResponseJobSelect;
	private boolean xiangying;
	int pageNo = 1;
	public boolean isRefresh = false;
	private ParamsManager pm;

	private IntentFilter intentFilter;
	private NetworkChangeRecevier networkChangeRecevier;
	private Button networkChange;

	private final int SESSION_FAIL=1;
	private final int SESSION_OK=2;
	private final int UPDATE_SESSION_OK=3;
	private final int UPDATE_SESSION_FAIL=4;

	  Handler handler =new Handler(){
		@Override
		public void handleMessage(Message message){
			switch (message.what){

				case SESSION_FAIL:
					Log.i(TAG,"Session has failed !");
					updateSession();
					break;

				case SESSION_OK:
					Log.i(TAG,"Session is okey !");
					List<MaintainItemType> list = (List<MaintainItemType>)message.obj;
					message_refresh_progress.setVisibility(View.GONE);
					if (list != null) {
						if (list.size() == 0)
							Toast.makeText(getApplicationContext(), "暂无更新",
									Toast.LENGTH_SHORT).show();
						else {
							HashMap<String, Object> map = new HashMap<String, Object>();
							for (int i = 0; i < list.size(); i++) {
								map = new HashMap<String, Object>();
								map.put("userId", list.get(i).getUserId());
								map.put("id", list.get(i).getId());
								map.put("name", list.get(i).getName());
								map.put("jobLevel", list.get(i).getJobLevel());
								map.put("dealResult", list.get(i).getDealResult());
								map.put("address", list.get(i).getAddress());
								map.put("accepteTime", list.get(i).getAccepteTime());
								map.put("trblStatus", list.get(i).getTrblStatus());
								mData.add(map);
							}

							footerMoreTxt.setText("更多");
							mMoreView.setClickable(true);
						}
						if (pageNo == 1) {
							mListView.onRefreshComplete();
						} else {
							mListView.setSelection(mListView.getSelectedItemPosition());
						}
					} else {
						Toast.makeText(getApplicationContext(), "更新失败",
								Toast.LENGTH_SHORT).show();
					}
					// View 控件进行创新之用 只能在UI线程中使用
					Log.i(TAG, "getmtJobList onPostExecute update the content of listview");
					mListView.invalidate();
					footerMoreProgress.setVisibility(View.INVISIBLE);

					break;

				case UPDATE_SESSION_OK:
					message_refresh_progress.setVisibility(View.GONE);
					pageNo = 1;
					mData.clear();
					new getmtJobList().execute();// 获取新列表

					break;

				case UPDATE_SESSION_FAIL:
					message_refresh_progress.setVisibility(View.GONE);
					mListView.onRefreshComplete();
					Toast.makeText(MaintainActivity.this,"更新失败，请检查网络状况或者重新登录",
							Toast.LENGTH_SHORT).show();
					break;

				default:
					break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintain);
		getDate();
		initViews();
		ActivityCollector.addActivity(this);
		// 下面是一个线程
		new getmtJobList().execute();
		Log.i(TAG, "onCreate end");
	}

	// 筛选规则返回后需要更新数据
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume() start");
		super.onResume();
		Log.i(TAG, "onResume xiangying = " + xiangying);
		Log.i(TAG, "the password store in the cellphone = "
				+preferencesHelper.getString("store_password", "") );

		/*
		if (xiangying) {
			mData.clear();
			new getmtJobList().execute();
		} */

		//此时的pm对象获取的为全局数据
		pm = (ParamsManager) getApplication();
		isRefresh = pm.isRefresh();
		Log.i(TAG, "onResume isRefresh = " + isRefresh);
		if (isRefresh) {
			Log.i(TAG, "在onResume里面重新更新数据");
			isRefresh = false;
			mData.clear();
			pm.setRefresh(isRefresh);
			getDate();
			pageNo=1;
			// 重新设置筛选的规则是在getmtJobList里面设置的
			new getmtJobList().execute();
		}
		Log.i(TAG, "onResume() end");
	}

	private void getDate() {
		Log.i(TAG, "getDate() start");
		preferencesHelper = new PreferencesHelper(getApplicationContext(),PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		name = preferencesHelper.getString("name", "");
		groupname = preferencesHelper.getString("groupname", "");
		groupSelect = preferencesHelper.getString("scope", groupname);
		// 由于支援和所有对应的请求参数有别于其他组名。
		// 所以需要加入下面的控制代码
		if (groupSelect.equals("支援")) {
			groupSelect = "support";
		}
		if (groupSelect.equals("所有")) {
			groupSelect = "all";
		}
		timeSelect = preferencesHelper.getString("sex", "3");
		jobOrder = preferencesHelper.getString("sort", "time");
		MyResponseSelect = preferencesHelper.getString("select", "no");// 已响应
		UnResponseJobSelect = preferencesHelper.getString("unselect", "no");// 未响应
		CloseJobSelect = preferencesHelper.getString("closeJobSelect", "no");// 已消单
		UnCloseJobSelect = preferencesHelper.getString("uncloseJobSelect", "yes");// 未消单
		xiangying = preferencesHelper.getBoolean("xiangying", false);
		Log.i(TAG, "getDate() end");
	}

	private void initViews() {
		Log.i(TAG, "initViews() start");
		message_refresh_progress = (ProgressBar) findViewById(R.id.message_refresh_progress);
		mListView = (PullToRefreshListView) findViewById(R.id.listView1);
		mAdapter = new MaintainAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.i(TAG, "setOnRefreshListener onRefresh start");
				 pageNo = 1;
				 mData.clear();
				//new getmtJobList().execute();// 获取新列表
				message_refresh_progress.setVisibility(View.VISIBLE);
				testSession();
				Log.i(TAG, "setOnRefreshListener onRefresh end");
			}
		});
		mInflater = (LayoutInflater) getApplicationContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		mMoreView = (LinearLayout) mInflater.inflate(
				R.layout.pull_to_refresh_footer, null);
		footerMoreProgress = (ProgressBar) mMoreView
				.findViewById(R.id.refresh_footer_more_progress);
		footerMoreProgress.setVisibility(View.INVISIBLE);
		footerMoreTxt = (TextView) mMoreView
				.findViewById(R.id.refresh_footer_more_txt);
		mMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pageNo++;
				testSession();
			}
		});
		mListView.addFooterView(mMoreView);
		bt_rule = (Button) findViewById(R.id.bt_rule);
		bt_rule.setOnClickListener(this);

		networkChange=(Button)findViewById(R.id.show_network_status_button);
		networkChange.setOnClickListener(this);
		intentFilter=new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		networkChangeRecevier=new NetworkChangeRecevier();
		registerReceiver(networkChangeRecevier,intentFilter);
		Log.i(TAG, "initViews() end");


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_rule:
			startActivity(new Intent(MaintainActivity.this,ScreenActivity.class));
			break;
			case R.id.show_network_status_button:
				Intent intent=null;
				if (Build.VERSION.SDK_INT>10){
					intent=new Intent(Settings.ACTION_SETTINGS);
				}else {
					intent=new Intent();
					intent.setClassName("com.android.settings","com.android.settings.WirelessSettings");
				}
				  startActivity(intent);

		}
	}

	/**
	 * json解析
	 */
	private List<MaintainItemType> parserList(String s) {
		Log.i(TAG, "parserList start");
		MaintainListParser ww = new MaintainListParser(s);
		ww.praser();
		Log.i(TAG, "parserList end");
		return ww.praserList();
	}

	/**
	 * 设置筛选规则
	 */
		public void setRule() {
		Log.i(TAG, "setRule() start");
		testMapToJSON text = new testMapToJSON();
		text.setTimeSelect(timeSelect);
		// 根据老师的要求设置对应groupSelect的变量
		text.setGroupSelect(groupSelect);
		Log.i(TAG, "setRule()  groupSelect= " + groupSelect);
		text.setMyResponseSelect(MyResponseSelect);
		text.setUnResponseJobSelect(UnResponseJobSelect);
		text.setCloseJobSelect(CloseJobSelect);
		text.setUnCloseJobSelect(UnCloseJobSelect);
		text.setJobOrder(jobOrder);
		preferencesHelper.setString("sex", timeSelect);
		preferencesHelper.setString("scope", groupSelect);
		preferencesHelper.setString("select", MyResponseSelect);
		preferencesHelper.setString("unselect", UnResponseJobSelect);
		preferencesHelper.setString("closeJobSelect", CloseJobSelect);
		preferencesHelper.setString("uncloseJobSelect", UnCloseJobSelect);
		preferencesHelper.setString("sort", jobOrder);
		Gson gson = new Gson();
		String gsons = gson.toJson(text);
		ruleList = gsons.toString();
		Log.i(TAG, "setRule() ruleList = " + ruleList);
		Log.i(TAG, "setRule() end");
	}

	/**
	 * 获取信息json数据
	 * 
	 * @param b
	 * @return
	 */
	private List<MaintainItemType> getListData() {
		Log.i(TAG, "getListData() start");
		List<MaintainItemType> blessingList;
		String sign = "";
		timestamp = ParamsManager.getTime();
		sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
				+ timestamp + Config.VER + account + name + ruleList + pageNo);
		IEasy ieasy = new IEasy(new IEasyHttpApiV1());
		String re = "";
		re = ieasy.getMainList(sign, Config.APPKEY, timestamp, Config.VER,
				account, name, ruleList, String.valueOf(pageNo));
		Log.i(TAG, "getListData() re = " + re);
		blessingList = parserList(re);
		Log.i(TAG, "getListData() end");
		return blessingList;
	}

	// TODO
	// 这里是加载维护单列表的主要实现代码
	// 现在出现的一个问题就是会出现一种情况就是维护单列表没有更多的数据显示
	/**
	 * 异步加载数据
	 * 
	 * @author luxun
	 * 
	 */
	private class getmtJobList extends AsyncTask<Void, Void, String> {
		List<MaintainItemType> list;

		@Override
		protected String doInBackground(Void... params) {
			setRule();
			list = getListData();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Log.i(TAG, "");
			message_refresh_progress.setVisibility(View.GONE);
			if (list != null) {
				if (list.size() == 0)
					Toast.makeText(getApplicationContext(), "暂无更新",
							Toast.LENGTH_SHORT).show();
				else {
					HashMap<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < list.size(); i++) {
						map = new HashMap<String, Object>();
						map.put("userId", list.get(i).getUserId());
						map.put("id", list.get(i).getId());
						map.put("name", list.get(i).getName());
						map.put("jobLevel", list.get(i).getJobLevel());
						map.put("dealResult", list.get(i).getDealResult());
						map.put("address", list.get(i).getAddress());
						map.put("accepteTime", list.get(i).getAccepteTime());
						map.put("trblStatus", list.get(i).getTrblStatus());
						mData.add(map);
					}

					footerMoreTxt.setText("更多");
					mMoreView.setClickable(true);
				}
				if (pageNo == 1) {
					mListView.onRefreshComplete();
				} else {
					mListView.setSelection(mListView.getSelectedItemPosition());
				}
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
			// View 控件进行创新之用 只能在UI线程中使用
			Log.i(TAG, "getmtJobList onPostExecute update the content of listview");
			mListView.invalidate();
			footerMoreProgress.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			message_refresh_progress.setVisibility(View.VISIBLE);
		}
	}

	class NetworkChangeRecevier extends BroadcastReceiver{
		@Override
		public void onReceive(Context context,Intent intent){
			ConnectivityManager connectivityManager=(ConnectivityManager)
					getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
			if (networkInfo!=null&&networkInfo.isAvailable()){
				if (networkInfo.isConnected()){
					networkChange.setVisibility(View.GONE);
				}else {
					networkChange.setVisibility(View.VISIBLE);
				}
			}else {
				networkChange.setVisibility(View.VISIBLE);
			}

		}
	}

	private void testSession(){
        new Thread(new Runnable() {
			@Override
			public void run() {
				List<MaintainItemType> blessingList;
				String sign = "";
				timestamp = ParamsManager.getTime();
				sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
						+ timestamp + Config.VER + account + name + ruleList + pageNo);
				IEasy ieasy = new IEasy(new IEasyHttpApiV1());
				String re = "";
				re = ieasy.getMainList(sign, Config.APPKEY, timestamp, Config.VER,
						account, name, ruleList, String.valueOf(pageNo));
				Log.i(TAG, "getListData() re = " + re);

				if (re.equals("noEffect")){
					Message message=new Message();
					message.what=SESSION_FAIL;
					handler.sendMessage(message);

				}else {
					blessingList = parserList(re);
					Message message=new Message();
					message.what=SESSION_OK;
					message.obj=blessingList;
					handler.sendMessage(message);


				}

			}
		}).start();
	}
	private void updateSession() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "update session start ");
				timestamp = ParamsManager.getTime();
				Log.i(TAG, "timestamp =  "+timestamp);
				String sign = "";
				//String password = pm.getStorePassword();
				String password =preferencesHelper.getString("store_password","");
				Log.i(TAG, "password getting from global variable  = "+pm.getStorePassword());
				Log.i(TAG, "password getting from Preferences = "+password);
				Log.i(TAG, "update session start ");
				Log.i(TAG, "acoount = "+account);
				password = ParamsManager.enCode(ParamsManager.getMd5sign(password));
				Log.i(TAG, "password after md5 = "+password);
				sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + password);
				IEasyHttpApiV1 httApi = new IEasyHttpApiV1();
				IEasy ieasy = new IEasy(httApi);
				String guestInfo;
				guestInfo = ieasy.invitationCodeLogin(Config.APPKEY, timestamp, sign, Config.VER, account, password);
				Log.i(TAG, "update session end "+"   "+guestInfo);
				if (guestInfo.equals("noEffect")) {
					Message m = new Message();
					m.what=UPDATE_SESSION_FAIL;
					handler.sendMessage(m);

				}else {
					Message m = new Message();
					m.what=UPDATE_SESSION_OK;
					handler.sendMessage(m);

				}

			}
		}).start();

	}
}
