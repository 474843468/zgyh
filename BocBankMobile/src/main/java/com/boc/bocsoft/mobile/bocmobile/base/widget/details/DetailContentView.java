package com.boc.bocsoft.mobile.bocmobile.base.widget.details;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;

/**
 * 详情组件，不包含头部和底部按钮
 * Created by Administrator on 2016/5/21.
 */
public class DetailContentView extends LinearLayout {
    private Context mContext;
    private LinearLayout detail_content;
    private DetailTableRowButton tableBtn;

    public DetailContentView(Context context) {
        this(context, null, 0);
    }


    public DetailContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        detail_content = new LinearLayout(mContext);
        detail_content.setLayoutParams(lp);
        detail_content.setOrientation(LinearLayout.VERTICAL);
        this.addView(detail_content);
    }

    /**
     *
     *
     **/
    public DetailContentView addDetailTableHead(String title, String sum, String name, String value) {
        DetailTableHead tableHead = new DetailTableHead(mContext);
        tableHead.updateData(title, sum);
        tableHead.setTableRow(name, value);
        detail_content.addView(tableHead);
        return this;
    }

    /**
     * 添加详情item。（不带分组和按钮）
     *
     * @param name
     * @param value
     */
    public DetailContentView addDetailRow(String name, String value) {
        return addDetailRow(name, value, LayoutParams.WRAP_CONTENT);
    }

    public DetailContentView addDetailRow(String name, String value, int height) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.setBodyHeight(height);
        tableRow.updateData(name, value);
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 添加详情item。（不带分组和按钮）
     *
     * @param name
     * @param value
     */
    public DetailContentView addDetail(String name, String value) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setLineMargging(0, 0);
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 添加详情item
     *
     * @param view
     * @return
     */
    public DetailContentView addDetailRowView(View view) {
        detail_content.addView(view);
        return this;
    }

    /**
     * 添加详情item,不显示分割线。（不带分组和按钮）
     *
     * @param name
     * @param value
     */
    public DetailContentView addDetailRowNotLine(String name, String value) {
        return addDetailRowNotLine(name, value, LayoutParams.WRAP_CONTENT);
    }

    /**
     * 添加详情item,不显示所有分割线
     *
     * @param name
     * @param value
     */
    public DetailContentView addDetailRowNotAllLine(String name, String value) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.isShowDividerLine(false);
        tableRow.setRowLineVisable(false);
        detail_content.addView(tableRow);
        return this;
    }

    public DetailContentView addDetailRowNotLine(String name, String value, int height) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.setBodyHeight(height);
        tableRow.updateData(name, value);
        tableRow.setRowLineVisable(false);
        detail_content.addView(tableRow);
        return this;
    }


    /**
     * 添加详情item,显示文字按钮
     *
     * @param name:左侧详情标题
     * @param value:右侧详情数据
     * @param text:按钮文字
     * @param textColor:按钮文字颜色
     */
    public DetailContentView addTextBtnRow(String name, String value, String text, int textColor) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.addTextBtn(name, value, text, textColor);
        detail_content.addView(tableBtn);
        return this;
    }

    public DetailContentView addTextBtnRow(String name, String value, String text, int textColor, OnClickListener onClickListener) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.addTextBtn(name, value, text, textColor);
        tableBtn.setOnClickListener(onClickListener);
        detail_content.addView(tableBtn);
        return this;
    }

    /**
     * 添加详情item,显示图片按钮
     *
     * @param name：左侧详情标题
     * @param value：右侧详情数据
     * @param bgImg：图片按钮背景图，,图片来自Drawable
     */

    public DetailContentView addImgBtnRow(String name, String value, int bgImg, DetailTableRowButton.BtnCallback btnCallback) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.setOnclick(btnCallback);
        tableBtn.addImgBtn(name, value, bgImg);
        detail_content.addView(tableBtn);
        return this;
    }

    public DetailContentView addImgBtnRow(boolean isTrue, String name, String value, int bgImg, DetailTableRowButton.BtnCallback btnCallback) {
        return addImgBtnRow(isTrue, name, value, bgImg, LayoutParams.WRAP_CONTENT, btnCallback);
    }

    public DetailContentView addImgBtnRow(boolean isTrue, String name, String value, int bgImg, int height, DetailTableRowButton.BtnCallback btnCallback) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        if (isTrue)
            this.tableBtn = tableBtn;
        tableBtn.setBodyHeight(height);
        tableBtn.setOnclick(btnCallback);
        tableBtn.addImgBtn(name, value, bgImg);
        detail_content.addView(tableBtn);
        return this;
    }

    public DetailTableRowButton getDetailTableRowButton() {
        if (tableBtn == null) {
            DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
            this.tableBtn = tableBtn;
            detail_content.addView(tableBtn);
        }
        return tableBtn;
    }

    public DetailContentView updateImgBtnRow(String value) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);

        return this;
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
    public DetailContentView addTextAndImgBtn(String name, String value, String text, int textColor, int textBgColor, int bgImg) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.addTextAndImgBtn(name, value, text, textColor, textBgColor, bgImg);
        return this;
    }

    public DetailContentView addTextAndImgBtn(String name, String value, String text, int bgImg, OnClickListener onClickListener, OnClickListener onClickListener2) {
        DetailTableRowButton tableBtn = new DetailTableRowButton(mContext);
        tableBtn.tvBtn.setOnClickListener(onClickListener);
        tableBtn.ivBtn.setOnClickListener(onClickListener2);
        return addTextAndImgBtn(name, value, text, bgImg, R.color.boc_main_button_color, R.color.boc_main_bg_color);
    }

    /**
     * 添加详情，三个字段
     *
     * @param left   左边赋值
     * @param middle 中间赋值
     * @param right  右边赋值
     */
    public DetailContentView addDetailList(String left, String middle, String right) {
        DetailListView detalist = new DetailListView(mContext);
        detalist.setDataValue(left, middle, right);
        detail_content.addView(detalist);
        return this;
    }

    /**
     * 添加拨打电话组件
     *
     * @param name
     * @param value
     * @param btnText
     * @return
     */
    public DetailContentView addEnterButton(String name, String value, String btnText, OnClickListener onClickListener) {
        DetailTabRowEnterButton enterButton = new DetailTabRowEnterButton(mContext);
        enterButton.btnValue.setOnClickListener(onClickListener);
        enterButton.addRow(name, value, btnText);
        detail_content.addView(enterButton);
        return this;
    }

    /**
     * 添加交易明细title
     *
     * @param text
     * @return
     */
    public DetailContentView addAccountTitleHead(String text) {
        AccountDetailHead titleHead = new AccountDetailHead(mContext);
        titleHead.setTitle(text);
        detail_content.addView(titleHead);
        return this;
    }

    /**
     * 持仓详情条目项，内容单行显示，最大限制为20
     *
     * @param name
     * @param value
     * @return
     */
    public DetailContentView addTextCntent(String name, String value) {
        DetailTabRowTextButton textContent = new DetailTabRowTextButton(mContext);
        textContent.addTextAndValue(name, value);
        detail_content.addView(textContent);
        return this;
    }

    /**
     * 持仓详情条目项，内容单行显示，按钮字体表现为蓝色，在内容之后,优先右侧按钮显示
     *
     * @param name
     * @param value
     */
    public DetailTabRowTextButton addTextCntent(String name, String value, String button, OnClickListener clickListener) {
        DetailTabRowTextButton textButton = new DetailTabRowTextButton(mContext);
        textButton.addTextAndValue(name, value, button);
        textButton.tvButton.setOnClickListener(clickListener);
        textButton.setLineMargging(0, 0);
        detail_content.addView(textButton);
        return textButton;
    }

    /**
     * 持仓详情条目项，内容单行显示，按钮字体表现为蓝色，在内容之后,优先右侧按钮显示,基金需要显示分割线
     * @param name
     * @param value
     * @param button
     * @param isShowDevide
     * @param clickListener
     * @return
     */
    public DetailTabRowTextButton addTextCntent(String name, String value, String button,boolean isShowDevide, OnClickListener clickListener) {
        DetailTabRowTextButton textButton = new DetailTabRowTextButton(mContext);
        textButton.addTextAndValue(name, value, button);
        textButton.tvButton.setOnClickListener(clickListener);
        textButton.setLineMargging(0, 0);
        textButton.setShowDevide(isShowDevide);
        detail_content.addView(textButton);
        return textButton;
    }


