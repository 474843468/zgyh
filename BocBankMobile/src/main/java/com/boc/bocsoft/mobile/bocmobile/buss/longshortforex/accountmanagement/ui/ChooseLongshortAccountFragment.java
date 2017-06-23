package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.adapter.FilterDebitCardAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGFilterDebitCardViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.presenter.ChooseLongshortAccountPresenter;

import java.util.List;

/**
 * Fragment：双向宝-账户管理-新增保证金账户-选择账户列表
 * Created by zhx on 2016/12/12
 */
public class ChooseLongshortAccountFragment extends MvpBussFragment<ChooseLongshortAccountPresenter> implements ChooseLongshortAccountContact.View {
    private View mRootView;
    private FilterDebitCardAdapter mAdapter;
    private ListView lv_list;
    private VFGFilterDebitCardViewModel viewModel;
    private String currentAccountNumber; // 当前交易账户
    private String oldAccountNumber; // 原账户(old)
    private int fromWhere; // 从哪个页面过来（1表示AddNewBailFragment, 2表示ChangeContractFragment, 3表示ChangeTradeAccountFragment）

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.fragment_choose_long_short_account, null);
        return mRootView;
    }

    @Override
    public void initView() {
        lv_list = (ListView) mRootView.findViewById(R.id.lv_list);
    }

    @Override
    public void initData() {
        currentAccountNumber = getArguments().getString("currentAccountNumber");
        oldAccountNumber = getArguments().getString("oldAccountNumber");
        fromWhere = getArguments().getInt("fromWhere");

        VFGFilterDebitCardViewModel vfgFilterDebitCardViewModel = new VFGFilterDebitCardViewModel();
        showLoadingDialog("加载中...");
        getPresenter().vFGFilterDebitCard(vfgFilterDebitCardViewModel);
    }

    // 从哪个页面过来（1表示AddNewBailFragment, 2表示ChangeContractFragment, 3表示ChangeTradeAccountFragment）
    public static ChooseLongshortAccountFragment newInstance(String currentAccountNumber, String oldAccountNumber, int fromWhere) {
        Bundle args = new Bundle();
        args.putString("currentAccountNumber", currentAccountNumber);
        args.putString("oldAccountNumber", oldAccountNumber);
        args.putInt("fromWhere", fromWhere);
        ChooseLongshortAccountFragment fragment = new ChooseLongshortAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setListener() {
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity = viewModel.getCardList().get(position);

                switch (fromWhere) {
                    case 2:
                        if (oldAccountNumber.equals(debitCardEntity.getAccountNumber())) {
                            showErrorDialog("原资金账户不能和新资金账户相同，请重新选择！");
                            return;
                        }
                        break;
                    case 3:
                        if (oldAccountNumber.equals(debitCardEntity.getAccountNumber())) {
                            showErrorDialog("原交易账户不能和新交易账户相同，请重新选择！");
                            return;
                        }
                        break;
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable("debitCardEntity", debitCardEntity);
                setFramgentResult(246, bundle);
                pop();
            }
        });
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
    protected String getTitleValue() {
        return "选择账户";
    }

    @Override
    protected ChooseLongshortAccountPresenter initPresenter() {
        return new ChooseLongshortAccountPresenter(this);
    }

    @Override
    public void vFGFilterDebitCardSuccess(VFGFilterDebitCardViewModel viewModel) {
        this.viewModel = viewModel;

        setAdapterOrNotify();

        closeProgressDialog();
    }

    private void setAdapterOrNotify() {
        List<VFGFilterDebitCardViewModel.DebitCardEntity> cardList = viewModel.getCardList();

        // 排序，把“交易账户”排在第1位
        VFGFilterDebitCardViewModel.DebitCardEntity currentDebitCard = null; // 当前交易账户的借记卡
        for (VFGFilterDebitCardViewModel.DebitCardEntity debitCardEntity : cardList) {
            if (currentAccountNumber.equals(debitCardEntity.getAccountNumber())) {
                currentDebitCard = debitCardEntity;
                break;
            }
        }
        cardList.remove(currentDebitCard);
        cardList.add(0, currentDebitCard);

        // 设置适配器或者刷新
        if (mAdapter == null) {
            mAdapter = new FilterDebitCardAdapter(mActivity, cardList);
            mAdapter.setCurrentTradeAccountNumber(currentAccountNumber);
            lv_list.setAdapter(mAdapter);
        } else {
            mAdapter.setList(cardList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void vFGFilterDebitCardFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    @Override
    public void setPresenter(ChooseLongshortAccountContact.Presenter presenter) {

    }
}
