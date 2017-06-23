package com.boc.bocsoft.mobile.framework.widget.imageLoader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.LoadListener;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.RequestProcessorProvider;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.Transformation;

import java.util.ArrayList;
import java.util.List;

import static com.boc.bocsoft.mobile.common.utils.PublicUtils.checkNotNull;

/**
 * 保存image加载请求的类
 * Created by XieDu on 2016/5/30.
 */
public class ImageRequestHolder {
    RequestProcessorProvider imageLoader;
    Uri uri;
    int resourceId;
    ImageView target;
    LoadListener loadListener;
    int targetWidth;
    int targetHeight;
    boolean isResize;
    int scaleType;
    boolean isScaled;
    Bitmap.Config config;
    int priority;
    List<Transformation> transformations;
    Drawable placeholderDrawable;
    Drawable errorDrawable;
    Object tag;
    boolean skipMemoryCache;
    boolean skipDiskCache;
    /**
     * 重新加载
     */
    boolean refresh;
    float rotationDegrees;
    float rotationPivotX;
    float rotationPivotY;
    boolean hasRotationPivot;

    ImageRequestHolder(RequestProcessorProvider imageLoader, Uri uri, int resourceId) {
        this.imageLoader = imageLoader;
        this.uri = uri;
        this.resourceId = resourceId;
    }

    public ImageRequestHolder placeholder(int placeholderResId) {
        if (placeholderResId == 0) {
            throw new IllegalArgumentException("Placeholder image resource invalid.");
        }
        this.placeholderDrawable =
                ContextCompat.getDrawable(imageLoader.getContext(), placeholderResId);
        return this;
    }

    public ImageRequestHolder placeholder(Drawable placeholderDrawable) {
        this.placeholderDrawable = placeholderDrawable;
        return this;
    }

    public ImageRequestHolder error(int errorResId) {
        if (errorResId == 0) {
            throw new IllegalArgumentException("Error image resource invalid.");
        }
        this.errorDrawable = ContextCompat.getDrawable(imageLoader.getContext(), errorResId);
        return this;
    }

    public ImageRequestHolder error(Drawable errorDrawable) {
        checkNotNull(errorDrawable, "Error image may not be null.");
        this.errorDrawable = errorDrawable;
        return this;
    }

    public ImageRequestHolder tag(Object tag) {
        checkNotNull(tag, "Tag invalid.");
        this.tag = tag;
        return this;
    }

    public ImageRequestHolder resize(int targetWidth, int targetHeight) {
        isResize = true;
        this.targetWidth = targetWidth;
        this.targetHeight = targetHeight;
        return this;
    }

    /**
     * @param scaleType ImageLoader里的SCALETYPE_FIT
     * 或者SCALETYPE_CENTER_CROP或者SCALETYPE_CENTER_INSIDE
     */
    public ImageRequestHolder scaleType(int scaleType) {
        this.scaleType = scaleType;
        isScaled = true;
        return this;
    }

    public ImageRequestHolder config(Bitmap.Config config) {
        this.config = config;
        return this;
    }

    /**
     * 设置优先级
     *
     * @param priority 优先级 ImageLoader里的PRIORITY_LOW或者PRIORITY_NORMAL或者PRIORITY_HIGH
     */
    public ImageRequestHolder priority(int priority) {
        this.priority = priority;
        return this;
    }

    public ImageRequestHolder rotate(float degrees) {
        this.rotationDegrees = degrees;
        return this;
    }

    public ImageRequestHolder rotate(float degrees, float pivotX, float pivotY) {
        this.rotationDegrees = degrees;
        this.rotationPivotX = pivotX;
        this.rotationPivotY = pivotY;
        this.hasRotationPivot = true;
        return this;
    }

    public ImageRequestHolder transform(Transformation transformation) {
        checkNotNull(transformation, "Transformation must not be null.");
        checkNotNull(transformation.key(), "Transformation key must not be null.");
        if (this.transformations == null) {
            this.transformations = new ArrayList<Transformation>(2);
        }
        this.transformations.add(transformation);
        return this;
    }

    public ImageRequestHolder transform(List<? extends Transformation> transformations) {
        checkNotNull(transformations, "Transformation list must not be null.");
        for (Transformation transformation : transformations)
            this.transform(transformation);
        return this;
    }

    public ImageRequestHolder skipMemoryCache(boolean skipMemoryCache) {
        this.skipMemoryCache = skipMemoryCache;
        return this;
    }

    public ImageRequestHolder skipDiskCache(boolean skipDiskCache) {
        this.skipDiskCache = skipDiskCache;
        return this;
    }

    public ImageRequestHolder refresh(boolean refresh) {
        this.refresh = refresh;
        return this;
    }

    public void into(ImageView target) {
        this.into(target, null);
    }

    public void into(ImageView target, LoadListener callback) {
        this.target = target;
        this.loadListener = callback;
        //TODO 设置到真正的处理图片加载请求的类里去
        this.imageLoader.provideRequestProcessor().processRequest(this);
    }
}