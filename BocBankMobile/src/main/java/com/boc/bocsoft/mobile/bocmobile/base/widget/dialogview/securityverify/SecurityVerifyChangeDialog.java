package com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.BaseDialog;


/**
 * @Description:更改安全认证方式
 * @author: wangtong
 * @time: 2016/5/26
 */
public class SecurityVerifyChangeDialog extends BaseDialog {

    private View rootView;
    private Activity activity;
    private SecurityTypeAdapter adapter;

    public SecurityVerifyChangeDialog(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected View onAddContentView() {
        rootView = inflater.inflate(R.layout.boc_security_change_dialog, null);
        return rootView;
    }

    @Override
    protected void initView() {
        ListView listView = (ListView) rootView.findViewById(R.id.list_view);
        adapter = new SecurityTypeAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedPosition(position);
                adapter.notifyDataSetChanged();

                if (SecurityVerity.getInstance().verifyCodeResult != null) {
                    CombinListBean bean = (CombinListBean) adapter.getItem(adapter.getSelectedPosition());
                    SecurityVerity.getInstance().verifyCodeResult.onSecurityTypeSelected(bean);
                    SecurityVerity.getInstance().setCurrentSecurityVerifyTypeId(bean.getId());
                }
                cancel();
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void setListener() {
    }

    /**
     * 列表适配器
     */
    class SecurityTypeAdapter extends BaseAdapter {

        private int selectedPosition = 0;

        //安全认证数据模型
        private SecurityFactorModel factorResult;

        public SecurityTypeAdapter() {
            factorResult = SecurityVerity.getInstance().getFactorSecurity();
            selectedPosition = getItemPosition(SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
        }

        public void setSelectedPosition(int selected) {
            selectedPosition = selected;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        private int getItemPosition(String id) {
            int position = 0;
            for (int i = 0; i < factorResult.getCombinList().size(); i++) {
                CombinListBean combin = factorResult.getCombinList().get(i);
                if (combin.getId().equals(id)) {
                    position = i;
                    break;
                }
            }
            return position;
        }

        @Override
        public int getCount() {
            return factorResult.getCombinList().size();
        }

        @Override
        public Object getItem(int position) {
            return factorResult.getCombinList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(activity, R.layout.boc_item_security_type, null);
            TextView textView = (TextView) convertView.findViewById(R.id.item_name);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.item_check);

            CombinListBean bean = (CombinListBean) getItem(position);
            textView.setText(bean.getName());
//            if (selectedPosition == position) {
//                imageView.setVisibility(View.VISIBLE);
//            } else {
//                imageView.setVisibility(View.GONE);
//            }
            return convertView;
        }
    }
}














