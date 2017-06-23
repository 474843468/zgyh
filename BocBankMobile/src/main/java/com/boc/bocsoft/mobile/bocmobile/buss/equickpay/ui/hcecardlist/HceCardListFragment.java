package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.equickpay.service.EquickpayService;
import com.boc.bocsoft.mobile.bii.common.BiiException.BiiResultErrorException;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.SpUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.crop.enjoycrop.core.debug.L;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.adapter.HceCardListAdapterFinal;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceCancelModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.presenter.HceCardListPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceAddNewCarFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardsetting.HceMoreFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceUtil;
import com.boc.bocsoft.mobile.common.utils.LoggerUtils;
import com.boc.bocsoft.mobile.framework.utils.NetworkUtils;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gengjunying on 2016/11/29
 * 获取hce卡列表信息
 */
public class HceCardListFragment extends MvpBussFragment<HceCardListPresenter> implements HceCardListContact.View,
        View.OnClickListener, CardStackView.ItemExpandListener {
    //整个布局view
    private View rootView;
    //root
    private LinearLayout linearLayout_root;
    //手机不能使用NFC模块
    private LinearLayout linearLayout_no_support_hce;
    //手机版本低
    private LinearLayout linearLayout_android_version_no_support_hce;
    //手机支持nfc
    private RelativeLayout linearLayout_support_hcecard;
    //无hce卡数据
    private RelativeLayout relativelayoutNoHceCard;
    //用户向导
    private TextView textview_user_guide;


    //hce卡列表适配器
    private HceCardListAdapterFinal hceCardListAdapter;
    //Hce 卡列表
    private CardStackView cardStackView;
    //申请新卡
    private LinearLayout linearLayout_add_hce_card;
    // 卡品牌
    private String cardBrand = "";

    private List<HceCancelModel> cancelList = new ArrayList();

    private EquickpayService equickpayService = new EquickpayService();

    //test
    private List<HceCardListQueryViewModel> mHceCardListQueryViewModelList = new ArrayList<HceCardListQueryViewModel>();

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_hce_card_list, null);
        return rootView;
    }

    @Override
    public void initView() {
        //手机已经root
        linearLayout_root = (LinearLayout) rootView.findViewById(R.id.linearLayout_root);

        //手机不能使用NFC模块
        linearLayout_no_support_hce = (LinearLayout) rootView.findViewById(R.id.linearLayout_no_support_hce);

        //手机版本低
        linearLayout_android_version_no_support_hce = (LinearLayout) rootView.findViewById(R.id.linearLayout_android_version_no_support_hce);

        //手机可以使用NFC
        linearLayout_support_hcecard = (RelativeLayout) rootView.findViewById(R.id.linearLayout_support_hcecard);

        //无hce卡数据
        relativelayoutNoHceCard = (RelativeLayout) rootView.findViewById(R.id.relativelayoutNoHceCard);
        relativelayoutNoHceCard.setVisibility(View.GONE);

        //用户向导
        textview_user_guide = (TextView) rootView.findViewById(R.id.textview_user_guide);
        textview_user_guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showErrorDialog("进入用户向导页面");
            }
        });

        //hce卡列表
        cardStackView = (CardStackView) rootView.findViewById(R.id.card_stack_view);
        cardStackView.setItemExpandListener(new CardStackView.ItemExpandListener() {
            @Override
            public void onItemExpand(boolean expend) {
            }
        });


        //申请新卡
        linearLayout_add_hce_card = (LinearLayout) rootView.findViewById(R.id.linearLayout_add_hce_card);
        linearLayout_add_hce_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HceAddNewCarFragment hceAddNewCarFragment = new HceAddNewCarFragment();
                start(hceAddNewCarFragment);
            }
        });
    }

    @Override
    public void initData() {
        //判断手机可以使用NFC, 网络状态
//        if(HceConstants.NFC_SUPPORT.ROOT == HceUtil.checkHceFun(getContext())){//判断手机是否root
//            linearLayout_root.setVisibility(View.VISIBLE);
//            return;
//        }else if(HceConstants.NFC_SUPPORT.ANDROID_VERTION_NOT_SUPPORT == HceUtil.checkHceFun(getContext())){//安卓版本是否支持HCE
//            linearLayout_android_version_no_support_hce.setVisibility(View.VISIBLE);
//            return;
//        }else if(HceConstants.NFC_SUPPORT.NOT_SUPPORT_NFC == HceUtil.checkHceFun(getContext())){//手机您的手机不支持NF
//            linearLayout_no_support_hce.setVisibility(View.VISIBLE);
//            return;
//        }else if(HceConstants.NFC_SUPPORT.SUPPORT == HceUtil.checkHceFun(getContext())){
//            linearLayout_support_hcecard.setVisibility(View.VISIBLE);
//            return;
//        }else if (!NetworkUtils.haveNetworkConnection(mContext)) {
            // TODO
//            return;
//        }

        //手机银行已经登录
        if (ApplicationContext.getInstance().isLogin()) {
            //获取hce卡列表数据，刷新UI
            getPresenter().HCEQuickPassListQuery(HceUtil.getDeviceId(mContext));
        } else {//未登录
            //先判断用户是否以前登录过手机银行没有，如果登录过，则显示（未登录的hce卡列表）
            //如果用户以前未曾登录过，则显示申请hce静态页面，当点击“申请新卡”，此时，提示用户登录
            //获取用户是否登录过得信息
            SharedPreferences preferences = getContext().getSharedPreferences(getContext().getPackageName(), Context.MODE_PRIVATE); //则该配置文件只能被自己的应用程序访问。
            String identityType = preferences.getString(SpUtils.SPKeys.KEY_LOGINIDENTITYTYPE, "");
            String identityNumber = preferences.getString(SpUtils.SPKeys.KEY_LOGINIDENTITYNUMBER, "");

            //证件类型和证件号码，都存在，获取登录前的hce卡列表
            if (!"".equals(identityType) && !"".equals(identityNumber)) {
                //有用户登录过的信息，显示未登录的Hce列表
                //获取未登录的hce卡列表
                getPresenter().HCEQuickPassListNoLoginQuery(HceUtil.getDeviceId(mContext), identityType, identityNumber);

                //test luk 接口
                //getPresenter().PsnHCEQuickPassLukLoad(HceUtil.getDeviceId(mContext), "111111", "10", "");
            } else {
                //无用户登录过的信息，调至向导页
                HceAddNewCarFragment hceAddNewCarFragment = new HceAddNewCarFragment();
                start(hceAddNewCarFragment);
            }
        }
    }

    @Override
    public void setListener() {


    }


    //获取hce卡列表数据
    private void refreshLoginHceCardData(List<HceCardListQueryViewModel> hceCardListQueryViewModelList) {

        //后台hce卡列表与本地卡列表比较
        //hceCardItemEntityList = compareHceCardList(hceCardListQueryViewModel.getHceCardList());

        mHceCardListQueryViewModelList = hceCardListQueryViewModelList;

        //列表适配器填充数据,显示hce卡列表
        if (hceCardListAdapter == null) {
            hceCardListAdapter = new HceCardListAdapterFinal(mActivity, this);
            hceCardListAdapter.setData(hceCardListQueryViewModelList);
            cardStackView.setAdapter(hceCardListAdapter);
        } else {
            hceCardListAdapter.updateData(hceCardListQueryViewModelList);
            hceCardListAdapter.notifyDataSetChanged();
        }


        //显示隐藏  申请新卡（当小于3张hce卡时，显示“申请新卡”，反之，隐藏“申请新卡”）
        if (hceCardListQueryViewModelList.size() > 3) {
            linearLayout_add_hce_card.setVisibility(View.GONE);
        } else {
            linearLayout_add_hce_card.setVisibility(View.VISIBLE);
        }
    }


    //BI后台获取的Hce卡与本地卡列表比较，取交集
    public List<HceCardListQueryViewModel> compareHceCardList(List<HceCardListQueryViewModel> hceCardList) {

        // 卡列表非空
        if (hceCardList.size() > 0) {
            // 遍历支持的所有卡品牌
            for (HceConstants.CardOrg itCardOrg : HceConstants.CardOrg.values()) {//4个
                //卡品牌
                cardBrand = itCardOrg.toString();

                String hceCardNo = HceUtil.checkIfCanceled(hceCardList, cardBrand);//和遍历得到的卡品牌相同的从卡卡号
                // TSM应用类型
                int appType = HceConstants.CardOrg.valueOf(cardBrand).ordinal() + 1;

                // HCE控件获取卡号返回字符串
                String tsmHceCardNo = "";

                // 本地HCE卡号
                String locHceCardNo = "";

                // 判断本地是否已有
                try {
                    //获取HCE卡序列号
                    tsmHceCardNo = HceUtil.getHceCardNoOrSerialNo(mContext, appType, HceConstants.HCE_CARD_FLAG);
                } catch (Exception ex) {
                    showErrorDialog("很抱歉，e闪付控件异常");
                    return hceCardList;
                }

                // 本地已有
                if (tsmHceCardNo.startsWith(HceConstants.HCE_SUCCEED)) {
                    locHceCardNo = tsmHceCardNo.substring(4).trim().replaceAll("\\D", "");//去掉非数字

                    // 卡列表有，正常HCE卡,则这个hce卡正常的
                    if (hceCardNo != null && hceCardNo.equals(locHceCardNo)) {
                        //判断LUK个数是否小于JUDGE_LUK_NUM，若小于JUDGE_LUK_NUM，更新LUK
                        LoggerUtils.Error("限制密钥个数:" + HceUtil.getLimitKeyLetfTimes(mContext, appType));

                        if (HceUtil.getLimitKeyLetfTimes(mContext, appType) < HceConstants.JUDGE_LUK_NUM) {
                            LoggerUtils.Error("限制密钥个数小于JUDGE_LUK_NUM");
                            // 获取卡片限制密钥
                            //此处暂未完善，获取卡限制密钥
                            //getPresenter().GetHceLuk("862806035774345", "02");
                        }
                    }// 卡列表无，已注销HCE卡，需清除本地个人化数据
                    else {
                        String cancelRes = HceUtil.clearHceEnv(mContext, appType);

                        if (cancelRes.equals(HceConstants.HCE_SUCCEED)) {
                            LoggerUtils.Error("卡列表中已注销，本地个人化数据清除成功");
                        }
                    }
                }// 本地无
                else {
                    // 卡列表有，本地个人化失败的HCE卡，需调注销接口
                    if (hceCardNo != null) {
                        /**
                         * 先从返回的卡列表中去掉该卡，然后去后台注销该卡
                         */
                        for (int i = 0; i < hceCardList.size(); i++) {
                            if (hceCardList.get(i).getSlaveCardNo().equals(hceCardNo)) {
                                hceCardList.remove(i);

                                HceCancelModel hceCancelModel = new HceCancelModel();
                                hceCancelModel.setMasterCardNo(hceCardList.get(i).getMasterCardNo());
                                hceCancelModel.setSlaveCardNo(hceCardList.get(i).getSlaveCardNo());

                                cancelList.add(hceCancelModel);
                            }
                        }

                    }
                }
            }
            // 调注销接口
            cancelHceCard(cancelList);
        }

        return hceCardList;
    }


    private void cancelHceCard(List<HceCancelModel> list) {

//        List<Observable<PsnHCEQuickPassCancelResult>> temp_list = new ArrayList<>();
//
//        for (int i = 0; i < list.size(); i++){
//            PsnHCEQuickPassCancelParams psnHCEQuickPassCancelParams = new PsnHCEQuickPassCancelParams();
//            psnHCEQuickPassCancelParams.setMasterCardNo(list.get(i).getMasterCardNo());
//            psnHCEQuickPassCancelParams.setSlaveCardNo(list.get(i).getSlaveCardNo());
//
//            temp_list.add(equickpayService.PsnHCEQuickPassCancel(psnHCEQuickPassCancelParams));
//        }
//
//        Observable<String> mergeObservale = Observable.mergeDelayError(Observable.from(temp_list));
//
//
//        mergeObservale.subscribe(new Observer<String>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//
//            }
//
//            @Override
//            public void onNext(String s) {
//
//            }
//        });
    }


    @Override
    protected String getTitleValue() {
        return "E闪付";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected HceCardListPresenter initPresenter() {
        return new HceCardListPresenter(this);
    }


    @Override
    public void setPresenter(HceCardListContact.Presenter presenter) {

    }


    //已登录---卡列表数据返回回调---成功
    @Override
    public void HCEQuickPassListQuerySuccess(List<HceCardListQueryViewModel> hceCardListQueryViewModelList) {
        //有hce卡数据显示列表，反之显示图片
        if(hceCardListQueryViewModelList.size() > 0){
            refreshLoginHceCardData(hceCardListQueryViewModelList);
        }else {
            relativelayoutNoHceCard.setVisibility(View.VISIBLE);
        }
    }

    //已登录---卡列表数据返回回调---失败
    @Override
    public void HCEQuickPassListQueryFail(BiiResultErrorException biiResultErrorException) {

    }


    //未登录---卡列表数据返回回调---成功
    @Override
    public void HCEQuickPassListNoLoginQuerySuccess(List<HceCardListQueryViewModel> hceCardListQueryViewModelList) {
        //有hce卡数据显示列表，反之显示图片
        if(hceCardListQueryViewModelList.size() > 0){
            refreshLoginHceCardData(hceCardListQueryViewModelList);
        }else {
            relativelayoutNoHceCard.setVisibility(View.VISIBLE);
        }
    }

    //未登录---卡列表数据返回回调---失败
    @Override
    public void HCEQuickPassListNoLoginQueryFail(BiiResultErrorException biiResultErrorException) {

    }




    //注销成功
    @Override
    public void PsnHCEQuickPassCancelSuccess() {

        //重新获取列表
        getPresenter().HCEQuickPassListQuery(HceUtil.getDeviceId(mContext));
        showToast("您已成功注销这张云闪付卡");
    }

    //注销失败
    @Override
    public void PsnHCEQuickPassCancelFail() {

    }

    //HCE闪付卡LUK加载 成功
    @Override
    public void PsnHCEQuickPassLukLoadSuccess(List<String> list) {

    }


    //HCE闪付卡LUK加载 失败
    @Override
    public void PsnHCEQuickPassLukLoadFail(BiiResultErrorException biiResultErrorException) {

    }


    @Override
    protected boolean isDisplayRightIcon() {
        if(mHceCardListQueryViewModelList.size() > 0){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void titleRightIconClick() {
        // TODO: 2016/12/20

        start(HceMoreFragment.newInstance(mHceCardListQueryViewModelList));
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    public void onItemExpand(boolean expend) {

    }

    @Override
    public void onClick(View v) {

    }

}
