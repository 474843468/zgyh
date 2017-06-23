package com.boc.bocma.serviceinterface.op.interfacemodel.querynearbybranches;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询附近网点地理位置结果
 */
public class MAOPQueryNearbyBranchesModel extends MAOPBaseResponseModel {
    private static final String RCDCNT_KEY = "rcdcnt";
    
    //TODO 是否拼写错误（一般为saplist)
    private static final String SPALIST_KEY = "spalist";
    
    public MAOPQueryNearbyBranchesModel(JSONObject jsonResponse) throws JSONException {
        count = jsonResponse.optInt(RCDCNT_KEY);
        JSONArray infos = jsonResponse.optJSONArray(SPALIST_KEY);
        for (int i = 0; i < infos.length(); i++) {
            JSONObject info = infos.getJSONObject(i);
            geoInfos.add(MAOPBranchGEOInfo.CREATOR.createFromJson(info));
        }
    }

    public static final Creator<MAOPQueryNearbyBranchesModel> CREATOR = new Creator<MAOPQueryNearbyBranchesModel>() {
        @Override
        public MAOPQueryNearbyBranchesModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueryNearbyBranchesModel(jsonResponse);
        }
        
    };
    
    private int count;
    
    private List<MAOPBranchGEOInfo> geoInfos = new ArrayList<MAOPBranchGEOInfo>();
    /**
     * @return 附近网点位置信息
     */
    public List<MAOPBranchGEOInfo> getGEOInfo() {
        return geoInfos;
    }
    
    /**
     * @return 笔数
     */
    public int getCount() {
        return count;
    }
}
