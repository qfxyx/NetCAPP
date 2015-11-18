package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.types.MtPicType;

public class MtPicListParser {
	private String info;
	private int code;
	private int account;
	private String json;
	private JSONArray arrayJson;
	private JSONObject tempJson;

	private List<MtPicType> resultList = new ArrayList<MtPicType>();

	public MtPicListParser(String json) {
		this.json = json;
		praser();
	}

	public void praser() {
		try {
			JSONObject dataJson = new JSONObject(json);
			arrayJson = dataJson.getJSONArray("picList");
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
		}
	}

	public List<MtPicType> praserList() {
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				arrayJson = dJson.getJSONArray("picList");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			for (int i = 0; i < arrayJson.length(); i++) {
				tempJson = arrayJson.optJSONObject(i);
				MtPicType t = new MtPicType();
				t.setId(tempJson.getInt("id"));
				t.setPicname(tempJson.getString("picname"));
				t.setSamllurl(Config.PICURL + tempJson.getString("smallUrl"));
				resultList.add(t);
			}
		} catch (Exception ex) {
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
