/**
 * 文件名	：BaseHttpEngine.java
 * 
 * 创建日期	：2013-03-22
 * 
 * Copyright (c) 2003-2012 北京联龙博通

 * All rights reserved.
 */
package com.chinamworld.bocmbci.http.engine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequest;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.http.MySSLSocketFactory;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.DeviceUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.MyRelativeLayout;
import com.chinamworld.llbt.userwidget.dialogview.LoadingDialog;

/**
 * 描述:BaseHttpEngine
 * <p />
 * 
 * 基cfr于http的基础Engine
 * <p />
 * 
 * @version 1.00
 * @author wez
 * @date 2012-10-24 10:06:25
 * 
 */
public abstract class BaseHttpEngine{
	/** 发送请求的对象 */
	public static DefaultHttpClient sClient; // 发送请求的对象
	/** 是否需要设置代理 */
	protected static boolean needProxy = false; // 是否需要设置代理
	/** 设置代理的主机名 */
	protected static String hostname = ""; // 设置代理的主机名
	/** 设置代理的端口 */
	protected static int hostport = 0; // 设置代理的端口

	protected boolean cancleFlag = false;
	protected HttpGet httpGet = null;
	protected HttpPost httpPost = null;
	protected String cookieKey = null;

	protected boolean alertFlag = true; // 是否弹出通讯进度条

	protected static CustomDialog pd; // 通讯进度条
	
	protected static CustomDialog pdc; // 倒计时进度条

	private static final String TAG = "BaseHttpEngine"; // add by wez 2011.01.06
	public static boolean isReClicked = false;

	/** 通信失败后是否回退上个页面 */
	public static boolean canGoBack = false;

	public void setCancleFlag(boolean cancleFlag) {
		this.cancleFlag = cancleFlag;
		if (this.cancleFlag) {
			if (httpGet != null) {
				httpGet.abort();
			}
			if (httpPost != null) {
				httpPost.abort();
			}
		}
	}

	/**
	 * 拦截处理params
	 * 
	 * @param params
	 */
	protected Object paramsPre(Object params) {

		return params;
	}

	/**
	 * @param url
	 *            发送通信的地址
	 * @param method
	 *            GET POST 代表当前发送的是get或者post请求
	 * @param context
	 * @param nameValuePairs
	 *            POST 请求上送服务器的键值对
	 * @param dialogFlag
	 *            是否弹出通信提示框的标识
	 */
	public HashMap<String, Object> httpSend(String url, String method,
			Context context, Object params) {

		// 准备
		looperPrepare();

		// initProxy(context);
		HashMap<String, Object> result = null;

		try {
			// 拦截处理params
			params = paramsPre(params);

			// 记录request信息
			logRequestStart(url, params);

			// checkAndSetAlertStatus(dialogFlag, true);

			HttpResponse response = null;// 通讯响应对象

			// 根据请求方式post/get不同走不同方式
			if (ConstantGloble.GO_METHOD_GET.equals(method)) {// 如果method为get

				response = doGet(url, context, params);

			} else if (ConstantGloble.GO_METHOD_POST.equals(method)
					|| "".equals(method) || method == null) {

				response = doPost(url, context, params);
			}

			// 处理response
			result = dealWithResponse(response, params,url);
			return result;
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			result = new HashMap<String, Object>();
			if (e instanceof NetworkErrorException) {// 没有网络的异常
				result.put(ConstantGloble.HTTP_RESPOSE_CODE, 100);
			}
			result.put(ConstantGloble.HTTP_RESPOSE_CODE, 500);
			// 关闭通信框
			if (pd != null) {
				if (pd.isShowing()) {
					pd.dismiss();
				}
			}
			pd = null;

			return result;
		} finally {

			logRequestEnd(url);

			// checkAndSetAlertStatus(dialogFlag, false);

		}
	}

	/**
	 * do get
	 * 
	 * @param url
	 * @param context
	 * @param params
	 * @param response
	 * @throws Exception
	 * @return response
	 */
	protected HttpResponse doGet(String url, Context context, Object params)
			throws Exception {
		sClient.getCookieStore().clear();
		// 生成httpGet
		gennerateHttpGet(url, params);

		// 设置httpGet 的head
		httpGetHeaderSet();

		// 执行通讯，获得返回对象
		HttpResponse response = execute(httpGet, context);

		return response;
	}

