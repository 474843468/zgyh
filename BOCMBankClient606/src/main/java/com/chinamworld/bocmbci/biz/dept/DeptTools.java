package com.chinamworld.bocmbci.biz.dept;

import android.app.Activity;
import android.content.Intent;

import com.chinamworld.bocmbci.base.application.CommonApplication;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.bii.constant.Comm;
import com.chinamworld.bocmbci.biz.dept.myreg.MyPositDrawMoneyActivity;
import com.chinamworld.bocmbci.biz.dept.myreg.MyRegSaveChooseTranInAccActivity;
import com.chinamworld.bocmbci.constant.ConstantGloble;
import com.chinamworld.bocmbci.http.engine.BaseHttpEngine;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utiltools.HttpHandle;

import java.util.List;
import java.util.Map;

/**
 * DeptTools.class 存款管理 模块入口管理
 *
 * @author luqp 2016/10/14
 */
public class DeptTools extends HttpHandle {
    private Activity mActivity;
    private String depositsTypes;
    private Map<String ,Object> accountMaps;
    public DeptTools(Activity activity) {
        super(activity);
        mActivity = activity;
        CommonApplication.getInstance().setCurrentAct(activity); // 从软件中心模块跳过来，需要先保存当前的activity
    }

//    "continueSave" 续存标识
//     "convertType" 转存
    /** 当前选择的账户详情 */
//    Map<String, Object> accountMap ;  "PsnAccountQueryAccountDetail"

    /**
     * 支取 入口
     *
     * @param activity
     * @param accountMap 用戶选择账户
     * @param selectAccountMap 选择账户信息 -- PsnCommonQueryAllChinaBankAccount查询中行所有帐户列表
     * @oaram notifyMgFlag 1 通知 2 定期一本通
     */
    public void gotoWithdrawDeposits(Activity activity, Map<String, Object> accountMap, Map<String, Object> selectAccountMap, String depositsType){
        this.mActivity = activity;
        depositsTypes = depositsType;
        accountMaps = accountMap;
        accountMap.putAll(selectAccountMap);  // 合并两个几个并去重
        DeptDataCenter.getInstance().setAccountContentMap(accountMap); //用来储存外来模块用户选择数据
        requestSystemDateTime(); // 查询系统时间
    }

    public void requestSystemDateTime() {
        BaseHttpEngine.showProgressDialog(); //查询系统时间
        httpTools.requestHttp(Comm.QRY_SYSTEM_TIME, "requestSystemDateTimeCallBack", null);
    }

    public void requestSystemDateTimeCallBack(Object resultObj) {
        BaseHttpEngine.dissMissProgressDialog();
        BiiResponse biiResponse = (BiiResponse)resultObj;
        List<BiiResponseBody> biiResponseBodys = biiResponse.getResponse();
        BiiResponseBody biiResponseBody = biiResponseBodys.get(0);
        Map<String, Object> resultMap = (Map<String, Object>)biiResponseBody.getResult();
        String dateTime = (String)resultMap.get(Comm.DATETME);
        // 如果用户选择的账户类型 教育储蓄 教育储蓄 定期一本通
        if (!accountMaps.get("type").equals(DeptBaseActivity.EDUCATION_SAVE1)
                && !accountMaps.get("type").equals(DeptBaseActivity.ZERO_SAVE1)
                && !accountMaps.get("type").equals(DeptBaseActivity.RANDOM_ONE_SAVE)) {
            Intent intent = new Intent();
            intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
            intent.putExtra("isFromBoc", true);
            intent.setClass(mActivity, MyRegSaveChooseTranInAccActivity.class);
            mActivity.startActivity(intent);
        } else {
            if (!accountMaps.get("type").equals(DeptBaseActivity.EDUCATION_SAVE1)
                    && !accountMaps.get("type").equals(DeptBaseActivity.ZERO_SAVE1)) {
                DeptDataCenter.getInstance().setCurDetailContent(accountMaps);
                Intent intent = new Intent();
                intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
                intent.setClass(mActivity, MyPositDrawMoneyActivity.class);
                mActivity.startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtra(ConstantGloble.DATE_TIEM, dateTime);
                intent.putExtra("isFromBoc", true);
                intent.setClass(mActivity, MyRegSaveChooseTranInAccActivity.class);
                mActivity.startActivity(intent);
            }
        }
    }

}
