package com.boc.bocsoft.mobile.bocmobile.buss.login.dao;

import android.database.Cursor;

import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录过的历史账号DAO
 * Created by lxw on 2016/7/11 0011.
 */
public class LoginAccountHistoryDao extends BaseDao {

    private final static String INSERT_ACCOUNT = "insert into tr_login_account_history (account_id, create_date) values (?, datetime('now'))";

    private final static String QUERY_ACCOUNT_LIST = "select account_id from tr_login_account_history order by create_date limit ?";
    /**
     * 添加账号
     * @param accountId
     */
    public void addAccount(String accountId){
        execSQL(INSERT_ACCOUNT, new String[]{accountId});
    }

    /**
     * 查询账户列表
     * @return
     */
    public List<String> queryAccountList(String rowNumber){
        Cursor cursor = query(QUERY_ACCOUNT_LIST, new String[]{rowNumber});
        List<String> result = new ArrayList<>();
        while(cursor.moveToNext()){
            String accountId = cursor.getString(cursor.getColumnIndex("account_id"));
            result.add(accountId);
        }
        return result;
    }
}
