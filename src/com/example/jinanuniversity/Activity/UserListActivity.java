package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.jinanuniversity.adapter.UserListAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.UserListParser;
import com.example.jinanuniversity.types.NetcUser;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.example.jinanuniversity.view.PullToRefreshListView;
import com.example.jinanuniversity.view.PullToRefreshListView.OnRefreshListener;

/** 用户列表 */
public class UserListActivity extends Activity {

	private static final String TAG = "Activity.UserListActivity";

	private PullToRefreshListView mListView;
	private UserListAdapter mAdapter;
	private LayoutInflater mInflater;
	private LinearLayout mMoreView;
	private TextView footerMoreTxt;
	private ProgressBar footerMoreProgress;
	private Button bt_back;

	private PreferencesHelper preferencesHelper;
	private ProgressDialog mProgressDialog;
	private String account, pwd, ruleList,pwd_no_md5;
	private int code = 1, pageNo = 1;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_list);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		pwd_no_md5 = getIntent().getStringExtra("pwd") == null ? "" : getIntent().getStringExtra("pwd");
		ruleList = getIntent().getStringExtra("ruleListJson") == null ? "": getIntent().getStringExtra("ruleListJson");
		initView();
		new getInfo().execute();
		Log.i(TAG, "onCreate end");
	}

	private void initView() {
		Log.i(TAG, "initView start");
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mListView = (PullToRefreshListView) findViewById(R.id.user_list);
		mAdapter = new UserListAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				pageNo = 1;
				mData.clear();
				new getInfo().execute(); // 获取新列表
			}
		});
		mInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMoreView = (LinearLayout) mInflater.inflate(R.layout.pull_to_refresh_footer, null);
		footerMoreProgress = (ProgressBar) mMoreView.findViewById(R.id.refresh_footer_more_progress);
		footerMoreProgress.setVisibility(View.INVISIBLE);
		footerMoreTxt = (TextView) mMoreView.findViewById(R.id.refresh_footer_more_txt);
		mMoreView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pageNo++;
				new getInfo().execute();
			}
		});
		mListView.addFooterView(mMoreView);
		Log.i(TAG, "initView end");
	}

	/** 加载列表数据 */
	private class getInfo extends AsyncTask<String, Void, Integer> {
		List<NetcUser> list;

		@Override
		protected Integer doInBackground(String... params) {
			Log.i(TAG, "getInfo doInBackground start");
			String timestamp = ParamsManager.getTime();
			pwd = ParamsManager.getMd5sign(ParamsManager.enCode(pwd_no_md5));
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account
					+ ruleList + pageNo + pwd);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = ieasy.getUserList(sign, timestamp, account, ruleList,
					String.valueOf(pageNo), pwd);
			Log.i(TAG, "sign==" + sign);
			Log.i(TAG, "timestamp==" + timestamp);
			Log.i(TAG, "account==" + account);
			Log.i(TAG, "ruleList==" + ruleList);
			Log.i(TAG, "pwd==" + pwd);
			if (!re.equals("noEffect")) {
				list = getListFromJosn(re);
				Log.i(TAG, "getInfo doInBackground list = " + list.toString());
				return code;
			}
			Log.i(TAG, "getInfo doInBackground end");
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dismissProgressDialog();
			footerMoreProgress.setVisibility(View.INVISIBLE);
			if (result == 0) {
				if (list != null) {
					initData(list);
					footerMoreTxt.setText("更多");
					mMoreView.setClickable(true);
				} else {
					Toast.makeText(getApplicationContext(), "暂无更新",
							Toast.LENGTH_SHORT).show();
				}
				if (pageNo == 1) {
					mListView.onRefreshComplete();
				} else {
					mListView.setSelection(mListView.getSelectedItemPosition());
				}
			} else {
				mListView.onRefreshComplete();
				footerMoreTxt.setText("更新失败");
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
			mAdapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
			footerMoreProgress.setVisibility(View.VISIBLE);
			footerMoreTxt.setText("更新中");
			mMoreView.setClickable(false);
		}
	}

	public List<NetcUser> getListFromJosn(String json) {
		Log.i(TAG, "getListFromJosn start");
		UserListParser parser = new UserListParser(json);
		code = parser.getCode();
		Log.i(TAG, "code = " + code);
		Log.i(TAG, "getListFromJosn end");
		return parser.praserList();
	}

	private void initData(List<NetcUser> list) {
		HashMap<String, Object> map;
		try {
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("building", list.get(i).getBuilding());
				map.put("ip", list.get(i).getIp());
				map.put("userName", list.get(i).getUserName());
				map.put("usrFlag", list.get(i).getUsrFlag());
				map.put("userId", list.get(i).getUserId());
				mData.add(map);
			}
		} catch (Exception e) {
			System.out.println("获取用户列表出错!");
		}
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
}
