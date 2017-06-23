package com.boc.bocsoft.mobile.framework.widget.imageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.boc.bocsoft.mobile.common.client.BaseHttpClient;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.RequestManager;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.RequestProcessor;
import com.boc.bocsoft.mobile.framework.widget.imageLoader.itf.RequestProcessorProvider;
import com.squareup.picasso.Picasso;

import java.io.File;

import static com.boc.bocsoft.mobile.common.utils.PublicUtils.checkNotNull;

/**
 * 图片加载类，以单例模式供外部使用。
 * Created by XieDu on 2016/5/30.
 */
public class ImageLoader implements RequestManager, RequestProcessorProvider {


    // =================图片scaletype=====================
    /**
     *设置SCALETYPE_FIT时imageview不能是wrap_content
     */
    public static final int SCALETYPE_FIT = 0;
    public static final int SCALETYPE_CENTER_CROP = 1;
    public static final int SCALETYPE_CENTER_INSIDE = 2;
    // =================优先级=====================
    public static final int PRIORITY_LOW= 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;


    private static volatile ImageLoader singleton;
    private Context context;
    private RequestProcessor requestProcessor;
    private BaseHttpClient httpClient;

    private ImageLoader(Context context, BaseHttpClient httpClient) {
        this.context = context;
        this.httpClient = httpClient;
        this.requestProcessor = new PicassoRequestProcessor(this);
    }

    /**
     * @param context context，构造对象时使用它的getApplicationContext
     * @return ImageLoader单例
     */
    public static ImageLoader with(Context context) {
        if (singleton == null) {
            synchronized (Picasso.class) {
                if (singleton == null) {
                    singleton = new Builder(context).build();
                }
            }
        }
        return singleton;
    }


    @Override
    public void cancelRequest(ImageView view) {
        requestProcessor.cancelRequest(view);
    }

    @Override
    public void pauseRequests() {
        requestProcessor.pauseRequests();
    }

    @Override
    public void resumeRequests() {
        requestProcessor.resumeRequests();
    }


    public ImageRequestHolder load(Uri uri) {
        return new ImageRequestHolder(this, uri, 0);
    }

    public ImageRequestHolder load(String path) {
        if (path == null) {
            return new ImageRequestHolder(this, (Uri) null, 0);
        } else if (path.trim().length() == 0) {
            throw new IllegalArgumentException("Path must not be empty.");
        } else {
            return this.load(Uri.parse(path));
        }
    }

    public ImageRequestHolder load(File file) {
        return file == null ? new ImageRequestHolder(this, null, 0) : this.load(Uri.fromFile(file));
    }

    public ImageRequestHolder load(int resourceId) {
        if (resourceId == 0) {
            throw new IllegalArgumentException("Resource ID must not be zero.");
        } else {
            return new ImageRequestHolder(this, null, resourceId);
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public RequestProcessor provideRequestProcessor() {
        return requestProcessor;
    }

    @Override
    public BaseHttpClient getHttpClient() {
        return httpClient;
    }


    public static class Builder {
        private Context context;
        private BaseHttpClient httpClient;
        private Bitmap.Config defaultBitmapConfig;

        public Builder(Context context) {
            checkNotNull(context, "Context must not be null.");
            this.context = context.getApplicationContext();
        }

        public Builder httpClient(BaseHttpClient httpClient) {
            checkNotNull(httpClient, "httpClient must not be null.");
            this.httpClient = httpClient;
            return this;
        }


        public ImageLoader build() {
            if (httpClient == null) {
                httpClient = new BaseHttpClient();
            }
            return new ImageLoader(context, httpClient);
        }
    }
}
