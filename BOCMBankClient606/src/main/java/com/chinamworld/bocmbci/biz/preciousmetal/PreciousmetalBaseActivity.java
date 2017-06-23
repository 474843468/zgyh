package com.chinamworld.bocmbci.biz.preciousmetal;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.BaseHttpManager;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.abstracttools.ShowDialogTools;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.base.activity.BaseActivity;
import com.chinamworld.bocmbci.base.application.BaseDroidApp;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMainActivity;
import com.chinamworld.bocmbci.biz.preciousmetal.goldstoremainpage.GoldstoreMoreActivity;
import com.chinamworld.bocmbci.constant.BaseLocalData;
import com.chinamworld.bocmbci.constant.LocalData;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.http.engine.BiiHttpEngine;
import com.chinamworld.bocmbci.log.LogGloble;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.llbt.userwidget.NewLabelTextView;
import com.chinamworld.llbt.userwidget.dialogview.MessageDialog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 贵金属积存基类
 * Created by linyl on 2016/8/25.
 */
public class PreciousmetalBaseActivity extends BaseActivity {
    private int whichSelect = 0;
    //报错信息
    private String message;
    /**
     * 背景组件
     */
    private PreviousmeatalBackgroundLayout backgroundLayout = null;

    /**
     * 获得背景组件
     */
    public PreviousmeatalBackgroundLayout getPreviousmeatalBackgroundLayout() {
        return backgroundLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backgroundLayout = new PreviousmeatalBackgroundLayout(this);
        super.setContentView(backgroundLayout);
        backgroundLayout.init(this);
        backgroundLayout.setPaddingWithParent(0, 0, 0, 0);
    }

    @Override
    public void setContentView(int layoutResID) {
        backgroundLayout.addView(LayoutInflater.from(this).inflate(layoutResID, backgroundLayout, false));
    }


    @Override
    public ActivityTaskType getActivityTaskType() {
        return ActivityTaskType.TwoTask;
    }

    /**
     * 创建详情页面文本显示LabelTextView控件
     *
     * @param resid
     * @param valuetext
     * @return
     */
    public NewLabelTextView createNewLabelTextView(int resid, String valuetext) {
        NewLabelTextView v = new NewLabelTextView(this);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setLabelText(resid);
        v.setValueText(StringUtil.isNullChange(valuetext));
//        v.setWeightShowRate("1:2");
        return v;
    }

    /**
     * 创建详情页面文本显示LabelTextView控件 valuetext 为空 不显示-
     *
     * @param resid
     * @param valuetext
     * @return
     */
    public NewLabelTextView createNewLabelTextViewTwo(int resid, String valuetext) {
        NewLabelTextView v = new NewLabelTextView(this);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v.setLabelText(resid);
        v.setValueText(valuetext);
//        v.setWeightShowRate("1:2");
        return v;
    }

    /**
     * 外置和登陆首页的选择金属时的弹窗
     */
    public int ShowMetalList(Activity activity, List<Object> arrayList) {
        View dialogView = View.inflate(activity, R.layout.goldstore_choselist, null);
        ListView choseList = (ListView) dialogView.findViewById(R.id.chose_list);
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < arrayList.size(); i++) {
            Map<String, Object> map = (Map<String, Object>) arrayList.get(i);
            data.add((String) map.get(PreciousmetalDataCenter.CURRCODENAME));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        choseList.setAdapter(adapter);

        choseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                whichSelect = position;
                BaseDroidApp.getInstanse().closeAllDialog();
            }
        });
        BaseDroidApp.getInstanse().showDialog(dialogView);

        return whichSelect;
    }

    /**
     * 返回贵金属积存 首页的方法
     **/
    public void goGoldstoreMain() {
        ActivityTaskManager.getInstance().removeAllSecondActivity();
        Intent intent = new Intent(this, GoldstoreMainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 贵金属积存 账户查询接口
     */
    public void requsetPsnGoldStoreAccountQuery() {
        this.getHttpTools().requestHttpWithNoDialog(PreciousmetalDataCenter.PSNGOLDSTOREACCOUNTQUERY,
                "requestPsnGoldStoreAccountQueryCallback", null,
                false);

    }

    public void requestPsnGoldStoreAccountQueryCallback(
            Object resultObj) {
        Map<String, Object> resultMapseven = HttpTools.getResponseResult(resultObj);
        if (StringUtil.isNullOrEmpty(resultMapseven)) {
            return;
        }
        PreciousmetalDataCenter.getInstance().ACCOUNTQUERYMAP = resultMapseven;
        }
    //网络异常时触发
    @Override
    public void commonHttpErrorCallBack(final String requestMethod) {
        if("Logout".equals(requestMethod) || "PNS001".equals(requestMethod)){
            return;
        }

        String message = this.getResources().getString(BaseRUtil.Instance.getID("R.string.communication_fail"));
        LogGloble.e("BaseActivity", "请求失败的接口名称" + requestMethod);
        MessageDialog.showMessageDialog(PreciousmetalBaseActivity.this,message);
        MessageDialog.showMessageDialog(PreciousmetalBaseActivity.this,message, new View.OnClickListener() {
            public void onClick(View v) {
                ShowDialogTools.Instance.dismissErrorDialog();
                MessageDialog.closeDialog();
                if(BaseHttpManager.Instance.getcanGoBack() || BaseLocalData.queryCardMethod.contains(requestMethod)) {
                    PreciousmetalBaseActivity.this.finish();
                    BaseHttpManager.Instance.setCanGoBack(false);
                }

            }
        });

    }
    //对报错信息进行处理
    @Override
    public boolean doBiihttpRequestCallBackPre(BiiResponse response) {
        BaseHttpEngine.dissMissProgressDialog();
        List<BiiResponseBody> biiResponseBodyList = response.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodyList.get(0);
        if (!StringUtil.isNullOrEmpty(biiResponseBodyList)) {
            for (BiiResponseBody body : biiResponseBodyList) {

                    BiiHttpEngine.dissMissProgressDialog();
                    BiiError biiError = biiResponseBody.getError();

                    // 判断是否存在error
                    if (biiError != null && biiError.getCode() != null) {
                    message=biiError.getMessage();
                        MessageDialog.showMessageDialog(PreciousmetalBaseActivity.this,message);
                        return true;
                    }
                }

        } else {
            return super.doBiihttpRequestCallBackPre(response);
        }
        return super.doBiihttpRequestCallBackPre(response);
    }

    public static Map<String, String> Accountstyle = new LinkedHashMap<String, String>() {

        private static final long serialVersionUID = 1L;

        {//
            put("188", "活期一本通");
            put("119", "借记卡");
        }
    };

}
