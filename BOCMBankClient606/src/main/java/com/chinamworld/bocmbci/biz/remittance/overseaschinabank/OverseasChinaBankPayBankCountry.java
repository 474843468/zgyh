package com.chinamworld.bocmbci.biz.remittance.overseaschinabank;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.bii.constant.Remittance;
import com.chinamworld.bocmbci.biz.remittance.RemittanceBaseActivity;
import com.chinamworld.bocmbci.biz.remittance.RemittanceContent;
import com.chinamworld.bocmbci.biz.remittance.RemittanceDataCenter;
import com.chinamworld.bocmbci.biz.remittance.adapter.AreaAdapter;
import com.chinamworld.bocmbci.biz.remittance.adapter.PinyinAdapter;
import com.chinamworld.bocmbci.biz.remittance.adapter.model.RemittanceCollectionBankItem;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.OverseasChinaBankRemittanceInfoInputActivity;
import com.chinamworld.bocmbci.biz.remittance.applicationForRemittance.pinyin.AssortView;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.widget.LinearListView;
import com.chinamworld.bocmbci.widget.adapter.CommonAdapter;
import com.chinamworld.bocmbci.widget.adapter.ICommonAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/7 0007.
 */
public class OverseasChinaBankPayBankCountry extends RemittanceBaseActivity {

    /** 列表适配器 */
    private AreaAdapter adapter;
    /** 列表控件 */
    private ListView lv_overseaschianbank_seas;
    /** 收款银行地区信息 */
    private List<String> countryData;
    /** 被点击得地区（国家） */
    private String sear;


    private List<Map<String, Object>> listBySear = new ArrayList<Map<String, Object>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** 隐藏右按钮 */
        setRightTopGone();
        setTitle(this.getString(R.string.remittance_apply_overseas_chian_bank));
        addView(R.layout.remittance_info_input_choose_overpaybank_sears);
        initView();
    }

    private void initView() {
        lv_overseaschianbank_seas = (ListView) findViewById(R.id.lv_overseaschianbank_seas);
        countryData = RemittanceDataCenter.getInstance().getBocPayeeBankRegionList();
        lvadapter = new CommonAdapter<String>(OverseasChinaBankPayBankCountry.this, countryData, come);
        lv_overseaschianbank_seas.setAdapter(lvadapter);



        lv_overseaschianbank_seas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sear = countryData.get(i);

                Log.v("BiiHttpEngine",sear+"---------");
                Log.v("100",sear+"---------");

                requestPsnBOCPayeeBankInfoQuery();

            }
        });
    }

    private ICommonAdapter<String> come = new ICommonAdapter<String>() {

        @Override
        public View getView(int i, String s, LayoutInflater layoutInflater, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if(convertView == null){

                convertView = layoutInflater.inflate(R.layout.remittance_oversearchianbank_bankcountry, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text1);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(s);
            return convertView;
        }
    };

    class ViewHolder{
        TextView textView;
    }

    private CommonAdapter<String> lvadapter;

    /**
     * 调用查询境外中行收款银行信息接口
     */
    public void requestPsnBOCPayeeBankInfoQuery() {
        Map<String, Object> params = new HashMap<String, Object>();
        BaseHttpEngine.showProgressDialog();
        OverseasChinaBankPayBankCountry.this.getHttpTools().requestHttp("PsnBOCPayeeBankInfoQuery",
                "requestPsnBOCPayeeBankInfoQueryCallBack", params, false);


    }

    /**
     * 境外中行收款行信息查询接口回调
     * @param resultObj
     */
    public void requestPsnBOCPayeeBankInfoQueryCallBack(Object resultObj) {

        if (StringUtil.isNullOrEmpty(resultObj)) {
            BaseHttpEngine.dissMissProgressDialog();
            return;
        }
        BaseHttpEngine.dissMissProgressDialog();
        Log.v("BiiHttpEngine", resultObj + "");

        Map<String, Object> result = HttpTools.getResponseResult(resultObj);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> list = (List<Map<String, Object>>) result
                .get("bocPayeeBankInfoList");

        listBySear.clear();
        for (int i = 0; i < list.size(); i++) {
           String bocPayeeBankRegionCN = (String) list.get(i).get("bocPayeeBankRegionCN");
            if (sear.equals(bocPayeeBankRegionCN)) {
                Log.v("BiiHttpEngine",bocPayeeBankRegionCN+"===========");
                listBySear.add(list.get(i));
            } else {
                continue;
            }
        }

        RemittanceDataCenter.getInstance().setlistBySear(listBySear);

        if (listBySear.size()>1) {
            Intent intent = new Intent(
                    OverseasChinaBankPayBankCountry.this,
                    OverseasChinaBankCollectionBank.class);
            intent.putExtra("sear",sear);
            intent.setAction(OverseasChinaBankRemittanceInfoInputActivity.ACTION_COLLECTION_BANK);
            startActivityForResult(intent, RemittanceContent.RESULT_CODE_COLLECTION_BANK_RESULT);
        } else if(listBySear.size() == 1 ){
            Intent intent = new Intent();

            intent.putExtra("sear",sear);
            setResult(RemittanceContent.RESULT_CODE_PAY_BANK_AREA, intent);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RemittanceContent.RESULT_CODE_COLLECTION_BANK_RESULT){

            this.setResult(RemittanceContent.RESULT_CODE_COLLECTION_BANK_RESULT, data);     //就是在这里把返回所选择的城市setResult给FirstActivity
        }
        super.onActivityResult(requestCode, resultCode, data);
        finish();   //finish应该写到这个地方
    }


}
