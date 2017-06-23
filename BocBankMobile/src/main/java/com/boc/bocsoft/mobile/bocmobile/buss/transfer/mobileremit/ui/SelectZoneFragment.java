package com.boc.bocsoft.mobile.bocmobile.buss.transfer.mobileremit.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.ListModel;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 选择地区页面
 * <p/>
 * Created by liuweidong on 2016/8/13.
 */
public class SelectZoneFragment extends BussFragment {
    private ListView listView;
    private ZoneAdapter adapter;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        listView = new ListView(mContext);
        return listView;
    }

    @Override
    public void initView() {
        listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        listView.setBackgroundColor(getResources().getColor(R.color.boc_common_bg_color));
        listView.setPadding(0, 20, 0, 0);
    }

    @Override
    public void initData() {
        List<ListModel> list = new ArrayList<>();
        Map<String, String> map = PublicCodeUtils.getTransferIbkMap(mContext);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getKey().equals("40208") || entry.getKey().equals("49998") || entry.getKey().equals("49404")
                    || entry.getKey().equals("45599") || entry.getKey().equals("40004") || entry.getKey().equals("/**账户地区*/")) {
                continue;
            } else if (entry.getKey().equals("47504")) {
                ListModel item = new ListModel();
                item.setNameID(entry.getKey());
                item.setName("广东");
                list.add(item);
            } else {
                ListModel item = new ListModel();
                item.setNameID(entry.getKey());
                item.setName(entry.getValue());
                list.add(item);
            }
        }
        adapter = new ZoneAdapter(list);
        listView.setAdapter(adapter);
    }

    @Override
    public void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("ListModel", adapter.getItem(position));
                setFramgentResult(100, bundle);
                pop();
            }
        });
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_mobile_remit_zone_title);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    class ZoneAdapter extends BaseAdapter {
        private List<ListModel> list;

        public ZoneAdapter(List<ListModel> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ListModel getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.boc_fragment_zone_item, null);
                viewHolder.editChoiceWidget = (EditChoiceWidget) convertView.findViewById(R.id.edit_choice);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.editChoiceWidget.setChoiceTextName(list.get(position).getName());
            return convertView;
        }

        private class ViewHolder {
            private EditChoiceWidget editChoiceWidget;
        }
    }
}
