package com.boc.bocsoft.mobile.bocmobile.base.widget.selector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.ArrayList;

/**
 * Created by wangtong on 2016/5/21.
 */
    public class LoginNameSelector extends RelativeLayout {

    private EditText phoneNoEdit;
    private ImageView downBtn;
    private ListView phoneList;
    private ArrayList<String> listData = null;
    private OnItemListDeletedListener itemListDeletedListener = null;

    /**
     * @Title: OnItemListDeletedListener
     * @Description: 删除手机号码列表条目事件
     */
    public interface OnItemListDeletedListener {

        public void OnItemListDeleted();
    }

    public LoginNameSelector(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initView();
    }

    /**
     * @Title: setItemListDeletedListener
     * @Description:设置删除手机号码列表条目事件
     * @param:
     */
    public void setItemListDeletedListener(OnItemListDeletedListener listener) {
        itemListDeletedListener = listener;
    }

    /**
     * @Title: getPhoneNo
     * @Description: 获取电话号码
     * @return:电话号码
     */
    public String getAccountNumber() {
        return phoneNoEdit.getText().toString();
    }

    private void initView() {
        View contentView = View.inflate(getContext(), R.layout.boc_view_login_name_selector, null);
        phoneNoEdit = (EditText) contentView.findViewById(R.id.phone_no_edit);
        downBtn = (ImageView) contentView.findViewById(R.id.down_btn);
        downBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listData.size() > 0) {
                    if (phoneList.getVisibility() == View.GONE) {
                        phoneList.setVisibility(View.VISIBLE);
                        downBtn.setImageResource(R.drawable.boc_image_up);
                    } else {
                        phoneList.setVisibility(View.GONE);
                        downBtn.setImageResource(R.drawable.boc_image_down);
                    }
                }
            }
        });
        phoneList = (ListView) contentView.findViewById(R.id.phone_list);
        initPhoneList();
        addView(contentView);
    }

    /**
     * @Title: initPhoneList
     * @Description: 初始化电话下拉表
     */
    private void initPhoneList() {

        listData = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            listData.add("1315207913" + i);
        }

        if (listData.size() <= 0) {
            setDownListBtnVisiblity(false);
        } else {

            ListAdapter listAdapter = new ListAdapter();
            phoneList.setAdapter(listAdapter);
            phoneList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    phoneNoEdit.setText(listData.get(position));
                    phoneList.setVisibility(View.GONE);
                    downBtn.setImageResource(R.drawable.boc_image_down);
                }
            });
        }

    }

    /**
     * @Title: setVisibleEditeBtn
     * @Description: 隐藏下拉按钮
     * @param: 是否隐藏
     */
    private void setDownListBtnVisiblity(boolean visiblity) {

        if (visiblity) {
            downBtn.setVisibility(View.VISIBLE);
            downBtn.setImageResource(R.drawable.boc_image_up);
        } else {
            downBtn.setVisibility(View.GONE);
            if (phoneList != null) {
                phoneList.setVisibility(View.GONE);
            }
        }
    }

    /**
     * @Title: ListAdapter
     * @Description: 电话下拉列表的适配器
     * @author: Administrator
     * @time: 2016/5/20
     */
    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(getContext(), R.layout.boc_item_login_name_selector, null);
                ViewHolder holder = new ViewHolder(convertView, position);
                convertView.setTag(holder);
            }

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.phoneNo.setText(listData.get(position));
            return convertView;
        }

        /**
         * @Title: ViewHolder
         * @Description: contentView的容器
         */
        class ViewHolder {

            TextView phoneNo = null;
            ImageView btn_item = null;

            public ViewHolder(View convertView, final int position) {
                phoneNo = (TextView) convertView.findViewById(R.id.phone_no_text);
                btn_item = (ImageView) convertView.findViewById(R.id.delete_btn);
                btn_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listData.remove(position);

                        if (itemListDeletedListener != null) {
                            itemListDeletedListener.OnItemListDeleted();
                        }

                        if (listData.size() <= 0) {
                            /**点击删除按钮后，删除这一行*/
                            setDownListBtnVisiblity(false);
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

}
