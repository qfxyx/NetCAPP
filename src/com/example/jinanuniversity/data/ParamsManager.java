package com.example.jinanuniversity.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.jinanuniversity.types.PicPageType;
import com.example.jinanuniversity.util.MD5;

import android.app.Application;

public class ParamsManager extends Application {

	private int id;
	private ArrayList<PicPageType> picPageList = new ArrayList<PicPageType>();
	private int page;
	public boolean isRefresh = false;
	public String storePassword="";

	public int getId() {
		return id;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public ArrayList<PicPageType> getPicPageList() {
		return picPageList;
	}

	public void setPicPageList(ArrayList<PicPageType> picPageList) {
		this.picPageList = picPageList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public static String getTimestamp() {
		Date date = new Date();
		String timestamp = String.valueOf(date.getTime()/*
														 * + 8 * 60 60 * 1000
														 */);
		return timestamp;
	}

	public static String getMd5sign(String sign) {
		MD5 md5 = new MD5();
		String Md5sign = md5.getMD5ofStr(sign).toLowerCase();
		return Md5sign;
	}

	// 对称加密
	// 加密
	public static String enCode(String str) {

		String resultStr;
		// System.out.println(str);
		// System.out.println(str.substring(0, 3));
		// System.out.println(str.substring(3, str.length()));

		resultStr = str.substring(3, str.length()) + str.substring(0, 3);
		// System.out.println(resultStr);
		return resultStr;
	}

	// 解密
	public static String deCode(String str) {

		String resultStr;
		// System.out.println(str);
		// System.out.println(str.substring(0, str.length()-3));
		// System.out.println(str.substring(str.length()-3, str.length()));

		resultStr = str.substring(str.length() - 3, str.length())
				+ str.substring(0, str.length() - 3);
		// System.out.println(resultStr);
		return resultStr;
	}

	/**
	 * 获取时间，data类型
	 */
	public static String getTime() {
		String time = ParamsManager.getTimestamp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long times = new Long(time);
		return format.format(times);
	}

	public void setRefresh(boolean isRefresh) {
		this.isRefresh = isRefresh;
	}

	public boolean isRefresh() {
		return isRefresh;
	}

	public void setStorePassword(String password){
		this.storePassword=password;
	}
	public String getStorePassword(){
		return storePassword;
	}
}
