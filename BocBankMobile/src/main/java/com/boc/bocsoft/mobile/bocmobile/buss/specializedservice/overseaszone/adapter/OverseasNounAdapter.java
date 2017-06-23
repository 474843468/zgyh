package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boc.bocop.sdk.util.StringUtil;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.overseaszone.model.OverseasNounModel;
import com.boc.bocsoft.mobile.framework.widget.listview.BaseListAdapter;

/**
 * 出国攻略文本中listview适配器
 */
public class OverseasNounAdapter extends BaseListAdapter<OverseasNounModel>{

    public OverseasNounAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView =mInflater.inflate(R.layout.boc_item_overseas_noun, parent, false);
            viewHolder.tvTitle=(TextView)convertView.findViewById(R.id.tv_noun_title);
            viewHolder.tvContent=(TextView)convertView.findViewById(R.id.tv_noun_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        OverseasNounModel nounModel=getItem(position);
        if (!StringUtil.isNullOrEmpty(nounModel.getTitle())){
            viewHolder.tvTitle.setVisibility(View.VISIBLE);
            viewHolder.tvTitle.setText(nounModel.getTitle());
        }else{
            viewHolder.tvTitle.setVisibility(View.GONE);
        }
        String content=nounModel.getContent();
        if (content.contains("A外经贸部A")){
            content=content.replace("A外经贸部A","“外经贸部”");
        }
        viewHolder.tvContent.setText(content);

        return convertView;
    }

    class ViewHolder{
        private TextView tvTitle;
        private TextView tvContent;
    }
}
