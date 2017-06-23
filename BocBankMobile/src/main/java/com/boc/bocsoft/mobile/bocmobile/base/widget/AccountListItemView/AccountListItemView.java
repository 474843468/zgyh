package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.AccountListItemView.model.AccountListItemViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.PublicUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.squareup.picasso.Picasso;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils.getCardPic;

/**
 * 账户列表卡片控件
 * Created by Administrator on 2016/5/20.
 */
public class AccountListItemView extends LinearLayout {

    protected View rootView;
    protected ImageView ivPic;
    protected TextView tvNumber;
    protected TextView tvName;
    protected ImageView ivStatus;
    protected View divider;
    protected TextView tvAmountTitle;
    protected LinearLayout lytAmountList;
    protected PartialLoadView ivLoading;
    protected RelativeLayout lytAmount;
    protected ImageView ivMedical;
    protected ImageView ivEcash;
    protected ImageView ivArrow;
    private Context mContext;
    private AccountListItemViewModel mCardInfo;//卡片信息

    private boolean amountInfoVisible = true;//额度信息区域是否可见
    private boolean medicalEcashVisible = true;//医保电子现金标志是否可见

    public AccountListItemView(Context context) {
        super(context);
        initView(context);
    }

    public AccountListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AccountListItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        View rootView = inflate(mContext, R.layout.boc_item_accountlist, this);
        ivPic = (ImageView) rootView.findViewById(R.id.iv_pic);
        tvNumber = (TextView) rootView.findViewById(R.id.tv_number);
        tvName = (TextView) rootView.findViewById(R.id.tv_name);
        ivStatus = (ImageView) rootView.findViewById(R.id.iv_status);
        divider = rootView.findViewById(R.id.divider);
        tvAmountTitle = (TextView) rootView.findViewById(R.id.tv_amount_title);
        lytAmountList = (LinearLayout) rootView.findViewById(R.id.lyt_amount_list);
        lytAmountList.setGravity(Gravity.RIGHT);
        ivLoading = (PartialLoadView) rootView.findViewById(R.id.iv_loading);
        lytAmount = (RelativeLayout) rootView.findViewById(R.id.lyt_amount);
        ivMedical = (ImageView) rootView.findViewById(R.id.iv_medical);
        ivEcash = (ImageView) rootView.findViewById(R.id.iv_ecash);
        ivArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
    }

    /**
     * 获取卡片信息
     *
     * @return 卡片信息
     */
    public AccountListItemViewModel getCardInfo() {
        return mCardInfo;
    }

    /**
     * 设置卡片信息。
     * 卡片根据账户类型来决定是否显示卡片额度区域（活期定期不显示）
     * 第一次加载卡片列表时没有额度数据，从接口请求到数据后设置到卡片信息里。
     * 卡片控件只需要根据是否有额度数据来决定显示额度列表还是显示加载按钮，不负责控制请求接口。
     *
     * @param cardInfo 卡片信息
     */
    public void setData(AccountListItemViewModel cardInfo) {
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
        if (medicalEcashVisible) {
            ivEcash.setVisibility(
                    "1".equals(mCardInfo.getAccountBean().getIsECashAccount()) ? VISIBLE : GONE);
            ivMedical.setVisibility(
                    "1".equals(mCardInfo.getAccountBean().getIsMedicalAccount()) ? VISIBLE : GONE);
        } else {
            ivEcash.setVisibility(GONE);
            ivMedical.setVisibility(GONE);
        }
        int cardStatusPic;
        if (!StringUtils.isEmpty(mCardInfo.getAccountBean().getAccountStatus())
                && (cardStatusPic = getCardStatusPic(mCardInfo.getAccountBean().getAccountStatus()))
                != 0) {
            Picasso.with(mContext).load(cardStatusPic).into(ivStatus);
            ivStatus.setVisibility(VISIBLE);
        } else {
            ivStatus.setVisibility(INVISIBLE);
        }
        boolean hasAmountInfo =
                !PublicUtils.isEmpty(mCardInfo.getCardAmountViewModelList());//检查有没有数据
        tvAmountTitle.setText(mCardInfo.getAmountTitle());
        if (!amountInfoVisible) {
            showAmountInfo(false);
            return;
        }
        //根据账户类型和额度列表判断额度区域是否要显示
        boolean isAmountInfoShown = isAmountAreaShown(mCardInfo.getAccountBean().getAccountType());
        showAmountInfo(isAmountInfoShown);
        if (isAmountInfoShown) {
            //有额度信息直接展示
            if (hasAmountInfo) {
                ivLoading.setLoadStatus(PartialLoadView.LoadStatus.SUCCESS);
                showAmountListView();
            } else {//没有额度信息的话，第一次展示
                lytAmountList.setVisibility(GONE);
                ivLoading.setLoadStatus(PartialLoadView.LoadStatus.REFRESH);
            }
        }
    }

    /**
     * 根据卡片状态获取对应图片
     * 01，已销户，05 挂失，06冻结，09其他，00正常
     * 正常时返回0，表示不用显示
     */
    //TODO 状态图片切图没有全部给出
    private int getCardStatusPic(String accountStatus) {
        if ("01".equals(accountStatus)) {
            return R.drawable.boc_account_status_cancelled;
        }
        if ("05".equals(accountStatus)) {
            return R.drawable.boc_account_status_reportloss;
        }
        //        if ("06".equals(accountStatus)) {
        //            return R.drawable.boc_account_status_frozen;
        //        }
        if ("09".equals(accountStatus)) {
            return 0;
        }
        return 0;
    }

    /**
     * 根据账户类型判断额度区域是否需要显示
     *
     * @param accountType 账户类型
     * @return 额度区域是否需要显示
     */
    private boolean isAmountAreaShown(String accountType) {
        //所有定期不显示额度区域
        return !(StringUtils.isEmpty(accountType)
                || AccountUtils.CardType.TERM_ACCOUNT == AccountUtils.getCardType(accountType)
                || AccountUtils.CardType.XNCRCD == AccountUtils.getCardType(accountType));
    }

    /**
     * 设置额度信息区域是否可见
     *
     * @param showAmountInfo 额度信息区域是否可见
     */
    private void showAmountInfo(boolean showAmountInfo) {
        if (showAmountInfo) {
            divider.setVisibility(VISIBLE);
            lytAmount.setVisibility(VISIBLE);
        } else {
            divider.setVisibility(GONE);
            lytAmount.setVisibility(GONE);
        }
    }

    public void setAmountInfoVisible(boolean visible) {
        amountInfoVisible = visible;
    }

    /**
     * 显示额度列表
     */
    private void showAmountListView() {
        lytAmountList.removeAllViews();
        Observable.from(mCardInfo.getCardAmountViewModelList())
                  .firstOrDefault(mCardInfo.getCardAmountViewModelList().get(0),
                          new Func1<AccountListItemViewModel.CardAmountViewModel, Boolean>() {
                              @Override
                              public Boolean call(
                                      AccountListItemViewModel.CardAmountViewModel cardAmountViewModel) {
                                  return ApplicationConst.CURRENCY_CNY.equals(
                                          cardAmountViewModel.getCurrencyCode());
                              }
                          })
                  .subscribe(new Action1<AccountListItemViewModel.CardAmountViewModel>() {
                      @Override
                      public void call(
                              AccountListItemViewModel.CardAmountViewModel cardAmountViewModel) {
                          AccountListItemAmountView amountInfoView =
                                  new AccountListItemAmountView(mContext);
                          amountInfoView.setData(cardAmountViewModel);
                          lytAmountList.addView(amountInfoView);
                      }
                  });
        lytAmountList.setVisibility(VISIBLE);
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

    public void setArrowVisible(boolean visible) {
        ivArrow.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setMedicalEcashVisible(boolean visible) {
        medicalEcashVisible = visible;
    }
}
