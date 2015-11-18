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

//主界面Activity
public class Maintain_xiaodan extends Activity implements Callback,
		OnClickListener {
	// PopupWindow对象
	private PopupWindow selectPopupWindow = null;
	// 自定义Adapter
	private OptionsAdapter optionsAdapter = null;
	// 下拉框选项数据源
	private ArrayList<String> reason_datas = new ArrayList<String>();
	private ArrayList<String> result_data = new ArrayList<String>();
	private ArrayList<String> means_data = new ArrayList<String>();
	// 下拉框依附组件
	private RelativeLayout parent, parent_result, parent_means;
	// 下拉框依附组件宽度，也将作为下拉框的宽度parent_result
	private int pwidth;
	// 文本框
	private EditText et_reason, et_result, et_means;
	// 下拉箭头图片组件
	private ImageView reason_image, result_image, means_image, img_means;
	// 恢复数据源按钮
	// private Button button;
	// 展示所有下拉选项的ListView
	private ListView listView = null;
	// 用来处理选中或者删除下拉项消息
	private Handler handler;
	// 是否初始化完成标志
	private boolean flag = false;
	private String timestamp, account, jobId, troubleReason, dealResult,
			dealMethod;
	PreferencesHelper preferencesHelper;
	private ProgressDialog mProgressDialog;
	private int result;
	private Button close_job, bt_back;
	private String troubleReasonList;
	private String dealResultList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.xiaodan);

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
			initWedget();
			flag = true;
		}
		jobId = preferencesHelper.getString("jobId", "");
		account = preferencesHelper.getString("account", "");

	}

	/**
	 * 初始化界面控件
	 */
	private void initWedget() {
		// 初始化Handler,用来处理消息
		handler = new Handler(Maintain_xiaodan.this);

		// 初始化界面组件
		parent = (RelativeLayout) findViewById(R.id.parent);
		parent_result = (RelativeLayout) findViewById(R.id.parent_result);
		parent_means = (RelativeLayout) findViewById(R.id.parent_means);
		et_reason = (EditText) findViewById(R.id.et_reason);
		et_result = (EditText) findViewById(R.id.et_result);
		et_means = (EditText) findViewById(R.id.et_means);
		reason_image = (ImageView) findViewById(R.id.img_reason);
		result_image = (ImageView) findViewById(R.id.img_result);
		means_image = (ImageView) findViewById(R.id.img_means);
		img_means = (ImageView) findViewById(R.id.img_means);
		close_job = (Button) findViewById(R.id.close_job);
		bt_back = (Button) findViewById(R.id.bt_back);

		reason_image.setOnClickListener(this);
		result_image.setOnClickListener(this);
		means_image.setOnClickListener(this);
		close_job.setOnClickListener(this);
		bt_back.setOnClickListener(this);
		img_means.setOnClickListener(this);

		// 获取下拉框依附的组件宽度
		int width = parent.getWidth();
		pwidth = width;

		// 初始化PopupWindow
		initPopuWindow_reason();
		initPopuWindow_result();

	}

	private class colseJob extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			timestamp = ParamsManager.getTime();
			String sign = "";
			troubleReason = et_reason.getText().toString();
			dealResult = et_result.getText().toString();
			dealMethod = et_means.getText().toString();
			System.out.println("result==" + jobId + troubleReason + "+"
					+ dealResult);
			sign = ParamsManager.getMd5sign(Config.SECRET + Config.APPKEY
					+ timestamp + Config.VER + account + jobId + troubleReason
					+ dealResult + dealMethod);
			System.out.println("sign==" + sign);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String resutl = ieasy.closeJob(Config.APPKEY, sign, timestamp,
					Config.VER, account, jobId, troubleReason, dealResult,
					dealMethod);
			System.out.println("resutl==" + resutl);
			if (resutl.equals("noEffect")) {

			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if (result) {
				Toast.makeText(Maintain_xiaodan.this, "恭喜你消单成功！",
						Toast.LENGTH_SHORT).show();
				Maintain_xiaodan.this.finish();
			} else {
				Toast.makeText(Maintain_xiaodan.this, "消单失败！",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}
	}

	/**
	 * 初始化填充Adapter所用List数据 
	 * 消单原因
	 */
	private void initData_reason() {
		reason_datas.clear();
		troubleReasonList = preferencesHelper.getString("troubleReasonList", "");
		troubleReasonList = troubleReasonList.substring(1,
				troubleReasonList.length() - 1);
		String[] strings = troubleReasonList.split(",");
		for (int i = 0; i < strings.length; i++) {
			reason_datas.add(strings[i].substring(1, strings[i].length() - 1));
		}
	}

	/**
	 * 消单结果
	 */
	private void initData_result() {

		result_data.clear();
		dealResultList = preferencesHelper.getString("dealResultList", "");
		dealResultList = dealResultList.substring(1,
				dealResultList.length() - 1);
		String[] strings = dealResultList.split(",");
		for (int i = 0; i < strings.length; i++) {
			result_data.add(strings[i].substring(1, strings[i].length() - 1));
		}
	}

	/**
	 * 消单方法
	 */
	private void initData_means() {

		means_data.clear();
		dealResultList = preferencesHelper.getString("dealResultList", "");
		dealResultList = dealResultList.substring(1,
				dealResultList.length() - 1);
		String[] strings = dealResultList.split(",");
		for (int i = 0; i < strings.length; i++) {
			means_data.add(strings[i].substring(1, strings[i].length() - 1));
		}
	}

	/**
	 * 初始化PopupWindow 
	 * 弹出原因列表
	 */
	@SuppressWarnings("deprecation")
	private void initPopuWindow_reason() {
		// PopupWindow浮动下拉框布局
		initData_reason();
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(this, handler, reason_datas);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);
		selectPopupWindow.setOutsideTouchable(true);

		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 初始化PopupWindow 
	 * 弹出原因列表
	 */
	@SuppressWarnings("deprecation")
	private void initPopuWindow_result() {
		// PopupWindow浮动下拉框布局
		initData_result();
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(this, handler, result_data);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
				LayoutParams.WRAP_CONTENT, true);
		selectPopupWindow.setOutsideTouchable(true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		selectPopupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	/**
	 * 初始化PopupWindow 弹出方法列表
	 */
	@SuppressWarnings("deprecation")
	private void initPopuWindow_means() {
		// PopupWindow浮动下拉框布局
		initData_means();
		View loginwindow = (View) this.getLayoutInflater().inflate(
				R.layout.options, null);
		listView = (ListView) loginwindow.findViewById(R.id.list);

		// 设置自定义Adapter
		optionsAdapter = new OptionsAdapter(this, handler, means_data);
		listView.setAdapter(optionsAdapter);

		selectPopupWindow = new PopupWindow(loginwindow, pwidth,
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
			if (result == 1) {
				int selIndex = data.getInt("selIndex");
				et_reason.setText(reason_datas.get(selIndex));
			} else if (result == 2) {
				int selIndex = data.getInt("selIndex");
				et_result.setText(result_data.get(selIndex));
			} else {
				int selIndex = data.getInt("selIndex");
				et_means.setText(means_data.get(selIndex));
			}

			dismiss();
			break;
		case 2:
			// 移除下拉项数据
			int delIndex = data.getInt("delIndex");
			reason_datas.remove(delIndex);
			// 刷新下拉列表
			optionsAdapter.notifyDataSetChanged();
			break;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_reason:
			result = 1;
			initPopuWindow_reason();
			selectPopupWindow.showAsDropDown(parent, 0, -3);
			break;
		case R.id.img_result:
			result = 2;
			initPopuWindow_result();
			selectPopupWindow.showAsDropDown(parent_result, 0, -3);
			break;
		case R.id.img_means:
			result = 3;
			initPopuWindow_means();
			selectPopupWindow.showAsDropDown(parent_means, 0, -3);
			break;
		case R.id.close_job:
			new colseJob().execute();
			break;
		case R.id.bt_back:
			finish();
			break;
		}
	}

	private ProgressDialog showProgressDialog() {
		if (mProgressDialog == null) {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setMessage("消单中，请稍候...");
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
