package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeData;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.base.model.WealthAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthDetailsBean;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.utils.ResultConvertUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuweidong on 2016/10/25.
 */
public class BuildViewData {
    /**
     * 封装筛选数据
     */
    public static List<SelectTypeData> buildSelectData() {
        String[] name = {"交易币种", "风险级别", "基金状态"};
        String[] value0 = {"全部", "人民币元", "英镑", "港币","美元", "日元", "欧元"};
        String[] value1 = {"全部", "低风险", "中低风险", "中风险", "中高风险", "高风险"};
        String[] value2 = {"全部", "正常开放", "可认购", "暂停交易", "暂停申购", "暂停赎回"};
        String[] id0 = {"000", "001", "012", "013", "014", "027","038"};
        String[] id1 = {"0", "1", "2", "3", "4", "5"};
        String[] id2 = {"00", "0", "1", "4", "5", "6"};
        List<String[]> contentList = new ArrayList<>();
        contentList.add(value0);
        contentList.add(value1);
        contentList.add(value2);
        //       contentList.add(value3);

        List<SelectTypeData> list = new ArrayList<>();
        for (int i = 0; i < name.length; i++) {
            SelectTypeData item = new SelectTypeData();
            List<Content> itemList = new ArrayList<>();
            String[] tempValues = contentList.get(i);
            for (int j = 0; j < tempValues.length; j++) {
                Content content = new Content();
                content.setName(tempValues[j]);
                if(i == 0){//币种
                    content.setContentNameID(id0[j]);
                    item.setDefaultId(id0[1]);
                    if(j==1){
                        content.setSelected(true);
                    }
                }
                if(i == 1){//风险级别
                    item.setDefaultId(id1[0]);
                    content.setContentNameID(id1[j]);
                    if(j==0){
                        content.setSelected(true);
                    }
                }
                if(i == 2){//基金状态
                    item.setDefaultId(id2[1]);
                    content.setContentNameID(id2[j]);
                    if(j==1){
                        content.setSelected(true);
                    }
                }
                itemList.add(content);
            }
            item.setTitle(name[i]);
            item.setList(itemList);
            list.add(item);
        }
        return list;
    }

    /**
     * 理财账户（登录后）
     *
     * @param accountList
     * @return
     */
    public static List<Content> buildSingleData(List<WealthAccountBean> accountList) {
        List<Content> list = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
            Content content = new Content();
            content.setName(accountList.get(i).getAccountNo());
            content.setContentNameID(accountList.get(i).getAccountId());
            if (i == 0) {
                content.setSelected(true);
            }
            list.add(content);
        }
        return list;
    }
}
