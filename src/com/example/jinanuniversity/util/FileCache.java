package com.example.jinanuniversity.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 缓存文件管理
 * @author Junbin
 */
public class FileCache {
	private static FileCache fileCache; // 本类的引用
	private String strImgPath; // 图片保存的路径

	private FileCache() {
		// this.context = context;
		String strPathHead = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			strPathHead = Environment.getExternalStorageDirectory().toString();
		} else {
			strPathHead = "/data/data/com.listen";
		}
		strImgPath = strPathHead + "/jnu/cache/";
	}

	public static FileCache getInstance() {
		if (null == fileCache) {
			fileCache = new FileCache();
		}
		return fileCache;
	}

	// 用图片的URL来命名图片，并保存图片
	public boolean savaBmpData(String imgurl, Bitmap bmp) {
		String imgName = null;
		try {
			imgName = imgurl.substring(imgurl.lastIndexOf('/') + 1,
					imgurl.length());
		} catch (Exception e) {
			System.out.println("FileCache --> 图片命名错误");
			return false;
		}
		File imgFileDirs = new File(strImgPath);
		if (!imgFileDirs.exists()) {
			imgFileDirs.mkdirs();
		}
		File fImg = new File(strImgPath + imgName);
		if (fImg.exists()) {
			fImg.delete();
		}
		this.writeToFile(bmp, fImg);
		return true;
	}

	/**
	 * 传入图片的URL地址，来获得Bitmap
	 * */
	public Bitmap getBmp(String imgurl) {

		String imgName = null;
		try {
			imgName = imgurl.substring(imgurl.lastIndexOf('/') + 1,
					imgurl.length());
		} catch (Exception e) {
			System.out.println("FileCache --> 图片地址错误");
			return null;
		}

		File imgFile = new File(strImgPath + imgName);
		if (imgFile.exists()) {
			FileInputStream fis;
			try {
				fis = new FileInputStream(imgFile);
				return BitmapFactory.decodeStream(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else
			Log.v("提醒", "要请求的图片文件不存在");
		return null;
	}

	private boolean writeToFile(Bitmap bmp, File file) {
		if (file.exists()) {
			file.delete();
		}
		String name = file.getName();
		String geShi = null;
		try {
			geShi = name.substring(name.lastIndexOf('.'), name.length());
		} catch (Exception e) {
			System.out.println("FileCache --> 图片文件错误");
			return false;
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			if (null != bmp) {
				if (".JPEG".equalsIgnoreCase(geShi)
						|| ".JPG".equalsIgnoreCase(geShi)) {
					bmp.compress(CompressFormat.JPEG, 100, bos);
					bos.flush();
					bos.close();
				} else if (".PNG".equalsIgnoreCase(geShi)) {
					bmp.compress(CompressFormat.PNG, 100, bos);
					bos.flush();
					bos.close();
				}
				return true;
			} else
				bos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					Log.v("错误", "图片写入缓存文件错误");
				}
			}
		}
		return false;
	}
}
