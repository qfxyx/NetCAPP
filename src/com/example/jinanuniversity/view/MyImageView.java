package com.example.jinanuniversity.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.example.jinanuniversity.R;
import com.example.jinanuniversity.data.Service;

/**
 * 实现 ImageView 图片异步下载
 * @author Junbin
 */
public class MyImageView extends ImageView {
	private String PicFilePath = "/sdcard/jnu/cache/";
	private String filename;
	private Bitmap bm = null;
	private int newWidth;
	private int newHeight;

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 加载网络图片并缩放 newWidth 为 0， newHeight不为0时，按比例缩放
	 * 
	 * @param url
	 *            图片地址
	 * @param newWidth
	 *            0 为不缩放
	 * @param newHeight
	 *            0 为不缩放
	 */
	public void setUrl(String url, int newWidth, int newHeight) {
		this.newWidth = newWidth;
		this.newHeight = newHeight;
		if (newWidth != 0) {
			PicFilePath = "/sdcard/jnu/pic/";
		}
		File file = new File(PicFilePath);
		if (!file.exists())
			file.mkdir();

		if (url != null) {
			bm = BitmapFactory.decodeResource(getResources(),
					R.drawable.photo_loading_bg);
			setImageBitmap(bm);
		}
		try {
			filename = String.valueOf(url.hashCode()) + ".jpg";
			filename = PicFilePath + filename;
			File f = new File(filename);
			if (f.exists()) {
				bm = BitmapFactory.decodeFile(filename);
				setImageBitmap(getRoundedCornerBitmap(bm, 12));
			} else
				new DownLoadPhotoWork(url, filename).execute();
		} catch (Exception e) {
			System.out.println("获取图片出错");
		}
	}

	private class DownLoadPhotoWork extends AsyncTask<Void, Void, Boolean> {

		private String fileName = "";
		private String url = "";
		private Bitmap bitmap = null;

		public DownLoadPhotoWork(String url, String filename) {
			this.fileName = filename;
			this.url = url;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			URL uri;
			try {
				uri = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) uri
						.openConnection();
				conn.setRequestProperty("Cookie", "JSESSIONID="
						+ Service.getInstance().getJSESSIONID());
				conn.connect();
				InputStream is = conn.getInputStream();
				if (is != null) {
					bitmap = getRoundedCornerBitmap(
							BitmapFactory.decodeStream(is), 12);
				}
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			File destDir = new File(PicFilePath);

			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			File f = new File(fileName);
			FileOutputStream fOut = null;
			try {
				if (bitmap == null) {
					setVisibility(View.GONE);
				} else {
					f.createNewFile();
					fOut = new FileOutputStream(f);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
					fOut.flush();
					fOut.close();
					setImageBitmap(bitmap);
					setVisibility(View.VISIBLE);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Bitmap getRoundedCornerBitmap(Bitmap bitmap, int round) {
		if (bitmap == null)
			return null;
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), android.graphics.Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = round;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		if (newWidth == 0) {
			return bitmap;
		} else {
			return zoomImg(bitmap, newWidth, newHeight);
		}
	}

	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		if (newHeight == 0) {
			matrix.postScale(scaleWidth, scaleWidth);
		} else {
			matrix.postScale(scaleWidth, scaleHeight);
		}
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}
}