	/**
	 * do Post
	 * 
	 * @param url
	 * @param context
	 * @Object params
	 * @param response
	 * @throws Exception
	 * @return response
	 */
	protected HttpResponse doPost(String url, Context context, Object params)
			throws Exception {
		sClient.getCookieStore().clear();
		// 生成httpPost
		gennerateHttpPost(url);

		// 设置httpPost的head
		httpPostHeaderSet();	
		//add by liwei for http1.1 消息推送主机自动关闭链接
		if(url.contains(SystemConfig.BASE_PUSH_RUL)){
			httpPostHeaderSetAutoClose();
		}
		
		// 设置httpPost的Entity
		httpPostEntitySet(params);

		// 执行通讯，获得返回对象
		HttpResponse response = execute(httpPost, context);

		return response;
	}

	/**
	 * 
	 * 设置 httpPost Entity
	 * 
	 */
	protected abstract void httpPostEntitySet(Object params);

	/**
	 * 
	 * 根据url和参数键值对生成HttpGet
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	protected abstract HttpGet gennerateHttpGet(String url, Object params);

	/**
	 * 
	 * 根据url和参数键值对生成HttpPost
	 * 
	 * @param url
	 * @param nameValuePairs
	 * @return
	 */
	public HttpPost gennerateHttpPost(String url) {
		httpPost = new HttpPost(url);

		String host = httpPost.getURI().getHost();
		String path = httpPost.getURI().getPath();

		/**
		 * 处理cookie key
		 */
		dealWithCookieKey(host, path);

		return httpPost;
	}

	/**
	 * 
	 * 设置 httpGet header
	 * 
	 */
	protected void httpGetHeaderSet() {
		httpGet.setHeader(ConstantGloble.USER_AGENT, getUserAgent());
		String cookie = BaseDroidApp.getInstanse().getAllCookie(httpGet.getURI().getHost());
		httpGet.setHeader(ConstantGloble.COOKIE, cookie);
//		String cookie = BaseDroidApp.getInstanse().getCookie(cookieKey);
//		if (!StringUtil.isNullOrEmpty(cookie)) {
//			LogGloble.i(ConstantGloble.COOKIE, ConstantGloble.COOKIE + ": "
//					+ cookie);
//			httpGet.setHeader(ConstantGloble.COOKIE, cookie);
//		}
//		Map<String,String> cookieMap = BaseDroidApp.getInstanse().getCookieMap(httpPost.getURI().getHost());
//		for(Map.Entry<String, String> entry:cookieMap.entrySet()){
//    		String key = entry.getKey();
//    		httpGet.setHeader(ConstantGloble.COOKIE, cookieMap.get(key));
//    	}
		
	}

	/**
	 * 
	 * 设置 httpPost header
	 * 
	 */
	protected abstract void httpPostHeaderSet();
	//add by liwei for http1.1 消息推送主机自动关闭链接
	protected abstract void httpPostHeaderSetAutoClose();

	/**
	 * 处理response
	 * 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected HashMap<String, Object> dealWithResponse(HttpResponse response,
			Object params,String url) throws Exception {
		// 从URL中取出对应的域名
		String[] dns = url.split("//");
		dns = dns[1].split("/");
		url = dns[0];
		
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		int reCode = 0;

		try {
			reCode = response.getStatusLine().getStatusCode();
			result.put(ConstantGloble.HTTP_RESPOSE_CODE, reCode);
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			result.put(ConstantGloble.HTTP_RESPOSE_CODE, 500);
			return result;
		}

		Header[] headers = response.getAllHeaders();
		for (int i = 0; i < headers.length; i++) {
			Header header = headers[i];
			LogGloble.i("Header ", header.getName() + "=" + header.getValue());
			if (header.getName().equals("Set-Cookie")) {
//				BaseDroidApp.getInstanse().setCookie(header.getValue(),
//						cookieKey);

				//String [] cookie = header.getValue().split("=");
 				BaseDroidApp.getInstanse().SetCookie(header.getValue(),url);
				// 以下代码为保留Cookie到默认路径可以提供webView 使用
				// CookieManager cookieManager = CookieManager.getInstance();
				// CookieSyncManager.createInstance(BaseDroidApp.getInstanse());
				// cookieManager.setAcceptCookie(true);
				// cookieManager.setCookie("http://22.188.20.117:9095/BII/bii.do",
				// header.getValue());
				// CookieSyncManager.getInstance().sync(); //强制同步代码
			}
		}

		if (reCode == HttpStatus.SC_OK) {
			if (params instanceof BiiRequest) {
				result.put(ConstantGloble.BII_REQUETMETHOD,
						((BiiRequest) params).getMethod());
			}
			convertResponse(response, result);
		}

		return result;
	}

	/**
	 * 处理CookieKey
	 * 
	 * @param host
	 * @param path
	 */
	protected void dealWithCookieKey(String host, String path) {
		String contextPath = "";
		if (!StringUtil.isNullOrEmpty(path)) {
			String[] paths = path.split("/");
			if (!StringUtil.isNullOrEmpty(path)) {
				if (paths.length > 1) {
					contextPath = paths[1];
				}
			}
		}

		cookieKey = host + "." + contextPath;
		LogGloble.i(TAG, this.getClass().getSimpleName() + " CookieKey is:  "
				+ cookieKey);
	}

