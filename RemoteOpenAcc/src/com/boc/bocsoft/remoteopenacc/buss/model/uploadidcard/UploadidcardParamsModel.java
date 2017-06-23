package com.boc.bocsoft.remoteopenacc.buss.model.uploadidcard;

import com.boc.bocma.serviceinterface.op.interfacemodel.uploadidcard.MAOPUploadidcardParamsModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseParamsModel;

/**
 * 1.2.9 【SA9192】身份证图像上传
 * 
 * @author gwluo
 * 
 */
public class UploadidcardParamsModel implements BaseParamsModel {

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
	public MAOPUploadidcardParamsModel transformParamsModel() {
		MAOPUploadidcardParamsModel model = new MAOPUploadidcardParamsModel();
		model.imageNameA = imageNameA;
		model.imageA = imageA;
		model.imageNameB = imageNameB;
		model.imageB = imageB;
		model.metaData = metaData;
		model.operType = operType;
		model.piCid = piCid;
		return model;
	}

}
