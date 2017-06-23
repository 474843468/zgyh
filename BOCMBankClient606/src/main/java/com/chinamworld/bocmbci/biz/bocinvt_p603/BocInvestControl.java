package com.chinamworld.bocmbci.biz.bocinvt_p603;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.constant.BocInvt;
import com.chinamworld.bocmbci.biz.bocinvt.BociDataCenter;
import com.chinamworld.bocmbci.biz.bocinvt.myproduct.EchartWebViewActivity;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.BuyProductChooseActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.myinvetproduct.domain.BOCProductForHoldingInfo;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.InterfaceSuccessBack;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyAgreementApplyActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity;
import com.chinamworld.bocmbci.biz.bocinvt_p603.productqueryandbuy.activity.ProductQueryAndBuyYearRateActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.utils.RegexpBean;
import com.chinamworld.bocmbci.utils.StringUtil;
/**
 * 理财公共控制类
 * @author yuht
 *
 */
public class BocInvestControl extends BociDataCenter {

	public BocInvestControl(){
		super();
	}
	
	/**
	 * 当前选中的理财持仓信息
	 */
	public BOCProductForHoldingInfo curBOCProductForHoldingInfo;
	
	
	
	
//	接口 
	/**组合购买*/
	public static final String INF_PSNXPADGUARANTYBUYRESULT="PsnXpadGuarantyBuyResult";
	/**投资协议列表*/
	public static final String PSNXPADPRODUCTINVESTTREATYQUERY="PsnXpadProductInvestTreatyQuery";
	/**智能协议详情查询*/
	public static final String PSNXPADAPTITUDETREATYDETAILQUERY="PsnXpadAptitudeTreatyDetailQuery";
	/**智能协议预交易*/
	public static final String PSNXPADAPTITUDETREATYAPPLYVERIFY="PsnXpadAptitudeTreatyApplyVerify";
	/**智能协议交易*/
	public static final String PSNXPADAPTITUDETREATYAPPLYCOMMIT="PsnXpadAptitudeTreatyApplyCommit";
	/**收益试算*/
	public static final String PSNXPADPROFITCOUNT="PsnXpadProfitCount";
	/**预计年收益率*/
	public static final String PSNXPADEXPECTYIELDQUERY="PsnXpadExpectYieldQuery";
	/**周期性产品续约协议签约/签约初始化*/
	public static final String PSNXPADSIGNINIT="PsnXpadSignInit";
	/**周期性产品续约协议签约/签约结束*/
	public static final String PSNXPADSIGNRESULT="PsnXpadSignResult";
	/**查询客户理财账户信息*/
	public static final String PSNXPADACCOUNTQUERY="PsnXpadAccountQuery";
	
//	数字常量
	/** 组合购买请求结果码 */
	public static int ACTIVITY_RESULT_CODE_COMBO_BUY = 10101;
	/** 投资协议申请请求结果码 */
	public static int ACTIVITY_RESULT_CODE_AGREEMENT_APPLY = 10102;
	/** 功能外置，产品购买 */
	public static int ACTIVITY_RESULT_CODE_PRODUCT_BUY = 10103;
	/** 投资协议申请，tv_sp1的ID值 */
	public static int TV_SP1_ID = 10104;
	/** 投资协议申请，tv_sp2的ID值 */
	public static int TV_SP2_ID = 10105;
	/**用户输入的投资期数*/
	public static final String BOCINVT_INVEST_CYCLE_INPUT = "investCycleInput";
//	字符串常量
	/**每页显示条数*/
	public static final String PAGESIZE="10";
	/**刷新标识*/
	public static final String _REFRESH="true";
	/**产品风险等级*/
	public static final String PRORISK="proRisk";
	/** 预计年收益率（%） */
	public static final String YEARLYRR = "yearlyRR";
	/**产品类型,1：现金管理类产品  2：净值开放类产品  3：固定期限产品*/
	public static final String ISSUETYPE="issueType";
	/**是否可组合购买*/
	public static final String IMPAWNPERMIT="impawnPermit";
	/**剩余额度*/
	public static final String AVAILAMT="availamt";
	/**指定赎回日期*/
	public static final String REDDATE="redDate";
	/**投资期数，用户输入*/
	public static final String INVESTCYCLE="investCycle";
	/**转换手续费收取方式*/
	public static final String CHANGEFEEMODE="changeFeeMode";
	/**预计到期日*/
	public static final String REDEEMDATE="redeemDate";
	/**交易手续费*/
	public static final String TRANSFEE="transFee";
	/**产品期限*/
	public static final String PROTERM="proTerm";
	/**预计年收益率-购买确认页*/
	public static final String EXYIELD="exYield";
	/**持有现钞份额*/
	public static final String CASHSHARE="cashShare";
	/**持有现汇份额*/
	public static final String REMITSHARE="remitShare";
	/**组合买入金额*/
	public static final String GUARANTYBUYAMOUT="GuarantyBuyAmount";
	/**被组合份额*/
	public static final String FREEZEUNIT="freezeUnit";
	/**产品币种，上送字段key*/
	public static final String PRODUCTCURRENCY="productCurrency";
	/**组合产品总数，上送字段key*/
	public static final String PRODUCTNUM="productNum";
	/**押品总市值超过质押买入金额，是否同意继续交易，上送字段key*/
	public static final String OVERFLAG="overFlag";
	/**产品成立日*/
	public static final String PRODUCTBEGINDATE="productBeginDate";
	/**产品到期日*/
	public static final String PRODUCTENDDATE="productEndDate";
	/**组合买入金额*/
	public static final String BUYAMOUNT="buyAmount";
	/**产品代码*/
	public static final String PROID="proId";
	/**投资方式*/
	public static final String INSTTYPE="instType";
	/**投资方式*/
	public static final String INVESTTYPE="investType";
	/**协议类型*/
	public static final String AGRTYPE="agrType";
	/**协议名称*/
	public static final String AGRNAME="agrName";
	/**投资产品*/
	public static final String PRONAM="proNam";
	/**协议周期频率*/
	public static final String PERIODAGR="periodAgr";
	/**协议编码*/
	public static final String AGRCODE="agrCode";
	/**协议总期数*/
	public static final String AGRPERIOD="agrPeriod";
	/**协议当前期数*/
	public static final String AGRCURRPERIOD="agrCurrPeriod";
	/**最小投资期数*/
	public static final String MININVESTPERIOD="minInvestPeriod";
	/**单周期投资期限*/
	public static final String SINGLEINVESTPERIOD="singleInvestPeriod";
	/**购买频率*/
	public static final String ISNEEDPUR="isNeedPur";
	/**下次购买日期*/
	public static final String FIRSTDATEPUR="firstDatePur";
	/**赎回频率*/
	public static final String ISNEEDRED="isNeedRed";
	/**下次赎回日期*/
	public static final String FIRSTDATERED="firstDateRed";
	/**预计年收益率，投资协议申请*/
	public static final String RATE="rate";
	/**协议投资起点金额*/
	public static final String AGRPURSTART="agrPurStart";
	/**协议投资金额模式*/
	public static final String AMOUNTTYPE="amountType";
	/**投资协议申请，判断投资金额模式 ，true/不定额、false/定额*/
	public static final String key_isFlag_agreement_apply="是否不定额";
	/**投资协议申请，投资方式：1:周期连续协议、2:周期不连续协议、3:多次购买协议、4:多次赎回协议*/
	public static final String key_investType_agreement_apply="投资方式";
	/**产品代码*/
	public static final String PRODUCTCODE="productCode";
	/**产品性质,0:结构性理财产品、1:类基金理财产品*/
	public static final String PRODUCTKIND="productKind";
	/**资金账号缓存标识*/
	public static final String CAPITALACTNOKEY="capitalActNoKey";
	/**产品工作开始时间*/
	public static final String STARTTIME="startTime";
	/**产品工作结束时间*/
	public static final String ENDTIME="endTime";
	/**产品挂单开始时间*/
	public static final String ORDERSTARTTIME="orderStartTime";
	/**产品挂单结束时间*/
	public static final String ORDERENDTIME="orderEndTime";
	/**单位净值*/
	public static final String PRICE="price";
	/**预计年收益率最大值*/
	public static final String RATEDETAIL="rateDetail";
	/**0：认购、1：申购*/
	public static final String TRANSTYPECODE="transTypeCode";
	/**认购手续费*/
	public static final String SUBSCRIBEFEE="subscribeFee";
	/**申购手续费*/
	public static final String PURCHFEE="purchFee";
	/**系统时间*/
	public static String SYSTEM_DATE="";
	/**投资协议申请-产品币种*/
	public static String PROCUR="proCur";
	/**购买页-用户选择是否指定赎回日期，true/是*/
	public static String KEY_ISCHECK_RABTN="key_isCheck_rabtn";
	/**周期滚续协议申请，存储结果页数据的key*/
	public static String PERIODAGREEMENTRESULTMAP="PeriodAgreementResultMap";
	
//	周期滚续协议申请，输入页传递到确认页的数据  key
	/**钞/汇：0、1、2*/
	public static String CASHREMIT="cashremit";
	/**产品币种*/
	public static String CURCODE="curCode";
	/**购买期数*/
	public static String BUYCYCLECOUNT="buyCycleCount";
	/**基础金额*/
	public static String BASEAMOUNT="baseAmount";
	/**最低预留金额*/
	public static String MINAMOUNT="minAmount";
	/**最大扣款金额*/
	public static String MAXAMOUNT="maxAmount";
	/**基础金额模式，0：定额/1：不定额*/
	public static String BASEAMOUNTMODE="baseAmountMode";
	
