package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.types.MtMsgType;

public class InformationListParser {
	private String info;
	private int code;
	private int account;
	private String json;
	private JSONArray arrayJson;
	private JSONObject tempJson;

	private List<MtMsgType> resultList = new ArrayList<MtMsgType>();

	public InformationListParser(String json) {
		this.json = json;
	}

	public void praser() {
		try {
			JSONObject dataJson = new JSONObject(json);
			account = dataJson.getInt("account");
			arrayJson = dataJson.getJSONArray("msgList");
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
		}
	}

	public List<MtMsgType> praserList() {
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				arrayJson = dJson.getJSONArray("msgList");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			for (int i = 0; i < arrayJson.length(); i++) {
				tempJson = arrayJson.optJSONObject(i);
				MtMsgType t = new MtMsgType();
				t.setSender(tempJson.getString("sender"));
				t.setTitle(tempJson.getString("title"));
				t.setMessage(tempJson.getString("message"));
				t.setTime(tempJson.getString("time"));
				t.setId(tempJson.getInt("id"));
				t.setFlag(tempJson.getBoolean("flag"));
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
