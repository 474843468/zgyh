package com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.buss.specializedservice.consumefinance.module.LoanTypeItemModle;

import java.util.List;

/**
 * Created by zcy7065 on 2016/11/1.
 */
public class LoanListAdapter extends ArrayAdapter<LoanTypeItemModle> {

    private ILoanListListener listener = null;


    private int rescourseId;

    public LoanListAdapter(Context context, int textViewrescourseId, List<LoanTypeItemModle> object){
        super(context,textViewrescourseId,object);
        rescourseId = textViewrescourseId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View item = convertView;
        if(item == null){
            View view = LayoutInflater.from(getContext()).inflate(rescourseId,null);
            ViewHolder viewHolder = new ViewHolder();

            viewHolder.LoanTypeImage = (ImageView) view.findViewById(R.id.image_loantype);
            viewHolder.LoanTypeName = (TextView) view.findViewById(R.id.text_loanname);
            viewHolder.LoanTypeDescrip = (TextView) view.findViewById(R.id.text_loandescript);
            viewHolder.LoanTypeinsert = (TextView) view.findViewById(R.id.text_loaninsert);
            view.setTag(viewHolder);

            item = view;
        }

        ViewHolder viewHolder =  (ViewHolder) item.getTag();

        LoanTypeItemModle loanType = getItem(position);

        viewHolder.LoanTypeImage.setImageResource(loanType.getLoanTypeImage());
        viewHolder.LoanTypeName.setText(loanType.getLoanTypeName());
        viewHolder.LoanTypeDescrip.setText(loanType.getLoanTypeDescrip());

        final int pos = position;
        viewHolder.LoanTypeinsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(),"aaa",Toast.LENGTH_SHORT).show();
                if(listener != null){
                    listener.onClick(pos);
                }
            }
        });

        if(loanType.isLableVisable().equals("visable")){
            viewHolder.LoanTypeinsert.setVisibility(View.VISIBLE);
        }
        else if(loanType.isLableVisable().equals("invisable")){
            viewHolder.LoanTypeinsert.setVisibility(View.INVISIBLE);
        }
        else if(loanType.isLableVisable().equals("gone")){
            viewHolder.LoanTypeinsert.setVisibility(View.GONE);
        }

        return  item;
    }

    public void setListener(ILoanListListener value){
        listener = value;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public LoanTypeItemModle getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }



    /*private void actionItemInnerClick(int pos){

        if(itemInnerClick != null)itemInnerClick.onClick(pos);
    }

    private ItemInnerClickListener itemInnerClick;

    public void setItemInnerClick(ItemInnerClickListener itemInnerClick) {
        this.itemInnerClick = itemInnerClick;
    }

    static interface ItemInnerClickListener{
        void onClick(int pos);
    }*/


    private  static class ViewHolder{
        ImageView LoanTypeImage ;
        TextView LoanTypeName ;
        TextView LoanTypeDescrip ;
        TextView LoanTypeinsert ;
    }

    public interface ILoanListListener{
        void onClick(int position);
    }
}

