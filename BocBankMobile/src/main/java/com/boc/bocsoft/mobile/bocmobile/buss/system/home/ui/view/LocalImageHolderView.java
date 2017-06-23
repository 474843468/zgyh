package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.holder.Holder;

/**
 * Created by Administrator on 2016/5/26.
 */
public class LocalImageHolderView implements Holder<Integer> {

    private ImageView imageView;
    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, Integer data) {
        imageView.setImageResource(data);
    }
}
