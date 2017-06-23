package com.boc.bocma.serviceinterface.op.interfacemodel.debitcardtranshistory;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.common.MAOPBankTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 借记卡批量查询交易流水
 */

public class MAOPDebitCardTransHistoryModel extends MAOPBaseResponseModel {
    private static final String SAP_LIST_KEY = "saplist";
    private static final String ITEM_LIST_KEY = "itemlist";
    private static final String IDENTIFIER_KEY = "lmtamt";
    
    /**
     * key为卡唯一标识，value为卡的交易流水集合
     */
    HashMap<String, ArrayList<MAOPBankTransaction>> transactions = 
            new HashMap<String, ArrayList<MAOPBankTransaction>>();
    
    public MAOPDebitCardTransHistoryModel(JSONObject jsonResponse) throws JSONException {
        JSONArray cardListWithTrans = jsonResponse.optJSONArray(SAP_LIST_KEY);
        for (int i = 0; cardListWithTrans !=null && i < cardListWithTrans.length(); i++) {
            JSONObject cardWithTrans = cardListWithTrans.getJSONObject(i);
            String identifier = cardWithTrans.optString(IDENTIFIER_KEY);
            transactions.put(identifier, new ArrayList<MAOPBankTransaction>());
            JSONArray transList = cardWithTrans.optJSONArray(ITEM_LIST_KEY);
            for (int j = 0; transList != null && j < transList.length(); j++) {
                JSONObject trans = transList.getJSONObject(j);
                MAOPBankTransaction transaction = MAOPBankTransaction.CREATOR.createFromJson(trans);
                transactions.get(identifier).add(transaction);
            }
        }
    }

    public MAOPDebitCardTransHistoryModel() {
    }

    public List<MAOPBankTransaction> getTransactionsByCardIdentifier(String identifier) {
        return transactions.get(identifier);
    }
    
    public Set<String> getCardIdentifierList() {
        return transactions.keySet();
    }
    
    public static final Creator<MAOPDebitCardTransHistoryModel> CREATOR = 
            new Creator<MAOPDebitCardTransHistoryModel>() {
                @Override
                public MAOPDebitCardTransHistoryModel createFromJson(JSONObject jsonResponse) throws JSONException {
                    return new MAOPDebitCardTransHistoryModel(jsonResponse);
                }
        
    };
}
