package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.eshield;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.CFCAEditTextView;
import java.math.BigDecimal;
import java.text.DecimalFormat;


public class PassKeybroadPop extends Dialog implements OnClickListener {

    private static final int MAX_SIZE = 8;
    // 真实的输入值
    private String mContentText = "";

    private final static int KEYBROAD_HEIGHT = 315;
    public final static int AMOUNTTYPE_13_2 = 0;
    public final static int AMOUNTTYPE_15_2 = 1;
    public final static int AMOUNTTYPE_16_2 = 2;

    // 数字键盘布局
    private LinearLayout num_keybaord;
    // 字母键盘布局
    private LinearLayout letter_keybaord;

    private TextView textA, textB, textC, textD, textE, textF, textG, textH,
            textI, textJ, textK, textL, textM, textN, textO, textP, textQ,
            textR, textS, textT, textU, textV, textW, textX, textY, textZ;

    /**
     * 大写锁定键
     */

    private ImageView viewCapsLock1;

    private boolean isCapsLock;

    private long currentTime;
    private DecimalFormat dfLocal = new DecimalFormat("#0.00");
    private DecimalFormat dfShow = new DecimalFormat("#,##0.00");

    private CFCAEditTextView.SecurityKeyboardListener securityKeyboardListener;
    private View rootView;

    public interface InputCompleteListener {
        //输入完成
        public void onInputComplete();

        //输入文字
        public void textChanged(String value);
    }

    private InputCompleteListener inputCompleteListener;

    public void setInputCompleteListener(InputCompleteListener inputCompleteListener) {
        this.inputCompleteListener = inputCompleteListener;
    }

