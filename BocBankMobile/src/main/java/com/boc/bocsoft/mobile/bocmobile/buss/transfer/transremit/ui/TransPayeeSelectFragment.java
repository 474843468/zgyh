package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnCrcdChargeOnRMBAccountQuery.PsnCrcdChargeOnRMBAccountQueryResult;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BaseMobileActivity;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.BankLogoUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ErrorDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.TitleAndBtnDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.index.QuickIndexBar;
import com.boc.bocsoft.mobile.bocmobile.base.widget.index.QuickIndexBar2;
import com.boc.bocsoft.mobile.bocmobile.base.widget.stickylistheaders.StickyListHeadersListView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter.ManagePayeeQueryPayeeListAdapter2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.AddPayeeFragment2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui.PayeeDetailFragment2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.AccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.CrcdQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model.PayerAndPayeeInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.presenter.TransPayeeAccSelectPagePresenter;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 收款人账户
 * Created by wy on 2016/8/30
 */
public class TransPayeeSelectFragment extends MvpBussFragment<TransPayeeAccSelectPagePresenter> implements TransContract.TransViewPayeeAccSelectPage, View.OnLayoutChangeListener {
    private static final int REQUEST_CODE_ADD_PAYEE = 101; // 请求码：添加付款人
    public static final int RESULT_CODE_SELECT_ACCOUNT_SUCCESS = 102; // 响应码，选择付款人成功
    public static final String DO_WHAT = "dowhat"; // 来这个页面要干啥
    public static final String ACTION_JUST_CHOOSE_ACCOUNT = "JustChooseAccount"; // 操作，仅仅选择帐号
    public static final String KEY_PAYEE_ENTITY = "payeeEntity"; // key：payeeEntity

    private boolean isSearchNoDataState = false; // 是否是搜索无数据状态
    private boolean isKeyBoardUp = false; // 键盘是否弹起
    private View mRootView;
    private QuickIndexBar quickIndexBar;
    private QuickIndexBar2 quickIndexBar2;
    private StickyListHeadersListView listview;
    private TextView currentWord;
    //    private PayeeEntityAdapter mAdapter;
    private EditText et_search_content;
    private TextView tv_payee_name;
    private TextView tv_account_num;
    private TextView tv_bank_name;
    private TextView tv_no_data;

    private ImageView vrightIconIv; // 标题栏右侧的按钮

    int countPinyinZ = 0;

    private ManagePayeeQueryPayeeListAdapter2 mAdapter;
    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> mPayeeEntityList = new ArrayList<>(); // 收款人列表
    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> tmpPayeeEntityList = new ArrayList<>(); // 收款人列表
//    private TransPayeeAccSelectPagePresenter payeeSelectPresenter;
    private PsnTransPayeeListqueryForDimViewModel.PayeeEntity mCurrentClickPayeeEntity; // 当前点击的条目对应的PayeeEntity
    private PsnTransPayeeListqueryForDimViewModel.PayeeEntity mNewPayeeEntity; // 新收款人
    private View mHeaderView;
    private RelativeLayout mRlNewPayee;
    private boolean isNewPayeeItemClick = false; // 是否点击了“新收款人条目”，默认为false
    private String mDowhat;
    private ImageView iv_bank_logo_new;
    private ErrorDialog errorMessage;
//    private TitleAndBtnDialog warningDialog;
    /**
     * 王园添加 2016-8-4
     */
    //    private boolean isLinkedAccountOk;//关联账户数据完毕
    boolean isPayeeQureyDone = false;//查询收款人数据成功
    private List<AccountBean> linkedAccountList;
    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> linkedPayeeEntityList;
    private TextView tv_search_no_data;
    private int screenHeight;
    private int keyHeight;
    private ImageView iv_delete_text;
    //    private TextView letter_me;

