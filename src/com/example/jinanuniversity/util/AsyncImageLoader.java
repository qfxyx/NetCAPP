package com.example.jinanuniversity.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.example.jinanuniversity.data.Service;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

/**
 * 异步图片下载工具类 缓存+线程池+MessageQueue
 * @author Junbin
 */
public class AsyncImageLoader {

	public Map<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	public Drawable loaDrawable(final String imageUrl, final ImageCallBack imageCallBack) {
		Bitmap bmpFromSD = FileCache.getInstance().getBmp(imageUrl);
		if (null != bmpFromSD) {
			return new BitmapDrawable(bmpFromSD);
		}
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			Drawable drawable = softReference.get();
			if (null != drawable) {
				return drawable;
			}
		}

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallBack.imageLoaded((Drawable) message.obj);
			}
		};

		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;
	}
	
	/**
	 * 下载图片
	 */
	public Drawable loadImageFromUrl(String url) {
		URL tempUrl;
		InputStream inputStream = null;
		Drawable drawable = null;

		try {
			tempUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) tempUrl
					.openConnection();
			conn.setRequestProperty("Cookie", "JSESSIONID="
					+ Service.getInstance().getJSESSIONID());
			conn.connect();
			inputStream = conn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			drawable = Drawable.createFromStream(inputStream, "src");
		} catch (OutOfMemoryError err) {
			System.out.println("内存溢出...");
		}
		return drawable;
	}

	public interface ImageCallBack {
		public void imageLoaded(Drawable imageDrawable);
	}
}
