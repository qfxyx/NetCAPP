package com.example.jinanuniversity.Activity;

import com.example.jinanuniversity.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Window;

public class WelcomeActivity extends Activity {
	private static final String TAG = "Activity.WelcomeActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate() start");
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
					System.out.println("WelcomeActivity has a Bug.");
				} finally {
					startActivity(new Intent(WelcomeActivity.this,LandingActivity.class));
					WelcomeActivity.this.finish();
				}
			}
		}.start();
		Log.i(TAG, "onCreate() end");
	}

}