    //到此结束
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_payee_manage2, null);
        // 获取屏幕高度
        screenHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
        // 阀值设置为屏幕高度的1/3（监听键盘弹起事件）
        keyHeight = screenHeight / 3;
        return mRootView;
    }

    @Override
    public void initView() {
        quickIndexBar = (QuickIndexBar) mRootView.findViewById(R.id.quickIndexBar);
        quickIndexBar2 = (QuickIndexBar2) mRootView.findViewById(R.id.quickIndexBar2);
        listview = (StickyListHeadersListView) mRootView.findViewById(R.id.listview);
        currentWord = (TextView) mRootView.findViewById(R.id.currentWord);
        tv_no_data = (TextView) mRootView.findViewById(R.id.tv_no_data); // 没有数据时的提示信息
        // 没有数据时的提示信息
        tv_search_no_data = (TextView) mRootView.findViewById(R.id.tv_search_no_data);
//        warningDialog=new TitleAndBtnDialog(mContext);
        errorMessage=new ErrorDialog(mContext);
    }
    private PayerAndPayeeInfoModel accAlreadyInfo;
    @Override
    public void initData() {
//        setAdapterAndNotify();
        mHeaderView = View.inflate(mActivity, R.layout.list_payee_header, null);
        mRlNewPayee = (RelativeLayout) mHeaderView.findViewById(R.id.rl_new_payee);
//        view_search_line = (View) mHeaderView.findViewById(R.id.view_search_line);
        iv_delete_text = (ImageView) mRootView.findViewById(R.id.iv_delete_text);
        mRlNewPayee.setVisibility(View.GONE);
        et_search_content = (EditText) mRootView.findViewById(R.id.et_search_content);
        tv_payee_name = (TextView) mHeaderView.findViewById(R.id.tv_payee_name);
        tv_account_num = (TextView) mHeaderView.findViewById(R.id.tv_account_num);
        tv_bank_name = (TextView) mHeaderView.findViewById(R.id.tv_bank_name);
        iv_bank_logo_new = (ImageView) mHeaderView.findViewById(R.id.iv_bank_logo_new);
        listview.addHeaderView(mHeaderView);

//        payeeSelectPresenter = new TransPayeeAccSelectPagePresenter(this);
        // 接收上一个页面传来的参数
        //wangyuan 添加 2016-8-5
        linkedPayeeEntityList = new ArrayList<>();
        mDowhat = getArguments().getString(DO_WHAT); // 上一个页面跳转到本页面想干啥
        if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
            // letter_me.setVisibility(View.VISIBLE);
            linkedAccountList = getArguments().getParcelableArrayList(TransRemitBlankFragment.LINKED_ACCOUNT_TYPE);
            tmpPayeeEntityList=getArguments().getParcelableArrayList(TransRemitBlankFragment.PAYEE_ACCOUNT_TYPE);
            mPayeeEntityList.clear();
            mPayeeEntityList.addAll(tmpPayeeEntityList);
            accAlreadyInfo=getArguments().getParcelable(TransRemitBlankFragment.ACCOUNT_EXIST);
            if (null != linkedAccountList && linkedAccountList.size() > 0) {
                for (AccountBean onebean : linkedAccountList) {
                    PsnTransPayeeListqueryForDimViewModel.PayeeEntity oneLinkedPayeeEntity =
                            new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();
                    BeanConvertor.toBean(onebean, oneLinkedPayeeEntity);
                    oneLinkedPayeeEntity.setPinyin("我");
                    oneLinkedPayeeEntity.setLinked(true);
                    oneLinkedPayeeEntity.setNickNamepinyin("");
                    oneLinkedPayeeEntity.setType(onebean.getAccountType());
                    oneLinkedPayeeEntity.setPayeetId(0);
                    linkedPayeeEntityList.add(oneLinkedPayeeEntity);

                }
                setAdapterAndNotify();
                quickIndexBar2.setVisibility(View.VISIBLE);
                quickIndexBar.setVisibility(View.GONE);
            }
        } else {
            quickIndexBar2.setVisibility(View.GONE);
            quickIndexBar.setVisibility(View.VISIBLE);
        }
//        isCrcdError=false;
    }

    int y = 0;
    int dy = 0;
    boolean isNeedScroll = false;

    Bundle bundle = new Bundle();//传输数据
    public void queryAccoutDetailInfo(AccountBean accountBean) {
        showLoadingDialog();
        //103 104 107 信用卡
        if ("103".equals(accountBean.getAccountType()) || "104".equals(accountBean.getAccountType())
                || "107".equals(accountBean.getAccountType())) {
            getPresenter().queryCrcdAccountDetail(accountBean.getAccountId(), "");
        } else {
            getPresenter().queryAccountBalance(accountBean.getAccountId());
        }

    }

//    private boolean isCrcdError;
    private List<String> currencyList =new ArrayList();
    @Override
    public void setListener() {
        iv_delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search_content.setText("");
            }
        });
        // 设置“新付款人”的点击事件
