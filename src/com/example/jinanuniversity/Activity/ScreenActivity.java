package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;

public class ScreenActivity extends Activity implements OnClickListener,OnItemSelectedListener {
	
	private static final String TAG = "Activity.ScreenActivity";
	
	private CheckBox respond,unselect, closeJobSelect, uncloseJobSelect;
	private RadioGroup raGroup_time, raSort;
	private RadioButton one_day, two_day, three_day, studentid, rb_address, rb_time;

	private Spinner groupScopeSpinner;
	private ArrayAdapter<String> groupscopeAdapter;
	
	// 用于存放组名
	private String[] groupNames;
	private Button bt_back;
	private PreferencesHelper preferencesHelper;
	private ParamsManager pm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.screen);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),PreferencesHelper.LOGININFO);
		
		initView();
		measure(respond, "select");
		measure(unselect, "unselect");
		measure(closeJobSelect, "closeJobSelect");
		measure(uncloseJobSelect, "uncloseJobSelect");
		getDateTmie();
		getDateScope();
		getDateraSort();
		Log.i(TAG, "onCreate end");
	}

	/**
	 * 时间筛选
	 */
	private void getDateTmie() {
		String one = "1";
		String two = "2";
		String three = "3";
		String message = preferencesHelper.getString("sex", "");
		if (message.equals(one)) {
			one_day.setChecked(true);
		} else {
			one_day.setChecked(false);
		}
		if (message.equals(two)) {
			two_day.setChecked(true);
		} else {
			two_day.setChecked(false);
		}
		if (message.equals(three)) {
			three_day.setChecked(true);
		} else {
			three_day.setChecked(false);
		}
	}

	/**
	 * 范围筛选
	 */
	private void getDateScope() {
		String groupname = preferencesHelper.getString("groupname", "");
		// String two = "all";
		// String three = "support";
		String scope = preferencesHelper.getString("scope", "");
		int index = 0;
		if (scope.equals("support")) {
			scope="支援";
		}
		if (scope.equals("all")) {
			scope="所有";
		}
		if (null != groupNames && 0 != groupNames.length) {
			for (int i = 0; i < groupNames.length; i++) {
				if (groupNames[i].equals(scope)) {
					index = i;
					break;
				}
			}
		}
		groupScopeSpinner.setSelection(index);
	}

	/**
	 * 排序规则
	 */
	private void getDateraSort() {
		String one = "id";
		String two = "address";
		String three = "time";
		String message = preferencesHelper.getString("sort", "");
		if (message.equals(one)) {
			studentid.setChecked(true);
		} else {
			studentid.setChecked(false);
		}
		if (message.equals(two)) {
			rb_address.setChecked(true);
		} else {
			rb_address.setChecked(false);
		}
		if (message.equals(three)) {
			rb_time.setChecked(true);
		} else {
			rb_time.setChecked(false);
		}
	}

	/**
	 * 设置 CheckBox 状态
	 */
	private void measure(CheckBox checkBox, String name) {
		if (checkBox != null) {
			String value = preferencesHelper.getString(name, "no");
			if (value.equals("yes")) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}
		}
	}

	/**
	 * 保存选择状态
	 */
	private void setMeasure(String value, String name) {
		preferencesHelper.setString(name, value);
	}

	private void initView() {
		pm = (ParamsManager) getApplication();
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_back.setOnClickListener(this);
		respond = (CheckBox) findViewById(R.id.cb_respond);
		unselect = (CheckBox) findViewById(R.id.unselect);
		closeJobSelect = (CheckBox) findViewById(R.id.closeJobSelect);
		uncloseJobSelect = (CheckBox) findViewById(R.id.uncloseJobSelect);

		one_day = (RadioButton) findViewById(R.id.rb_oneday);
		two_day = (RadioButton) findViewById(R.id.rb_twoday);
		three_day = (RadioButton) findViewById(R.id.rb_threeday);
		raGroup_time = (RadioGroup) findViewById(R.id.genderGroup);
		raSort = (RadioGroup) findViewById(R.id.rg_sort);
		
		String groupList = preferencesHelper.getString("groupList", "");
		groupList=groupList.substring(1,groupList.length()-1);
		groupNames=groupList.split(",");
		if(groupNames!=null){
			for(int i=0;i<groupNames.length;i++){
				groupNames[i]=groupNames[i].substring(1, groupNames[i].length()-1);
			}
		}

		// 下面代码用于初始化Spinner控件，包括其中各项显示的内容。
		groupScopeSpinner = (Spinner) findViewById(R.id.group_scope);
		groupscopeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, groupNames);
		groupscopeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		groupScopeSpinner.setAdapter(groupscopeAdapter);
		groupScopeSpinner.setOnItemSelectedListener(this);
		groupScopeSpinner.setVisibility(View.VISIBLE);

		studentid = (RadioButton) findViewById(R.id.rb_studentid);
		rb_address = (RadioButton) findViewById(R.id.rb_address);
		rb_time = (RadioButton) findViewById(R.id.rb_time);

		/** 响应按键 */
		respond.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				String select = "";
				if (isChecked) {
					select = "yes";
				} else {
					select = "no";
				}
				setMeasure(select, "select");
			}

		});
		/** 未响应按键 */
		unselect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				String select = "";
				if (isChecked) {
					select = "yes";
				} else {
					select = "no";
				}
				setMeasure(select, "unselect");
			}

		});
		/** 已消单按键 */
		closeJobSelect
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						String closeselect = "";
						if (isChecked) {
							closeselect = "yes";
						} else {
							closeselect = "no";
						}
						setMeasure(closeselect, "closeJobSelect");
					}

				});
		/** 未消单按键 */
		uncloseJobSelect
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						String uncloseSelect = "";
						if (isChecked) {
							uncloseSelect = "yes";
						} else {
							uncloseSelect = "no";
						}
						setMeasure(uncloseSelect, "uncloseJobSelect");
					}
				});

		/** 时间筛选（单项） */
		raGroup_time
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						String sex = "";
						if (one_day.getId() == checkedId) {
							sex = "1";
							setMeasure(sex, "id");
						} else if (two_day.getId() == checkedId) {
							sex = "2";
							setMeasure(sex, "id");
						} else if (three_day.getId() == checkedId) {
							sex = "3";
							setMeasure(sex, "id");
						}
						setMeasure(sex, "sex");
					}
				});

		/** 排序规则（单项） */
		raSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String sort = "";
				if (studentid.getId() == checkedId) {
					sort = "id";
				} else if (rb_address.getId() == checkedId) {
					sort = "address";
				} else if (rb_time.getId() == checkedId) {
					sort = "time";
				}
				setMeasure(sort, "sort");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			pm.setRefresh(true);
			finish();
			break;
		default:
			break;
		}
	}

	/** 范围筛选（单项） */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		String scope = "";
		String selectedItem = groupscopeAdapter.getItem(arg2).toString();
		if ("学生维护1组".equals(selectedItem)) {
			scope = "学生维护1组";

		} else if ("学生维护2组".equals(selectedItem)) {
			scope = "学生维护2组";

		} else if ("学生维护3组".equals(selectedItem)) {
			scope = "学生维护3组";

		} else if ("学生维护4组".equals(selectedItem)) {
			scope = "学生维护4组";

		} else if ("教工机动组".equals(selectedItem)) {
			scope = "教工机动组";

		} else if ("学生机动组".equals(selectedItem)) {
			scope = "学生机动组";

		} else if ("办公机动组".equals(selectedItem)) {
			scope = "办公机动组";

		} else if ("支援".equals(selectedItem)) {
			scope = "支援";
		} else if ("所有".equals(selectedItem)) {
			scope = "所有";
		} else if("南校区机动组".equals(selectedItem)){
			//new added by liangbin
			scope="南校区机动组";
		}
		setMeasure(scope, "scope");
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			Log.i(TAG, "onKeyDown you has touch the home button");
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i(TAG, "onKeyDown you has touch the back button");
			pm.setRefresh(true);
		}
		return super.onKeyDown(keyCode, event);
	}
}
