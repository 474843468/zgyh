package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PinYinUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model.FundRegCompanyModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.presenter.TaAccountRegisterPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.model.InvstBindingInfoViewModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public class TaAccountRegisterFragment extends MvpBussFragment<TaAccountRegisterContract.Presenter>
        implements TaAccountRegisterContract.View {
    private final int REQCODE_SELECTCO = 1;

    private View rootView;
    private TaAccountRegisterContract.Presenter mTaAccountRegistPresenter;
    private EditChoiceWidget fincAccount;    // 基金交易账户
    private EditClearWidget taAccount;     // TA账户
    private EditChoiceWidget undRegName;     // 注册登记机构名称·
    private Button btn_next;     // 下一步按钮

    private FundRegCompanyModel.RegCompanyBean mSelectCompanyBean = null; //选择的机构

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_ta_account_register, null);
        return rootView;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fund_account_register);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void initView() {
        fincAccount = (EditChoiceWidget) rootView.findViewById(R.id.fincAccount);
        fincAccount.setArrowImageGone(false);
        // TODO: Change TextColor to gray HOW??
        taAccount = (EditClearWidget) rootView.findViewById(R.id.TaAccountNo);
        undRegName = (EditChoiceWidget) rootView.findViewById(R.id.undRegName);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
    }

    @Override
    public void initData() {
        InvstBindingInfoViewModel bindingInfo = (InvstBindingInfoViewModel) getArguments().getSerializable("binding_info");

        if (bindingInfo != null) {
            fincAccount.setChoiceTextContent(bindingInfo.getInvestAccount());
        }

        fincAccount.setChoiceTitleBold(true);
        taAccount.setEditWidgetHint(getString(R.string.boc_fund_num_ABC));
        taAccount.setChoiceTitleBold(true);
        undRegName.setChoiceTextContent(getString(R.string.boc_common_select));
        undRegName.setChoiceTitleBold(true);
    }

    @Override
    protected TaAccountRegisterContract.Presenter initPresenter() {
        return new TaAccountRegisterPresenter(this);
    }

    @Override
    public void setListener() {
        undRegName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingDialog();
                getPresenter().queryFundCoList();    // 上送参数为空
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 校验
                String taAccountNumber = taAccount.getEditWidgetContent();
                if (StringUtils.isEmptyOrNull(taAccountNumber)) {
                    showErrorDialog("请输入大写字母或数字，长度为12位字符");
                    return;
                }

                if (mSelectCompanyBean == null) {
                    showErrorDialog("请选择注册登记机构");
                    return;
                }

                TaAccountRegisterConfirmFragment taAccountRegisterConfirmFragment = new TaAccountRegisterConfirmFragment();
                Bundle bundle = new Bundle();
                bundle.putString("trans_account", fincAccount.getChoiceTextContent());    // 交易账户
                bundle.putString("ta_account", taAccountNumber);    // TA账户
                bundle.putSerializable("select_company", mSelectCompanyBean);    // 选择机构信息

                taAccountRegisterConfirmFragment.setArguments(bundle);
                start(taAccountRegisterConfirmFragment);
            }
        });
    }

//    正则校验
//    @Override
//    private void judge() {
//
//    }


    /**
     * 033注册登记机构请求成功，
     * 添加每个基金公司的首字母至viewModel
     * 将viewModel传给selectFundCoFragment
     */
    @Override
    public void queryFundCoListSuccess(FundRegCompanyModel result) {
        sortRegCompanyList(result);
        updateRegCompanyListTitle(result);
        Bundle bundle = new Bundle();
        bundle.putSerializable("regCoModel", result);
        SelectFundCoFragment fragment = new SelectFundCoFragment();
        fragment.setArguments(bundle);
        closeProgressDialog();
        startForResult(fragment, REQCODE_SELECTCO);
    }

    /**
     * 注册登记机构名称按拼音排序
     * 利用sort排序方法
     */
    private List<FundRegCompanyModel.RegCompanyBean> sortRegCompanyList(FundRegCompanyModel result) {
        List<FundRegCompanyModel.RegCompanyBean> sortList = result.getList();
        for (FundRegCompanyModel.RegCompanyBean bean : sortList) {
            bean.setFundRegNamePinyin(PinYinUtil.getPinYin(bean.getFundRegName()));
        }
//        for(int i = 0; i < sortList.size(); i++) {
//            sortList.get(i).setFundRegNamePinyin(PinYinUtil.getPinYin(sortList.get(i).getFundRegName()));
//        }
        LogUtil.d("FundRegName 0           :" + sortList.get(0).getFundRegName());
        LogUtil.d("FundRegNamePinyin 0 :" + sortList.get(0).getFundRegNamePinyin());
        Collections.sort(sortList);
        return sortList;
    }

    /**
     * 更新基金公司列表的字母标题
     */
    private void updateRegCompanyListTitle(FundRegCompanyModel model) {

        if (model == null || model.getList() == null) {
            return;
        }

        String lastTitleLetters = "";    // 上一次记录的首字母
        for (int i = 0; i < model.getList().size(); i++) {
            FundRegCompanyModel.RegCompanyBean bean = model.getList().get(i);
            String name = bean.getFundRegName();
            String compLetters = PinYinUtil.getPinYin(name);    // 公司名称对应的拼音
            String firstLetters = "";    // 首字母标识

            // toUpperCase.substring(0,1)升大写字母，截取第一个字母
            // trim().去除（单词间的空格）以外的空格
            if (!StringUtils.isEmptyOrNull(compLetters) && compLetters.trim().length() > 1) {
                firstLetters = compLetters.toUpperCase().substring(0, 1);
            } else {
                firstLetters = "";
            }

            if (!StringUtils.isEmptyOrNull(firstLetters)) {
                if (!firstLetters.equals(lastTitleLetters)) {
                    bean.setFundRegLetterTitle(firstLetters);
                    lastTitleLetters = firstLetters;
                }
            }
        }
    }

    @Override
    public void queryFundCoListFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);

        if (resultCode == REQCODE_SELECTCO && data != null) {
            mSelectCompanyBean =
                    (FundRegCompanyModel.RegCompanyBean) data.getSerializable("selectFundCo");
            if (mSelectCompanyBean != null) {
                undRegName.setChoiceTextContent(mSelectCompanyBean.getFundRegName());
            }
        }
    }
}