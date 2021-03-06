package com.example.jinanuniversity.Http;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import com.example.jinanuniversity.error.IEasyCredentialsException;
import com.example.jinanuniversity.error.IEasyException;
import com.example.jinanuniversity.error.IEasyParseException;
import com.example.jinanuniversity.types.IEasyType;
import com.example.jinanuniversity.util.Parser;


public class HttpApiWithBasicAuth extends AbstractHttpApi {

    private HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

        @Override
        public void process(final HttpRequest request, final HttpContext context)
                throws HttpException, IOException {

            AuthState authState = (AuthState)context.getAttribute(ClientContext.TARGET_AUTH_STATE);
            CredentialsProvider credsProvider = (CredentialsProvider)context
                    .getAttribute(ClientContext.CREDS_PROVIDER);
            HttpHost targetHost = (HttpHost)context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

            // If not auth scheme has been initialized yet
            if (authState.getAuthScheme() == null) {
                AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
                // Obtain credentials matching the target host
                org.apache.http.auth.Credentials creds = credsProvider.getCredentials(authScope);
                // If found, generate BasicScheme preemptively
                if (creds != null) {
                    authState.setAuthScheme(new BasicScheme());
                    authState.setCredentials(creds);
                }
            }
        }

    };
	
	public HttpApiWithBasicAuth(DefaultHttpClient httpClient,
			String clientVersion) {
		super(httpClient, clientVersion);
		httpClient.addRequestInterceptor(preemptiveAuth, 0);
 	}
	
	@Override
	public IEasyType doHttpRequest(HttpRequestBase httpRequest,Parser<? extends IEasyType> parser) 
		throws IEasyCredentialsException,IEasyParseException,IEasyException,IOException {
		return executeHttpRequest(httpRequest, parser);
	}

}