	/**
	 * 转换Response
	 * 
	 * @param response
	 * @throws Exception
	 */
	protected abstract void convertResponse(HttpResponse response,
			HashMap<String, Object> result) throws Exception;

	// /**
	// * 检查和设置AlertFlag
	// */
	// public void checkAndSetAlertStatus(boolean alertFlag, boolean status) {
	// if (alertFlag) {
	// setAlertStatus(status);
	// }
	// }

	/**
	 * logRequest 记录请求参数 -satrt
	 */
	public void logRequestStart(String url, Object params) {
//		LogGloble.w(TAG,
//				this.getClass().getSimpleName() + " " + this.hashCode()
//						+ "  url= " + url + " start");
//		LogGloble.i(TAG, "url:" + url);
//		LogGloble.i(TAG, "params:" + params);
	}

	/**
	 * logRequest 记录请求参数 -End
	 */
	public void logRequestEnd(String url) {
//		LogGloble.w(TAG,
//				this.getClass().getSimpleName() + " " + this.hashCode()
//						+ " url= " + url + " end");
	}

	/**
	 * looperPrepare
	 */
	public void looperPrepare() {
		try {
			Looper.prepare();// 与UI交互需要prepare
		} catch (Exception e) {
			LogGloble.w(TAG, "Looper.prepare error");
		}
	}

