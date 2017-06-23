package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.gallery.Gallery;
import com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model.PurchaseModel;

/**
 * @author wangyang
 *         2016/12/19 14:53
 */
public class PriceAdapter extends BaseAdapter implements AdapterView.OnItemSelectedListener, Gallery.OnPageScrollListener {

    private Context context;

    private Gallery gallery;

    private PurchaseModel model;

    private float maxTitleSize, minTitleSize, differenceTitleSize, maxPriceSize, minPriceSize, differencePriceSize;

    public PriceAdapter(Context context, Gallery gallery) {
        this.context = context;
        this.gallery = gallery;

        gallery.setOnItemSelectedListener(this);
        gallery.setOnPageScrollListener(this);

        minPriceSize = maxTitleSize = getPixel(R.dimen.boc_text_size_little_big);
        minTitleSize = getPixel(R.dimen.boc_text_size_small);
        maxPriceSize = getPixel(R.dimen.boc_text_size_60px);
        differenceTitleSize = maxTitleSize - minTitleSize;
        differencePriceSize = maxPriceSize - minPriceSize;
    }

    public int getColor(int colorId) {
        return context.getResources().getColor(colorId);
    }

    public float getPixel(int dimenId) {
        return context.getResources().getDimensionPixelOffset(dimenId);
    }

    public String getString(int stringId) {
        return context.getResources().getString(stringId);
    }

    public void setModel(PurchaseModel model) {
        this.model = model;
        if (model != null && model.isHadQuotation())
            notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return model != null && model.isHadQuotation() ? 2 : 0;
    }

    @Override
    public Object getItem(int position) {
        switch (position) {
            case 0:
                return model.getSellRate().toPlainString();
            case 1:
                return model.getBuyRate().toPlainString();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null)
            viewHolder = new ViewHolder(View.inflate(context, R.layout.boc_item_longshortforex_price, null));
        else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvPrice.setText(getItem(position).toString());
        if (position == 0)
            viewHolder.tvTitle.setText(getString(R.string.boc_purchase_sell_price));
        else
            viewHolder.tvTitle.setText(getString(R.string.boc_purchase_buy_price));

        if (gallery.getSelectedItemPosition() == position)
            setTvStyle(viewHolder, R.color.boc_text_color_red, maxTitleSize,maxPriceSize, View.VISIBLE);
        else
            setTvStyle(viewHolder, R.color.boc_text_mobile_color, minTitleSize,minPriceSize, View.INVISIBLE);

        return viewHolder.itemView;
    }

    private void setTvStyle(ViewHolder holder, int colorId, float titleSize, float priceSize, int imgVisibility) {
        holder.tvTitle.setTextColor(getColor(colorId));
        holder.tvPrice.setTextColor(getColor(colorId));
        if (titleSize > 0)
            holder.tvTitle.getPaint().setTextSize(titleSize);
        if (priceSize > 0)
            holder.tvPrice.getPaint().setTextSize(priceSize);
        holder.ivArrow.setVisibility(imgVisibility);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onScroll(int curSelectedPosition, int nextSelectedPosition, float offSet, boolean isScrollLimitOffSet) {
        ViewHolder curHolder = (ViewHolder) gallery.getChildAt(curSelectedPosition).getTag();
        ViewHolder nextHolder = (ViewHolder) gallery.getChildAt(nextSelectedPosition).getTag();

        float curTitleSize = maxTitleSize - (differenceTitleSize * offSet);
        float nextTitleSize = minTitleSize + (differenceTitleSize * offSet);
        float curPriceSize = maxPriceSize - (differencePriceSize * offSet);
        float nextPriceSize = minPriceSize + (differencePriceSize * offSet);

        if (isScrollLimitOffSet) {
            setTvStyle(curHolder, R.color.boc_text_mobile_color, curTitleSize,curPriceSize, View.INVISIBLE);
            setTvStyle(nextHolder, R.color.boc_text_color_red, nextTitleSize,nextPriceSize, View.VISIBLE);
        } else {
            setTvStyle(curHolder, R.color.boc_text_color_red, curTitleSize,curPriceSize, View.VISIBLE);
            setTvStyle(nextHolder, R.color.boc_text_mobile_color, nextTitleSize,nextPriceSize, View.INVISIBLE);
        }

        resetText(curHolder);
        resetText(nextHolder);
    }

    private void resetText(ViewHolder holder) {
        holder.tvTitle.setText(holder.tvTitle.getText());
        holder.tvPrice.setText(holder.tvPrice.getText());
    }

    public class ViewHolder {

        private TextView tvTitle, tvPrice;

        private ImageView ivArrow;

        private View itemView;

        public ViewHolder(View itemView) {
            this.itemView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            ivArrow = (ImageView) itemView.findViewById(R.id.iv_arrow);

            itemView.setTag(this);
        }
    }
}
