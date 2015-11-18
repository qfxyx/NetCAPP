package com.example.jinanuniversity.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.data.ParamsManager;
import com.example.jinanuniversity.data.PreferencesHelper;
import com.example.jinanuniversity.json.MtPicParser;
import com.example.jinanuniversity.types.MtPicType;
import com.example.jinanuniversity.util.AsyncImageLoader;
import com.example.jinanuniversity.util.AsyncImageLoader.ImageCallBack;
import com.example.jinanuniversity.util.FileCache;
import com.example.jinanuniversity.util.IEasy;
import com.example.jinanuniversity.util.IEasyHttpApiV1;

public class PhotoDetails extends Activity {

	private TextView img_txt;
	private ImageView img_details;
	private ProgressBar refresh_progress;
	private Button btn_back;

	private PreferencesHelper preferencesHelper;
	private String account, picId;
	private boolean isShow = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.photo_details);

		preferencesHelper = new PreferencesHelper(getApplicationContext(),
				PreferencesHelper.LOGININFO);
		account = preferencesHelper.getString("account", "");
		picId = getIntent().getStringExtra("picId") == null ? "" : getIntent()
				.getStringExtra("picId");

		initView();
		new getInfo().execute();
	}

	private void initView() {
		img_txt = (TextView) findViewById(R.id.img_txt);
		btn_back = (Button) findViewById(R.id.btn_back);
		img_txt.setMovementMethod(new ScrollingMovementMethod());
		refresh_progress = (ProgressBar) findViewById(R.id.refresh_progress);
		img_details = (ImageView) findViewById(R.id.img_details);

		img_txt.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				isShow = true;
				img_txt.setVisibility(ViewGroup.INVISIBLE);
			}
		});

		img_details.setOnTouchListener(new TounchListener());
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public class getInfo extends AsyncTask<Void, Void, String> {

		MtPicType mtPicType;

		@Override
		protected String doInBackground(Void... params) {
			String timestamp = ParamsManager.getTime();
			String sign = ParamsManager.getMd5sign(Config.SECRET
					+ Config.APPKEY + timestamp + Config.VER + account + picId);
			IEasy ieasy = new IEasy(new IEasyHttpApiV1());
			String re = ieasy.getPicDetails(sign, timestamp, account, picId);

			System.out.println("re = " + re);
			mtPicType = parserList(re);
			return re;
		}

		@Override
		protected void onPostExecute(String result) {
			refresh_progress.setVisibility(View.GONE);
			if (mtPicType != null) {
				img_txt.setText(mtPicType.getDetail() == null ? "" : mtPicType
						.getDetail());
				loadPIc(mtPicType.getBigurl());
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
	private MtPicType parserList(String s) {
		MtPicParser ww = new MtPicParser(s);
		return ww.praserList();
	}

	public void loadPIc(final String url) {
		AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
		refresh_progress.setVisibility(View.VISIBLE);
		if (url == null) {
			Bitmap defaultbmp = BitmapFactory.decodeResource(null,
					R.drawable.photo_loading_bg);
			img_details.setImageBitmap(defaultbmp);
		} else {
			Bitmap bitmap = FileCache.getInstance().getBmp(url);
			if (bitmap != null) {
				img_details.setImageBitmap(bitmap);
				refresh_progress.setVisibility(View.GONE);
			} else {
				Drawable drawable = asyncImageLoader.loaDrawable(url,
						new ImageCallBack() {
							public void imageLoaded(Drawable imageDrawable) {
								Bitmap bitmap = drawToBmp(imageDrawable);
								FileCache.getInstance()
										.savaBmpData(url, bitmap);
								if (bitmap != null) {
									img_details.setImageBitmap(bitmap);
									refresh_progress.setVisibility(View.GONE);
								}
							}
						});
				if (drawable == null) {
					Bitmap defaultbmp = BitmapFactory.decodeResource(null,
							R.drawable.photo_loading_bg);
					img_details.setImageBitmap(defaultbmp);
				} else {
					bitmap = drawToBmp(drawable);
					img_details.setImageBitmap(bitmap);
					refresh_progress.setVisibility(View.GONE);
				}
			}
		}
	}

	public Bitmap drawToBmp(Drawable d) {
		if (d != null) {
			BitmapDrawable bd = (BitmapDrawable) d;
			return bd.getBitmap();
		}
		return null;
	}

	private class TounchListener implements OnTouchListener {

		private PointF startPoint = new PointF();
		private Matrix matrix = new Matrix();
		private Matrix currentMaritx = new Matrix();

		private int mode = 0;// 用于标记模式
		private static final int DRAG = 1;// 拖动
		private static final int ZOOM = 2;// 放大
		private float startDis = 0;
		private PointF midPoint;// 中心点
		private int x, y;

		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				mode = DRAG;
				currentMaritx.set(img_details.getImageMatrix()); // 记录ImageView当期的移动位置
				startPoint.set(event.getX(), event.getY()); // 开始点
				break;

			case MotionEvent.ACTION_MOVE:// 移动事件
				x = (int) (event.getX() - startPoint.x);
				y = (int) (event.getY() - startPoint.y);
				if (mode == DRAG) { // 图片拖动事件
					float dx = event.getX() - startPoint.x; // x轴移动距离
					float dy = event.getY() - startPoint.y;

					matrix.set(currentMaritx); // 在当前的位置基础上移动
					matrix.postTranslate(dx, dy);
				} else if (mode == ZOOM) { // 图片放大事件
					float endDis = distance(event); // 结束距离
					if (endDis > 10f) {
						float scale = endDis / startDis; // 放大倍数
						matrix.set(currentMaritx);
						matrix.postScale(scale, scale, midPoint.x, midPoint.y);
					}
				}
				break;

			case MotionEvent.ACTION_UP:
				if (x == 0 && y == 0) {
					if (isShow) {
						img_txt.setVisibility(View.VISIBLE);
						isShow = false;
					} else {
						img_txt.setVisibility(View.INVISIBLE);
						isShow = true;
					}
				}
				x = y = 0;
				mode = 0;
				break;

			// 有手指离开屏幕，但屏幕还有触点(手指)
			case MotionEvent.ACTION_POINTER_UP:
				mode = 0;
				break;

			// 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
			case MotionEvent.ACTION_POINTER_DOWN:
				mode = ZOOM;
				startDis = distance(event);

				if (startDis > 10f) {
					midPoint = mid(event);
					currentMaritx.set(img_details.getImageMatrix()); // 记录当前的缩放倍数
				}
				break;
			}
			img_details.setImageMatrix(matrix);
			return true;
		}
	}

	/**
	 * 两点之间的距离
	 */
	private static float distance(MotionEvent event) {
		// 两根线的距离
		float dx = event.getX(1) - event.getX(0);
		float dy = event.getY(1) - event.getY(0);
		return FloatMath.sqrt(dx * dx + dy * dy);
	}

	/**
	 * 计算两点之间中心点的距离
	 */
	private static PointF mid(MotionEvent event) {
		float midx = event.getX(1) + event.getX(0);
		float midy = event.getY(1) - event.getY(0);
		return new PointF(midx / 2, midy / 2);
	}
}
