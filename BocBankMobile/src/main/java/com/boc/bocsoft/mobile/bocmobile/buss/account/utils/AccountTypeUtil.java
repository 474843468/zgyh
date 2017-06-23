package com.boc.bocsoft.mobile.bocmobile.buss.account.utils;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeData;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.singlemoreselect.Content;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         16/8/10 19:26
 *         账户类型Util
 */
public class AccountTypeUtil {

    /** 整存整取 */
    public static final String REGULAR_TYPE_ZCZQ = "110";
    /** 定活两变 */
    public static final String REGULAR_TYPE_DHLB = "160";
    /** 通知存款 */
    public static final String REGULAR_TYPE_TZCK = "166";

    /** 冻结 */
    public static final String ACCOUNT_STATUS_DJ = "06";
    /** 已销户 */
    public static final String ACCOUNT_STATUS_YXH = "01";
    /** 有效 */
    public static final String ACCOUNT_STATUS_NORMAL = "00";

    /** 是否为医保账户/电子现金账户 */
    public static final String RESULT_TRUE_FINANCE_MEDICAL = "1";

    /**
     * 获取活期账户类型
     *
     * @return
     */
    public static List<String> getCurrentType() {
        List<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_ORD);
        types.add(ApplicationConst.ACC_TYPE_BRO);
        types.add(ApplicationConst.ACC_TYPE_RAN);
        types.add(ApplicationConst.ACC_TYPE_YOUHUITONG);
        return types;
    }

    /**
     * 获取活期账户类型(排除借记卡)
     *
     * @return
     */
    public static List<String> getCurrentTypeWithOutBro() {
        List<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_ORD);
        types.add(ApplicationConst.ACC_TYPE_RAN);
        types.add(ApplicationConst.ACC_TYPE_YOUHUITONG);
        return types;
    }

    /**
     * 获取信用卡账户类型
     *
     * @return
     */
    public static ArrayList<String> getCreditType() {
        ArrayList<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        types.add(ApplicationConst.ACC_TYPE_GRE);
        types.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);
        return types;
    }

    /**
     * 获取定期账户类型
     *
     * @return
     */
    public static List<String> getRegularType() {
        List<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_CBQX);
        types.add(ApplicationConst.ACC_TYPE_ZOR);
        types.add(ApplicationConst.ACC_TYPE_EDU);
        types.add(ApplicationConst.ACC_TYPE_REG);
        return types;
    }

    /**
     * 获取网上专属理财账户类型
     *
     * @return
     */
    public static List<String> getMoneyType() {
        List<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_BOCINVT);
        return types;
    }

    /**
     * 获取纯电子现金账户类型
     *
     * @return
     */
    public static List<String> getFinanceType() {
        List<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_ECASH);
        return types;
    }

    /**
     * 获取虚拟银行卡类型
     *
     * @return
     */
    public static List<String> getVirtualType() {
        List<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_XNCRCD2);
        types.add(ApplicationConst.ACC_TYPE_XNCRCD1);
        return types;
    }

    /**
     * 获取借记卡类型
     *
     * @return
     */
    public static ArrayList<String> getBroType() {
        ArrayList<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_BRO);
        return types;
    }

    /**
     * 获取可以冻结/挂失类型
     *
     * @return
     */
    public static List<String> getCanFrozen() {
        List<String> types = new ArrayList<>();
        types.add(ApplicationConst.ACC_TYPE_BRO);
        types.add(ApplicationConst.ACC_TYPE_ORD);
        types.add(ApplicationConst.ACC_TYPE_REG);
        types.add(ApplicationConst.ACC_TYPE_RAN);
        types.add(ApplicationConst.ACC_TYPE_YOUHUITONG);
        return types;
    }

    /**
     * 获取允许动户通知类型
     *
     * @return
     */
    public static List<String> getCanNotify() {
        List<String> types = AccountTypeUtil.getCurrentType();
        types.add(ApplicationConst.ACC_TYPE_REG);
        return types;
    }

    public static List<SelectTypeData> getAllSelectType(String accountType) {
        List<SelectTypeData> typeList;

        SelectTypeData typeData1 = new SelectTypeData();
        Content content0 = new Content();
        content0.setName("全部");
        content0.setContentNameID(SelectTypeView.DEFAULT_ID);
        content0.setSelected(true);

        Content content1 = new Content();
        content1.setName("整存整取");
        content1.setContentNameID(REGULAR_TYPE_ZCZQ);
        content1.setSelected(false);

        Content content2 = new Content();
        content2.setName("定活两便");
        content2.setContentNameID(REGULAR_TYPE_DHLB);
        content2.setSelected(false);

        Content content3 = new Content();
        content3.setName("通知存款");
        content3.setContentNameID(REGULAR_TYPE_TZCK);
        content3.setSelected(false);

        List<Content> contentList1 = new ArrayList<Content>();
        contentList1.add(content0);
        contentList1.add(content1);
        contentList1.add(content2);
        contentList1.add(content3);

        typeData1.setDefaultId(SelectTypeView.DEFAULT_ID);
        typeData1.setTitle("存款类型");
        typeData1.setList(contentList1);

        /****************************************************/
        SelectTypeData typeData01 = new SelectTypeData();
        Content content01 = new Content();
        content01.setName("全部");
        content01.setContentNameID(SelectTypeView.DEFAULT_ID);
        content01.setSelected(false);

        Content content02 = new Content();
        content02.setName("有效");
        content02.setContentNameID(ACCOUNT_STATUS_NORMAL);
        content02.setSelected(true);

        Content content03 = new Content();
        content03.setName("冻结");
        content03.setContentNameID(ACCOUNT_STATUS_DJ);
        content03.setSelected(false);

        Content content04 = new Content();
        content04.setName("已销户");
        content04.setContentNameID(ACCOUNT_STATUS_YXH);
        content04.setSelected(false);

        List<Content> contentList01 = new ArrayList<>();
        contentList01.add(content01);
        contentList01.add(content02);
        contentList01.add(content03);
        contentList01.add(content04);

        typeData01.setDefaultId(ACCOUNT_STATUS_NORMAL);
        typeData01.setTitle("存单状态");
        typeData01.setList(contentList01);

        typeList = new ArrayList<>();
        if (ApplicationConst.ACC_TYPE_REG.equals(accountType))
            typeList.add(typeData1);
        typeList.add(typeData01);

        return typeList;
    }

    /**
     * 获取账户概览页面需要查询余额接口账户数量
     * @return
     */
    public static int getCanQueryDetailCount() {
        List<String> types = new ArrayList<>();
        types.addAll(AccountTypeUtil.getBroType());
        types.addAll(AccountTypeUtil.getCreditType());
        types.addAll(AccountTypeUtil.getCurrentTypeWithOutBro());
        types.addAll(AccountTypeUtil.getFinanceType());
        types.addAll(AccountTypeUtil.getMoneyType());
        return ApplicationContext.getInstance().getChinaBankAccountList(types).size();
    }
}
