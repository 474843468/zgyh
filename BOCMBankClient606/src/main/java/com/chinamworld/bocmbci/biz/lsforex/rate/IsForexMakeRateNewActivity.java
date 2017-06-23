package com.chinamworld.bocmbci.biz.lsforex.rate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiRequestBody;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.IsForex;
import com.chinamworld.bocmbci.biz.lsforex.IsForexBaseNewActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.constant.DictionaryData;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpManager;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.KeyAndValueItem;
import com.chinamworld.bocmbci.utils.ListUtils;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.CustomDialog;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zxj on 2016/10/18.
 * 汇率定制
 */
public class IsForexMakeRateNewActivity extends IsForexBaseNewActivity implements ICommonAdapter<Map<String, Object>> {

//    private TextView forex_make_text;
    //定制自选
    private TextView tv_selected_number;
    private TextView tv_surplus_number;
    private ListView forex_rate_listView;
    /**保存按钮*/
//    private Button rightBtn;
    /** allResult:全部货币对 */
    private List<Map<String, Object>> allResult = null;
    /** customerResult:用户定制的货币对 */
    private List<Map<String, Object>> customerResult = null;
    /**适配器*/
    private static CommonAdapter<Map<String, Object>> adapter;
    /** 用于记录用户选择了多少对货币对 */
    private int number = 0;
    /** 标记选中的list*/
    private List<Boolean> customerCoseList = null;
    /** 用于记录用户选择的货币对位置 */
    private List<Integer> customerChoicePosition = null;
    /** 提交货币对 */
    private String selectedArr[] = null;
    /** 记录定制的货币对数目 */
    private int length = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBaseLayout();
        commConversationId = (String) BaseDroidApp.getInstanse().getBizDataMap().get(ConstantGloble.CONVERSATION_ID);
        if(StringUtil.isNullOrEmpty(commConversationId)){
            return;
        }else {
            init();
            initClick();
            /**查询用户定制币种*/
            BaseHttpEngine.showProgressDialog();
            requestCustomerSetRate();
        }
    }
    /**
     * 初始化基类布局
     */
    private void initBaseLayout(){
        setLeftButtonPopupGone();
        getBackGroundLayout().setTitleText("编辑自选");
        getBackGroundLayout().setMetalRightText("保存");
        getBackGroundLayout().setRightButtonTextColor(getResources().getColor(R.color.fonts_pink)); //设置右面字体颜色
//        getBackGroundLayout().setOnLeftButtonImage(getResources().getDrawable(R.drawable.base_btn_left_back_black));
        getBackGroundLayout().setLeftButtonVisibility(View.VISIBLE);
        getBackGroundLayout().setRightButtonVisibility(View.VISIBLE);
//        getBackGroundLayout().setRightButtonImage(getResources().getDrawable(R.drawable.btn_rqcode));
        getBackGroundLayout().setShareButtonVisibility(View.GONE);
        getBackGroundLayout().setOnLeftButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getBackGroundLayout().setOnRightButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseHttpEngine.showProgressDialog();
                getSelectedPosition();
            }
        });


        getBackGroundLayout().setOnShareButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void init(){
        setContentView(R.layout.forex_rate_make_code_new);
//        rightBtn = (Button)findViewById(R.id.ib_top_right_btn);
//        forex_make_text = (TextView)findViewById(R.id.forex_make_text);
        tv_selected_number = (TextView)findViewById(R.id.tv_selected_number);
        tv_surplus_number = (TextView)findViewById(R.id.tv_surplus_number);
        forex_rate_listView = (ListView)findViewById(R.id.forex_rate_listView);
        allResult = new ArrayList<Map<String, Object>>();
        customerResult = new ArrayList<Map<String, Object>>();
        customerChoicePosition = new ArrayList<Integer>();
    }
    private void initClick(){
        forex_rate_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                /**后台最少返3个定制的货币对 */
                number = getNumber();
                if (allResult.get(position).get("flag").equals("1")) {
                    number--;
                    if (number <= 0) {
                        number = 0;
                    }
                    allResult.get(position).put("flag", "0");
                    adapter.setSourceList(allResult, 0);
                } else {
                    if (number > 5) {
                        /** 选择的货币对超过6对，弹出提示框 */
                        BaseDroidApp.getInstanse().showInfoMessageDialog(
                                IsForexMakeRateNewActivity.this.getString(R.string.forex_rate_makecode));
                        allResult.get(position).put("flag", "0");
                        adapter.setSourceList(allResult, 0);
                        return;
                    } else {
                        number++;
                        allResult.get(position).put("flag", "1");
                        adapter.setSourceList(allResult, 0);
                    }
                }
                tv_selected_number.setText(""+number);
                tv_surplus_number.setText(""+(6-number));
//                forex_make_text.setText("已定制"+(number)+"个内容，还可添加"+(6-number)+"个内容");
            }
        });
