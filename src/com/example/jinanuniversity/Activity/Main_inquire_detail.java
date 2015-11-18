package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jinanuniversity.R;
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
 * 历史维护明细
 * */
public class Main_inquire_detail extends Activity implements OnClickListener {

	private ProgressBar message_refresh_progress;
	private ListView mListView;
	private MainDetailedAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	PreferencesHelper preferencesHelper;
	MtJob blessingList;
	NetcUser user;
	private String timestamp, account;
	private int historyJobId;
	private String historyJob = "historyJob";
	private Button bt_built;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_detail);
		getDate();
		initView();
		new getHistoryJobList().execute();
	}

	private void initView() {
		message_refresh_progress = (ProgressBar) findViewById(R.id.message_refresh_progress);
		mListView = (ListView) findViewById(R.id.listView1);
		mAdapter = new MainDetailedAdapter(this, mData);
		mListView.setAdapter(mAdapter);

		bt_built = (Button) findViewById(R.id.bt_built);

		bt_built.setOnClickListener(this);

	}

	private void getDate() {
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		historyJobId = (getIntent() == null ? 0 : getIntent().getIntExtra("id",
				0));
		System.out.println("historyJobId==" + historyJobId);
	}

	/**
	 * 
	 * @param s
	 *            json解析
	 * @return
	 */
	private void parserList(String s) {
		MainDetailedParser ww = new MainDetailedParser(s);
		ww.praser();
		blessingList = ww.praserList(historyJob);
		user = ww.praserUserList();
	}

	/**
	 * 异步加载数据
	 * 
	 * @author luxun
	 * 
	 */
	private class getHistoryJobList extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			String sign = "";
			timestamp = ParamsManager.getTime();
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account + historyJobId);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = "";
			re = ieasy.getHistoryJob(sign, Config.APPKEY, timestamp,
					Config.VER, account, historyJobId + "");
			System.out.println("re==" + re);
			parserList(re);
			return re;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(String result) {
			message_refresh_progress.setVisibility(View.GONE);
			// dismissProgressDialog();
			if (blessingList != null) {
				if (result.equals("noEffect")) {
					Toast.makeText(getApplicationContext(), "暂无更新",
							Toast.LENGTH_SHORT).show();
				} else {
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
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_built:
			finish();
			break;

		default:
			break;
		}
	}
}