//        mRlNewPayee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isNewPayeeItemClick = true;
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(TransPayeeSelectFragment.KEY_PAYEE_ENTITY, mNewPayeeEntity);
//                PayeeDetailFragment2 payeeDetailFragment = new PayeeDetailFragment2();
//                payeeDetailFragment.setArguments(bundle);
//                startForResult(payeeDetailFragment, 101); // 启动付款人详情界面
//            }
//        });
//        warningDialog.setDialogBtnClickListener(new TitleAndBtnDialog.DialogBtnClickCallBack() {
//            @Override
//            public void onLeftBtnClick(View view) {
//                warningDialog.dismiss();
//            }
//
//            @Override
//            public void onRightBtnClick(View view) {
//
//            }
//        });
//        BaseMobileActivity.ErrorDialogClickCallBack errorDialogClickCallBack=new BaseMobileActivity.ErrorDialogClickCallBack() {
//            @Override
//            public void onEnterBtnClick() {
//                if (isCrcdError){
//                    setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
//                    pop();
//                }
//            }
//        };

//        errorMessage.setOnBottomViewClickListener(new ErrorDialog.OnBottomViewClickListener() {
//            @Override
//            public void onBottomViewClick() {
//
//            }
//        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentClickPayeeEntity = mAdapter.getItem(position - 1);
                bundle.putParcelable(KEY_PAYEE_ENTITY, mCurrentClickPayeeEntity);
                if (mDowhat != null && mDowhat.equals(ACTION_JUST_CHOOSE_ACCOUNT)) { // 处理如果是仅仅选择账户的情况
                    //wangyuan 转账汇款功能
                    hideSoftInput(); // 记得如果键盘弹出来，先把键盘隐藏掉
                    if (mCurrentClickPayeeEntity.isLinked()){
                        if (null!=accAlreadyInfo&&mCurrentClickPayeeEntity.getAccountNumber().equals(accAlreadyInfo.getPayerAccoutNum())){
                            showErrorDialog("付款账户和收款账户相同，请修改");
                            return;
                        }
                        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(mCurrentClickPayeeEntity.getType())||ApplicationConst.ACC_TYPE_GRE.equals(mCurrentClickPayeeEntity.getType())
                                ||ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(mCurrentClickPayeeEntity.getType())){
                            //只有信用卡需要校验币种
                            showLoadingDialog();
                            queryAccoutDetailInfo(linkedAccountList.get(position-1));
                        }else{
                            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
                            pop();
                        }
                    }else{
                        if (null!=accAlreadyInfo&&!StringUtils.isEmptyOrNull(accAlreadyInfo.getTransCurrency())&&!"001".equals(accAlreadyInfo.getTransCurrency())){
                            showErrorDialog("该账户不支持外币转账，请修改");
                            return;
                        }
                        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
                        pop();
                    }
                } else { // 处理本页面显示收款人详情的逻辑
                    hideSoftInput(); // 记得如果键盘弹出来，先把键盘隐藏掉
                    PayeeDetailFragment2 payeeDetailFragment = new PayeeDetailFragment2();
                    payeeDetailFragment.setArguments(bundle);
                    startForResult(payeeDetailFragment, 101); // 启动付款人详情界面
                }
            }
        });

        quickIndexBar.setOnTouchLetterListener(new QuickIndexBar.OnTouchLetterListener() {

            @Override
            public void onTouchLetter(String word) {

                if ("搜索".equals(word)) { // 如果滑动到“搜索”，那么把ListView的位置置为0
                    listview.setSelection(0);
                    currentWord.setVisibility(View.GONE);
                    return;
                }

                showWord(word);

                if (TextUtils.isEmpty(et_search_content.getText().toString().trim())) { // 如果search_content为空，mPayeeEntityList；如果不为空，mAdapter.getPayeeEntityFilterList()
                    if (mPayeeEntityList == null) {
                        return;
                    }
                    // 遍历所有的bean，找到首字母为触摸字母的条目，然后将其设置到屏幕顶端
                    for (int i = 0; i < mPayeeEntityList.size(); i++) {
                        String currentFirstWord = mPayeeEntityList.get(i).getPinyin().charAt(0) + "";

                        if ("#".equals(word)) { // 如果滑动到“搜索”，那么把ListView的位置置为0
                            if (currentFirstWord.equals("z")) {
                                // 将第一个首字母为word设置到屏幕顶端
                                listview.setSelection(i + 1);
                                break;
                            }
                        }

                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            listview.setSelection(i + 1);
                            break;
                        }

                    }
                } else {
                    if (mAdapter.getPayeeEntityFilterList() == null) {
                        return;
                    }

                    // 遍历所有的bean，找到首字母为触摸字母的条目，然后将其设置到屏幕顶端
                    for (int i = 0; i < mAdapter.getPayeeEntityFilterList().size(); i++) {
                        String currentFirstWord = mAdapter.getPayeeEntityFilterList().get(i).getPinyin().charAt(0) + "";

                        if ("#".equals(word)) { // 如果滑动到“搜索”，那么把ListView的位置置为0
                            if (currentFirstWord.equals("z")) {
                                // 将第一个首字母为word设置到屏幕顶端
                                listview.setSelection(i + 1);
                                break;
                            }
                        }
                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            listview.setSelection(i + 1);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onTouchUp() {
                currentWord.setVisibility(View.GONE);
            }
        });
        quickIndexBar2.setOnTouchLetterListener(new QuickIndexBar2.OnTouchLetterListener() {

            @Override
            public void onTouchLetter(String word) {
                if ("搜索".equals(word)) { // 如果滑动到“搜索”，那么把ListView的位置置为0
                    currentWord.setVisibility(View.GONE);
                    listview.setSelection(0);
                    return;
                }

                showWord(word);

                if ("我".equals(word)) {
                    listview.setSelection(1);
                    return;
                }

                if (TextUtils.isEmpty(et_search_content.getText().toString().trim())) { // 如果search_content为空，mPayeeEntityList；如果不为空，mAdapter.getPayeeEntityFilterList()
                    if (mPayeeEntityList == null) {
                        return;
                    }

                    // 遍历所有的bean，找到首字母为触摸字母的条目，然后将其设置到屏幕顶端
                    for (int i = 0; i < mPayeeEntityList.size(); i++) {
                        String currentFirstWord = mPayeeEntityList.get(i).getPinyin().charAt(0) + "";

                        if ("#".equals(word)) { // 如果滑动到“搜索”，那么把ListView的位置置为0
                            if (currentFirstWord.equals("z")) {
                                // 将第一个首字母为word设置到屏幕顶端
                                listview.setSelection(i + 1);
                                break;
                            }
                        }

                        if (!mPayeeEntityList.get(i).isLinked()) {
                            if (currentFirstWord.equals(word)) {
                                // 将第一个首字母为word设置到屏幕顶端
                                listview.setSelection(i + 1);
                                break;
                            }
                        }
                    }
                } else {
                    if (mAdapter.getPayeeEntityFilterList() == null) {
                        return;
                    }

                    // 遍历所有的bean，找到首字母为触摸字母的条目，然后将其设置到屏幕顶端
                    for (int i = 0; i < mAdapter.getPayeeEntityFilterList().size(); i++) {
                        String currentFirstWord = mPayeeEntityList.get(i).getPinyin().charAt(0) + "";

                        if ("#".equals(word)) { // 如果滑动到“搜索”，那么把ListView的位置置为0
                            if (currentFirstWord.equals("z")) {
                                // 将第一个首字母为word设置到屏幕顶端
                                listview.setSelection(i + 1);
                                break;
                            }
                        }

                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            listview.setSelection(i + 1);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onTouchUp() {
                currentWord.setVisibility(View.GONE);
            }
        });

        et_search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();
                mAdapter.setFilterWord(str);

                if (!TextUtils.isEmpty(str)) {
                    iv_delete_text.setClickable(true);
                    iv_delete_text.setVisibility(View.VISIBLE);

                    if (mAdapter.getCount() == 0) {
                        isSearchNoDataState = true;
                        quickIndexBar.setVisibility(View.GONE);
                        quickIndexBar2.setVisibility(View.GONE);
                        tv_search_no_data.setVisibility(View.VISIBLE);
                    } else {
                        isSearchNoDataState = false;
                        if(!isKeyBoardUp) {
                            if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                                quickIndexBar2.setVisibility(View.VISIBLE);
                                quickIndexBar.setVisibility(View.GONE);
                            } else {
                                quickIndexBar2.setVisibility(View.GONE);
                                quickIndexBar.setVisibility(View.VISIBLE);
                            }
                        }

                        tv_search_no_data.setVisibility(View.GONE);
                    }
                } else {
                    iv_delete_text.setClickable(false);
                    iv_delete_text.setVisibility(View.INVISIBLE);
                    isSearchNoDataState = false;
                    if(!isKeyBoardUp) {
                        if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                            quickIndexBar2.setVisibility(View.VISIBLE);
                            quickIndexBar.setVisibility(View.GONE);
                        } else {
                            quickIndexBar2.setVisibility(View.GONE);
                            quickIndexBar.setVisibility(View.VISIBLE);
                        }
                    }

                    tv_search_no_data.setVisibility(View.GONE);
                }
            }
        });

        mRootView.addOnLayoutChangeListener(this);
    }



    /**
     * 默认标题栏view
     * 子类可以重写此方法，改变标题栏样式
     *
     * @return
     */
    protected View getTitleBarView() {
        View view = super.getTitleBarView();

        vrightIconIv = (ImageView) view.findViewById(R.id.rightIconIv);
        TextView rightTextButton = (TextView) view.findViewById(R.id.rightTextButton);
        rightTextButton.setVisibility(View.GONE);
        vrightIconIv.setImageResource(R.drawable.btn_left_top_add);
        int padding = (int) mActivity.getResources().getDimension(R.dimen.boc_space_between_30px);
        vrightIconIv.setPadding(0, padding, 0, padding);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vrightIconIv.getLayoutParams();
        layoutParams.rightMargin = 6;
        vrightIconIv.setLayoutParams(layoutParams);

        vrightIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开添加收款页面
                AddPayeeFragment2 toFragment = new AddPayeeFragment2();
                hideSoftInput();
                startForResult(toFragment, REQUEST_CODE_ADD_PAYEE);
            }
        });

        return view;
    }

    @Override
    protected boolean isDisplayRightIcon() {
            return false;
    }

    @Override
    public boolean onBack() {
        hideSoftInput();
        return super.onBack();
    }

    @Override
    public boolean onBackPress() {
        return super.onBackPress();
    }

    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (resultCode == PayeeDetailFragment2.RESULT_CODE_MODIFY_SUCCESS) { // 修改成功
            // 此处要判断修改的是“新收款人”，还是列表中的
            PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity = data.getParcelable("payeeEntity");
            mCurrentClickPayeeEntity.setPayeeAlias(payeeEntity.getPayeeAlias());
            mCurrentClickPayeeEntity.setMobile(payeeEntity.getMobile());
            if (isNewPayeeItemClick) {
                isNewPayeeItemClick = false;
            } else {
                mAdapter.restore(mPayeeEntityList);
            }
        } else if (resultCode == PayeeDetailFragment2.RESULT_CODE_DELETE_SUCCESS) { // 删除收款人成功
            // 此处要判断删除的是“新收款人”，还是列表中的
            if (isNewPayeeItemClick) {
                isNewPayeeItemClick = false;

//                mRlNewPayee.setVisibility(View.GONE); // 隐藏“新收款人”条目
//                view_search_line.setVisibility(View.GONE); // 隐藏“分割条”
                mNewPayeeEntity = null;
            } else {
                mPayeeEntityList.remove(mCurrentClickPayeeEntity);
                mCurrentClickPayeeEntity = null;
                mAdapter.restore(mPayeeEntityList);
            }
        } else if (resultCode == AddPayeeFragment2.RESULT_CODE_ADD_PAYEE_SUCCESS) { // 添加收款人成功

            // 要做的事情就是:1如果有旧的，把旧的“新”添加到列表中、2把“新”的显示出来。
            if (mNewPayeeEntity != null) {
                mPayeeEntityList.add(mNewPayeeEntity);

                Collections.sort(mPayeeEntityList);
                mAdapter.restore(mPayeeEntityList);
            }

            mNewPayeeEntity = data.getParcelable("mNewPayeeEntity");
//            mRlNewPayee.setVisibility(View.VISIBLE);
//            view_search_line.setVisibility(View.VISIBLE); // 显示“分割条”

            tv_payee_name.setText(mNewPayeeEntity.getAccountName());
            tv_account_num.setText(NumberUtils.formatCardNumberStrong(mNewPayeeEntity.getAccountNumber()));
            tv_bank_name.setText(mNewPayeeEntity.getBankName());
            // 设置银行的logo
            Integer resId = null;
            if (mNewPayeeEntity.getBankName() != null && mNewPayeeEntity.getBankName().contains("中国银行")) {
                resId = BankLogoUtil.getLogoResByBankName1("中国银行");
            } else {
                resId = BankLogoUtil.getLogoResByBankName1(mNewPayeeEntity.getBankName());
            }

            if (resId != null) {
                iv_bank_logo_new.setImageResource(resId);
            } else {
                iv_bank_logo_new.setImageDrawable(null);
            }
        }
    }

    // 显示当前触摸的字母
    private void showWord(String word) {
        currentWord.setVisibility(View.VISIBLE);
        currentWord.setText(word);
    }
    @Override
    protected String getTitleValue() {
        return "收款人";
    }
    @Override
    protected boolean getTitleBarRed() {
        return false;
    }


    // 设置适配器并刷新列表
    private void setAdapterAndNotify() {
            // 排序
            Collections.sort(mPayeeEntityList);
            /**
             * wangyuan添加2016-8-5
             */
            mPayeeEntityList.addAll(0, linkedPayeeEntityList);
            // 设置“无数据”提示的显示
            tv_no_data.setVisibility(mPayeeEntityList.size() == 0 ? View.VISIBLE : View.GONE);
            // 计算“#”下面条目的数目
            countPinyinZ = 0;
            for (PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity : mPayeeEntityList) {
                if (payeeEntity.getPinyin().charAt(0) == 'z') {
                    countPinyinZ++;
                }
            }
            // 设置适配器
            if (mAdapter == null) {
                mAdapter = new ManagePayeeQueryPayeeListAdapter2(mActivity, mPayeeEntityList);
                listview.setAdapter(mAdapter);
            } else {
                mAdapter.setPayeeEntityList(mPayeeEntityList);
                mAdapter.notifyDataSetChanged();
            }
    }

    @Override
    public void setPresenter(TransContract.TransPresenterPayeeAccSelectPage presenter) {
    }
