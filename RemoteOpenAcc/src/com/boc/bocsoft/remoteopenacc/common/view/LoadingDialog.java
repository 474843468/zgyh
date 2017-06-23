package com.boc.bocsoft.remoteopenacc.common.view;


import com.boc.bocma.serviceinterface.op.MAOPRequestManager;
import com.boc.bocsoft.remoteopenacc.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

import android.widget.Button;

/**
 * 加载对话框
 * @author lxw
 *
 */
public class LoadingDialog extends Dialog{

	private Context mContext;
	private Button closeButton;
	
    public LoadingDialog(Context context) {
        super(context, R.style.bocroa_style_dialog_normal);
        this.mContext = context;
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        setContentView(R.layout.bocroa_dialog_progress);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.width = width * 3 / 4;
		lp.height = width / 3;
		getWindow().setAttributes(lp);
		setCancelable(false);
		initView();
    }
	
    protected void initView(){
    	
    	closeButton = (Button) findViewById(R.id.btnClose);
    	closeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				LoadingDialog.this.cancel();
				new Thread(){
					public void run() {
						MAOPRequestManager.cancelAllRequest();
				    	};
					}.start();
				    
//					if(monLoadingClick!=null){
//						monLoadingClick.onLoadingCloseClick();
//					}
			}
		});
        
    }
    
    /**
     * 设置关闭按钮是否显示
     * @param isDisplay
     */
    public void setCloseDispaly(boolean isDisplay){
    	if (isDisplay) {
    		closeButton.setVisibility(View.VISIBLE);
    	} else {
    		closeButton.setVisibility(View.GONE);
    	}
    }

}
