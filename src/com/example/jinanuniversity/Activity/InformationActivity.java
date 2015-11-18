package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.jinanuniversity.adapter.ImformationAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.InformationListParser;
import com.example.jinanuniversity.types.MtMsgType;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.example.jinanuniversity.view.PullToRefreshListView;
import com.example.jinanuniversity.view.PullToRefreshListView.OnRefreshListener;

public class InformationActivity extends Activity implements OnClickListener {
	private static final String TAG = "Activity.InformationActivity";
	private PullToRefreshListView mListView;
	private ImformationAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private LayoutInflater mInflater;
	private LinearLayout mMoreView;
	private TextView footerMoreTxt;
	private ProgressBar footerMoreProgress;
	private ProgressBar message_refresh_progress;
	PreferencesHelper preferencesHelper;
	private String account, timestamp;
	private Button bt_built;
	int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.information);
		initViews();
		new getMessage().execute();
		Log.i(TAG, "onCreate end");
	}
	
	@Override
	protected void onResume() {
		Log.i(TAG, "onResume start");
		super.onResume();
		Log.i(TAG, "onResume end");
	}

	/**
	 * 实例化对象
	 */
	private void initViews() {
		bt_built = (Button) findViewById(R.id.bt_built);
		bt_built.setOnClickListener(this);

		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		message_refresh_progress = (ProgressBar) findViewById(R.id.message_refresh_progress);
		mListView = (PullToRefreshListView) findViewById(R.id.listView1);
		mAdapter = new ImformationAdapter(this, mData);
		mListView.setAdapter(mAdapter);

		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				mData.clear();
				new getMessage().execute();// 获取新列表
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
				page++;
				new getMessage().execute();
			}
		});
		mListView.addFooterView(mMoreView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_built:
			startActivity(new Intent(InformationActivity.this, NewMessage.class));
			break;

		default:
			break;
		}
	}

	/**
	 * json解析
	 */
	private List<MtMsgType> parserList(String s) {
		InformationListParser ww = new InformationListParser(s);
		ww.praser();
		return ww.praserList();
	}

	/**
	 * 异步加载数据
	 */
	private class getMessage extends AsyncTask<Void, Void, String> {

		List<MtMsgType> list;

		@Override
		protected String doInBackground(Void... params) {
			timestamp = ParamsManager.getTime();
			String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + page);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = "";
			re = ieasy.getMessage(Config.APPKEY, sign, timestamp, Config.VER, account, String.valueOf(page));
			list = parserList(re);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			message_refresh_progress.setVisibility(View.GONE);
			if (list != null) {
				if (list.size() == 0) {
					Toast.makeText(getApplicationContext(), "暂无更新", Toast.LENGTH_SHORT).show();
					footerMoreTxt.setText("暂无更新");
				} else {
					HashMap<String, Object> map = new HashMap<String, Object>();
					try {
						for (int i = 0; i < list.size(); i++) {
							map = new HashMap<String, Object>();
							map.put("sender", list.get(i).getSender());
							map.put("title", list.get(i).getTitle());
							map.put("message", list.get(i).getMessage());
							map.put("time", list.get(i).getTime());
							map.put("flag", list.get(i).getFlag());
							map.put("msgId", list.get(i).getId() + "");
							mData.add(map);
						}
					} catch (Exception e) {
						System.out.println("信息出错!");
					}
					footerMoreTxt.setText("更多");
					mMoreView.setClickable(true);
				}
				if (page == 1) {
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
}
