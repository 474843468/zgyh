package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.BaseAnimator;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackAdapter;
import com.boc.bocsoft.mobile.bocmobile.base.widget.cardstackview.CardStackView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.ConfirmDialog;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.CardBrandModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.HceTransactionViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceactivecard.HceActivateConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceaddcard.HceAddNewCarFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardlist.HceCardListFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hcecardsetting.HceQuotaSettingFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.ui.hceunlosscard.HceCardUnlossConfirmFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceConstants;
import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util.HceUtil;

import java.text.Normalizer;

/**
 * 信用卡首页适配器
 * Created by liuweidong on 2016/12/26..
 */

public class HceCardListAdapterFinal extends CardStackAdapter<HceCardListQueryViewModel> {
    private int itemCount = 0;
    private HceCardListFragment mHceCardListFragment;


    public HceCardListAdapterFinal(Context context, HceCardListFragment hceCardListFragment) {
        super(context);
        mHceCardListFragment = hceCardListFragment;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public CardStackView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.list_item_equickpay_hcecard_final, parent, false);
        return new ItemViewHolder(view);

    }

    @Override
    public void bindView(HceCardListQueryViewModel data, int position, CardStackView.ViewHolder holder) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.onBind(data, position);
    }

    class ItemViewHolder extends CardStackView.ViewHolder implements View.OnClickListener {
        TextView textview_hce_account; //hce卡号
        TextView textview_maincard_account;//主卡卡号
        ImageView imageView_hce_status;//hce卡状态
        ImageView imageView_hce_bland;//hce卡品牌


        LinearLayout llContainerContent;// 展开父容器
        DetailTableRowButton table_single_order;//单笔交易限额
        TextView textview_dayQuota;//日交易限额
        TextView textview_unloss;//解挂
        TextView textview_activie;//激活
        TextView textview_zhuxiao;//注销

        public ItemViewHolder(View view) {
            super(view);
            //hce卡卡号
            textview_hce_account = (TextView) view.findViewById(R.id.textview_hce_account);
            //主卡卡号
            textview_maincard_account = (TextView) view.findViewById(R.id.textview_maincard_account);
            //hce卡状态
            imageView_hce_status = (ImageView) view.findViewById(R.id.imageView_hce_status);
            //卡品牌
            imageView_hce_bland = (ImageView) view.findViewById(R.id.imageView_hce_bland);


            //卡信息 展开的布局
            llContainerContent = (LinearLayout) view.findViewById(R.id.linearLayout_account_info);
            //单笔交易限额
            table_single_order = (DetailTableRowButton) view.findViewById(R.id.table_single_order);
            //日交易限额
            textview_dayQuota = (TextView) view.findViewById(R.id.textview_dayQuota);
            //解挂
            textview_unloss = (TextView) view.findViewById(R.id.textview_unloss);
            //激活
            textview_activie = (TextView) view.findViewById(R.id.textview_activie);
            //注销
            textview_zhuxiao = (TextView) view.findViewById(R.id.textview_zhuxiao);


        }

        @Override
        public void onItemExpand(boolean b) {
            llContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);

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

        public void onBind(final HceCardListQueryViewModel data, int position) {
            //hce卡
            textview_hce_account.setText(data.getSlaveCardNo());
            //主卡卡号
            textview_maincard_account.setText(data.getMasterCardNo());
            //卡状态
            imageView_hce_status.setBackground(hceCardStatus(data.getCardStatus()));
            //卡品牌
            imageView_hce_bland.setImageDrawable(hceCardBland(data.getCardBrand()));


            //日交易限额
            if (null != data.getDayQuota()) {
                textview_dayQuota.setText(data.getDayQuota().toString());
            } else {
                textview_dayQuota.setText("20000");
            }

            //单笔交易限额
            table_single_order.clearValueWeight();
            if (null != data.getSingleQuota()) {
                table_single_order.addTextBtn("单笔交易限额", "人民币" + data.getSingleQuota().toString(), "变更", getContext().getResources().getColor(R.color.boc_main_button_color));
            } else {
                table_single_order.addTextBtn("单笔交易限额", "人民币5000", "变更", getContext().getResources().getColor(R.color.boc_main_button_color));
            }

            //跳转model
            final HceTransactionViewModel hceTransactionViewModel = new HceTransactionViewModel();

            if (null != data) {
                hceTransactionViewModel.setDeviceNo(HceUtil.getDeviceId(getContext()));
                hceTransactionViewModel.setMasterCardNo(data.getMasterCardNo());
                hceTransactionViewModel.setSlaveCardNo(data.getSlaveCardNo());
                if (null != data.getSingleQuota()) {
                    hceTransactionViewModel.setSingleQuota(data.getSingleQuota().toString());
                }

                if (null != data.getDayQuota()) {
                    hceTransactionViewModel.setPerDayQuota(data.getDayQuota().toString());
                }

                hceTransactionViewModel.setConversationId(mHceCardListFragment.getPresenter().getConversationId());
                hceTransactionViewModel.setFrom((HceTransactionViewModel.From.CARD_LIST));

                CardBrandModel cardBrandModel = new CardBrandModel();
                cardBrandModel.setAppType(CardBrandStran(data.getCardBrand()));
                hceTransactionViewModel.setCardBrandModel(cardBrandModel);
            }


            //限额设置
            table_single_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHceCardListFragment.start(HceQuotaSettingFragment.newInstance(hceTransactionViewModel));
                }
            });

            //解挂
            textview_unloss.setVisibility(View.GONE);
            if("6".equals(data.getCardStatus())){//卡状态是 已挂失，
                textview_unloss.setVisibility(View.VISIBLE);
            }
            textview_unloss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHceCardListFragment.start(HceCardUnlossConfirmFragment.newInstance(hceTransactionViewModel));
                }
            });

            //激活
            textview_activie.setVisibility(View.GONE);
            if("3".equals(data.getCardStatus())){//未激活
                textview_activie.setVisibility(View.VISIBLE);
            }
            textview_activie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHceCardListFragment.start(HceActivateConfirmFragment.newInstance(hceTransactionViewModel));
                }
            });

            //注销
            textview_zhuxiao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ConfirmDialog dialog = new ConfirmDialog(getContext());
                    dialog.setMessage("是否确认注销这张云闪付卡？");
                    dialog.setLeftButton("取消");
                    dialog.setRightButton("确定");
                    dialog.setButtonClickInterface(new ConfirmDialog.OnButtonClickInterface() {
                        @Override
                        public void onLeftClick(ConfirmDialog warnDialog) {
                            dialog.cancel();
                        }

                        @Override
                        public void onRightClick(ConfirmDialog warnDialog) {
                            //注销接口调用
                            String conversationId = mHceCardListFragment.getPresenter().getConversationId();
                            mHceCardListFragment.getPresenter().PsnHCEQuickPassCancel(conversationId, data.getMasterCardNo(), data.getSlaveCardNo());
                        }
                    });
                    dialog.show();
                }
            });

            llContainerContent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.linearLayout_account_info) {
                ((CardStackView) itemView.getParent()).performItemClick(ItemViewHolder.this);
            }
        }


        //hce卡状态对应关系
        private Drawable hceCardStatus(String value) {
            if ("6".equals(value)) {
                return getContext().getResources().getDrawable(R.drawable.loss);
            } else if ("3".equals(value)) {
                return  getContext().getResources().getDrawable(R.drawable.unactive);
            } else if ("4".equals(value)) {//正常boc_view_banner_page_indicator
                return getContext().getResources().getDrawable(R.drawable.boc_view_banner_page_indicator);
            } else if ("7".equals(value)) {//已注销
                return getContext().getResources().getDrawable(R.drawable.boc_view_banner_page_indicator);
            }
            return getContext().getResources().getDrawable(R.drawable.boc_view_banner_page_indicator);
        }

        //hce卡品牌对应关系
        private Drawable hceCardBland(String value) {
            if ("01".equals(value) || "11".equals(value)) {
                return getContext().getResources().getDrawable(R.drawable.mastercard);
            } else if ("03".equals(value)) {
                return getContext().getResources().getDrawable(R.drawable.visa);
            } else if ("13".equals(value)) {
                return getContext().getResources().getDrawable(R.drawable.visa);
            } else if ("02".equals(value)) {
                return getContext().getResources().getDrawable(R.drawable.unionpay);
            } else if ("12".equals(value)) {
                return getContext().getResources().getDrawable(R.drawable.unionpay);
            }
            return getContext().getResources().getDrawable(R.drawable.boc_view_banner_page_indicator);
        }


        public HceConstants.CardOrg CardBrandStran(String cardOrg) {

            if ("01".equals(cardOrg)) {
                return HceConstants.CardOrg.MasterCard;
            } else if ("02".equals(cardOrg)) {
                return HceConstants.CardOrg.PBOC_Credit;
            } else if ("12".equals(cardOrg)) {
                return HceConstants.CardOrg.PBOC_Debit;
            } else if ("03".equals(cardOrg)) {
                return HceConstants.CardOrg.VISA;
            }
            return HceConstants.CardOrg.VISA;
        }


    }
}
