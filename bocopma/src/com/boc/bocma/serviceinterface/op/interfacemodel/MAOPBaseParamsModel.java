package com.boc.bocma.serviceinterface.op.interfacemodel;

//import com.boc.bocop.sdk.service.BaseService;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;

import com.boc.bocma.global.OPURL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * 开放平台上送报文参数类。
 * 1、上送SAP的URL
 * 2、请求方式
 * 3、上送SAP的报文头（参见公共报文头，从OP SDK中获取）
 * 4、上送SAP报文体内容
 */
abstract public class MAOPBaseParamsModel {
    public abstract String getUrl();
    
    public String getMethod() {
        return HttpPost.METHOD_NAME;
    }
    
    public LinkedHashMap<String, String> getHead() {
    	LinkedHashMap<String, String> head = new LinkedHashMap<String, String>();
    	head.put("clentid", OPURL.APPKEY);
    	head.put("userid", "");
    	head.put("acton", "");
    	head.put("chnflg", "1");
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		// 获取当前时间
		String nowData = format.format(new Date(System.currentTimeMillis()));
		head.put("trandt", nowData);
		SimpleDateFormat formatTime = new SimpleDateFormat("HHmmss");
		// 获取当前时间
		String nowTime = formatTime
				.format(new Date(System.currentTimeMillis()));
		head.put("trantm", nowTime);
		//增加UUID
		head.put("uuid", UUID.randomUUID().toString().replaceAll("-", "")); 			
//        return BaseService.genSapHeader();
    	return head;
    }
    
    public abstract String getJsonBody() throws JSONException;
    
}