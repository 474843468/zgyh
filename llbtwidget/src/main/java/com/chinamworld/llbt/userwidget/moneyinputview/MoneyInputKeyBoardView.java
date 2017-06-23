package com.chinamworld.llbt.userwidget.moneyinputview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.chinamworld.llbtwidget.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 　金额输入处理
 */
public class MoneyInputKeyBoardView implements View.OnClickListener {

    private final List<String> noPointCurrencyList = new ArrayList<>();
    protected View rootView;
    protected TextView payKeyboardOne;
    protected TextView payKeyboardTwo;
    protected TextView payKeyboardThree;
    protected TextView payKeyboardFour;
    protected TextView payKeyboardFive;
    protected TextView payKeyboardSex;
    protected TextView payKeyboardSeven;
    protected TextView payKeyboardEight;
    protected TextView payKeyboardNine;
    protected TextView payKeyboardPoint;
    protected TextView payKeyboardZero;
    protected ImageView payKeyboardDel;
    private StringBuffer mSBuffer = new StringBuffer();
    private String appearText = "";
    private OnKeyBoardChangedListener keyBoardChangedListener;
    private Context mContext;
    private long currentTime;

    private int maxLeftNumber = 13;
    private int maxRightNumber = 2;

    public interface OnKeyBoardChangedListener {

        void onNumberChanged(String number);

        void onKeyBoardCanceled(String formatNumber);
    }

    public MoneyInputKeyBoardView(Context context) {
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater l = LayoutInflater.from(mContext);
        rootView = l.inflate(R.layout.boc_money_key_layout, null);

        payKeyboardOne = (TextView) rootView.findViewById(R.id.pay_keyboard_one);
        payKeyboardOne.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardTwo = (TextView) rootView.findViewById(R.id.pay_keyboard_two);
        payKeyboardTwo.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardThree = (TextView) rootView.findViewById(R.id.pay_keyboard_three);
        payKeyboardThree.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardFour = (TextView) rootView.findViewById(R.id.pay_keyboard_four);
        payKeyboardFour.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardFive = (TextView) rootView.findViewById(R.id.pay_keyboard_five);
        payKeyboardFive.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardSex = (TextView) rootView.findViewById(R.id.pay_keyboard_sex);
        payKeyboardSex.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardSeven = (TextView) rootView.findViewById(R.id.pay_keyboard_seven);
        payKeyboardSeven.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardEight = (TextView) rootView.findViewById(R.id.pay_keyboard_eight);
        payKeyboardEight.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardNine = (TextView) rootView.findViewById(R.id.pay_keyboard_nine);
        payKeyboardNine.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardPoint = (TextView) rootView.findViewById(R.id.pay_keyboard_point);
        payKeyboardPoint.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardZero = (TextView) rootView.findViewById(R.id.pay_keyboard_zero);
        payKeyboardZero.setOnClickListener(MoneyInputKeyBoardView.this);
        payKeyboardDel = (ImageView) rootView.findViewById(R.id.pay_keyboard_del);
        payKeyboardDel.setOnClickListener(MoneyInputKeyBoardView.this);
        //长按删除键
        payKeyboardDel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                parseActionType(KeyboardEnum.longdel);
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if ((System.currentTimeMillis() - currentTime < 150)) {
            return;
        }
        currentTime = System.currentTimeMillis();
        if (view.getId() == R.id.pay_keyboard_one) {
            parseActionType(KeyboardEnum.one);
        } else if (view.getId() == R.id.pay_keyboard_two) {
            parseActionType(KeyboardEnum.two);
        } else if (view.getId() == R.id.pay_keyboard_three) {
            parseActionType(KeyboardEnum.three);
        } else if (view.getId() == R.id.pay_keyboard_four) {
            parseActionType(KeyboardEnum.four);
        } else if (view.getId() == R.id.pay_keyboard_five) {
            parseActionType(KeyboardEnum.five);
        } else if (view.getId() == R.id.pay_keyboard_sex) {
            parseActionType(KeyboardEnum.sex);
        } else if (view.getId() == R.id.pay_keyboard_seven) {
            parseActionType(KeyboardEnum.seven);
        } else if (view.getId() == R.id.pay_keyboard_eight) {
            parseActionType(KeyboardEnum.eight);
        } else if (view.getId() == R.id.pay_keyboard_nine) {
            parseActionType(KeyboardEnum.nine);
        } else if (view.getId() == R.id.pay_keyboard_point) {
            String text = mSBuffer.toString();
            if (null != text && text.contains(".")) {
                return;
            }
            if (TextUtils.isEmpty(text)) {
                mSBuffer.append("0.");
                appearText = appearText + "0.";
                if (keyBoardChangedListener != null) {
                    keyBoardChangedListener.onNumberChanged(appearText);
                }
            } else {
                parseActionType(KeyboardEnum.point);
            }
        } else if (view.getId() == R.id.pay_keyboard_zero) {
            String str = mSBuffer.toString();
            if (str.length() == 1 && str.equals("0")) {
                return;
            } else {
                parseActionType(KeyboardEnum.zero);
            }
        } else if (view.getId() == R.id.pay_keyboard_del) {
            parseActionType(KeyboardEnum.del);
        }
    }