	public static String MATCH = "0";
	public static String NOMATCHCAN = "1";
	public static String NOMATCH = "2";
	/**其他模块跳转到投资协议申请页，存储流程完成后回调的key*/
	public static String AGREEMENT_APPLAY_KEY = "agreement_applay_key";
	
	
	
//	map、list集合，无初始值
	/**存储用户选择的账户号信息*/
	public static Map<String, Object> accound_map= new HashMap<String, Object>();
	/**组合购买，持有产品列表查询 结果*/
	public static Map<String, Object> GuarantyProductList_map= new HashMap<String, Object>();
	/**组合购买，持有产品列表-用户选择、输入要组合购买的产品列表*/
	public static List<Map<String, Object>> GuarantyProductList_edit_list= new ArrayList<Map<String,Object>>();
	/**组合购买，是否进行过风险评估，回调结果*/
	public static Map<String, Object> danger_map=new HashMap<String, Object>();
	/**组合购买，回调结果*/
	public static Map<String, Object> result_combo_buy=new HashMap<String, Object>();
	/**投资协议申请，投资协议列表  用户点击选择的item*/
	public static Map<String, Object> map_listview_choose=new HashMap<String, Object>();
	/**投资协议申请，智能协议详情*/
	public static Map<String, Object> map_agreement_detail=new HashMap<String, Object>();
	/**投资协议申请，请求预交易结果*/
	public static Map<String, Object> map_agreement_result_sure=new HashMap<String, Object>();
	/**投资协议申请，请求交易结果*/
	public static Map<String, Object> map_agreement_result_succeed=new HashMap<String, Object>();
	/**存储跳转到产品查询与购买模块的成功回调--InterfaceSuccessBack*/
	public static Map<String, Object> map_interface_success_back=new HashMap<String, Object>();
	/**
	 * 清除产品查询与购买模块数据
	 */
	public static void clearDate_productQueryAndBuy(){
		GuarantyProductList_map.clear();
		GuarantyProductList_edit_list.clear();
		danger_map.clear();
		result_combo_buy.clear();
		map_listview_choose.clear();
		map_agreement_detail.clear();
		map_agreement_result_sure.clear();
		map_agreement_result_succeed.clear();
	}
	
//	map 集合中的key
	/**key 集合列表 List*/
	public static final String key_List="List";
	/**key 集合列表 list*/
	public static final String key_list="list";
	/**key 记录条数*/
	public static final String key_recordNumber="recordNumber";
	
//	list列表、map集合，赋初始值
	/** 客户类型  map 编码key 中文value*/
	public static final Map<String, String> map_custLevel_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "普通客户");
			put("1", "中银理财");
			put("2", "中银财富管理");
			put("3", "中银私人银行");
		}
	};
	/** 是否是业绩基准型产品  map 中文key 编码value*/
	public static final Map<String, String> map_isLockPeriod_code_value=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("非业绩基准产品", "0");
			put("业绩基准-锁定期转低收益", "1");
			put("业绩基准-锁定期后入账", "2");
			put("业绩基准-锁定期周期滚续", "3");
		}
	};
	/** 投资金额模式  list*/
	public static final List<String> list_invest_money_Type=new ArrayList<String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("定额");
			add("不定额");
		}
	};
	/** 投资金额模式  map 编码key 中文value*/
	public static final Map<String, String> map_invest_money_Type_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "定额");
			put("1", "不定额");
		}
	};
	/** 投资金额模式  map 中文key 编码value*/
	public static final Map<String, String> map_invest_money_Type_code_value=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("定额", "0");
			put("不定额", "1");
		}
	};
	/** 投资协议申请-购买频率  map 编码key 中文value*/
	public static final Map<String,String> map_isNeedPur_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
