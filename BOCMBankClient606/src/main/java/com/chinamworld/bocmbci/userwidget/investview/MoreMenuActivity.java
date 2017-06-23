package com.chinamworld.bocmbci.userwidget.investview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.activity.NewStyleBaseActivity;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */
public class MoreMenuActivity extends NewStyleBaseActivity {
    @Override
    public ActivityTaskType getActivityTaskType() {
        return ActivityTaskType.TwoTask;
    }

    private LinearLayout mRootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootLayout = new LinearLayout(this);
        mRootLayout.setOrientation(LinearLayout.VERTICAL);
        mRootLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        setContentView(mRootLayout);
        if(mMenuList == null)
            return;
        for(int i = 0; i < mMenuList.size();i++){
            mRootLayout.addView(createMenuItemView(mMenuList.get(i)));
        }
    }

    /** 跳转到更多页面 */
    public static void gotoMoreMenuActivity(Activity activity,List<MoreMenuItem> menuList, IMoreMenuClickListener clickListener){
        mMenuList = menuList;
        mIMoreMenuClickListener = clickListener;
        ActivityIntentTools.intentToActivity(activity,MoreMenuActivity.class);
    }


    private View createMenuItemView(MoreMenuItem menuItem){
        View v = LayoutInflater.from(this).inflate(R.layout.more_menu_item_layout,null);
        ((TextView)v.findViewById(R.id.menuText)).setText(menuItem.getMenuStr());;
        View rootView = v.findViewById(R.id.menuLayout);
        rootView.setTag(menuItem);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mIMoreMenuClickListener != null){
                    mIMoreMenuClickListener.onMoreMenuItemClick((MoreMenuItem)v.getTag());
                }
            }
        });

        return v;
    }

    protected static List<MoreMenuItem> mMenuList;
    protected static IMoreMenuClickListener mIMoreMenuClickListener;

    public interface IMoreMenuClickListener{
        void onMoreMenuItemClick(MoreMenuItem menuItem);
    }

   }
