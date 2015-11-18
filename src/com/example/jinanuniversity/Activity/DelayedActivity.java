package com.example.jinanuniversity.Activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.adapter.OptionsAdapter;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

public class DelayedActivity extends Activity implements OnClickListener,
		Callback {

	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	// 自定义Adapter
	private OptionsAdapter optionsAdapter = null;
	// 下拉框选项数据源
	private ArrayList<String> help_datas = new ArrayList<String>();
	private ArrayList<String> reason_data = new ArrayList<String>();
	// 下拉框依附组件
	private RelativeLayout parent, parent_help;
	// 下拉框依附组件宽度，也将作为下拉框的宽度parent_result
	private int pwidth, pwidth1;
	// 文本框
	private EditText et_result, et_means;
	// 下拉箭头图片组件
	private ImageView reason_image, result_image, means_image;
	// 恢复数据源按钮
	// private Button button;
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	// 用来处理选中或者删除下拉项消息
	private Handler handler;
	// 是否初始化完成标志
	private boolean flag = false;
	private boolean result = true;

	private Button mDelayed, mHelp, bt_back;
	PreferencesHelper preferencesHelper;
	private ProgressDialog mProgressDialog;
	private String account, jobId, delayReason, passReason;
	private EditText et_reason, et_help;
	private String passReasonList;
	private String delayReasonList;
	private ImageView img_reason, img_help;
	private String delay = "延时中，请稍等...", Play = "请求支援中，请稍后...";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.delayed);

	}

	/**
	 * 没有在onCreate方法中调用initWedget()，而是在onWindowFocusChanged方法中调用，
	 * 是因为initWedget()中需要获取PopupWindow浮动下拉框依附的组件宽度，在onCreate方法中是无法获取到该宽度的
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		while (!flag) {
			initView();
			flag = true;
		}
		jobId = preferencesHelper.getString("jobId", "");
		account = preferencesHelper.getString("account", "");

	}

	private void initView() {
		// 初始化Handler,用来处理消息
		handler = new Handler(DelayedActivity.this);

		parent = (RelativeLayout) findViewById(R.id.parent);
		parent_help = (RelativeLayout) findViewById(R.id.parent_help);

		mDelayed = (Button) findViewById(R.id.delayed);
		mHelp = (Button) findViewById(R.id.bt_help);
		et_reason = (EditText) findViewById(R.id.et_reason);
		et_help = (EditText) findViewById(R.id.et_help);
		bt_back = (Button) findViewById(R.id.bt_back);
		img_reason = (ImageView) findViewById(R.id.img_reason);
		img_help = (ImageView) findViewById(R.id.img_help);

		mDelayed.setOnClickListener(this);
		mHelp.setOnClickListener(this);
		bt_back.setOnClickListener(this);
		img_reason.setOnClickListener(this);
		img_help.setOnClickListener(this);

		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;
		int width1 = parent_help.getWidth();
		pwidth1 = width1;

		// 初始化PopupWindow
		initPopuWindow();
		initPopuWindow1();

	}

	/**
	 * @author luxun 延时
	 * 
	 */
	private class getDelayed extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			String timestamp = ParamsManager.getTime();
			String sign = "";
			delayReason = et_reason.getText().toString();
			System.out.println("delayReason==" + delayReason);
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account + jobId + delayReason);
			System.out.println("sign==" + sign);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String resutl = ieasy.getDelayed(Config.APPKEY, sign, timestamp,
					Config.VER, account, jobId, delayReason);
			System.out.println("resutl==" + resutl);
			if (resutl.equals("noEffect")) {
				System.out.println("发送失败 !");
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if (result) {
				Toast.makeText(DelayedActivity.this, "恭喜你延时成功！",
						Toast.LENGTH_SHORT).show();
				DelayedActivity.this.finish();
			} else {
				Toast.makeText(DelayedActivity.this, "延时失败！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog(delay);
		}
	}

	/**
	 * @author luxun 请求支援
	 * 
	 */
	private class getPass extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			String timestamp = ParamsManager.getTime();
			String sign = "";
			String passMemo = "请求支援";
			passReason = et_help.getText().toString();
			System.out.println("passReason==" + passReason);
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account + jobId + passReason
					+ passMemo);
			System.out.println("sign==" + sign);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String resutl = ieasy.getPass(Config.APPKEY, sign, timestamp,
					Config.VER, account, jobId, passReason, passMemo);
			System.out.println("resutl==" + resutl);
			if (resutl.equals("noEffect")) {
				System.out.println("发送失败 !");
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if (result) {
				Toast.makeText(DelayedActivity.this, "恭喜你请求成功！",
						Toast.LENGTH_SHORT).show();
				DelayedActivity.this.finish();
			} else {
				Toast.makeText(DelayedActivity.this, "请求失败！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog(Play);
		}
	}

	/**
	 * 请求支援
	 */
	private void initData_help() {
		passReasonList = preferencesHelper.getString("passReasonList", "");
		help_datas.clear();

		passReasonList = passReasonList.substring(1,
				passReasonList.length() - 1);
		String[] strings = passReasonList.split(",");
		for (int i = 0; i < strings.length; i++) {
			help_datas.add(strings[i].substring(1, strings[i].length() - 1));
		}
	}

	/**
	 * 延时原因
	 */
	private void initData_reason() {
		reason_data.clear();
		delayReasonList = preferencesHelper.getString("delayReasonList", "");
		delayReasonList = delayReasonList.substring(1,
				delayReasonList.length() - 1);
		String[] strings = delayReasonList.split(",");
		for (int i = 0; i < strings.length; i++) {
			reason_data.add(strings[i].substring(1, strings[i].length() - 1));
		}
	}

	/**
	 * 初始化PopupWindow 延时原因
	 */
	@SuppressWarnings("deprecation")
	private void initPopuWindow() {
		// PopupWindow浮动下拉框布局
		initData_reason();
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(this, handler, reason_data);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);
		selectPopupWindow.setOutsideTouchable(true);

		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 初始化PopupWindow 请求支援
	 */
	@SuppressWarnings("deprecation")
	private void initPopuWindow1() {
		// PopupWindow浮动下拉框布局
		initData_help();
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(this, handler, help_datas);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth1,
				LayoutParams.WRAP_CONTENT, true);
		selectPopupWindow.setOutsideTouchable(true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * PopupWindow消失
	 */
	public void dismiss() {
		selectPopupWindow.dismiss();
	}

	/**
	 * 处理Hander消息
	 */
	@Override
	public boolean handleMessage(Message message) {
		Bundle data = message.getData();
		switch (message.what) {
		case 1:
			// 选中下拉项，下拉框消失
			if (result) {
				int selIndex = data.getInt("selIndex");
				et_reason.setText(reason_data.get(selIndex));
			} else {
				int selIndex = data.getInt("selIndex");
				et_help.setText(help_datas.get(selIndex));
			}
			dismiss();
			break;
		case 2:
			// 移除下拉项数据
			int delIndex = data.getInt("delIndex");
			help_datas.remove(delIndex);
			// 刷新下拉列表
			optionsAdapter.notifyDataSetChanged();
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.delayed:
			new getDelayed().execute();
			break;
		case R.id.bt_help:
			new getPass().execute();
			break;
		case R.id.bt_back:
			this.finish();
			break;
		case R.id.img_help:
			result = false;
			initPopuWindow1();
			selectPopupWindow.showAsDropDown(parent_help, 0, -3);
			break;
		case R.id.img_reason:
			result = true;
			initPopuWindow();
			selectPopupWindow.showAsDropDown(parent, 0, -3);
			break;
		}
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
		} catch (IllegalArgumentException e) {

		}
	}
}
