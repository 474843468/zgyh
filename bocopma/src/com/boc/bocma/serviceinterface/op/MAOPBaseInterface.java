package com.boc.bocma.serviceinterface.op;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.boc.bocma.exception.MAOPException;
import com.boc.bocma.global.config.MAGlobalConfig;
import com.boc.bocma.network.communication.http.MAOPHttpUtils;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseParamsModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel;
import com.boc.bocma.serviceinterface.op.interfacemodel.MAOPBaseResponseModel.Creator;
import com.boc.bocma.test.MAMessageWarehouse;
import com.boc.bocma.tools.LogUtil;
import com.boc.bocma.tools.StringUtils;

public class MAOPBaseInterface {
    private Context mContext;
    
    public MAOPBaseInterface (Context context) {
        mContext = context;
    }
    
    private static String getResponse(Context context, MAOPBaseParamsModel params) throws Exception {
        String response;
        LogUtil.i("request: " + params.getJsonBody());
        if (MAGlobalConfig.ISDEMO) {
            
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            MAMessageWarehouse.getResponseContent(context,
                                    null,
                                    params.getClass().getSimpleName()), "UTF-8"));

            String str = "";
            StringBuilder sb = new StringBuilder();
            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            bufferedReader.close();
            response = sb.toString();
            //LogUtil.d("response: " + response);
        } else {
            response  = MAOPHttpUtils.getStringFromWeb(
                     params.getUrl(), 
                     params.getMethod(), 
                     params.getHead(), params.getJsonBody());
            MAMessageWarehouse.writeResponseContent(null, params.getClass().getSimpleName(), new ByteArrayInputStream(response
                                .getBytes("UTF-8")));
        }
        return response;
    }
    
    public <T extends MAOPBaseResponseModel> T executeSynchronous(MAOPBaseParamsModel params, 
           T targetModelClass) throws MAOPException {
        MAOPException opException = null;
        T result = null;
        try {
            String response  = getResponse(mContext, params);
            Field field = targetModelClass.getClass().getDeclaredField(Creator.FIELD_NAME);
            @SuppressWarnings("unchecked")
            Creator<T> creator = (Creator<T>) field.get(null);
            JSONObject jsonResponse = new JSONObject(response);
            if (jsonResponse.isNull(MAOPException.MSG_CODE_KEY)) {
                result = creator.createFromJson(jsonResponse);
            } else {
                opException = new MAOPException();
                opException.setMsgCode(jsonResponse.optString(MAOPException.MSG_CODE_KEY));
                opException.setMessage(jsonResponse.optString(MAOPException.RTN_MSG_KEY));
            }
        } catch (Exception e) {
            e.printStackTrace();
            opException = new MAOPException(e);
        }
        if (opException != null) throw opException;
        return result;
    }
    
    protected void execute(MAOPBaseParamsModel paramsModel, Class<? extends MAOPBaseResponseModel> targetModelClass, OnOPResultCallback callback) {
        MAOPAsyncTask task = new MAOPAsyncTask(mContext, callback, targetModelClass);
        MAOPRequestManager.addTask(task);
        task.execute(paramsModel);
    }
    
    private static class MAOPAsyncTask extends AsyncTask<MAOPBaseParamsModel, Void, MAOPBaseResponseModel> {
        private OnOPResultCallback mCallback;
        private Class<? extends MAOPBaseResponseModel> modelClass;
        boolean isSuccess = false;
        private Context mContext;
        
        public MAOPAsyncTask(Context context, OnOPResultCallback callback, Class<? extends MAOPBaseResponseModel> targetClass) {
            this.mContext = context;
            this.mCallback = callback;
            modelClass = targetClass;
        }
        
        @Override
        protected MAOPBaseResponseModel doInBackground(MAOPBaseParamsModel... paramsList) {
            MAOPBaseResponseModel result = new MAOPBaseResponseModel();
            isSuccess = false;
            try {
                MAOPBaseParamsModel params = paramsList[0];
                String response = getResponse(mContext, params);
                LogUtil.i("response: " + response);

                Field field = modelClass.getDeclaredField(Creator.FIELD_NAME);
                @SuppressWarnings("unchecked")
                Creator<? extends MAOPBaseResponseModel> creator = 
                (Creator<? extends MAOPBaseResponseModel>) field.get(null);
                JSONObject jsonResponse = new JSONObject(response);
                //有的接口会返回msgcde和rtnmsg（身份证上传），如果都不为空认为失败
                String msgcde=jsonResponse.optString(MAOPBaseResponseModel.MSGCDE);
                String rtnmsg=jsonResponse.optString(MAOPBaseResponseModel.RTNMSG);
                if (!StringUtils.isEmptyOrNull(msgcde)&&!StringUtils.isEmptyOrNull(rtnmsg)) {
                	isSuccess = false;
					MAOPException opException = new MAOPException();
					opException.setMsgCode(msgcde);
					opException.setMessage(rtnmsg);
					result.setException(opException);
				}else {
					JSONObject responseStatus = jsonResponse.optJSONObject(MAOPBaseResponseModel.RESPONSE_STATUS);
					if (responseStatus == null){
						isSuccess = true;
						result = creator.createFromJson(jsonResponse);
					} else {
						String reponseCode = responseStatus.optString(MAOPBaseResponseModel.RESPONSE_CODE);
						String reponseMsg = responseStatus.optString(MAOPBaseResponseModel.RESPONSE_MSG);
						//远程开户进度查询成功返回结果reponseCode为空，所以加reponseCode为空成功逻辑，lgw2016.3.23
						if (MAOPBaseResponseModel.RESPONSE_OK.equals(reponseCode)||(StringUtils.isEmpty(reponseCode))) {
							isSuccess = true;
							result = creator.createFromJson(jsonResponse);
						} else {
							isSuccess = false;
							MAOPException opException = new MAOPException();
							
							opException.setMsgCode(reponseCode);
							opException.setMessage(reponseMsg);
							result.setException(opException);
						}
					}
				}

            } catch (Exception e) {
                /*
                 * TODO: throw BOCOPException,JSONException,NoSuchFieldException,IllegalArgumentException,
                 * IllegalAccessException, catch specific Exception in case we need to do something else.
                 */
                MAOPException opException = new MAOPException();
            	opException.setMessage("网络异常，请稍后重试");
                e.printStackTrace();
                result.setException(opException);
                isSuccess = false;
            }
            return result;
        }
        
        @Override
        public void onPostExecute(MAOPBaseResponseModel result) {
            if (mCallback == null) return;
            //TODO: 处理通用错误情况，如登录token过期，网络异常等（http://22.188.1.77/mediawiki/index.php/错误码一览表）
            if (isSuccess) {
                mCallback.onSuccess(result);
            } else {
                mCallback.onFault(result.getException());
            }
        }
    }
}
