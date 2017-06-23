package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.widget.FreePassListItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils.QrCodeUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.squareup.picasso.Picasso;

import static com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils.getCardPic;

/**
 * 小额免密列表卡片控件
 * Created by wangf on 2016/9/22.
 */
public class FreePassListItemView extends LinearLayout {

    private Context mContext;
    private View rootView;
    private ImageView ivPic;
    private TextView tvItemTitle;
    private TextView tvNumber;
    private TextView tvName;
    protected CheckBox cbFree;
    protected PartialLoadView ivLoading;
    private LinearLayout lytQuota;
    private TextView tvQuota;
    private FreePassViewModel mCardInfo;//卡片信息

    public FreePassListItemView(Context context) {
        super(context);
        initView(context);
    }

    public FreePassListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FreePassListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View rootView = inflate(mContext, R.layout.boc_item_qrpay_free_pwd, this);
        ivPic = (ImageView) rootView.findViewById(R.id.iv_qrpay_pic);
        tvItemTitle = (TextView) rootView.findViewById(R.id.tv_qrpay_item_title);
        tvNumber = (TextView) rootView.findViewById(R.id.tv_qrpay_number);
        tvName = (TextView) rootView.findViewById(R.id.tv_qrpay_name);
        cbFree = (CheckBox) rootView.findViewById(R.id.cb_qrpay_free);
        ivLoading = (PartialLoadView) rootView.findViewById(R.id.iv_qrpay_loading);
        lytQuota = (LinearLayout) rootView.findViewById(R.id.ll_qrpay_free_tip);
        tvQuota = (TextView) rootView.findViewById(R.id.tv_qrpay_quota);
    }

    /**
     * 获取卡片信息
     *
     * @return 卡片信息
     */
    public FreePassViewModel getCardInfo() {
        return mCardInfo;
    }

    /**
     * 设置卡片信息。
     * 卡片根据是否开通小额免密来决定是否显示卡片额度区域
     * 第一次加载卡片列表时没有额度数据，从接口请求到数据后设置到卡片信息里。
     * 卡片控件只需要根据是否有额度数据来决定显示额度列表还是显示加载按钮，不负责控制请求接口。
     *
     * @param cardInfo 卡片信息
     */
    public void setData(FreePassViewModel cardInfo) {
        if (cardInfo == null || cardInfo.getAccountBean() == null) {
            return;
        }
        this.mCardInfo = cardInfo;
        Picasso.with(mContext).load(getCardPic(mCardInfo.getAccountBean())).into(ivPic);
        tvNumber.setText(
                NumberUtils.formatCardNumber(mCardInfo.getAccountBean().getAccountNumber()));
        /*tvName.setText(PublicCodeUtils.getAccountType(mContext,
                mCardInfo.getAccountBean().getAccountType()));*/
        tvName.setText(mCardInfo.getAccountBean().getNickName());

        boolean isShowQuota = false;
        boolean isCreditCard = false;
        boolean isOpenFree = false;

        // 606
        //0:未开通  1:已开通
        if (mCardInfo.getFreeInfoViewModel() != null){
            if ("1".equals(mCardInfo.getFreeInfoViewModel().getPassFreeFlag())) {
                isShowQuota = true;
//                isCreditCard = true;
                isOpenFree = true;
            }else if("0".equals(mCardInfo.getFreeInfoViewModel().getPassFreeFlag())){
                isShowQuota = true;
//                isCreditCard = true;
                isOpenFree = false;
            }
        }

        // X610
//        String accountType = mCardInfo.getAccountBean().getAccountType();
//        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
//                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
//            //0:未开通  1:已开通
//            if (mCardInfo.getFreeInfoViewModel() != null){
//                if ("1".equals(mCardInfo.getFreeInfoViewModel().getCreditCardFlag())) {
//                    isShowQuota = true;
//                    isCreditCard = true;
//                    isOpenFree = true;
//                }else if("0".equals(mCardInfo.getFreeInfoViewModel().getCreditCardFlag())){
//                	isShowQuota = true;
//                    isCreditCard = true;
//                    isOpenFree = false;
//                }
//            }
//
//        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
//            //0:未开通  1:已开通
//            if (mCardInfo.getFreeInfoViewModel() != null){
//                if ("1".equals(mCardInfo.getFreeInfoViewModel().getDebitCardFlag())) {
//                    isShowQuota = true;
//                    isCreditCard = false;
//                    isOpenFree = true;
//                }else if("0".equals(mCardInfo.getFreeInfoViewModel().getDebitCardFlag())){
//                	isShowQuota = true;
//                    isCreditCard = false;
//                    isOpenFree = false;
//                }
//            }
//        }

        showAmountInfo(isOpenFree, isCreditCard);
        //若开通免密金额直接显示
        if (isShowQuota) {
        	if(isOpenFree){
        		cbFree.setChecked(true);
        	}else{
        		cbFree.setChecked(false);
        	}
            cbFree.setVisibility(VISIBLE);
            ivLoading.setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
        } else {
            cbFree.setVisibility(GONE);
            cbFree.setChecked(false);
            ivLoading.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
        }
    }


    /**
     * 设置额度信息区域是否可见
     * @param showAmountInfo 额度信息区域是否可见
     * @param isCreditCard 是否是信用卡
     */
    private void showAmountInfo(boolean showAmountInfo, boolean isCreditCard) {
        if (showAmountInfo) {
            lytQuota.setVisibility(VISIBLE);

            //606
            if(!StringUtils.isEmpty(mCardInfo.getFreeInfoViewModel().getPassFreeAmount())){
                tvQuota.setText(MoneyUtils.transMoneyFormat(
                        QrCodeUtils.getFormatTransQuotaNoSign(mCardInfo.getFreeInfoViewModel().getPassFreeAmount()),
                        ApplicationConst.CURRENCY_CNY) + "元/笔");
            }

            // X610
//            if (isCreditCard){
//                //信用卡
//            	if(!StringUtils.isEmpty(mCardInfo.getFreeInfoViewModel().getCreditCardPassFreeAmount())){
//            		tvQuota.setText(MoneyUtils.transMoneyFormat(
//            				QrCodeUtils.getFormatTransQuotaNoSign(mCardInfo.getFreeInfoViewModel().getCreditCardPassFreeAmount()),
//            				ApplicationConst.CURRENCY_CNY) + "元/笔");
//            	}
//            }else {
//                //借记卡
//                if(!StringUtils.isEmpty(mCardInfo.getFreeInfoViewModel().getDebitCardPassFreeAmount())){
//                    tvQuota.setText(MoneyUtils.transMoneyFormat(
//                            QrCodeUtils.getFormatTransQuotaNoSign(mCardInfo.getFreeInfoViewModel().getDebitCardPassFreeAmount()),
//                            ApplicationConst.CURRENCY_CNY) + "元/笔");
//                }
//            }
        } else {
            lytQuota.setVisibility(GONE);
            tvQuota.setText("");
        }
    }


    /**
     * 显示加载动画，或者显示刷新按钮，或者加载成功隐藏按钮。
     * 刷新按钮时可点击
     *
     * @param loadStatus 加载状态
     */
    public void setLoadStatus(PartialLoadView.LoadStatus loadStatus) {
        ivLoading.setLoadStatus(loadStatus);
    }


    /**
     * 设置item的头部标题是否显示
     * @param isShow
     */
    public void isShowItemTitle(boolean isShow){
        if (isShow) {
            tvItemTitle.setVisibility(View.VISIBLE);
        } else {
            tvItemTitle.setVisibility(GONE);
        }
    }

    /**
     * 设置item的头部标题文字
     * @param str
     */
    public void setItemTitleText(String str){
            tvItemTitle.setText(str);
    }
}
