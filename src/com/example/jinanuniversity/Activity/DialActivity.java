package com.example.jinanuniversity.Activity;

import java.lang.reflect.Method;

import com.example.jinanuniversity.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class DialActivity extends Activity implements OnClickListener {

	private static final String TAG = "Activity.DialActivity";
	private String[] phoneNums;
	private Spinner userPhoneNumSpinner;
	private Button dialButton;
	private ArrayAdapter<String> userPhoneAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dial_activity);
		phoneNums = getIntent().getStringArrayExtra("phoneNumsArray") == null ? null
				: getIntent().getStringArrayExtra("phoneNumsArray");
		initView();
		Log.i(TAG, "onCreate end");
	}

	private void initView() {
		userPhoneNumSpinner = (Spinner) findViewById(R.id.dial_phonenum_scope);
		dialButton = (Button) findViewById(R.id.dial_button);
		dialButton.setOnClickListener(this);
		userPhoneAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, phoneNums);
		userPhoneAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		userPhoneNumSpinner.setAdapter(userPhoneAdapter);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.dial_button:
			dial(phoneNums[userPhoneNumSpinner.getSelectedItemPosition()]);
			break;
		}
	}

	/**
	 * 弹出拨打电话界面
	 */
	private void dial(String number) {
		Class<TelephonyManager> c = TelephonyManager.class;
		Method getITelephonyMethod = null;
		try {
			getITelephonyMethod = c.getDeclaredMethod("getITelephony",
					(Class[]) null);
			getITelephonyMethod.setAccessible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			Object iTelephony;
			iTelephony = (Object) getITelephonyMethod.invoke(tManager,
					(Object[]) null);
			Method dial = iTelephony.getClass().getDeclaredMethod("dial",
					String.class);
			dial.invoke(iTelephony, number);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
