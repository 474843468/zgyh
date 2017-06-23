package com.chinamworld.bocmbci.utiltools;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.chinamworld.bocmbci.R;
import com.chinamworld.bocmbci.abstracttools.AbstractLoginTool;
import com.chinamworld.bocmbci.abstracttools.BaseHttpManager;
import com.chinamworld.bocmbci.abstracttools.BaseRUtil;
import com.chinamworld.bocmbci.abstracttools.ShowDialogTools;
import com.chinamworld.bocmbci.base.activity.ActivityTaskManager;
import com.chinamworld.bocmbci.bii.BiiError;
import com.chinamworld.bocmbci.bii.BiiResponse;
import com.chinamworld.bocmbci.bii.BiiResponseBody;
import com.chinamworld.bocmbci.constant.BaseLocalData;
import com.chinamworld.bocmbci.http.HttpTools;
import com.chinamworld.bocmbci.http.IHttpRequestHandle;
import com.chinamworld.bocmbci.utils.ActivityIntentTools;
import com.chinamworld.bocmbci.utils.LoginTask;
import com.chinamworld.bocmbci.utils.StringUtil;
import com.chinamworld.bocmbci.utils.Utils;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HttpHandle implements IHttpRequestHandle {
  protected   HttpTools httpTools;

    @Override
    public void commonHttpErrorCallBack(String s) {

    }

    Activity currentAct;
    public HttpHandle(Activity activity){
        currentAct= activity;
        httpTools = new HttpTools(activity,this);
    }



    @Override
    public boolean doBiihttpRequestCallBackPre(BiiResponse response) {

        List biiResponseBodyList = response.getResponse();
        if(StringUtil.isNullOrEmpty(biiResponseBodyList)) {
            return false;
        } else {
            Iterator var4 = biiResponseBodyList.iterator();

            while(var4.hasNext()) {
                BiiResponseBody body = (BiiResponseBody)var4.next();
                if(!"01".equals(body.getStatus())) {
                    ShowDialogTools.Instance.dissMissProgressDialog();
                    if("Logout".equals(body.getMethod())) {
                        return false;
                    }

                    if(!"PSNBmpsCloseConversation".equals(body.getMethod()) && !"PsnGetTicketForMessage".equals(body.getMethod()) && !"insertTranSeq".equals(body.getMethod())) {
                        BiiError biiError = body.getError();
                        if(biiError == null) {
                            ShowDialogTools.Instance.dismissErrorDialog();
                            ShowDialogTools.Instance.createDialog("", currentAct.getResources().getString(R.string.request_error), new View.OnClickListener() {
                                public void onClick(View v) {
                                    ShowDialogTools.Instance.dismissErrorDialog();
                                    if(BaseHttpManager.Instance.getcanGoBack()) {
                                        currentAct.finish();
                                        BaseHttpManager.Instance.setCanGoBack(false);
                                    }

                                }
                            });
                            return true;
                        }

                        if(biiError.getCode() == null) {
                            showHttpErrorDialog(currentAct,"", biiError.getMessage());
                            return true;
                        }

                        BaseLocalData.Code_Error_Message.errorToMessage(body);
                        if(BaseLocalData.timeOutCode.contains(biiError.getCode())) {
                            showTimeOutDialog(currentAct,biiError.getMessage());
                            return true;
                        }
                        if(this.httpTools != null && this.httpTools.IsInterceptErrorCode(body.getMethod(), biiError.getCode())) {
                            return this.doHttpErrorHandler(body.getMethod(), biiError);
                        }


                        if(biiError.getCode().equals("role.no.invest.service")) {
                            ShowDialogTools.Instance.showErrorDialog(biiError.getMessage(), BaseRUtil.Instance.getID("R.string.cancle"), BaseRUtil.Instance.getID("R.string.confirm"), new View.OnClickListener() {
                                public void onClick(View v) {
                                    switch(Integer.parseInt("" + v.getTag())) {
                                        case 6:
                                            ActivityIntentTools.intentToActivityByClassName(currentAct, "InvesHasOpenActivity");
                                            ShowDialogTools.Instance.dismissErrorDialog();
                                            if(BaseHttpManager.Instance.getcanGoBack()) {
                                                currentAct.finish();
                                                BaseHttpManager.Instance.setCanGoBack(false);
                                            }
                                            break;
                                        case 7:
                                            ShowDialogTools.Instance.dismissErrorDialog();
                                            if(BaseHttpManager.Instance.getcanGoBack()) {
                                                currentAct.finish();
                                                BaseHttpManager.Instance.setCanGoBack(false);
                                            }
                                    }

                                }
                            });
                        } else {
                           showHttpErrorDialog(currentAct,biiError.getCode(), biiError.getMessage());
                        }

                        return true;
                    }

                    return false;
                }
            }

            return false;
        }
    }

    @Override
    public boolean doBiihttpRequestCallBackAfter(BiiResponse biiResponse) {
        return false;
    }

    protected void showHttpErrorDialog(final Activity currentAct, String errorCode, String message) {
        ShowDialogTools.Instance.dismissErrorDialog();
        ShowDialogTools.Instance.createDialog(errorCode, message, new View.OnClickListener() {
            public void onClick(View v) {
                ShowDialogTools.Instance.dismissErrorDialog();
                if(BaseHttpManager.Instance.getcanGoBack()) {
                    currentAct.finish();
                    BaseHttpManager.Instance.setCanGoBack(false);
                }

            }
        });
    }
    protected void showTimeOutDialog(final Activity currentAct, String message) {
        ShowDialogTools.Instance.showMessageDialog(message, new View.OnClickListener() {
            public void onClick(View v) {
                ShowDialogTools.Instance.dismissErrorDialog();
                ActivityTaskManager.getInstance().removeAllActivity();
                ActivityTaskManager.getInstance().removeAllSecondActivity();
//                Intent intent = new Intent();
//                intent.setClassName(currentAct, "com.chinamworld.bocmbci.biz.login.LoginActivity");
//                currentAct.startActivityForResult(intent, 1);
//                new LoginTask(currentAct).exe(new LoginTask.LoginCallback() {
//                    @Override
//                    public void loginStatua(boolean isLogin) {
//                    }
//                });
                AbstractLoginTool.Instance.Login(currentAct, new LoginTask.LoginCallback(){
                    @Override
                    public void loginStatua(boolean isLogin) {
                    }
                });
            }
        });
    }

    public boolean doHttpErrorHandler(String method, BiiError biiError) {
        return false;
    }
}
