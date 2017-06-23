package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttype.SelectTypeView;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.adapter.OverviewTermlyAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.AccountInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model.TermlyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.presenter.OverviewPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleCode;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.widget.listview.ListAdapterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangyang
 *         16/8/12 17:01
 *         定期详情界面
 */
@SuppressLint("ValidFragment")
public class RegularFragment extends BaseOverviewFragment<OverviewPresenter> implements OverviewContract.RegularView, View.OnClickListener, ListAdapterHelper.OnClickChildViewInItemItf<TermlyViewModel>, SelectTypeView.SelectListener {

    private final int SELECT_TYPE_COLUMNS = 3;

    private PullableListView lvDetail;
    private OverviewTermlyAdapter adapter;
    /**
     * 侧滑菜单
     */
    private SlipDrawerLayout drawerDetail;
    /**
     * 侧滑页面
     */
    private SelectTypeView stvType;
    /**
     * 数据界面,无数据界面
     */
    private ViewGroup llData, rlNoData;
    /**
     * 标题左侧,右侧按钮
     */
    private ImageView ivLeft, ivRight;
    /**
     * 标题
     */
    private TextView tvTitle, tvNoData;
    /**
     * 存定期按钮
     */
    private Button btnRegular;

    private AccountBean accountBean;

    private AccountInfoBean accountInfoBean;

    private List<TermlyViewModel> models;

    @Override
    protected OverviewPresenter initPresenter() {
        return new OverviewPresenter(this);
    }

    public RegularFragment() {
    }

    public RegularFragment(AccountBean accountBean) {
        this.accountBean = accountBean;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_overview_regular, null);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initView() {
        ivLeft = (ImageView) mContentView.findViewById(R.id.leftIconIv);
        ivRight = (ImageView) mContentView.findViewById(R.id.rightIconIv);
        tvTitle = (TextView) mContentView.findViewById(R.id.titleValueTv);
        tvNoData = (TextView) mContentView.findViewById(R.id.tv_no_data);
        lvDetail = (PullableListView) mContentView.findViewById(R.id.lv_detail);
        drawerDetail = (SlipDrawerLayout) mContentView.findViewById(R.id.drawer_detail);
        stvType = (SelectTypeView) mContentView.findViewById(R.id.stv_type);
        llData = (ViewGroup) mContentView.findViewById(R.id.ll_data);
        rlNoData = (ViewGroup) mContentView.findViewById(R.id.rl_no_data);
        btnRegular = (Button) mContentView.findViewById(R.id.btn_regular);
        stvType.setSelectView(true, SELECT_TYPE_COLUMNS);
        stvType.setSelectSize(true, mActivity.getResources().getDimensionPixelOffset(R.dimen.boc_regular_drawer_cell_width), mActivity.getResources().getDimensionPixelOffset(R.dimen.boc_space_between_70px));
        stvType.setDrawerLayout(drawerDetail);
    }

    @Override
    public void setListener() {
        btnRegular.setOnClickListener(this);
        mContentView.findViewById(R.id.ll_type).setOnClickListener(this);
        stvType.setClickListener(this);
        adapter.setOnClickChildViewInItemItf(this);
        ivLeft.setOnClickListener(this);
        ivRight.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //设置标题,由于使用公共组件,所以需要代码设置字体大小
        tvTitle.getPaint().setTextSize(getResources().getDimensionPixelSize(R.dimen.boc_text_size_big));
        tvTitle.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvTitle.setText(NumberUtils.formatCardNumber(accountBean.getAccountNumber()));

        adapter = new OverviewTermlyAdapter(mContext);
        lvDetail.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (isCurrentFragment()) {
            /**获取最近交易明细*/
            showLoadingDialog();
            getPresenter().queryRegularAccountDetail(accountBean);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_type) {
            ((TextView) mContentView.findViewById(R.id.tv_type)).setTextColor(getResources().getColor(R.color.boc_text_color_red));
            ((ImageView) mContentView.findViewById(R.id.iv_type)).setImageResource(R.drawable.boc_select_red);
            drawerDetail.toggle();
        } else if (v.getId() == R.id.leftIconIv) {
            super.titleLeftIconClick();
        } else if (v.getId() == R.id.rightIconIv) {
            start(new MoreAccountFragment(accountBean, accountInfoBean));
        } else if (v.getId() == R.id.btn_regular) {
            ModuleActivityDispatcher.dispatch(mActivity, ModuleCode.MODEUL_DEPTSTORAGE);
        }
    }

    @Override
    public void onClickChildViewInItem(int position, TermlyViewModel item, View childView) {
        start(new RegularTransDetailFragment(accountBean, item));
    }

    @Override
    public void onClick(List<String> selectIds) {
        String type;
        String status;

        //定一本外的其他类型,筛选条件没有类型,赋默认值全部
        if (selectIds.size() == 1) {
            type = SelectTypeView.DEFAULT_ID;
            status = selectIds.get(0);
        } else {
            type = selectIds.get(0);
            status = selectIds.get(1);
        }

        //类型与状态为全部,不做筛选
        if (type.equals(SelectTypeView.DEFAULT_ID) && status.equals(SelectTypeView.DEFAULT_ID)) {
            showSiftPanel(View.GONE, View.VISIBLE);
            adapter.setDatas(models);
            return;
        }

        //筛选数据
        siftTermlyViewModel(type, status);
    }

    @Override
    public void resetClick() {

    }

    private void siftTermlyViewModel(String type, String status) {
        List<TermlyViewModel> datas = new ArrayList<>();
        for (TermlyViewModel model : models) {
            if (!SelectTypeView.DEFAULT_ID.equals(type) && !model.getType().equals(type))
                continue;

            if (!SelectTypeView.DEFAULT_ID.equals(status) && !model.getStatus().equals(status))
                continue;

            datas.add(model);
        }

        if (!datas.isEmpty()) {
            showSiftPanel(View.GONE, View.VISIBLE);
            adapter.setDatas(datas);
            return;
        }
        showSiftPanel(View.VISIBLE, View.GONE);
    }

    private void showSiftPanel(int visibility1, int visibility2) {
        tvNoData.setVisibility(visibility1);
        lvDetail.setVisibility(visibility2);
    }

    @Override
    public void queryAccountDetail(List<TermlyViewModel> models, AccountInfoBean accountInfoBean) {
        closeProgressDialog();
        this.models = models;
        this.accountInfoBean = accountInfoBean;
        showDataPanel();
    }

    @Override
    public void queryAccountDetailFail() {
        closeProgressDialog();
        llData.setVisibility(View.GONE);
        rlNoData.setVisibility(View.GONE);
    }

    private void showDataPanel() {
        int showDataPanel = View.GONE;
        int showNoDataPanel = View.VISIBLE;

        if (models != null && !models.isEmpty()) {
            showDataPanel = View.VISIBLE;
            showNoDataPanel = View.GONE;

            if (stvType.isEmpty()) {
                stvType.setData(AccountTypeUtil.getAllSelectType(accountBean.getAccountType()));
                siftTermlyViewModel(SelectTypeView.DEFAULT_ID, AccountTypeUtil.ACCOUNT_STATUS_NORMAL);
            } else
                onClick(stvType.getSelectId());
        }

        llData.setVisibility(showDataPanel);
        rlNoData.setVisibility(showNoDataPanel);
    }
}
