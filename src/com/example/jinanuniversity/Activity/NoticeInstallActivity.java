package com.example.jinanuniversity.Activity;

import java.io.IOException;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.PreferencesHelper;

public class NoticeInstallActivity extends Activity implements OnClickListener {

	private Button bt_back, bt_hold;
	private CheckBox phone_shock, phone_sound, phone_top;
	private Vibrator vibrator = null;
	private boolean shouldPlayBeep;
	MediaPlayer mediaPlayer = new MediaPlayer();
	PreferencesHelper preferencesHelper;
	private static final int ID = 1213;
	private static final String KEY_COUNT = "keyCount";
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setContentView(R.layout.notice_setup);
		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		initView();
		sound();
		measure(phone_shock, null, "shock");
		measure(phone_sound, null, "sound");
		measure(phone_top, null, "top");
	}

	/**
	 * @param value
	 *            状态
	 * @param checkBox
	 *            控件
	 * @param radioButton
	 *            控件
	 * @param name
	 */
	private void measure(CheckBox checkBox, RadioButton radioButton, String name) {
		if (checkBox != null) {
			String value = "yes";
			String message = preferencesHelper.getString(name, "no");
			if (message.equals(value)) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}
		}
	}

	private void setMeasure(String value, CheckBox checkBox, String name) {
		preferencesHelper.setString(name, value);
	}

	private void initView() {
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		bt_back = (Button) findViewById(R.id.bt_back);
		bt_hold = (Button) findViewById(R.id.bt_hold);
		phone_shock = (CheckBox) findViewById(R.id.phone_shock);
		phone_sound = (CheckBox) findViewById(R.id.phone_sound);
		phone_top = (CheckBox) findViewById(R.id.phone_top);

		bt_back.setOnClickListener(this);
		bt_hold.setOnClickListener(this);
		phone_shock.setOnClickListener(this);
		phone_sound.setOnClickListener(this);
		phone_top.setOnClickListener(this);

		/** 震动按键 */

		phone_shock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = phone_shock.isChecked();
				String shock = "";
				if (isChecked) {
					shock = "yes";
					vibrator.vibrate(new long[] { 100, 1000, 50, 100, 50 }, -1);
					System.out.println("震动");
				} else {
					shock = "no";
					vibrator.cancel();
					System.out.println("关闭震动");
				}

				setMeasure(shock, phone_shock, "shock");
			}
		});
		/** 声音按键 */
		phone_sound.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = phone_sound.isChecked();
				String sound = "";
				if (isChecked) {
					sound = "yes";
					if (mediaPlayer != null) {
						mediaPlayer.start();
					}
				} else {
					sound = "no";
				}

				setMeasure(sound, phone_sound, "sound");
			}
		});
		/** 顶部提示按键 */
		phone_top.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				boolean isChecked = phone_top.isChecked();
				String top = "";
				if (isChecked) {
					top = "yes";
					AddNotification();
					NoticeInstallActivity.this.finish();
				} else {
					top = "no";
				}

				setMeasure(top, phone_top, "top");
			}
		});

	}

	private void sound() {
		AudioManager audioService = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			shouldPlayBeep = false;
		}

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer player) {
						player.seekTo(0);
					}
				});

		AssetFileDescriptor file = this.getResources().openRawResourceFd(
				R.raw.beep);
		try {
			mediaPlayer.setDataSource(file.getFileDescriptor(),
					file.getStartOffset(), file.getLength());
			file.close();
			mediaPlayer.prepare();
		} catch (IOException ioe) {
			mediaPlayer = null;
		}
		return;

	}

	/**
	 * 添加顶部通知
	 * 
	 * @author liuzhao
	 */
	public void AddNotification() {
		count++;
		// 添加通知到顶部任务栏
		// 获得NotificationManager实例
		String service = NOTIFICATION_SERVICE;
		NotificationManager nm = (NotificationManager) getSystemService(service);
		// 实例化Notification
		Notification n = new Notification();
		// 设置显示图标
		int icon = R.drawable.ic_launcher;
		// 设置提示信息
		String tickerText = "我的程序";
		// 显示时间
		long when = System.currentTimeMillis();

		n.icon = icon;
		n.tickerText = tickerText;
		n.when = when;
		// 显示在“正在进行中”
		// n.flags = Notification.FLAG_ONGOING_EVENT;
		n.flags |= Notification.FLAG_AUTO_CANCEL; // 自动终止
		// 实例化Intent
		Intent it = new Intent(this, NoticeInstallActivity.class);
		it.putExtra(KEY_COUNT, count);
		/*********************
		 * 获得PendingIntent FLAG_CANCEL_CURRENT: 如果当前系统中已经存在一个相同的PendingIntent对象，
		 * 那么就将先将已有的PendingIntent取消，然后重新生成一个PendingIntent对象。 FLAG_NO_CREATE:
		 * 如果当前系统中不存在相同的PendingIntent对象， 系统将不会创建该PendingIntent对象而是直接返回null。
		 * FLAG_ONE_SHOT: 该PendingIntent只作用一次， 如果该PendingIntent对象已经触发过一次，
		 * 那么下次再获取该PendingIntent并且再触发时，
		 * 系统将会返回一个SendIntentException，在使用这个标志的时候一定要注意哦。 FLAG_UPDATE_CURRENT:
		 * 如果系统中已存在该PendingIntent对象， 那么系统将保留该PendingIntent对象，
		 * 但是会使用新的Intent来更新之前PendingIntent中的Intent对象数据，
		 * 例如更新Intent中的Extras。这个非常有用， 例如之前提到的，我们需要在每次更新之后更新Intent中的Extras数据，
		 * 达到在不同时机传递给MainActivity不同的参数，实现不同的效果。
		 *********************/

		PendingIntent pi = PendingIntent.getActivity(this, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// 设置事件信息，显示在拉开的里面
		n.setLatestEventInfo(NoticeInstallActivity.this,
				"我的软件" + Integer.toString(count), "我的软件正在运行……", pi);

		// 发出通知
		nm.notify(ID, n);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_back:
			finish();
			break;
		case R.id.bt_hold:
			System.out.println("保存中");
			break;
		// case R.id.phone_shock:
		// if (phone_shock.isChecked()) {
		// // 根据指定的模式进行震动
		// // 第一个参数：该数组中第一个元素是等待多长的时间才启动震动，
		// // 之后将会是开启和关闭震动的持续时间，单位为毫秒
		// // 第二个参数：重复震动时在pattern中的索引，如果设置为-1则表示不重复震动
		// vibrator.vibrate(new long[] { 100, 1000, 50, 100, 50 }, -1);
		// System.out.println("震动");
		// } else {
		// // 关闭震动
		// vibrator.cancel();
		// System.out.println("关闭震动");
		// }
		// break;
		// case R.id.phone_sound:
		// if (phone_sound.isChecked()) {
		// if (mediaPlayer != null) {
		// mediaPlayer.start();
		// }
		//
		// }
		// break;

		default:
			break;
		}
	}

}
