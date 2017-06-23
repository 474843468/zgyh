package com.boc.bocsoft.mobile.bocmobile.buss.common.menu;

import android.util.Xml;

import com.excelsecu.util.LogUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

/**
 * 菜单解析
 * Created by lxw on 2016/7/21 0021.
 */
public class MenuParse {

    private final static String LOG_TAG = MenuParse.class.getSimpleName();
    private final static String TAG_CATEGORY = "category";
    private final static String TAG_GROUP = "group";
    private final static String TAG_ITEM = "item";
    private final static String TAG_LIST = "list";

    /**
     * 解析菜单xml，生成java的model 注：目前的解析方法只能解析固定深度的xml文件，如果菜单层级增加，需要更新此方法
     *
     * @param is
     *            xml文件的输入流
     * @param is
     *            登录后获得的市场细分码即国家地区
     * @return MBIMenuModel类型，菜单结构的根节点对象
     */
    public static Menu parseXML(InputStream is) throws Exception {
        LogUtil.i(LOG_TAG, "parseXML:解析菜单方法开始执行....");
        Menu menu = new Menu();
        Category category = null;
        //Group group = null;
        Item item = null;
        Stack<Item> stack = new Stack<>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (TAG_CATEGORY.equals(tag)) {
                            category =  new Category();
                            category.setId(parser.getAttributeValue(null, Category.ATTR_ID));
                            category.setTitle(parser.getAttributeValue(null, Category.ATTR_TITLE));
                            menu.addCategory(category);
                        }
//                        else if (TAG_GROUP.equals(tag)) {
//                            group = new Group();
//                            group.setGroupId(parser.getAttributeValue(null, Group.ATTR_ID));
//                            group.setTitle(parser.getAttributeValue(null, Group.ATTR_TITLE));
//                            group.setIconId(parser.getAttributeValue(null, Group.ATTR_ICON));
//                            category.addGroup(group);
//                        }
                        else if (TAG_ITEM.equals(tag)) {
                            item = new Item();
                            item.setModuleId(parser.getAttributeValue(null, Item.ATTR_ID));
                            item.setTitle(parser.getAttributeValue(null, Item.ATTR_TITLE));
                            item.setIconId(parser.getAttributeValue(null, Item.ATTR_ICON));
                            item.setType(parser.getAttributeValue(null, Item.ATTR_TYPE));
                            item.setOld(parser.getAttributeValue(null, Item.ATTR_OLD));
                            item.setLogin(parser.getAttributeValue(null, Item.ATTR_LOGIN));

                            // 栈为空时，当前节点为一级菜单
                            if (stack.empty()){
                                category.addItem(item);
                            } else {
                                stack.lastElement().addChild(item);
                            }
                        } else if (TAG_LIST.equals(tag)) {
                            stack.add(item);
                        }
                        break;
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_TAG:
                        if (TAG_LIST.equals(tag))
                            // 弹出栈
                            stack.pop();
                        break;
                    default:
                        break;
                }
                parser.next();
                eventType = parser.getEventType();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return menu;
    }
}
