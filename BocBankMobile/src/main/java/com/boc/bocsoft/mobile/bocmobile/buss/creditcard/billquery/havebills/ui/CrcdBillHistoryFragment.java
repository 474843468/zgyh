package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.shareinfoview.ListViewForScrollView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.presenter.CrcdBillYPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.chartview.LineChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史账单——查询近一年账单数据（一次查询半年数据），数据存储在本地数据库。
 * Created by liuweidong on 2016/12/14.
 */
public class CrcdBillHistoryFragment extends MvpBussFragment<CrcdBillYPresenter> implements CrcdBillYContract.CrcdBillHistoryView {

    public static final String CUR_ACCOUNT = "cur_account";

    private View rootView;
    private RadioButton rbCurrency1;
    private RadioButton rbCurrency2;
    private LineChartView lineChartView;// 折线图
    private ListViewForScrollView listView;
    private TextView txtMore;// 更多

    private AccountBean curAccount;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_crcd_bill_history, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        rbCurrency1 = (RadioButton) rootView.findViewById(R.id.rb_currency_1);
        rbCurrency2 = (RadioButton) rootView.findViewById(R.id.rb_currency_2);
        lineChartView = (LineChartView) rootView.findViewById(R.id.line_chart_view);
        listView = (ListViewForScrollView) rootView.findViewById(R.id.listview);
        txtMore = (TextView) rootView.findViewById(R.id.txt_more);
    }

    @Override
    public void initData() {
        super.initData();
        curAccount = getArguments().getParcelable(CUR_ACCOUNT);
        List<String> dateList = new ArrayList<>();
        dateList.add("2032/09");
        dateList.add("2032/08");
        dateList.add("2032/07");
        dateList.add("2032/06");
        dateList.add("2032/05");
        dateList.add("2032/04");
        getPresenter().queryCrcdHistoryBill(dateList, curAccount.getAccountId());
    }

    @Override
    protected CrcdBillYPresenter initPresenter() {
        return new CrcdBillYPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_crcd_bill_history_title);
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
    public void queryCrcdHistoryBillSuccess() {

    }

    class BillAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
