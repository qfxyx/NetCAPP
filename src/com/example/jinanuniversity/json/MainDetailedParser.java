package com.example.jinanuniversity.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.jinanuniversity.types.MtJob;
import com.example.jinanuniversity.types.NetcUser;

public class MainDetailedParser {
	
	private static final String TAG="MainDetailedParser";
	private String info;
	private int code;
	private int account;
	private String json;
	private JSONArray arrayJson;
	private JSONObject tempJson;

	private MtJob resultList = new MtJob();

	public MainDetailedParser(String json) {
		this.json = json;
	}

	public void praser() {
		try {
			JSONObject dataJson = new JSONObject(json);
			tempJson = dataJson.getJSONObject("netcUser");
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
			// 异常处理代码
		}
	}

	public MtJob praserList(String name) {
		MtJob t = new MtJob();
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				tempJson = dJson.getJSONObject(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			t.setId(tempJson.getInt("id"));
			t.setUserId(tempJson.getString("userId"));
			t.setName(tempJson.getString("name"));
			t.setJobLevel(tempJson.getString("jobLevel"));
			t.setTrblStatus(tempJson.getString("trblStatus"));
			t.setPhone(tempJson.getString("phone"));
			t.setAddress(tempJson.getString("address"));
			t.setArea(tempJson.getString("area"));
			t.setAccepter(tempJson.getString("accepter"));
			t.setAccepteTime(tempJson.getString("accepteTime"));
			t.setGroupAdmin(tempJson.getString("groupAdmin"));
			t.setGroupName(tempJson.getString("groupName"));
			t.setResponser(tempJson.getString("responser"));
			t.setResponseTime(tempJson.getString("responseTime"));
			t.setDealer1(tempJson.getString("dealer1"));
			t.setDealTime1(tempJson.getString("dealTime1"));
			t.setDealResult1(tempJson.getString("dealResult1"));
			t.setDealer2(tempJson.getString("dealer2"));
			t.setDealTime2(tempJson.getString("dealTime2"));
			t.setDealResult(tempJson.getString("dealResult"));
			t.setTrblCource(tempJson.getString("trblCource"));
			t.setDealMethod(tempJson.getString("dealMethod"));
		} catch (Exception ex) {
			// 异常处理代码
		}
		return t;
	}

	public NetcUser praserUserList() {
		NetcUser suer = new NetcUser();
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				Log.i(TAG, "dJson = "+dJson.toString());
				tempJson = dJson.getJSONObject("netcUser");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			suer.setMac(tempJson.getString("mac"));
			suer.setUserId(tempJson.getString("userId"));
			suer.setUserName(tempJson.getString("userName"));
			suer.setLevel(tempJson.getString("level"));
			suer.setUsrFlag(tempJson.getString("usrFlag"));
			suer.setBuilding(tempJson.getString("building"));
			suer.setRoomNo(tempJson.getString("roomNo"));
			suer.setPhone(tempJson.getString("phone"));
			suer.setOfficeName(tempJson.getString("officeName"));
			suer.setEmail(tempJson.getString("email"));
			suer.setServIp(tempJson.getBoolean("servIp"));
			suer.setServAbroad(tempJson.getBoolean("servAbroad"));
			suer.setOpenDate(tempJson.getString("openDate"));
			suer.setFromDate(tempJson.getString("fromDate"));
			suer.setToDate(tempJson.getString("toDate"));
			suer.setActiveFlag(tempJson.getBoolean("activeFlag"));
			suer.setIp(tempJson.getString("ip"));
			suer.setGateway(tempJson.getString("gateway"));
			suer.setMask(tempJson.getString("mask"));
			suer.setSam3Tempelate(tempJson.getString("sam3Tempelate"));
			suer.setMemo(tempJson.getString("memo"));
		} catch (Exception ex) {
			// 异常处理代码

		}
		return suer;
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
