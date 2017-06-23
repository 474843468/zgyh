package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.ui;

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

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.BankLogoUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.index.QuickIndexBar;
import com.boc.bocsoft.mobile.bocmobile.base.widget.index.QuickIndexBar2;
import com.boc.bocsoft.mobile.bocmobile.base.widget.stickylistheaders.StickyListHeadersListView;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.adapter.ManagePayeeQueryPayeeListAdapter2;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model.PsnTransPayeeListqueryForDimViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.presenter.PsnTransPayeeListqueryForDimPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.ui.TransRemitBlankFragment;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 收款人管理的首页（切勿随意修改）
 * Created by zhx on 2016/8/30
 */
public class PayeeManageFragment2 extends BussFragment implements PsnTransPayeeListqueryForDimContact.View, View.OnLayoutChangeListener {
    private static final int REQUEST_CODE_ADD_PAYEE = 101; // 请求码：添加付款人
    public static final int RESULT_CODE_SELECT_ACCOUNT_SUCCESS = 102; // 响应码，选择付款人成功
    public static final String DO_WHAT = "dowhat"; // 来这个页面要干啥
    public static final String ACTION_JUST_CHOOSE_ACCOUNT = "JustChooseAccount"; // 操作，仅仅选择账号
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
    private PsnTransPayeeListqueryForDimPresenter mPresenter;
    private ManagePayeeQueryPayeeListAdapter2 mAdapter;
    private List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> mPayeeEntityList = new ArrayList<PsnTransPayeeListqueryForDimViewModel.PayeeEntity>(); // 收款人列表