//    @Override
//    public void queryPsnCardQueryBindInfoSuccess(CardQueryBindInfoResult result) {
//
//    }
//
//    @Override
//    public void queryPsnCardQueryBindInfoFailed(BiiResultErrorException exception) {
//
//    }

    private ArrayList<String> payeeCurrencyList =new ArrayList<>();
    private ArrayList<String> payeeCashRemitList =new ArrayList<>();
     @Override
    public void queryAccountBalanceSuccess(AccountQueryAccountDetailResult result) {
        closeProgressDialog();

         ///////2016年12月1日屏蔽
         payeeCurrencyList.clear();
         payeeCashRemitList.clear();
//        for (AccountQueryAccountDetailResult.AccountDetaiListBean oneBean : result.getAccountDetaiList()) {
//            payeeCurrencyList.add(oneBean.getCurrencyCode());
//            payeeCashRemitList.add(oneBean.getCashRemit());
//        }
//         bundle.putStringArrayList(TransRemitBlankFragment.PAYEE_CURRENCY_LIST, payeeCurrencyList);
//         bundle.putStringArrayList(TransRemitBlankFragment.PAYEE_CASHREMIT_LIST, payeeCashRemitList);
         ///////2016年12月1日屏蔽
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
            pop();
//        }
    }

    @Override
    public void queryAccountBalanceFailed(BiiResultErrorException exception) {
        closeProgressDialog();
//        showErrorDialog(exception.getErrorMessage());
        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
        pop();
    }

    @Override
    public void queryCrcdAccountBalanceSuccess(CrcdQueryAccountDetailResult result) {
        payeeCurrencyList.clear();
        payeeCashRemitList.clear();
        for (CrcdQueryAccountDetailResult.CrcdAccountDetailListBean oneBean : result.getCrcdAccountDetailList()) {
            payeeCurrencyList.add(oneBean.getCurrency());
            if (ApplicationConst.CURRENCY_CNY.equals(oneBean.getCurrency())){
                payeeCashRemitList.add("00");
            }else{
                payeeCashRemitList.add("01");//信用卡外币种都是钞
            }
        }

        if(payeeCurrencyList.size()==2){
//            startPresenter();
            getPresenter().queryPsnCrcdChargeOnRMBAccount(mCurrentClickPayeeEntity.getAccountId());
        }else{
            closeProgressDialog();
            payeeCCRDJumpBack();
        }

//        }
    }

    public void payeeCCRDJumpBack(){
        bundle.putStringArrayList(TransRemitBlankFragment.PAYEE_CURRENCY_LIST, payeeCurrencyList);
        bundle.putStringArrayList(TransRemitBlankFragment.PAYEE_CASHREMIT_LIST, payeeCashRemitList);
//        if (!StringUtils.isEmptyOrNull(accAlreadyInfo.getTransCurrency())&&!StringUtils.isEmptyOrNull(accAlreadyInfo.getTransCsahRemit())){
//            if (payeeCurrencyList.contains(accAlreadyInfo.getTransCurrency())&&payeeCashRemitList.contains(accAlreadyInfo.getTransCsahRemit())){
//                setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
//                pop();
//            }
////            else{
////                showErrorDialog("收款账户不支持此币种转账，请修改");
////                return;
////            }
//        }else{
        currencyList.addAll(payeeCurrencyList);
//        if(accAlreadyInfo.getPayerCurrencyList().size()>0){
//            //说明付款账户已经选择了好了。
////            if (!StringUtils.isEmptyOrNull(accAlreadyInfo.getTransCurrency())){
//                if(!payeeCurrencyList.contains(accAlreadyInfo.getTransCurrency())){
//                    currencyList.retainAll(accAlreadyInfo.getPayerCurrencyList());
//                    if (currencyList.size()>0){
//                        isCrcdError=false;
//                    }else{
//                        isCrcdError=true;//如果没有币种交集报错
//                    }
//                    showErrorDialog("付款、收款账户币种不符，请调整");
//                    return;
//                }else{
//                    isCrcdError=true;
//                }
//        }

        if (accAlreadyInfo.getPayerCurrencyList().size()>0&&!StringUtils.isEmptyOrNull(accAlreadyInfo.getTransCurrency())){
            if(!payeeCurrencyList.contains(accAlreadyInfo.getTransCurrency())){
                currencyList.retainAll(accAlreadyInfo.getPayerCurrencyList());
                if (currencyList.size()>0){
                    setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
                    pop();
//                    isCrcdError=false;
                }else{
//                    isCrcdError=true;//如果没有币种交集报错
                    showErrorDialog("付款、收款账户币种不符，请调整");
                    return;
                }
            }else{
//                isCrcdError=false;
                setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
                pop();
            }
        }else{
//          isCrcdError=false;
            setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
            pop();
        }

    }


    @Override
    public void queryCrcdAccountBalanceFailed(BiiResultErrorException exception) {
        closeProgressDialog();
//        showErrorDialog(exception.getErrorMessage());
        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
        pop();
    }

    @Override
    public void queryPsnCrcdChargeOnRMBAccountSuccess(PsnCrcdChargeOnRMBAccountQueryResult result) {
        closeProgressDialog();
        if (result.getOpenFlag()){//开通
            payeeCurrencyList.clear();
            payeeCashRemitList.clear();
            payeeCurrencyList.add("001");//把币种清空，换为人民币
            payeeCashRemitList.add("00");
        }
        payeeCCRDJumpBack();
    }

    @Override
    public void queryPsnCrcdChargeOnRMBAccountFaild(BiiResultErrorException exception) {
        closeProgressDialog();
//        payeeCCRDJumpBack();
        showErrorDialog(exception.getErrorMessage());
    }

    @Override
    protected TransPayeeAccSelectPagePresenter initPresenter() {
        return new TransPayeeAccSelectPagePresenter(this);
    }
//
//    @Override
//    public void queryPsnOFAAccountStateSuccess(OFAAccountStateQueryResult result) {
//
//    }
//
//    @Override
//    public void queryPsnOFAAccountStateFailed(BiiResultErrorException exception) {
//
//    }
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom) > keyHeight) { // 键盘弹起
            isKeyBoardUp = true;
            if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                quickIndexBar2.setVisibility(View.GONE);
            } else {
                quickIndexBar.setVisibility(View.GONE);
            }
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom) > keyHeight) { // 键盘关闭
            isKeyBoardUp = false;
            if(isSearchNoDataState == false) {
                if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                    quickIndexBar2.setVisibility(View.VISIBLE);
                } else {
                    quickIndexBar.setVisibility(View.VISIBLE);
                }
            }
        }
    }

}

