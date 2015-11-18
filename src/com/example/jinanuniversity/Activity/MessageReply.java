package com.example.jinanuniversity.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;
import com.google.gson.Gson;

public class MessageReply extends Activity implements OnClickListener {

	private Button bt_back, bt_default, bt_hold;
	private String title, message, sender, receiver, account;
	private TextView mTitle;
	private EditText mMessage;
	private String timestamp, mReceiver;
	PreferencesHelper preferencesHelper;
	private ProgressDialog mProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_reply);
		
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		sender = preferencesHelper.getString("name", "");
		getDate();
		initView();
		test();
	}

	private void initView() {
		bt_back = (Button) findViewById(R.id.bt_back);
		mTitle = (TextView) findViewById(R.id.my_title);
		mMessage = (EditText) findViewById(R.id.phone_sound);
		bt_default = (Button) findViewById(R.id.bt_default);
		bt_hold = (Button) findViewById(R.id.bt_hold);

		mMessage.setMovementMethod(new ScrollingMovementMethod());
		
		bt_back.setOnClickListener(this);
		bt_default.setOnClickListener(this);
		bt_hold.setOnClickListener(this);

		mTitle.setText("回复 ："+ title);
		mMessage.setText(message);
	}

	private void getDate() {
		title = (getIntent() == null ? "" : getIntent().getStringExtra("title"));
		message = (getIntent() == null ? "" : getIntent().getStringExtra(
				"content"));
		mReceiver = (getIntent() == null ? "" : getIntent().getStringExtra(
				"recerver"));
	}

	@SuppressWarnings("unchecked")
	public void test() {
		@SuppressWarnings("rawtypes")
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> receiverList = new ArrayList();
		receiverList.add(mReceiver);
		map.put("receiverList", receiverList);
		Gson gson = new Gson();
		String gsons = gson.toJson(map);
		receiver = gsons.toString();
	}

	private class imagesSend extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {

			getTime();
			String sign = "";
			String memo = "发送";
			String message = mMessage.getText().toString();
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account
					+ sender + receiver + title + message + memo);
			System.out.println("sign==" + sign);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String resutl = ieasy.sendLive(sign, timestamp, account, sender,
					receiver, title, message, memo);
			if (resutl.equals("noEffect")) {
				System.out.println("发送失败 !");
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if (result) {
				Toast.makeText(MessageReply.this, "恭喜你发送成功！",
						Toast.LENGTH_SHORT).show();
				MessageReply.this.finish();
			} else {
				Toast.makeText(MessageReply.this, "发送失败！", Toast.LENGTH_SHORT)
						.show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}
	}

	/**
	 * 获取时间，data类型
	 */
	private void getTime() {
		String time = ParamsManager.getTimestamp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long times = new Long(time);
		timestamp = format.format(times);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;
		case R.id.bt_default:
			mMessage.setText("");
			break;
		case R.id.bt_hold:
			new imagesSend().execute();
			break;

		default:
			break;
		}
	}

	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("发送中，请稍候...");
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
