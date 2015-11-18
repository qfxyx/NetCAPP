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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.adapter.UserDetailAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MainDetailedParser;
import com.example.jinanuniversity.types.NetcUser;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

public class UserDetailActivity extends Activity implements OnClickListener {

	private static final String TAG = "Activity.UserDetailActivity";

	private Button btn_back, sam3_message, synchronous_sam3, mJournal,
			user_operate, net_detailed, kick_user_offline;
	private ProgressBar refresh_progress;
	private ProgressDialog mProgressDialog;
	private ParamsManager pm;
	private PreferencesHelper preferencesHelper;
	private Config config = new Config();
	private String account, userId, text = "获取信息成功 !", info, mac;
	NetcUser user;
	private ListView mListView;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private UserDetailAdapter mAdapter;
	private int code = 1;
	public boolean isRefresh = false;

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume start");
		super.onResume();
		pm = (ParamsManager) getApplication();
		isRefresh = pm.isRefresh();
		if (isRefresh) {
			isRefresh = false;
			mData.clear();
			pm.setRefresh(isRefresh);
			new getInfo().execute(config.USERDETAIL);
		}
		Log.i(TAG, "onResume end");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_detail);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		userId = getIntent().getStringExtra("userId") == null ? ""
				: getIntent().getStringExtra("userId");

		initView();
		new getInfo().execute(config.USERDETAIL);
		Log.i(TAG, "onCreate end");
	}

	private void initView() {
		refresh_progress = (ProgressBar) findViewById(R.id.refresh_progress);
		mListView = (ListView) findViewById(R.id.listView1);
		mAdapter = new UserDetailAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		btn_back = (Button) findViewById(R.id.btn_back);
		user_operate = (Button) findViewById(R.id.user_operate);
		sam3_message = (Button) findViewById(R.id.sam3_message);
		synchronous_sam3 = (Button) findViewById(R.id.synchronous_sam3);
		mJournal = (Button) findViewById(R.id.journal);
		net_detailed = (Button) findViewById(R.id.net_detailed);
		kick_user_offline = (Button) findViewById(R.id.kick_user_offline);
		btn_back.setOnClickListener(this);
		user_operate.setOnClickListener(this);
		sam3_message.setOnClickListener(this);
		synchronous_sam3.setOnClickListener(this);
		mJournal.setOnClickListener(this);
		net_detailed.setOnClickListener(this);
		kick_user_offline.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_back:
			this.finish();
			break;

		case R.id.user_operate:
			intent.setClass(UserDetailActivity.this, UserOperate.class);
			intent.putExtra("userId", userId);
			intent.putExtra("mac", mac);
			startActivity(intent);
			break;

		case R.id.sam3_message:
			intent.setClass(UserDetailActivity.this, Sam3Activity.class);
			intent.putExtra("userId", userId);
			startActivity(intent);
			break;

		case R.id.synchronous_sam3:
			new sam3_synchronous().execute();
			break;

		case R.id.journal:
			intent.setClass(UserDetailActivity.this, DailyRecordActivity.class);
			intent.putExtra("userId", userId);
			startActivity(intent);
			break;
		case R.id.net_detailed:
			intent.setClass(UserDetailActivity.this, NetworkDetail.class);
			intent.putExtra("userId", userId);
			startActivity(intent);
			break;

		case R.id.kick_user_offline:
			new KickUserOffline().execute();
			break;
		default:
			break;
		}
	}

	/**
	 * json解析
	 */
	private void parserList(String s, String type) {
		MainDetailedParser ww = new MainDetailedParser(s);
		ww.praser();
		code = ww.getCode();
		info = ww.getInfo();
		if (type.equals("getInfo")) {
			user = ww.praserUserList();
		}
		// TODO ACCORDING TO THE TYPE
		else {

		}
	}

	/** 加载列表数据 */
	private class getInfo extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String re = getJsonBack(params[0]);
			Log.i(TAG, " re = " + re);
			parserList(re, "getInfo");
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			refresh_progress.setVisibility(View.INVISIBLE);
			if (code == 0) {
				if (result.equals("noEffect")) {
					Toast.makeText(getApplicationContext(), "暂无更新",
							Toast.LENGTH_SHORT).show();
				} else {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map = new HashMap<String, Object>();
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
					mac = user.getMac();
					Toast.makeText(getApplicationContext(), text,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), info,
						Toast.LENGTH_SHORT).show();
			}
			mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			refresh_progress.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 同步sam3
	 */
	private class sam3_synchronous extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			// 这里之前是使用了一个错误的连接
			String re = getJsonBack(config.SAM3SYN);
			parserList(re, "sam3_synchronous");
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			if (code == 0) {
				Toast.makeText(UserDetailActivity.this, "恭喜你同步成功！",
						Toast.LENGTH_SHORT).show();
				UserDetailActivity.this.finish();
			} else {
				Toast.makeText(UserDetailActivity.this, "同步失败！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog("同步中，请稍候...");
		}
	}

	/**
	 * 将指定用户踢下线
	 * **/
	private class KickUserOffline extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// 将getJsonBack的参数换成踢下线的链接
			String re = getJsonBack(config.SAM3KICK);
			Log.i(TAG, "KickUserOffline re = "+re);
			parserList(re, "KickUserOffline");
			Log.i(TAG, "KickUserOffline re = "+re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			if (code == 0) {
				Toast.makeText(UserDetailActivity.this, "踢下线操作成功！",
						Toast.LENGTH_SHORT).show();
				UserDetailActivity.this.finish();
			} else {
				Toast.makeText(UserDetailActivity.this, "踢下线操作失败！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog("正在进行踢下线操作，请稍候...");
		}

	}

	private String getJsonBack(String url) {
		String timestamp = ParamsManager.getTime();
		String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + userId);
		IEasy ieasy = new IEasy(new IEasyHttpApiV1());
		Log.i(TAG, "account = " + account);
		Log.i(TAG, "userId = " + userId);
		return ieasy.getUserDetail(url, sign, timestamp, account, userId);
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// String url = data.getStringExtra("url") == null ? "" : getIntent()
	// .getStringExtra("url");
	// text = data.getStringExtra("text") == null ? "" : getIntent()
	// .getStringExtra("text");
	// switch (requestCode) {
	// case 1:
	// mData.clear();
	// new getInfo().execute(url);
	// break;
	//
	// default:
	// break;
	// }
	// }

	private ProgressDialog showProgressDialog(String message) {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(message);
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
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop");
		super.onStop();
	}

}
