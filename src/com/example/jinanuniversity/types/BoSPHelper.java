package com.example.jinanuniversity.types;

import android.content.Context;
import android.content.SharedPreferences;

public class BoSPHelper {
	public final static String TAG = "BoSPHelper";
	public final static String FILENAME = "boframework";
	public static int readSelect = 0;
	 /*删除all记录*/
    public boolean delSaveall(Context c)
    {
    	SharedPreferences settings = c.getSharedPreferences(FILENAME,Context.MODE_WORLD_WRITEABLE);
    	settings.edit().clear().commit();
    	return true;
    }
    
    /*保存记录*/
	public static void saveMark(String record, Context c)
    {
    	SharedPreferences settings = c.getSharedPreferences(FILENAME,0);
    	settings.edit()
    	.putString(Integer.toString(getSavesize(c)),record)
    	.commit();
    }
	
	/*保存记录*/
	public static void saveMarkByKey(String key , String record, Context c)
    {
    	SharedPreferences settings = c.getSharedPreferences(FILENAME,0);
    	settings.edit()
    	.putString(key,record)
    	.commit();
    }
	
	 /*更新记录*/
	public static void updateMark(String key , String record, Context c)
    {
    	SharedPreferences settings = c.getSharedPreferences(FILENAME,0);
    	settings.edit()
    	.putString(key,record)
    	.commit();
    }
	
    /*读取记录*/
    public static String getMark(String key , Context c)
    {
    	String temp = "0";
    	SharedPreferences settings = c.getSharedPreferences(FILENAME,0);
    	temp=settings.getString(key,temp);
    	settings.edit().commit();
    	return temp;
    }
	
	/*记录大小*/
    public static int getSavesize(Context c)
    {
    	int temp = 0;
    	SharedPreferences settings = c.getSharedPreferences(FILENAME,0);
    	temp=settings.getAll().size();
    	settings.edit().commit();
    	System.out.println("size"+temp);
    	return temp;
    }
    
	/*保存记录*/
	public static void saveMark(Context context, String key,String record)
    {
		//System.out.println("save "+record);
    	SharedPreferences settings = context.getSharedPreferences(FILENAME,0);
    	settings.edit()
    	.putString(key,record)
    	.commit();
    }
	
	/*保存记录*/
	public static void saveMark(Context context, String key,boolean record)
    {
		//System.out.println("save "+record);
    	SharedPreferences settings = context.getSharedPreferences(FILENAME,0);
    	settings.edit()
    	.putBoolean(key,record)
    	.commit();
    }
	
	/*保存记录*/
	public static void saveMark(Context context, String key,int record)
    {
		//System.out.println("save "+record);
    	SharedPreferences settings = context.getSharedPreferences(FILENAME,0);
    	settings.edit()
    	.putInt(key,record)
    	.commit();
    }
	
	public static String getMark(Context contxet, String key,String dev)
    {
    	String temp = null;
    	SharedPreferences settings = contxet.getSharedPreferences(FILENAME,0);
    	temp=settings.getString(key,dev);
    	settings.edit().commit();
    	return temp;
    }
    
    public static boolean getMark(Context contxet, String key,boolean dev)
    {
    	boolean temp = false;
    	SharedPreferences settings = contxet.getSharedPreferences(FILENAME,0);
    	temp=settings.getBoolean(key,dev);
    	return temp;
    }
    
    public static int getMark(Context contxet, String key,int dev)
    {
    	int temp = 0;
    	SharedPreferences settings = contxet.getSharedPreferences(FILENAME,0);
    	temp=settings.getInt(key,dev);
    	return temp;
    }
}
