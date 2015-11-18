package com.example.jinanuniversity.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class mtVwUserParser {
	private String info;
	private int code;
	private int account;
	private String json;
	private JSONArray arrayJson;
	private JSONObject tempJson;

	private MtVwUser mtVwUser = new MtVwUser();

	public mtVwUserParser(String json) {
		this.json = json;
		try {
			JSONObject dataJson = new JSONObject(json);
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
		}
	}

	public MtVwUser praser() {
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				// arrayJson = dJson.getJSONArray("mtVwUser");
				tempJson = dJson.getJSONObject("mtVwUser");
				mtVwUser.setName(tempJson.getString("name"));
				mtVwUser.setPhone(tempJson.getString("phone"));
				mtVwUser.setQq(tempJson.getString("qq"));
				mtVwUser.setShortphone(tempJson.getString("shortphone"));
				mtVwUser.setEmail(tempJson.getString("email"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return mtVwUser;
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
