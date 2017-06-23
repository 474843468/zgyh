package com.boc.bocma.serviceinterface.op.interfacemodel.queryopenaccountprogress;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

/**
 * 1.2.6 【SA9184】查询开户进度
 * 
 * @author
 * @version p601 1.新增4-20项返回字段 ;2.将appliStat新增一个状态6，标识为重发状态。3.
 *          将custFullName改成custName by lgw at 15.12.3
 */
public class MAOPQueryOpenAccountProgressResponseModel extends
		MAOPBaseResponseModel {
	private final static String RESULTS = "results";

	private List<MAOQueryOpenAccountProgressResultModel> list = new ArrayList<MAOQueryOpenAccountProgressResultModel>();

	public List<MAOQueryOpenAccountProgressResultModel> getList() {
		return list;
	}

	public void setList(List<MAOQueryOpenAccountProgressResultModel> list) {
		this.list = list;
	}

	public MAOPQueryOpenAccountProgressResponseModel(JSONObject jsonResponse) {
		try {
			JSONArray jsonArray = jsonResponse.getJSONArray(RESULTS);
			for (int i = 0; i < jsonArray.length(); i++) {
				MAOQueryOpenAccountProgressResultModel item = new MAOQueryOpenAccountProgressResultModel();
				item.parserJson(jsonArray.getJSONObject(i));
				list.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static final Creator<MAOPQueryOpenAccountProgressResponseModel> CREATOR = new Creator<MAOPQueryOpenAccountProgressResponseModel>() {
		@Override
		public MAOPQueryOpenAccountProgressResponseModel createFromJson(
				JSONObject jsonResponse) throws JSONException {
			return new MAOPQueryOpenAccountProgressResponseModel(jsonResponse);
		}

	};
}
