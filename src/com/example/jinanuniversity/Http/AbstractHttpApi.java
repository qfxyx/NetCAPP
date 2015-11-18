package com.example.jinanuniversity.Http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.example.jinanuniversity.data.Service;
import com.example.jinanuniversity.error.IEasyCredentialsException;
import com.example.jinanuniversity.error.IEasyException;
import com.example.jinanuniversity.error.IEasyParseException;
import com.example.jinanuniversity.types.IEasyType;
import com.example.jinanuniversity.util.JSONUtils;
import com.example.jinanuniversity.util.Parser;

import android.util.Log;

public abstract class AbstractHttpApi implements HttpApi {
	
	protected static final Logger LOG = Logger.getLogger(AbstractHttpApi.class
			.getCanonicalName());
	private static final String TAG = "AbstractHttpApi";
	private static final String CLIENT_VERSION_HEADER = "User-Agent";
	private static final int TIMEOUT = 60;
	private final DefaultHttpClient mHttpClient;
	private final String mClientVersion;
	private static final String DEFAULT_CLIENT_VERSION = "netcapi1.jnu.edu.cn";

	public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {
		mHttpClient = httpClient;
		if (clientVersion != null) {
			mClientVersion = clientVersion;
		} else {
			mClientVersion = DEFAULT_CLIENT_VERSION;
		}
	}

	public IEasyType executeHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends IEasyType> parser)
			throws IEasyCredentialsException, IEasyParseException,
			IEasyException, IOException {

		HttpResponse response = executeHttpRequest(httpRequest);
		int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
		case 200:
			String content = EntityUtils.toString(response.getEntity(),
					HTTP.UTF_8);
			return JSONUtils.consume(parser, content);

		case 400:
			throw new IEasyException(response.getStatusLine().toString(),
					EntityUtils.toString(response.getEntity()));

		case 401:
			response.getEntity().consumeContent();
			throw new IEasyCredentialsException(response.getStatusLine()
					.toString());

		case 404:
			response.getEntity().consumeContent();
			Log.d(TAG, "HTTP Code: 404");
			throw new IEasyException(response.getStatusLine().toString());

		case 500:
			response.getEntity().consumeContent();
			throw new IEasyException("Foursquare is down. Try again later.");

		default:
			response.getEntity().consumeContent();
			throw new IEasyException("Error connecting to Foursquare: "
					+ statusCode + ". Try again later.");
		}
	}

	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest)
			throws IOException {
		try {
			mHttpClient.getConnectionManager().closeExpiredConnections();
			return mHttpClient.execute(httpRequest);
		} catch (IOException e) {
			httpRequest.abort();
			throw e;
		}
	}

	@Override
	public String doHttpPost(String url, NameValuePair... nameValuePairs)
			throws IEasyCredentialsException, IEasyParseException,
			IEasyException, IOException {
		
		HttpPost httpPost = createHttpPost(url, nameValuePairs);
		HttpResponse response = executeHttpRequest(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();

		switch (statusCode) {
		case 200:
			try {
				return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
			} catch (ParseException e) {
				throw new IEasyParseException(e.getMessage());
			}

		case 401:
			response.getEntity().consumeContent();
			throw new IEasyCredentialsException(response.getStatusLine()
					.toString());

		case 404:
			response.getEntity().consumeContent();
			throw new IEasyException(response.getStatusLine().toString());

		default:
			response.getEntity().consumeContent();
			throw new IEasyException(response.getStatusLine().toString());
		}
	}

	@Override
	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs),
				HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		return httpGet;
	}

	@Override
	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Cookie", "JSESSIONID="
				+ Service.getInstance().getJSESSIONID());
		httpPost.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(
					stripNulls(nameValuePairs), HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			throw new IllegalArgumentException(
					"Unable to encode http parameters.");
		}
		return httpPost;
	}

	@Override
	public HttpURLConnection createHttpURLConnectionPost(URL url,
			String boundary) throws IOException {
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setUseCaches(false);
		conn.setConnectTimeout(TIMEOUT * 1000);
		conn.setRequestMethod("POST");
		conn.setRequestProperty(CLIENT_VERSION_HEADER, mClientVersion);
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
				+ boundary);
		return conn;
	}

	public static final DefaultHttpClient createHttpClient() {
		
		final SchemeRegistry supportedSchemes = new SchemeRegistry();
		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));
		supportedSchemes.register(new Scheme("https", SSLSocketFactory
				.getSocketFactory(), 443));

		final HttpParams httpParams = createHttpParams();
		HttpClientParams.setRedirecting(httpParams, false);

		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(
				httpParams, supportedSchemes);
		return new DefaultHttpClient(ccm, httpParams);
	}

	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				params.add(param);
			}
		}
		return params;
	}

	private static final HttpParams createHttpParams() {
		
		final HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		return params;
	}
}