//        rightBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BaseHttpEngine.showProgressDialog();
//                getSelectedPosition();
//            }
//        });
    }
    /** 得到用户选择的货币对位置 */
    public void getSelectedPosition() {
        int count = 0;
        int len = allResult.size();
        for (int i = 0; i < len; i++) {
            //flag=1被选中
            if (allResult.get(i).get("flag").equals("1")) {
                Map<String, Object> choiseMap = allResult.get(i);
                String sourceCurCde = (String) choiseMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
                String targetCurCde = (String) choiseMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
                if (!StringUtil.isNullOrEmpty(sourceCurCde) && !StringUtil.isNullOrEmpty(targetCurCde)) {
                    String sourceDealCode = null;
                    if (LocalData.Currency.containsKey(sourceCurCde)) {
                        sourceDealCode = LocalData.Currency.get(sourceCurCde);
                    }
                    String targetDealCode = null;
                    if (LocalData.Currency.containsKey(targetCurCde)) {
                        targetDealCode = LocalData.Currency.get(targetCurCde);
                    }
                    if (!StringUtil.isNullOrEmpty(sourceDealCode) && !StringUtil.isNullOrEmpty(targetDealCode)) {
                        customerChoicePosition.add(i);
                        count++;
                    }
                }
            }// if
        }// for
        if (count <= 0) {
            // 用户没有定制货币对
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(
                    IsForexMakeRateNewActivity.this.getString(R.string.forex_rate_makeRate_notify));
            return;
        } else {
            getSelectedCode();
        }
    }
    /** 得到用户选择的货币对 */
    public void getSelectedCode() {
        if (customerChoicePosition == null || customerChoicePosition.size() <= 0) {
            return;
        } else {
            int len = customerChoicePosition.size();
            /** 存储用户选择的源货币对代码 */
            List<String> selectedCodeList = new ArrayList<String>();
            /** 存储用户选择的目标货币对代码 */
            List<String> selectedTargetCodeList = new ArrayList<String>();
            for (int i = 0; i < len; i++) {
                int position = customerChoicePosition.get(i);
                Map<String, Object> choiseMap = allResult.get(position);
                String sourceCurCde = (String) choiseMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
                String targetCurCde = (String) choiseMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
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
                    length = len1;
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
                // 货币对提交
                requestPsnVFGRateSetting(ConstantGloble.ISFOREX_UPDATE, selectedArr);
            }
        }
    }

    /** 汇率定制 */
    private void requestPsnVFGRateSetting(String submitType, String[] list) {
        BiiRequestBody biiRequestBody = new BiiRequestBody();
        biiRequestBody.setMethod(IsForex.ISFOREX_PSNVFGRATESETTING_API);
        biiRequestBody.setConversationId(commConversationId);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(IsForex.ISFOREX_SUBMITTYPE_REQ, submitType);
        map.put(IsForex.ISFOREX_HIDDENCURRENCYPAIR_REQ, list);
        biiRequestBody.setParams(map);
        HttpManager.requestBii(biiRequestBody, this, "requestPsnVFGRateSettingCallback");
    }

    /** 汇率定制 -----回调 */
    public void requestPsnVFGRateSettingCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        List<Map<String, String>> result = (List<Map<String, String>>) biiResponseBody.getResult();
        BaseHttpEngine.dissMissProgressDialog();
         if (result == null || result.size() != length) {
             CustomDialog.toastInCenter(this,
             getString(R.string.forex_rate_makeCode_failuer));
             return;
         } else {
        // 返回到外汇详情页面
        CustomDialog.toastInCenter(this, getString(R.string.forex_rate_makeCode_success));
        Intent intent = new Intent(IsForexMakeRateNewActivity.this, IsForexTwoWayTreasureNewActivity.class);
        startActivity(intent);
        finish();
         }
    }


    /** 查询用户定制的货币对 */
    @Override
    public void requestCustomerSetRateCallback(Object resultObj) {
        super.requestCustomerSetRateCallback(resultObj);
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        customerResult = (List<Map<String, Object>>) biiResponseBody.getResult();
        if (customerResult != null && customerResult.size() > 0) {
            // 处理用户定制的货币对
            customerResult = getTrueDate(customerResult);
        }
        tv_selected_number.setText(""+(customerResult.size()));
        tv_surplus_number.setText(""+(6-customerResult.size()));
//        forex_make_text.setText("已定制"+(customerResult.size())+"个内容，还可添加"+(6-customerResult.size())+"个内容");
        // 查询全部汇率
        requestPsnVFGGetAllRate("");
    }
    /** 查询全部汇率---回调 */
    @Override
    public void requestPsnVFGGetAllRateCallback(Object resultObj) {
        BiiResponse biiResponse = (BiiResponse) resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        // 得到response
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        allResult = (List<Map<String, Object>>) biiResponseBody.getResult();
        if (allResult == null || allResult.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
            return;
        }
        allResult = getTrueDate(allResult);
        allResult = setListFlagValue();
        if (allResult == null || allResult.size() <= 0) {
            BaseHttpEngine.dissMissProgressDialog();
            BaseDroidApp.getInstanse().showInfoMessageDialog(getResources().getString(R.string.forex_no_list));
            return;
        }
        BaseHttpEngine.dissMissProgressDialog();
        ListUtils.sortForInvest(allResult, new ListUtils.ISynchronousList<Map<String, Object>, KeyAndValueItem>() {
            @Override
            public boolean doSomeThing(Map<String, Object> stringObjectMap, KeyAndValueItem keyAndValueItem) {
                String sourceCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
                String targetCurrencyCode = (String) stringObjectMap.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
                if(keyAndValueItem.getKey().equals(sourceCurrencyCode) && keyAndValueItem.getValue().equals(targetCurrencyCode)){
                    stringObjectMap.put("formatNumber",keyAndValueItem.getParam());
                    return true;
                }
                return false;
            }
        });
        if (StringUtil.isNullOrEmpty(customerResult)) {
            // 用户没有定制货币对
            adapter = new CommonAdapter<Map<String, Object>>(IsForexMakeRateNewActivity.this, allResult, this);
            forex_rate_listView.setAdapter(adapter);
            forex_rate_listView.setFocusable(true);
        } else {
            // 用户已经定制货币对
            checkCurrency(allResult, customerResult);
            adapter = new CommonAdapter<Map<String, Object>>(IsForexMakeRateNewActivity.this, allResult, this);
            forex_rate_listView.setAdapter(adapter);
            forex_rate_listView.setFocusable(true);
        }
    }
    /**将listFlag的值全部设置为0*/
    private List<Map<String, Object>> setListFlagValue(){
        List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
        for(int i=0; i<allResult.size(); i++){
            Map<String, Object> rateMap = allResult.get(i);
            rateMap.put("flag", "0");
            dateList.add(rateMap);
        }
        return dateList;
    }
    /**
     * 用于标志用户定制的货币对，1-定制，0-未定制
     *
     * @param allResult
     *            :全部货币对list
     * @param customerResult
     *            :用户定制的货币对
     */
    private void checkCurrency(List<Map<String, Object>> allResult, List<Map<String, Object>> customerResult) {
        int len1 = allResult.size();
        int len2 = customerResult.size();
        for (int i = 0; i < len1; i++) {
            Map<String, Object> rateMap = allResult.get(i);
            for (int j = 0; j < len2; j++) {
                String sourceCus = (String) customerResult.get(j).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
                String targetCus = (String) customerResult.get(j).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
                String sourceAll = (String) allResult.get(i).get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
                String targetAll = (String) allResult.get(i).get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
                if (sourceCus.equals(sourceAll) && targetCus.equals(targetAll)) {
                    // 两个List里面有相同的数据时
                    rateMap.put("flag", "1");
                    continue;
                }
            }
        }
    }

    /** 处理货币对，返回的货币对可能没有对应的名称，将其除去 */
    private List<Map<String, Object>> getTrueDate(List<Map<String, Object>> list) {
        int len = list.size();
        List<Map<String, Object>> dateList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < len; i++) {
            Map<String, Object> map = (Map<String, Object>) list.get(i);
            // 得到源货币的代码
            String sourceCurrencyCode = (String) map.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
            String targetCurrencyCode = (String) map.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
            if (!StringUtil.isNull(sourceCurrencyCode) && !StringUtil.isNull(targetCurrencyCode)
                    && LocalData.Currency.containsKey(sourceCurrencyCode) && LocalData.Currency.containsKey(targetCurrencyCode)) {
                dateList.add(map);
            }
        }
        return dateList;
    }

    @Override
    public View getView(int i, final Map<String, Object> currentItem, LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
        ViewHoldler holder = null;
        if(convertView == null){
            holder = new ViewHoldler();
            convertView = inflater.inflate(R.layout.forex_rate_make_code_list_new, null);
            holder.rate_currency = (TextView)convertView.findViewById(R.id.rate_currency);
            holder.rate_check = (CheckBox) convertView.findViewById(R.id.rate_check);
            convertView.setTag(holder);
        }else {
            holder = (ViewHoldler) convertView.getTag();
        }
        String temp = "";
        // 得到源货币的代码
        String sourceCurrencyCode = (String) currentItem.get(IsForex.ISFOREX_SOURCECURRENCYCODE1_RES);
        String sourceDealCode = null;
        /** 得到目标货币代码*/
        String targetCurrencyCode = (String) currentItem.get(IsForex.ISFOREX_TARGETCURRENCYCODE1_RES);
        String targetDealCode = null;
        sourceDealCode = DictionaryData.transCurrency(sourceCurrencyCode);
        targetDealCode = DictionaryData.transCurrency(targetCurrencyCode);
        StringBuilder sb = new StringBuilder(sourceDealCode);
        sb.append("/");
        sb.append(targetDealCode);
        //货币对
        holder.rate_currency.setText(sb.toString().trim());
//        temp = LocalData.code_Map.get(sourceCurrencyCode+targetCurrencyCode);
//        if(!StringUtil.isNullOrEmpty(temp)){
//            //货币对
//            holder.rate_currency.setText(temp);
//        }else {
//            if (LocalData.Currency.containsKey(sourceCurrencyCode)) {
//                sourceDealCode = LocalData.Currency.get(sourceCurrencyCode);
//            }
//            if (LocalData.Currency.containsKey(targetCurrencyCode)) {
//                targetDealCode = LocalData.Currency.get(targetCurrencyCode);
//            }
//            StringBuilder sb = new StringBuilder(sourceDealCode);
//            sb.append("/");
//            sb.append(targetDealCode);
//            //货币对
//            holder.rate_currency.setText(sb.toString().trim());
//        }
        String flag = (String)currentItem.get("flag");
        if(flag.equals("1")){
            holder.rate_check.setBackgroundResource(R.drawable.boc_checkbox_true);
        }else {
            holder.rate_check.setBackgroundResource(R.drawable.boc_checkbox);
        }


        return convertView;
    }
    class ViewHoldler {
        TextView rate_currency;
        CheckBox rate_check;
    }
    private int getNumber() {
        int len = allResult.size();
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (allResult.get(i).get("flag").equals("1")) {
                count++;
            }
        }
        return count;
    }
}
