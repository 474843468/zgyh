package com.boc.bocsoft.mobile.bocmobile.buss.system.home.dao;

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
public class HomeMenuDao extends BaseDao {

    private final static String TABLE_NAME = "tr_home_saved_menu";
    // 查询用户保存的模块列表
    private final static String QUERY_HOME_SAVED_MENU_LIST = "select " +
                                                "T1.module_id, " +
                                                "T1.module_name, " +
                                                "T1.order_id, " +
                                                "T1.icon_id " +
                                                "from tr_home_saved_menu T1 " +
                                                "order by order_id asc";
    // 删除用户保留的模块列表
    private final static String DELETE_HOME_SAVED_MENU_LIST = "delete from tr_home_saved_menu";

    // 插入用户保留的模块列表
    private final static String INSERT_HOME_SAVED_MENU_LIST = "insert into " +
                                                        "tr_home_saved_menu(module_id, module_name, icon_id, order_id) values (?, ?, ?, ?)";

    // 初始化数据
    private static final String  INSERT_TR_APP_STATE_DEFAULT = "insert into tr_app_state(app_key, state) values (?, ?)";

    // 查询数据
    private static final String QUERY_TR_APP_STATE = "select app_key, state from tr_app_state where app_key = ?";

    /**
     * 查询用户保存的模块列表
     * @return
     */
    public List<Item> queryUserSaveMenuList(){
        Cursor cursor = this.query(QUERY_HOME_SAVED_MENU_LIST, new String[]{});
        List<Item> items = new ArrayList<>();
        while(cursor.moveToNext()){
            Item item = new Item();
            String moduleId = cursor.getString(cursor.getColumnIndex("module_id"));
            String moduleName = cursor.getString(cursor.getColumnIndex("module_name"));
            String iconId = cursor.getString(cursor.getColumnIndex("icon_id"));
            int order = cursor.getInt(cursor.getColumnIndex("order_id"));
            item.setModuleId(moduleId);
            item.setTitle(moduleName);
            item.setIconId(iconId);
            item.setOrder(order);
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
            Cursor cursor = this.query(QUERY_TR_APP_STATE, new String[]{"home_menu"});
            if (cursor.getCount() == 0) {
                db.execSQL(INSERT_TR_APP_STATE_DEFAULT, new String[]{"home_menu", "1"});
            }
            db.execSQL(DELETE_HOME_SAVED_MENU_LIST);

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                db.execSQL(INSERT_HOME_SAVED_MENU_LIST, new Object[]{item.getModuleId(), item.getTitle(),  item.getIconId(), i});
            }

            this.db.setTransactionSuccessful();
        }catch (Exception ex){
            throw ex;
        } finally {
            this.db.endTransaction();
        }
    }

}
