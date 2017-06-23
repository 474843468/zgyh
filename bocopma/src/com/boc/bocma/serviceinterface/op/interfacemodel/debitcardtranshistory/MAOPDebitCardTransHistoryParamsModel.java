package com.boc.bocma.serviceinterface.op.interfacemodel.debitcardtranshistory;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.boc.bocma.global.OPURL;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.common.MAOPBankCard;

/**
 * 借记卡批量查询交易流水参数类
 */
public class MAOPDebitCardTransHistoryParamsModel extends MAOPBaseParamsModel {
    private static final String INTERFACE_URL = "faa/debitbankquery/batch";
    
    private static final String START_DATE_KEY = "stdate";
    private static final String END_DATE_KEY = "endate";
    private static final String SAP_LIST_KEY = "saplist";
    private static final String CARD_IDENTIFIER_KEY = "lmtamt";
    private static final String USER_ID_KEY = "userid";
    private static final String TRAN_CODE_KEY = "trancode";
    private static final String TRAN_MEN_KEY = "tranmen";
    private static final String SUB_ACCNO_KEY = "subaccno";
    private static final String CURRENCY_KEY = "currency";
    private static final String SOURCE_KEY= "source";
    
    /** 用户id */
    private String userid;
    /** 交易码 */
    private String trancode = "C020";
    /** 网银操作员 */
//    private String tranmen = "BOCOP";
    private String tranmen = "ctis";
    /** 子账户类别号 */
    private String subaccno = "";
    /** 币种 */
    private String currency = "CNY";
    /** 请求来源 */
    private String source = "03";
    /** 开始时间 */
    private String startDate;
    /** 结束时间 */
    private String endDate;
    
    
    public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTrancode() {
		return trancode;
	}

	public void setTrancode(String trancode) {
		this.trancode = trancode;
	}

	public String getTranmen() {
		return tranmen;
	}

	public void setTranmen(String tranmen) {
		this.tranmen = tranmen;
	}

	public String getSubaccno() {
		return subaccno;
	}

	public void setSubaccno(String subaccno) {
		this.subaccno = subaccno;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate 开始时间 YYYYMMDD
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate 截止时间 YYYYMMDD
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    private List<MAOPBankCard> cardList = new ArrayList<MAOPBankCard>();
    
    
    public List<MAOPBankCard> getCardList() {
        return new ArrayList<MAOPBankCard>(cardList);
    }

    public void setCardList(List<MAOPBankCard> cardList) {
        this.cardList = new ArrayList<MAOPBankCard>(cardList);
    }

    @Override
    public String getUrl() {
        return OPURL.getFAABaseTransUrl() + INTERFACE_URL;
    }
    
    private JSONArray listToJsonArray() throws JSONException {
        JSONArray array = new JSONArray();
        for (MAOPBankCard card : cardList) {
            JSONObject object = new JSONObject();
            object.put(CARD_IDENTIFIER_KEY, card.getIdentifier());
            array.put(object);
        }
        return array;
    }

    @Override
    public String getJsonBody() throws JSONException {
        JSONObject body = new JSONObject();
        body.put(START_DATE_KEY, startDate);
        body.put(END_DATE_KEY, endDate);
        body.put(USER_ID_KEY, userid);
        body.put(TRAN_CODE_KEY, trancode);
        body.put(TRAN_MEN_KEY, tranmen);
        body.put(SUB_ACCNO_KEY, subaccno);
        body.put(CURRENCY_KEY, currency);
        body.put(SOURCE_KEY, source);
        body.put(SAP_LIST_KEY, listToJsonArray());
        return body.toString();
    }

}
