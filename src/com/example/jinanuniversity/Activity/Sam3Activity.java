package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.adapter.Sam3Adapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.sam3ListParse;
import com.example.jinanuniversity.types.sam3ItemType;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

public class Sam3Activity extends Activity {
	private Button mBack;
	private ProgressBar refresh_progress;
	private String account, userId;
	private Config config = new Config();
	private PreferencesHelper preferencesHelper;
	private ListView mListView;
	private Sam3Adapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	sam3ItemType sam3item = new sam3ItemType();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.sam3);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		initView();
		new sam3().execute();
	}

	private void initView() {
		account = preferencesHelper.getString("account", "");
		userId = getIntent().getStringExtra("userId") == null ? ""
				: getIntent().getStringExtra("userId");

		refresh_progress = (ProgressBar) findViewById(R.id.refresh_progress);
		mListView = (ListView) findViewById(R.id.listView1);
		mAdapter = new Sam3Adapter(this, mData);
		mListView.setAdapter(mAdapter);
		
		mBack = (Button) findViewById(R.id.bt_back);
		
		mBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 
	 * @param s
	 *            json解析
	 * @return
	 */
	private void parserList(String s) {
		sam3ListParse ww = new sam3ListParse(s);
		ww.praser();
		sam3item = ww.praserList();
	}

	/** 加载列表数据 */
	private class sam3 extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
//			String re = getJsonBack("http://netcapi2.jnu.edu.cn/netcapi/netcUser/sam3Info.do");
			String re = getJsonBack(config.SAM3);
			System.out.println("re = " + re);
			parserList(re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			refresh_progress.setVisibility(View.INVISIBLE);
			if (result != null) {
				if (result.equals("noEffect")) {
					Toast.makeText(getApplicationContext(), "暂无更新",
							Toast.LENGTH_SHORT).show();
				} else {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map = new HashMap<String, Object>();
					System.out.println("name==" + sam3item.getName());
					System.out.println("tempelate==" + sam3item.getTemplate());
					map.put("name", sam3item.getName());
					map.put("address", sam3item.getAddress());
					map.put("group", sam3item.getGroup());
					map.put("template", sam3item.getTemplate());
					map.put("mac", sam3item.getMac());
					map.put("power", sam3item.getPower());
					map.put("online", sam3item.getOnline());
					mData.add(map);
				}
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
			mListView.invalidate();
			 mAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			refresh_progress.setVisibility(View.VISIBLE);
		}
	}

	private String getJsonBack(String url) {
		String timestamp = ParamsManager.getTime();
//		String userId = "fatfu12345";
		String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
				+ timestamp + Config.VER + account + userId);
		IEasy ieasy = new IEasy(new IEasyHttpApiV1());
		return ieasy.getUserDetail(url, sign, timestamp, account, userId);
	}
}
