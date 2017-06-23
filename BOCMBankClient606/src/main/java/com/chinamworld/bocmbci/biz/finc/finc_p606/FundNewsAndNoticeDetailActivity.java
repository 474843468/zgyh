package com.chinamworld.bocmbci.biz.finc.finc_p606;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * Created by Administrator on 2016/10/31 0031.
 */
public class FundNewsAndNoticeDetailActivity extends FincBaseActivity{

    private TextView newsAndNoticeTitle,newsAndNoticeTime,newsAndNoticeContent;
    private String isNewsOrNotice;
    private WebView mWebView;
    private WebSettings mWebSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finc_news_and_notice_detail);
        setTitle("详情");
        isNewsOrNotice = getIntent().getStringExtra("isNewsOrNotice");
        init();
    }

    private void init(){
        newsAndNoticeTitle = (TextView) findViewById(R.id.newsAndNoticeTitle);
        newsAndNoticeTime = (TextView) findViewById(R.id.newsAndNoticeTime);
//        newsAndNoticeContent = (TextView) findViewById(R.id.newsAndNoticeContent);
        newsAndNoticeTitle.setText("0".equals(isNewsOrNotice)?
                fincControl.noticeItem.getStrcaption():fincControl.newsItem.getTitle());
        newsAndNoticeTime.setText("0".equals(isNewsOrNotice)?
                fincControl.noticeItem.getSdtpublish():fincControl.newsItem.getCreated());
//        newsAndNoticeContent.setText(Html.fromHtml(fincControl.contentDetail.getContent()));
        mWebView = (WebView) findViewById(R.id.fund_webView);
        mWebView.loadDataWithBaseURL("",fincControl.contentDetail.getContent(),"text/html", "UTF-8","");
        mWebView.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8

    }
}
