package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ImageLoaderUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.banner.holder.Holder;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.model.AdvertisementModel;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 广告view
 */
public class BannerImageHolderView implements Holder<AdvertisementModel> {

    private ImageView imageView;

    @Override
    public View createView(Context context) {
        imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return imageView;
    }

    @Override
    public void UpdateUI(Context context, int position, AdvertisementModel data) {
        int defaultRes = data.getPlaceHolderImageRes();
        defaultRes = defaultRes>0?defaultRes: R.drawable.life_ad_default;
        String imageUrl = data.getImageUrl();
        if (StringUtils.isEmptyOrNull(imageUrl)) {
            imageView.setImageResource(defaultRes);
            return;
        }
        ImageLoaderUtils.getBIIImageLoader()
            .load(data.getImageUrl())
            .error(defaultRes)
            .placeholder(defaultRes)
            .into(imageView);
    }
}
