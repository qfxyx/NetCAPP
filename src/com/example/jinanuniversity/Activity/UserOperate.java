package com.example.jinanuniversity.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MainDetailedParser;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

/** 用户操作 */
public class UserOperate extends Activity implements OnClickListener {

	private static final String TAG = "Activity.UserOperate";
	private Button btn_back, repeat_pwd, repeat_mac, Unbunded_mac, btn_change;
	private Button button;
	private Spinner spinner;
	private TextView tvOldMacAddress;
	private EditText etNewMacAddress;
	private ArrayAdapter<String> adapter;

	private PreferencesHelper preferencesHelper;
	private ProgressDialog mProgressDialog;
	private ParamsManager pm;
	private Config config = new Config();
	private StringBuffer mac = new StringBuffer();
	private String account, userId, info, oldMac, template, txt, property,txtdialog, newMac,templateList;
	private int code = 1;
	private boolean isChange = false, isTemplate = false; // 物理地址是否改动

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_operate);
		initView();
	}

	private void initView() {
		preferencesHelper = new PreferencesHelper(getApplicationContext(),PreferencesHelper.LOGININFO);
		pm = (ParamsManager) getApplication();
		account = preferencesHelper.getString("account", "");
		property = preferencesHelper.getString("property", "");
		templateList=preferencesHelper.getString("templateList", "");
		templateList=templateList.substring(1,templateList.length()-1);
		String[] templateStrArray=templateList.split(",");
		for(int i=0;i<templateStrArray.length;i++){
			templateStrArray[i]=templateStrArray[i].substring(1,templateStrArray[i].length()-1);
		}

		userId = getIntent().getStringExtra("userId") == null ? "": getIntent().getStringExtra("userId");
		oldMac = getIntent().getStringExtra("mac") == null ? "" : getIntent().getStringExtra("mac");
		if (oldMac.indexOf("-") == -1) {
			oldMac = "AA-AA-AA-AA-AA-AA";
		}
		mac.append(oldMac);
		oldMac = oldMac.replaceAll("-", "");
		btn_back = (Button) findViewById(R.id.bt_back);
		repeat_pwd = (Button) findViewById(R.id.repeat_pwd);
		repeat_mac = (Button) findViewById(R.id.repeat_mac);
		Unbunded_mac = (Button) findViewById(R.id.Unbunded_mac);
		btn_change = (Button) findViewById(R.id.btn_change);

		tvOldMacAddress = (TextView) findViewById(R.id.tv_oldmac_address);
		tvOldMacAddress.setText(mac.toString());
		etNewMacAddress = (EditText) findViewById(R.id.ed_newmac_address);

		if (property.equals("机动")) {
			btn_change.setText("修改模板");
		}

		spinner = (Spinner) findViewById(R.id.spinner);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, templateStrArray);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				template = parent.getItemAtPosition(position).toString();
				Log.i(TAG, "template ="+template);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		spinner.setVisibility(View.VISIBLE);
		btn_back.setOnClickListener(this);
		repeat_pwd.setOnClickListener(this);
		repeat_mac.setOnClickListener(this);
		Unbunded_mac.setOnClickListener(this);
		btn_change.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			this.finish();
			break;

		case R.id.repeat_pwd:
			txt = "密码重置成功 !";
			txtdialog = "密码重置中...";
			//showProgressDialog(txtdialog);
			//new getInfo().execute(config.PWDRESET);
			Intent send_intent=new Intent(this,ResetPwd.class);
			send_intent.putExtra("userId",userId);
			send_intent.putExtra("account",account);
			startActivity(send_intent);
			break;

		case R.id.repeat_mac:
			isTemplate = false;
			showProgressDialog("更改MAC中...");
			Log.i(TAG, "onClick repeat_mac mac = " + mac);
			getNewMacAddress();
			if (!isChange) {
				dismissProgressDialog();
				Toast.makeText(this, "请更改MAC地址 !", Toast.LENGTH_SHORT).show();
			} else {
				new macUpdate().execute(mac.toString());
			}
			break;

		case R.id.Unbunded_mac:
			txt = "解绑物理地址成功 !";
			txtdialog = "解绑MAC中...";
			showProgressDialog(txtdialog);
			new getInfo().execute(config.MAC_FREE);
			break;

		case R.id.btn_change:
			isTemplate = true;
			showProgressDialog("更改模板中...");
			new macUpdate().execute(template);
			break;
		default:
			break;
		}
	}

	@SuppressLint("DefaultLocale")
	private void getNewMacAddress() {
		newMac = etNewMacAddress.getText().toString();
		newMac = newMac.toUpperCase();
		if (!newMac.equals(oldMac) && newMac.length() == 12) {
			mac.delete(0, mac.length());
			Log.i(TAG, "getNewMacAddress mac = " + mac.toString());
			String[] apartOfMacAdr = new String[6];
			for (int i = 0; i < apartOfMacAdr.length; i++) {
				apartOfMacAdr[i] = newMac.substring(i * 2, i * 2 + 2);
			}
			mac.append(apartOfMacAdr[0]);
			for (int i = 1; i < apartOfMacAdr.length; i++) {
				mac.append("-");
				mac.append(apartOfMacAdr[i]);
			}
			isChange = true;
			Log.i(TAG, "getNewMacAddress new addrress mac = " + mac.toString());
		}

	}

	/** 用户物理地址修改 修改模版 */
	private class macUpdate extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			Log.i(TAG, "macUpdate doInBackground mac = " + params[0]);
			String timestamp = ParamsManager.getTime();
			String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY + timestamp + Config.VER + account + userId + params[0]);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = null;
			if (isTemplate) {
				re = ieasy.template(sign, timestamp, account, userId, params[0]);
			} else {
				re = ieasy.macUpdate(sign, timestamp, account, userId, params[0]);
			}
			parserList(re);
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dismissProgressDialog();
			String title = "";
			if (result == 0) {
				if (isTemplate) {
					title = "修改模版成功";
				} else {
					title = "物理地址修改成功";
				}
				pm.setRefresh(true);
			} else {
				title = info;
			}
			Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * json解析
	 */
	private void parserList(String json) {
		MainDetailedParser ww = new MainDetailedParser(json);
		ww.praser();
		code = ww.getCode();
		info = ww.getInfo();
	}

	/** 加载列表数据 */
	private class getInfo extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String re = getJsonBack(params[0]);
			parserList(re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			if (code == 0) {
				pm.setRefresh(true);

				Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), info,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private String getJsonBack(String url) {
		String timestamp = ParamsManager.getTime();
		String sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
				+ timestamp + Config.VER + account + userId);
		IEasy ieasy = new IEasy(new IEasyHttpApiV1());
		return ieasy.getUserDetail(url, sign, timestamp, account, userId);
	}

	private ProgressDialog showProgressDialog(String name) {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage(name);
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mProgressDialog = dialog;
			mProgressDialog.show();
		}
		return mProgressDialog;
	}

	private void dismissProgressDialog() {
		try {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		} catch (IllegalArgumentException e) {
		}
	}

}
