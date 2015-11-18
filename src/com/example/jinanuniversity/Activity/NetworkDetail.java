package com.example.jinanuniversity.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.adapter.NetWorkAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

/**
 * 上网明细
 * 
 * @author luxun
 * 
 */
public class NetworkDetail extends Activity implements OnClickListener {

	private static final String TAG = "Activity.NetworkDetail";

	private Button mBcak, search, bt_finish_time, bt_begin_time;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;
	private EditText ed_finish_time, ed_begin_time;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	static final int DATE_FINISH_ID = 2;
	static final int TIME_FINISH_ID = 3;
	private ListView mListView;
	private NetWorkAdapter mAdapter;
	private List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
	private LayoutInflater mInflater;
	private LinearLayout mMoreView;
	private TextView footerMoreTxt;
	private ProgressBar footerMoreProgress;
	private ProgressBar message_refresh_progress;
	PreferencesHelper preferencesHelper;
	private String timestamp, account;
	StringBuilder beginTime, finishTime;
	private int pageNo = 1;
	private String userId;
	private Time currentTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.network_detail);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		userId = getIntent().getStringExtra("userId") == null ? ""
				: getIntent().getStringExtra("userId");
		initView();
		Log.i(TAG, "onCreate end");
	}

	private void initView() {
		currentTime = new Time();
		message_refresh_progress = (ProgressBar) findViewById(R.id.nmessage_refresh_progress);
		mListView = (ListView) findViewById(R.id.nlistView1);
		mAdapter = new NetWorkAdapter(this, mData);
		mListView.setAdapter(mAdapter);
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
				new sam3DetailList().execute();
			}
		});

		mListView.addFooterView(mMoreView);
		mBcak = (Button) findViewById(R.id.bt_back);
		search = (Button) findViewById(R.id.search);
		bt_finish_time = (Button) findViewById(R.id.bt_finish_time);
		bt_begin_time = (Button) findViewById(R.id.bt_begin_time);
		ed_begin_time = (EditText) findViewById(R.id.ed_begin_time);
		ed_finish_time = (EditText) findViewById(R.id.ed_finish_time);
		currentTime.setToNow();
		int year = currentTime.year;
		int month = currentTime.month;
		int day = currentTime.monthDay;
		month += 1;
		ed_begin_time.setText(year + "-" + month + "-" + day + " 00:00");
		ed_finish_time.setText(year + "-" + month + "-" + day + " 23:59");
		mBcak.setOnClickListener(this);
		search.setOnClickListener(this);
		bt_finish_time.setOnClickListener(this);
		bt_begin_time.setOnClickListener(this);
		ed_begin_time.setOnClickListener(this);
		ed_finish_time.setOnClickListener(this);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		final Calendar cc = Calendar.getInstance();
		mHour = cc.get(Calendar.HOUR_OF_DAY);
		mMinute = cc.get(Calendar.MINUTE);
		// updateDisplay();
		// updateDisplay1();
	}

	/** 加载列表数据 */
	private class sam3DetailList extends AsyncTask<String, Void, String> {

		JSONObject dataJson;

		@Override
		protected String doInBackground(String... params) {
			String startTime = String.valueOf(beginTime) + ":" + "00";
			String endTime = String.valueOf(finishTime) + ":" + "00";
			if (beginTime == null || finishTime == null) {
				startTime = ed_begin_time.getText().toString() + ":" + "00";
				endTime = ed_finish_time.getText().toString() + ":" + "00";
			}
			Log.i(TAG, "sam3LogList startTime = " + startTime);
			Log.i(TAG, "sam3LogList endTime = " + endTime);
			timestamp = ParamsManager.getTime();
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account + userId
					+ startTime + endTime + pageNo);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = "";
			re = ieasy.getsam3DetailList(Config.APPKEY, sign, timestamp,
					Config.VER, account, userId, startTime, endTime,
					String.valueOf(pageNo));
			Log.i(TAG, "sam3LogList doInBackground re = " + re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			message_refresh_progress.setVisibility(View.INVISIBLE);

			if (result != null) {
				if (result.equals("noEffect")) {
					Toast.makeText(getApplicationContext(), "暂无更新",
							Toast.LENGTH_SHORT).show();
					footerMoreTxt.setText("暂无更新");
				} else {
					try {
						dataJson = new JSONObject(result);
						JSONArray arrayJson = dataJson
								.getJSONArray("resultList");
						HashMap<String, Object> map = new HashMap<String, Object>();
						for (int i = 0; i < arrayJson.length(); i++) {
							map = new HashMap<String, Object>();
							// Log.i(TAG,
							// "onPostExecute map = "
							// + formatArrayJsonContent(String
							// .valueOf(arrayJson.get(i))));
							map.put("sam3DetailList",
									formatArrayJsonContent(String
											.valueOf(arrayJson.get(i))));
							mData.add(map);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					footerMoreTxt.setText("更多");
					mMoreView.setClickable(true);
				}
				if (pageNo == 1) {
				} else {
					mListView.setSelection(mListView.getSelectedItemPosition());
				}
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
			mAdapter.notifyDataSetChanged();
			mListView.invalidate();
			footerMoreProgress.setVisibility(View.INVISIBLE);
		}

		private String formatArrayJsonContent(String content) {
			String[] arrayOfContent = content.split(",");
			StringBuilder targetContent = new StringBuilder();
			if (0 != arrayOfContent.length && arrayOfContent != null) {
				for (int i = 0; i < arrayOfContent.length; i++) {
					targetContent.append(arrayOfContent[i] + "\n");
				}
			}
			return targetContent.length() == 0 ? "" : targetContent.toString();
		}

		@Override
		protected void onPreExecute() {
			footerMoreTxt.setText("更新中");
			message_refresh_progress.setVisibility(View.VISIBLE);
			mMoreView.setClickable(false);
		}
	}

	private void updateDisplay() {
		beginTime = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" ")
				.append(pad(mHour)).append(":").append(pad(mMinute));
		ed_begin_time.setText(beginTime);

	}

	private void updateDisplay1() {
		finishTime = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" ")
				.append(pad(mHour)).append(":").append(pad(mMinute));
		ed_finish_time.setText(finishTime);

	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay();
		}
	};

	private DatePickerDialog.OnDateSetListener mFinishDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay1();
		}
	};

	private TimePickerDialog.OnTimeSetListener mFinishTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDisplay1();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);

		case DATE_FINISH_ID:
			return new DatePickerDialog(this, mFinishDateSetListener, mYear,
					mMonth, mDay);
		case TIME_FINISH_ID:
			return new TimePickerDialog(this, mFinishTimeSetListener, mHour,
					mMinute, false);

		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			this.finish();
			break;
		case R.id.bt_begin_time:
			showDialog(DATE_DIALOG_ID);
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.bt_finish_time:
			showDialog(DATE_FINISH_ID);
			showDialog(TIME_FINISH_ID);
			break;
		case R.id.search:
			mData.clear();
			new sam3DetailList().execute();
			break;
		default:
			break;
		}
	}
}
