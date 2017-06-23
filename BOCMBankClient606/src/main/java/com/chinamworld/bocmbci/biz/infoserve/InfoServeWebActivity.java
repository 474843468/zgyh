package com.chinamworld.bocmbci.biz.infoserve;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.utils.ViewUtils;

/**
 * 消息服务的详细内容展示类
 * 
 * @author xby
 * 
 */
public class InfoServeWebActivity extends InfoServeBaseActivity {

	private WebView mWebView = null;
	private Button btn = null;
	private Button forwardBtn = null;
    private Button backBtn = null;
    private EditText edit = null;
    private String cur_url = null;
    private Intent mLastIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addView(R.layout.service_info_web);
		initView();
	}

	private void initView() {
		mWebView=(WebView) findViewById(R.id.detial_info);
		ViewUtils.initWebView(mWebView);
		forwardBtn = (Button)findViewById(R.id.forward_btn);
    	backBtn = (Button)findViewById(R.id.back_btn);
    	backBtn.setText("back");
    	backBtn.setVisibility(View.GONE);
    	forwardBtn.setText("go");
    	forwardBtn.setVisibility(View.GONE);
    	Intent intent=getIntent();
    	String StringE=intent.getStringExtra("extra");
    	
    	/*btn.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	String str = edit.getText().toString();
            	if(str != "")
            	{
            		cur_url = str;
            		mWebView.loadUrl(str); 
            	}
            }
        } );*/
        
        forwardBtn.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	if(mWebView.canGoForward())
            		mWebView.goForward();
            }
        } );
        backBtn.setOnClickListener( new Button.OnClickListener()
        {
            public void onClick( View v )
            {
                // TODO Auto-generated method stub
            	if(mWebView.canGoBack())
            		mWebView.goBack();
            }
        } );
      
        
        mWebView.setWebViewClient(new WebViewClient(){     
        	public boolean shouldOverrideUrlLoading(WebView  view, String url) {     
        		mWebView.loadUrl(url);  
        		cur_url = url;
        		//insertTable(url,1,mWebView.getTitle());
        		return true;     
        	}     
        	}); 
        
       /* mWebView.setWebChromeClient(new WebChromeClient() {   
            public void onProgressChanged(WebView view, int progress) {   
              //Activity和Webview根据加载程度决定进度条的进度大小   
             //当加载到100%的时候 进度条自动消失   
              context.setProgress(progress * 100); 
              if(progress>=100)
              {
            	  insertTable(cur_url,1,view.getTitle());
              }
              //LogGloble.d("TTTTTTTTT",progress+","+view.getTitle());
            }           
        	});   */
        
        	mWebView.loadUrl(StringE);  
        	
        	setTitle(getString(R.string.infoserve_info));
    		btn_right.setVisibility(View.GONE);
    		back.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				/*Intent intent = new Intent(InfoServeWebActivity.this, InfoServeDetialActivity1.class);
    				startActivity(intent);*/
    				finish();
    			}
    		});
    	}
		
	}

	


