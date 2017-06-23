package com.boc.bocma.serviceinterface.op.interfacemodel.querykeywords;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询关键字结果类
 */
public class MAOPQueryKeywordsModel extends MAOPBaseResponseModel {
    public MAOPQueryKeywordsModel(JSONObject jsonResponse) throws JSONException {
        recordCount = jsonResponse.optInt(RECORD_COUNTS_KEY);
        JSONArray array = jsonResponse.optJSONArray(RECORD_LIST_KEY);
        for (int i = 0; array != null && i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            KeywordEntity entity = new KeywordEntity();
            entity.keyword = obj.getString(KEYWORD_KEY);
            entity.typeId = obj.getInt(TYPE_ID_KEY);
            keywordList.add(entity);
        }
    }

    public MAOPQueryKeywordsModel() {
    }

    private static final String RECORD_COUNTS_KEY = "recordcounts";
    private static final String RECORD_LIST_KEY = "recordlists";
    private static final String KEYWORD_KEY = "keyword";
    private static final String CONSUMPTION_TYPE_KEY = "cusumtype";
    private static final String TYPE_ID_KEY = "typeid";
    
    public static final Creator<MAOPQueryKeywordsModel> CREATOR = new Creator<MAOPQueryKeywordsModel>() {
        @Override
        public MAOPQueryKeywordsModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueryKeywordsModel(jsonResponse);
        }
    };
    
    private int recordCount;
    
    private List<KeywordEntity> keywordList = new ArrayList<KeywordEntity>();
    
    /**
     * @return 关键字列表
     */
    public List<KeywordEntity> getKeywordList() {
        return keywordList;
    }

    /**
     * @return the recordCount
     */
    public int getRecordCount() {
        return recordCount;
    }

    public static class KeywordEntity {
        public String keyword;
        public int typeId;
    }
}
