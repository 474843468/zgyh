package com.chinamworld.bocmbci.biz.bocinvt;

import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.utils.StringUtil;

public class BocinvtUtils {
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
	
	/**
	 * 构造spinner数据
	 * @param list
	 * @param key
	 * @return
	 */
//	public static List<String> initSpinnerData(List<Map<String, Object>> list,String key) {
//		if (StringUtil.isNullOrEmpty(list)) {
//			return null;
//		}
//		ArrayList<String> mList = new ArrayList<String>();
//		for (int i = 0; i < list.size(); i++) {
//			if (key.equals(Comm.ACCOUNTNUMBER) || key.equals(BocInvt.ACCOUNTNO)) {
//				mList.add(StringUtil.getForSixForString((String) list.get(i).get(key)));
//			}else{
//				mList.add((String) list.get(i).get(key));
//			}
//		}
//		return mList;
//	}
	
	/**
	 * 将返回英文缩略频率转换成中文
	 * @param freq
	 * @return
	 */
	public static String frequencyTransForm(String freq){
		String result = ConstantGloble.FINC_COMBINQURY_NONE;
		if (StringUtil.isNull(result)) {
			return result;
		}
		if (freq.endsWith("D")) {
			result = freq.substring(0, freq.length()-1)+"天";
		}else if(freq.endsWith("W")){
			result = freq.substring(0, freq.length()-1)+"周";
		}else if (freq.endsWith("M")){
			result = freq.substring(0, freq.length()-1)+"月";
		}else if(freq.endsWith("Y")){
			result = freq.substring(0, freq.length()-1)+"年";
		}
		return result;
	}

}
