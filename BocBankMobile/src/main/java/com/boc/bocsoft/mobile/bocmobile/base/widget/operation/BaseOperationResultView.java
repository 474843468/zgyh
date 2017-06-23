package com.boc.bocsoft.mobile.bocmobile.base.widget.operation;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.buyprocedure.BuyProcedureWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowButton;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.flowchart.BuyRedeemFlowView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead.Status;

/**
 * 操作结果组件，包含头部和底部按钮
 * Created by niuguobin on 2016/5/23.
 */
public class BaseOperationResultView extends LinearLayout {

    protected View dividerTop;
    protected TextView txtTitle;
    protected LinearLayout llBottomParent;
    protected LinearLayout layoutProcedure;
    //横向的步骤图
    protected BuyProcedureWidget view_buy_procedure;
    //竖着的步骤图
//    protected BuyRedeemWidget view_buy_redeem;
    protected BuyRedeemFlowView view_buy_redeem;

    //赎回流程 地步分割线
    protected View view_buyredeem_bottom_divider_line ;

    protected LinearLayout layoutLike;
    protected TextView titleTwo;
    protected GridView gridView;
    private Context context;
    private ViewGroup rootView;
    public OperationResultHead head;
    public OperationResultBottom bottom;
    public OperationResultDetail detail;
    // 可能需要的父控件
    private LinearLayout llBottomView;
    private TextView txtBottomTitle;
    private BtnCallback btnCallback;


    private int flag = 0;

    public BaseOperationResultView(Context context) {
        this(context, null);
    }

