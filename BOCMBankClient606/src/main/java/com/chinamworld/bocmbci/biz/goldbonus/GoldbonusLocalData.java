package com.chinamworld.bocmbci.biz.goldbonus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chinamworld.bocmbci.utils.KeyAndValueItem;

/**
 * 存储贵金属积利金的数据信息
 * @author linyl
 *
 */
public class GoldbonusLocalData {
	
	private static GoldbonusLocalData instance = null;
	public synchronized static GoldbonusLocalData getInstance() {
		if (instance == null)
			instance = new GoldbonusLocalData();
		return instance;
	}
	/**
	 * 清掉数据实例对象
	 */
	public static void cleanInstance(){
		instance = null;
	}

	public Map<String, Object> psnGoldBonusPriceListQuery;//积利金外置接口详情
	
	public Map<String, Object> psnGoldBonusSignInfoQuery;//积利金签约关系查询
	
	public Map<String, Object> PsnGoldBonusPriceListQuery;//登陆后行情
	public Map<String, Object> PsnGoldBonusAccountQuery;//2好接口的返回数据
	public int mCurrentPosition;//账户列表的位置
	
	/**贵金属积利金 交易查询 接口返回  被选中项数据**/
	public Map<String,Object> TradeQueryDetailMap;
	/**贵金属积利定投计划签约确认  返回数据**/
	public Map<String,Object> FixInvestSignPreMap;
	/**贵金属积利定投计划签约  返回数据**/
	public Map<String,Object> FixInvestSignMap;
	/**贵金属积利金定投列表查询**/
	public Map<String,Object> FixInvestListDetailQueryMap;
	/**贵金属积利金定投执行计划列表 返回数据**/
	public Map<String,Object> FixInvestDetailQryMap;
	/**贵金属积利金 定投终止确认 返回数据**/
	public Map<String,Object> FixInvestStopPreMap;
	/**贵金属积利产品信息查询 返回数据 **/
	public List<Map<String,Object>> ProductInfoQueryList;
	/**贵金属积利定存持仓账户查询  选中List item项数据 **/
	public Map<String,Object> AccountQueryMap;
	/**贵金属积利定存 定期产品查询  选中List item项数据**/
	public Map<String,Object> ProductInfoQueryQueryMap;
	/**贵金属积利金 银行买入价**/
	public String BankBuyPrice;
	/**贵金属积利金 银行卖出价**/
	public String bankSellPrice;
	/**贵金属积利金 公共余额数据**/
	public String availbalance;
	/**贵金属积利账户设定确认  3号接口数据**/
	public Map<String,Object> RegisterAccountConfirmMap;
	/**贵金属积利账户变更确认  5号接口数据**/
	public Map<String,Object> PsnGoldBonusModifyAccountConfirmMap;
	/**选中账户标识**/
	public String accountId;
	/**第一次签约手机号**/
	public String phoneNumber;
	/**选中中号number**/
	public String accountNumber;
	/**原来账户账户标识**/
	public String accountIdOld;
	/**新账户账户标识**/
	public String accountIdNew;
	/**买卖交易时用的账户ID**/
	public String accountIdBusi;
	/**新账户账户号**/
	public String accountNumberNew;
	/**原来账户账户号**/
	public String accountNumberOld;
	public String ISSELLLOGIN ;
	public final static Map<String, String> accountType = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("SY", "活期一本通");
			put("CD", "长城电子借记卡");
			put("188", "活期一本通");
			put("119", "长城电子借记卡");
		}
	};
	/**定投管理进入买卖交易 交易方向标示 定投预约状态**/
	public static final String FIXINVESTINTENTFLAG = "RB_RESERVE";
	/**贵金属积利所有积利金账户查询的公共接口返回数据 **/
	public List<Map<String,Object>> requestPsnCommonQueryAllChinaBankAccountList;
	
	/**
	 * 积利金 交易查询 交易类型字典
	 */
	public  static List<KeyAndValueItem> SaleTypeList= new ArrayList<KeyAndValueItem>(){
	
		private static final long serialVersionUID = 1L;
		{
//			add(new KeyAndValueItem("请选择",""));
			add(new KeyAndValueItem("买入活期贵金属积利","0"));
			add(new KeyAndValueItem("卖出活期贵金属积利","1"));
			add(new KeyAndValueItem("贵金属积利活期转为定期","2"));
			add(new KeyAndValueItem("贵金属积利定期转为活期","3"));
		}
	};
	
}
