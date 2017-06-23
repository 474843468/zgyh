package com.boc.bocsoft.mobile.bocmobile.base.widget.selectmanagementview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyang on 2016/7/6.
 */
public class SelectMangerFragment extends BussFragment{

    private View mRootView;

    private ListView mListView;
    /**
     * 账户列表的adapter
     */
//    private AccoutSelectorViewAdapter selectorViewAdapter;

    /**
     * 页面跳转数据传递
     */
    public static final String ACCOUNT_TYPE_LIST = "AccountTypeList";
    public static final String ACCOUNT_SELECT = "AccountBean";
    public static final int REQUEST_CODE_SELECT_ACCOUNT = 1;
    public static final int RESULT_CODE_SELECT_ACCOUNT = 100;



    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_transdetail_selectaccount, null);
        return mRootView;
    }

    @Override
    public void beforeInitView() {

    }

    @Override
    public void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.lv_transdetail_selectaccount);
//        selectorViewAdapter = new AccoutSelectorViewAdapter(mContext);
//        mListView.setAdapter(selectorViewAdapter);
    }

    public ListView getmListView() {
        return mListView;
    }

//    public AccoutSelectorViewAdapter getSelectorViewAdapter() {
//        return selectorViewAdapter;
//    }

    @Override
    protected String getTitleValue() {
        return "收款人";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void initData() {

        ArrayList<String> stringList = getArguments().getStringArrayList(ACCOUNT_TYPE_LIST);
        List<AccountBean> accountBeanList = ApplicationContext.getInstance().getChinaBankAccountList(stringList);

//        selectorViewAdapter.setData(accountBeanList, new SelectorViewAdapter.ItemClickListener() {
//            @Override
//            public void onItemClick(int pos, AccountBean item) {
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(ACCOUNT_SELECT, item);
//                setFramgentResult(RESULT_CODE_ADD_PAYER_SUCCESS, bundle);
//                pop();
//            }
//        });
    }

    @Override
    public void setListener() {
        super.setListener();
    }


}
