package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.types.MaintainItemType;

public class MaintainListParser {

	private String info;
	private int code;
	private int account;
	private String json;
	private JSONArray arrayJson;
	private JSONObject tempJson;

	private List<MaintainItemType> resultList = new ArrayList<MaintainItemType>();

	public MaintainListParser(String json) {
		this.json = json;
	}

	public void praser() {
		try {
			JSONObject dataJson = new JSONObject(json);
			// account = dataJson.getInt("account");
			arrayJson = dataJson.getJSONArray("jobList");
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {

		}
	}

	public List<MaintainItemType> praserList() {
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				arrayJson = dJson.getJSONArray("jobList");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			for (int i = 0; i < arrayJson.length(); i++) {
				tempJson = arrayJson.optJSONObject(i);
				MaintainItemType t = new MaintainItemType();
				t.setAccepteTime(tempJson.getString("accepteTime"));
				t.setAddress(tempJson.getString("address"));
				t.setDealResult(tempJson.getString("dealResult"));
				t.setJobLevel(tempJson.getString("jobLevel"));
				t.setName(tempJson.getString("name"));
				t.setUserId(tempJson.getString("userId"));
				t.setId(tempJson.getInt("id"));
				t.setTrblStatus(tempJson.getString("trblStatus"));
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
