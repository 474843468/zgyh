package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileTransferQueryUnSubmitTrans.PsnMobileTransferQueryUnSubmitTransResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.DateTimePicker;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.SlipMenu.SlipDrawerLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectview.selecttimerange.SelectTimeRange;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.model.PhoneTransferQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.phonetransferquery.presenter.PhoneTransferQueryPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import java.util.ArrayList;
import java.util.List;
import org.threeten.bp.LocalDate;

/**
 * 手机号转账查询
 * <p/>
 * Created by liuweidong on 2016/6/20.
 */
public class PhoneTransferQueryFragment extends BussFragment implements View.OnClickListener, PhoneTransferQueryContract.View {
    // 根视图
    private View rootView;
    // 侧滑
    private SlipDrawerLayout slipDrawerLayout;
    // 上拉刷新
    private PullToRefreshLayout refreshLayout;
    // 查询列表组件
    private TransactionView transactionView;
    // 选择时间范围组件
    private SelectTimeRange selectTimeRange;

    // listView的Head
    private View headView;
    // 左边标题返回按钮
    private ImageView leftIconIv;
    // 标题内容
    private TextView titleValueTv;
    // 右边标题更多按钮
    private ImageView rightIconIv;

    // 筛选父布局
    private LinearLayout selectParentLayout;
    // 筛选文本
    private TextView selectTxt;
    // 筛选图片
    private ImageView selectImg;

    /**
     * 手机号转账查询Presenter
     */
    private PhoneTransferQueryPresenter mPresenter;

