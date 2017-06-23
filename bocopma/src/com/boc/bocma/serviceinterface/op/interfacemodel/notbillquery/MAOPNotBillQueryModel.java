package com.boc.bocma.serviceinterface.op.interfacemodel.notbillquery;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.common.MAOPCreditCardTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 未出账单查询结果类
 */
public class MAOPNotBillQueryModel extends MAOPBaseResponseModel {
    private static final String COIN_FLAG_KEY = "coinflag";
    private static final String RECORD_COUNT_KEY = "record_count";
    private static final String SAP_LIST_KEY = "saplist";
    
    private int coinFlag;
    private int recordCount;
    private List<MAOPCreditCardTransaction> transList = new ArrayList<MAOPCreditCardTransaction>();
    
    public MAOPNotBillQueryModel(JSONObject jsonResponse) throws JSONException {
        coinFlag = jsonResponse.optInt(COIN_FLAG_KEY);
        recordCount = jsonResponse.optInt(RECORD_COUNT_KEY);
        JSONArray array = jsonResponse.optJSONArray(SAP_LIST_KEY);
        for (int i = 0; array != null && i < array.length(); i++) {
            MAOPCreditCardTransaction transaction = 
                    MAOPCreditCardTransaction.CREATOR.createFromJson(array.getJSONObject(i));
            transList.add(transaction);
        }
    }

    public MAOPNotBillQueryModel() {
    }

    /**
     * @return 是否还有后续数据包 1-还有后续数据, 0-无后续数据  
     */
    public int getCoinFlag() {
        return coinFlag;
    }
    
    public int getRecordCount() {
        return recordCount;
    }
    
    public List<MAOPCreditCardTransaction> getTransactionList() {
        return transList;
    }
    
    public static final Creator<MAOPNotBillQueryModel> CREATOR = new Creator<MAOPNotBillQueryModel>() {
        @Override
        public MAOPNotBillQueryModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPNotBillQueryModel(jsonResponse);
        }
        
    };
}
