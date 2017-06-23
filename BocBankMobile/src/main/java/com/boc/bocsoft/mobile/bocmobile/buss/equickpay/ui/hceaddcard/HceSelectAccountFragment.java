package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.AddNewCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.CardBrandModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter.HceSelectAccountPresent;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceUtil;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;

import java.util.ArrayList;

/**
 * Created by yangle on 2016/12/15.
 * 描述: hce选择账户界面
 */
public class HceSelectAccountFragment extends SelectAccoutFragment<HceSelectAccountContract.Presenter> implements HceSelectAccountContract.View {
    public static final String CURRENT_NEW_CARD_MODEL = "card_model";
    public static final String SELECTED_NEW_CARD_SUPPORT_HCE = "selected_new_card_support_hce";
    private  AddNewCardViewModel mCurrentNewCardModel;
    private AccountBean mAccountBean;

    public static HceSelectAccountFragment newInstance(ArrayList<String> types,AddNewCardViewModel newCardViewModel) {
        HceSelectAccountFragment fragment = new HceSelectAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(SelectAccoutFragment.ACCOUNT_TYPE_LIST, types);
        bundle.putParcelable(CURRENT_NEW_CARD_MODEL,newCardViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected HceSelectAccountContract.Presenter initPresenter() {
        return new HceSelectAccountPresent(this);
    }

    @Override
    public void initData() {
        super.initData();
        mCurrentNewCardModel = getArguments().getParcelable(CURRENT_NEW_CARD_MODEL);
        if (PublicUtils.isEmpty(accountListAdapter.getDatas())){
            super.showNoDataView("你暂无可支持e闪付的卡");
            return;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mAccountBean = accountListAdapter.getItem(position).getAccountBean();

        if (isSelectedChange(mAccountBean)) {
            getPresenter().queryAccountDetail(mAccountBean);
        } else {
            backWithResult();
        }
    }

    private boolean isSelectedChange(AccountBean accountBean) {
        return !accountBean.getAccountId().equals(mCurrentNewCardModel.getAccountId());
    }

    @Override
    public void onQueryCardBrandSuccess(AccountBean supportHceAccount, CardBrandModel cardBrand) {
        // // TODO: 2016/12/30 添加本地是否已申请的判断
//        if (hasAppliedHce(cardBrand)) {
//            return;
//        }
        mCurrentNewCardModel.setAccountId(supportHceAccount.getAccountId());
        mCurrentNewCardModel.setMasterCardNo(supportHceAccount.getAccountNumber());
        mCurrentNewCardModel.setAccountType(supportHceAccount.getAccountType());
        mCurrentNewCardModel.setCardBrandModel(cardBrand);
        backWithResult();
    }

    // 判断此品牌是否已经申请过hce卡
    private boolean hasAppliedHce(CardBrandModel cardBrand) {
        String resultStr;

        int applicationType = cardBrand.getAppTypeValue();
        try {
             resultStr = HceUtil.getHceCardNoOrSerialNo(mContext, applicationType, HceConstants.HCE_CARD_FLAG);
        } catch (Exception e) {
            ToastUtils.showLong("很抱歉，e闪付控件异常");
            e.printStackTrace();
            return false;
        }

        if (resultStr != null && resultStr.length() != 4) {
            LogUtil.d("已申请的e闪付卡号：" +resultStr);
            showErrorDialog("很抱歉，本手机已申请过该品牌的e闪付卡，不能再次申请");
            return true;
        }

        return false;
    }

    private void backWithResult() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(SELECTED_NEW_CARD_SUPPORT_HCE,mCurrentNewCardModel);
        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT, bundle);
        pop();
    }

    @Override
    public void showLoading() {
        super.closeProgressDialog();
    }

    @Override
    public void closeLoading() {
        super.closeProgressDialog();
    }
}
