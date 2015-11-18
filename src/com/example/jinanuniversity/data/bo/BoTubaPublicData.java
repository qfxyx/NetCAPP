package com.example.jinanuniversity.data.bo;

import com.example.jinanuniversity.data.Config;

import android.os.Environment;

public class BoTubaPublicData {
	/**是否为调试模式*/
	public static final boolean DEBUG = false;
	/**使用哪里的服务器*/
	public static String SERVICE = "http://netcapi1.jnu.edu.cn/netcapi/";
	/**欢迎界面等待时间*/
	public static final int BOWELCOMEDELAY = 2000;
	/**是否在后http连接加上网络类型*/
	public static boolean ADDNETTYPE = false;
	/**手机号码*/
	public static String pnumber = "13800138000";
	/**网络类型*/
	public static String pnettype = "wifi";
	/**列表中每一页大小*/
	public static int pageSize = 10;
	/**客户端*/
	public static String appType = "boTuba_android";
	/**隐藏web页面*/
	public static  boolean addWebTag = false;
	/**图片评论每一页的大小*/
	public static  int  commentpageSize = 10;
	/**本地代理地址*/
	public static String localURL = "file:///"+Environment.getExternalStorageDirectory()+"/botuba/buffer/";
	/** 图片后缀 */
	public static final String imgSuffix = ".botuba";
	
//	public static final String KEY_USERNAME = "username";
//	public static final String KEY_USERPSW = "userpsw";
//	public static final String KEY_USERID = "userid";
}
