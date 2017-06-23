package com.boc.bocsoft.mobile.bocmobile.buss.system.invest.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.boc.bocsoft.mobile.bocmobile.base.db.BaseDao;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单Dao
 * Created by lxw on 2016/7/5 0005.
 */
public class InvestMenuDao extends BaseDao {

    private final static String TABLE_NAME = "tr_invest_saved_menu";
    // 查询用户保存的模块列表
    private final static String QUERY_INVEST_SAVED_MENU_LIST = "select " +
                                                "T1.module_id, " +
                                                "T1.module_name, " +
                                                "T1.icon_id " +
                                                "from tr_invest_saved_menu T1 " +
                                                "order by order_id asc";
    // 删除用户保留的模块列表
    private final static String DELETE_INVEST_SAVED_MENU_LIST = "delete from tr_invest_saved_menu";

    /**
     * 查询用户保存的模块列表
     * @return
     */
    public List<Item> queryInvestSaveMenuList(){
        Cursor cursor = this.query(QUERY_INVEST_SAVED_MENU_LIST, new String[]{});
        List<Item> items = new ArrayList<>();
        while(cursor.moveToNext()){
            Item item = new Item();
            String moduleId = cursor.getString(cursor.getColumnIndex("module_id"));
            String moduleName = cursor.getString(cursor.getColumnIndex("module_name"));
            String iconId = cursor.getString(cursor.getColumnIndex("icon_id"));
            item.setModuleId(moduleId);
            item.setTitle(moduleName);
            item.setIconId(iconId);
            items.add(item);
        }
        return items;
    }

    /**
     * 更新菜单列表
     * @param items
     */
    public void updateMenuList(List<Item> items) throws Exception{
        this.db.beginTransaction();
        try{
            db.execSQL(DELETE_INVEST_SAVED_MENU_LIST);

            int index = 0;
            for (Item item : items) {
                ContentValues values = new ContentValues();
                values.put("module_id", item.getModuleId());
                values.put("module_name", item.getTitle());
                values.put("order_id", index);
                values.put("icon_id", item.getIconId());
                index++;

                db.insert(TABLE_NAME, "",values);

            }

            this.db.setTransactionSuccessful();
        }catch (Exception ex){
            throw ex;
        } finally {
            this.db.endTransaction();
        }
    }
}