    public BaseOperationResultView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.boc_view_operation_resulr_base, this);
        dividerTop = rootView.findViewById(R.id.divider_top);
        head = (OperationResultHead) rootView.findViewById(R.id.head);
        detail = (OperationResultDetail) rootView.findViewById(R.id.detail);
        bottom = (OperationResultBottom) rootView.findViewById(R.id.bottom);
        llBottomView = (LinearLayout) rootView.findViewById(R.id.ll_bottom_parent);
        txtBottomTitle = (TextView) rootView.findViewById(R.id.txt_title);
        gridView = (GridView) rootView.findViewById(R.id.grid_view);
        layoutProcedure = (LinearLayout) rootView.findViewById(R.id.layout_procedure);
        view_buy_procedure = (BuyProcedureWidget) rootView.findViewById(R.id.view_buy_procedure);
        layoutLike = (LinearLayout) rootView.findViewById(R.id.layout_like);
        titleTwo = (TextView) rootView.findViewById(R.id.title_two);
        view_buy_redeem = (BuyRedeemFlowView) rootView.findViewById(R.id.view_buy_redeem);
        view_buyredeem_bottom_divider_line = (View) rootView.findViewById(R.id.view_buyredeem_bottom_divider_line);
        layoutLike.setVisibility(View.GONE);
    }

    /**
     * 理财操作步骤图表是否显示
     */
    public void setProcedureLayoutVisible(boolean visible) {
        if (visible) {
            layoutProcedure.setVisibility(View.VISIBLE);
        } else {
            layoutProcedure.setVisibility(View.GONE);
        }
    }

    public View getProcedureLayout() {
        return layoutProcedure;
    }

    /**
     * 理财专用
     *
     * @param visible            所在布局是否显示
     * @param isShowBuyProcedure 理财操作步骤图表是否显示  横向的
     * @date 2016年10月29日 14:54:55
     * @author yx
     */
    public void setProcedureLayoutVisible(boolean visible, boolean isShowBuyProcedure) {
        if (visible) {
            layoutProcedure.setVisibility(View.VISIBLE);
            if (isShowBuyProcedure) {
                view_buy_procedure.setVisibility(View.VISIBLE);
            } else {
                view_buy_procedure.setVisibility(View.GONE);
            }

        } else {
            layoutProcedure.setVisibility(View.GONE);
            view_buy_procedure.setVisibility(View.GONE);
        }
    }

    /**
     * 中银理财-赎回-结果界面使用功能
     * @param visible 是否显示 竖向的步骤图
     * @param mStrTitle 文本信息  title 数据集合
     * @param mStrValue 文本信息  value 数据集合
     * @param status 显示状态-即现在的流程走到那步
     * @date 2016年11月5日 17:09:17
     * @author yx
     */
    public void setViewBuyRedeemVisible(boolean visible, String[] mStrTitle, String[] mStrValue, BuyRedeemFlowView.CompleteRedeemStatusT status) {
        if (visible) {
            layoutProcedure.setVisibility(View.VISIBLE);
            view_buy_redeem.setVisibility(View.VISIBLE);
            view_buy_procedure.setVisibility(View.GONE);
            titleTwo.setVisibility(View.GONE);
            view_buyredeem_bottom_divider_line.setVisibility(View.VISIBLE);
            view_buy_redeem.setItemViewTitle(mStrTitle);
            view_buy_redeem.setItemViewValue(mStrValue);
            view_buy_redeem.setStatus(status);
        } else {
            layoutProcedure.setVisibility(View.GONE);
            view_buy_redeem.setVisibility(View.GONE);
            view_buyredeem_bottom_divider_line.setVisibility(View.GONE);
        }
    }

    /**
     * 理财副标题内容
     */
    public void setTitleTwo(String text) {
        titleTwo.setVisibility(View.VISIBLE);
        titleTwo.setText(text);
    }

    public void setTopDividerVisible(boolean visible) {
        dividerTop.setVisibility(visible ? VISIBLE : GONE);
    }

    @Deprecated
    public void updateButtonStyle() {
    }

    /**
     * 更新操作结果
     */
    public void updateHead(Status status, String text) {
        head.updateData(status, text);
    }

    /**
     * 更新操作结果二级头部
     *
     * @param isTrue:操作结果是否显示
     * @param text:操作结果描述
     * @date 2016-09-14 11:00:43
     */
    public void isShowInfo(boolean isTrue, String text) {
        head.isShowInfo(isTrue, text);
    }

    public void isShowOther(boolean isTrue, String text) {
        head.isShowOther(isTrue, text);
    }

    /**
     * 更新操作结果
     *
     * @param status       结果状态
     * @param statusDesc   结果描述
     * @param statusDetail 结果详情
     */
    public void updateHead(Status status, String statusDesc, String statusDetail) {
        head.updateData(status, statusDesc, statusDetail);
    }

    /**
     * 设置底部按钮点击事件
     */
    public void setBottomClickedListener(OnClickListener listener) {
        bottom.setClickable(true);
        bottom.setOnClickListener(listener);
    }

    /**
     * 设置是否显示 交易明细 title
     *
     * @param isShowDetailsTitle true 有title 点击展开明细，false 没有title 直接显示明细
     * @date 2016年10月9日 20:01:56
     * @author yx
     */
    public void setDetailsTitleIsShow(boolean isShowDetailsTitle) {
        detail.setDetailsTitleIsShow(isShowDetailsTitle);
    }

    /**
     * 设置详情visibility
     */
    public void setDetailVisibility(int visibility) {
        detail.setVisibility(visibility);
    }

    /**
     * 设置详情底部按钮visibility
     */
    public void setBodyBtnVisibility(int visibility) {
        llBottomView.setVisibility(visibility);
    }

    /**
     * 添加详情
     */
    public void addDetailRow(String name, String value) {
        detail.addDetailRow(name, value);
    }

    /**
     * 添加top详情
     *
     * @date 2016年10月13日 20:34:54
     * @author yx
     */
    public void addTopDetailRow(String name, String value) {
        detail.addTopDetailRow(name, value);
    }

    /**
     * @param isShowTopDetailRow 是否显示 top detailRow
     * @date 2016年10月13日 20:34:54
     * @author yx
     */
    public void isAddTopDetailRow(boolean isShowTopDetailRow) {
        detail.isAddTopDetailRow(isShowTopDetailRow);
    }

    public void addDetailRow(String name, String value, boolean isVisible, boolean isShow) {
        detail.addDetailRow(name, value, isVisible, isShow);
    }

    public void addDetailRowBtn(String name, String value, String text, int textColor, DetailTableRowButton.BtnCallback btnCallback) {
        detail.addDetailRowBtn(name, value, text, textColor, btnCallback);
    }

    /**
     * 添加详情下面的按钮 (方法已废弃)
     */
    @Deprecated
    public void addBodyBtn(String title, int image, String text) {
        llBottomView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题（可能需要）
     */
    public void setTitleNeedName(String text) {
        txtBottomTitle.setText(text);
    }

    /**
     * 设置"需要"标题的颜色（可能需要）
     *
     * @param colorId
     */
    public void setTitleNeedColor(int colorId) {
        txtBottomTitle.setTextColor(getResources().getColor(colorId));
    }

    /**
     * 添加内容的子条目
     */
    public void addContentItem(String name, int id) {
        isShowBottomInfo(true);
        EditChoiceWidget editChoiceWidget = new EditChoiceWidget(context);
        editChoiceWidget.getChoiceNameTextView().setTypeface(Typeface.DEFAULT_BOLD);
        editChoiceWidget.setChoiceTextName(name);
        editChoiceWidget.setNameWidth();
        editChoiceWidget.setBottomLineVisibility(true);
        llBottomView.addView(editChoiceWidget);
        editChoiceWidget.setId(id);
        editChoiceWidget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCallback.onClickListener(v);
            }
        });
    }

    /**
     * 添加内容的子条目(重载)
     */
    public void addContentItem(String name, OnClickListener listener) {
        isShowBottomInfo(true);
        EditChoiceWidget editChoiceWidget = new EditChoiceWidget(context);
        editChoiceWidget.getChoiceNameTextView().setTypeface(Typeface.DEFAULT_BOLD);
        editChoiceWidget.setChoiceTextName(name);
        editChoiceWidget.setNameWidth();
        editChoiceWidget.setBottomLineVisibility(true);
        llBottomView.addView(editChoiceWidget);
        editChoiceWidget.setOnClickListener(listener);
    }

    /**
     * 头部添加子信息
     */
    public void addHeadInfo(String name, String value) {
        DetailTableRow detailTableRow = new DetailTableRow(context);
        detailTableRow.updateData(name, value);
        detailTableRow.setRowLineVisable(true);
        detailTableRow.isShowDividerLine(false);
        head.isShowHeadInfo(true);
        head.getHeadInfoParent().addView(detailTableRow);
    }

    /**
     * 设置头部字信息的自定义布局
     *
     * @return
     */
    public LinearLayout getHeadInfoParent() {
        head.isShowHeadInfo(true);
        return head.getHeadInfoParent();
    }

    /**
     * 获取详情layout
     *
     * @return
     */
    public LinearLayout getLayoutDetailParent() {
        return detail.getLayoutDetailParent();
    }


    /**
     * 修改按钮文字
     */
    public void updateBottomBtnText(String text) {
        bottom.updateButton(text);
    }

    public void setOnLikeItemClickListner(AdapterView.OnItemClickListener listener) {
        gridView.setOnItemClickListener(listener);
    }

    /**
     * 添加详情下面的按钮回调函数
     */
    public interface BtnCallback {
        public void onClickListener(View v);
    }

    /**
     * 添加详情下面的按钮点击事件
     */
    public void setOnclick(BtnCallback btnCallback) {
        this.btnCallback = btnCallback;
    }

    /**
     * 底部返回首页的按钮点击事件
     */
    public void setgoHomeOnclick(OperationResultBottom.HomeBtnCallback goHomeBtnCallback) {
        bottom.setgoHomeOnclick(goHomeBtnCallback);
    }

    /**
     * 设置头部位置的提示信息
     *
     * @param name
     */
    public void setHeadInfo(String name) {
        head.isShowInfo(true, name);
    }

    public void setDetailsName(String name) {
        detail.setTextTitle(name);
    }

    public void setHeadTxtInfo() {
        head.setHeadTxtInfo();
    }

    /**
     * 设置是否显示底部信息（您可能需要）
     */
    public void isShowBottomInfo(boolean isVisible) {
        if (isVisible) {
            llBottomView.setVisibility(View.VISIBLE);
        } else {
            llBottomView.setVisibility(View.GONE);
        }
    }

    /**
     * 向详情区域添加指定view
     *
     * @param detailLayout
     */
    public void addDetailLayout(View detailLayout) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        detail.addDetailLayout(detailLayout, params);
    }

    public void setYouLikeAdapter(BaseAdapter adapter,AdapterView.OnItemClickListener listener) {
        gridView.setNumColumns(adapter.getCount());
        gridView.setLayoutParams(new LayoutParams(adapter.getCount() * getResources().getDimensionPixelOffset(R.dimen.boc_space_between_400px), ViewGroup.LayoutParams.WRAP_CONTENT));
        layoutLike.setVisibility(View.VISIBLE);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(listener);
    }

    public BuyProcedureWidget getBuyProcedureWidget(){
        return view_buy_procedure;
    }

}
