package com.chinamworld.bocmbci.biz.bocnet;

import com.chinamworld.bocmbci.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BocnetDataCenter {
	private static BocnetDataCenter instance;
	public static final String ModleName = "Bocnet";

	private String intentAction;
	private String cardTypeStr;
	/** 账户类型   true:借记卡    false:信用卡*/
	private boolean isDebitCard = true;
	/** 登录后Conversation  */
	private String conversationId;
	/** 服务器登录时间  */
	private String systemTime;
	/** 账号标识  */
	private String accountSeq;
	/** 信用卡信息  **/
	private Map<String, Object> CrcdDetail;
	/** 借记卡信息  **/
	private Map<String, Object> DebitDetail;
	/** 登录信息  **/
	private Map<String, Object> loginInfo;
	/** 信用卡未出账单 **/
	private Map<String, Object> crcdUnsettledBills;
	/** 信用卡未出账单合计 **/
	private List<Map<String, Object>> crcdUnsettledTotal;
	/** 信用卡已出账单 **/
	private Map<String, Object> CrcdStatement;
	/** 信用卡已出账单交易明细 **/
	private Map<String, Object> CrcdStatementDetail;
	
	public static BocnetDataCenter getInstance() {
		if (instance == null) {
			instance = new BocnetDataCenter();
		}
		return instance;
	}
	
	public void clearAllData(){
		if(!StringUtil.isNull(conversationId))
			conversationId = null;
		if(!StringUtil.isNull(systemTime))
			systemTime = null;
		if(!StringUtil.isNullOrEmpty(CrcdDetail))
			CrcdDetail.clear();
		if(!StringUtil.isNullOrEmpty(DebitDetail))
			DebitDetail.clear();
		if(!StringUtil.isNullOrEmpty(crcdUnsettledBills))
			crcdUnsettledBills.clear();
		if(!StringUtil.isNullOrEmpty(crcdUnsettledTotal))
			crcdUnsettledTotal.clear();
		if(!StringUtil.isNullOrEmpty(CrcdStatement))
			CrcdStatement.clear();
		if(!StringUtil.isNullOrEmpty(CrcdStatementDetail))
			CrcdStatementDetail.clear();
		if(!StringUtil.isNull(cardTypeStr)){
			cardTypeStr = null;
		}
	}
	
	public void clearLoginInfo(){
		if(!StringUtil.isNull(accountSeq))
			accountSeq = null;
		if(!StringUtil.isNullOrEmpty(loginInfo))
			loginInfo.clear();
	}
	
	public boolean isDebitCard() {
		return isDebitCard;
	}

	public void setDebitCard(boolean isDebitCard) {
		this.isDebitCard = isDebitCard;
	}

	public String getIntentAction() {
		return intentAction;
	}

	public void setIntentAction(String intentAction) {
		this.intentAction = intentAction;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	
	
	public String getSystemTime() {
		return systemTime;
	}

	public void setSystemTime(String systemTime) {
		this.systemTime = systemTime;
	}

	public String getAccountSeq() {
		return accountSeq;
	}

	public void setAccountSeq(String accountSeq) {
		this.accountSeq = accountSeq;
	}

	public Map<String, Object> getCrcdDetail() {
		return CrcdDetail;
	}

	public void setCrcdDetail(Map<String, Object> crcdDetail) {
		CrcdDetail = crcdDetail;
	}

	public Map<String, Object> getDebitDetail() {
		return DebitDetail;
	}

	public void setDebitDetail(Map<String, Object> debitDetail) {
		DebitDetail = debitDetail;
	}
	

	public Map<String, Object> getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(Map<String, Object> loginInfo) {
		this.loginInfo = loginInfo;
	}

	

	public Map<String, Object> getCrcdUnsettledBills() {
		return crcdUnsettledBills;
	}

	public void setCrcdUnsettledBills(Map<String, Object> crcdUnsettledBills) {
		this.crcdUnsettledBills = crcdUnsettledBills;
	}

	public List<Map<String, Object>> getCrcdUnsettledTotal() {
		return crcdUnsettledTotal;
	}

	public void setCrcdUnsettledTotal(List<Map<String, Object>> crcdUnsettledTotal) {
		this.crcdUnsettledTotal = crcdUnsettledTotal;
	}

	public Map<String, Object> getCrcdStatement() {
		return CrcdStatement;
	}

	public void setCrcdStatement(Map<String, Object> crcdStatement) {
		CrcdStatement = crcdStatement;
	}

	public Map<String, Object> getCrcdStatementDetail() {
		return CrcdStatementDetail;
	}

	public void setCrcdStatementDetail(Map<String, Object> crcdStatementDetail) {
		CrcdStatementDetail = crcdStatementDetail;
	}

	

	public String getCardTypeStr() {
		return cardTypeStr;
	}

	public void setCardTypeStr(String cardTypeStr) {
		this.cardTypeStr = cardTypeStr;
	}



	/** 卡类型   1-信用卡 ，2-借记卡 **/
	public static final Map<String, String> cardType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;

		{
			put("103", "1");
			put("104", "1");
			put("107", "1");
			put("119", "2");
		}
	};
	
	/**
	 * 货币
	 */
	public static final Map<String, String> Currency = new HashMap<String, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			put("", "不可选择");
			put("000", "不可选择");
			put("001", "人民币元");
			put("012", "英镑");
			put("013", "港币");
			put("014", "美元");
			put("015", "瑞士法郎");
			put("016", "德国马克");
			put("017", "法国法郎");
			put("018", "新加坡元");
			put("020", "荷兰盾");
			put("021", "瑞典克朗");
			put("022", "丹麦克朗");
			put("023", "挪威克朗");
			put("024", "奥地利先令");
			put("025", "比利时法郎");
			put("026", "意大利里拉");
			put("027", "日元");
			put("028", "加拿大元");
			put("029", "澳大利亚元");
			put("068", "人民币银");
			put("036", "美元银");
			put("034", "美元金");
			put("035", "人民币金");
			put("844", "人民币钯金");
			put("845", "人民币铂金");
			put("841", "美元钯金");
			put("045", "美元铂金");
			put("038", "欧元");
			put("042", "芬兰马克");
			put("081", "澳门元");
			put("082", "菲律宾比索");
			put("084", "泰国铢");
			put("087", "新西兰元");
			put("088", "韩元");
			put("094", "记账美元");
			put("095", "清算瑞士法郎");
			put("056", "印尼盾");
			put("064", "越南盾");
			put("096", "阿联酋迪拉姆");
			put("126", "阿根廷比索");
			put("134", "巴西雷亚尔");
			put("053", "埃及磅");
			put("085", "印度卢比");
			put("057", "约旦第纳尔");
			put("179", "蒙古图格里克");
			put("032", "马拉西亚林吉特");
			put("MYR", "马拉西亚林吉特");
			put("076", "尼日尼亚奈拉");
			put("062", "罗马尼亚列伊");
			put("093", "土耳其里拉");
			put("246", "乌克兰格里夫纳");
			put("070", "南非兰特");
			put("065", "匈牙利福林");//065  匈牙利福林
			put("101", "哈萨克斯坦坚戈");
			put("080", "赞比亚克瓦查");
			put("032", "林吉特");
			put("131", "文莱币");//131  文莱币
			put("039", "博茨瓦纳普拉");//039  博茨瓦纳普拉 
			put("253", "赞比亚新币种 克瓦查");//253  赞比亚新币种 克瓦查
			put("166", "柬埔寨货币  瑞尔");//166  柬埔寨货币  瑞尔
			put("072", "清算卢布");
			put("196", "卢布");
			put("-1", "通配");
			put("096", "美元指数");
			put("841", "美元钯金（盎司）");//841 美元钯金（盎司）
			put("845", "人民币铂金（克）");
			put("213", "新台湾元");
			put("zzz", "其他");
		}
	};
	
	public static final HashMap<String, String> balanceFlag = new HashMap<String, String>(){
		{
			put("0", "(欠款金额)");
			put("1", "(存款金额)");
			put("2", "余额");
		}
	};

	/**提示信息*/
	public final Map<String,String> messageInfo=new HashMap<String, String>(){
		{
			put("1","一");
			put("2","两");
			put("3","三");
			put("6","六");
		}
	};
}
