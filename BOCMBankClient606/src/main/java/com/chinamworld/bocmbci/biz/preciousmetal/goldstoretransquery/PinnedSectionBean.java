package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 把初始Arraylist里面的数据,转化成带有分组并且标识悬浮类别(SECTION)和内容(ITEM)的list
 *
 */
public class PinnedSectionBean {
    //类型--内容
    public static final int ITEM = 0;
    //类型--顶部悬浮的标题
    public static final int SECTION = 1;

    public final int type; //所属于的类型
    public final Messages messages; //listview显示的item的数据实体类,可根据自己的项目来设置

    public int sectionPosition; //顶部悬浮的标题的位置
    public int listPosition; //内容的位置

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public Messages getMessages() {
        return messages;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public int getType() {
        return type;
    }

    public PinnedSectionBean(int type, Messages messages) {
        super();
        this.type = type;
        this.messages = messages;
    }

    public PinnedSectionBean(int type, Messages messages,
                             int sectionPosition, int listPosition) {
        super();
        this.type = type;
        this.messages = messages;
        this.sectionPosition = sectionPosition;
        this.listPosition = listPosition;
    }

    @Override
    public String toString() {
        return messages.getTime();
    }




    private static List<Messages> getListMessageByTitle(Map<Messages, List<Messages>> map,String title){
        for(Messages m : map.keySet()){
            if(title.equals(m.getTitle())){
                return map.get(m);
            }
        }
        List<Messages> list = new ArrayList<Messages>();
        Messages detail = new Messages();
        detail.setTitle(title);
        map.put(detail,list);
        return list;
    }


    /**
     * 通过HashMap键值对的特性，将ArrayList的数据进行分组，返回带有分组Header的ArrayList。
     * @param details 从后台接受到的ArrayList的数据，其中日期格式为：yyyy-MM-dd HH:mm:ss
     * @return list  返回的list是分类后的包含header（yyyy-MM-dd）和item（HH:mm:ss）的ArrayList
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static ArrayList<PinnedSectionBean> getData(
            List<Messages> details) {
        //最后我们要返回带有分组的list,初始化
        ArrayList<PinnedSectionBean> list = new ArrayList<PinnedSectionBean>();
        //时间转换的util类
        TimeManagement management = new TimeManagement();
        Map<Messages, List<Messages>> map = new LinkedHashMap<Messages, List<Messages>>();
        // 按照warndetail里面的时间进行分类
        Messages detail = new Messages();
        for (int i = 0; i < details.size(); i++) {
            try {
//                String key = management.exchangeStringDate(details.get(i)
//                        .getTitle());
                String key = details.get(i).getTitle();
//                if (detail.getTitle() != null && !"".equals(detail.getTitle())) {
//                    //判断这个Key对象有没有生成,保证是唯一对象.如果第一次没有生成,那么new一个对象,之后同组的其他item都指向这个key
//                    boolean b = !key.equals(detail.getTitle().toString());
//                    if (b) {
//                        detail = new Messages();
//                    }
//                }
//                detail.setTitle(key);
                List<Messages> warnDetails = getListMessageByTitle(map,key);
                //把属于当天yyyy-MM-dd的时间HH:mm:ss全部指向这个key
//                List<Messages> warnDetails = map.get(detail);
                //判断这个key对应的值有没有初始化,若第一次进来,这new一个arryalist对象,之后属于这一天的item都加到这个集合里面
//                if (warnDetails == null) {
//                    warnDetails = new ArrayList<Messages>();
//                }
//                String time = details.get(i).getTitle();
////                time = management.exchangeStringTime(time);
//                //把HH:mm:ss时间替换之前yyyy-MM-dd HH:mm:ss格式的时间.标识属于标题下的子类
//                details.get(i).setTitle(time);
                warnDetails.add(details.get(i));
//                map.put(detail, warnDetails);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 用迭代器遍历map添加到list里面
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            Messages key = (Messages) entry.getKey();
            //我们的key(yyyy-MM-dd)作为标题.类别属于SECTION
            list.add(new PinnedSectionBean(SECTION, key));
            List<Messages> li = (List<Messages>) entry.getValue();
            for (Messages warnDetail : li) {
                //对应的值(HH:mm:ss)作为标题下的item,类别属于ITEM
                list.add(new PinnedSectionBean(ITEM, warnDetail));
            }
        }
        // 把分好类的hashmap添加到list里面便于显示
        return list;
    }

}
