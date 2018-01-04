package com.tsinova.httprequest;

import com.tsinova.bike.base.TsinovaApplication;
import com.tsinova.bluetoothandroid.pojo.SingletonBTInfo;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpClientManager {
	private static final int DEFAULT_MAX_CONNECTIONS = 30;
	private static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;
	private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;

	private static DefaultHttpClient sHttpClient;
//	public static CookieStore COOKIE = null;

	static {
		final HttpParams httpParams = new BasicHttpParams();

		ConnManagerParams.setTimeout(httpParams, 5000);
		ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(20));
		ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);

		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF-8");
		HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
		HttpClientParams.setRedirecting(httpParams, false);
		HttpProtocolParams.setUserAgent(httpParams, "Android client");
		HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		HttpConnectionParams.setTcpNoDelay(httpParams, true);
		HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		InputStream ins = null;
		try {
			ins = TsinovaApplication.getInstance().getAssets().open("tsinovaSSL.cer"); //下载的证书放到项目中的assets目录中
			CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);
			SSLSocketFactory sf = new MySSLSocketFactory(keyStore);

			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			schemeRegistry.register(new Scheme("https", sf, 443));
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
		sHttpClient = new DefaultHttpClient(manager, httpParams);
	}

	public static DefaultHttpClient getHttpClient() {
		return sHttpClient;
	}

	private HttpClientManager() {
	}

	public static HttpResponse execute(HttpHead head) throws IOException {

		PersistentCookieStore cookieStore = new PersistentCookieStore(TsinovaApplication.getInstance().getApplicationContext());
		sHttpClient.setCookieStore(cookieStore);
		setCookie(cookieStore);
		return sHttpClient.execute(head);
	}

	public static HttpResponse execute(HttpGet get) throws IOException {
		final HttpHost host = (HttpHost)sHttpClient.getParams().getParameter(
				ConnRoutePNames.DEFAULT_PROXY);
		if(host != null) {
			sHttpClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
		}
//		if (COOKIE != null) {
//			sHttpClient.setCookieStore(COOKIE);
//		}

		PersistentCookieStore cookieStore = new PersistentCookieStore(TsinovaApplication.getInstance().getApplicationContext());
		sHttpClient.setCookieStore(cookieStore);

		setCookie(cookieStore);


		HttpResponse response = sHttpClient.execute(get);
		if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//			COOKIE = HttpClientManager.getHttpClient().getCookieStore();


			PersistentCookieStore myCookieStore = TsinovaApplication.getInstance().getPersistentCookieStore();
			List<Cookie> cookies = sHttpClient.getCookieStore().getCookies();
			for (Cookie cookie:cookies){
				myCookieStore.addCookie(cookie);
			}

		}
		return response;
	}

	private static void setCookie(PersistentCookieStore cookieStore) {
		try {
			if (cookieStore.getCookies().size()!=0){
				String stringCookie = getCookie(cookieStore.getCookies());
				SingletonBTInfo.INSTANCE.setCookies(stringCookie);
			}
		}catch (Exception e){

		}
	}

	private static String getCookie(List<Cookie> cookies) {
		StringBuilder builder = new StringBuilder();
		for (Cookie cookie : cookies) {
			builder.append(";");            builder.append(cookie.getName());
			builder.append("=");
			builder.append(cookie.getValue());
			builder.append("; domain=");
			builder.append(cookie.getDomain());
//                    if (cookie.getExpiryDate() != null) {
//                        builder.append("; expires=");
//                        calendar.setTime(cookie.getExpiryDate());
//                        builder.append(DateFormat.format("EEE, dd MMM yyyy hh:mm:ss z", calendar.getTimeInMillis()));
//                    }
			builder.append("; path=");
			builder.append(cookie.getPath());
			builder.append("; version=");
			builder.append(cookie.getVersion());
		}
		return builder.substring(1, builder.length());
	}


	public static HttpResponse execute(HttpUriRequest post) throws IOException {

		PersistentCookieStore cookieStore = new PersistentCookieStore(TsinovaApplication.getInstance().getApplicationContext());
		sHttpClient.setCookieStore(cookieStore);
		setCookie(cookieStore);
		return sHttpClient.execute(post);
	}

	public static HttpResponse execute(HttpPost post) throws IOException {
		final HttpHost host = (HttpHost)sHttpClient.getParams().getParameter(
				ConnRoutePNames.DEFAULT_PROXY);
		if(host != null) {
			sHttpClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
		}
//		if (COOKIE != null) {
//			sHttpClient.setCookieStore(COOKIE);
//		}

		PersistentCookieStore cookieStore = new PersistentCookieStore(TsinovaApplication.getInstance().getApplicationContext());
		sHttpClient.setCookieStore(cookieStore);
		setCookie(cookieStore);

		HttpResponse response = sHttpClient.execute(post);
		if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//			COOKIE = HttpClientManager.getHttpClient().getCookieStore();


			PersistentCookieStore myCookieStore = TsinovaApplication.getInstance().getPersistentCookieStore();
			List<Cookie> cookies = sHttpClient.getCookieStore().getCookies();
			for (Cookie cookie:cookies){
				myCookieStore.addCookie(cookie);
			}
		}
		return response;
	}

	public static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					try {
						chain[0].checkValidity();
					} catch (Exception e) {
						throw new CertificateException("Certificate not valid or trusted.");
					}
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
				throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

}
