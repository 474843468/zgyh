package com.boc.bocsoft.mobile.bocmobile.base.widget.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

/**
 * 公共搜索组件
 * Created by Administrator on 2016/5/30.
 */
public class SearchView extends LinearLayout {

    private Context context;
    private View root_view;
    private RelativeLayout layout_search,search_body;
    private ImageView img_search,img_clean;
    private EditText search_edit;
    private TextView btn_search;
    private OnSearchListener onSearchListener = null;

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public interface OnSearchListener {
            public void OnSearch();
    }

    public void setOnSearchListener(OnSearchListener listener){
        onSearchListener = listener;
    }

    private void init() {
        root_view = LayoutInflater.from(context).inflate(R.layout.boc_view_search,this);
        layout_search = (RelativeLayout) root_view. findViewById(R.id.layout_search);
        search_body = (RelativeLayout) root_view. findViewById(R.id.search_body);
        img_search  = (ImageView) root_view.findViewById(R.id.img_search);
        img_clean = (ImageView) root_view.findViewById(R.id.img_clean);
        img_clean.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edit.setText("");
            }
        });
        search_edit = (EditText) root_view.findViewById(R.id.search_edit);
        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(search_edit.getText().toString().length()>0){
                    img_clean.setVisibility(View.VISIBLE);
                }else{
                    img_clean.setVisibility(View.GONE);
                }
            }
        });
        btn_search = (TextView) root_view.findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSearchListener != null){
                    onSearchListener.OnSearch();
                }
            }
        });
    }

    /**
     * 更换搜索框提示文字
     * @param hintText
     */
    public void changeSearchEditHint(String hintText){
        search_edit.setHint(hintText);
    }

    /**
     * 是否显示搜索按钮
     * @param isShow：true为显示，false不显示
     */
    public void hiddenSearchBtn(boolean isShow){
        if(!isShow){
            btn_search.setVisibility(View.GONE);
        }
    }

    /**
     * 获取搜索框文本
     * @return
     */
    public String getSearchText(){
        String searchText = search_edit.getText().toString();
        return searchText;
    }

}
