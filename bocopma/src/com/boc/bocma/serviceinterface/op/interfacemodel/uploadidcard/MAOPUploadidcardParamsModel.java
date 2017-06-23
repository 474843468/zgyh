package com.boc.bocma.serviceinterface.op.interfacemodel.uploadidcard;

import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;

/**
 * 1.2.9 【SA9192】身份证图像上传
 * 
 * @author gwluo
 * 
 */
public class MAOPUploadidcardParamsModel extends MAOPBaseParamsModel {

	private static final String INTERFACE_URL = "uploadidcard";

	private static final String IMAGENAMEA = "imageNameA";
	private static final String IMAGEA = "imageA";
	private static final String IMAGENAMEB = "imageNameB";
	private static final String IMAGEB = "imageB";
	private static final String OPERTYPE = "operType";
	private static final String PICID = "piCid";
	private static final String METADATA = "metaData";

	/**
	 * 正面图片名称 X（60） 证件号_a.jpeg Y
	 */
	public String imageNameA;
	/**
	 * 正面图片流 X（340000） 单张图片不超过2M Y
	 */
	public String imageA;
	/**
	 * 反面图片名称 X(60) 证件号_b.jpeg Y
	 */
	public String imageNameB;
	/**
	 * imageB 反面图片流 X（340000） 单张图片不超过2M Y
	 */
	public String imageB;
	/**
	 * 影像操作类型 X(1) A：新增；E：修改
	 */
	public String operType;
	/**
	 * 影像ID X(32) 分配的影像ID
	 */
	public String piCid;
	/**
	 * 业务数据索引 身份证号|姓名拼接
	 */
	public String metaData;

	@Override
	public String getUrl() {
		return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
	}

	@Override
	public String getJsonBody() throws JSONException {
		JSONObject body = new JSONObject();
		body.put(IMAGENAMEA, imageNameA);
		body.put(IMAGEA, imageA);
		body.put(IMAGENAMEB, imageNameB);
		body.put(IMAGEB, imageB);
		body.put(OPERTYPE, operType);
		body.put(PICID, piCid);
		body.put(METADATA, metaData);
		return body.toString();
	}

}
