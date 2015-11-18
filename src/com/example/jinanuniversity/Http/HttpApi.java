package com.example.jinanuniversity.Http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import com.example.jinanuniversity.error.IEasyCredentialsException;
import com.example.jinanuniversity.error.IEasyException;
import com.example.jinanuniversity.error.IEasyParseException;
import com.example.jinanuniversity.types.IEasyType;
import com.example.jinanuniversity.util.Parser;



public interface HttpApi {
	abstract public IEasyType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends IEasyType> parser)
			throws IEasyCredentialsException, IEasyParseException,
			IEasyException, IOException;

	abstract public String doHttpPost(String url,
			NameValuePair... nameValuePairs) throws IEasyCredentialsException,
			IEasyParseException, IEasyException, IOException;
	
    abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

    abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);
    
    abstract public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary)
            throws IOException; 
}
