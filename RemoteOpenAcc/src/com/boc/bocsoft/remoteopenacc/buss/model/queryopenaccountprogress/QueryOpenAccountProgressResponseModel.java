package com.boc.bocsoft.remoteopenacc.buss.model.queryopenaccountprogress;

import java.util.ArrayList;
import java.util.List;

import com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress.MAOPQueryOpenAccountProgressResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress.MAOQueryOpenAccountProgressResultModel;
import com.boc.bocsoft.remoteopenacc.common.model.BaseResultModel;

/**
 * 查询开户进度返回model
 * 
 * @author lxw
 * @version p601 1.新增4-20项返回字段 ;2.将appliStat新增一个状态6，标识为重发状态。3.
 *          将custFullName改成custName by lgw at 15.12.3
 */
public class QueryOpenAccountProgressResponseModel implements
		BaseResultModel<QueryOpenAccountProgressResponseModel> {

	public List<QueryOpenAccountProgressResultModel> list = new ArrayList<QueryOpenAccountProgressResultModel>();

	@Override
	public QueryOpenAccountProgressResponseModel parseResultModel(
			Object resultModel) {
		MAOPQueryOpenAccountProgressResponseModel result = (MAOPQueryOpenAccountProgressResponseModel) resultModel;
		List<MAOQueryOpenAccountProgressResultModel> list2 = result.getList();
		for (int i = 0; i < list2.size(); i++) {
			QueryOpenAccountProgressResultModel item = new QueryOpenAccountProgressResultModel();
			MAOQueryOpenAccountProgressResultModel resultModel2 = list2.get(i);
			item.bindEzCode = resultModel2.bindEzCode;
			item.bindEzExCode = resultModel2.bindEzExCode;
			item.bindEzExStat = resultModel2.bindEzExStat;
			item.bindEzStat = resultModel2.bindEzStat;
//			item.bindPhoneCode = resultModel2.bindPhoneCode;
//			item.bindPhoneStat = resultModel2.bindPhoneStat;
			item.bindVcardExStat = resultModel2.bindVcardExStat;
			item.bindVcardStat = resultModel2.bindVcardStat;
			item.creatCifStat = resultModel2.creatCifStat;
			item.creatVcardExStat = resultModel2.creatVcardExStat;
			item.creatVcardStat = resultModel2.creatVcardStat;
			item.failCode = resultModel2.failCode;
			item.failReason = resultModel2.failReason;
			item.msgStat = resultModel2.msgStat;
			item.openAccountType = resultModel2.openAccountType;
			item.openBocnetCode = resultModel2.openBocnetCode;
			item.openBocnetStat = resultModel2.openBocnetStat;
			item.openEzCode = resultModel2.openEzCode;
			item.openEzStat = resultModel2.openEzStat;
			item.openPhoneCode = resultModel2.openPhoneCode;
			item.openPhoneStat = resultModel2.openPhoneStat;
			item.orgCode = resultModel2.orgCode;
			item.orgName = resultModel2.orgName;
			item.othCardNo = resultModel2.othCardNo;
			item.queryRcpsStat = resultModel2.queryRcpsStat;
			item.uuid = resultModel2.uuid;
			item.vcardExNo = resultModel2.vcardExNo;
			item.vcardNo = resultModel2.vcardNo;
			list.add(item);
		}
		return this;
	}
}
