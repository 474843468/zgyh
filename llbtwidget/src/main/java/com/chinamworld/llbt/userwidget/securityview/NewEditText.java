package com.chinamworld.llbt.userwidget.securityview;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chinamworld.llbtwidget.R;


/**
 * Created by Administrator on 2016/8/30.
 */
public class NewEditText extends FrameLayout {
    public NewEditText(Context context) {
        super(context);
        initView(context);
    }

    public NewEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public NewEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private TextView[] tvList;


    private void initView(Context context){
        tvList = new TextView[6];
        View root = LayoutInflater.from(context).inflate(R.layout.new_edit_text,this,true);

        tvList[0] = (TextView)findViewById(R.id.tv1);
        tvList[1] = (TextView)findViewById(R.id.tv2);
        tvList[2] = (TextView)findViewById(R.id.tv3);
        tvList[3] = (TextView)findViewById(R.id.tv4);
        tvList[4] = (TextView)findViewById(R.id.tv5);
        tvList[5] = (TextView)findViewById(R.id.tv6);



        EditText et = (EditText)findViewById(R.id.et);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setTextViewData(s + "");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void setTextViewData(String s){
        for(int i = 0; i < 6 ;i++){
            if(i < s.length() ){
                tvList[i].setText(s.substring(i,i+1));
            }
            else
                tvList[i].setText("");
        }
    }




}
