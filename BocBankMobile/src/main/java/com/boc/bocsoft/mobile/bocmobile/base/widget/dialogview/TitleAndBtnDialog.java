package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;


/**
 * 分为头部标题、中间内容、底部按钮三部分的dialog
 * setDialogBtnClickListener设置按钮监听
 * setNoticeContent设置提示内容
 * setTitle设置标题
 * setTitleBackground标题背景
 * isShowTitle是否显示头部标题
 * isShowBottomBtn是否显示底部按钮
 * setBtnName设置底部按钮的名称
 * setMiddleContentView设置中间内容view
 * setGravity提示语对齐方式
 *
 * @author gwluo
 */
public class TitleAndBtnDialog extends BaseDialog implements
        View.OnClickListener {
    private LinearLayout rootView;
    private LinearLayout ll_title;
    private LinearLayout ll_bottom;
    private FrameLayout fl_content;
    private TextView tv_title;
    private TextView tv_notice;
    private Button btn_left;
    private Button btn_right;
    private View view_line;

    // =========================使用方法，开始========================

    /**
     * 设置提示内容
     *
     * @param content
     * @return
     */
    public TitleAndBtnDialog setNoticeContent(String content) {
        tv_notice.setText(content);
        return this;
    }

    public TextView getTvTitle() {
        return tv_title;
    }

    public LinearLayout getLlTitle() {
        return ll_title;
    }

    public Button getBtnRight() {
        return btn_right;
    }

    public Button getBtnLeft() {
        return btn_left;
    }

    public void setBtn_right(Button btn_right) {
        this.btn_right = btn_right;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public TitleAndBtnDialog setTitle(String title) {
        tv_title.setText(title);
        return this;
    }

    /**
     * 设置标题背景
     *
     * @param color
     */
    public void setTitleBackground(int color) {
        ll_title.setBackgroundColor(color);
    }


    /**
     * 否显示头部标题
     *
     * @param isShow
     */
    public TitleAndBtnDialog isShowTitle(boolean isShow) {
        if (isShow) {
            ll_title.setVisibility(View.VISIBLE);
        } else {
            ll_title.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 是否显示底部view
     *
     * @param isShow
     */
    public TitleAndBtnDialog isShowBottomBtn(boolean isShow) {
        if (isShow) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置按钮名称，传入数组，长度1显示一个按钮，长度2显示两个按钮
     *
     * @param btnName
     */
    public TitleAndBtnDialog setBtnName(String[] btnName) {
        if (btnName == null) {
            isShowBottomBtn(false);
        } else {
            int length = btnName.length;
            if (length == 0) {
                ll_bottom.setVisibility(View.GONE);
            } else if (length == 1) {
                btn_right.setText(btnName[0]);
                btn_left.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
            } else if (length == 2) {
                btn_left.setText(btnName[0]);
                btn_right.setText(btnName[1]);
            } else {
                throw new RuntimeException(
                        "param btnName only support one or two words");
            }
        }
        return this;
    }

    /**
     * 设置中间的view
     *
     * @param view
     */
    public TitleAndBtnDialog setMiddleContentView(View view) {
        if (view != null) {
            fl_content.removeAllViews();
            fl_content.addView(view);
        }
        return this;
    }

    public void setGravity(int gravity) {
        tv_notice.setGravity(gravity);
    }

    /**
     * 设置按钮字体和背景点击颜色
     *
     * @param pressedTextColor 按下的颜色
     * @param normalTextColor  默认的颜色
     * @param pressedBtnBg     按下的背景颜色
     * @param normalBtnBg      默认的背景颜色
     */
    public void setBtnTextBgColor(final int pressedTextColor,
                                  final int normalTextColor, final int pressedBtnBg,
                                  final int normalBtnBg) {
        btn_right.setTextColor(normalTextColor);
        btn_right.setBackgroundColor(normalBtnBg);
        btn_right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_MOVE) {
                    btn_right.setTextColor(pressedTextColor);
                    btn_right.setBackgroundColor(pressedBtnBg);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_right.setTextColor(normalTextColor);
                    btn_right.setBackgroundColor(normalBtnBg);
                }
                return false;
            }
        });

        btn_left.setBackgroundColor(normalBtnBg);
        btn_left.setTextColor(normalTextColor);
        btn_left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_MOVE) {
                    btn_left.setBackgroundColor(pressedBtnBg);
                    btn_left.setTextColor(pressedTextColor);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_left.setBackgroundColor(normalBtnBg);
                    btn_left.setTextColor(normalTextColor);
                }
                return false;
            }
        });
    }

    public void setLeftBtnTextBgColor(final int pressedTextColor, final int normalTextColor,
                                      final int pressedBtnBg, final int normalBtnBg) {
        view_line.setVisibility(View.GONE);
        btn_left.setBackgroundColor(normalBtnBg);
        btn_left.setTextColor(normalTextColor);
        btn_left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_MOVE) {
                    btn_left.setBackgroundColor(pressedBtnBg);
                    btn_left.setTextColor(pressedTextColor);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_left.setBackgroundColor(normalBtnBg);
                    btn_left.setTextColor(normalTextColor);
                }
                return false;
            }
        });
    }

    public void setRightBtnTextBgColor(final int pressedTextColor, final int normalTextColor,
                                       final int pressedBtnBg, final int normalBtnBg) {
        view_line.setVisibility(View.GONE);
        btn_right.setTextColor(normalTextColor);
        btn_right.setBackgroundColor(normalBtnBg);
        btn_right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_MOVE) {
                    btn_right.setTextColor(pressedTextColor);
                    btn_right.setBackgroundColor(pressedBtnBg);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btn_right.setTextColor(normalTextColor);
                    btn_right.setBackgroundColor(normalBtnBg);
                }
                return false;
            }
        });
    }


    // =========================使用方法，结束========================

    // ===================继承此类，要复写的方法，开始=====================

    /**
     * 复写此方法，是否显示标题
     *
     * @return
     */
    protected boolean getTitleVisiable() {
        return false;
    }

    /**
     * 复写此方法,传入标题和btn中间显示的view
     *
     * @return
     */
    protected View getContentView() {
        return null;
    }

    /**
     * 复写此方法设置Button名称，返回数组中存放一个或者两个参数或不传参数，传入一个显示一个btn，传入两个显示两个btn,不传参数不显示按钮
     *
     * @return
     */
    protected String[] getBtnName() {
        String[] btnStrings = new String[2];
        btnStrings[0] = mContext.getString(R.string.boc_common_cancel);
        btnStrings[1] = mContext.getString(R.string.boc_common_confirm);
        return btnStrings;
    }

    // ===================继承此类，要复写的方法，结束=====================
    public TitleAndBtnDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View onAddContentView() {
        rootView = (LinearLayout) View.inflate(mContext,
                R.layout.boc_dialog_title_btn, null);
        return rootView;
    }

    public TextView getTvNotice() {
        return tv_notice;
    }

    @Override
    protected void initView() {
        ll_title = (LinearLayout) rootView.findViewById(R.id.ll_title);
        ll_bottom = (LinearLayout) rootView.findViewById(R.id.ll_bottom);
        fl_content = (FrameLayout) rootView.findViewById(R.id.fl_content);
        tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        tv_notice = (TextView) rootView.findViewById(R.id.tv_notice);
        btn_left = (Button) rootView.findViewById(R.id.btn_left);
        btn_right = (Button) rootView.findViewById(R.id.btn_right);
        view_line = (View) rootView.findViewById(R.id.view_line);
        if (getTitleVisiable()) {
            ll_title.setVisibility(View.VISIBLE);
        } else {
            ll_title.setVisibility(View.GONE);
        }
        setBtnName(getBtnName());
        setMiddleContentView(getContentView());
    }

    @Override
    protected void initData() {
        // if (getTitleVisiable()) {
        // ll_title.setVisibility(View.VISIBLE);
        // } else {
        // ll_title.setVisibility(View.GONE);
        // }
        // setBtnName(getBtnName());
        // setMiddleContentView(getContentView());
    }

    @Override
    protected void setListener() {
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (R.id.btn_left == v.getId()) {
            if (btnClickCallBack != null) {
                btnClickCallBack.onLeftBtnClick(v);
            }
        } else if (R.id.btn_right == v.getId()) {
            if (btnClickCallBack != null) {
                btnClickCallBack.onRightBtnClick(v);
            }
        }
    }

    private DialogBtnClickCallBack btnClickCallBack;

    /**
     * 设置按钮点击监听
     *
     * @param callBack
     */
    public void setDialogBtnClickListener(DialogBtnClickCallBack callBack) {
        btnClickCallBack = callBack;
    }

    public interface DialogBtnClickCallBack {
        public void onLeftBtnClick(View view);

        public void onRightBtnClick(View view);
    }

}
