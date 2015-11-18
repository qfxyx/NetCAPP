package com.example.jinanuniversity.util;

import java.security.acl.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.types.IEasyType;

public interface Parser<T extends IEasyType> {
	public abstract T parse(JSONObject json) throws JSONException;

	public Group parse(JSONArray array) throws JSONException;
}
