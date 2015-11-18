package com.example.jinanuniversity.util;

import java.io.IOException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.jinanuniversity.error.IEasyCredentialsException;
import com.example.jinanuniversity.error.IEasyException;
import com.example.jinanuniversity.error.IEasyParseException;
import com.example.jinanuniversity.types.IEasyType;

public class JSONUtils {

	public static IEasyType consume(Parser<? extends IEasyType> parser,
			String content) throws IEasyCredentialsException,
			IEasyParseException, IEasyException, IOException {

		try {
			JSONObject json = new JSONObject(content);
			Iterator<String> it = json.keys();
			if (it.hasNext()) {
				String key = it.next();
				if (key.equals("error")) {
					throw new IEasyException(json.getString(key));
				} else {
					Object obj = json.get(key);
					if (obj instanceof JSONArray) {
						return (IEasyType) parser.parse((JSONArray) obj);
					} else {
						JSONObject job = (JSONObject) obj;
						IEasyType itype = parser.parse(job);
						return itype;
					}
				}
			} else {
				throw new IEasyException(
						"Error parsing JSON response, object had no single child key.");
			}
		} catch (JSONException ex) {
			throw new IEasyException("Error parsing JSON response: "
					+ ex.getMessage());
		}
	}
}
