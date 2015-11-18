package com.example.jinanuniversity.json;

import org.json.JSONObject;

import com.example.jinanuniversity.data.Config;
import com.example.jinanuniversity.types.MtMsgType;

public class MtMsgParser {
	
	private String info;
	private int code;
	private int account;
	private String json;
	private JSONObject tempJson;

	public MtMsgParser(String json) {
		this.json = json;
		praser();
	}

	public void praser() {
		try {
			JSONObject dataJson = new JSONObject(json);
			tempJson = dataJson.getJSONObject("mtMsg");
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
		}
	}

	public MtMsgType praserList() {
		if (tempJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				tempJson = dJson.getJSONObject("mtMsg");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		MtMsgType t = new MtMsgType();
		try {
			t.setId(tempJson.getInt("id"));
			t.setMessage(tempJson.getString("message"));
			t.setFlag(tempJson.getBoolean("flag"));
			t.setMemo(tempJson.getString("memo"));
			t.setReceiver(tempJson.getString("receiver"));
			t.setSender(tempJson.getString("sender"));
			t.setTime(tempJson.getString("time"));
			t.setTitle(tempJson.getString("title"));
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
