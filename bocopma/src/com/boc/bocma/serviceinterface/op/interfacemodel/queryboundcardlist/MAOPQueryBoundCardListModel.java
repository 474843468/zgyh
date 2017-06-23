package com.boc.bocma.serviceinterface.op.interfacemodel.queryboundcardlist;


import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.common.MAOPBankCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 根据userid查询用户借记卡列表结果Model
 */
public final class MAOPQueryBoundCardListModel extends MAOPBaseResponseModel {
    private static final String CARD_NUMBER_LIST_KEY = "cardnolist";
    private static final String CARD_NUMBER_KEY = "cardno";
    private static final String  CARD_IDENTIFIER_KEY = "lmtamt";
    
    
    private List<MAOPBankCard> cardList = new ArrayList<MAOPBankCard>();
    
    private MAOPQueryBoundCardListModel(JSONObject response) throws JSONException {
        JSONArray array = response.optJSONArray(CARD_NUMBER_LIST_KEY);
        for (int i = 0; array != null && i < array.length(); i++) {
            MAOPBankCard card = new MAOPBankCard();
           JSONObject obj = array.getJSONObject(i);
           card.setCardNumber(obj.optString(CARD_NUMBER_KEY));
           card.setIdentifier(obj.optString(CARD_IDENTIFIER_KEY));
           cardList.add(card);
        }
    }

    public MAOPQueryBoundCardListModel() {
    }

    public List<MAOPBankCard> getCardList() {
        return new ArrayList<MAOPBankCard>(cardList);
    }
    
    public static final Creator<MAOPQueryBoundCardListModel> CREATOR = new Creator<MAOPQueryBoundCardListModel>() {
        @Override
        public MAOPQueryBoundCardListModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPQueryBoundCardListModel(jsonResponse);
        }
    };
}
