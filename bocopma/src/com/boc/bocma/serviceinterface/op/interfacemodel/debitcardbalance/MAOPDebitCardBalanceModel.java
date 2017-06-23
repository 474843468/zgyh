package com.boc.bocma.serviceinterface.op.interfacemodel.debitcardbalance;

import android.text.TextUtils;

import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.common.MAOPBankCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量查询账户余额结果
 *
 */
public class MAOPDebitCardBalanceModel extends MAOPBaseResponseModel {
    private static final String CARD_NUMBER_LIST_KEY = "cardnolist";
    private static final String CARD_NUMBER_KEY = "cardno";
    private static final String BALANCE_KEY = "balance";
    
    private List<MAOPBankCard> cardList = new ArrayList<MAOPBankCard>();
    
    private MAOPDebitCardBalanceModel(JSONObject jsonResponse) throws JSONException {
        JSONArray cards = jsonResponse.optJSONArray(CARD_NUMBER_LIST_KEY);
        for (int i = 0; cards != null && i < cards.length(); i++) {
            MAOPBankCard card = new MAOPBankCard();
            JSONObject object = cards.getJSONObject(i);
            card.setCardNumber(object.optString(CARD_NUMBER_KEY));
            card.setBalance(jsonString2Double(object.optString(BALANCE_KEY)));
            cardList.add(card);
        }
    }
    
    /**
     * 借记卡余额返回报文中的金额后两位为小数
     */
    private static final long KEEP_DIGITS = 100;
    
    private double jsonString2Double(String amount) {
        if (TextUtils.isEmpty(amount)) {
            return 0f;
        } else {
            return Double.valueOf(amount)/KEEP_DIGITS;
        }
    }
    
    public MAOPDebitCardBalanceModel() {
    }

    public List<MAOPBankCard> getCardList() {
        return new ArrayList<MAOPBankCard>(cardList);
    }
    
    public static final Creator<MAOPDebitCardBalanceModel> CREATOR = new Creator<MAOPDebitCardBalanceModel>() {
        @Override
        public MAOPDebitCardBalanceModel createFromJson(JSONObject jsonResponse) throws JSONException {
            return new MAOPDebitCardBalanceModel(jsonResponse);
        }
    };
}
