package com.example.jinanuniversity.json;

import org.json.JSONObject;

import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.types.MtPicType;

public class MtPicParser {
	
	private String info;
	private int code;
	private int account;
	private String json;
	private JSONObject tempJson;

	public MtPicParser(String json) {
		this.json = json;
		praser();
	}

	public void praser() {
		try {
			JSONObject dataJson = new JSONObject(json);
			tempJson = dataJson.getJSONObject("mtPic");
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
		}
	}

	public MtPicType praserList() {
		if (tempJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				tempJson = dJson.getJSONObject("mtPic");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MtPicType t = new MtPicType();
		try {
			t.setId(tempJson.getInt("id"));
			t.setDetail(tempJson.getString("detail"));
			t.setBigurl(Config.PICURL + tempJson.getString("bigUrl"));
		} catch (Exception ex) {
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
