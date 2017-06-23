package com.boc.bocsoft.mobile.bocmobile.base.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.boc.bocsoft.mobile.bocmobile.yun.db.YunDB;

/**
 * 移动手机数据库
 * Created by Administrator on 2016/5/21.
 */
public class BocMobileDB extends SQLiteOpenHelper {

    // 数据库名
    private static final String DATABASE_NAME = "boc_mobile_database.db";

    // 数据库版本 （比以前版本大1，从1开始）
    private static final int DATABASE_VERSION = 1;

    // 当前版本
    private int currentVersion;
    // 是否是新建数据库
    private boolean isNewInstall;

    /*************************************************************************
     *  建表sql脚本
     *************************************************************************/

    // 数据库表名
//    private static final String CREATE_TABLE_TM_MODULE =
//            "create table if not exists tm_module (module_id text primary key," +
//                    "module_name text," +
//                    "parent_id text," +
//                    "default_flg integer," +
//                    "order_id integer," +
//                    "icon_id text)" ;
    // 首页菜单
    private static final String CREATE_TABLE_TR_HOME_SAVED_MODULE =
            "create table if not exists tr_home_saved_menu (module_id text primary key," +
                    "module_name text," +
                    "order_id integer," +
                    "icon_id text)";

    // 首页优选投资
    private static final String CREATE_TABLE_TR_HOME_SAVED_INVEST =
            "create table if not exists tr_home_saved_invest (invest_id text primary key," +
                    "type_code text," +
                    "invest_name text," +
                    "order_id integer)";

    private static final String CREATE_TABLE_TR_INVEST_SAVED_MODULE =
            "create table if not exists tr_invest_saved_menu (module_id text primary key," +
                    "module_name text," +
                    "order_id integer," +
                    "icon_id text)";

    // 登录过的历史账户
    private static final String CREATE_TABLE_TR_LOGIN_ACCOUNT_HISTORY =
            "create table if not exists tr_login_account_history (account_id text primary key," +
                    "create_date date)" ;

    // 登录过的历史账户
    private static final String CREATE_TABLE_TR_APP_STATE =
            "create table if not exists tr_app_state (app_key text primary key," +
                    "state text)" ;



    ////生活菜单
    //private static final String CREATE_TABLE_TR_LIFE =
    //    "create table if not exists life_menu(typeId text,menuId text,catId text,isAvalid text,"
    //        + "catName text,prvcDispName text,cityDispName text,"
    //        + "prvcShortName text,cityDispNo text,orderIndex integer)";

    //生活菜单顺序表
    //private static final String CREATE_TABLE_TR_LIFE_MENU_SORT = "create table if not exists life_menu_sort(user text,cityCode text,menuId text,sortIndex integer)";

    public static final String CREATE_TABLE_TR_LIFE =
        "create table if not exists life_menu3(bocuser text,typeId text,menuname text,menuId text,catId text,isAvailable text,"
            + "cityDispNo text,cityDispName text,prvcDispName text,prvcShortName text,flowFileId text,merchantName text,orderIndex integer)";

    private String prvcDispName;//吉林
    private String cityDispName;//四平
    private String prvcShortName;//JL
    private String cityDispNo;

    private String flowFileId	  ;//流程文件ID	String
    private String merchantName	;//商户名称	String

    private Context mContext;
    private SQLiteDatabase database;

    public BocMobileDB(Context ctx) {
        super(ctx, DATABASE_NAME, null, 1);
        this.database = this.getWritableDatabase();
        this.mContext = ctx;
        //ctx.deleteDatabase(DATABASE_NAME);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // 当前数据库版本
        currentVersion = db.getVersion();
        isNewInstall = (currentVersion == 0);
        initDatabase(db);
    }


    /**
     * 初始化数据库
     */
    private void initDatabase(SQLiteDatabase db){

        switch (currentVersion) {
            case 0:
                // TODO
                // 开始建表
                //db.execSQL(CREATE_TABLE_TM_MODULE);
                db.execSQL(CREATE_TABLE_TR_HOME_SAVED_MODULE);
                db.execSQL(CREATE_TABLE_TR_LOGIN_ACCOUNT_HISTORY);
                db.execSQL(CREATE_TABLE_TR_INVEST_SAVED_MODULE);
                db.execSQL(CREATE_TABLE_TR_LIFE);
                db.execSQL(CREATE_TABLE_TR_HOME_SAVED_INVEST);
                db.execSQL(CREATE_TABLE_TR_APP_STATE);
                db.execSQL(BocMobileDB.CREATE_TABLE_TR_LIFE);
                YunDB.createTable(db);

                //insertAllModuleInfo();
                //inserDefaultHomeModule();
                currentVersion++;
        }

        db.setVersion(DATABASE_VERSION);
    }
    public SQLiteDatabase getDatabase() {
        return database;
    }

    public static void deleteDatabase(Context ctx) {
        ctx.deleteDatabase(DATABASE_NAME);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    

    /**
     * 插入所有模块信息
     */
//    private void insertAllModuleInfo(){
//
//        // 账户管理
//        ContentValues values = new ContentValues();
//        values.put("module_id", "account_0");
//        values.put("module_name", "账户管理");
//        values.put("parent_id", "0");
//        values.put("order_id", 0);
//        values.put("icon_id", "boc_main_home_nav_trans");
//        db.insert("tm_module", null,values);
//
//        values = new ContentValues();
//        values.put("module_id", "trans_0");
//        values.put("module_name", "转账汇款");
//        values.put("parent_id", "0");
//        values.put("order_id", 1);
//        values.put("icon_id", "boc_main_home_nav_trans");
//        db.insert("tm_module", null,values);
//
//        values = new ContentValues();
//        values.put("module_id", "loan_0");
//        values.put("module_name", "贷款");
//        values.put("parent_id", "0");
//        values.put("order_id", 2);
//        values.put("icon_id", "boc_main_home_nav_trans");
//        db.insert("tm_module", null,values);
//    }

    /**
     * 插入主页默认的功能导航菜单
     */
//    private void inserDefaultHomeModule(){
//        ContentValues values = new ContentValues();
//        values.put("module_id", "account_0000");
//        values.put("order_id", 0);
//        db.insert("tr_home_module", null,values);
//
//        values = new ContentValues();
//        values.put("module_id", "trans_0000");
//        values.put("order_id", 1);
//        db.insert("tr_home_module", null,values);
//
//        values = new ContentValues();
//        values.put("module_id", "loan_0000");
//        values.put("order_id", 2);
//        db.insert("tr_home_module", null,values);
//    }
}
