package com.example.jinanuniversity.Activity;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.NewMessageListParser;
import com.example.jinanuniversity.util.ActivityCollector;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class OtherActivity extends Activity implements OnClickListener {
	private static final String TAG = "Activity.OtherActivity";
	private Button bt_qiut, bt_notice, bt_server, bt_cancel;
	PreferencesHelper preferencesHelper;
	private String account;
	private int code;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		initView();
		ActivityCollector.addActivity(this);
		Log.i(TAG, "onCreate end");
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume start");
		super.onResume();
		Log.i(TAG, "onResume end");
	}

	private void initView() {
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");

		bt_notice = (Button) findViewById(R.id.bt_notice);
		bt_server = (Button) findViewById(R.id.bt_server);
		bt_cancel = (Button) findViewById(R.id.bt_cancel);
		bt_qiut=(Button)findViewById(R.id.bt_quit);

		bt_notice.setOnClickListener(this);
		bt_server.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		bt_qiut.setOnClickListener(this);
	}

	private class logout extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			String timestamp = ParamsManager.getTime();
			String sign = "";
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account);
			System.out.println("sign==" + sign);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String result = ieasy.cancel(Config.APPKEY, sign, timestamp,
					Config.VER, account);
			System.out.println("result==" + result);
			NewMessageListParser ww = new NewMessageListParser(result);
			int code = ww.getCode();
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			dismissProgressDialog();
			if (result == 0) {
				Toast.makeText(OtherActivity.this, "注销成功！", Toast.LENGTH_SHORT)
						.show();
				startActivity(new Intent(OtherActivity.this,
						LandingActivity.class));
				OtherActivity.this.finish();
			} else {
				Toast.makeText(OtherActivity.this, "注销失败！", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.bt_notice:
			startActivity(new Intent(OtherActivity.this,
					NoticeInstallActivity.class));
			break;
		case R.id.bt_server:
			startActivity(new Intent(OtherActivity.this, ServerActivity.class));
			break;
		case R.id.bt_cancel:
			// startActivity(new
			// Intent(OtherActivity.this,ServerActivity.class));
			new logout().execute();
			break;
			case R.id.bt_quit:
				ActivityCollector.finishAll();



		default:
			break;
		}
	}

	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("注销中，请稍候...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			mProgressDialog = dialog;
		}
		mProgressDialog.show();
		return mProgressDialog;
	}

	private void dismissProgressDialog() {
		try {
			mProgressDialog.dismiss();
		} catch (IllegalArgumentException e) {
		}
	}

}
