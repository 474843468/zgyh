package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.activity.NewStyleBaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Finc;
import com.chinamworld.bocmbci.bii.constant.Login;
import com.chinamworld.bocmbci.biz.bocinvt.productlist.TestInvtEvaluationAnswerActivity;
import com.chinamworld.bocmbci.biz.finc.control.FincControl;
import com.chinamworld.bocmbci.biz.finc.fundacc.FincFundAccMainActivity;
import com.chinamworld.bocmbci.biz.finc.fundprice.FincFundDetailActivityNew;
import com.chinamworld.bocmbci.biz.finc.fundprice.FundPricesActivityNew;
import com.chinamworld.bocmbci.biz.finc.orcm.OrcmProductListActivity;
import com.chinamworld.bocmbci.biz.finc.query.FincQueryDQDEActivity;
import com.chinamworld.bocmbci.biz.invest.activity.InvesAgreeActivity;
import com.chinamworld.bocmbci.biz.invest.constant.InvestConstant;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.NewLabelTextView;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 基金基类
 */
public class FincBaseActivity extends NewStyleBaseActivity {
    protected ActivityTaskManager activityTaskManager = ActivityTaskManager
            .getInstance();
    protected FincControl fincControl = FincControl.getInstance();

    @Override
    public ActivityTaskType getActivityTaskType() {
        return ActivityTaskType.TwoTask;
    }

    /**
     * 创建详情页面文本显示LabelTextView控件
     *
     * @param resid
     * @param valuetext
     * @return
     */
    public NewLabelTextView createNewLabelTextView(int resid, String valuetext) {
        NewLabelTextView v = new NewLabelTextView(this);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setLabelText(resid);
        v.setValueText(StringUtil.isNullChange(valuetext));
        v.setLabelTextColor(getResources().getColor(R.color.fonts_black));
        v.setValueTextColor(getResources().getColor(R.color.fonts_dark_gray));
        v.setLabelTextSize(13);
        v.setValueTextSize(13);
        v.setValueTextBold(true);

//        v.setWeightShowRate("1:2");
        return v;
    }

    /**
     * 判断是否开通投资理财服务
     */
    public void doCheckRequestPsnInvestmentManageIsOpen() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_API);
        biiRequestBody.setConversationId(null);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this,
                "doCheckRequestPsnInvestmentManageIsOpenCallback");
    }
    /**
     * 检查时调用的 是否开通中银理财服务 回调
     *
     * @param resultObj
     */
    public void doCheckRequestPsnInvestmentManageIsOpenCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        String isOpenOr = (String) biiResponseBody.getResult();
        if (StringUtil.parseStrToBoolean(isOpenOr)) {
            fincControl.ifInvestMent = true;
        } else {
            fincControl.ifInvestMent = false;
        }
        doCheckRequestQueryInvtBindingInfo();

    }
    /**
     * 查询基金账户check
     */
    public void doCheckRequestQueryInvtBindingInfo() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_QUERYINVTBINDINGINFO_API);
        biiRequestBody.setConversationId(null);
        Map<String, String> map = new Hashtable<String, String>();
        map.put(Finc.FINC_INVTTYPE_REQ, ConstantGloble.FINC_SERVICECODE);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this,
                "doCheckRequestQueryInvtBindingInfoCallback");
    }

    /**
     * 这个东西怎么处理
     *
     * @param resultObj
     */
    public void doCheckRequestQueryInvtBindingInfoCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        if (biiResponseBody.getResult() == null ) {
            fincControl.accId = null;
            fincControl.ifhaveaccId = false;
            BaseHttpEngine.dissMissProgressDialog();
            getPopup();
            return;
        } else {
            Map<String, String> map = (Map<String, String>) biiResponseBodys
                    .get(0).getResult();
            if (!StringUtil.isNullOrEmpty(map.get(Finc.FINC_INVESTACCOUNT_RES))) {
                fincControl.accId = map.get(Finc.FINC_ACCOUNTID_RES);
                fincControl.bankId = map.get(Finc.FINC_BANKID_RES);
                fincControl.invAccId = map.get(Finc.FINC_INVESTACCOUNT_RES);
                fincControl.accNum = map.get(Finc.FINC_ACCOUNT_RES);
                fincControl.accDetailsMap = map;
                fincControl.ifhaveaccId = true;
            }
        }
        if (!fincControl.ifInvestMent) {
            BaseHttpEngine.dissMissProgressDialog();
            getPopup();
            return;
        }
