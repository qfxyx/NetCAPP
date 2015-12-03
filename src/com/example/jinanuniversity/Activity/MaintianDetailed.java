package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.R.color;
import com.example.jinanuniversity.adapter.MainDetailedAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MainDetailedParser;
import com.example.jinanuniversity.types.MtJob;
import com.example.jinanuniversity.types.NetcUser;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

/**
 * 维护列表详情
 */
public class MaintianDetailed extends Activity implements OnClickListener {

	private static final String TAG = "Activity.MaintianDetailed";

	private Button bt_xiaodan, bt_List, bt_respond, delayed;
	private String timestamp, account;
	private int jobId;
	private ProgressBar message_refresh_progress;
	private ListView mListView;
	private MainDetailedAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	PreferencesHelper preferencesHelper;
	private ProgressDialog mProgressDialog;
	MtJob blessingList;
	NetcUser user;
	private String dealResult;
	private Boolean xiangying = false;
	private Button bt_tell;
	private String mtJob = "mtJob";
	private boolean updateSession=false;
	private boolean test=true;
	private final int UPDATE_OK=1;
	private final int UPDATE_FAILED=2;
	private final int UPDATE_SESSION=3;
	ParamsManager paramsManager;

	private Handler handler = new Handler(){
		@Override
	public void handleMessage(Message message){
			switch (message.what){

				case UPDATE_SESSION:
					UpdateSession();
					break;

				case UPDATE_OK:
					new getmtJobList().execute();
					break;

				case UPDATE_FAILED:
					Toast.makeText(MaintianDetailed.this,"获取维护单明细失败，" +
							"请检查网络情况或重新登录",Toast.LENGTH_SHORT).show();
					break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.maintian_detailed);
		paramsManager=(ParamsManager)getApplication();
		getDate();
		initView();
		new getmtJobList().execute();
		Log.i(TAG, "onCreate end");
	}

	private void getDate() {
		Log.i(TAG, "getDate start");
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		jobId = (getIntent() == null ? 0 : getIntent().getIntExtra("id", 0));
		preferencesHelper.setString("jobId", this.jobId + "");
		Log.i(TAG, "getDate end");
	}

	private void initView() {
		message_refresh_progress = (ProgressBar) findViewById(R.id.message_refresh_progress);
		mListView = (ListView) findViewById(R.id.listView1);
		mAdapter = new MainDetailedAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		bt_xiaodan = (Button) findViewById(R.id.bt_xiaodan);
		bt_List = (Button) findViewById(R.id.bt_list);
		bt_respond = (Button) findViewById(R.id.bt_respond);
		delayed = (Button) findViewById(R.id.delayed);
		bt_tell = (Button) findViewById(R.id.bt_tell);
		bt_xiaodan.setOnClickListener(this);
		bt_List.setOnClickListener(this);
		bt_respond.setOnClickListener(this);
		delayed.setOnClickListener(this);
		bt_tell.setOnClickListener(this);


	}

	private void decide() {
		if (dealResult.equals("未处理")) {

		} else {
			bt_respond.setBackgroundColor(color.yello);
		}

		if (dealResult.equals("未处理") || dealResult.equals("申请消单")
				|| dealResult.equals("已处理")) {
			delayed.setBackgroundColor(color.yello);
		} else {

		}

		if (dealResult.equals("申请消单") || dealResult.equals("已处理")) {
			bt_xiaodan.setBackgroundColor(color.yello);
		}
	}

	/**
	 * json解析
	 */
	private void parserList(String s) {
		MainDetailedParser ww = new MainDetailedParser(s);
		ww.praser();
		blessingList = ww.praserList(mtJob);
		user = ww.praserUserList();
	}

	/**
	 * 异步加载数据
	 */
	private class getmtJobList extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String sign = "";
			timestamp = ParamsManager.getTime();
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account + jobId);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = "";
			re = ieasy.getMtJob(sign, Config.APPKEY, timestamp, Config.VER,
					account, jobId + "");
			parserList(re);
			Log.i(TAG, "getmtJobList re = " + re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			message_refresh_progress.setVisibility(View.GONE);
			if (blessingList != null) {
			   if (result.equals("noEffect")) {
					Log.i(TAG, "session may have been failed " );
					if (!updateSession){
						Message message=new Message();
						message.what=UPDATE_SESSION;
						handler.sendMessage(message);
					}else {
						Toast.makeText(getApplicationContext(), "暂无更新",
								Toast.LENGTH_SHORT).show();
					}




				} else {
					dealResult = blessingList.getDealResult();
					decide();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map = new HashMap<String, Object>();
					map.put("Id", blessingList.getId());
					map.put("userId", blessingList.getUserId());
					map.put("name", blessingList.getName());
					map.put("jobLevel", blessingList.getJobLevel());
					map.put("trblStatus", blessingList.getTrblStatus());
					map.put("phone", blessingList.getPhone());
					map.put("address", blessingList.getAddress());
					map.put("area", blessingList.getArea());
					map.put("accepter", blessingList.getAccepter());
					map.put("accepteTime", blessingList.getAccepteTime());
					map.put("groupAdmin", blessingList.getGroupAdmin());
					map.put("groupName", blessingList.getGroupName());
					map.put("responser", blessingList.getResponser());
					map.put("responseTime", blessingList.getResponseTime());
					map.put("dealer1", blessingList.getDealer1());
					map.put("dealTime1", blessingList.getDealTime1());
					map.put("dealResult1", blessingList.getDealResult1());
					map.put("dealer2", blessingList.getDealer2());
					map.put("dealTime2", blessingList.getDealTime2());
					map.put("dealResult", blessingList.getDealResult());
					map.put("trblCource", blessingList.getTrblCource());
					map.put("dealMethod", blessingList.getDealMethod());
					map.put("userId", user.getUserId());
					map.put("userName", user.getUserName());
					map.put("level", user.getLevel());
					map.put("usrFlag", user.getUsrFlag());
					map.put("building", user.getBuilding());
					map.put("roomNo", user.getRoomNo());
					map.put("userphone", user.getPhone());
					map.put("officeName", user.getOfficeName());
					map.put("email", user.getEmail());
					map.put("servIp", user.isServIp());
					map.put("servAbroad", user.isServAbroad());
					map.put("openDate", user.getOpenDate());
					map.put("fromDate", user.getFromDate());
					map.put("toDate", user.getToDate());
					map.put("activeFlag", user.isActiveFlag());
					map.put("mac", user.getMac());
					map.put("ip", user.getIp());
					map.put("gateway", user.getGateway());
					map.put("mask", user.getMask());
					map.put("sam3Tempelate", user.getSam3Tempelate());
					map.put("memo", user.getMemo());
					mData.add(map);
				}
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			message_refresh_progress.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_xiaodan:
			if (dealResult != null) {
				if (dealResult.equals("申请消单") || dealResult.equals("已处理")) {
					Toast.makeText(getApplicationContext(), "此单不能消单",
							Toast.LENGTH_SHORT).show();
				} else {
					startActivity(new Intent(MaintianDetailed.this,
							Maintain_xiaodan.class));
				}
			}
			break;

		case R.id.bt_list:
			finish();
			break;

		case R.id.bt_respond:
			xiangying = true;
			preferencesHelper.setBoolean("xiangying", xiangying);
			paramsManager.setRefresh(true);
			if (dealResult != null) {
				if (dealResult.equals("未处理")) {
					new response().execute();
					bt_respond.setBackgroundColor(color.yello);
				} else {
					Toast.makeText(getApplicationContext(), "已响应",
							Toast.LENGTH_SHORT).show();
				}
			}
			break;

		case R.id.delayed:
			if (dealResult != null) {
				if (dealResult.equals("未处理") || dealResult.equals("申请消单")
						|| dealResult.equals("已处理")) {
					Toast.makeText(getApplicationContext(), "此单不能延时",
							Toast.LENGTH_SHORT).show();
				} else {
					paramsManager.setRefresh(true);
					startActivity(new Intent(MaintianDetailed.this,
							DelayedActivity.class));
				}
			}
			break;

		case R.id.bt_tell:
			List<String> phoneNums = new ArrayList<String>();
			phoneNums.add(blessingList.getPhone());
			String phoneInTrblStatus = getPhoneNumFromTrblStatus(blessingList
					.getTrblStatus());
			if (null != phoneInTrblStatus) {
				phoneNums.add(phoneInTrblStatus);
			}
			String[] phones = new String[phoneNums.size()];
			phoneNums.toArray(phones);
			Intent intent = new Intent(MaintianDetailed.this,
					DialActivity.class);
			intent.putExtra("phoneNumsArray", phones);
			this.startActivity(intent);
			break;

		}
	}

