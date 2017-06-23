package com.boc.bocsoft.mobile.bocmobile.base.widget.selectwheel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.List;



public class WheelPopupwidnow extends PopupWindow {

	private View rootView;
	private Context mContext;
	private List<String> listContent;
	
	private OnCommitClickListener listener;
	
	private String selectContent;
	
	public WheelPopupwidnow(Activity mContext, List<String> listContent){
		this.mContext=mContext;
		this.listContent=listContent;
		LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		rootView = inflater.inflate(R.layout.view_pop_wheel,null);
		Button pop_cancle=(Button) rootView.findViewById(R.id.pop_cancle);
		Button pop_commit=(Button) rootView.findViewById(R.id.pop_commit);
		WheelView popwheel=(WheelView) rootView.findViewById(R.id.popwheel);
		popwheel.setOffset(1);
		popwheel.setItems(listContent);
		popwheel.setOnWheelViewListener(new WheelView.OnWheelViewListener(){
			@Override
			public void onSelected(int selectedIndex, String item) {
				selectContent=item;
				
			}
		});
		//取消按钮点击事件
		pop_cancle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WheelPopupwidnow.this.dismiss();
			}
		});
		
		//确定按钮点击事件
		pop_commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				listener.OnCommitClick(selectContent);
				WheelPopupwidnow.this.dismiss();
			}
		});
		
        // 设置SelectPicPopupWindow的View  
        this.setContentView(rootView);  
        // 设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        this.setOutsideTouchable(true);  
        // 刷新状态  
        this.update();  
        // 实例化一个ColorDrawable颜色为半透明  
//        ColorDrawable dw = new ColorDrawable(0000000000);  
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作  
        this.setBackgroundDrawable(new BitmapDrawable());
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);  
	   
	        
	}
	
	 /** 
     * 显示popupWindow 
     *  
     * @param parent 
     */  
    public void showPopupWindow(View parent) {  
        if (!this.isShowing()) {  
            // 以下拉方式显示popupwindow  
            this.showAtLocation(parent, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        } else {  
            this.dismiss();  
        }  
    }  
    
    public interface OnCommitClickListener{
    	void OnCommitClick(String listItem);//确定按钮点击事件
    }
    
    public void setonCommitClickListener(OnCommitClickListener listener){
    	this.listener=listener;
    }
	
}