    private PsnTransPayeeListqueryForDimViewModel.PayeeEntity mCurrentClickPayeeEntity; // 当前点击的条目对应的PayeeEntity
    private PsnTransPayeeListqueryForDimViewModel.PayeeEntity mNewPayeeEntity; // 新收款人
    private View mHeaderView;
    private RelativeLayout mRlNewPayee;
    private boolean isNewPayeeItemClick = false; // 是否点击了“新收款人条目”，默认为false
    private String mDowhat;
    private ImageView iv_bank_logo_new;


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
    private boolean isUpdateNew;
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
    }

    @Override
    public void initData() {
        setAdapterAndNotify();

        mHeaderView = View.inflate(mActivity, R.layout.list_payee_header, null);
        mRlNewPayee = (RelativeLayout) mHeaderView.findViewById(R.id.rl_new_payee);
        iv_delete_text = (ImageView) mRootView.findViewById(R.id.iv_delete_text);
        mRlNewPayee.setVisibility(View.GONE);
        et_search_content = (EditText) mRootView.findViewById(R.id.et_search_content);
        tv_payee_name = (TextView) mHeaderView.findViewById(R.id.tv_payee_name);
        tv_account_num = (TextView) mHeaderView.findViewById(R.id.tv_account_num);
        tv_bank_name = (TextView) mHeaderView.findViewById(R.id.tv_bank_name);
        iv_bank_logo_new = (ImageView) mHeaderView.findViewById(R.id.iv_bank_logo_new);
        listview.addHeaderView(mHeaderView);

        // 接收上一个页面传来的参数
        //wangyuan 添加 2016-8-5
        linkedPayeeEntityList = new ArrayList<>();

        mDowhat = getArguments().getString(DO_WHAT); // 上一个页面跳转到本页面想干啥
        if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
            //            letter_me.setVisibility(View.VISIBLE);
            linkedAccountList = getArguments().getParcelableArrayList(TransRemitBlankFragment.LINKED_ACCOUNT_TYPE);
            if (null != linkedAccountList && linkedAccountList.size() > 0) {
                for (AccountBean onebean : linkedAccountList) {
                    PsnTransPayeeListqueryForDimViewModel.PayeeEntity oneLinkedPayeeEntity =
                            new PsnTransPayeeListqueryForDimViewModel.PayeeEntity();
                    BeanConvertor.toBean(onebean, oneLinkedPayeeEntity);
                    oneLinkedPayeeEntity.setPinyin("我");
                    oneLinkedPayeeEntity.setLinked(true);
                    oneLinkedPayeeEntity.setPayeetId(0);
                    linkedPayeeEntityList.add(oneLinkedPayeeEntity);
                }
                //                isLinkedAccountOk = true;
                setAdapterAndNotify();
                quickIndexBar2.setVisibility(View.VISIBLE);
                quickIndexBar.setVisibility(View.GONE);
            }
            //            isLinkedAccountOk = false;
        } else {
            //            letter_me.setVisibility(View.GONE);
            quickIndexBar2.setVisibility(View.GONE);
            quickIndexBar.setVisibility(View.VISIBLE);
        }

        // 检查全局中是否有值
        ApplicationContext context = (ApplicationContext) mActivity.getApplicationContext();
        List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> globalList = context.getPayeeEntityList();
        mPresenter = new PsnTransPayeeListqueryForDimPresenter(this);
        if(globalList == null) {
            showLoadingDialog("加载中...", false);
            PsnTransPayeeListqueryForDimViewModel viewModel = new PsnTransPayeeListqueryForDimViewModel();
            mPresenter.psnTransPayeeListqueryForDim(viewModel);
        } else {
            mPayeeEntityList.addAll(globalList);
            isPayeeQureyDone = true;
            setAdapterAndNotify();
        }
    }

    int y = 0;
    int dy = 0;
    boolean isNeedScroll = false;

    @Override
    public void setListener() {
        iv_delete_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search_content.setText("");
            }
        });
        // 设置“新付款人”的点击事件
        mRlNewPayee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNewPayeeItemClick = true;
                mCurrentClickPayeeEntity = mNewPayeeEntity;
                Bundle bundle = new Bundle();
                bundle.putParcelable(PayeeManageFragment2.KEY_PAYEE_ENTITY, mNewPayeeEntity);
                PayeeDetailFragment2 payeeDetailFragment = new PayeeDetailFragment2();
                payeeDetailFragment.setArguments(bundle);
                startForResult(payeeDetailFragment, 101); // 启动付款人详情界面
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    mCurrentClickPayeeEntity = mAdapter.getItem(position - 1); // 此处偶尔会崩溃
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(KEY_PAYEE_ENTITY, mCurrentClickPayeeEntity);
                    if (mDowhat != null && mDowhat.equals(ACTION_JUST_CHOOSE_ACCOUNT)) { // 处理如果是仅仅选择账户的情况
                        //wangyuan 转账汇款功能
                        setFramgentResult(RESULT_CODE_SELECT_ACCOUNT_SUCCESS, bundle);
                        pop();
                    } else { // 处理本页面显示收款人详情的逻辑
                        hideSoftInput(); // 记得如果键盘弹出来，先把键盘隐藏掉

                        PayeeDetailFragment2 payeeDetailFragment = new PayeeDetailFragment2();
                        payeeDetailFragment.setArguments(bundle);
                        startForResult(payeeDetailFragment, 101); // 启动付款人详情界面
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                                listview.setSelection(i);
                                break;
                            }
                        }

                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            listview.setSelection(i);
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
                                listview.setSelection(i);
                                break;
                            }
                        }

                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            listview.setSelection(i);
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
                                listview.setSelection(i);
                                break;
                            }
                        }

                        if (!mPayeeEntityList.get(i).isLinked()) {
                            if (currentFirstWord.equals(word)) {
                                // 将第一个首字母为word设置到屏幕顶端
                                listview.setSelection(i);
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
                                listview.setSelection(i);
                                break;
                            }
                        }

                        if (currentFirstWord.equals(word)) {
                            // 将第一个首字母为word设置到屏幕顶端
                            listview.setSelection(i);
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

                if (!TextUtils.isEmpty(str)) { // 搜索文字不为空
                    // “搜索无结果”和“暂无常用收款人”的切换 todo

                    iv_delete_text.setClickable(true);
                    iv_delete_text.setVisibility(View.VISIBLE);

                    if (mAdapter.getCount() == 0 && mNewPayeeEntity == null) {
                        isSearchNoDataState = true;
                        quickIndexBar.setVisibility(View.GONE);
                        quickIndexBar2.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.GONE);
                        tv_search_no_data.setVisibility(View.VISIBLE);
                    } else {
                        isSearchNoDataState = false;
                        if (!isKeyBoardUp) {
                            if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                                quickIndexBar2.setVisibility(View.VISIBLE);
                                quickIndexBar.setVisibility(View.GONE);
                            } else {
                                quickIndexBar2.setVisibility(View.GONE);
                                quickIndexBar.setVisibility(View.VISIBLE);
                            }
                        }
                        tv_no_data.setVisibility(View.GONE);
                        tv_search_no_data.setVisibility(View.GONE);
                    }
                } else { // 搜索文字为空
                    iv_delete_text.setClickable(false);
                    iv_delete_text.setVisibility(View.INVISIBLE);
                    isSearchNoDataState = false;

                    if (mAdapter.getCount() == 0 && mNewPayeeEntity == null) {
                        isSearchNoDataState = false;
                        quickIndexBar.setVisibility(View.GONE);
                        quickIndexBar2.setVisibility(View.GONE);
                        tv_search_no_data.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.VISIBLE);
                    } else {
                        if (!isKeyBoardUp) {
                            if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                                quickIndexBar2.setVisibility(View.VISIBLE);
                                quickIndexBar.setVisibility(View.GONE);
                            } else {
                                quickIndexBar2.setVisibility(View.GONE);
                                quickIndexBar.setVisibility(View.VISIBLE);
                            }
                        }

                        tv_search_no_data.setVisibility(View.GONE);
                        tv_no_data.setVisibility(View.GONE);
                    }
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
        if (mDowhat != null && mDowhat.equals(ACTION_JUST_CHOOSE_ACCOUNT)) {
            return false;
        } else {
            return true;
        }
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
            // 此处要判断修改的是“新收款人”，还是列表中的 TODO
            PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity = data.getParcelable("payeeEntity");
            mCurrentClickPayeeEntity.setPayeeAlias(payeeEntity.getPayeeAlias());
            mCurrentClickPayeeEntity.setMobile(payeeEntity.getMobile());
            if (isNewPayeeItemClick) {
                isNewPayeeItemClick = false;
            } else {
                mAdapter.restore(mPayeeEntityList);
            }
        } else if (resultCode == PayeeDetailFragment2.RESULT_CODE_DELETE_SUCCESS) { // 删除收款人成功
            // 此处要判断删除的是“新收款人”，还是列表中的 TODO
            if (isNewPayeeItemClick) {
                isNewPayeeItemClick = false;

                mRlNewPayee.setVisibility(View.GONE); // 隐藏“新收款人”条目
                //                view_search_line.setVisibility(View.GONE); // 隐藏“分割条”
                mNewPayeeEntity = null;
            } else {
                mPayeeEntityList.remove(mCurrentClickPayeeEntity);
                mAdapter.restore(mPayeeEntityList);
            }
            mCurrentClickPayeeEntity = null;

            // 设置“无数据”提示的显示
            tv_no_data.setVisibility(mPayeeEntityList.size() == 0 && mNewPayeeEntity == null ? View.VISIBLE : View.GONE);
            // 设置索引条的隐藏和显示
            if (mPayeeEntityList.size() == 0 && mNewPayeeEntity == null) {
                quickIndexBar.setVisibility(View.GONE);
                quickIndexBar2.setVisibility(View.GONE);
            } else {
                if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                    quickIndexBar2.setVisibility(View.VISIBLE);
                } else {
                    quickIndexBar.setVisibility(View.VISIBLE);
                }
            }
        } else if (resultCode == AddPayeeFragment2.RESULT_CODE_ADD_PAYEE_SUCCESS) { // 添加收款人成功

            // 要做的事情就是:1如果有旧的，把旧的“新”添加到列表中、2把“新”的显示出来。
            if (mNewPayeeEntity != null) {
                mPayeeEntityList.add(mNewPayeeEntity);

                Collections.sort(mPayeeEntityList);

                mAdapter.restore(mPayeeEntityList);
            }

            mNewPayeeEntity = data.getParcelable("mNewPayeeEntity");
            // zhx 2016-10-27 添加更新new的逻辑 start
            isUpdateNew = true;
            PsnTransPayeeListqueryForDimViewModel viewModel = new PsnTransPayeeListqueryForDimViewModel();
            String[] bocFlag = {"0", "1", "3"};
            viewModel.setBocFlag(bocFlag);
            viewModel.setPageSize("500");
            viewModel.setPayeeName("");
            viewModel.setCurrentIndex("0");
            viewModel.setIsAppointed("");
            mPresenter.psnTransPayeeListqueryForDim(viewModel);
            // zhx 2016-10-27 添加更新new的逻辑 end

            mRlNewPayee.setVisibility(View.VISIBLE);
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
                iv_bank_logo_new.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.other_bank_logo));
            }

            // 设置“无数据”提示的显示
            tv_no_data.setVisibility(mPayeeEntityList.size() == 0 && mNewPayeeEntity == null ? View.VISIBLE : View.GONE);
            // 设置索引条的隐藏和显示
            if (mPayeeEntityList.size() == 0 && mNewPayeeEntity == null) {
                quickIndexBar.setVisibility(View.GONE);
                quickIndexBar2.setVisibility(View.GONE);
            } else {
                if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                    quickIndexBar2.setVisibility(View.VISIBLE);
                } else {
                    quickIndexBar.setVisibility(View.VISIBLE);
                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 执行保存到全局的逻辑
        mPayeeEntityList.removeAll(linkedPayeeEntityList);
        if (mNewPayeeEntity != null) { // 如果新的不为null，那么把新的添加到列表中
            mPayeeEntityList.add(mNewPayeeEntity);
        }
        // 排序
        Collections.sort(mPayeeEntityList);
        // 保存到全局中
        ApplicationContext context = (ApplicationContext) mActivity.getApplicationContext();
        context.setPayeeEntityList(mPayeeEntityList);
    }

    // 设置适配器并刷新列表
    private void setAdapterAndNotify() {
        if (isPayeeQureyDone) {
            // 排序
            Collections.sort(mPayeeEntityList);

            /**
             * wangyuan天剑2016-8-5
             */
            //            if (isLinkedAccountOk &&isPayeeQureyDone){//防止二次添加
            mPayeeEntityList.addAll(0, linkedPayeeEntityList);
            //            }

            // 设置“无数据”提示的显示
            tv_no_data.setVisibility(mPayeeEntityList.size() == 0 ? View.VISIBLE : View.GONE);
            // 设置索引条的隐藏和显示
            if (mPayeeEntityList.size() == 0) {
                quickIndexBar.setVisibility(View.GONE);
                quickIndexBar2.setVisibility(View.GONE);
            } else {
                if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                    quickIndexBar2.setVisibility(View.VISIBLE);
                } else {
                    quickIndexBar.setVisibility(View.VISIBLE);
                }
            }

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

            closeProgressDialog();
        }

    }

    @Override
    public void psnTransPayeeListqueryForDimSuccess(PsnTransPayeeListqueryForDimViewModel viewModel) {
        closeProgressDialog();
        if(isUpdateNew) { // 如果是更新新添加的联系人
            for (PsnTransPayeeListqueryForDimViewModel.PayeeEntity payeeEntity : viewModel.getPayeeEntityList()) {
                if ("1".equals(mNewPayeeEntity.getBocFlag())) { // 中国银行
                    if (payeeEntity.getAccountNumber().endsWith(mNewPayeeEntity.getAccountNumber())) {
                        BeanConvertor.toBean(payeeEntity, mNewPayeeEntity);
                        break;
                    }
                } else { // 他行
                    if (payeeEntity.getAccountNumber().endsWith(mNewPayeeEntity.getAccountNumber()) && payeeEntity.getCnapsCode().equals(mNewPayeeEntity.getCnapsCode())) {
                        BeanConvertor.toBean(payeeEntity, mNewPayeeEntity);
                        break;
                    }
                }
            }
        } else {
            List<PsnTransPayeeListqueryForDimViewModel.PayeeEntity> payeeEntityListFromInternet = viewModel.getPayeeEntityList();
            mPayeeEntityList.addAll(payeeEntityListFromInternet);
            isPayeeQureyDone = true;
            setAdapterAndNotify();
        }

        isUpdateNew = false;
    }

    @Override
    public void psnTransPayeeListqueryForDimFailed(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
        isPayeeQureyDone = true;
    }

    @Override
    public void setPresenter(PsnTransPayeeListqueryForDimContact.Presenter presenter) {

    }

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
            if (isSearchNoDataState == false) {
                if (ACTION_JUST_CHOOSE_ACCOUNT.equals(mDowhat)) {
                    if (mPayeeEntityList.size() > 0 || mNewPayeeEntity != null) {
                        quickIndexBar2.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (mPayeeEntityList.size() > 0 || mNewPayeeEntity != null) {
                        quickIndexBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}