//    =====================================下面是yx add==2016年11月16日 20:41:19===========

    /**
     * 添加详情item。（不带分组和按钮） 字段为空也需要显示标题
     *
     * @param name       标题
     * @param value      内容
     * @param isNullShow 是否空字符 也需要显示 整行view
     * @date 2016年11月16日 20:29:40
     * @author yx
     */
    public DetailContentView addDetailRow(String name, String value, boolean isNullShow) {
        return addDetailRow(name, value, LayoutParams.WRAP_CONTENT, isNullShow, false);
    }

    public DetailContentView addDetailRowWithNullShow(String name, String value) {
        return addDetailRow(name, value, LayoutParams.WRAP_CONTENT,true);
    }

    /**
     * 添加详情item。（不带分组和按钮） 字段为空也需要显示标题
     *
     * @param name             标题
     * @param value            内容
     * @param isNullShow       是否空字符 也需要显示 整行view
     * @param isShowBottomLine 是否显示底部分割线
     * @date 2016年11月16日 20:29:40
     * @author yx
     */
    public DetailContentView addDetailRow(String name, String value, boolean isNullShow, boolean isShowBottomLine) {
        return addDetailRow(name, value, LayoutParams.WRAP_CONTENT, isNullShow, isShowBottomLine);
    }

    /**
     * 添加详情item。（不带分组和按钮） 字段为空也需要显示标题
     *
     * @param name             标题
     * @param value            内容
     * @param height           高度自适应
     * @param isNullShow       是否空字符 也需要显示 正行view
     * @param isShowBottomLine 是否显示底部分割线
     * @date 2016年11月16日 20:29:40
     * @author yx
     */
    public DetailContentView addDetailRow(String name, String value, int height, boolean isNullShow, boolean isShowBottomLine) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.setBodyHeight(height);
        tableRow.updateData(name, value, isNullShow);
        if (!isShowBottomLine) {
            tableRow.setRowLineVisable(false);
            tableRow.isShowDividerLine(false);
        } else {
            tableRow.setRowLineVisable(true);
        }
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 添加详情item。（不带分组和按钮） 字段为空也需要显示标题
     *
     * @param name             标题
     * @param value            内容
     * @param height           高度自适应
     * @param isNullShow       是否空字符 也需要显示 正行view
     * @date 2016年11月16日 20:29:40
     * @author yx
     */
    public DetailContentView addDetailRow(String name, String value, int height, boolean isNullShow) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.setBodyHeight(height);
        tableRow.updateData(name, value, isNullShow);
        tableRow.isShowDividerLine(true);
        tableRow.setRowLineVisable(false);
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 添加详情item。（不带分组和按钮）{持仓专用}
     *
     * @param name
     * @param value
     * @date 2016年11月13日 17:10:40
     * @author yx
     */
    public DetailContentView addDetail(String name, String value, boolean isShowLine) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setLineMargging(0, 0);
        if (!isShowLine) {
            tableRow.setRowLineVisable(false);
            tableRow.isShowDividerLine(false);
        } else {
            tableRow.setRowLineVisable(true);
        }
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 添加详情item。（不带分组和按钮）{持仓详情-返回DetailTableRow，可进行修改值}
     *
     * @param name
     * @param value
     * @date 2016年11月15日 18:21:09
     * @author yx
     */
    public DetailTableRow addTableRowDetail(String name, String value, boolean isShowLine) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setLineMargging(0, 0);
        if (!isShowLine) {
            tableRow.setRowLineVisable(false);
            tableRow.isShowDividerLine(false);
        }
        detail_content.addView(tableRow);
        return tableRow;
    }

    /**
     * 添加详情item,不显示分割线。（不带分组和按钮）{字符为空的时候 view还显示}
     *
     * @param name
     * @param value
     * @author yx
     * @date 2016-09-24 16:12:22
     */
    public DetailContentView addDetailRowNotLineBg(String name, String value, int mviewcolor, int mLeftColor) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value, true);
        tableRow.setRowLineVisable(false);
        tableRow.isShowDividerLine(false);
        tableRow.setRootViewBackground(mviewcolor);
        if (mLeftColor != 0) {
            tableRow.setValueColor(mLeftColor);
        }
        LayoutParams lp = new LayoutParams(detail_content.getLayoutParams());
        lp.setMargins(80, 0, 0, 0);
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 添加详情item,不显示分割线。（不带分组和按钮）可以自定义 标题的样式
     *
     * @param name
     * @param value
     * @author yx
     * @date 2016年11月11日 15:32:52
     */

    public DetailContentView addDetailRowNotLineBgSpannable(SpannableString name, String value, int mviewcolor, int mLeftColor) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setRowLineVisable(false);
        tableRow.isShowDividerLine(false);
        tableRow.setRootViewBackground(mviewcolor);
        if (mLeftColor != 0) {
            tableRow.setValueColor(mLeftColor);
        }
        LayoutParams lp = new LayoutParams(detail_content.getLayoutParams());
        lp.setMargins(80, 0, 0, 0);
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 添加详情item,（不带分组和按钮）可以自定义 标题的样式
     *
     * @param name              标题
     * @param value             内容
     * @param mviewcolor        view 背景颜色
     * @param mLeftColor        内容字体颜色
     * @param isShowDividerLine 是否显示分割线
     * @return
     */

    public DetailContentView addDetailRowNotLineBgSpannable(SpannableString name, String value, int mviewcolor, int mLeftColor, boolean isShowDividerLine) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(name, value);
        tableRow.setRowLineVisable(false);
        tableRow.isShowDividerLine(isShowDividerLine);
        tableRow.setRootViewBackground(mviewcolor);
        if (mLeftColor != 0) {
            tableRow.setValueColor(mLeftColor);
        }
        LayoutParams lp = new LayoutParams(detail_content.getLayoutParams());
        lp.setMargins(80, 0, 0, 0);
        detail_content.addView(tableRow);
        return this;
    }

    /**
     * 持仓详情条目项，内容单行显示，按钮字体表现为蓝色，在内容之后,优先右侧按钮显示{例如：产品名称+产品代码}
     *
     * @param name
     * @param value
     * @author yx
     * @date 2016年11月9日 19:19:51
     */
    public DetailFinancialTabRowTextButton addTextAndButtonContent(String name, String value, String button) {
        DetailFinancialTabRowTextButton textValueButton = new DetailFinancialTabRowTextButton(mContext);
        textValueButton.setRightTvListener(new DetailFinancialTabRowTextButton.DetailRightTvOnClickListener() {
            @Override
            public void onClickRightTextView() {
                if (mListener != null) {
                    mListener.onClickRightTextView();
                    LogUtils.d("yx------------onClickSpan");
                }
            }
        });
        textValueButton.addTextAndValue(name, value, button);
        detail_content.addView(textValueButton);
        return textValueButton;
    }

    // 右侧蓝色字体点击事件
    private DetailContentRightTvOnClickListener mListener;

    /***
     * 右侧蓝色按钮 点击监听事件
     */
    public interface DetailContentRightTvOnClickListener {
        void onClickRightTextView();

    }

    /**
     * 设置监听
     *
     * @param onClickListener
     */
    public void setRightTvListener(DetailContentRightTvOnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    /**
     * 基金持仓详情条目：title + {基金名称+基金代码} + button
     * @param title
     * @param leftValue
     * @param rightVAlue
     * @param button
     * @return
     */
    public DetailThreeTableRowTextButton addTextAndButtonContent(String title,String leftValue,String rightVAlue,String button){
        DetailThreeTableRowTextButton textButton = new DetailThreeTableRowTextButton(mContext);
        textButton.addTextandView(title,leftValue,rightVAlue,button);
        detail_content.addView(textButton);
        return textButton;
    }

    public DetailTabRowTextButtonWithHint addTextAndButtonContent(String title,String value,String button,DetailTabRowTextButtonWithHint.DetailTvButtonOnClickListener detailTvOnClickListener){
        DetailTabRowTextButtonWithHint textButtonWithHint = new DetailTabRowTextButtonWithHint(mContext);
        textButtonWithHint.addTextandView(title,value,button,detailTvOnClickListener);
        detail_content.addView(textButtonWithHint);
        return textButtonWithHint;
    }
}
