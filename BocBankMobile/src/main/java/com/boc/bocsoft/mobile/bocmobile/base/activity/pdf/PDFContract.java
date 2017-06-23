package com.boc.bocsoft.mobile.bocmobile.base.activity.pdf;

import android.net.Uri;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;
import com.boc.bocsoft.mobile.framework.ui.BaseView;

/**
 * Created by dingeryue on 2016年11月21.
 */

public interface PDFContract {

  interface PDFPresenter extends BasePresenter{
    void loadPDF(Uri uri);
  }

  interface  PDFView extends BaseView<PDFPresenter>{
    void showLoading(String msg);
    void closeLoading();
    void onLoadSuccess(Uri html);
    void onLoadFaile();
  }

}
