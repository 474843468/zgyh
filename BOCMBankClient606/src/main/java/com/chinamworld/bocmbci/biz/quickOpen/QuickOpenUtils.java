package com.chinamworld.bocmbci.biz.quickOpen;


public class QuickOpenUtils {
	/**
	 * 处理返回数据
	 * 
	 * @param result
	 * @return
	 */
//	public static Object httpResponseDeal(Object result) {
//		BiiResponse biiResponse = (BiiResponse) result;
//		List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
//		BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
//		return biiResponseBody.getResult();
//	}
	
	/** 根据接口性别代码返回中文性别 */
	public static String getGender(String genderCode){
		if (genderCode.equals("0")) {
			return "女";
		} else if (genderCode.equals("1")) {
			return "男";
		} else {
			return "-";
		}
	}
}