//		else {
//			// 这个接口必须有账户的时候才可以
//			doCheckRequestPsnFundRiskEvaluationQueryResult();
//		}

    }

    /**
     * 任务提示框
     */
    public void getPopup() {
        View popupView = LayoutInflater.from(this).inflate(
                R.layout.finc_task_notify, null);
        // 关闭按钮
        ImageView taskPopCloseButton = (ImageView) popupView
                .findViewById(R.id.top_right_close);
        // 设定账户
        LinearLayout accButtonView = (LinearLayout) popupView
                .findViewById(R.id.forex_acc_button_show);
        LinearLayout accTextView = (LinearLayout) popupView
                .findViewById(R.id.forex_acc_text_hide);
        // TextView setAccButton = (TextView) popupView
        // .findViewById(R.id.forex_acc_button);

        // 理财服务功能
        LinearLayout moneyButtonView = (LinearLayout) popupView
                .findViewById(R.id.forex_money_button_show);
        LinearLayout moneyTextView = (LinearLayout) popupView
                .findViewById(R.id.forex_money_text_hide);

        // 风险评估
        LinearLayout risktestBtnLayout = (LinearLayout) popupView
                .findViewById(R.id.finc_risk_button_show);
        risktestBtnLayout.setVisibility(View.GONE);
        LinearLayout risktestTextLayout = (LinearLayout) popupView
                .findViewById(R.id.finc_risk_text_hide);
        // TextView ristTestButton = (TextView) popupView
        // .findViewById(R.id.finc_risktest_button);

        // 先判断是否开通投资理财服务
        if (fincControl.ifInvestMent && fincControl.ifhaveaccId) {
            // finishPopupWindow();
            return;
        }
        if (fincControl.ifInvestMent) {// 已经开通投资理财
            moneyButtonView.setVisibility(View.GONE);
            moneyTextView.setVisibility(View.VISIBLE);
        } else {// 没有开通投资理财服务开通投资理财服务
            moneyButtonView.setVisibility(View.VISIBLE);
            moneyTextView.setVisibility(View.GONE);
            moneyButtonView.setOnClickListener(new View.OnClickListener() {
                // @Override
                public void onClick(View v) {
                    // 跳转到投资理财服务协议页面
                    Intent gotoIntent = new Intent(BaseDroidApp.getInstanse()
                            .getCurrentAct(), InvesAgreeActivity.class);
                    startActivityForResult(gotoIntent,
                            ConstantGloble.ACTIVITY_REQUEST_MANAGE_CODE);

                }
            });
        }
        if (fincControl.ifhaveaccId) {// 有基金账户
            accButtonView.setVisibility(View.GONE);
            accTextView.setVisibility(View.VISIBLE);
        } else {// 没基金账户 设定 基金账户
            accButtonView.setVisibility(View.VISIBLE);
            accTextView.setVisibility(View.GONE);
            if (fincControl.ifInvestMent) {
                accButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 到设定基金账户设定页面
                        Intent gotoIntent = new Intent(BaseDroidApp
                                .getInstanse().getCurrentAct(),
                                FincFundAccMainActivity.class);
                        startActivityForResult(
                                gotoIntent,
                                ConstantGloble.ACTIVITY_REQUEST_SETFINCSACC_CODE);
                    }
                });
            } else {
                accButtonView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CustomDialog.toastInCenter(
                                BaseDroidApp.getInstanse().getCurrentAct(),
                                BaseDroidApp
                                        .getInstanse()
                                        .getCurrentAct()
                                        .getString(
                                                R.string.bocinvt_task_toast_1));

                    }
                });
            }
        }
        // 关闭按钮事件
        taskPopCloseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BaseDroidApp.getInstanse().dismissMessageDialog();
                // modi by fsm
                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincQueryDQDEActivity)
                    BaseDroidApp.getInstanse().getCurrentAct().finish();

                // 503add
                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincFundDetailActivityNew)
                    ActivityTaskManager.getInstance().removeAllActivity();
