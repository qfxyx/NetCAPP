package com.example.jinanuniversity.Activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.util.ActivityCollector;

/** 维护查询 */
public class Maintenance_inquire extends Activity implements OnClickListener {

	private static final String TAG = "Activity.Maintenance_inquire";
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	static final int DATE_FINISH_ID = 2;
	static final int TIME_FINISH_ID = 3;
	static final int TIMESTART_ID = 4;
	static final int TIMEENDG_ID = 5;
	static final int CLOSE_TIMESTART_ID = 6;
	static final int CLOSE_TIMEENDG_ID = 7;

	private Button bt_detail;
	private String jobId, userId, userName, jobIdSelect, userIdSelect,
			userNameSelect, accepteTimeStart, accepteTimeEnd, closeTimeStart,
			closeTimeEnd;
	private CheckBox maintian_num, user_num, user_name, TimeSelec, close_time;
	private RadioButton my_account, my_group, all_group, rb_num, rb_time,
			rb_address;
	private RadioGroup my_Select, genderGroup;
	private EditText ed_maintian_num, ed_user_num, ed_user_name;
	private DatePicker TimeStart, TimeEnd, close_timeStart, close_timeEnd;
	PreferencesHelper preferencesHelper;
	private Button bt_begin_time, bt_finish_time, bt_close_time,
			bt_close_time1;
	private EditText close_time1, ed_close_time, ed_finish_time, ed_begin_time;
	StringBuilder beginTime, finishTime, close_bg_time, close_fs_time;