	/**
	 * 方法功能说明：初始化代理，在这个方法中设置需要的代理配置，它主要是根据当前默认的接入点来设置这个代理
	 * 
	 * @param con
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws KeyStoreException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
	public static void initProxy(Context con) throws Exception {
		LogGloble.v(TAG, "initProxy ...");
		// 设置基础数据
		HttpParams params = new BasicHttpParams();// 设置参数列表对象
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);// 设置版本
		HttpProtocolParams.setContentCharset(params,
				ConstantGloble.CONTENT_CHARSET);// 设置编码类型
		HttpProtocolParams.setUseExpectContinue(params, true);// 设置异常是否继续???
		HttpProtocolParams.setUserAgent(params, getUserAgent());

		// 设置链接池
		ConnPerRoute connPerRoute = new ConnPerRouteBean(
				ConstantGloble.CONN_PER_ROUTE_BEAN);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);// 设置每个路由最大链接数
		ConnManagerParams.setMaxTotalConnections(params,
				ConstantGloble.MAX_TOTAL_CONNECTIONS);// 设置最大链接数

		// 设置超时
		HttpConnectionParams.setStaleCheckingEnabled(params, false);
		HttpConnectionParams.setConnectionTimeout(params,
				ConstantGloble.CONNECTION_TIME_OUT);// 设置链接超时时间
		HttpConnectionParams.setSoTimeout(params,
				ConstantGloble.SOCKET_TIME_OUT);// 设置socket超时时间
		HttpConnectionParams.setSocketBufferSize(params,
				ConstantGloble.SOCKET_BUFFER_SIZE);// 设置sokect缓存最大字节数

		// 设置不验证证书
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		trustStore.load(null, null);
		SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
		sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		// 设置不验证证书

		// 设置http/https通讯模式
		SchemeRegistry schReg = new SchemeRegistry(); // 注册通讯模式对象

		schReg.register(new Scheme(ConstantGloble.HTTP, PlainSocketFactory
				.getSocketFactory(), ConstantGloble.HTTP_PORT));// 注册http模式，普通socket

		// 生产模式需要验证https
		if (SystemConfig.ENV.equals(SystemConfig.ENV_PRD)) {
			// // 注册https模式，SSLsoket
			SSLSocketFactory sh = SSLSocketFactory.getSocketFactory();
			sh.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
			schReg.register(new Scheme(ConstantGloble.HTTPS, sh/*SSLSocketFactory
					.getSocketFactory()*/, ConstantGloble.HTTPS_PORT));
		} else {
			// 注册不验证证书的 https模式
			schReg.register(new Scheme(ConstantGloble.HTTPS, sf,
					ConstantGloble.HTTPS_PORT));// 注册https模式，SSLsoket
		}

		// 生成链接管理对象，把设置的参数和模式设置进去
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);
		sClient = new DefaultHttpClient(conMgr, params);// 发送请求的对象
		sClient.getCookieStore().clear();
		setRedirection(); // 设置重定向
		networkInfo = ((ConnectivityManager) con.getApplicationContext()
				.getSystemService("connectivity")).getActiveNetworkInfo();
		String proxy = android.net.Proxy.getDefaultHost();
		int ports = android.net.Proxy.getDefaultPort() == -1 ? 80
				: android.net.Proxy.getDefaultPort();

		if (isWapNetwork()) {// 注意，is
			if (proxy != null) {
				hostname = proxy;
				hostport = ports;
				HttpHost httpHost = new HttpHost(proxy, ports, "http");
				sClient.getParams().setParameter("http.route.default-proxy",
						httpHost);
			} else {
				sClient.getParams().removeParameter("http.route.default-proxy");
			}
		}

		// // 根据代理地址是否为空设置是否需要代理
		// needProxy = (proxyHost != null);
		//
		// WifiManager wifiManager = (WifiManager) con
		// .getSystemService(Context.WIFI_SERVICE);
		// // 判断如果wifi开启，就不需要设置代理了
		// if (wifiManager.isWifiEnabled()) {
		// needProxy = false;
		// }
		//
		// if (needProxy) {// 如果需要代理
		// hostname = android.net.Proxy.getDefaultHost();// 代理主机名
		// hostport = android.net.Proxy.getDefaultPort();// 代理主机端口
		// HttpHost proxy = new HttpHost(hostname, hostport);// 代理主机对象
		// sClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
		// proxy);// 请求对象设置代理
		// } else {
		// hostname = "";
		// hostport = 0;
		// }
	}

	private static NetworkInfo networkInfo;

	public static boolean isWapNetwork() {
		// return this.mUseWap;
		if (networkInfo != null && networkInfo.getType() == 0) {// 0表示是wap请求，你可以去检查一下0表示的是哪个宏
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 设置重定向
	 */
	protected static void setRedirection() {
		// 处理重定向 add by wez 2012.1102
		sClient.setRedirectHandler(new DefaultRedirectHandler() {
			@Override
			public boolean isRedirectRequested(HttpResponse response,
					HttpContext context) {
				boolean isRedirect = super.isRedirectRequested(response,
						context);
				if (!isRedirect) {
					int responseCode = response.getStatusLine().getStatusCode();
					if (responseCode == HttpStatus.SC_MOVED_TEMPORARILY
							|| responseCode == HttpStatus.SC_MOVED_PERMANENTLY
							|| responseCode == HttpStatus.SC_SEE_OTHER
							|| responseCode == HttpStatus.SC_TEMPORARY_REDIRECT) {
						return true;
					}
				}
				return isRedirect;
			}

			@Override
			public URI getLocationURI(HttpResponse response, HttpContext context)
					throws ProtocolException {
				String newUri = response.getLastHeader("Location").getValue();
				return URI.create(newUri);
			}

		});
	}

	/**
	 * 方法功能说明：发送get请求
	 * 
	 * @param get
	 *            HttpGet类的实例对象
	 * @return HttpResponse 对返回的流，响应头等信息的封装
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @see BTCHttpConnection#httpSend（）
	 */
	public HttpResponse execute(HttpGet get, Context con) throws Exception {
		if (!DeviceUtils.haveInternet(con)) {
			LogGloble.v(TAG, "get 没有网络可用");
			throw new NetworkErrorException("错误信息");
		}
		// TODO
		LogGloble.v(TAG, "execute HttpGet " + get.hashCode() + " start ...");
		HttpResponse response;
		if (needProxy) {
			response = sClient.execute(get);
		} else {
			response = sClient.execute(get);
		}
		LogGloble.v(TAG, "execute HttpGet " + get.hashCode() + " finish ...");
		return response;
	}

	/**
	 * 方法功能说明：发送Post请求
	 * 
	 * @param get
	 *            HttpPost类的实例对象
	 * @return HttpResponse 对返回的流，响应头等信息的封装
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws UnrecoverableKeyException
	 * @throws CertificateException
	 * @throws KeyStoreException
	 * @see BTCHttpConnection#httpSend()
	 */
	public HttpResponse execute(HttpPost post, Context con) throws Exception {
		if (!DeviceUtils.haveInternet(con)) {
			LogGloble.v(TAG, "post 没有网络可用");
			throw new NetworkErrorException(con.getResources().getString(
					R.string.communication_fail));
		}
		LogGloble.v(TAG, "execute HttpPost " + post.hashCode() + " start ...");
		HttpResponse response = null;
		if (needProxy) {
			response = sClient.execute(post);
		} else {
			response = sClient.execute(post);

		}

		return response;
	}

	/**
	 * 获取cookie保存内容
	 * 
	 * @return
	 */
	public static synchronized CookieStore getCookieStore() {
		return sClient.getCookieStore();
	}

	/**
	 * 方法功能说明：把流转换成字符串
	 * 
	 * @param
	 * @return String
	 * @see
	 */
	public static String inputStream2String(InputStream inStream) {
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		// add finally by wez 2013.01.11
		try {
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inStream.read(buffer)) != -1) {// 把字符串读取到buffer，并判断是否读取到结尾
				outSteam.write(buffer, 0, len);// 把buffer写入到输出流
			}

			String out = new String(outSteam.toByteArray(),
					ConstantGloble.DEFAULT_ENCODE);
			// 把输出流根据UTF-8编码转为字符串对象返回
			return out;
		} catch (Exception e) {
			LogGloble.exceptionPrint(e);
			return "";
		} finally {
			try {
				outSteam.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LogGloble.exceptionPrint(e);
			}// 读取完毕关闭输出流
		}

	}

	/**
	 * 取得user agent
	 * 
	 * @return
	 */
	public static String getUserAgent() {
		return ConstantGloble.APP_USER_AGENT_PREFIX + ConstantGloble.SLASH
				+ SystemConfig.APP_VERSION;
	}

	/**
	 * 
	 * 转化请求参数
	 * 
	 * @author wez
	 * @param parameters
	 * @return Object
	 */
	protected abstract Object convertParameters(Object parameters);

	/**
	 * 为http请求URL添加参数
	 * 
	 * @param url
	 *            http请求url
	 * @param nameValuePairs
	 *            参数键值对
	 * @return url (带参数)
	 * 
	 * @author wez
	 * @date 2012-10-29 10:06:25
	 */
	protected String addListParamsToHttpUrl(String url,
			List<NameValuePair> nameValuePairs) {
		if (StringUtil.isNullOrEmpty(nameValuePairs)) {
			return url;
		}

		// 判断URL是否以问号结尾
		if (!url.endsWith("?")) {
			url += "?";
		}

		// 生成参数字符串
		String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
		url += paramString;

		LogGloble.i(TAG, "addListParamsToHttpUrl coded url->" + url);

		return url;
	}

	public void setAlertFlag(boolean flag) {
		alertFlag = flag;
	}

	/**
	 * 通信框，通信异常时点击确定回到上个页面（防止出现白板页面）
	 */
	public static void showProgressDialogCanGoBack() {
		canGoBack = true;
		if (pd != null) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
			pd = null;
		}
		if (pd == null) {
			pd = CustomDialog.createProgressDialog(BaseDroidApp.getInstanse()
					.getCurrentAct());
		}
	}

	/**
	 * 显示通信框
	 */
	public static void showProgressDialog() {
		LogGloble.i("BiiHttpEngine","通讯框打开");
		showProgressDialog(null);
	}

	/**
	 * 显示通信框
	 */
	public static void showProgressDialog(OnClickListener closeListener) {
		LogGloble.i("BiiHttpEngine","通讯框打开");
		canGoBack = false;
		try {
			if (pd == null) {
				pd = CustomDialog.createProgressDialog(BaseDroidApp
						.getInstanse().getCurrentAct(), closeListener);
			}
		} catch (BadTokenException e) {
			LogGloble.w(TAG, "HttpEngine "
					+ " showing loading dialog BadTokenException", e);

		}
	}

	/**
	 * 屏蔽掉关闭通信框的按钮
	 */
	public static void dissmissCloseOfProgressDialog() {
		if (pd != null && pd.isShowing()) {
//			((Button) pd.getWindow().getDecorView().findViewById(R.id.btnClose))
//					.setVisibility(View.GONE);
			((View) pd.getWindow().getDecorView().findViewById(R.id.btn_close))
					.setVisibility(View.INVISIBLE);


		}
	}

	/**
	 * 关闭通信框
	 */
	public static void dissMissProgressDialog() {
		LogGloble.i("BiiHttpEngine","关闭通讯框");
		LoadingDialog.closeDialog();
		MyRelativeLayout.lastClickTime = System.currentTimeMillis();
		if (pd != null) {
			if (pd.isShowing()) {
				pd.dismiss();
			}
		}
		pd = null;
		dissMissPdcDialog();
	}

	/**dxd   
	 * 显示转账倒计时 通信框
	 */
	public  static void showTranProgressDialog(OnClickListener closeListener, Handler myHandler) {
		try {
			dissMissProgressDialog();
			if (pdc == null) {
				pdc = CustomDialog.createFincProgressDialog(BaseDroidApp
						.getInstanse().getCurrentAct(), closeListener,myHandler);
				
			}
		} catch (BadTokenException e) {
			LogGloble.w(TAG, "HttpEngine "
					+ " showing loading dialog BadTokenException", e);

		}
	}
	/**
	 * 关闭通信框
	 */
	public static void dissMissPdcDialog() {
		LogGloble.i("BiiHttpEngine","关闭倒计时通讯框");
		MyRelativeLayout.lastClickTime = System.currentTimeMillis();
		if (pdc != null) {
			if (pdc.isShowing()) {
				pdc.dismiss();
			}
		}
		pdc = null;
	}
	
	 /**dxd   
		 * 显示转账失败倒计时 通信框
		 */
		public static void showFailProgressDialog(SpannableString sp ,OnClickListener closeListener, Handler handler) {
			canGoBack = false;
			try {
				
				dissMissProgressDialog();
				if (pdc == null) {
					pdc = CustomDialog.createFailProgressDialog(BaseDroidApp
							.getInstanse().getCurrentAct(),sp, closeListener,handler);
				}
			} catch (BadTokenException e) {
				LogGloble.w(TAG, "HttpEngine "
						+ " showing loading dialog BadTokenException", e);

			}
		}

		
		
		/**dxd   
		 * 显示转账结果未知 通信框（unknown）
		 * skipListener 页面跳转事件
		 * 
		 */
		public static void showUnknownProgressDialog(OnClickListener closeListener,OnClickListener  skipListener) {
			canGoBack = false;
			try {
				if (pd == null) {
					pd = CustomDialog.createUnknownProgressDialog(BaseDroidApp
							.getInstanse().getCurrentAct(), closeListener,skipListener);
				}
			} catch (BadTokenException e) {
				LogGloble.w(TAG, "HttpEngine "
						+ " showing loading dialog BadTokenException", e);

			}
		}



	// /**
	// * 设置通信进度框展示或者消失
	// *
	// * @param flag
	// * </p> true 展示 progressdialog </p> false 消失 progressdialog</p>
	// * @author qr
	// */
	// protected void setAlertStatus(final boolean flag) {
	// BaseDroidApp.getInstanse().getCurrentAct()
	// .runOnUiThread(new Runnable() {
	//
	// @Override
	// public void run() {
	// // true 展示内容
	// if (flag) {
	// LogGloble.v(TAG, "HttpEngine " + this.hashCode()
	// + " open loading dialog");
	// // add by wez 2012.11.13 fix bug bug-view-1084.html
	// if (pd != null) {
	// if (pd.isShowing()) {
	// pd.dismiss();
	// }
	// }
	// pd = null;
	// // pd = new CustomProgressDialog(BaseDroidApp
	// // .getInstanse().getCurrentAct());
	// // 但且仅当pd的上下文等于控制中心的上下文时，才弹出对话框
	//
	// try {
	// pd =CustomDialog.createProgressDialog(BaseDroidApp
	// .getInstanse().getCurrentAct());
	// } catch (BadTokenException e) {
	// LogGloble.w(
	// TAG,
	// "HttpEngine "
	// + this.hashCode()
	// + " showing loading dialog BadTokenException",
	// e);
	//
	//
	// }
	// }// false 将提示框消失掉
	// else {
	// LogGloble.v(TAG, "HttpEngine " + this.hashCode()
	// + " close loading dialog");
	// if (pd != null) {
	// if (pd.isShowing()) {
	// try {
	// pd.dismiss();
	// } catch (BadTokenException e) {
	// LogGloble.w(
	// TAG,
	// "HttpEngine "
	// + this.hashCode()
	// + " close loading dialog BadTokenException",
	// e);
	// }
	//
	// }
	// } else {
	// LogGloble.v(TAG,
	// "HttpEngine " + this.hashCode()
	// + " pd is null");
	// }
	// }
	// }
	// });
	// }

}
