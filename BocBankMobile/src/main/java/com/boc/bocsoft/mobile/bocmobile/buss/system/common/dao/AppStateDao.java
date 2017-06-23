package com.boc.bocsoft.mobile.bocmobile.buss.system.common.dao;

import android.database.Cursor;

import com.boc.bocma.tools.LogUtil;
import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;

/**
 * 应用状态Dao类
 * Created by lxw on 2016/9/21 0021.
 */
public class AppStateDao extends BaseDao{

    private final static String LOG_TAG = AppStateDao.class.getSimpleName();
    // 插入数据
    private static final String  INSERT_TR_APP_STATE_DEFAULT = "insert into tr_app_state(app_key, state) values (?, ?)";
    // 查询数据
    private static final String QUERY_TR_APP_STATE = "select app_key, state from tr_app_state where app_key = ?";

    /**
     * 更新应用状态
     * @param appKey
     */
    public void updateAppState(String appKey){

        try{
            if (!hasValue(appKey)) {
                this.execSQL(INSERT_TR_APP_STATE_DEFAULT, new String[]{appKey, "1"});
            }
        } catch (Exception ex){
            LogUtils.e(LOG_TAG, "updateAppState执行时发生异常:"  + ex.getMessage());
        }

    }

    /**
     * 当前app状态是否有值
     * @param appKey
     * @return
     */
    public boolean hasValue(String appKey){
        Cursor cursor = this.query(QUERY_TR_APP_STATE, new String[]{appKey});
        try {
            if (cursor.getCount() != 0) {
                cursor.close();
                return true;
            } else {
                cursor.close();
                return false;
            }

        } catch (Exception ex){
            LogUtils.e(LOG_TAG, ex.getMessage());
            return false;
        } finally {
            if(!cursor.isClosed()){
                cursor.close();
            }
        }
    }
}
