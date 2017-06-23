package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.ui;

import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayAccountDetailModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model.PrepayVerifyRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.ErrorException;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.QueryAllChinaBankAccountRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.changeRepaymentAccount.model.RepaymentAccountCheckRes;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

import java.util.List;

/**
 * 贷款管理-中银E贷-提前还款接口类 Created by xintong on 2016/6/23.
 */
public class PrepayContract {

	public interface PrepayView extends BaseView<Presenter> {

		// 获取会话ID，成功调用
		void obtainConversationSuccess(String conversationId);

		// 获取会话ID，失败调用
		void obtainConversationFail(ErrorException e);

		// 安全因子，成功调用
		void obtainSecurityFactorSuccess(SecurityFactorModel result);

		// 安全因子，失败调用
		void obtainSecurityFactorFail(ErrorException e);

		// 提前还款预交易成功调用
		void prepayVerifySuccess(PrepayVerifyRes result);

		// 提前还款预交易失败调用
		void prepayVerifyFail(ErrorException e);

		// 提前还款根据账户ID查询账户详情成功
		// void
		// prepayCheckAccountDetailSuccess(PsnAccountQueryAccountDetailResult
		// result);
		// void prepayCheckAccountDetailSuccess(PrepayAccountDetailModel
		// result);
		void prepayCheckAccountDetailSuccess(
				PrepayAccountDetailModel.AccountDetaiListBean result);

		// 提前还款根据账户ID查询账户详情失败
		void prepayCheckAccountDetailFail(ErrorException e);

		// 获取中行所有帐户列表，成功调用
		void obtainAllChinaBankAccountSuccess(
				List<QueryAllChinaBankAccountRes> res);

		// 获取中行所有帐户列表，失败调用
		void obtainAllChinaBankAccountFail(ErrorException e);
		//==========2016年10月10日 20:23:24 yx add
		//还款账户检查，成功调用
		void doRepaymentAccountCheckSuccess(RepaymentAccountCheckRes res);
		//还款账户检查，失败调用
		void doRepaymentAccountCheckFail(ErrorException e);

	}

	public interface PrepayConfirmView extends BaseView<Presenter> {

		// 获取随机数成功
		void obtainRandomSuccess(String random);

		// 获取随机数失败
		void obtainRandomFail(ErrorException e);

		// 提前还款预交易成功调用
		void prepayVerifySuccess(PrepayVerifyRes result);

		// 提前还款预交易失败调用
		void prepayVerifyFail(ErrorException e);

		// 提前还款提交交易成功调用
		void prepaySubmitSuccess(PrepaySubmitRes result);

		// 提前还款提交交易失败调用
		void prepaySubmitFail(ErrorException e);
	}

	public interface Presenter extends BasePresenter {

		// 创建页面公共会话
		void creatConversation();

		// 获取安全因子
		void getSecurityFactor();

		// 获取随机数
		void getRandom();

		// 提前还款预交易
		void prepayVerify();

		// 提前还款提交交易
		void prepaySubmit();

		// 提前还款根据账户ID查询账户详情
		void prepayCheckAccountDetail(String accountID);

		/**
		 * 查询中行所有帐户列表
		 * 
		 * @param mListDataAccountType
		 *            账户类型
		 */
		void queryAllChinaBankAccount(List<String> mListDataAccountType);

		//还款账户检查
		void checkRepaymentAccount(QueryAllChinaBankAccountRes queryAllChinaBankAccountRes);
	}
}
