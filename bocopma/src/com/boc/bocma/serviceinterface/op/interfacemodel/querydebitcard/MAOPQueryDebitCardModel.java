package com.boc.bocma.serviceinterface.op.interfacemodel.querydebitcard;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;

public class MAOPQueryDebitCardModel extends MAOPBaseResponseModel {
    private static final String CARD_NUMBER_LIST_KEY = "saplist";
    private static final String CARD_NUMBER_KEY = "cardno";
    
    private List<String> cardNumbers = new ArrayList<String>();
    private List<MAOPDebitCardInfo> debitCardInfo = new ArrayList<MAOPDebitCardInfo>();
    
    public MAOPQueryDebitCardModel(JSONObject jsonResponse) throws JSONException {
        JSONArray array = jsonResponse.optJSONArray(CARD_NUMBER_LIST_KEY);
        for (int i = 0; array != null && i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            String cardNumber = object.optString(CARD_NUMBER_KEY);
            cardNumbers.add(cardNumber);
            MAOPDebitCardInfo model = MAOPDebitCardInfo.CREATOR.createFromJson(object);
            debitCardInfo.add(model);
        }
    }

    public MAOPQueryDebitCardModel() {
    }

    /**
     * @return 卡号列表
     */
    public List<String> getCardNumbers() {
        return cardNumbers;
    }
    
    /**
     * 获取借记卡信息列表
     * @return
     */
    public List<MAOPDebitCardInfo> getDebitCardInfo(){
    	return debitCardInfo;
    }
    
    public static final Creator<MAOPQueryDebitCardModel> CREATOR = new Creator<MAOPQueryDebitCardModel>() {
        @Override
        public MAOPQueryDebitCardModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueryDebitCardModel(jsonResponse);
        }
        
    };
}