    //起始时间
    private LocalDate startLocalDate;
    //结束时间
    private LocalDate endLocalDate;
    // 当前页起始索引
    private int currentIndex = 0;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_transfer_query, null);
        return rootView;
    }

    @Override
    public void initView() {
        slipDrawerLayout = (SlipDrawerLayout) rootView.findViewById(R.id.drawer_layout_transfer_query);
        refreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.refresh_transfer_query);
        transactionView = (TransactionView) rootView.findViewById(R.id.lv_transfer_query);
        selectTimeRange = (SelectTimeRange) rootView.findViewById(R.id.right_drawer_transfer_query);

        // ListView的header
        headView = View.inflate(mContext, R.layout.boc_head_view_no_account, null);
        transactionView.addHeaderView(headView, null, false);
        transactionView.setAdapter();

        // 标题栏
        leftIconIv = (ImageView) headView.findViewById(R.id.leftIconIv);
        titleValueTv = (TextView) headView.findViewById(R.id.titleValueTv);
        rightIconIv = (ImageView) headView.findViewById(R.id.rightIconIv);

        // 筛选按钮
        selectParentLayout = (LinearLayout) headView.findViewById(R.id.layout_parent_select);
        selectTxt = (TextView) headView.findViewById(R.id.txt_select);
        selectImg = (ImageView) headView.findViewById(R.id.img_select);
    }

    @Override
    public void initData() {
        mPresenter = new PhoneTransferQueryPresenter(this);

        // 设置标题栏
        titleValueTv.setText(getResources().getString(R.string.boc_transfer_phone_query_title));
        rightIconIv.setVisibility(View.GONE);

        // 设置默认日期
        startLocalDate = LocalDate.now().plusMonths(-6);
        endLocalDate = LocalDate.now();
        selectTimeRange.setDefaultDate(startLocalDate.format(DateFormatters.dateFormatter1),
                endLocalDate.format(DateFormatters.dateFormatter1));

        // TODO 请求接口

        // 网络请求
        queryMobileTransfer();
//        updateData(test());
    }

    @Override
    public void setListener() {
        // 返回按钮监听
        leftIconIv.setOnClickListener(this);
        // 筛选按钮监听
        selectParentLayout.setOnClickListener(this);

        // 列表的监听
        transactionView.setListener(new TransactionView.ClickListener() {
            @Override
            public void onItemClickListener(int position) {
                Toast.makeText(mContext, "位置" + position, Toast.LENGTH_LONG).show();
                start(new PhoneTransferQueryDetailsFragment());
            }
        });

        // 日期筛选的监听
        selectTimeRange.setListener(new SelectTimeRange.ClickListener() {
            @Override
            public void startClick() {
                judgeStartTimeAndSet();
            }

            @Override
            public void endClick() {
                judgeEndTimeAndSet();
            }

            @Override
            public void cancelClick() {
                slipDrawerLayout.toggle();
            }

            @Override
            public void rightClick() {
                if (judgeChoiceDateRange()) {
                    startLocalDate = LocalDate.parse(selectTimeRange.getTxtStart().getText().toString(), DateFormatters.dateFormatter1);
                    endLocalDate = LocalDate.parse(selectTimeRange.getTxtEnd().getText().toString(), DateFormatters.dateFormatter1);
                    slipDrawerLayout.toggle();
                    // TODO 全局加载

                    // 网络请求
                    queryMobileTransfer();
                }
            }
        });

        // 上拉加载
        refreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

                // TODO 上拉加载
//                pullToRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refreshLayout.loadmoreCompleted(PullToRefreshLayout.NO_MORE_DATA);
//                    }
//                }, 2000);

            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.leftIconIv) {
            titleLeftIconClick();

        } else if (i == R.id.layout_parent_select) {
            slipDrawerLayout.toggle();

        }
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    /**
     * 查询手机号转账记录
     */
    private void queryMobileTransfer() {
        mPresenter.queryMobileTransfer(buildPhoneTransferQueryViewModel());
    }

    /**
     * 封装页面数据
     *
     * @return
     */
    private PhoneTransferQueryViewModel buildPhoneTransferQueryViewModel() {
        PhoneTransferQueryViewModel phoneTransferQueryViewModel = new PhoneTransferQueryViewModel();
        phoneTransferQueryViewModel.setServiceId("PB035");// 服务码
        phoneTransferQueryViewModel.setBeiginDate(startLocalDate);// 开始日期
        phoneTransferQueryViewModel.setEndDate(endLocalDate);// 结束日期
        phoneTransferQueryViewModel.setStatus("");// 状态
        phoneTransferQueryViewModel.setCurrentIndex(currentIndex);// 当前页起始索引
        phoneTransferQueryViewModel.setPageSize(50);// 页面显示记录条数
        return phoneTransferQueryViewModel;
    }

    /**
     * 测试用
     *
     * @return
     */
    private PsnMobileTransferQueryUnSubmitTransResult test(){
        PsnMobileTransferQueryUnSubmitTransResult result = new PsnMobileTransferQueryUnSubmitTransResult();
        List<PsnMobileTransferQueryUnSubmitTransResult.ListBean> listBeen = new ArrayList<PsnMobileTransferQueryUnSubmitTransResult.ListBean>();
        result.setRecordNumber(10);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean.setHandleDate(LocalDate.now());
        bean.setPayeeName("张三");
        bean.setPayeeMobile("12345665432");
        bean.setAmount("456789023445");
        listBeen.add(bean);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean1 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean1.setHandleDate(LocalDate.now());
        bean1.setPayeeName("李四");
        bean1.setPayeeMobile("72347897652");
        bean1.setAmount("9896789023445");
        listBeen.add(bean1);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean2 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean2.setHandleDate(LocalDate.now());
        bean2.setPayeeName("李四");
        bean2.setPayeeMobile("72347897652");
        bean2.setAmount("9896789023445");
        listBeen.add(bean2);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean3 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean3.setHandleDate(LocalDate.now().plusDays(1));
        bean3.setPayeeName("王五");
        bean3.setPayeeMobile("72347897652");
        bean3.setAmount("9896789023445");
        listBeen.add(bean3);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean4 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean4.setHandleDate(LocalDate.now().plusWeeks(2));
        bean4.setPayeeName("刘备");
        bean4.setPayeeMobile("72347897652");
        bean4.setAmount("9896789023445");
        listBeen.add(bean4);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean5 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean5.setHandleDate(LocalDate.now());
        bean5.setPayeeName("关羽");
        bean5.setPayeeMobile("72347897652");
        bean5.setAmount("9896789023445");
        listBeen.add(bean5);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean6 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean6.setHandleDate(LocalDate.now());
        bean6.setPayeeName("张飞");
        bean6.setPayeeMobile("72347897652");
        bean6.setAmount("9896789023445");
        listBeen.add(bean6);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean7 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean7.setHandleDate(LocalDate.now().plusYears(1));
        bean7.setPayeeName("张飞");
        bean7.setPayeeMobile("72347897652");
        bean7.setAmount("9896789023445");
        listBeen.add(bean7);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean8 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean8.setHandleDate(LocalDate.now());
        bean8.setPayeeName("张飞");
        bean8.setPayeeMobile("72347897652");
        bean8.setAmount("9896789023445");
        listBeen.add(bean8);

        PsnMobileTransferQueryUnSubmitTransResult.ListBean bean9 = new PsnMobileTransferQueryUnSubmitTransResult.ListBean();
        bean9.setHandleDate(LocalDate.now());
        bean9.setPayeeName("张飞");
        bean9.setPayeeMobile("72347897652");
        bean9.setAmount("9896789023445");
        listBeen.add(bean9);

        result.setList(listBeen);

        return result;
    }

    /**
     * 更新UI数据
     *
     * @param psnMobileTransferQueryUnSubmitTransResult
     */
    private void updateData(PsnMobileTransferQueryUnSubmitTransResult psnMobileTransferQueryUnSubmitTransResult) {
        int sum = psnMobileTransferQueryUnSubmitTransResult.getRecordNumber();
        List<PsnMobileTransferQueryUnSubmitTransResult.ListBean> listBeen = psnMobileTransferQueryUnSubmitTransResult.getList();

        List<TransactionBean> list = new ArrayList<TransactionBean>();
        for (int i = 0; i < sum; i++) {
            TransactionBean transactionBean = new TransactionBean();
            transactionBean.setTitleID(4);
            transactionBean.setTime(listBeen.get(i).getHandleDate());
            transactionBean.setContentLeftAbove(listBeen.get(i).getPayeeName());
            transactionBean.setContentLeftBelow(NumberUtils.formatMobileNumber(listBeen.get(i).getPayeeMobile()));
            transactionBean.setContentRightBelow(MoneyUtils.transMoneyFormat(listBeen.get(i).getAmount(), "001"));
            list.add(transactionBean);
        }
        transactionView.setData(list);
    }

    /**
     * 比较两个日期间隔是否超过mouthRange个月，startDate < endDate
     * true为超过mouthRange个月，false为没有超过。
     *
     * @return
     */
    public static boolean isCompareDateRange(LocalDate startDate, LocalDate endDate, int mouthRange) {
        if (startDate == null || endDate == null) {
            return false;
        } else {
            LocalDate newDate = endDate.plusMonths(-mouthRange);
            return !newDate.isBefore(startDate.plusDays(1));
        }
    }

    /**
     * 起始日期的选择
     */
    private void judgeStartTimeAndSet() {
        DateTimePicker.showDatePick(mContext, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (LocalDate.now().isBefore(choiceDate)) {
                    Toast.makeText(mContext, getResources().getString(R.string.boc_account_transdetail_start_before), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isCompareDateRange(choiceDate, LocalDate.now(), 24)) {
//                    Toast.makeText(mContext, getResources().getString(R.string.boc_account_transdetail_start_year), Toast.LENGTH_SHORT).show();
                    return;
                }
                selectTimeRange.setStartDate(strChoiceTime);
            }
        });
    }

    /**
     * 结束日期的选择
     */
    private void judgeEndTimeAndSet() {
        DateTimePicker.showDatePick(mContext, DateFormatters.dateFormatter1, new DateTimePicker.DatePickCallBack() {
            @Override
            public void onChoiceDateSet(String strChoiceTime, LocalDate choiceDate) {
                if (LocalDate.now().isBefore(choiceDate)) {
                    Toast.makeText(mContext, getResources().getString(R.string.boc_account_transdetail_end_before), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isCompareDateRange(choiceDate, LocalDate.now(), 24)) {
//                    Toast.makeText(mContext, getResources().getString(R.string.boc_account_transdetail_end_year), Toast.LENGTH_SHORT).show();
                    return;
                }
                selectTimeRange.setEndDate(strChoiceTime);
            }
        });
    }

    /**
     * 判断选择时间范围是否合法
     *
     * @return
     */
    private boolean judgeChoiceDateRange() {
        if (startLocalDate == null || endLocalDate == null) {
            Toast.makeText(mContext, getResources().getString(R.string.boc_account_transdetail_date_range_null), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (endLocalDate.isBefore(startLocalDate)) {
            Toast.makeText(mContext, getResources().getString(R.string.boc_account_transdetail_start_end), Toast.LENGTH_SHORT).show();
            return false;
        }
        if (isCompareDateRange(startLocalDate, endLocalDate, 6)) {
//            Toast.makeText(mContext, getResources().getString(R.string.boc_account_transdetail_date_range_six), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 根据是否有数据显示筛选按钮的颜色
     * 若查询数据为空，则查询按钮需要变成红色
     *
     * @param haveDate
     */
    private void haveDataSelectStatust(boolean haveDate) {
        if (haveDate) {
            selectTxt.setTextColor(getResources().getColor(R.color.boc_text_color_common_gray));
            selectImg.setImageResource(R.drawable.boc_select_gray);
        } else {
            selectTxt.setTextColor(getResources().getColor(R.color.boc_text_color_red));
            selectImg.setImageResource(R.drawable.boc_select_red);
        }
    }

    @Override
    public void queryMobileTransferSuccess(PsnMobileTransferQueryUnSubmitTransResult psnMobileTransferQueryUnSubmitTransResult) {
        updateData(psnMobileTransferQueryUnSubmitTransResult);
    }

    @Override
    public void queryMobileTransferFail(BiiResultErrorException biiResultErrorException) {
        Toast.makeText(mContext, "查询手机号转账记录失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(PhoneTransferQueryContract.Presenter presenter) {

    }
}
