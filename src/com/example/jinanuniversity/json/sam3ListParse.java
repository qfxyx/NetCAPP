package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.types.sam3ItemType;
import com.example.jinanuniversity.types.sam3ItemType;



public class sam3ListParse {
	private String info;
	private int code;
	private int account;
	private String json; 
	private JSONArray arrayJson;
	private JSONObject tempJson; 
	

	public sam3ListParse(String json) {
		this.json = json; 
	}

	public void praser() { 
		try {
			JSONObject dataJson = new JSONObject(json);
			tempJson = dataJson.getJSONObject("sam3userinfo"); 
			code = dataJson.getInt("code");
			info = dataJson.getString("info");

		} catch (Exception ex) {
			// 异常处理代码

		} 
	}
	
	public sam3ItemType praserList() {
		sam3ItemType t = new sam3ItemType();
		if(arrayJson==null){
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				tempJson = dJson.getJSONObject("sam3userinfo");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {  
				t.setName(tempJson.getString("name"));
				t.setAddress(tempJson.getString("address")); 
				t.setGroup(tempJson.getString("group"));
				t.setMac(tempJson.getString("mac"));
				t.setOnline(tempJson.getString("online"));
				t.setPower(tempJson.getString("power"));
				t.setTemplate(tempJson.getString("template"));
//				t.setId(tempJson.getString("id"));
		} catch (Exception ex) {
			// 异常处理代码
			System.out.println("数据错误");
		}
		return t;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getAccount() {
		return account;
	}

	public void setAccount(int account) {
		this.account = account;
	}

}
