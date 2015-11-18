package com.example.jinanuniversity.Http;
//package com.listen.http;
//
//import java.io.IOException;
//import java.util.logging.Logger;
//
//import org.apache.http.NameValuePair;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.impl.client.DefaultHttpClient;
//
//import com.listen.error.IEasyCredentialsException;
//import com.listen.error.IEasyError;
//import com.listen.error.IEasyException;
//import com.listen.error.IEasyParseException;
//import com.listen.types.IEasyType;
//import com.listen.util.Parser;
//
//
//public class HttpApiWithOAuth extends AbstractHttpApi {
//	protected static final Logger LOG = Logger.getLogger(HttpApiWithOAuth.class
//			.getCanonicalName());
////	protected static final boolean DEBUG = IEasySetting.DEBUG;
//	private OAuthConsumer mConsumer;
//
//	public HttpApiWithOAuth(DefaultHttpClient httpClient, String clientVersion) {
//		super(httpClient, clientVersion);
//	}
//
//	public IEasyType doHttpRequest(HttpRequestBase httpRequest,
//			Parser<? extends IEasyType> parser)
//			throws IEasyCredentialsException, IEasyParseException,
//			IEasyException, IOException {
////		if (DEBUG)
////			LOG.log(Level.FINE, "doHttpRequest: " + httpRequest.getURI());
//		try {
////			if (DEBUG)
////				LOG.log(Level.FINE, "Signing request: " + httpRequest.getURI());
////			if (DEBUG)
////				LOG.log(Level.FINE, "Consumer: " + mConsumer.getConsumerKey()
////						+ ", " + mConsumer.getConsumerSecret());
////			if (DEBUG)
////				LOG.log(Level.FINE, "Token: " + mConsumer.getToken() + ", "
////						+ mConsumer.getTokenSecret());
//			mConsumer.sign(httpRequest);
//		} catch (OAuthMessageSignerException e) {
////			if (DEBUG)
////				LOG.log(Level.FINE, "OAuthMessageSignerException", e);
//			throw new RuntimeException(e);
//		} catch (OAuthExpectationFailedException e) {
////			if (DEBUG)
////				LOG.log(Level.FINE, "OAuthExpectationFailedException", e);
//			throw new RuntimeException(e);
//		}
//		return executeHttpRequest(httpRequest, parser);
//	}
//
//	public String doHttpPost(String url, NameValuePair... nameValuePairs)
//			throws IEasyError, IEasyParseException, IOException,
//			IEasyCredentialsException {
//		return doHttpPost(url, nameValuePairs);
//	//	throw new RuntimeException("Haven't written this method yet.");
//	}
//
//	public void setOAuthConsumerCredentials(String key, String secret) {
//		mConsumer = new CommonsHttpOAuthConsumer(key, secret,
//				SignatureMethod.HMAC_SHA1);
//	}
//
//	public void setOAuthTokenWithSecret(String token, String tokenSecret) {
//		verifyConsumer();
//		if (token == null && tokenSecret == null) {
////			if (DEBUG)
////				LOG.log(Level.FINE,
////						"Resetting consumer due to null token/secret.");
//			String consumerKey = mConsumer.getConsumerKey();
//			String consumerSecret = mConsumer.getConsumerSecret();
//			mConsumer = new CommonsHttpOAuthConsumer(consumerKey,
//					consumerSecret, SignatureMethod.HMAC_SHA1);
//		} else {
//			mConsumer.setTokenWithSecret(token, tokenSecret);
//		}
//	}
//
//	public boolean hasOAuthTokenWithSecret() {
//		verifyConsumer();
//		return (mConsumer.getToken() != null)
//				&& (mConsumer.getTokenSecret() != null);
//	}
//
//	private void verifyConsumer() {
//		if (mConsumer == null) {
//			throw new IllegalStateException(
//					"Cannot call method without setting consumer credentials.");
//		}
//	}
//
//}
