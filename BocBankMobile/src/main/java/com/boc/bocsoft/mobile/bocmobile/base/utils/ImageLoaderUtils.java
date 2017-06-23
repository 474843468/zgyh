package com.boc.bocsoft.mobile.bocmobile.base.utils;

import com.boc.bocsoft.mobile.bii.common.client.BIIClient;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.ImageLoader;

/**
 * 获取绑定了bocbiiclient的图片加载工具类
 * Created by XieDu on 2016/6/1.
 */
public class ImageLoaderUtils {

    public static ImageLoader getBIIImageLoader() {
        return ImageLoaderHolder.biiImageLoader;
    }

    private static class ImageLoaderHolder {
        private static ImageLoader biiImageLoader =
                new ImageLoader.Builder(ApplicationContext.getAppContext())
                        .httpClient(BIIClient.instance.getHttpClient())
                        .build();
    }
}
