package com.example.jinanuniversity.Activity;

import com.example.jinanuniversity.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MenuActivity extends TabActivity {

	private RadioGroup group;
	private TabHost tabHost;
	private RadioButton radio_button1;

	//public static final String Message = "Message";
	public static final String Maintenance = "Maintenance";
	public static final String Maintenance_inquire = "Maintenance_inquire";
	public static final String User = "User";
	public static final String Ohter = "Ohter";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.menu);

		group = (RadioGroup) findViewById(R.id.main_radio);
		tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec(Maintenance)
				.setIndicator(Maintenance)
				.setContent(new Intent(this, MaintainActivity.class)));
		tabHost.addTab(tabHost.newTabSpec(Maintenance_inquire)
				.setIndicator(Maintenance_inquire)
				.setContent(new Intent(this, Maintenance_inquire.class)));
		tabHost.addTab(tabHost.newTabSpec(User).setIndicator(User)
				.setContent(new Intent(this, UserActivity.class)));
		tabHost.addTab(tabHost.newTabSpec(Ohter).setIndicator(Ohter)
				.setContent(new Intent(this, OtherActivity.class)));

		//tab 设置在InformationActivity这里
		radio_button1 = (RadioButton) findViewById(R.id.radio_button1);
		radio_button1.setChecked(true);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_button1:
					tabHost.setCurrentTabByTag(Maintenance);
					break;

				case R.id.radio_button2:
					tabHost.setCurrentTabByTag(Maintenance_inquire);
					break;

				case R.id.radio_button3:
					tabHost.setCurrentTabByTag(User);
					break;

				case R.id.radio_button4:
					tabHost.setCurrentTabByTag(Ohter);
					break;
				}
			}
		});
	}


	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
			// TODO Auto-generated method stub
			if(event.getKeyCode() == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN) {
				moveTaskToBack(false);
				return true;
		}
		return super.dispatchKeyEvent(event);

	}

}
