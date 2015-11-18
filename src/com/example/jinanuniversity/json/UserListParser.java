package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.types.NetcUser;

public class UserListParser {
	private String info;
	private int code;
	private int account;
	private String json;

	public UserListParser(String json) {
		this.json = json;
		try {
			JSONObject dataJson = new JSONObject(json);
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
		} catch (Exception ex) {
		}
	}

	public List<NetcUser> praserList() {
		List<NetcUser> resultList = new ArrayList<NetcUser>();
		JSONArray arrayJson = null;
		if (arrayJson == null) {
			JSONObject dJson;
			try {
				dJson = new JSONObject(json);
				arrayJson = dJson.getJSONArray("netcUserList");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			JSONObject tempJson;
			for (int i = 0; i < arrayJson.length(); i++) {
				tempJson = arrayJson.optJSONObject(i);
				NetcUser nUser = new NetcUser();
				nUser.setBuilding(tempJson.getString("building"));
				nUser.setIp(tempJson.getString("ip"));
				nUser.setLevel(tempJson.getString("level"));
				nUser.setRoomNo(tempJson.getString("roomNo"));
				nUser.setUserId(tempJson.getString("userId"));
				nUser.setUserName(tempJson.getString("userName"));
				nUser.setUsrFlag(tempJson.getString("usrFlag"));
				resultList.add(nUser);
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
