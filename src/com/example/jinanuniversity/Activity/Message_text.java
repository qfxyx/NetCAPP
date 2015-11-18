package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MtMsgParser;
import com.example.jinanuniversity.types.MtMsgType;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

public class Message_text extends Activity implements OnClickListener {

	private Button bt_back, bt_reply;
	private TextView mTitle, mMessage;
	private ProgressBar refresh_progress;

	private PreferencesHelper preferencesHelper;
	private String account, msgId, title, cont, recerver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.message_text);

		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		msgId = getIntent().getStringExtra("msgId") == null ? "" : getIntent()
				.getStringExtra("msgId");

		initView();
		new getInfo().execute();
	}

	private void initView() {
		bt_back = (Button) findViewById(R.id.bt_back);
		mTitle = (TextView) findViewById(R.id.my_title);
		mMessage = (TextView) findViewById(R.id.txt_cont);
		bt_reply = (Button) findViewById(R.id.bt_reply);
		refresh_progress = (ProgressBar) findViewById(R.id.refresh_progress);
		mMessage.setMovementMethod(new ScrollingMovementMethod());

		bt_back.setOnClickListener(this);
		bt_reply.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;

		case R.id.bt_reply:
			Intent intent = new Intent(Message_text.this, MessageReply.class);
			intent.putExtra("title", title);
			intent.putExtra("content", cont);
			intent.putExtra("recerver", recerver);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public class getInfo extends AsyncTask<Void, Void, String> {

		MtMsgType msgType;

		@Override
		protected String doInBackground(Void... params) {
			String timestamp = ParamsManager.getTime();
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account + msgId);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = ieasy.getMsgDetails(sign, timestamp, account, msgId);

			System.out.println("re = " + re);
			msgType = parserList(re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			refresh_progress.setVisibility(View.GONE);
			if (msgType != null) {
				title = msgType.getTitle() == null ? "" : msgType.getTitle();
				cont = msgType.getMessage() == null ? "" : msgType.getMessage();
				recerver = msgType.getReceiver() == null ? "" : msgType.getReceiver();
				mTitle.setText(title);
				mMessage.setText(cont);
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			refresh_progress.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * json解析
	 */
	private MtMsgType parserList(String s) {
		MtMsgParser ww = new MtMsgParser(s);
		return ww.praserList();
	}
}