//					BaseDroidApp.getInstanse().getCurrentAct().finish();

                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivityNew) {
//					ActivityTaskManager.getInstance().removeAllSecondActivity();
                    finish();
                }
                //TODO 基金推荐弹框，点击X之后，返回到首页
                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof OrcmProductListActivity) {
                    finish();
                }
                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivity) {
                    finish();
                }
                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincProductDetailActivity) {
                    finish();
                }
            }
        });
        // taskPop.setFocusable(true);
        BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);

    }

    /**
     * 获取所有的基金公司
     *
     * @Author
     */
    protected void getFundCompanyList() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_GETFUNDCOMPANCYLIST);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this,
                "getFundCompanyListCallback");
    }

    /**
     * 查询所有的基金公司回调处理
     *
     * @Author
     * @param resultObj
     */
    public void getFundCompanyListCallback(Object resultObj) {

    }

    /**
     * 基金组合查询
     *
     * @Author
     */
    protected void combainQueryFundInfos(int currentIndex, int pageSize,
                                         String currencyCode, String fundCompanyCode, String risklv,
                                         String fntype , String fundProductTypeStr,String fundState,
                                         String sortFlag ,String sortField) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        if (getString(R.string.all).equals(fundCompanyCode)) {// 如果是全部 就传入空
            fundCompanyCode = null;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
        params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(pageSize));
        params.put(Finc.PSNQUERYFUNDDETAIL_CURRENCYCODE, currencyCode);
        params.put(Finc.PSNQUERYFUNDDETAIL_COMPANY, fundCompanyCode);
        params.put(Finc.PSNQUERYFUNDDETAIL_RISKGRADE, risklv);
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, fntype);
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, fundProductTypeStr);
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
        biiRequestBody.setParams(params);
        HttpManager.requestBii(biiRequestBody, this,
                "combainQueryFundInfosCallback");
    }

    /**
     * 基金组合查询
     * */
    public void combainQueryFundInfosCallback(Object resultObj) {
    }

    /** 查询关注的基金 */
    public void attentionFundQuery() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_ATTENTIONQUERYLIST);
        biiRequestBody.setConversationId(null);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this,
                "attentionFundQueryCallback");
    }

    /**
     * 查询关注的基金 返回处理
     *
     * @param resultObj
     */
    public void attentionFundQueryCallback(Object resultObj) {

    }

    /**
     * @author  基金持仓--查询基金持仓信息
     */
    public void requestPsnFundQueryFundBalance() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_FUNDQUERYFUNDBALANCE_API);
        biiRequestBody.setConversationId(null);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this,
                "requestPsnFundQueryFundBalanceCallback");
    }

    /**
     * @author 基金持仓--查询基金持仓信息
     */
    public void requestPsnFundQueryFundBalance(String fundCode) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_FUNDQUERYFUNDBALANCE_API);
        biiRequestBody.setConversationId(null);
        Map<String, String> map = new Hashtable<String, String>();
        map.put(Finc.I_FUNDCODE, fundCode);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this,
                "requestPsnFundQueryFundBalanceCallback");
    }

    /**
     * @author  基金持仓--查询基金持仓信息--回调
     */
    public void requestPsnFundQueryFundBalanceCallback(Object resultObj) {

    }

    /**
     * 检查是否做了风险认证
     */
    public void doCheckRequestPsnFundRiskEvaluationQueryResult() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_RISKEVALUATIONQUERYRESULT_API);
        biiRequestBody.setConversationId(null);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this,
                "doCheckRequestPsnFundRiskEvaluationQueryResultCallback");
    }

    /**
     * 检查是否做了风险认证的回调处理
     *
     * @param resultObj
     */
    public void doCheckRequestPsnFundRiskEvaluationQueryResultCallback(
            Object resultObj) {
        // BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, String> resultMap = (Map<String, String>) biiResponseBody
                .getResult();
        if (resultMap == null) {

//			fincControl.ifdorisk = false;
//			BaseHttpEngine.dissMissProgressDialog();
//			getPopupForRisk();

            return;
        }
        String isEvaluated = resultMap.get(Finc.FINC_ISEVALUATED_RES);
        if (StringUtil.parseStrToBoolean(isEvaluated)) {// 做了风险评估
            fincControl.ifdorisk = true;
            fincControl.userRiskLevel = resultMap.get(Finc.FINC_RISKLEVEL_RES);
            BaseDroidApp.getInstanse().getBizDataMap()
                    .put(ConstantGloble.BOCINVT_EVALUATION_RESULT, resultMap);
        } else {
            fincControl.ifdorisk = false;
            BaseHttpEngine.dissMissProgressDialog();
            getPopupForRisk();
            return;
        }

    }

    /**
     * 风险评估 P601改造任务提示框
     */
    public void getPopupForRisk() {
        View popupView = LayoutInflater.from(this).inflate(
                R.layout.finc_task_notify, null);
        // 关闭按钮
        ImageView taskPopCloseButton = (ImageView) popupView
                .findViewById(R.id.top_right_close);
        // title
        TextView titleTextView = (TextView) popupView
                .findViewById(R.id.tv_acc_account_accountState);
        titleTextView.setText("您需要完成风险评估才可以进行基金交易");

        // 设定账户
        LinearLayout accButtonView = (LinearLayout) popupView
                .findViewById(R.id.forex_acc_button_show);
        accButtonView.setVisibility(View.GONE);

        // 理财服务功能
        LinearLayout moneyButtonView = (LinearLayout) popupView
                .findViewById(R.id.forex_money_button_show);
        moneyButtonView.setVisibility(View.GONE);

        // 风险评估
        LinearLayout risktestBtnLayout = (LinearLayout) popupView
                .findViewById(R.id.finc_risk_button_show);
        risktestBtnLayout.setVisibility(View.VISIBLE);
        LinearLayout risktestTextLayout = (LinearLayout) popupView
                .findViewById(R.id.finc_risk_text_hide);

        // 先判断是否开通投资理财服务
        if (fincControl.ifdorisk) {
            // finishPopupWindow();
            risktestBtnLayout.setVisibility(View.GONE);
            risktestTextLayout.setVisibility(View.VISIBLE);
            return;
        }
        // 未风险认证
        // 理财服务并且有账户
        // 开通风险认证可点击
        risktestBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BaseDroidApp.getInstanse()
                        .getCurrentAct(),
                        TestInvtEvaluationAnswerActivity.class);
                intent.putExtra(InvestConstant.RISKTYPE,
                        InvestConstant.FUNDRISK);
                intent.putExtra(ConstantGloble.BOCINVT_ISNEWEVA, false);
                // intent.putExtra(InvestConstant.FROMMYSELF, true);
                BaseDroidApp
                        .getInstanse()
                        .getCurrentAct()
                        .startActivityForResult(intent,
                                InvestConstant.FUNDRISK);

            }
        });
        // 关闭按钮事件
        taskPopCloseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                BaseDroidApp.getInstanse().dismissMessageDialog();
                // modi by fsm
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincQueryDQDEActivity)
//					BaseDroidApp.getInstanse().getCurrentAct().finish();
//
//				// 503add
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincFundDetailActivityNew)
//					ActivityTaskManager.getInstance().removeAllActivity();
////					BaseDroidApp.getInstanse().getCurrentAct().finish();
//
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivityNew) {
////					ActivityTaskManager.getInstance().removeAllSecondActivity();
//					finish();
//				}
//				//TODO 基金推荐弹框，点击X之后，返回到首页
//				if (BaseDroidApp.getInstanse().getCurrentAct() instanceof OrcmProductListActivity) {
//					finish();
//				}

                // 503add
                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FincFundDetailActivityNew)
                    ActivityTaskManager.getInstance().removeAllActivity();
