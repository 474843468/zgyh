package com.chinamworld.bocmbci.fidget;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.SystemConfig;
import com.chinamworld.bocmbci.log.LogGloble;


/** 
*类功能描述：程序通过这个接口去发送http请求。在这个程序中访问互联网是通过绑定gprs的方式，也就是必须通过wap网关去进行访问。
*@author：秦
*@version：1.0
*@see 包名：com.chinamworld.btwapview.httpconnection
*/
public class BTCHttpManager {  
	private static DefaultHttpClient sClient;  //发送请求的对象
	private static boolean needProxy = false;  //是否需要设置代理
	private static String useragent;

	public static void init(Context context){
		useragent=getUserAgent(context);
		
	}
	
	/**
	 * 方法功能说明：组装user-agent请求头参数的值
	 * @param context 
	 * @return String 请求头信息user-agent的值
	 */
	private static String getUserAgent(Context context){
		//组装版本信息MBCCCB/Android/[程序版本号]/[页面版本号]/[设备id]
		StringBuffer sb = new StringBuffer();
		sb.append("Android|");
		sb.append(SystemConfig.APP_VERSION).append("|");
		String modle = Build.MODEL;//手机型号
		sb.append(ConstantGloble.APP_USER_AGENT_PRODUCT_NAME);
		sb.append("/");
		if("".equals(modle))
		{
			sb.append("Android00");
		}
		else
		{
		sb.append(modle);
		}
		sb.append("/BTWapView");
		LogGloble.i("apn", "user-agent:"+sb.toString());
		return sb.toString();
	}

	/**
	 * 方法功能说明：初始化代理，在这个方法中设置需要的代理配置，它主要是根据当前默认的接入点来设置这个代理
	 * @param xml 
	 * @return String 请求头信息user-agent的值
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws KeyStoreException 
	 * @throws IOException 
	 * @throws CertificateException 
	 * @throws UnrecoverableKeyException 
	 * @throws InterruptedException 
	 */
	public static void initProxy(Context context) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, IOException, UnrecoverableKeyException, InterruptedException {
		
		// modify by xxhr 2012-02-15 判断wifi是否打开，如果关闭，切换接入点，如果打开，不切换接入点
		// end
		
		// 设置基础数据       
		HttpParams params = new BasicHttpParams();//设置参数列表对象
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);//设置版本 
		HttpProtocolParams.setContentCharset(params, BTCGlobal.CONTENT_CHARSET);//设置编码类型
		HttpProtocolParams.setUseExpectContinue(params, true);//设置异常是否继续???
		HttpProtocolParams.setUserAgent(params,useragent);
		// 设置链接池           
		ConnPerRoute connPerRoute = new ConnPerRouteBean(BTCGlobal.CONN_PER_ROUTE_BEAN);
		ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);//设置每个路由最大链接数 
		ConnManagerParams.setMaxTotalConnections(params, BTCGlobal.MAX_TOTAL_CONNECTIONS);//设置最大链接数
		
		// 设置超时         
		HttpConnectionParams.setStaleCheckingEnabled(params, false);   
		HttpConnectionParams.setConnectionTimeout(params, BTCGlobal.CONNECTION_TIME_OUT);//设置链接超时时间
		HttpConnectionParams.setSoTimeout(params, BTCGlobal.SOCKET_TIME_OUT);//设置socket超时时间
		HttpConnectionParams.setSocketBufferSize(params, BTCGlobal.SOCKET_BUFFER_SIZE);//设置sokect缓存最大字节数
		
		// 设置客户端参数           
		HttpClientParams.setRedirecting(params, false);//设置中是否重定向     
		
		//设置不验证证书
