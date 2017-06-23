package com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao;

import android.database.Cursor;

import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 优选投资DAO类
 * Created by lxw on 2016/9/6 0006.
 */
public class HomeInvestDao extends BaseDao {

    private final static String TABLE_NAME = "tr_home_saved_invest";

    // 查询用户保存的模块列表
    private final static String QUERY_HOME_SAVED_LIST = "select " +
            "T1.invest_id, " +
            "T1.invest_name, " +
            "T1.order_id, " +
            "T1.type_code " +
            "from tr_home_saved_invest T1 " +
            "order by order_id asc";

    // 删除用户保留的模块列表
    private final static String DELETE_HOME_SAVED_INVEST_LIST = "delete from tr_home_saved_invest";

    // 插入用户保留的模块列表
    private final static String INSERT_HOME_SAVED_INVEST_LIST = "insert into " +
            "tr_home_saved_invest(invest_id, invest_name, order_id, type_code) values (?,?,?,?)";


    /**
     * 更新菜单列表
     * @param items
     */
    public void updateInvestList(List<InvestItemBO> items) throws Exception{
        this.db.beginTransaction();
        try{
            db.execSQL(DELETE_HOME_SAVED_INVEST_LIST);

            for (InvestItemBO item : items) {
                db.execSQL(INSERT_HOME_SAVED_INVEST_LIST,
                        new Object[]{item.getInvestId(),
                                    item.getInvestName(),
                                    item.getOrderId(),
                                    InvestItemBO.convertTypeToCode(item.getType())});
            }

            this.db.setTransactionSuccessful();
        }catch (Exception ex){
            throw ex;
        } finally {
            this.db.endTransaction();
        }
    }

    /**
     * 查询用户保存的模块列表
     * @return
     */
    public List<InvestItemBO> queryUserInvestList(){
        Cursor cursor = this.query(QUERY_HOME_SAVED_LIST, new String[]{});
        List<InvestItemBO> items = new ArrayList<>();
        while(cursor.moveToNext()){
            InvestItemBO item = new InvestItemBO();
            String investId = cursor.getString(cursor.getColumnIndex("invest_id"));
            String investName = cursor.getString(cursor.getColumnIndex("invest_name"));
            String typeCode = cursor.getString(cursor.getColumnIndex("type_code"));
            int orderId = cursor.getInt(cursor.getColumnIndex("order_id"));
            item.setInvestId(investId);
            item.setInvestName(investName);
            item.setOrderId(orderId);
            item.setType(InvestItemBO.convertCodeToType(typeCode));
            items.add(item);
        }
        return items;
    }

}
