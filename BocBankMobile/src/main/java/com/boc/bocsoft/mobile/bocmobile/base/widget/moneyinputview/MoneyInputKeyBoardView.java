package com.boc.bocsoft.mobile.bocmobile.base.widget.moneyinputview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 　金额输入处理
 */
public class MoneyInputKeyBoardView implements View.OnClickListener {

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
    //private String appearText = "";
    private OnKeyBoardChangedListener keyBoardChangedListener;
    private Context mContext;
    private long currentTime;

    private int maxLeftNumber = 13;
    private int maxRightNumber = 2;
    private String currency = "001";
    private DecimalFormat format = new DecimalFormat("#####0.00");
    private DecimalFormat formatNoPoint = new DecimalFormat("#####0");

    private boolean tripZero = false;

    public interface OnKeyBoardChangedListener {

        void onNumberChanged(String number);

        void onKeyBoardCanceled(String formatNumber);
    }

    public MoneyInputKeyBoardView(Context context) {
        mContext = context;
        initView();
    }

    private void initView() {
        rootView = LayoutInflater.from(mContext).inflate(R.layout.boc_money_key_layout, null);

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
            if (maxRightNumber <= 0) {
                return;
            }
            String text = mSBuffer.toString();
            if (null != text && text.contains(".")) {
                return;
            }
            if (TextUtils.isEmpty(text)) {
                mSBuffer.append("0.");
                //appearText = appearText + "0.";
                if (keyBoardChangedListener != null) {
                    keyBoardChangedListener.onNumberChanged(mSBuffer.toString());
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        if (StringUtils.isEmptyOrNull(currency)) {
            return;
        }

        this.currency = currency;
        if (ApplicationConst.currencyCodeNoPoint.contains(currency)) {
            maxRightNumber = 0;
        } else {
            maxRightNumber = 2;
        }
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

    /**
     * 　格式化输入金额
     */
    public String getFormatNumber() {
        String str = mSBuffer.toString();
        if (tripZero) {
            if (StringUtils.isEmpty(str)) {
                return "";
            }
            return new BigDecimal(str).stripTrailingZeros().toPlainString();
        }
        //appearText = MoneyUtils.transMoneyFormat(str, currency);
        //mSBuffer.delete(0, mSBuffer.length());
        //mSBuffer.append(MoneyUtils.getNormalMoneyFormat(appearText));
        return MoneyUtils.transMoneyFormat(str, maxLeftNumber, maxRightNumber, tripZero);
    }

    /**
     * 　获取实际金额
     */
    public String getInputMoney() {
        String str;
        str = tripZero ? StringUtils.subZeroAndDot(mSBuffer.toString())
                : MoneyUtils.transMoneyFormat(mSBuffer.toString(), maxLeftNumber, maxRightNumber,
                        tripZero);
        clearBuffer();
        mSBuffer.append(MoneyUtils.getNormalMoneyFormat(str));
        return mSBuffer.toString();
    }

    /**
     * 　设置金额
     */
    public void setInputMoney(String money) {
        clearBuffer();
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
                //appearText = appearText + type.getValue();
                if (keyBoardChangedListener != null) {
                    keyBoardChangedListener.onNumberChanged(mSBuffer.toString());
                }
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.delete) {
            if (mSBuffer.length() > 0) {
                //appearText = appearText.substring(0, appearText.length() - 1);
                mSBuffer = mSBuffer.delete(mSBuffer.length() - 1, mSBuffer.length());
                //mSBuffer.append(MoneyUtils.getNormalMoneyFormat(appearText));
                if (keyBoardChangedListener != null) {
                    keyBoardChangedListener.onNumberChanged(mSBuffer.toString());
                }
            }
        } else if (type.getType() == KeyboardEnum.ActionEnum.longClick) {
            if (keyBoardChangedListener != null) {
                clear();
            }
        }
    }

    public void clear() {
        clearBuffer();
        //appearText = "";
        keyBoardChangedListener.onNumberChanged(mSBuffer.toString());
    }

    private void clearBuffer() {mSBuffer.delete(0, mSBuffer.length());}

    public void setTripZero(boolean tripZero) {
        this.tripZero = tripZero;
    }
}
