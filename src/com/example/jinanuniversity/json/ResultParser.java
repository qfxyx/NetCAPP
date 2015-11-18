package com.example.jinanuniversity.json;

import org.json.JSONObject;

import com.example.jinanuniversity.types.ResultType;


public class ResultParser {

	private String json;
	private ResultType result;
	
	public ResultParser(String json) {
		this.json = json;
		
	} 

	public ResultType praser() {
		result = new ResultType();
		try {
			JSONObject dataJson = new JSONObject(json);
			result.setCode(dataJson.getInt("code"));
			result.setInfo(dataJson.getString("info")); 
			
		} catch (Exception ex) {
			// 异常处理代码
			
		}
		return result;
	}
}