//		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//		trustStore.load(null, null);
//		SSLSocketFactory sf = new BTCMySSLSocketFactory(trustStore);
//	    sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		//设置不验证证书
		
		//设置http/https通讯模式
		SchemeRegistry schReg = new SchemeRegistry();  //注册通讯模式对象
		schReg.register(new Scheme(BTCGlobal.HTTP, PlainSocketFactory.getSocketFactory(), BTCGlobal.HTTP_PROXY_PORT));//注册http模式，普通socket
		 schReg.register(new Scheme(ConstantGloble.HTTPS, SSLSocketFactory
				 .getSocketFactory(), ConstantGloble.HTTPS_PORT));
//		schReg.register(new Scheme(BTCGlobal.HTTPS, sf, BTCGlobal.HTTPS_PROXY_PORT));//注册https模式，SSLsoket
		//生成链接管理对象，把设置的参数和模式设置进去
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg); 
		sClient = new DefaultHttpClient(conMgr, params);//发送请求的对象
		//获取默认通讯代理主机ip
		String proxyHost = android.net.Proxy.getDefaultHost();
		//根据代理地址是否为空设置是否需要代理
		needProxy=(proxyHost != null);
		//判断如果wifi开启，就不需要设置代理了
		
		
		networkInfo = ((ConnectivityManager) context
				.getApplicationContext().getSystemService("connectivity"))
				.getActiveNetworkInfo();
		String proxy=android.net.Proxy.getDefaultHost();
		int ports = android.net.Proxy.getDefaultPort() == -1 ? 80 : android.net.Proxy.getDefaultPort();
		
		if (isWapNetwork()) {// 注意，is
			if (proxy != null) {
				HttpHost httpHost = new HttpHost(proxy, ports, "http");
				sClient.getParams().setParameter("http.route.default-proxy",
						httpHost);
			} else {
				sClient.getParams().removeParameter(
						"http.route.default-proxy");
			}
		}
	
		
//		if(wifiManager.isWifiEnabled()){
//			needProxy = false;
//		}
//		if(needProxy){//如果需要代理
////			BTCSystemLog.i("Font", "needProxy");
//			hostname=android.net.Proxy.getDefaultHost();//代理主机名
//			hostport=android.net.Proxy.getDefaultPort();//代理主机端口
//			HttpHost proxy = new HttpHost(hostname,hostport);//代理主机对象
//			sClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);//请求对象设置代理
//		}else{
////			BTCSystemLog.i("Font", "no_needProxy");
//			hostname="";
//			hostport=0;
//		}
	}
	
	private static NetworkInfo networkInfo;

	public  static boolean isWapNetwork() {
		// return this.mUseWap;
		if (networkInfo != null && networkInfo.getType() == 0) {// 0表示是wap请求，你可以去检查一下0表示的是哪个宏
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 方法功能说明：发送get请求 
	 * @param get HttpGet类的实例对象
	 * @return HttpResponse 对返回的流，响应头等信息的封装
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws UnrecoverableKeyException 
	 * @throws CertificateException 
	 * @throws KeyStoreException 
	 * @throws InterruptedException 
	 * @see BTCHttpConnection#httpSend（）
	 */
	public static HttpResponse execute(HttpGet get, Context context) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, InterruptedException {
		initProxy(context);
		HttpResponse response;
		if(needProxy){
			response = sClient.execute(get);
		}else{
			response = sClient.execute(get);
		}
		sClient = null;
		return response;
	}   
	
	/**
	 * 方法功能说明：发送Post请求 
	 * @param get HttpPost类的实例对象
	 * @return HttpResponse 对返回的流，响应头等信息的封装
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 * @throws UnrecoverableKeyException 
	 * @throws CertificateException 
	 * @throws KeyStoreException 
	 * @see BTCHttpConnection#httpSend（）
	 */
	public static HttpResponse execute(HttpPost post, Context context) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableKeyException, Exception{  
		initProxy(context);
		HttpResponse response;
		if(needProxy){
			response = sClient.execute(post);
		}else{
			response = sClient.execute(post);
		}
		sClient = null;
		return response;
	}
	
	/**
	 * 获取cookie保存内容
	 * @return
	 */
	public static synchronized CookieStore getCookieStore() {     
		return sClient.getCookieStore();        
	}
}   
	


