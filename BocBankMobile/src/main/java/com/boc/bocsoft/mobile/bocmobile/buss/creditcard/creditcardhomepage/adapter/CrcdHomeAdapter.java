package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdInfoBean;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model.CrcdModel;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.utils.CrcdResultConvertUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.utils.ImageUtil;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.BaseAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackView;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;

/**
 * 信用卡首页适配器
 * Created by liuweidong on 2016/11/28.
 */

public class CrcdHomeAdapter extends CardStackAdapter<CrcdModel> {
    private static final String TAG = "CrcdHomeAdapter";

    private static ClickListener listener;
    private static boolean isLastItem = false;// 是否为最后一个itemview

    public CrcdHomeAdapter(Context context) {
        super(context);
    }

    @Override
    public CardStackView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.boc_fragment_credit_card_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void bindView(CrcdModel data, int position, CardStackView.ViewHolder holder) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (position == getItemCount() - 1) {
            isLastItem = true;
        }
        itemViewHolder.onBind(data, position);
    }

    class ItemViewHolder extends CardStackView.ViewHolder implements View.OnClickListener {
        FrameLayout frameLayout;
        TextView txtCardNo;
        ImageView imgDetail;
        RelativeLayout llContainerContent;// 展开父容器
        TextView txtCurrencyAbove;
        TextView txtCurrencyBelow;
        EditChoiceWidget billNo;// 未出账单
        EditChoiceWidget billYes;// 已出账单
        RelativeLayout rlContainerRepayment;
        TextView txtStatus;// 还款状态
        LinearLayout llContainerMoney;
        TextView txtMoneyCN;
        TextView txtMoneyOther;
        LinearLayout llNoResult;
        LinearLayout llContainerBtn;
        Button btnRepayment;// 还款
        Button btnBillPeriod;// 账单分期
        Button btnApply;// 申请信用卡

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public ItemViewHolder(View view) {
            super(view);
            frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
            txtCardNo = (TextView) view.findViewById(R.id.txt_card_no);
            imgDetail = (ImageView) view.findViewById(R.id.img_details);
            llContainerContent = (RelativeLayout) view.findViewById(R.id.ll_container_content);
            txtCurrencyAbove = (TextView) view.findViewById(R.id.txt_currency_above);
            txtCurrencyBelow = (TextView) view.findViewById(R.id.txt_currency_below);
            billNo = (EditChoiceWidget) view.findViewById(R.id.bill_no);
            billYes = (EditChoiceWidget) view.findViewById(R.id.bill_yes);
            rlContainerRepayment = (RelativeLayout) view.findViewById(R.id.rl_container_repayment);
            txtStatus = (TextView) view.findViewById(R.id.txt_status);
            llContainerMoney = (LinearLayout) view.findViewById(R.id.ll_container_money);
            txtMoneyCN = (TextView) view.findViewById(R.id.txt_money_cn);
            txtMoneyOther = (TextView) view.findViewById(R.id.txt_money_other);
            llNoResult = (LinearLayout) view.findViewById(R.id.ll_no_result);
            llContainerBtn = (LinearLayout) view.findViewById(R.id.ll_container_btn);
            btnRepayment = (Button) view.findViewById(R.id.btn_repayment);
            btnBillPeriod = (Button) view.findViewById(R.id.btn_bill_period);
            btnApply = (Button) view.findViewById(R.id.btn_apply);

            Bitmap bitmap = ImageUtil.drawableToBitmap(getContext().getResources().getDrawable(R.drawable.icon_credit_card));
            Drawable drawable = new BitmapDrawable(ImageUtil.fillet(bitmap, 20, ImageUtil.CORNER_ALL));
            frameLayout.setBackground(drawable);
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onItemExpand(boolean isExpand) {
            LogUtils.i(TAG + "--------onItemExpand--------isExpand=" + isExpand);
            llContainerContent.setVisibility(isExpand ? View.VISIBLE : View.GONE);
            imgDetail.setVisibility(isExpand ? View.VISIBLE : View.GONE);
            Bitmap bitmap = ImageUtil.drawableToBitmap(getContext().getResources().getDrawable(R.drawable.icon_credit_card));
            Drawable drawable;
            if (isExpand) {
                drawable = new BitmapDrawable(ImageUtil.fillet(bitmap, 20, ImageUtil.CORNER_TOP));
            } else {
                drawable = new BitmapDrawable(ImageUtil.fillet(bitmap, 20, ImageUtil.CORNER_ALL));
            }
            frameLayout.setBackground(drawable);
        }

        @Override
        protected void onAnimationStateChange(int state, boolean willBeSelect) {
            super.onAnimationStateChange(state, willBeSelect);
            if (state == BaseAnimator.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true);
            }
            if (state == BaseAnimator.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false);
            }
        }

        public void onBind(CrcdModel data, int position) {
            if (isLastItem) {
                btnApply.setVisibility(View.VISIBLE);
                isLastItem = false;
            }
            txtCardNo.setText(NumberUtils.formatCardNumber(data.getAccountBean().getAccountNumber()));// 设置卡号
            int size = data.getActList().size();
            if (size > 0) {
                LocalDateTime time = ApplicationContext.getInstance().getCurrentSystemDate();// 当前系统时间
                int month1 = time.plusMonths(-1).getMonthValue();
                String billDate = "";
                if (time.getDayOfMonth() < Integer.valueOf(data.getBillDate().trim())) {// 上月账单
                    int month2 = time.plusMonths(-2).getMonthValue();
                    billDate = "(" + dateStr(month2, Integer.valueOf(data.getBillDate().trim()) + 1)
                            + "-" + dateStr(month1, Integer.valueOf(data.getBillDate().trim())) + ")";
                } else {// 当月账单
                    if ("0".equals(data.getIsBillExist())) {// 未出
                        llNoResult.setVisibility(View.VISIBLE);
                        rlContainerRepayment.setVisibility(View.GONE);
                        llContainerMoney.setVisibility(View.GONE);
                        llContainerBtn.setVisibility(View.GONE);
                    } else if ("1".equals(data.getIsBillExist())) {// 已出
                        billDate = "(" + dateStr(month1, Integer.valueOf(data.getBillDate().trim()) + 1)
                                + "-" + dateStr(time.getMonthValue(), Integer.valueOf(data.getBillDate().trim())) + ")";
                    }
                }
                if (!"".equals(billDate)) {// 账单日
                    rlContainerRepayment.setVisibility(View.VISIBLE);
                    llContainerMoney.setVisibility(View.VISIBLE);
                    llContainerBtn.setVisibility(View.VISIBLE);
                    billYes.setNameWidth();
                    billYes.setChoiceTextName("已出账单" + billDate);
                }
                boolean isExistAmount = false;
                for (int i = 0; i < data.getActList().size(); i++) {// 不同币种信息
                    CrcdInfoBean crcdInfoBean = data.getActList().get(i);
                    if (new BigDecimal(crcdInfoBean.getHaveNotRepayAmout()).compareTo(new BigDecimal("0")) == 1) {
                        isExistAmount = true;
                    }
                    /*实时余额*/
                    String balanceFlag = CrcdResultConvertUtils.convertRtBalanceFlag(getContext(), crcdInfoBean.getRtBalanceFlag());
                    String balance = "[" + balanceFlag + "]" + PublicCodeUtils.getCurrency(getContext(), crcdInfoBean.getCurrency())
                            + MoneyUtils.transMoneyFormat(crcdInfoBean.getRealTimeBalance(), crcdInfoBean.getCurrency());
                    /*待还金额*/
                    String currencySymbol = PublicCodeUtils.getCurrencySymbol(getContext(), crcdInfoBean.getCurrency());
                    if (i == 0) {
                        txtCurrencyAbove.setText(balance);
                        txtMoneyCN.setText(currencySymbol + MoneyUtils.transMoneyFormat(crcdInfoBean.getHaveNotRepayAmout(), crcdInfoBean.getCurrency()));
                    } else if (i == 1) {
                        txtCurrencyBelow.setVisibility(View.VISIBLE);
                        txtCurrencyBelow.setText(balance);
                        txtMoneyOther.setText(currencySymbol + MoneyUtils.transMoneyFormat(crcdInfoBean.getHaveNotRepayAmout(), crcdInfoBean.getCurrency()));
                    }
                }
                if (isExistAmount) {// 还款日
                    txtStatus.setText(getContext().getString(R.string.boc_crcd_home_repayment_date) + data.getDueDate());
                    txtStatus.setBackgroundResource(R.drawable.bg_crcd_bill_state_yellow);
                    txtStatus.setTextColor(getContext().getResources().getColor(R.color.boc_text_color_yellow));
                } else {// 已还清
                    txtStatus.setText(getContext().getString(R.string.boc_crcd_home_repayment_no));
                    txtStatus.setBackgroundResource(R.drawable.bg_crcd_bill_state_green);
                    txtStatus.setTextColor(getContext().getResources().getColor(R.color.boc_text_color_green));
                }
            }

            llContainerContent.setOnClickListener(this);
            imgDetail.setOnClickListener(this);
            billNo.setOnClickListener(this);
            billYes.setOnClickListener(this);
            btnRepayment.setOnClickListener(this);
            btnBillPeriod.setOnClickListener(this);
            llContainerContent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.ll_container_content) {
//                ((CardStackView) itemView.getParent()).performItemClick(ItemViewHolder.this);
            } else if (i == R.id.img_details || i == R.id.btn_repayment || i == R.id.btn_bill_period || i == R.id.bill_no || i == R.id.bill_yes) {
                listener.onClick(i);
            }
        }

        private String dateStr(int month, int date) {
            return month + "月" + date + "日";
        }
    }

    public interface ClickListener {
        void onClick(int id);
    }

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

}
