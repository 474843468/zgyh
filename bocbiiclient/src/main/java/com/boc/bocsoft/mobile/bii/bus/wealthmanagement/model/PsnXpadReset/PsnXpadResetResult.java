package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReset;

/**
 * 理财账号重新登记(资金账户列表)_结果
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnXpadResetResult {

        //网银账户标识
        public String accountId;
        //账户名称
        public String accountName;
        //账号
        public String accountNumber;
        //联行号
        public String accountIbkNum;
        //账户类型(账户，卡，存折等)
        public String accountType;
        //所属银行机构标识
        public Integer branchId;
        //账户别名
        public String nickName;
        //	账户状态
        public String accountStatus;
        //	使用客户标识
        public Integer customerId;
        //	货币码
        public String currencyCode;
        //货币代码2 ->{目前双币信用卡使用}
        public String currencyCode2;
        //所属网银机构名称
        public String branchName;
        //	卡类型描述,供页面展示{如：都市信用卡}
        public String cardDescription;
        //是否有旧账号
        public String hasOldAccountFlag;
        //验证因子HMAC加密串
        public String verifyFactor;
        //是否开通电子现金功能
        public String isECashAccount;
        //账号缓存标识
        public String accountKey;
        //是否一卡双账户  1:是  0：否
        public String isMedicalAccount;
        //是否电子卡账户  1:电子卡  0:非电子卡
        public String eCard;
        //卡片描述代码
        public String cardDescriptionCode;
}
