package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.jinanuniversity.adapter.MaintainInquireAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MaintenanceListParser;
import com.example.jinanuniversity.json.testMapToJSON;
import com.example.jinanuniversity.types.MaintenanceItemType;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.example.jinanuniversity.view.PullToRefreshListView;
import com.example.jinanuniversity.view.PullToRefreshListView.OnRefreshListener;
import com.google.gson.Gson;

/**
 * 历史维护列表
 * */
public class Main_inquire_list extends Activity {

	private MaintainInquireAdapter mAdapter;
	private PullToRefreshListView mListView;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

	private String timestamp, account, ruleList, name;
	PreferencesHelper preferencesHelper;
	private ProgressBar message_refresh_progress;
	private boolean isGetNewest = true;
	private String rangeSelect, jobIdSelect, jobId, userIdSelect, userId,
			userNameSelect, userName, accepteTimeSelect, accepteTimeStart,
			accepteTimeEnd, closeTimeSelect, closeTimeStart, closeTimeEnd,
			jobOrder;
	int pageNo = 1;
	private LayoutInflater mInflater;
	private LinearLayout mMoreView;
	private TextView footerMoreTxt;
	private ProgressBar footerMoreProgress;
	private Button btn_back;
	ParamsManager paramsManager;
	private boolean updateSession=false;
	private boolean test=true;
	private final int UPDATE_OK=1;
	private final int UPDATE_FAILED=2;
	private final int UPDATE_SESSION=3;
	private final String TAG=".Activity.Main_inquire_detail";
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
					Toast.makeText(Main_inquire_list.this,"获取历史维护单列表失败，" +
							"请检查网络情况或重新登录",Toast.LENGTH_SHORT).show();
					break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.inquire_list);
		paramsManager=(ParamsManager)getApplication();
		getDate();
		// initData();
		test();
		initView();
		new getmtJobList().execute();
	}

	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Main_inquire_list.this.finish();
			}
		});
		message_refresh_progress = (ProgressBar) findViewById(R.id.message_refresh_progress);
		mListView = (PullToRefreshListView) findViewById(R.id.listView1);
		mAdapter = new MaintainInquireAdapter(this, mData);
		mListView.setAdapter(mAdapter);

		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				pageNo = 1;
				mData.clear();
				new getmtJobList().execute();// 获取新列表
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
				new getmtJobList().execute();
			}
		});
		mListView.addFooterView(mMoreView);
	}

	private void getDate() {
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		name = preferencesHelper.getString("name", "");
		rangeSelect = preferencesHelper.getString("rangeSelect", "my");

		//jobIdSelect = preferencesHelper.getString("jobIdSelect", "no");
		jobIdSelect=(getIntent() == null ? "" : getIntent().getStringExtra("jobIdSelect"));
		//userIdSelect = preferencesHelper.getString("userIdSelect", "no");
		userIdSelect=(getIntent() == null ? "" : getIntent().getStringExtra("userIdSelect"));
		//userNameSelect = preferencesHelper.getString("userNameSelect", "no");
		userNameSelect=(getIntent() == null ? "" : getIntent().getStringExtra("userNameSelect"));

		accepteTimeSelect = preferencesHelper.getString("accepteTimeSelect",
				"no");
		closeTimeSelect = preferencesHelper.getString("closeTimeSelect", "no");
		jobOrder = preferencesHelper.getString("jobOrder", "id");

		jobId = (getIntent() == null ? "" : getIntent().getStringExtra("jobId"));
		userId = (getIntent() == null ? "" : getIntent().getStringExtra(
				"userId"));
		userName = (getIntent() == null ? "" : getIntent().getStringExtra(
				"userName"));
		accepteTimeStart = (getIntent() == null ? "" : getIntent()
				.getStringExtra("accepteTimeStart"));
		accepteTimeEnd = (getIntent() == null ? "" : getIntent()
				.getStringExtra("accepteTimeEnd"));
		closeTimeStart = (getIntent() == null ? "" : getIntent()
				.getStringExtra("closeTimeStart"));
		closeTimeEnd = (getIntent() == null ? "" : getIntent().getStringExtra(
				"closeTimeEnd"));
	}

	/**
	 * 
	 * @param s
	 *            json解析
	 * @return
	 */
	private List<MaintenanceItemType> parserList(String s) {
		MaintenanceListParser ww = new MaintenanceListParser(s);
		ww.praser();
		return ww.praserList();
	}

	/**
	 * 把String类型转成json类型
	 */
	public void test() {
		testMapToJSON text = new testMapToJSON();
		text.setRangeSelect(rangeSelect);
		text.setJobIdSelect(jobIdSelect);
		text.setJobId(jobId);
		text.setUserIdSelect(userIdSelect);
		text.setUserId(userId);
		text.setUserNameSelect(userNameSelect);
		text.setUserName(userName);
		text.setAccepteTimeSelect(accepteTimeSelect);
		text.setAccepteTimeStart(accepteTimeStart);
		text.setAccepteTimeEnd(accepteTimeEnd);
		text.setCloseTimeSelect(closeTimeSelect);
		text.setCloseTimeStart(closeTimeStart);
		text.setCloseTimeEnd(closeTimeEnd);
		text.setJobOrder(jobOrder);

		Gson gson = new Gson();
		String gsons = gson.toJson(text);
		ruleList = gsons.toString();
		System.out.println("ruleList==" + ruleList);
	}

	/**
	 * 获取信息json数据
	 * 
	 * @param b
	 * @return
	 */
	private List<MaintenanceItemType> getListData(boolean b) {
		List<MaintenanceItemType> blessingList;
		String sign = "";
		timestamp = ParamsManager.getTime();
		sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
				+ timestamp + Config.VER + account + name + ruleList + pageNo);
		IEasy ieasy = new IEasy(new IEasyHttpApiV1());
		String re = "";
		re = ieasy.getMain_inquireList(sign, Config.APPKEY, timestamp,
				Config.VER, account, name, ruleList, String.valueOf(pageNo));

		blessingList = parserList(re);

		return blessingList;
	}

	/**
	 * 异步加载数据
	 * 
	 * @author luxun
	 * 
	 */
	private class getmtJobList extends AsyncTask<Void, Void, String> {

		List<MaintenanceItemType> list;

		@Override
		protected String doInBackground(Void... params) {
			System.out.println("getBlessList " + isGetNewest);
			list = getListData(isGetNewest);
			return null;
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			// dismissProgressDialog();
		}

		@Override
		protected void onPostExecute(String result) {
			message_refresh_progress.setVisibility(View.GONE);
			// dismissProgressDialog();
			if (list != null) {
				if (list.size() == 0){
					if (!updateSession){
						Message message=new Message();
						message.what=UPDATE_SESSION;
						handler.sendMessage(message);
					}else {
						Toast.makeText(getApplicationContext(), "暂无更新",
								Toast.LENGTH_SHORT).show();
					}

				}
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
						map.put("dealer1", list.get(i).getDealer1());
						map.put("dealTime1", list.get(i).getDealTime1());
						map.put("dealer2", list.get(i).getDealer2());
						map.put("dealTime2", list.get(i).getDealTime2());
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
			mListView.invalidate();
			footerMoreProgress.setVisibility(View.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			footerMoreTxt.setText("更新中");
			message_refresh_progress.setVisibility(View.VISIBLE);
			mMoreView.setClickable(false);
		}
	}
	private void UpdateSession(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("update session start ");
				timestamp = ParamsManager.getTime();
				String sign = "";
				String password = preferencesHelper.getString("store_password", "");
				password = ParamsManager.enCode(ParamsManager.getMd5sign(password));
				sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + password);
				IEasyHttpApiV1 httApi = new IEasyHttpApiV1();
				IEasy ieasy = new IEasy(httApi);
				String guestInfo;
				guestInfo = ieasy.invitationCodeLogin(Config.APPKEY, timestamp, sign, Config.VER, account, password);
				System.out.println("update session end ");
				updateSession=true;
				test=false;
				if (guestInfo.equals("noEffect")){
					Message message = new Message();
					message.what=UPDATE_FAILED;
					handler.sendMessage(message);
				}else {
					Message message = new Message();
					message.what=UPDATE_OK;
					handler.sendMessage(message);
					System.out.println("update session succeed ");
				}
			}
		}).start();
	}

}
