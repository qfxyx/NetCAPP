package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.types.GroudListType;

public class NewMessageListParser {
	private String info;
	private int code;
	private int account;
	private String json;

	public NewMessageListParser(String json) {
		this.json = json;
		try {
			JSONObject dataJson = new JSONObject(json);
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
		}
	}

	public List<GroudListType> praserList(String name) {
		List<GroudListType> resultList = new ArrayList<GroudListType>();
		JSONArray arrayJson = null;
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				arrayJson = dJson.getJSONArray(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			JSONObject tempJson;
			for (int i = 0; i < arrayJson.length(); i++) {
				tempJson = arrayJson.optJSONObject(i);
				GroudListType t = new GroudListType();
				t.setAccount(tempJson.getString("account"));
				t.setName(tempJson.getString("name"));
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