//			put("0", "期初购买");
//			put("1", "协议不购买");
			put("0", "期初申购");
			put("1", "不申购");
		}
	};
	/** 投资协议申请-赎回频率  map 编码key 中文value*/
	public static final Map<String,String> map_isNeedRed_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
//			put("0", "到期赎回");
//			put("1", "协议不赎回");
			put("0", "期末赎回");
			put("1", "不赎回");
		}
	};
	/** 投资协议类型  list*/
	public static final List<String> list_agrType=new ArrayList<String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("智能投资");
			add("定时定额投资");
			add("周期滚续投资");
			add("余额理财投资");
		}
	};
	/** 投资协议类型  map 编码key 中文value*/
	public static final Map<String,String> map_agrType_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "全部");
			put("1", "智能投资");
			put("2", "定时定额投资");
			put("3", "周期滚续投资");
			put("4", "余额理财投资");
		}
	};
	/** 投资协议类型  map 编码value 中文key*/
	public static final Map<String,String> map_agrType_code_value=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("智能投资", "1");
			put("定时定额投资", "2");
			put("周期滚续投资", "3");
			put("余额理财投资", "4");
		}
	};
	/** 投资方式  list*/
	public static final List<String> list_instType=new ArrayList<String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("周期连续");
			add("周期不连续");
			add("多次购买");
			add("多次赎回");
		}
	};
	/** 投资方式(全部8种类型)  map 编码key 中文value*/
	public static final Map<String,String> map_instType_all_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "全部");
			put("1", "周期连续");
			put("2", "周期不连续");
			put("3", "多次购买");
			put("4", "多次赎回");
			put("5", "定时定额投资");
			put("6", "余额理财投资");
			put("7", "周期滚续");
			put("8", "周期滚续");
		}
	};
	/** 投资方式  map 中文key 编码value*/
	public static final Map<String,String> map_instType_code_value=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("周期连续", "1");
			put("周期不连续", "2");
			put("多次购买", "3");
			put("多次赎回", "4");
		}
	};
	/** 客户风险等级  map*/
	public static final Map<String, String> map_riskLevel=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("1", "保守型投资者");
			put("2", "稳健型投资者");
			put("3", "平衡型投资者");
			put("4", "成长型投资者");
			put("5", "进取型投资者");
		}
	};
	/** 钞/汇  list*/
	public static final List<String> list_cashRemit=new ArrayList<String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("现钞");//倒序
			add("现汇");//倒序
		}
	};
	/** 钞/汇  map 中文key 编码value*/
	public static final Map<String, String> map_cashRemit_code_value=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("-", "0");
			put("现钞", "1");
			put("现汇", "2");
		}
	};
	/** 钞/汇  map 编码key 中文value*/
	public static final Map<String, String> map_cashRemit_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "-");
			put("1", "现钞");
			put("2", "现汇");
		}
	};
	/** 钞/汇  map 中文key 编码value*/
	public static final Map<String, String> map_xpadCashRemit_code_value=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("-", "00");
			put("现钞", "01");
			put("现汇", "02");
		}
	};
	/** 钞/汇  map 编码key 中文value*/
	public static final Map<String, String> map_xpadCashRemit_code_key=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("00", "-");
			put("01", "现钞");
			put("02", "现汇");
		}
	};
	/** 转换手续费收取方式  map*/
	public static final Map<String, String> map_changeFeeMode=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "收取手续费");
			put("1", "不收取手续费");
		}
	};
	/** 是否允许撤单  map*/
	public static final Map<String, String> map_isCanCancle=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "不允许撤单");
			put("1", "只允许当日撤单");
			put("2", "允许撤单");
		}
	};
	/** 是否可组合购买  map*/
	public static final Map<String, String> map_impawnPermit=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("是", "0");
			put("否", "1");
		}
	};
	/** 产品排序方式  list*/
	public static final List<String> list_sortFlag=new ArrayList<String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("0");//正序
			add("1");//倒序
		}
	};
	/**排序字段 list*/
	public static final List<String> list_sortSite=new ArrayList<String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("0");//产品销售期
			add("1");//产品期限
			add("2");//收益率
			add("3");//购买起点金额
		}
	};
	/** 产品风险类型-即收益类型 map*/
	public static final Map<String, String> map_productRiskType=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("保本固定收益", "1");
			put("保本浮动收益", "2");
			put("非保本浮动收益", "3");
		}
	};
	/** 产品风险类型-即收益类型 list*/
	public static final List<String> list_productRiskType = new ArrayList<String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("保本固定收益");
			add("保本浮动收益");
			add("非保本浮动收益");
		}
	};
	/** 产品风险等级 map*/
	public static final Map<String, String> map_proRisk=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("低风险", "1");
			put("中低风险", "2");
			put("中等风险", "3");
			put("中高风险", "4");
			put("高风险", "5");
		}
	};
	/** 产品风险等级 map 编码 转 中文风险级别*/
	public static final Map<String, String> map_proRisk_toStr=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "全部");
			put("1", "低风险");
			put("2", "中低风险");
			put("3", "中等风险");
			put("4", "中高风险");
			put("5", "高风险");
		}
	};
	/** 产品风险等级  list*/
	public static final List<String> list_proRisk = new ArrayList<String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("低风险");
			add("中低风险");
			add("中等风险");
			add("中高风险");
			add("高风险");
		}
	};
	/** 产品风险等级 map 编码 转 中文风险级别*/
	public static final Map<String, String> map_prodRisklvl_toStr=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("0", "低风险产品");
			put("1", "中低风险产品");
			put("2", "中等风险产品");
			put("3", "中高风险产品");
			put("4", "高风险产品");
		}
	};
	/** 产品期限 map*/
	public static final Map<String, String> map_prodTimeLimit=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("30天以内", "1");
			put("31天-90天", "2");
			put("91天-180天", "3");
			put("181天以上", "4");
		}
	};
	/** 产品期限 list*/
	public static final List<String> list_prodTimeLimit = new ArrayList<String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("30天以内");
			add("31天-90天");
			add("91天-180天");
			add("181天以上");
		}
	};
	/** 产品币种 map 币种转 编码*/
	public static final Map<String, String> map_productCurCode=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "000");
			put("人民币元", "001");
			put("美元", "014");
			put("英镑", "012");
			put("港币", "013");
			put("加拿大元", "028");
			put("澳大利亚元", "029");
			put("欧元", "038");
			put("日元", "027");
		}
	};
	/** 产品币种 map 编码转币种*/
	public static final Map<String, String> map_productCurCode_toStr=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("000","全部");
			put("001","人民币元");
			put("014","美元");
			put("012","英镑");
			put("013","港币");
			put("028","加拿大元");
			put("029","澳大利亚元");
			put("038","欧元");
			put("027","日元");
		}
	};
	/** 产品币种 map 编码转币种*/
	public static final Map<String, String> map_productCurCode_toStr2=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("000","全部");
			put("001","元");
			put("014","美元");
			put("012","英镑");
			put("013","港币");
			put("028","加拿大元");
			put("029","澳大利亚元");
			put("038","欧元");
			put("027","日元");
		}
	};
	/** 产品币种 list*/
	public static final List<String> list_productCurCode = new ArrayList<String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("人民币元");
			add("美元");
			add("英镑");
			add("港币");
			add("加拿大元");
			add("澳大利亚元");
			add("欧元");
			add("日元");
		}
	};
	/** 产品类型 map */
	public static final Map<String, String> map_issueType=new HashMap<String, String>(){
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("现金管理类产品", "1");
			put("净值开放类产品", "2");
			put("固定期限类产品", "3");
		}
	};
	/** 产品类型 list*/
	public static final List<String> list_issueType = new ArrayList<String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("现金管理类产品");
			add("净值开放类产品");
			add("固定期限类产品");
		}
	};
	/** 产品属性分类 list*/
	public static final List<String> list_productType = new ArrayList<String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部");
			add("自营产品");
			add("代销产品");
		}
	};
	/** 产品属性分类 map*/
	public static final Map<String, String> map_productType = new HashMap<String, String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部", "0");
			put("自营产品", "1");
			put("代销产品", "2");
		}
	};
	/** 产品销售状态 list*/
	public static final List<String> list_productSta = new ArrayList<String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			add("全部产品");
			add("在售产品");
			add("停售产品");
		}
	};
	/** 产品销售状态  map*/
	public static final Map<String, String> map_productSta = new HashMap<String, String>() {
		/***/
		private static final long serialVersionUID = 1L;
		{
			put("全部产品", "0");
			put("在售产品", "1");
			put("停售产品", "2");
		}
	};
	
	public static Map<String, String> custLevelSaleMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("1", "客户等级在理财级（含）以上");
			put("2", "客户等级在财富级（含）以上");
			put("3", "客户等级为私行级");
		}
	};
	
	public static Map<String, String> prodRisklvlMap = new HashMap<String, String>() {
		private static final long serialVersionUID = 1L;
		{
			put("1", "风险等级为稳健型（含）以上");
			put("2", "风险等级为平衡型（含）以上");
			put("3", "风险等级为成长型（含）以上");
			put("4", "风险等级为进取型");
		}
	};
	
	/**净值开放类产品-文字串*/
	public static final String STR_JINGZHI=list_issueType.get(2);//净值开放类产品
	
	//理财公共方法区
	/**
	 * 功能：跳转到购买页
	 * @param	context			当前activity
	 * @param	productKind 	产品性质(传入0或1)		0:结构性理财产品1:类基金理财产品
	 * @param	productCode 	产品代码
	 * @param	map_acound		理财交易账户相关信息
	 * @param	systemTime		请求后台返回的系统时间，格式例如：2015/03/31 17:36:41
	 * @param	isForResult 	true/向当前页反馈结果、false/不需要反馈结果
	 * @param	requestCode 	startActivityForResult()的跳转请求码
	 */
	public static void toBuyActivity(Context context,String productKind,String productCode,Map<String, Object> map_acound,String systemTime,Boolean isForResult,int requestCode){
		if (StringUtil.isNullOrEmpty(productKind)||StringUtil.isNullOrEmpty(productCode)||StringUtil.isNullOrEmpty(map_acound)||StringUtil.isNullOrEmpty(systemTime)) {
			return;
		}
		BocInvestControl.SYSTEM_DATE=systemTime;
		BaseDroidApp.getInstanse().getBizDataMap().put(ConstantGloble.BOCINVT_BUYINIT_MAP, map_acound);
		Intent intent=new Intent(context,BuyProductChooseActivity.class);
		intent.putExtra(PRODUCTKIND, productKind);
		intent.putExtra(PRODUCTCODE, productCode);
		if (isForResult) {
			((FragmentActivity)context).startActivityForResult(intent, requestCode);
		}else {
			((FragmentActivity)context).startActivity(intent);
		}
	}
	/**
	 * 功能：跳转投资协议申请页
	 * @param	context			当前activity
	 * @param	accound_map  接口PsnXpadAccountQuery中的理财账户信息
	 * @param	detailMap  接口PsnXpadProductDetailQuery中的产品详情信息
	 * @param	successBack  成功后的回调
	 */
	public static void toPeriodAgreementApply(Context context,Map<String, Object> accound_map,Map<String, Object> detailMap,InterfaceSuccessBack successBack){
		if (StringUtil.isNullOrEmpty(context)||StringUtil.isNullOrEmpty(accound_map)||StringUtil.isNullOrEmpty(detailMap)) {
			return;
		}
		BocInvestControl.accound_map=accound_map;
		BociDataCenter.getInstance().setDetailMap(detailMap);
		if (!StringUtil.isNullOrEmpty(successBack)) {
			map_interface_success_back.put(AGREEMENT_APPLAY_KEY, successBack);
		}
		Intent intent = null;
		if(!StringUtil.isNullOrEmpty(BocInvestControl.map_listview_choose)){
			intent=new Intent(context,ProductQueryAndBuyAgreementApplyAgreementBaseMessageActivity.class);
		}else{
			intent=new Intent(context,ProductQueryAndBuyAgreementApplyActivity.class);
		}
		context.startActivity(intent);
	}
	/**
	 * 功能：从其他模块跳转到预计年收益率页面
	 * @param context 当前activity
	 * @param detailMap 调用接口PsnXpadProductDetailQuery返回的产品详情数据
	 * @param currentDate 调用接口requestSystemDateTime返回的系统时间，currentDate格式例如：2015/03/31 17:36:41
	 * @param isForResult true/向当前页反馈结果、false/不需要反馈结果
	 * @param requestCode startActivityForResult()的跳转请求码
	 */
	public static void toProductQueryAndBuyYearRateActivity(Context context,Map<String, Object> detailMap,String currentDate,Boolean isForResult,int requestCode){
		if (StringUtil.isNullOrEmpty(detailMap)||StringUtil.isNullOrEmpty(currentDate)) {
			return;
		}
		BocInvestControl.SYSTEM_DATE=currentDate;
		BociDataCenter.getInstance().setDetailMap(detailMap);
		Intent intent = new Intent(context,ProductQueryAndBuyYearRateActivity.class);
		if (isForResult) {
			((FragmentActivity)context).startActivityForResult(intent, requestCode);
		}else {
			((FragmentActivity)context).startActivity(intent);
		}
	}
	/**
	 * 设置预计年收益率
	 * @param map
	 * @param key1 预计年收益率最小值取值key
	 * @param key2 预计年收益率最大值取值key
	 */
	public static String getYearlyRR(Map<String,Object> map,String key1,String key2){
		if (StringUtil.isNullOrEmpty(map)) {
			return "";
		}
		String append2Decimals =null;
		Object str_1 = map.get(key1);
		Object str_2 = map.get(key2);
		if (StringUtil.isNullOrEmpty(str_1)&&StringUtil.isNullOrEmpty(str_2)) {
			return "";
		}
		if (StringUtil.isNullOrEmpty(str_2)||StringUtil.append2Decimals(str_2.toString(),2).equals("0.00")) {
			append2Decimals = StringUtil.append2Decimals(str_1.toString(), 2)+"%";
		}else {
			if (!StringUtil.isNullOrEmpty(str_1)) {
				append2Decimals = StringUtil.append2Decimals(str_1.toString(), 2)+"%"+"-"+StringUtil.append2Decimals(str_2.toString(), 2)+"%";
			}else {
				append2Decimals = StringUtil.append2Decimals(str_2.toString(), 2)+"%";
			}
		}
		return append2Decimals;
	}
	/**
	 * 获取产品期限值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getProductLimit(){
		Map<String, Object> detailMap = (Map<String, Object>) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.BOCINVT_PRODUCTDETAIL_LIST);
		String timeLimit = (String) detailMap.get(BocInvt.BOCI_DETAILPRODTIMELIMIT_RES);
		if (detailMap.get(PRODUCTKIND).toString().equals("0")&&detailMap.get("isLockPeriod").toString().equals("1")) {//结构性理财产品&&业绩基准-锁定期转低收益 
			return "最低持有"+timeLimit+"天";
		}
		return timeLimit+"天";
	}
	/**
	 * 获取接口返回的带d、m、w、y的频率值
	 * @param str格式： 1d、1w、1m、1y
	 * @return 对应返回格式：1天、1周、1月、1年
	 */
	public static String get_d_m_w_y(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return "";
		}
		String str_value = str.substring(0,str.length()-1);
		String str_last = str.substring(str.length()-1,str.length());
		if (str_last.equalsIgnoreCase("d")) {
			return str_value+"天";
		}else if (str_last.equalsIgnoreCase("w")) {
			return str_value+"周";
		}else if (str_last.equalsIgnoreCase("m")) {
			return str_value+"月";
		}else if (str_last.equalsIgnoreCase("y")) {
			return str_value+"年";
		}else {
			return str;
		}
	}
	/**
	 * 获取接口返回的带d、m、w、y的频率值
	 * @param str格式： 1d、1w、1m、1y
	 * @return 对应返回格式：1天、1周、1个月、1年
	 */
	public static String get_d_m_w_y2(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return "";
		}
		String str_value = str.substring(0,str.length()-1);
		String str_last = str.substring(str.length()-1,str.length());
		if (str_last.equalsIgnoreCase("d")) {
			return str_value+"天";
		}else if (str_last.equalsIgnoreCase("w")) {
			return str_value+"周";
		}else if (str_last.equalsIgnoreCase("m")) {
			return str_value+"个月";
		}else if (str_last.equalsIgnoreCase("y")) {
			return str_value+"年";
		}else {
			return str;
		}
	}
	/**
	 * 校验日元、韩元及其它没有小数的币种，curCode为null或""时进行普通校验
	 * @param curCode 币种(传入null或""时进行非币种校验)
	 * @param regexp_name 校验名，如：购买金额
	 * @param regexp_value 校验值
	 * @param str_regexp 要指定使用的校验规则(与regexplist中值对应)，传入null或""时 
	 * 			非币种校验使用通用校验（"最多14位且不能为0（小数点后最多2位数字）"）
	 * 			币种校验使用通用校验（"最多14位且不能为0（小数点后最多2位数字，日元、韩元、越南盾交易时输入整数）"）
	 * @return RegexpBean 校验体
	 */
	public static RegexpBean getRegexpBean(String curCode,String regexp_name,String regexp_value,String str_regexp){
		RegexpBean rb=null;
		if (StringUtil.isNullOrEmpty(curCode)) {
			if (StringUtil.isNullOrEmpty(str_regexp)) {
				rb=new RegexpBean(regexp_name, regexp_value, "amount");
			}else {
				rb=new RegexpBean(regexp_name, regexp_value, str_regexp);
			}
			return rb;
		}
		if (LocalData.codeNoNumber.contains(curCode)) {
			if (StringUtil.isNullOrEmpty(str_regexp)) {
				rb=new RegexpBean(regexp_name, regexp_value, "spetialAmount2");
			}else {
				rb=new RegexpBean(regexp_name, regexp_value, str_regexp);
			}
		}else {
			if (StringUtil.isNullOrEmpty(str_regexp)) {
				rb=new RegexpBean(regexp_name, regexp_value, "amount");
			}else {
				rb=new RegexpBean(regexp_name, regexp_value, str_regexp);
			}
		}
		return rb;
	}
	/**
	 * 输入框金额反显，如果是日元、韩元等则去掉小数
	 * @param curCode 币种
	 * @param str 要格式化的金额值
	 * @return
	 */
	public static String getMoney(String curCode,String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return "";
		}
		if (!StringUtil.isNullOrEmpty(curCode)&&LocalData.codeNoNumber.contains(curCode)) {
			if (str.contains(".")) {
				return str.substring(0,str.indexOf("."));
			}
			return str;
		}
		return StringUtil.append2Decimals(str, 2);
	}
	/**
	 * 转换字符串，防止字符串过长显示换行异常
	 * @param str
	 * @return
	 */
	public static String getNewString(String str){
		if (StringUtil.isNullOrEmpty(str)) {
			return "";
		}
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i]=='\n') {
				continue;
			}
			if (charArray[i]==' ') {
				charArray[i]='\u3000';
				continue;
			}
			if (charArray[i]<'\177') {
				charArray[i]=(char) (charArray[i]+65248);
			}
		}
		return new String(charArray);
	}
	
	/**
	 * 功能：从其他模块跳转到历史净值页面
	 * @param context 当前activity
	 * @param productCode 产品代码
	 * @param period 查询范围 0：三个月（默认）1:一年
	 * @param value 当天净值
	 * @param valueDate 净值日期
	 * @param isForResult true/向当前页反馈结果、false/不需要反馈结果
	 * @param requestCode startActivityForResult()的跳转请求码
	 */
	public static void toEchartWebViewActivity(Context context,String productCode, String period,
			String value,String valueDate,Boolean isForResult,int requestCode){
		if (StringUtil.isNullOrEmpty(productCode)||StringUtil.isNullOrEmpty(period)
				||StringUtil.isNullOrEmpty(value)||StringUtil.isNullOrEmpty(valueDate)) {
			return;
		}
		Intent intent = new Intent(context,EchartWebViewActivity.class);
		intent.putExtra("productCode", productCode);
		intent.putExtra("period", period);
		intent.putExtra("value", value);
		intent.putExtra("valueDate", valueDate);
		if (isForResult) {
			((FragmentActivity)context).startActivityForResult(intent, requestCode);
		}else {
			((FragmentActivity)context).startActivity(intent);
		}
	}
}
