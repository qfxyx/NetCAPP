package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.data.Service;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

public class ServerActivity extends Activity implements OnClickListener {
	private RadioButton mServer1, mServer2, user_defined;
	private Button mBack, mPreserver;
	private RadioGroup radioGroup;
	PreferencesHelper preferencesHelper;
	private EditText ed_url;

	private String account, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.server);

		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		initView();
		getDateraSort();
	}

	private void initView() {
		mBack = (Button) findViewById(R.id.bt_back);
		mPreserver = (Button) findViewById(R.id.preserver);
		mServer1 = (RadioButton) findViewById(R.id.server1);
		mServer2 = (RadioButton) findViewById(R.id.server2);
		user_defined = (RadioButton) findViewById(R.id.user_defined);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		ed_url = (EditText) findViewById(R.id.ed_url);

		mServer1.setOnClickListener(this);
		mServer2.setOnClickListener(this);
		mBack.setOnClickListener(this);
		mPreserver.setOnClickListener(this);

		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						String server = "";
						if (mServer1.getId() == checkedId) {
							server = "1";
						} else if (mServer2.getId() == checkedId) {
							server = "2";
						} else if (user_defined.getId() == checkedId) {
							server = "3";
						}
						preferencesHelper.setString("server", server);
					}
				});
	}

	private void getDateraSort() {
		String one = "1";
		String two = "2";
		String three = "3";
		String message = preferencesHelper.getString("server", "");
		if (message.equals(one)) {
			mServer1.setChecked(true);
		} else {
			mServer1.setChecked(false);
		}
		if (message.equals(two)) {
			mServer2.setChecked(true);
		} else {
			mServer2.setChecked(false);
		}
		if (message.equals(three)) {
			user_defined.setChecked(true);
		} else {
			user_defined.setChecked(false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;

		case R.id.preserver:
			if (mServer1.isChecked()) {
				Service.getInstance().setServiceIp("netcapi1.jnu.edu.cn");
			} else if (mServer2.isChecked()) {
				Service.getInstance().setServiceIp("netcapi2.jnu.edu.cn");
			} else if (user_defined.isChecked()) {
				Service.getInstance().setServiceIp(
						ed_url.getText().toString().trim());
			}
			new login().execute();
			break;

		default:
			break;
		}
	}

	private class login extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {

			account = preferencesHelper.getString("account", "");
			password = preferencesHelper.getString("loginServer", "");

			String timestamp = ParamsManager.getTime();
			password = ParamsManager.enCode(ParamsManager.getMd5sign(password));
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account
					+ password);

			IEasyHttpApiV1 httApi = new IEasyHttpApiV1();
			IEasy ieasy = new IEasy(httApi);
			String guestInfo = ieasy.invitationCodeLogin(Config.APPKEY,
					timestamp, sign, Config.VER, account, password);

			return guestInfo;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.equals("noEffect")) {
				Toast.makeText(ServerActivity.this, "保存失败", 2000).show();
			} else {
				Toast.makeText(ServerActivity.this, "保存成功", 2000).show();
			}
		}
	}
}
