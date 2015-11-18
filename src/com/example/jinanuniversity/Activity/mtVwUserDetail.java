package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MtVwUser;
import com.example.jinanuniversity.json.mtVwUserParser;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

/**
 * 维护员明细
 */
public class mtVwUserDetail extends Activity implements OnClickListener {

	private RelativeLayout reLay_user;
	private TextView name;
	private TextView txt_phone;
	private TextView shortphone;
	private TextView qq;
	private TextView email;
	private Button btn_call;
	private Button btn_info;
	private Button btn_back;
	private ProgressBar refresh_progress;
	private String short_phone;

	private PreferencesHelper preferencesHelper;
	private int code;
	private String account, adminId, phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mt_user_detail);

		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		adminId = getIntent().getStringExtra("adminId") == null ? ""
				: getIntent().getStringExtra("adminId");

		initView();
		new getInfo().execute();
	}

	private void initView() {
		reLay_user = (RelativeLayout) findViewById(R.id.reLay_user);
		name = (TextView) findViewById(R.id.name);
		txt_phone = (TextView) findViewById(R.id.phone);
		shortphone = (TextView) findViewById(R.id.shortphone);
		qq = (TextView) findViewById(R.id.qq);
		email = (TextView) findViewById(R.id.email);
		btn_call = (Button) findViewById(R.id.btn_call);
		btn_info = (Button) findViewById(R.id.btn_info);
		btn_back = (Button) findViewById(R.id.btn_back);
		refresh_progress = (ProgressBar) findViewById(R.id.refresh_progress);
		btn_call.setOnClickListener(this);
		btn_info.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_call:
			dialog();
			break;

		case R.id.btn_info:
			Uri smsToUri = Uri.parse("smsto:" + phone);
			Intent intent2 = new Intent(android.content.Intent.ACTION_SENDTO,
					smsToUri);
			startActivity(intent2);
			break;

		case R.id.btn_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	protected void dialog() {
		Builder builder = new Builder(mtVwUserDetail.this);
		builder.setMessage("拨打电话");

		builder.setTitle("提示");
		builder.setPositiveButton("长号", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + phone));
				startActivity(intent);
			}
		});
		builder.setNegativeButton("短号", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_CALL);
				intent.setData(Uri.parse("tel:" + short_phone));
				startActivity(intent);
			}
		});
		builder.create().show();
	}

	/**
	 * 异步加载数据
	 */
	private class getInfo extends AsyncTask<Void, Void, Integer> {

		MtVwUser mtVwUser;

		@Override
		protected Integer doInBackground(Void... params) {
			String timestamp = ParamsManager.getTime();
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account
					+ adminId);

			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = ieasy.getVwUser(sign, timestamp, account, adminId);
			System.out.println("re==" + re);
			mtVwUser = parser(re);
			return code;
		}

		@Override
		protected void onPostExecute(Integer result) {
			refresh_progress.setVisibility(View.GONE);
			reLay_user.setVisibility(View.VISIBLE);
			if (result == 0) {
				if (mtVwUser != null) {
					name.setText(mtVwUser.getName());
					txt_phone.setText(mtVwUser.getPhone());
					shortphone.setText(mtVwUser.getShortphone());
					qq.setText(mtVwUser.getQq());
					email.setText(mtVwUser.getEmail());
					phone = mtVwUser.getPhone();
					short_phone = mtVwUser.getShortphone();
				}
			} else {
				Toast.makeText(getApplicationContext(), "更新失败",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			refresh_progress.setVisibility(View.VISIBLE);
			reLay_user.setVisibility(View.INVISIBLE);
		}
	}

	/** 解析 Json */
	public MtVwUser parser(String josn) {
		mtVwUserParser parser = new mtVwUserParser(josn);
		code = parser.getCode();
		return parser.praser();
	}
}
