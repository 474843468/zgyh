package com.boc.bocsoft.mobile.framework.widget.imageLoader;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.common.client.BaseHttpClient;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.LoadListener;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.RequestProcessor;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.RequestProcessorProvider;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.Transformation;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用Picasso处理图片加载请求
 * Created by XieDu on 2016/5/31.
 */
public class PicassoRequestProcessor implements RequestProcessor {
    private RequestCreator requestCreator;
    private Picasso picasso;
    private RequestProcessorProvider imageLoader;
    private Object tag;//给请求添加标志，以便暂停或者继续

    public PicassoRequestProcessor(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
        //创建picasso
        //如果配置了okhttpclient，就把它设置给picasso
        BaseHttpClient httpClient = imageLoader.getHttpClient();
        if (httpClient != null) {
            picasso = new Picasso.Builder(imageLoader.getContext()).downloader(
                    new OkHttp3Downloader(httpClient.getClient())).build();
        } else {
            picasso = Picasso.with(imageLoader.getContext());
        }
        tag = imageLoader.getContext();
    }

    @Override
    public void processRequest(ImageRequestHolder requestHolder) {
        if (requestHolder == null) {
            return;
        }
        //如果设置了刷新图片的标志和加载的地址，就清除缓存
        if (requestHolder.refresh && requestHolder.uri != null) {
            picasso.invalidate(requestHolder.uri);
        }
        //从给定地址或者图片资源加载图片
        requestCreator = (requestHolder.uri != null) ? picasso.load(requestHolder.uri)
                : picasso.load(requestHolder.resourceId);
        //设置标签，以供暂停或者恢复
        requestCreator.tag(tag);
        //设置错误时要显示的图片
        if (requestHolder.errorDrawable != null) {
            requestCreator.error(requestHolder.errorDrawable);
        }
        //设置未加载时的占位的图片
        if (requestHolder.placeholderDrawable != null) {
            requestCreator.placeholder(requestHolder.placeholderDrawable);
        }
        //重新设置图片大小
        if (requestHolder.isResize) {
            requestCreator.resize(requestHolder.targetWidth, requestHolder.targetHeight);
        }
        //设置图片scaleType
        if (requestHolder.isScaled) {
            switch (requestHolder.scaleType) {
                case ImageLoader.SCALETYPE_FIT:
                    requestCreator.fit();
                    break;
                case ImageLoader.SCALETYPE_CENTER_CROP:
                    requestCreator.centerCrop();
                    break;
                case ImageLoader.SCALETYPE_CENTER_INSIDE:
                    requestCreator.centerInside();
                    break;
                default:
                    break;
            }
        }
        //设置内存缓存策略
        if (requestHolder.skipMemoryCache) {
            requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        }
        //设置硬盘缓存策略
        if (requestHolder.skipDiskCache) {
            requestCreator.networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE);
        }
        //设置优先级
        switch (requestHolder.priority) {
            case ImageLoader.PRIORITY_LOW:
                requestCreator.priority(Picasso.Priority.LOW);
                break;
            case ImageLoader.PRIORITY_NORMAL:
                requestCreator.priority(Picasso.Priority.NORMAL);
                break;
            case ImageLoader.PRIORITY_HIGH:
                requestCreator.priority(Picasso.Priority.HIGH);
                break;
        }
        //设置转换器
        if (requestHolder.transformations != null) {
            requestCreator.transform(convertTransformations(requestHolder.transformations));
        }
        if (requestHolder.hasRotationPivot) {
            requestCreator.rotate(requestHolder.rotationDegrees, requestHolder.rotationPivotX,
                    requestHolder.rotationPivotY);
        }else {
            requestCreator.rotate(requestHolder.rotationDegrees);
        }
        //设置bitmap config
        if (requestHolder.config != null) {
            requestCreator.config(requestHolder.config);
        }
        //设置加载监听器
        if (requestHolder.loadListener == null) {
            requestCreator.into(requestHolder.target);
        } else {
            requestCreator.into(requestHolder.target,
                    new PicassoCallBack(requestHolder.loadListener));
        }
    }

    @Override
    public void cancelRequest(ImageView view) {
        picasso.cancelRequest(view);
    }

    @Override
    public void pauseRequests() {
        picasso.pauseTag(tag);
    }

    @Override
    public void resumeRequests() {
        picasso.resumeTag(tag);
    }

    class PicassoCallBack implements Callback {
        private LoadListener loadListener;

        public PicassoCallBack(LoadListener loadListener) {
            this.loadListener = loadListener;
        }

        @Override
        public void onSuccess() {
            loadListener.onSuccess();
        }

        @Override
        public void onError() {
            loadListener.onError();
        }
    }

    public List<PicassoTransformation> convertTransformations(
            List<Transformation> transformations) {
        List<PicassoTransformation> result = new ArrayList<PicassoTransformation>();
        for (Transformation transformation : transformations)
            result.add(new PicassoTransformation(transformation));
        return result;
    }

    class PicassoTransformation implements com.squareup.picasso.Transformation {
        private Transformation transformation;

        public PicassoTransformation(Transformation transformation) {
            this.transformation = transformation;
        }

        @Override
        public Bitmap transform(Bitmap bitmap) {
            return transformation.transform(bitmap);
        }

        @Override
        public String key() {
            return transformation.key();
        }
    }
}
