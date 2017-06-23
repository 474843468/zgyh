package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.emptyview.CommonEmptyView;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model.VFGGetBindAccountViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.longshortforexhome.ui.LongShortForexHomeFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.adapter.MypurchaseAdapter;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.model.XpadPsnVFGPositionInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.mypurchase.presenter.MyPurchaseProductPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * 双向宝--我的持仓主页面
 * Created by zc on 2016/12/14
 */
public class MyPurchaseProductFragment extends BussFragment implements MyPurchaseProductContract.View {

    private View rootView;
    private MyPurchaseProductContract.Presenter mMyPurchaseProductPresenter;
    //交易账户信息
    private RelativeLayout account_details;
    private ImageView account_image;//账户图片
    private TextView account_number;//账号
    private TextView account_type;//账户类型

    private TextView account_management;//账户管理
    private TextView balance_query;//余额查询
    private TextView record_query;//对账单

    private VFGGetBindAccountViewModel resultmodel;//交易账户信息

    private LinearLayout no_purchase_product;//没有持仓时，空数据页面
    private CommonEmptyView no_data;
    private String account_num;//交易账户账号
    private String account_id;//交易账户ID
    private View showpurchaseview;
    private LinearLayout account_details_data;
    private ImageView account_image_data;
    private TextView account_number_data;
    private TextView account_type_data;
    private ListView showpurchaselist;
    private LinearLayout show_dara_query;
    private ListView listview_showdata;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
            rootView = mInflater.inflate(R.layout.boc_fragment_mypurchase_product, null);
            return rootView;
    }

    @Override
    public void initView() {
        account_details = (RelativeLayout) rootView.findViewById(R.id.account_details);
        account_image = (ImageView) rootView.findViewById(R.id.account_image);
        account_number = (TextView) rootView.findViewById(R.id.account_number);
        account_type = (TextView) rootView.findViewById(R.id.account_type);

        no_purchase_product = (LinearLayout) rootView.findViewById(R.id.no_data_query);
        no_data = (CommonEmptyView) rootView.findViewById(R.id.no_data);

        show_dara_query = (LinearLayout) rootView.findViewById(R.id.show_dara_query);
        listview_showdata = (ListView) rootView.findViewById(R.id.listview_mypurchase_showpurchase);
    }

    @Override
    public void initData() {
        showLoadingDialog();
        mMyPurchaseProductPresenter = new MyPurchaseProductPresenter(this);
        //查询当前交易账户
        VFGGetBindAccountViewModel vfgGetBindAccountViewModel = new VFGGetBindAccountViewModel();
        mMyPurchaseProductPresenter.psnVfgGetBindAccount(vfgGetBindAccountViewModel);

        //查询持仓信息
        XpadPsnVFGPositionInfoModel vfgPositionInfoModel = new XpadPsnVFGPositionInfoModel();
        mMyPurchaseProductPresenter.psnVFGPositionInfo(vfgPositionInfoModel);
    }

    @Override
    public void setListener() {
        no_data.setTextOnclickListener(new CommonEmptyView.TextOnclickListener() {//点我去看看点击事件
            @Override
            public void textOnclickListener() {
                popToAndReInit(LongShortForexHomeFragment.class);
            }
        });
    }

    // 处理有无数据时的情况
    private void handleNoData() {
        // 处理“数据是否为空”的情况
        no_data.setEmptyTips(R.drawable.no_result, 1, "您当前暂无持仓，", "点我去看看吧", "！");
        show_dara_query.setVisibility(View.GONE);
    }

     //显示持仓数据
    private void showhavedata(XpadPsnVFGPositionInfoModel vfgPositionInfoModel) {
        no_data.setVisibility(View.GONE);
        show_dara_query.setVisibility(View.VISIBLE);
       listview_showdata.setAdapter(new MypurchaseAdapter(getContext(),vfgPositionInfoModel));
    }


    /**
     * 查询交易账户成功
     * @param vfgGetBindAccountViewModel
     */
    @Override
    public void psnXpadVfgGetBindAccountSuccess(VFGGetBindAccountViewModel vfgGetBindAccountViewModel) {
        closeProgressDialog();
        resultmodel = vfgGetBindAccountViewModel;
        account_num = resultmodel.getAccountNumber();
        account_id = resultmodel.getAccountId();

        AccountUtils.CardType cardType = AccountUtils.getCardType(resultmodel.getAccountType());
        account_image.setImageResource(AccountUtils.getCardPic(cardType));
        account_number.setText(NumberUtils.formatCardNumberStrong(resultmodel.getAccountNumber()));
        account_type.setText(resultmodel.getNickName());
        account_details.setVisibility(View.VISIBLE);
    }

    /**
     * 查询交易账户失败
     * @param biiResultErrorException
     */
    @Override
    public void psnXpadVfgGetBindAccountFail(BiiResultErrorException biiResultErrorException) {
        closeProgressDialog();
    }

    /**
     * 查询持仓信息成功
     * @param vfgPositionInfoModel
     */

    @Override
    public void psnXpadVFGPositionInfoSuccess(XpadPsnVFGPositionInfoModel vfgPositionInfoModel) {
        Log.e("11111","1111") ;
        Log.e("222221",vfgPositionInfoModel.getDetails().toString()) ;
               if(vfgPositionInfoModel.getDetails()==null || vfgPositionInfoModel.getDetails().size() <= 0){
                   Log.e("444444444444444444444","没有数据");
                    handleNoData();
                }else{
                   Log.e("5555555" +
                           "" +
                           "555555555","数据显示了")  ;
                    showhavedata(vfgPositionInfoModel);
                }
    }

    /**
     * 查询持仓信息失败
     * @param biiResultErrorException
     */
    @Override
    public void psnXpadVFGPositionInfoFail(BiiResultErrorException biiResultErrorException) {

    }

    @Override
    protected String getTitleValue() {
        return "我的持仓";
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
    public void onDestroy() {
        mMyPurchaseProductPresenter.unsubscribe();
        super.onDestroy();
    }


    @Override
    public void setPresenter(MyPurchaseProductContract.Presenter presenter) {

    }
}
