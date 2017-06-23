package com.boc.bocma.serviceinterface.op.interfacemodel.debitcardbalance;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.common.MAOPBankCard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MAOPDebitCardBalanceParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "faa/queryCardBal";
    
    private static final String CARD_NUMBER_LIST_KEY = "cardnolist";
    private static final String CARD_NUMBER_KEY = "cardno";
    private static final String CARD_IDENTIFIER_KEY = "lmtamt";
    
    private List<MAOPBankCard> cardList = new ArrayList<MAOPBankCard>();
    
    public void setCardList(List<MAOPBankCard> cardList) {
        this.cardList = new ArrayList<MAOPBankCard>(cardList);
    }
    
    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONArray array = new JSONArray();
        for (MAOPBankCard card : cardList) {
            JSONObject object = new JSONObject();
            object.putOpt(CARD_NUMBER_KEY, card.getCardNumber());
            object.putOpt(CARD_IDENTIFIER_KEY, card.getIdentifier());
            array.put(object);
        }
        JSONObject object = new JSONObject();
        object.put(CARD_NUMBER_LIST_KEY, array);
        return object.toString();
    }
}
