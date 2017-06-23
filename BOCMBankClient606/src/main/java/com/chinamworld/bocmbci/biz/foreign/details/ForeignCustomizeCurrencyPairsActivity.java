package com.chinamworld.bocmbci.biz.foreign.details;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.bii.constant.Forex;
import com.chinamworld.bocmbci.biz.foreign.ForeignBaseActivity;
import com.chinamworld.bocmbci.biz.foreign.ForeignDataCenter;
import com.chinamworld.bocmbci.biz.foreign.adapter.ForeignCurrencyPairsAdapter;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.KeyAndValueItem;
import com.chinamworld.bocmbci.utils.ListUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

/**
 * 汇率定制页面 用户可以制定5对常用的货币对
 * @author luqp 2016/10/10
 */
public class ForeignCustomizeCurrencyPairsActivity extends ForeignBaseActivity {
    private static final String TAG = "ForexRateNotifyActivity";
    /** 用户已选货币对个数*/
    private TextView selectedNumber = null;
    /** 用户已选货币对个数*/
    private TextView surplusNumber = null;
    /** 显示所有的货币对*/
    private ListView listView = null;
    /** allResult:全部货币对*/
    private List<Map<String, String>> allResult = null;
    /** 存储处理后符合条件的数据 */
    private List<Map<String, String>> allResultList = null;
    /** customerResult:用户定制的货币对*/
    private List<Map<String, String>> customerResult = null;
    /** 用于标志用户选中的货币对*/
    private List<Boolean> listFlag = null;
    private ForeignCurrencyPairsAdapter adapter = null;
    /** 用于记录用户选择的货币对位置 */
    private List<Integer> customerChoicePosition = null;
    /** 用于记录用户选择了多少对货币对 */
    private int number = 0;
    private String tokenId = null;
    /** 提交货币对 */
    private String selectedArr[] = null;
    /** 是否有数据 */
    private boolean isTrue = false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogGloble.d(TAG, "onCreate");
        commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        if (StringUtil.isNullOrEmpty(commConversationId)) {
            return;
        } else {
            initView();
            initOnClick();
            initTitleClick();  // 标题按钮点击事件
            // 查询用户定制的货币地
            requestPsnUserCrcyCodePair();
        }
    }

    /** 初始化所有的控件*/
    private void initView() {
        setContentView(R.layout.foreign_customize_currency_pairs);
        getBackGroundLayout().setTitleText(R.string.isForex_edit_self); // 设置标题
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE); // 设置右按钮显示
        selectedNumber = (TextView)findViewById(R.id.tv_selected_number); // 已选定个数
        surplusNumber = (TextView)findViewById(R.id.tv_surplus_number); // 剩余可选个数
        listView = (ListView) findViewById(R.id.forex_rate_gridView);
        customerChoicePosition = new ArrayList<Integer>();
        allResultList = new ArrayList<Map<String, String>>();
        allResult = new ArrayList<Map<String, String>>();
        customerResult = new ArrayList<Map<String, String>>();
        surplusNumber.setText("6");
    }

    /** 标题按钮点击事件*/
    public void initTitleClick(){
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE); //设置右按钮显示
        getBackGroundLayout().setMetalRightText("保存"); //设置右按钮文字
        getBackGroundLayout().setRightButtonTextColor(getResources().getColor(R.color.fonts_pink)); //设置右面字体颜色
        /** 右边按钮点击事件*/
        getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTrue) {
                    getSelectedPosition();
                }
            }
        });

        /** 返回按钮点击事件 停止自动刷新*/
        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allResult.clear();
                allResultList.clear();
                customerResult.clear();
                listFlag.clear();
                customerChoicePosition.clear();
                finish();
            }
        });
    }

    private void initOnClick() {
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtil.isNullOrEmpty(customerResult) || customerResult == null || customerResult.size() <= 0) {
                    // 用户没有定制货币对
                    if (listFlag.get(position)) {
                        number--;
                        if (number <= 0) {
                            number = 0;
                        }
                        listFlag.set(position, false);
                        adapter.dateChanged(listFlag);
                        selectedNumber.setText(""+number); //用户已选货币对
                        surplusNumber.setText(""+ (6 - number)); //剩余可选货币对
                    } else {
                        if (number > 5) {
                            /** 选择的货币对超过5对，弹出提示框 */
//                            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                                    ForeignCustomizeCurrencyPairsActivity.this.getString(R.string.forex_rate_makecode));
                            MessageDialog.showMessageDialog(ForeignCustomizeCurrencyPairsActivity.this,getString(R.string.forex_rate_makecode));
                            listFlag.set(position, false);
                            adapter.dateChanged(listFlag);
                            return;
                        } else {
                            number++;
                            listFlag.set(position, true);
                            adapter.dateChanged(listFlag);
                            selectedNumber.setText(""+number); //用户已选货币对
                            surplusNumber.setText(""+ (6 - number)); //剩余可选货币对
                        }
                    }
                } else {  // 用户定制货币对，得到货币对的数目
                    number = adapter.getNumber();
                    if (listFlag.get(position)) {
                        number--;
                        if (number <= 0) {
                            number = 0;
                        }
                        listFlag.set(position, false);
                        adapter.dateChanged(listFlag);
                        selectedNumber.setText(""+number); //用户已选货币对
                        surplusNumber.setText(""+ (6 - number)); //剩余可选货币对
                    } else {
                        if (number > 5) {
                            /** 选择的货币对超过5对，弹出提示框 */
//                            BaseDroidApp.getInstanse().showInfoMessageDialog(
//                                    ForeignCustomizeCurrencyPairsActivity.this.getString(R.string.forex_rate_makecode));
                            MessageDialog.showMessageDialog(ForeignCustomizeCurrencyPairsActivity.this,getString(R.string.forex_rate_makecode));
                            listFlag.set(position, false);
                            adapter.dateChanged(listFlag);
                            return;
                        } else {
                            number++;
                            listFlag.set(position, true);
                            adapter.dateChanged(listFlag);
                            selectedNumber.setText(""+number); //用户已选货币对
                            surplusNumber.setText(""+ (6 - number)); //剩余可选货币对
                        }
                    }
                }
            }
        });
    }

    /** 查询所有的货币对--05*/
    private void requestPsnAllCrcyCodePairs() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_ALLRATE_CODE);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnAllCrcyCodePairsCallback");
    }

    /**
     * 查询所有的货币对05---回调
     * @param resultObj :返回结果
     */
    public void requestPsnAllCrcyCodePairsCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        // 得到result
        allResult = (List<Map<String, String>>) biiResponseBody.getResult();
        if (StringUtil.isNullOrEmpty(allResult) || allResult == null || allResult.size() <= 0) {
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
            return;
        } else {
            setListFlagValue();
            dealAllCode(); // 处理全部货币对数据
        }
    }

    /** 将listFlag的值全部设置为false */
    private void setListFlagValue() {
        listFlag = new ArrayList<Boolean>();
        for (int i = 0; i < allResult.size(); i++) {
            listFlag.add(false);
        }
    }

    /** 查询用户定制的货币对---11*/
    private void requestPsnUserCrcyCodePair() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_USER_CrcyCode);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        BaseHttpEngine.showProgressDialogCanGoBack();
        HttpManager.requestBii(biiRequestBody, this, "requestPsnUserCrcyCodePairCallback");
    }

    /**
     * 用户定制货币对11-----回调
     * @param resultObj:返回结果
     */
    public void requestPsnUserCrcyCodePairCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
        if (result != null && result.size() > 0) {
            // 用户已经定制货币对 对得到的货币对进行处理，货币对只能<=6
            int len = result.size();
            for (int i = 0; i < len; i++) {
                Map<String, String> map = result.get(i);
                if (!StringUtil.isNullOrEmpty(map)) {
                    // tag=1:用户定制货币对，tag=0:未定制货币对
                    String sourceCode = map.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
                    String targetCode = map.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
                    if (!LocalData.CurrencyShort.containsKey(sourceCode)
                            || !LocalData.CurrencyShort.containsKey(targetCode)) {
                    } else {
                        customerResult.add(map);
                    }
                }
            }
        }
        requestPsnAllCrcyCodePairs();  // 查询所有的货币对
    }

    /** 处理全部货币对的数据 */
    public void dealAllCode() {
        for (int i = 0; i < allResult.size(); i++) {
            Map<String, String> map = allResult.get(i);
            String sourceCode = map.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
            String targetCode = map.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
            if (!LocalData.CurrencyShort.containsKey(sourceCode) || !LocalData.CurrencyShort.containsKey(targetCode)) {
                // 不符合数据移除
            } else {
                allResultList.add(map);
            }
        }
        if (StringUtil.isNullOrEmpty(allResultList)) {
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
            return;
        } else {
            isTrue = true;
            List<Map<String,Object>>allList = (List) allResultList;
            ListUtils.sortForInvest(allList, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
                @Override
                public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
                    String sourceCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
                    String targetCurrencyCode = (String) stringObjectMap.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
                    if (keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals
                            (targetCurrencyCode)) {
                        return true;
                    }
                    return false;
                }
            });
            if (StringUtil.isNullOrEmpty(customerResult)) { // 用户没有定制货币对
                adapter = new ForeignCurrencyPairsAdapter(ForeignCustomizeCurrencyPairsActivity.this, allList);
                listView.setAdapter(adapter);
                listView.setFocusable(true);
            } else { // 用户已经定制货币对
                checkCurrency(allResultList, customerResult);
                adapter = new ForeignCurrencyPairsAdapter(ForeignCustomizeCurrencyPairsActivity.this, allList, listFlag);
                listView.setAdapter(adapter);
                listView.setFocusable(true);
            }
            number = adapter.getNumber();
            selectedNumber.setText(""+number); //用户已选货币对
            surplusNumber.setText(""+ (6 - number)); //剩余可选货币对
        }
    }

    /**
     * 用于标志用户定制的货币对，true-定制，false-未定制
     * @param allResult :全部货币对list
     * @param customerResult :用户定制的货币对
     */
    private void checkCurrency(List<Map<String, String>> allResult, List<Map<String, String>> customerResult) {
        int len1 = allResult.size();
        int len2 = customerResult.size();
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                String sourceCus = customerResult.get(j).get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
                String targetCus = customerResult.get(j).get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
                String sourceAll = allResult.get(i).get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
                String targetAll = allResult.get(i).get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
                if (sourceCus.equals(sourceAll) && targetCus.equals(targetAll)) {
                    listFlag.set(i, true); // 两个List里面有相同的数据时
                    continue;
                }
            }
        }
    }

    /** 得到用户选择的货币对位置 */
    public void getSelectedPosition() {
        int count = 0;
        for (int i = 0; i < listFlag.size(); i++) {
            if (listFlag.get(i)) {
                Map<String, String> choiseMap = allResultList.get(i);
                String sourceCurCde = choiseMap.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
                String targetCurCde = choiseMap.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
                if (!StringUtil.isNullOrEmpty(sourceCurCde) && !StringUtil.isNullOrEmpty(targetCurCde)) {
                    String sourceDealCode = null;
                    if (LocalData.CurrencyShort.containsKey(sourceCurCde)) {
                        sourceDealCode = LocalData.CurrencyShort.get(sourceCurCde);
                    }
                    String targetDealCode = null;
                    if (LocalData.CurrencyShort.containsKey(targetCurCde)) {
                        targetDealCode = LocalData.CurrencyShort.get(targetCurCde);
                    }
                    if (!StringUtil.isNullOrEmpty(sourceDealCode) && !StringUtil.isNullOrEmpty(targetDealCode)) {
                        customerChoicePosition.add(i);
                        count++;
                    }
                }
            }
        }
        if (count <= 0) {  // 用户没有定制货币对
            BaseDroidApp.getInstanse().showInfoMessageDialog(
                    ForeignCustomizeCurrencyPairsActivity.this.getString(R.string.forex_rate_makeRate_notify));
            return;
        } else {
            getSelectedCode();
        }
    }

    /** 得到用户选择的货币对 */
    public void getSelectedCode() {
        if (customerChoicePosition == null) {
            return;
        } else {
            int len = customerChoicePosition.size();
            /** 存储用户选择的源货币对代码 */
            List<String> selectedCodeList = new ArrayList<String>();
            /** 存储用户选择的目标货币对代码 */
            List<String> selectedTargetCodeList = new ArrayList<String>();
            for (int i = 0; i < len; i++) {
                int position = customerChoicePosition.get(i);
                Map<String, String> choiseMap = allResultList.get(position);
                String sourceCurCde = choiseMap.get(Forex.FOREX_MAKE_SOURCECURCDE_RES);
                String targetCurCde = choiseMap.get(Forex.FOREX_MAKE_TARGETCURCDE_RES);
                if (!StringUtil.isNull(sourceCurCde) && !StringUtil.isNull(targetCurCde)) {
                    selectedCodeList.add(sourceCurCde);
                    selectedTargetCodeList.add(targetCurCde);
                }
            }
            if (selectedCodeList == null || selectedCodeList.size() <= 0 || selectedTargetCodeList == null
                    || selectedTargetCodeList.size() <= 0) {
                return;
            } else {
                int len1 = selectedCodeList.size();
                int len2 = selectedTargetCodeList.size();
                if (len1 == len2) {
                    selectedArr = new String[len1];
                    for (int i = 0; i < len1; i++) {
                        String source = selectedCodeList.get(i);
                        String target = selectedTargetCodeList.get(i);
                        StringBuilder sb = new StringBuilder(source);
                        sb.append(target);
                        selectedArr[i] = sb.toString();
                    }
                } else {
                    return;
                }
                requestCommConversationId();
                BaseHttpEngine.showProgressDialogCanGoBack();
            }
        }
    }

    /** 请求ConversationId--回调*/
    public void requestCommConversationIdCallBack(Object resultObj) {
        super.requestCommConversationIdCallBack(resultObj);
        commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        if (StringUtil.isNull(commConversationId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {
            requestPSNGetTokenId();
        }
    }

    /** 获取tocken*/
    private void requestPSNGetTokenId() {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Comm.PSNINVESTMENTMANAGEISOPEN_TOKENID_API);
        biiRequestBody.setConversationId(commConversationId);
        biiRequestBody.setParams(null);
        HttpManager.requestBii(biiRequestBody, this, "requestPSNGetTokenIdCallback");
    }

    /**
     * 获取tokenId----回调
     * @param resultObj :返回结果
     */
    public void requestPSNGetTokenIdCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        tokenId = (String) biiResponseBody.getResult();
        if (StringUtil.isNull(tokenId)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        } else {
            LogGloble.d(TAG + " tokenId", tokenId);
            requestPsnSetCustmerCrcyPair(selectedArr);
        }
    }

    /** 客户定制货币对提交 */
    private void requestPsnSetCustmerCrcyPair(String[] list) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(Forex.FOREX_PSNSETCUSTEMRCRCY_API);
        biiRequestBody.setConversationId(commConversationId);
        Map<String, Object> map = new Hashtable<String, Object>();
        map.put(Forex.FOREX_HIDDENCURRENCYPAIR_REQ, list);
        map.put(Forex.FOREX_TOKEN_CODE_REQ, tokenId);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnSetCustmerCrcyPairCallback");
    }

    /**
     * 客户定制货币对提交---回调
     * @param resultObj
     */
    public void requestPsnSetCustmerCrcyPairCallback(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, String>> resultList = (List<Map<String, String>>) biiResponseBody.getResult();
        CustomDialog.toastInCenter(this, getString(R.string.forex_rate_makeCode_success));
        Intent intent = new Intent(ForeignCustomizeCurrencyPairsActivity.this, ForeignExchangeRateActivity.class);
        startActivity(intent);
        finish();
    }
}