//					BaseDroidApp.getInstanse().getCurrentAct().finish();

                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof FundPricesActivityNew) {
//					ActivityTaskManager.getInstance().removeAllSecondActivity();
                    finish();
                }
                //TODO 基金推荐弹框，点击X之后，返回到首页
                if (BaseDroidApp.getInstanse().getCurrentAct() instanceof OrcmProductListActivity) {
                    finish();
                }
            }
        });
        // taskPop.setFocusable(true);
        BaseDroidApp.getInstanse().showAccountMessageDialog(popupView);
    }

    /**
     * 获取tocken
     */
    public void requestPSNGetTokenId() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this,
                "requestPSNGetTokenIdCallback");
    }

    /**
     * 获取tokenId----回调
     *
     * @param resultObj
     *            :返回结果
     */
    public void requestPSNGetTokenIdCallback(Object resultObj) {
    }

    /**
     * 请求密码控件随机数
     */
    public void requestForRandomNumber(String conversationId) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Login.AQUIRE_RANDOM_NUMBER_API);
        biiRequestBody.setConversationId(conversationId);
        // 获取 随机数
        HttpManager.requestBii(biiRequestBody, this,
                "queryRandomNumberCallBack");
    }

    /**
     * 请求密码控件随机数 回调
     *
     * @param resultObj
     */
    public void queryRandomNumberCallBack(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        // 加密控件设置随机数
        fincControl.randomNumber = (String) biiResponseBody.getResult();
    }

    public void attentionFundAdd(String fundCode, String token) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_ATTENTIONQFUNDADD);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        Map<String, String> map = new HashMap<String, String>();
        map.put(Finc.FINC_FUNDCODE, fundCode);
        map.put(Finc.TOKEN, token);
        biiRequestBody.setParams(map);
        HttpManager
                .requestBii(biiRequestBody, this, "attentionFundAddCallback");
    }

    public void attentionFundAddCallback(Object resultObj) {

    }

    public void attentionFundConsern(String fundCode, String token) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_ATTENTIONQFUNDCONCEL);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        Map<String, String> map = new HashMap<String, String>();
        map.put(Finc.FINC_FUNDCODE, fundCode);
        map.put(Finc.TOKEN, token);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this,
                "attentionFundConsernCallback");
    }

    public void attentionFundConsernCallback(Object resultObj) {

    }

    /**2014-11-3 dxd
     * 基金基本信息查询
     * @param fundCode
     */
    protected void getFincFund(String fundCode) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_GETFUNDDETAIL);
        Map<String, String> map = new HashMap<String, String>();
        map.put(Finc.I_FUNDCODE, fundCode);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this,
                "getFincFundCallback");
    }

    /**
     * 基金基本信息查询  回调处理
     * @param resultObj
     */
    public void getFincFundCallback(Object resultObj) {

    }

    /**
     * 基金行情查询 401 快速查询
     *
     * @param fundInfo
     *            基金代码或名称
     */
    public void fastQuery(String fundInfo,Integer currentIndex,String fundState,
                          String sortFlag ,String sortField) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.PSNQUERYFUNDDETAIL);
