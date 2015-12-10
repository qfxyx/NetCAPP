package com.example.jinanuniversity.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.example.jinanuniversity.Http.AbstractHttpApi;
import com.example.jinanuniversity.Http.HttpApi;
import com.example.jinanuniversity.Http.HttpApiWithBasicAuth;
import com.example.jinanuniversity.data.Service;
import com.example.jinanuniversity.error.IEasyCredentialsException;
import com.example.jinanuniversity.error.IEasyException;
import com.example.jinanuniversity.error.IEasyParseException;

public class IEasyHttpApiV1 {

	private static final String TAG = "util.IEasyHttpApiV1";
	private final DefaultHttpClient mHttpClient = AbstractHttpApi .createHttpClient();
	private HttpApi mHttpApi;
	private final String mApiBaseUrl;
	private final AuthScope mAuthScope;

	public IEasyHttpApiV1() {
		String domain = Service.getInstance().getServiceIp();
		mApiBaseUrl = "http://" + domain;
		mAuthScope = new AuthScope(domain, 80);
		mHttpApi = new HttpApiWithBasicAuth(mHttpClient, null);
	}

	// 登陆验证
	public String invitationCodeLogin(String url, String appkey,
			String timestamp, String sign, String ver, String account,
			String password) throws Exception, IOException {
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		Log.i(TAG, "invitationCodeLogin 请求中:" + url);
		nvps.add(new BasicNameValuePair("appkey", appkey));
		nvps.add(new BasicNameValuePair("timestamp", timestamp));
		nvps.add(new BasicNameValuePair("sign", sign));
		nvps.add(new BasicNameValuePair("ver", ver));
		nvps.add(new BasicNameValuePair("account", account));
		nvps.add(new BasicNameValuePair("password", password));

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpParams httpParams = httpClient.getParams();
		httpParams.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse httpResponse = httpClient.execute(httpPost);

		CookieStore mCookieStore = httpClient.getCookieStore();
		List<Cookie> cookies = mCookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			if ("JSESSIONID".equals(cookies.get(i).getName())) {
				// 将cookies写进全局变量
				Service.getInstance().setJSESSIONID(cookies.get(i).getValue());
			}
		}
		String result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
		return result;
	}

	/** 获取信息 */
	public String getMessage(String url, String appkey, String sign,
			String timestamp, String ver, String account, String page)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getMessage 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("page", page),
				new BasicNameValuePair("ver", ver) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 用户SAM3认证日志 */
	public String getsam3LogList(String url, String appkey, String sign,
			String timestamp, String ver, String account, String userId,
			String startTime, String endTime, String pageNo)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getsam3LogList 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("userId", userId),
				new BasicNameValuePair("startTime", startTime),
				new BasicNameValuePair("endTime", endTime),
				new BasicNameValuePair("pageNo", pageNo),
				new BasicNameValuePair("ver", ver) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 发送 */
	public String sendLive(String url, String appkey, String sign,
			String timestamp, String ver, String account, String sender,
			String receiver, String title, String message, String memo)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "sendLive () url = " + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("sender", sender),
				new BasicNameValuePair("receiver", receiver),
				new BasicNameValuePair("title", title),
				new BasicNameValuePair("message", message),
				new BasicNameValuePair("memo", memo) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 获取维护单列表 */
	public String getMainList(String url, String sign, String appkey,
			String timestamp, String ver, String account, String name,
			String ruleList, String pageNo) throws NumberFormatException,
			IOException, IEasyCredentialsException, IEasyParseException,
			IEasyException {
		Log.i(TAG, "getMainList () url = " + url);
		NameValuePair[] namevalues = { new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("name", name),
				new BasicNameValuePair("ruleList", ruleList),
				new BasicNameValuePair("pageNo", pageNo), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 获取历史维护单列表接口 */
	public String getMain_inquireList(String url, String sign, String appkey,
			String timestamp, String ver, String account, String name,
			String ruleList, String pageNo) throws NumberFormatException,
			IOException, IEasyCredentialsException, IEasyParseException,
			IEasyException {
		Log.i(TAG, "getMain_inquireList () url = " + url);
		NameValuePair[] namevalues = { new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("name", name),
				new BasicNameValuePair("ruleList", ruleList),
				new BasicNameValuePair("pageNo", pageNo), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 获取维护单明细 */
	public String getMtJob(String url, String sign, String appkey,
			String timestamp, String ver, String account, String jobId)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getMtJob 请求中:" + url);
		NameValuePair[] namevalues = { new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("jobId", jobId), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 获取历史维护单明细 */
	public String getHistoryJob(String url, String sign, String appkey,
			String timestamp, String ver, String account, String historyJobId)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getHistoryJob 请求中:" + url);
		NameValuePair[] namevalues = { new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("historyJobId", historyJobId), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** */
	public String response(String url, String sign, String appkey,
			String timestamp, String ver, String account, String jobId)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "response 请求中:" + url);
		NameValuePair[] namevalues = { new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("jobId", jobId), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 获取新建信息 */
	public String getmtMsg(String url, String sign, String appkey,
			String timestamp, String ver, String account)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getmtMsg 请求中:" + url);
		NameValuePair[] namevalues = { new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 获取新建信息 */
	public String closeJob(String url, String appkey, String sign,
			String timestamp, String ver, String account, String jobId,
			String troubleReason, String dealResult, String dealMethod)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "closeJob 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("jobId", jobId),
				new BasicNameValuePair("troubleReason", troubleReason),
				new BasicNameValuePair("dealResult", dealResult),
				new BasicNameValuePair("dealMethod", dealMethod), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 注销 */
	public String cancel(String url, String appkey, String sign,
			String timestamp, String ver, String account)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "cancel 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 维护单延时 */
	public String getDelayed(String url, String appkey, String sign,
			String timestamp, String ver, String account, String jobId,
			String delayReason) throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getDelayed 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("jobId", jobId),
				new BasicNameValuePair("delayReason", delayReason), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 维护单请求支援 */
	public String getPass(String url, String appkey, String sign,
			String timestamp, String ver, String account, String jobId,
			String passReason, String passMemo) throws NumberFormatException,
			IOException, IEasyCredentialsException, IEasyParseException,
			IEasyException {
		Log.i(TAG, "getPass 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("jobId", jobId),
				new BasicNameValuePair("passReason", passReason),
				new BasicNameValuePair("passMemo", passMemo), };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 维护员明细 */
	public String getVwUser(String url, String appkey, String sign,
			String timestamp, String ver, String account, String adminId)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getVwUser 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("adminId", adminId) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 用户明细 */
	public String getUserDetail(String url, String appkey, String sign,
			String timestamp, String ver, String account, String userId)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getUserDetail 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("userId", userId) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	public String resetPwd(String url, String appkey, String sign, String timestamp,
						   String ver, String account, String userId,String New_pwd)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getUserDetail 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("newPwd", New_pwd),
				new BasicNameValuePair("userId", userId) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/**
	 * 用户物理地址修改
	 */
	public String macUpdate(String url, String appkey, String sign,
			String timestamp, String ver, String account, String userId,
			String mac) throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "macUpdate 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("userId", userId),
				new BasicNameValuePair("mac", mac) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 用户SAM3模版修改 */
	public String template(String url, String appkey, String sign,
			String timestamp, String ver, String account, String userId,
			String template) throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "template 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("userId", userId),
				new BasicNameValuePair("template", template) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 图片列表接口 */
	public String getPicList(String url, String appkey, String sign,
			String timestamp, String ver, String account, String building,
			String pageNo) throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getPicList 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("building", building),
				new BasicNameValuePair("pageNo", pageNo) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 图片明细 接口 */
	public String getPicDetails(String url, String appkey, String sign,
			String timestamp, String ver, String account, String picId)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getPicDetails 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("picId", picId) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 信息明细接口 */
	public String getMsgDetails(String url, String appkey, String sign,
			String timestamp, String ver, String account, String msgId)
			throws NumberFormatException, IOException,
			IEasyCredentialsException, IEasyParseException, IEasyException {
		Log.i(TAG, "getMsgDetails 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("msgId", msgId) };
		return mHttpApi.doHttpPost(url, namevalues);
	}

	/** 用户列表 */
	public String getUserList(String url, String appkey, String sign,
			String timestamp, String ver, String account, String ruleList,
			String pageNo, String queryPwd) throws NumberFormatException,
			IOException, IEasyCredentialsException, IEasyParseException,
			IEasyException {
		Log.i(TAG, "getUserList 请求中:" + url);
		NameValuePair[] namevalues = {
				new BasicNameValuePair("appkey", appkey),
				new BasicNameValuePair("sign", sign),
				new BasicNameValuePair("timestamp", timestamp),
				new BasicNameValuePair("ver", ver),
				new BasicNameValuePair("account", account),
				new BasicNameValuePair("ruleList", ruleList),
				new BasicNameValuePair("pageNo", pageNo),
				new BasicNameValuePair("queryPwd", queryPwd) };
		return mHttpApi.doHttpPost(url, namevalues);
	}
}
