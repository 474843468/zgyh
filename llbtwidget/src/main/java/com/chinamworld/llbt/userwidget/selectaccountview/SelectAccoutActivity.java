package com.chinamworld.llbt.userwidget.selectaccountview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chinamworld.llbt.model.AccountItem;
import com.chinamworld.llbt.utils.AccountInfoTransferTools;
import com.chinamworld.llbtwidget.R;

import java.util.Map;

/**
 * 选择账户
 * Created by wangf on 2016/6/13.
 */
public class SelectAccoutActivity extends Activity {

    private ListView mListView;
    private TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.llbt_select_account_view_activity_layout);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.tittlelayout);
        initView();
    }
    static SelectAccountButton curSelectAccountBt = null;
    public static void setSelectAccountButton(SelectAccountButton bt) {
        curSelectAccountBt = bt;
    }

    public void initView() {
        mListView = (ListView) findViewById(R.id.lv_transdetail_selectaccount);
        back=(TextView) findViewById(R.id.ib_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final CommonAdapter<AccountItem> adapter = new CommonAdapter<AccountItem>(this,mListView,curSelectAccountBt.getAccountList(),new ICommonAdapter<AccountItem>(){

            @Override
            public View getView(int arg0, AccountItem currentItem, LayoutInflater inflater, View convertView, ViewGroup viewGroup) {
                View rootView = inflater.inflate( R.layout.llbt_selected_account_item_layout, null);
                ImageView ivPic = (ImageView) rootView.findViewById(R.id.iv_pic);
                TextView tvNumber = (TextView) rootView.findViewById(R.id.tv_number);
                TextView tvName = (TextView) rootView.findViewById(R.id.tv_name);
//                ImageView ivStatus = (ImageView) rootView.findViewById(R.id.iv_status);
//                View divider = rootView.findViewById(R.id.divider);
//                TextView tvAmountTitle = (TextView) rootView.findViewById(R.id.tv_amount_title);
//                LinearLayout lytAmountList = (LinearLayout) rootView.findViewById(R.id.lyt_amount_list);
//                lytAmountList.setGravity(Gravity.RIGHT);
//                RelativeLayout lytAmount = (RelativeLayout) rootView.findViewById(R.id.lyt_amount);
                TextView tvDescription = (TextView) rootView.findViewById(R.id.tv_description);;
                tvDescription.setVisibility(View.GONE);
                tvNumber.setText(AccountInfoTransferTools.getForSixForString(currentItem.getAccountNum()));
                tvName.setText((String)((Map<Object,String>)currentItem.getSource()).get("nickName"));
                ivPic.setImageDrawable(SelectAccoutActivity.this.getResources().getDrawable(AccountInfoTransferTools.getCardPic(currentItem)));
                return rootView;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountItem curAccount = adapter.getItem(position);
                curSelectAccountBt.setData(curAccount);
                finish();
            }
        });

        mListView.setAdapter(adapter);
    }






}
