package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.types.MaintenanceItemType;



public class MaintenanceListParser {
	private String info;
	private int code;
	private int account;
	private String json; 
	private JSONArray arrayJson;
	private JSONObject tempJson; 
	
	private List<MaintenanceItemType> resultList = new ArrayList<MaintenanceItemType>();

	public MaintenanceListParser(String json) {
		this.json = json; 
	}

	public void praser() { 
		try {
			JSONObject dataJson = new JSONObject(json);
//			account = dataJson.getInt("account");
			arrayJson = dataJson.getJSONArray("historyJobList"); 
			code = dataJson.getInt("code");
			info = dataJson.getString("info");

		} catch (Exception ex) {
			// 异常处理代码

		} 
	}
	
	public List<MaintenanceItemType> praserList() {
		if(arrayJson==null){
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				arrayJson = dJson.getJSONArray("historyJobList"); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {  
			for (int i = 0; i < arrayJson.length(); i++) { 
				tempJson = arrayJson.optJSONObject(i);
				MaintenanceItemType t = new MaintenanceItemType();
				t.setId(tempJson.getInt("id"));
				t.setUserId(tempJson.getString("userId"));
				t.setName(tempJson.getString("name"));
				t.setJobLevel(tempJson.getString("jobLevel"));
				t.setDealResult(tempJson.getString("dealResult"));
				t.setAddress(tempJson.getString("address")); 
				t.setDealer1(tempJson.getString("dealer1"));
				t.setDealTime1(tempJson.getString("dealTime1"));
				t.setDealer2(tempJson.getString("dealer2"));
				t.setDealTime2(tempJson.getString("dealTime2"));
				resultList.add(t);
			}
		} catch (Exception ex) {
			// 异常处理代码

		}
		return resultList;
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