	// private int startHour, endHour, closeHour, close_endHour; // 存放时间的小时
	// private int startMinute, endMinute, closeMinute, close_endMinute; //
	// 存放时间的分钟

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintenance_inquire);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		initView();
		getDateScope();
		getjobOrder();
		ActivityCollector.addActivity(this);
		// getDate();
		Log.i(TAG, "onCreate end");
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume start");
		super.onResume();
		Log.i(TAG, "onResume end");
	}

	/**
	 * 范围筛选
	 */
	private void getDateScope() {
		String groupname = preferencesHelper.getString("groupname", "");
		String one = "my";
		String three = "all";
		String message = preferencesHelper.getString("rangeSelect", "");
		System.out.println("message==" + message);
		if (message.equals(one)) {
			my_account.setChecked(true);
		} else {
			my_account.setChecked(false);
		}
		if (message.equals(groupname)) {
			my_group.setChecked(true);
		} else {
			my_group.setChecked(false);
		}
		if (message.equals(three)) {
			all_group.setChecked(true);
		} else {
			all_group.setChecked(false);
		}
	}

	/**
	 * 排序筛选
	 */
	private void getjobOrder() {
		String one = "id";
		String two = "address";
		String three = "accepteTime";
		String message = preferencesHelper.getString("jobOrder", "");
		Log.i(TAG, "getjobOrder() message= " + message);
		if (message.equals(one)) {
			rb_num.setChecked(true);
		} else {
			rb_num.setChecked(false);
		}
		if (message.equals(three)) {
			rb_time.setChecked(true);
		} else {
			rb_time.setChecked(false);
		}
		if (message.equals(two)) {
			rb_address.setChecked(true);
		} else {
			rb_address.setChecked(false);
		}
	}

	/**
	 * @param value
	 *            状态
	 * @param checkBox
	 *            控件
	 * @param radioButton
	 *            控件
	 * @param name
	 */
	private void measure(CheckBox checkBox, RadioButton radioButton, String name) {
		if (checkBox != null) {
			String value = "yes";
			String message = preferencesHelper.getString(name, "no");
			if (message.equals(value)) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}
		}
	}

	/**
	 * 
	 * @param value
	 *            值
	 * @param checkBox
	 *            控件
	 * @param radioButton
	 *            控件
	 * @param name
	 *            键
	 */
	private void setMeasure(String value, CheckBox checkBox, String name) {
		preferencesHelper.setString(name, value);
	}

	private void setMeasure(String value, RadioGroup radioGroup, String name) {
		preferencesHelper.setString(name, value);
	}

	private void initView() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		final Calendar cc = Calendar.getInstance();
		mHour = cc.get(Calendar.HOUR_OF_DAY);
		mMinute = cc.get(Calendar.MINUTE);

		bt_detail = (Button) findViewById(R.id.bt_detail);
		my_account = (RadioButton) findViewById(R.id.my_account);
		my_group = (RadioButton) findViewById(R.id.my_group);
		all_group = (RadioButton) findViewById(R.id.all_group);
		rb_num = (RadioButton) findViewById(R.id.rb_num);
		rb_time = (RadioButton) findViewById(R.id.rb_time);
		rb_address = (RadioButton) findViewById(R.id.rb_address);
		maintian_num = (CheckBox) findViewById(R.id.maintian_num);
		user_num = (CheckBox) findViewById(R.id.user_num);
		user_name = (CheckBox) findViewById(R.id.user_name);
		my_Select = (RadioGroup) findViewById(R.id.my_Select);
		genderGroup = (RadioGroup) findViewById(R.id.genderGroup);

		ed_maintian_num = (EditText) findViewById(R.id.ed_maintian_num);
		ed_user_num = (EditText) findViewById(R.id.ed_user_num);
		ed_user_name = (EditText) findViewById(R.id.ed_user_name);

		ed_maintian_num.setEnabled(false);
		ed_user_num.setEnabled(false);
		ed_user_name.setEnabled(false);

		TimeSelec = (CheckBox) findViewById(R.id.TimeSelec);
		close_time = (CheckBox) findViewById(R.id.close_time);

		bt_begin_time = (Button) findViewById(R.id.bt_begin_time);
		bt_finish_time = (Button) findViewById(R.id.bt_finish_time);
		bt_close_time = (Button) findViewById(R.id.bt_close_time);
		bt_close_time1 = (Button) findViewById(R.id.bt_close_time1);

		close_time1 = (EditText) findViewById(R.id.close_time1);
		ed_close_time = (EditText) findViewById(R.id.ed_close_time);
		ed_finish_time = (EditText) findViewById(R.id.ed_finish_time);
		ed_begin_time = (EditText) findViewById(R.id.ed_begin_time);

		bt_detail.setOnClickListener(this);
		bt_finish_time.setOnClickListener(this);
		bt_begin_time.setOnClickListener(this);
		bt_close_time.setOnClickListener(this);
		bt_close_time1.setOnClickListener(this);

		/** 范围筛选（单项） */
		my_Select
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						String groupname = preferencesHelper.getString(
								"groupname", "");
						String rangeSelect = "";
						if (my_account.getId() == checkedId) {
							rangeSelect = "my";
						} else if (my_group.getId() == checkedId) {
							rangeSelect = groupname;
						} else if (all_group.getId() == checkedId) {
							rangeSelect = "all";
						}
						setMeasure(rangeSelect, my_Select, "rangeSelect");
					}
				});
		/** 排序筛选（单项） */
		genderGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						String jobOrder = "";
						if (rb_num.getId() == checkedId) {
							jobOrder = "id";
						} else if (rb_address.getId() == checkedId) {
							jobOrder = "address";
						} else if (rb_time.getId() == checkedId) {
							jobOrder = "accepteTime";
						}
						setMeasure(jobOrder, genderGroup, "jobOrder");
					}
				});

		/** 是否参与查询,维护单号 */

		maintian_num.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = maintian_num.isChecked();
				String jobIdSelect = "";
				if (isChecked) {
					jobIdSelect = "yes";
					ed_maintian_num.setEnabled(true);
				} else {
					jobIdSelect = "no";
					ed_maintian_num.setEnabled(false);
				}

				setMeasure(jobIdSelect, maintian_num, "jobIdSelect");
			}
		});

		/** 是否参与查询,用户编号 */
		user_num.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = user_num.isChecked();
				String userIdSelect = "";
				if (isChecked) {
					userIdSelect = "yes";
					ed_user_num.setEnabled(true);
				} else {
					userIdSelect = "no";
					ed_user_num.setEnabled(false);
				}
				setMeasure(userIdSelect, user_num, "userIdSelect");
			}
		});
		/** 是否参与查询,用户姓名 */
		user_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = user_name.isChecked();
				String userNameSelect = "";
				if (isChecked) {
					userNameSelect = "yes";
					ed_user_name.setEnabled(true);
				} else {
					userNameSelect = "no";
					ed_user_name.setEnabled(false);
				}
				setMeasure(userNameSelect, user_name, "userNameSelect");
			}
		});

		/** 是否参与查询,保修时间 */
		TimeSelec
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						String accepteTimeSelect = "";
						if (isChecked) {
							accepteTimeSelect = "yes";
						} else {
							accepteTimeSelect = "no";
						}
						setMeasure(accepteTimeSelect, TimeSelec,
								"accepteTimeSelect");
					}
				});
		/** 是否参与查询,消单时间 */
		close_time
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						String closeTimeSelect = "";
						if (isChecked) {
							closeTimeSelect = "yes";
						} else {
							closeTimeSelect = "no";
						}
						setMeasure(closeTimeSelect, close_time,
								"closeTimeSelect");
					}
				});
	}

	private void idTime() {
		String accepteTimeSelect = preferencesHelper.getString(
				"closeTimeSelect", "no");
		String closeTimeSelect = preferencesHelper.getString("closeTimeSelect",
				"no");
		if (accepteTimeSelect.equals("no")) {
			accepteTimeStart = "";
			accepteTimeEnd = "";
		} else {
			accepteTimeEnd = finishTime + ":" + "00";
			accepteTimeStart = beginTime + ":" + "00";
		}
		if (closeTimeSelect.equals("no")) {
			closeTimeStart = "";
			closeTimeEnd = "";
		} else {
			closeTimeStart = close_bg_time + ":" + "00";
			closeTimeEnd = close_fs_time + ":" + "00";
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_detail:
			idTime();
			jobId = ed_maintian_num.getText().toString();
			userId = ed_user_num.getText().toString();
			userName = ed_user_name.getText().toString();

			Intent intent = new Intent(Maintenance_inquire.this,
					Main_inquire_list.class);
			intent.putExtra("jobId", jobId);
			intent.putExtra("userId", userId);
			intent.putExtra("userName", userName);
			intent.putExtra("accepteTimeStart", accepteTimeStart);
			intent.putExtra("accepteTimeEnd", accepteTimeEnd);
			intent.putExtra("closeTimeStart", closeTimeStart);
			intent.putExtra("closeTimeEnd", closeTimeEnd);
			startActivity(intent);
			break;
		case R.id.bt_begin_time:
			showDialog(DATE_DIALOG_ID);
			showDialog(TIME_DIALOG_ID);
			break;
		case R.id.bt_finish_time:
			showDialog(DATE_FINISH_ID);
			showDialog(TIME_FINISH_ID);
			break;
		case R.id.bt_close_time:
			showDialog(TIMESTART_ID);
			showDialog(TIMEENDG_ID);
			break;
		case R.id.bt_close_time1:
			showDialog(CLOSE_TIMESTART_ID);
			showDialog(CLOSE_TIMEENDG_ID);
			break;

		}
	}

	/**
	 * 保单开始时间
	 */
	private void updateDisplay() {
		beginTime = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" ")
				.append(pad(mHour)).append(":").append(pad(mMinute));
		ed_begin_time.setText(beginTime);

	}

	/**
	 * 保单结束时间
	 */
	private void updateDisplay1() {
		finishTime = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" ")
				.append(pad(mHour)).append(":").append(pad(mMinute));
		ed_finish_time.setText(finishTime);

	}

	/**
	 * 消单开始时间
	 */
	private void closedateDisplay() {
		close_bg_time = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" ")
				.append(pad(mHour)).append(":").append(pad(mMinute));
		ed_close_time.setText(close_bg_time);

	}

	/**
	 * 消单结束时间
	 */
	private void closedateDisplay1() {
		close_fs_time = new StringBuilder().append(mYear).append("-")
				.append(mMonth + 1).append("-").append(mDay).append(" ")
				.append(pad(mHour)).append(":").append(pad(mMinute));
		close_time1.setText(close_fs_time);

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

	private DatePickerDialog.OnDateSetListener mCloseListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			closedateDisplay();
		}
	};

	private TimePickerDialog.OnTimeSetListener mCloseTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			closedateDisplay();
		}
	};

	private DatePickerDialog.OnDateSetListener mCloseListener1 = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			closedateDisplay1();
		}
	};

	private TimePickerDialog.OnTimeSetListener mCloseTimeSetListener1 = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			closedateDisplay1();
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

		case TIMESTART_ID:
			return new DatePickerDialog(this, mCloseListener, mYear, mMonth,
					mDay);

		case TIMEENDG_ID:
			return new TimePickerDialog(this, mCloseTimeSetListener, mHour,
					mMinute, false);

		case CLOSE_TIMESTART_ID:
			return new DatePickerDialog(this, mCloseListener1, mYear, mMonth,
					mDay);

		case CLOSE_TIMEENDG_ID:
			return new TimePickerDialog(this, mCloseTimeSetListener1, mHour,
					mMinute, false);

		}
		return null;
	}

}