    /**
     * 　设置左边金额最大位数
     */
    public void setMaxLeftNumber(int maxLeftNumber) {
        this.maxLeftNumber = maxLeftNumber;
    }

    /**
     * 　设置右边边金额最大位数
     */
    public void setMaxRightNumber(int maxRightNumber) {
        this.maxRightNumber = maxRightNumber;
    }

    /**
     * 　检查输入数据是否溢出
     */
    private boolean checkNumberLength(String num) {
        boolean ret = true;
        String left = "";
        String right = "";
        int index = num.indexOf('.');
        if (index > 0) {
            left = num.substring(0, num.indexOf('.'));
            if (index < num.length() - 1) {
                right = num.substring(num.indexOf('.') + 1);
            }
        } else {
            left = num;
        }

        if (left.length() > maxLeftNumber) {
            char point = num.charAt(maxLeftNumber);
            if (point != '.') {
                ret = false;
            } else if (right.length() > maxRightNumber) {
                ret = false;
            }
        } else if (right.length() > maxRightNumber) {
            ret = false;
        }
        return ret;
    }

    public void setOnKeyBoardChangedListener(OnKeyBoardChangedListener lis) {
        keyBoardChangedListener = lis;
    }
    private final static DecimalFormat format_nopoint = new DecimalFormat("###,##0");
    private final static DecimalFormat format_rmb = new DecimalFormat("###,##0.00");

    public String getNormalMoneyFormat(String money) {
        if (money == null || money.length() == 0) {
            return "";
        }
        return money.replace(",", "");
    }
    public String transMoneyFormat(String money, String currency) {
        if ("null".equals(money)) {
            return "-";
        }
        if (money == null || money.length() == 0) {
            return "";
        }
        // 逻辑 ： 日元不带小数位置 其他保留两位小数 整数部分 3位一个千分
        BigDecimal bigDecimal;
        try {
            bigDecimal = new BigDecimal(money);
        } catch (NumberFormatException exp) {
            return money;
        }
        return transMoneyFormat(bigDecimal, currency);
    }

    public String transMoneyFormat(BigDecimal money, String currency) {
        if (money == null) {
            return "";
        }
        DecimalFormat format;
        if (noPointCurrencyList.contains(currency)) {
            format = format_nopoint;
        } else {
            format = format_rmb;
        }
        format.setRoundingMode(RoundingMode.DOWN);//格式化舍弃多余小数时不四舍五入，而是直接舍弃
        try {
            return format.format(money);
        } catch (IllegalArgumentException exp) {
            return money.toPlainString();
        }
    }

    /**
     * 　格式化输入金额
     */
    public String getFormatNumber() {
        appearText = transMoneyFormat(mSBuffer.toString(), "0");
        mSBuffer.delete(0, mSBuffer.length());
        mSBuffer.append(getNormalMoneyFormat(appearText));
        return appearText;
    }

    /**
     * 　获取实际金额
     */
    public String getInputMoney() {
        return mSBuffer.toString();
    }

    /**
     * 　设置金额
     */
    public void setInputMoney(String money) {
        clear();
        mSBuffer.append(money);
        keyBoardChangedListener.onNumberChanged(getFormatNumber());
    }

    public OnKeyBoardChangedListener getOnKeyBoardChangedListener() {
        return keyBoardChangedListener;
    }

    public View getKeyBoardView() {
        return rootView;
    }


    private void parseActionType(KeyboardEnum type) {
        if (type.getType() == KeyboardEnum.ActionEnum.add) {
            String num = mSBuffer.toString() + type.getValue();
            if (checkNumberLength(num)) {
                mSBuffer.append(type.getValue());
                appearText = appearText + type.getValue();
                if (keyBoardChangedListener != null) {
                    keyBoardChangedListener.onNumberChanged(appearText);
                }
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.delete) {
            if (mSBuffer.length() > 0) {
                appearText = appearText.substring(0, appearText.length() - 1);
                mSBuffer = mSBuffer.delete(0, mSBuffer.length());
                mSBuffer.append(getNormalMoneyFormat(appearText));
                if (keyBoardChangedListener != null) {
                    keyBoardChangedListener.onNumberChanged(appearText);
                }
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.longClick) {
            if (keyBoardChangedListener != null) {
                clear();
            }
        }
    }

    public void clear() {
        mSBuffer.delete(0, mSBuffer.length());
        appearText = "";
        keyBoardChangedListener.onNumberChanged(appearText);
    }
}
