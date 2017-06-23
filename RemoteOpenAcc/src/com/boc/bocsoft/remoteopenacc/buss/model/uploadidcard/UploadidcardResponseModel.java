package com.boc.bocsoft.remoteopenacc.buss.model.uploadidcard;

import com.boc.bocma.serviceinterface.op.interfacemodel.uploadidcard.MAOPUploadidcardResponseModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 1.2.9 【SA9192】身份证图像上传
 * 
 * @author gwluo
 * 
 */
public class UploadidcardResponseModel implements
		BaseResultModel<UploadidcardResponseModel> {

	/**
	 * 图像版本号 X（10） 正常返回本次上送的图像版本号，异常返回错误信息！
	 */
	public String picVersion;

	@Override
	public UploadidcardResponseModel parseResultModel(Object resultModel) {
		MAOPUploadidcardResponseModel model = (MAOPUploadidcardResponseModel) resultModel;
		picVersion = model.picVersion;
		return this;
	}

}
