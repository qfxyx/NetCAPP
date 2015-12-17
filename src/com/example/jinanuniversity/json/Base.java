package com.example.jinanuniversity.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Base {
	
	private static final String TAG=Base.class.getSimpleName();

	private List<JsonMain> resultList = new ArrayList<JsonMain>();
	private String isLogin;

	private String json;
	private int code;
	private JSONArray arrayJson;
	private JSONObject tempJson;
	private String user;
	private String info;
	private String buildingList;
	private String troubleReasonList;
	private String dealResultList;
	private String delayReasonList;
	private String passReasonList;
	private String templateList;
	private String groupList;
	private String serverList;

    public String getServerList(){
		return serverList;
	}
	public void setServerList(String serverList){
		this.serverList=serverList;
	}
	public String getGroupList() {
		return groupList;
	}

	public void setGroupList(String groupList) {
		this.groupList = groupList;
	}

	public String getBuildingList() {
		return buildingList;
	}

	public void setBuildingList(String buildingList) {
		this.buildingList = buildingList;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Base(String json) {
		this.json = json;
	}

	public void praser() {
		try {
			Log.i(TAG, "praser start");
			JSONObject dataJson = new JSONObject(json);
			code = dataJson.getInt("code");
			info = dataJson.getString("info");
			buildingList = dataJson.getString("buildingList");
			troubleReasonList = dataJson.getString("troubleReasonList");
			dealResultList = dataJson.getString("dealResultList");
			delayReasonList = dataJson.getString("delayReasonList");
			passReasonList = dataJson.getString("passReasonList");
			templateList = dataJson.getString("templateList");
			groupList= dataJson.getString("groupList");
			serverList=dataJson.getString("serverList");

		} catch (Exception ex) {
			// 异常处理代码
			ex.printStackTrace();
			System.out.println("praser jason data occur wrong");
		}
	}

	public String getTroubleReasonList() {
		return troubleReasonList;
	}

	public void setTroubleReasonList(String troubleReasonList) {
		this.troubleReasonList = troubleReasonList;
	}

	public String getDealResultList() {
		return dealResultList;
	}

	public void setDealResultList(String dealResultList) {
		this.dealResultList = dealResultList;
	}

	public String getDelayReasonList() {
		return delayReasonList;
	}

	public void setDelayReasonList(String delayReasonList) {
		this.delayReasonList = delayReasonList;
	}

	public String getPassReasonList() {
		return passReasonList;
	}

	public void setPassReasonList(String passReasonList) {
		this.passReasonList = passReasonList;
	}

	public String getTemplateList() {
		return templateList;
	}

	public void setTemplateList(String templateList) {
		this.templateList = templateList;
	}

	public List<JsonMain> praserList() {
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
				JsonMain t = new JsonMain();
				t.setId(tempJson.getInt("id"));
				t.setSenderName(tempJson.getString("senderName"));
				t.setSenderId(tempJson.getInt("senderId"));
				t.setTitle(tempJson.getString("title"));
				t.setMessage(tempJson.getString("message"));
				t.setTime(tempJson.getString("time"));
				resultList.add(t);
			}
		} catch (Exception ex) {
		}
		return resultList;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public MtVwUser getMtVwUser() {
		JSONObject dJson = null;
		try {
			if (user == null) {
				JSONObject jsons = new JSONObject(json);
				user = jsons.getString("mtUser");
			}
			dJson = new JSONObject(user);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		MtVwUser mtUser = null;
		try {
			mtUser = new MtVwUser();
			mtUser.setAccount(dJson.getString("account"));
			mtUser.setAddress(dJson.getString("address"));
			mtUser.setArea(dJson.getString("area"));
			mtUser.setName(dJson.getString("name"));
			mtUser.setBulidings(dJson.getString("buildings"));
			mtUser.setDepartment(dJson.getString("department"));
			mtUser.setEmail(dJson.getString("email"));
			mtUser.setGroupname(dJson.getString("groupname"));
			mtUser.setPassword(dJson.getString("password"));
			mtUser.setPhone(dJson.getString("phone"));
			mtUser.setProperty(dJson.getString("property"));
			mtUser.setQq(dJson.getString("qq"));
			mtUser.setRole(dJson.getString("role"));
			mtUser.setShortphone(dJson.getString("shortphone"));
			mtUser.setQuerypwd(dJson.getString("queryPwd"));
			mtUser.setMemo1(dJson.getString("memo1"));
			mtUser.setMemo2(dJson.getString("memo2"));
		} catch (Exception ex) {
			System.out.println("解析出错");
		}
		return mtUser;
	}

	public List<JsonMain> getResultList() {
		return resultList;
	}

	public void setResultList(List<JsonMain> resultList) {
		this.resultList = resultList;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public JSONArray getArrayJson() {
		return arrayJson;
	}

	public void setArrayJson(JSONArray arrayJson) {
		this.arrayJson = arrayJson;
	}

	public JSONObject getTempJson() {
		return tempJson;
	}

	public void setTempJson(JSONObject tempJson) {
		this.tempJson = tempJson;
	}

	public String getIsLogin() {
		return isLogin;
	}

	public void setIsLogin(String isLogin) {
		this.isLogin = isLogin;
	}

}
