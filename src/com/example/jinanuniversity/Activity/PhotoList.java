package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.jinanuniversity.adapter.PhotoListAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MtPicListParser;
import com.example.jinanuniversity.types.MtPicType;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.example.jinanuniversity.view.PullToRefreshListView;
import com.example.jinanuniversity.view.PullToRefreshListView.OnRefreshListener;

public class PhotoList extends Activity {

	private PullToRefreshListView mListView;
	private PhotoListAdapter mAdapter;
	private ProgressBar refresh_progress, footerMoreProgress;
	private Button btn_back;
	private LayoutInflater mInflater;
	private LinearLayout mMoreView;
	private TextView footerMoreTxt;

	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private String account, building;
	private PreferencesHelper preferencesHelper;
	private int pageNo = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photolist);

		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		building = getIntent().getStringExtra("building") == null ? ""
				: getIntent().getStringExtra("building");

		initView();
		new getInfo().execute();
	}

	private void initView() {

		mListView = (PullToRefreshListView) findViewById(R.id.photo_list);
		mAdapter = new PhotoListAdapter(this, mData);
		mListView.setAdapter(mAdapter);
		refresh_progress = (ProgressBar) findViewById(R.id.refresh_progress);
		btn_back = (Button) findViewById(R.id.btn_back);
		
		btn_back.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		mListView.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				pageNo = 1;
				mData.clear();
				new getInfo().execute();
			}
		});

		mInflater = getLayoutInflater();
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
				footerMoreProgress.setVisibility(View.VISIBLE);
				new getInfo().execute();
			}
		});
		mListView.addFooterView(mMoreView);
	}

	public class getInfo extends AsyncTask<Void, Void, String> {

		List<MtPicType> list;

		@Override
		protected String doInBackground(Void... params) {
			String timestamp = ParamsManager.getTime();
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account
					+ building + pageNo);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = ieasy.getPicList(sign, timestamp, account, building,
					String.valueOf(pageNo));

			System.out.println("re = " + re);
			list = parserList(re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			refresh_progress.setVisibility(View.GONE);
			if (list != null) {
				if (list.size() == 0) {
					Toast.makeText(getApplicationContext(), "暂无更新",
							Toast.LENGTH_SHORT).show();
					footerMoreTxt.setText("暂无更新");
				} else {
					initData(list);
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
			refresh_progress.setVisibility(View.VISIBLE);
			footerMoreTxt.setText("更新中");
			mMoreView.setClickable(false);
		}
	}

	/**
	 * json解析
	 */
	private List<MtPicType> parserList(String s) {
		MtPicListParser ww = new MtPicListParser(s);
		return ww.praserList();
	}

	private void initData(List<MtPicType> list) {
		HashMap<String, Object> map;
		try {
			for (int i = 0; i < list.size(); i++) {
				map = new HashMap<String, Object>();
				map.put("picId", list.get(i).getId() + "");
				map.put("name", list.get(i).getPicname());
				map.put("pic", list.get(i).getSamllurl());
				mData.add(map);
			}
		} catch (Exception e) {
			System.out.println("PhotoList is wrong !");
		}
	}
}
