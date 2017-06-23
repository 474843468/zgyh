package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui.SelectFinanceAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.adapter.PayerAccountAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.CollectionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnBatchTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.PsnTransActCollectionVerifyViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model.QueryPayerListViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 我要收款的首页(单人和多人)
 * Created by zhx on 2016/6/29.
 */
public class MeMakeCollectionFragment extends BussFragment {
    private boolean isNeedReversed = false; // 是否需要把集合反转
    private AccountBean mPayeeAccount;
    private PayerAccountAdapter mPayerAccountAdapter;
    private ArrayList<QueryPayerListViewModel.ResultBean> mTestBeanList;
    private ArrayList<QueryPayerListViewModel.ResultBean> mSelectedPayerList;
    private int receiptWay = 0; // 收款方式，0表示“按人均额收”，1表示“按总额收”

    private View mRootView;
    private EditChoiceWidget ecwReceiptWay; // 组件：收款方式选择控件
    private EditChoiceWidget ecwChoosePayer; // 组件：选择付款账户控件
    private EditChoiceWidget ecwChoosePayeeAccount; // 组件：选择收款账户控件
    private SelectStringListDialog mReceiptWayDialog; // 组件：收款方式弹窗

    private TextView tv_payee_phone_number; // 收款人手机号码
    private TextView tv_total_amount;// 组件：收款总额
    private GridView gvPayerList;// 组件：付款人列表
    private TextView tv_next_step;
    private static final int REQUEST_CODE_PAYEE_ACCOUNT = 212120; // 请求码：收款账户
    private static final int REQUEST_CODE_PAYER_ACCOUNT = 212121; // 请求码：付款账户
    private ScrollView sv_root;
    private LinearLayout ll_total_amount_container;
    private EditText et_payer_num;
    private EditText et_remark;
    private EditMoneyInputWidget emiw_average_amount;
    private EditMoneyInputWidget emiw_pay_total_amount;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRootView = mInflater.inflate(R.layout.boc_fragment_me_make_collection, null);
        return mRootView;
    }

    @Override
    public void initView() {
        ecwReceiptWay = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_receipt_way); // 收款方式
        ecwChoosePayer = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_choose_payer);
        ecwChoosePayeeAccount = (EditChoiceWidget) mRootView.findViewById(R.id.ecw_choose_payee_account);
        tv_next_step = (TextView) mRootView.findViewById(R.id.tv_next_step);
        tv_payee_phone_number = (TextView) mRootView.findViewById(R.id.tv_payee_phone_number);
        tv_total_amount = (TextView) mRootView.findViewById(R.id.tv_total_amount);
        gvPayerList = (GridView) mRootView.findViewById(R.id.gv_payer_list);
        sv_root = (ScrollView) mRootView.findViewById(R.id.sv_root);
        ll_total_amount_container = (LinearLayout) mRootView.findViewById(R.id.ll_total_amount_container);
        emiw_average_amount = (EditMoneyInputWidget) mRootView.findViewById(R.id.emiw_average_amount);
        emiw_pay_total_amount = (EditMoneyInputWidget) mRootView.findViewById(R.id.emiw_pay_total_amount);
        et_payer_num = (EditText) mRootView.findViewById(R.id.et_payer_num);
        et_remark = (EditText) mRootView.findViewById(R.id.et_remark);
    }

    @Override
    public void initData() {


        tv_payee_phone_number.setText(((ApplicationContext) (mActivity.getApplicationContext())).getUser().getMobile());
        emiw_average_amount.setEditWidgetTitle("人均额");
        emiw_average_amount.setContentHint("请输入");
        emiw_pay_total_amount.setEditWidgetTitle("收款总额");
        emiw_pay_total_amount.setContentHint("请输入");
    }

    @Override
    public void setListener() {
        // 对收款总额的输入控件设置监听，动态更改下面显示的总额
        emiw_average_amount.getContentMoneyEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = emiw_average_amount.getContentMoney();
                if (!TextUtils.isEmpty(money) && mSelectedPayerList != null) { // 如果money不为空
                    tv_total_amount.setText(Double.parseDouble(money) * mSelectedPayerList.size() + "");
                } else {
                    tv_total_amount.setText("0.00");
                }
            }
        });
        // 对收款总额的输入控件设置监听，动态更改下面显示的总额
        emiw_pay_total_amount.getContentMoneyEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_total_amount.setText(MoneyUtils.transMoneyFormat(emiw_pay_total_amount.getContentMoney(), "001"));
            }
        });

        ecwChoosePayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 隐藏键盘
                hideSoftInput();
                // 打开选择付款账户页面
                ChoosePayerAccountFragment toFragment = new ChoosePayerAccountFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNeedReversed", isNeedReversed);
                isNeedReversed = false; // 说明：这块的逻辑是利用RecycleView倒序加集合数据反转实现数据项的移动的
                bundle.putParcelableArrayList(ChoosePayerAccountFragment.PAYER_ACCOUNT, mTestBeanList);
                bundle.putParcelableArrayList(ChoosePayerAccountFragment.SELECTED_PAYER_ACCOUNT, mSelectedPayerList);
                toFragment.setArguments(bundle);
                startForResult(toFragment, REQUEST_CODE_PAYER_ACCOUNT);
            }
        });

        ecwChoosePayeeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开选择收款账户页面
                ChoosePayeeAccountFragment toFragment = new ChoosePayeeAccountFragment();
                startForResult(toFragment, REQUEST_CODE_PAYEE_ACCOUNT);
            }
        });

        // 点击选择“收款方式”
        ecwReceiptWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReceiptWayDialog = new SelectStringListDialog(mContext);

                ArrayList<String> listData = new ArrayList<String>();
                listData.add("按总额收");
                listData.add("人均额收");

                mReceiptWayDialog.setListData(listData);
                mReceiptWayDialog.show();
                mReceiptWayDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                    @Override
                    public void onSelect(int position, String model) {
                        if ("按总额收".equals(model)) {
                            emiw_average_amount.setEditWidgetTitle("收款总额");
                            receiptWay = 1;

                            emiw_average_amount.setVisibility(View.GONE);
                            ll_total_amount_container.setVisibility(View.VISIBLE);

                            // 设置收款总额的显示
                            String totalAmount = emiw_pay_total_amount.getContentMoney();
                            if (TextUtils.isEmpty(totalAmount)) {
                                tv_total_amount.setText("0.00");
                            } else {
                                tv_total_amount.setText(MoneyUtils.transMoneyFormat(totalAmount, "001"));
                            }
                        } else if ("人均额收".equals(model)) {
                            emiw_average_amount.setEditWidgetTitle("人均额");
                            receiptWay = 0;

                            emiw_average_amount.setVisibility(View.VISIBLE);
                            ll_total_amount_container.setVisibility(View.GONE);

                            // 设置收款总额的显示
                            String averageAmount = emiw_average_amount.getContentMoney();
                            if (!TextUtils.isEmpty(averageAmount) && mSelectedPayerList != null) {
                                tv_total_amount.setText(MoneyUtils.transMoneyFormat(Double.parseDouble(averageAmount) * mSelectedPayerList.size() + "", "001"));
                            } else {
                                tv_total_amount.setText("0.00");
                            }

                        }
                        mReceiptWayDialog.dismiss();
                        ecwReceiptWay.setChoiceTextContent(model);
                    }
                });
            }
        });

        tv_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第1步，检查数据
                boolean flag = checkData();
                if (!flag) {
                    return;
                }

                // 第2步，根据选择的付款人数，发起向单人收款或者向多人收款
                if (mSelectedPayerList.size() == 1) { // 主动收款（单人）
                    PsnTransActCollectionVerifyViewModel viewModel = generateCollectionVerifyViewModel();

                    // 启动“确认信息”页面
                    PayConfirmImformationFragment payConfirmImformationFragment = new PayConfirmImformationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 0); // 0表示单人，1 表示多人
                    bundle.putSerializable("psnTransActCollectionVerifyViewModel", viewModel);
                    payConfirmImformationFragment.setArguments(bundle);
                    start(payConfirmImformationFragment);
                } else { // 主动收款（多人）
                    PsnBatchTransActCollectionVerifyViewModel viewModel = generateBatchCollectionVerifyViewModel();

                    // 启动“确认信息”页面
                    PayConfirmImformationFragment payConfirmImformationFragment = new PayConfirmImformationFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 1); // 0表示单人，1 表示多人
                    bundle.putSerializable("psnBatchTransActCollectionVerifyViewModel", viewModel);
                    payConfirmImformationFragment.setArguments(bundle);

                    MeMakeCollectionFragment.this.pop();
                    start(payConfirmImformationFragment);
                }
            }
        });

        gvPayerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View contentView = View.inflate(mActivity, R.layout.popup_payer_mobile, null);
                PopupWindow popup = new PopupWindow(contentView,
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                popup.setBackgroundDrawable(new ColorDrawable());

                TextView tv_item = (TextView) contentView.findViewById(R.id.tv_item);
                QueryPayerListViewModel.ResultBean testBean = (QueryPayerListViewModel.ResultBean) gvPayerList.getAdapter().getItem(position);
                tv_item.setText(testBean.getPayerMobile());

                int[] location = new int[2];
                view.getLocationOnScreen(location);
                popup.showAtLocation(sv_root, Gravity.NO_GRAVITY, (int) (location[0] - getResources().getDimension(R.dimen.boc_space_between_22px)), (int) (location[1] - view.getHeight() * 1.2));
            }
        });
    }

    // 生成预交易ViewModel(单人)
    private PsnTransActCollectionVerifyViewModel generateCollectionVerifyViewModel() {
        PsnTransActCollectionVerifyViewModel viewModel = new PsnTransActCollectionVerifyViewModel();

        viewModel.setToAccountId(mPayeeAccount.getAccountId()); // 收款人账ID
        viewModel.setPayeeName(mPayeeAccount.getAccountName()); // 收款人姓名
        viewModel.setCurrency("001"); // 币种，001表示人民币

        // 收款金额
        if (receiptWay == 0) {
            viewModel.setNotifyPayeeAmount(MoneyUtils.transMoneyFormat(emiw_average_amount.getContentMoney(), "001"));
        } else {
            String totalAmount = emiw_pay_total_amount.getContentMoney();
            String payerNum = et_payer_num.getText().toString().trim();
            double total = Double.parseDouble(totalAmount);
            double num = Double.parseDouble(payerNum);

            String notifyPayeeAmount = Double.toString(total / num);
            viewModel.setNotifyPayeeAmount(MoneyUtils.transMoneyFormat(notifyPayeeAmount, "001"));
        }

        viewModel.setPayerCustId(mSelectedPayerList.get(0).getPayerCustomerId()); // 付款人客户号
        viewModel.setRemark(et_remark.getText().toString().trim()); // 备注
        viewModel.setPayerMobile(mSelectedPayerList.get(0).getPayerMobile()); // 付款人手机
        viewModel.setPayerName(mSelectedPayerList.get(0).getPayerName()); // 付款人姓名
        viewModel.setPayeeMobile(((ApplicationContext) (mActivity.getApplicationContext())).getUser().getMobile()); // 收款人手机
        viewModel.setPayerChannel(mSelectedPayerList.get(0).getIdentifyType()); // 付款人类型:1：WEB渠道、2：手机渠道
        viewModel.setPayeeActno(mPayeeAccount.getAccountNumber());

        return viewModel;
    }

    // 生成预交易ViewModel(多人)
    private PsnBatchTransActCollectionVerifyViewModel generateBatchCollectionVerifyViewModel() {
        PsnBatchTransActCollectionVerifyViewModel viewModel = new PsnBatchTransActCollectionVerifyViewModel();

        viewModel.setToAccountId(mPayeeAccount.getAccountId()); // 收款人账ID
        viewModel.setPayeeName(mPayeeAccount.getAccountName()); // 收款人姓名
        viewModel.setPayeeActno(mPayeeAccount.getAccountNumber());
        viewModel.setCurrency("001"); // 币种，001表示人民币

        // 收款金额
        if (receiptWay == 0) { // 按平均额收
            viewModel.setNotifyPayeeAmount(MoneyUtils.transMoneyFormat(emiw_average_amount.getContentMoney(), "001"));
            String totalAmount = Double.parseDouble(emiw_average_amount.getContentMoney()) * mSelectedPayerList.size() + "";
            viewModel.setTotalAmount(MoneyUtils.transMoneyFormat(totalAmount, "001"));
            viewModel.setTotalNum(mSelectedPayerList.size() + "");
        } else { // 按总额收
            String totalAmount = emiw_pay_total_amount.getContentMoney();
            String payerNum = et_payer_num.getText().toString().trim();
            double total = Double.parseDouble(totalAmount);
            double num = Double.parseDouble(payerNum);

            String notifyPayeeAmount = Double.toString(total / num);
            viewModel.setNotifyPayeeAmount(MoneyUtils.transMoneyFormat(notifyPayeeAmount, "001"));
            viewModel.setTotalNum(et_payer_num.getText().toString().trim());
            viewModel.setTotalAmount(MoneyUtils.transMoneyFormat(totalAmount, "001"));
        }

        viewModel.setPayeeMobile(((ApplicationContext) (mActivity.getApplicationContext())).getUser().getMobile()); // 收款人手机
        viewModel.setRemark(et_remark.getText().toString().trim()); // 备注
        int i = 1;
        List<PsnBatchTransActCollectionVerifyViewModel.PayerEntity> payerEntityList = new ArrayList<PsnBatchTransActCollectionVerifyViewModel.PayerEntity>();
        for (QueryPayerListViewModel.ResultBean resultBean : mSelectedPayerList) {
            PsnBatchTransActCollectionVerifyViewModel.PayerEntity payerEntity = new PsnBatchTransActCollectionVerifyViewModel.PayerEntity();
            payerEntity.setPayerNo(i + "");
            payerEntity.setPayerCustId(resultBean.getPayerCustomerId());
            payerEntity.setPayerMobile(resultBean.getPayerMobile());
            payerEntity.setPayerName(resultBean.getPayerName());
            payerEntity.setPayerChannel(resultBean.getIdentifyType()); // 付款人类型
            payerEntityList.add(payerEntity);
            i++;
        }
        viewModel.setPayerList(payerEntityList);

        return viewModel;
    }

    /**
     * 检查预交易的数据
     *
     * @return
     */
    private boolean checkData() {
        if (mPayeeAccount == null) {
            showErrorDialog("请选择收款账户");
            return false;
        }

        if (receiptWay == 0) { // 按人均额收
            String averageAmount = emiw_average_amount.getContentMoney();
            if (TextUtils.isEmpty(averageAmount)) {
                showErrorDialog("请输入人均额");
                return false;
            }
        } else { // 按总额收
            String totalAmount = emiw_pay_total_amount.getContentMoney();
            if (TextUtils.isEmpty(totalAmount)) {
                showErrorDialog("请输入收款总额");
                return false;
            }
            String payerNum = et_payer_num.getText().toString().trim();
            if (TextUtils.isEmpty(payerNum)) {
                showErrorDialog("请输入收款人数");
                return false;
            }
        }

        if (mSelectedPayerList == null) {
            showErrorDialog("请选择付款人");
            return false;
        }
        return true;
    }

    public void prepareCollectionViewModel(CollectionViewModel collectionViewModel) {
        collectionViewModel.setToAccountId(mPayeeAccount.getAccountId()); // 收款人账ID
        collectionViewModel.setPayeeName(mPayeeAccount.getAccountName()); // 收款人姓名
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {

        if (resultCode == ChoosePayeeAccountFragment.RESULT_CODE_SELECT_PAYEE_ACCOUNT) { // 选择收款账户
            // 获取选择账户信息,并设置签约Model
            mPayeeAccount = data.getParcelable(SelectFinanceAccountFragment.ACCOUNT_SELECT);
            if (mPayeeAccount != null) {
                ecwChoosePayeeAccount.setChoiceTextContent(mPayeeAccount.getAccountNumber());
            }
        } else if (resultCode == ChoosePayerAccountFragment.RESULT_CODE_SELECTED_PAYER_ACCOUNT) { // 选择付款人
            isNeedReversed = data.getBoolean("isNeedReversed");
            mSelectedPayerList = data.getParcelableArrayList(ChoosePayerAccountFragment.SELECTED_PAYER_ACCOUNT);
            mTestBeanList = data.getParcelableArrayList(ChoosePayerAccountFragment.PAYER_ACCOUNT);

            // 设置收款总额
            if (receiptWay == 0) { // 如果是“按人均额”，那么计算收款总额
                String money = emiw_average_amount.getContentMoney();
                if (!TextUtils.isEmpty(money) && mSelectedPayerList != null) { // 如果money不为空
                    tv_total_amount.setText(MoneyUtils.transMoneyFormat(Double.parseDouble(money) * mSelectedPayerList.size() + "", "001"));
                } else {
                    tv_total_amount.setText("0.00");
                }
            }

            if (mPayerAccountAdapter == null) {
                mPayerAccountAdapter = new PayerAccountAdapter(mContext, mSelectedPayerList);
                gvPayerList.setAdapter(mPayerAccountAdapter);
            } else {
                mPayerAccountAdapter.setTestBeanList(mSelectedPayerList);
                mPayerAccountAdapter.notifyDataSetChanged();
            }

            // 手动计算GridView的高度
            int numColumns = gvPayerList.getNumColumns(); // 列数
            int count = mSelectedPayerList.size(); // 记录总数
            int row = 0;
            row = count / numColumns;
            if (count % numColumns != 0) {
                row++;
            }
            int verticalSpacing = gvPayerList.getVerticalSpacing();

            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) mContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float density = metrics.density;

            int itemHeight = (int) activity.getResources().getDimension(R.dimen.boc_space_between_80px);

            int paddingBottom = gvPayerList.getPaddingBottom();

            int gridViewHeight = verticalSpacing * (row - 1) + itemHeight * row + paddingBottom;

            LinearLayout.LayoutParams latyoutParams = (LinearLayout.LayoutParams) gvPayerList.getLayoutParams();
            latyoutParams.height = gridViewHeight;
            gvPayerList.setLayoutParams(latyoutParams);
        }
    }

    @Override
    protected String getTitleValue() {
        return "我要收款";
    }

    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    public void testInterface(View view) {
    }
}