    public PassKeybroadPop(Context context) {
        super(context, R.style.dialog_no_dim);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesap_ca_key_board_layout);
        setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
        rootView = findViewById(R.id.keybaord_all);
        initView();
    }

    public void setSecurityKeyboardListener(CFCAEditTextView.SecurityKeyboardListener securityKeyboardListener) {
        this.securityKeyboardListener = securityKeyboardListener;
    }

    private void initView() {

        findViewById(R.id.key_0).setOnClickListener(this);
        findViewById(R.id.key_1).setOnClickListener(this);
        findViewById(R.id.key_2).setOnClickListener(this);
        findViewById(R.id.key_3).setOnClickListener(this);
        findViewById(R.id.key_4).setOnClickListener(this);
        findViewById(R.id.key_5).setOnClickListener(this);
        findViewById(R.id.key_6).setOnClickListener(this);
        findViewById(R.id.key_7).setOnClickListener(this);
        findViewById(R.id.key_8).setOnClickListener(this);
        findViewById(R.id.key_9).setOnClickListener(this);

        textA = (TextView) findViewById(R.id.key_a);
        textB = (TextView) findViewById(R.id.key_b);
        textC = (TextView) findViewById(R.id.key_c);
        textD = (TextView) findViewById(R.id.key_d);
        textE = (TextView) findViewById(R.id.key_e);
        textF = (TextView) findViewById(R.id.key_f);
        textG = (TextView) findViewById(R.id.key_g);

        textH = (TextView) findViewById(R.id.key_h);
        textI = (TextView) findViewById(R.id.key_i);
        textJ = (TextView) findViewById(R.id.key_j);
        textK = (TextView) findViewById(R.id.key_k);
        textL = (TextView) findViewById(R.id.key_l);
        textM = (TextView) findViewById(R.id.key_m);
        textN = (TextView) findViewById(R.id.key_n);

        textO = (TextView) findViewById(R.id.key_o);
        textP = (TextView) findViewById(R.id.key_p);
        textQ = (TextView) findViewById(R.id.key_q);
        textR = (TextView) findViewById(R.id.key_r);
        textS = (TextView) findViewById(R.id.key_s);
        textT = (TextView) findViewById(R.id.key_t);

        textU = (TextView) findViewById(R.id.key_u);
        textV = (TextView) findViewById(R.id.key_v);
        textW = (TextView) findViewById(R.id.key_w);
        textX = (TextView) findViewById(R.id.key_x);
        textY = (TextView) findViewById(R.id.key_y);
        textZ = (TextView) findViewById(R.id.key_z);

        textA.setOnClickListener(this);
        textB.setOnClickListener(this);
        textC.setOnClickListener(this);
        textD.setOnClickListener(this);
        textE.setOnClickListener(this);
        textF.setOnClickListener(this);
        textG.setOnClickListener(this);
        textH.setOnClickListener(this);
        textI.setOnClickListener(this);
        textJ.setOnClickListener(this);

        textK.setOnClickListener(this);
        textL.setOnClickListener(this);
        textM.setOnClickListener(this);
        textN.setOnClickListener(this);
        textO.setOnClickListener(this);
        textP.setOnClickListener(this);
        textQ.setOnClickListener(this);
        textR.setOnClickListener(this);
        textS.setOnClickListener(this);

        textT.setOnClickListener(this);
        textU.setOnClickListener(this);
        textV.setOnClickListener(this);
        textW.setOnClickListener(this);
        textX.setOnClickListener(this);
        textY.setOnClickListener(this);
        textZ.setOnClickListener(this);

        viewCapsLock1 = (ImageView) findViewById(R.id.key_cap);
        viewCapsLock1.setOnClickListener(this);

        num_keybaord = (LinearLayout) findViewById(R.id.num_keybaord);
        letter_keybaord = (LinearLayout) findViewById(R.id.letter_keybaord);
        // 隐藏字母键盘，显示数字键盘
        letter_keybaord.setVisibility(View.GONE);
        num_keybaord.setVisibility(View.VISIBLE);

        // 数字键盘－确认按钮
        findViewById(R.id.key_enter).setOnClickListener(this);
        // 数字键盘－数字字母切换按钮
        findViewById(R.id.key_abc).setOnClickListener(this);
        // 数字键盘－收起键盘按钮
        findViewById(R.id.key_icon).setOnClickListener(this);
        // 字母键盘－确认按钮
        findViewById(R.id.key_enter1).setOnClickListener(this);
        // 字母键盘－数字字母切换按钮
        findViewById(R.id.key_123).setOnClickListener(this);
        // 字母键盘－收起键盘按钮
        findViewById(R.id.key_icon1).setOnClickListener(this);


        // 数字键盘Delete键
        View key_del = findViewById(R.id.key_del);
        key_del.setOnClickListener(this);
        key_del.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mContentText = "";
                setEditText();
                return true;
            }
        });
        // 字母键盘Delete键
        View key_del1 = findViewById(R.id.key_del1);
        key_del1.setOnClickListener(this);
        key_del1.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mContentText = "";
                setEditText();
                return true;
            }
        });

    }

    public void clear() {
        mContentText = "";
    }

    private void setEditText() {
        if (inputCompleteListener != null) {
            inputCompleteListener.textChanged(mContentText);
        }
    }

    @Override
    public void onClick(View v) {
        if ((System.currentTimeMillis() - currentTime) < 150) {
            return;
        }
        currentTime = System.currentTimeMillis();
        int id = v.getId();
        if (id == R.id.key_0) {
            clickNum("0");
        } else if (id == R.id.key_1) {
            clickNum("1");
        } else if (id == R.id.key_2) {
            clickNum("2");
        } else if (id == R.id.key_3) {
            clickNum("3");
        } else if (id == R.id.key_4) {
            clickNum("4");
        } else if (id == R.id.key_5) {
            clickNum("5");
        } else if (id == R.id.key_6) {
            clickNum("6");
        } else if (id == R.id.key_7) {
            clickNum("7");
        } else if (id == R.id.key_8) {
            clickNum("8");
        } else if (id == R.id.key_9) {
            clickNum("9");
        } else if (id == R.id.key_q) {
            clickNum("q");
        } else if (id == R.id.key_w) {
            clickNum("w");
        } else if (id == R.id.key_e) {
            clickNum("e");
        } else if (id == R.id.key_r) {
            clickNum("r");
        } else if (id == R.id.key_t) {
            clickNum("t");
        } else if (id == R.id.key_y) {
            clickNum("y");
        } else if (id == R.id.key_u) {
            clickNum("u");
        } else if (id == R.id.key_i) {
            clickNum("i");
        } else if (id == R.id.key_o) {
            clickNum("o");
        } else if (id == R.id.key_p) {
            clickNum("p");
        } else if (id == R.id.key_a) {
            clickNum("a");
        } else if (id == R.id.key_s) {
            clickNum("s");
        } else if (id == R.id.key_d) {
            clickNum("d");
        } else if (id == R.id.key_f) {
            clickNum("f");
        } else if (id == R.id.key_g) {
            clickNum("g");
        } else if (id == R.id.key_h) {
            clickNum("h");
        } else if (id == R.id.key_j) {
            clickNum("j");
        } else if (id == R.id.key_k) {
            clickNum("k");
        } else if (id == R.id.key_l) {
            clickNum("l");
        } else if (id == R.id.key_z) {
            clickNum("z");
        } else if (id == R.id.key_x) {
            clickNum("x");
        } else if (id == R.id.key_c) {
            clickNum("c");
        } else if (id == R.id.key_v) {
            clickNum("v");
        } else if (id == R.id.key_b) {
            clickNum("b");
        } else if (id == R.id.key_n) {
            clickNum("n");
        } else if (id == R.id.key_m) {
            clickNum("m");
        } else if (id == R.id.key_cap) {
            if (isCapsLock) {
                isCapsLock = false;
                viewCapsLock1.setImageResource(R.drawable.ca_cap_down);
                viewCapsLock1.setBackgroundResource(R.drawable.ca_grey_down);
            } else {
                isCapsLock = true;
                viewCapsLock1.setImageResource(R.drawable.ca_cap_up);
                viewCapsLock1.setBackgroundResource(R.drawable.ca_grey_up);
            }
            setKeyCapsLock(isCapsLock);
        } else if (id == R.id.key_del1) {
            if (!TextUtils.isEmpty(mContentText)) {
                mContentText = mContentText.substring(0, mContentText.length() - 1);
            }
            setEditText();
        } else if (id == R.id.key_del) {
            if (!TextUtils.isEmpty(mContentText)) {
                mContentText = mContentText.substring(0, mContentText.length() - 1);
            }
            setEditText();
        } else if (id == R.id.key_enter) {
            hiddenKeyBroad(mContentText);
            return;
        } else if (id == R.id.key_enter1) {
            hiddenKeyBroad(mContentText);
            return;
        } else if (id == R.id.key_123) {
            // 隐藏字母键盘，显示数字键盘
            letter_keybaord.setVisibility(View.GONE);
            num_keybaord.setVisibility(View.VISIBLE);
        } else if (id == R.id.key_abc) {
            // 隐藏数字键盘，显示字母键盘
            num_keybaord.setVisibility(View.GONE);
            letter_keybaord.setVisibility(View.VISIBLE);
            // 字母键盘恢复默认状态（显示小写字母键盘，大写按钮未按下
            isCapsLock = false;
            setKeyCapsLock(isCapsLock);
        } else if (id == R.id.key_icon) {
            hiddenKeyBroad(mContentText);
        } else if (id == R.id.key_icon1) {
            hiddenKeyBroad(mContentText);
        }
    }

    public void hiddenKeyBroad(String value) {
        if (value.length() == MAX_SIZE && inputCompleteListener != null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setEditText();
                    inputCompleteListener.onInputComplete();
                }
            }, 500);
        }
        //else {
        //    Toast.makeText(getContext(), "请输入8位密码！", Toast.LENGTH_SHORT).show();
        //}
        if (securityKeyboardListener != null) {
            securityKeyboardListener.onKeyBoardHidden();
        }
        dismiss();
    }

    public void showKeyBroad() {
        show();
        if (securityKeyboardListener != null) {
            rootView.post(new Runnable() {
                @Override
                public void run() {
                    securityKeyboardListener.onKeyBoardShow(rootView.getHeight());
                }
            });
        }
    }

    public String formatNum(String str) {
        try {
            if (null != str && str.contains(",")) {
                str = str.replace(",", "");
            }
            mContentText = dfLocal.format(new BigDecimal(str));
            return dfShow.format(new BigDecimal(mContentText));
        } catch (Exception e) {
            mContentText = "";
        }
        return "";
    }

    public String getContentText() {
        return mContentText;
    }

    private void clickNum(String num) {
        if (isCapsLock) {
            num = num.toUpperCase();
        }

        if (mContentText != null && mContentText.length() < MAX_SIZE) {
            mContentText = mContentText + num;
            setEditText();
            if (inputCompleteListener != null && mContentText.length() == MAX_SIZE) {
                hiddenKeyBroad(mContentText);
            }
        }
    }

    private void setKeyCapsLock(boolean isCapsLock) {
        textA.setText(isCapsLock ? "A" : "a");
        textB.setText(isCapsLock ? "B" : "b");
        textC.setText(isCapsLock ? "C" : "c");
        textD.setText(isCapsLock ? "D" : "d");
        textE.setText(isCapsLock ? "E" : "e");
        textF.setText(isCapsLock ? "F" : "f");
        textG.setText(isCapsLock ? "G" : "g");

        textH.setText(isCapsLock ? "H" : "h");
        textI.setText(isCapsLock ? "I" : "i");
        textJ.setText(isCapsLock ? "J" : "j");
        textK.setText(isCapsLock ? "K" : "k");
        textL.setText(isCapsLock ? "L" : "l");
        textM.setText(isCapsLock ? "M" : "m");
        textN.setText(isCapsLock ? "N" : "n");

        textO.setText(isCapsLock ? "O" : "o");
        textP.setText(isCapsLock ? "P" : "p");
        textQ.setText(isCapsLock ? "Q" : "q");
        textR.setText(isCapsLock ? "R" : "r");
        textS.setText(isCapsLock ? "S" : "s");
        textT.setText(isCapsLock ? "T" : "t");

        textU.setText(isCapsLock ? "U" : "u");
        textV.setText(isCapsLock ? "V" : "v");
        textW.setText(isCapsLock ? "W" : "w");
        textX.setText(isCapsLock ? "X" : "x");
        textY.setText(isCapsLock ? "Y" : "y");
        textZ.setText(isCapsLock ? "Z" : "z");
    }

}
