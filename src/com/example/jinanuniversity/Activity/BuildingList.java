package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.util.PinyinUtils;
import com.example.jinanuniversity.view.MySideBar;
import com.example.jinanuniversity.view.MySideBar.OnTouchingLetterChangedListener;

public class BuildingList extends Activity implements
		OnTouchingLetterChangedListener {

	private ListView lvShow;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private TextView overlay;
	private MySideBar myView;
	private Button btn_back;

	private OverlayThread overlayThread = new OverlayThread();
	private PreferencesHelper preferencesHelper;
	private String buildingList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.building_list);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),PreferencesHelper.LOGININFO);
		buildingList = preferencesHelper.getString("buildingList", "");
		buildingList = buildingList.substring(1, buildingList.length() - 1);
		String[] strings = buildingList.split(",");
		HashMap<String, Object> map;
		for (int i = 0; i < strings.length; i++) {
			map = new HashMap<String, Object>();
			//delete the ""
			map.put("name", strings[i].substring(1, strings[i].length() - 1));
			mData.add(map);
		}
		initView();
	}

	/** 初始化视图 */
	public void initView() {
		lvShow = (ListView) findViewById(R.id.lvShow);
		myView = (MySideBar) findViewById(R.id.myView);
		overlay = (TextView) findViewById(R.id.tvLetter);
		lvShow.setTextFilterEnabled(true);
		overlay.setVisibility(View.INVISIBLE);

		SimpleAdapter mAdapter = new SimpleAdapter(this, mData,
				R.layout.building_list_item, new String[] { "name" },
				new int[] { R.id.tx_problem });

		lvShow.setAdapter(mAdapter);
		myView.setOnTouchingLetterChangedListener(this);

		lvShow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("building",
						(String) mData.get(position).get("name"));
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		
		btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	private class OverlayThread implements Runnable {
		public void run() {
			overlay.setVisibility(View.GONE);
		}
	}

	@Override
	public void onTouchingLetterChanged(String s) {
		overlay.setText(s);
		overlay.setVisibility(View.VISIBLE);
		handler.removeCallbacks(overlayThread);
		handler.postDelayed(overlayThread, 1000);
		if (alphaIndexer(s) > 0) {
			int position = alphaIndexer(s);
			lvShow.setSelection(position);
		}
	}

	public int alphaIndexer(String s) {
		int position = 0;
		for (int i = 0; i < mData.size(); i++) {
			if (PinyinUtils.getAlpha(((String) mData.get(i).get("name")))
					.startsWith(s)) {
				position = i;
				break;
			}
		}
		return position;
	}

	private Handler handler = new Handler() {
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}
}
