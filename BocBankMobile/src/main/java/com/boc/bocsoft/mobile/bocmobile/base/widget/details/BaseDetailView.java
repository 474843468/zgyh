package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 详情组件，包含头部和底部按钮
 * Created by niuguobin on 2016/5/21.
 */
public class BaseDetailView extends LinearLayout {

    protected DetailTableHead headView;
    protected DetailContentGroupView groupView;
    protected DetailContentView bodyView;
    protected DeatilsBottomTableButtom bottomView;

    private Context mContext;
    private View root_view;
    private BtnCallback btnCallback;

    public BaseDetailView(Context context) {
        this(context, null, 0);

    }

    public BaseDetailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    private void initView() {
        root_view = LayoutInflater.from(mContext).inflate(R.layout.boc_view_detail_base, this);
        headView = (DetailTableHead) root_view.findViewById(R.id.head_view);
        bodyView = (DetailContentView) root_view.findViewById(R.id.body_view);
        groupView = (DetailContentGroupView) root_view.findViewById(R.id.group_view);
        bottomView = (DeatilsBottomTableButtom) root_view.findViewById(R.id.btn_delete);
        bottomView.setOnclick(new DeatilsBottomTableButtom.BtnCallback() {
            @Override
            public void onClickListener() {
                if (btnCallback != null) {
                    btnCallback.onClickListener();
                }
            }
        });
    }

    /**
     * 设置头部显示内容
     *
     * @param title
     * @param sum
     */

    public void updateHeadData(String title, String sum) {
        headView.updateData(title, sum);
    }

    /**
     * 设置头部状态信息
     * @param text
     * @param color
     */
    public void updateHeadStatus(String text, int color) {
        headView.showHeadStatus(text, color);
    }

    /**
     * 设置头部详情信息
     * @param name
     * @param value
     */
    public void updateHeadDetail(String name,String value){
        headView.addDetail(name,value);
    }

    /**
     * 添加详情页底部按钮
     *
     * @param
     */
    public void addBottomBtn(String text, int color) {
        DeatilsBottomTableButtom detailBottom = new DeatilsBottomTableButtom(mContext);
        detailBottom.setButtonText(text,color);
    }

    /**
     * 设置分组标题
     * @param title
     */
    public void setGroupTitleText(String title){
        if(groupView.getVisibility()==GONE){
            groupView.setVisibility(View.VISIBLE);
        }
        groupView.setTitleText(title);
    }

    /**
     * 添加带分组详细信息
     * @param name
     * @param value
     */
    public void addGroupDetailsItem(String name,String value){
        groupView.addDetailRow(name,value);
    }

    /**
     * 添加带分组详细信息，不带分割线
     * @param name
     * @param value
     */
    public void addGroupDetailNotLine(String name,String value){
        groupView.addGroupDetailNotLine(name,value);
    }

    /**
     * 设置详情页底部按钮
     *
     * @param
     */
    public void updateBottonBtn(String text, int color){
        bottomView.setVisibility(VISIBLE);
        bottomView.setButtonText(text,color);
    }

    /**
     * 添加详情item，不带头部和按钮
     *
     * @param name
     * @param value
     */
    public void addDetailRow(String name, String value) {
        bodyView.addDetailRow(name, value);
    }

    public void addDetailRowWithNullShow(String name, String value) {
        bodyView.addDetailRowWithNullShow(name, value);
    }

    public void addDetailRow(String name, String value,boolean isNullShow) {
        bodyView.addDetailRow(name, value,isNullShow);
    }

    /**
     * 把view添加到详情item
     * @param view
     */
    public void addDetailRowView(View view){
        bodyView.addDetailRowView(view);
    }

    /**
     * 添加详情item，不带头部和按钮，不带分割线
     *
     * @param name
     * @param value
     */
    public void addDetailRowNotLine(String name, String value) {
        bodyView.addDetailRowNotLine(name, value);
    }

    /**
     * 添加详情item,显示文字按钮
     *
     * @param name:左侧详情标题
     * @param value:右侧详情数据
     * @param text:按钮文字
     * @param textColor:按钮文字颜色
     */
    public void addTextBtn(String name, String value, String text, int textColor) {
        bodyView.addTextBtnRow(name, value, text, textColor);
    }

    /**
     *
     * @param name
     * @param value
     * @param text
     */
    public void addTextBtn(String name, String value, String text) {
        bodyView.addTextBtnRow(name, value, text,0);
    }

    /**
     *  添加详情item,显示文字按钮,并监听按钮点击事件
     * @param name
     * @param value
     * @param text
     * @param textColor
     * @param onClickListener
     */
    public void addTextBtn(String name, String value, String text, int textColor, OnClickListener onClickListener) {
        bodyView.addTextBtnRow(name, value, text, textColor, onClickListener);
    }

    /**
     * 添加详情item,显示图片按钮
     *
     * @param name：左侧详情标题
     * @param value：右侧详情数据
     * @param bgImg：图片按钮背景图，,图片来自Drawable
     */
    public void addImgBtn(String name, String value, int bgImg, DetailTableRowButton.BtnCallback btnCallback) {
        bodyView.addImgBtnRow(name, value, bgImg, btnCallback);
    }

    /**
     * 添加详情item，文字按钮和图片按钮同时显示
     *
     * @param name：左侧详情标题
     * @param value：右侧详情数据
     * @param text：按钮文字
     * @param textColor：文字按钮字体颜色
     * @param textBgColor：文字按钮背景颜色
     * @param bgImg：图片按钮背景图，,图片来自Drawable
     */
    public void addTextAndImgBtn(String name, String value, String text, int textColor, int textBgColor, int bgImg) {
        bodyView.addTextAndImgBtn(name, value, text, textColor, textBgColor, bgImg);
    }

    /**
     * 是否显示底部按钮组件
     * @param isShow：false为不显示
     */
    public void isShowBottomView(boolean isShow){
        if(!isShow){
            bottomView.setVisibility(View.GONE);
        }
    }

    /**
     * 底部按钮回调函数
     */
    public interface BtnCallback {
        public void onClickListener();
    }

    /**
     * 底部按钮点击事件
     * @param btnCallback
     */
    public void setOnclick(BtnCallback btnCallback) {
        this.btnCallback = btnCallback;
    }

    /**
     * 增加右侧按钮（不居右显示）
     * @param name
     * @param value
     * @param button
     * @param clickListener
     */
    public void addTextCntent(String name, String value, String button, OnClickListener clickListener){
        bodyView.addTextCntent(name, value, button,false, clickListener);
    }

    /**
     * 增加右侧按钮（不居右显示）是否显示分割线
     * @param name
     * @param value
     * @param button
     * @param isShowDevide
     * @param clickListener
     */
    public void addTextCntent(String name, String value, String button, boolean isShowDevide,OnClickListener clickListener){
        bodyView.addTextCntent(name, value, button,isShowDevide, clickListener);
    }


}
