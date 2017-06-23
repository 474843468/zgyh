package com.boc.bocma.global;

import org.apache.http.client.CookieStore;

/**
 */
public class MAHttpContext {

    /**
     * http网络请求超时时间
     */
	public static final int TIMEOUT = 60 * 1000;
	/**
     * 开放平台http网络请求超时时间
     */
	public static final int OP_TIMEOUT = 60 * 1000;
	public static CookieStore cookieStore;

}