	// get the phone number in trblstatus
	private String getPhoneNumFromTrblStatus(String trblStatus) {
		int startIndexOfNum, endIndexOfNum;
		Log.i(TAG, "trblStatus length = " + trblStatus.length());
		for (startIndexOfNum = 0; startIndexOfNum < trblStatus.length(); startIndexOfNum++) {
			if (trblStatus.charAt(startIndexOfNum) > '0'
					&& trblStatus.charAt(startIndexOfNum) < '9') {
				break;
			}
		}
		Log.i(TAG, "startIndexOfNum = " + startIndexOfNum);
		for (endIndexOfNum = trblStatus.length() - 1; endIndexOfNum > 0; endIndexOfNum--) {
			if (trblStatus.charAt(endIndexOfNum) > '0'
					&& trblStatus.charAt(endIndexOfNum) < '9') {
				break;
			}
		}
		Log.i(TAG, "endIndexOfNum = " + endIndexOfNum);
		if (startIndexOfNum < endIndexOfNum) {
			return trblStatus.substring(startIndexOfNum, endIndexOfNum + 1);
		} else {
			return null;
		}

	}

	private class response extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			String sign = "";
			timestamp = ParamsManager.getTime();
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account + jobId);
			Log.i(TAG, "response doInBackground sign =" + sign);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String resutl = ieasy.response(sign, Config.APPKEY, timestamp,
					Config.VER, account, jobId + "");
			Log.i(TAG, "response doInBackground resutl =" + resutl);
			if (resutl.equals("noEffect")) {
				Log.i(TAG, "response doInBackground 响应失败 !");
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if (result) {
				new getmtJobList().execute();
				delayed.setBackgroundResource(R.color.blue);
				Toast.makeText(MaintianDetailed.this, "恭喜你响应成功！",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MaintianDetailed.this, "响应失败！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}
	}

	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("响应中，请稍候...");
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
			e.printStackTrace();
		}
	}

	//更新Session
	private void UpdateSession(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "update session start ");
				timestamp = ParamsManager.getTime();
				String sign = "";
				String password = preferencesHelper.getString("store_password","");
				Log.i(TAG, "password getting from Preferences = "+password);
				password = ParamsManager.enCode(ParamsManager.getMd5sign(password));
				sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + password);
				IEasyHttpApiV1 httApi = new IEasyHttpApiV1();
				IEasy ieasy = new IEasy(httApi);
				String guestInfo;
				guestInfo = ieasy.invitationCodeLogin(Config.APPKEY, timestamp, sign, Config.VER, account, password);
				Log.i(TAG, "update session end ");
				updateSession=true;
				if (guestInfo.equals("noEffect")){
					Message message = new Message();
					message.what=UPDATE_FAILED;
					handler.sendMessage(message);
				}else {
					Message message = new Message();
					message.what=UPDATE_OK;
					handler.sendMessage(message);
				}
			}
		}).start();
	}



}