//		biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
//				.getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        Map<String, String> params = new HashMap<String, String>();
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDINFO, fundInfo);
        params.put(Finc.COMBINQUERY_CURRENTINDEX, String.valueOf(currentIndex));
        params.put(Finc.COMBINQUERY_PAGESIZE, String.valueOf(10));
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDTYPE, "00");
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDKIND, "00");
        params.put(Finc.PSNQUERYFUNDDETAIL_FUNDSTATE, fundState);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFLAG, sortFlag);
        params.put(Finc.PSNQUERYFUNDDETAIL_SORTFIELD, sortField);
        biiRequestBody.setParams(params);
        HttpManager.requestBii(biiRequestBody, this, "fastQueryCallback");
    }

    /**
     *
     * @param resultObj
     */
    public void fastQueryCallback(Object resultObj) {
    }

    /**
     * 指令交易产品查询
     * @param protpye 交易分类
     * @param tradeType 交易类型
     * @param pageSize 页面大小
     * @param currentIndex 当前页索引
     * @param _refresh
     */
    public void requestPsnOcrmProductQuery(String protpye, String tradeType, String pageSize,
                                           String currentIndex, boolean _refresh) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.METHOD_PSNOCRMPRODUCTQUERY);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Finc.PROTPYE, "01");
        map.put(Finc.I_TRADETYPE, tradeType);
        map.put(Finc.FINC_PAGESIZE, pageSize);
        map.put(Finc.COMBINQUERY_CURRENTINDEX, currentIndex);
        map.put(Finc.FINC_REFRESH, String.valueOf(_refresh));
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this, "ocrmProductQueryCallBack");
     //   this.getHttpTools().registAllErrorCode(Finc.METHOD_PSNOCRMPRODUCTQUERY);
    }

    public void ocrmProductQueryCallBack(Object resultObj){
        fincControl.OcrmProductMap = HttpTools.getResponseResult(resultObj);
        if(!StringUtil.isNullOrEmpty(fincControl.OcrmProductMap))
            fincControl.OcrmProductList = (List<Map<String, Object>>) fincControl.OcrmProductMap
                    .get(Finc.RESULTLIST);
    }

    /**
     * 在途交易查询
     *
     * @param currentIndex
     *            当前页
     * @param pageSize
     *            每页显示条数
     * @param flush
     *            刷新标志 是否刷新
     */
    public void fundQueryEnTrust(int currentIndex, int pageSize, boolean flush) {
        BaseHttpEngine.showProgressDialog();
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.QUERYTRANSONTRAN);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(Finc.FINC_CURRENTINDEX, String.valueOf(currentIndex));
        map.put(Finc.FINC_PAGESIZE, String.valueOf(pageSize));
        map.put(Finc.FINC_REFRESH, String.valueOf(flush));
        biiRequestBody.setParams(map);
        HttpManager
                .requestBii(biiRequestBody, this, "fundQueryEnTrustCallback");
    }

    /**
     * 在途交易查询
     *
     * @param resultObj
     */
    public void fundQueryEnTrustCallback(Object resultObj) {

    }

    /**登录前基金详情查询
     * 基金基本信息查询
     * @param fundCode
     */
    protected void getPsnFundDetailQueryOutlay(String fundCode) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod("PsnFundDetailQueryOutlay");
        Map<String, String> map = new HashMap<String, String>();
        map.put(Finc.I_FUNDCODE, fundCode);
        biiRequestBody.setParams(map);
        HttpManager.requestOutlayBii(biiRequestBody, this,
                "getPsnFundDetailQueryOutlayCallback");
    }

    /**
     * 登录前基金详情查询  回调处理
     * @param resultObj
     */
    public void getPsnFundDetailQueryOutlayCallback(Object resultObj) {

    }

    /**
     * 浮动盈亏 测算 20 基金35
     */
    public void getFDYKList(String fundCode, String startDate, String endDate) {
        BaseHttpEngine.showProgressDialog();
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Finc.FINC_FLOATPROFITANDLOSS);
        biiRequestBody.setConversationId((String) BaseDroidApp.getInstanse()
                .getBizDataMap().get(ConstantGloble.CONVERSATION_ID));
        Map<String, String> map = new HashMap<String, String>();
        if (!fundCode.equals(getString(R.string.finc_allFund))) {// 如果不是全部基金...
            map.put(Finc.FINC_FUNDCODE, fundCode);
        }
        map.put(Finc.FINC_STARTDATE, startDate);
        map.put(Finc.FINC_ENDDATE, endDate);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this, "getFDYKListCallback");

    }

    /**
     * 浮动盈亏 测算 20 基金35 回调
     *
     * @param resultObj
     */
    public void getFDYKListCallback(Object resultObj) {
    }
}
