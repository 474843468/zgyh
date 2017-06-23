package com.chinamworld.llbt.userwidget.moneyinputview;

/**
 * Created by Administrator on 2016/5/27.
 */
public enum KeyboardEnum {

    one(ActionEnum.add, "1"),
    two(ActionEnum.add, "2"),
    three(ActionEnum.add, "3"),
    four(ActionEnum.add, "4"),
    five(ActionEnum.add, "5"),
    sex(ActionEnum.add, "6"),
    seven(ActionEnum.add, "7"),
    eight(ActionEnum.add, "8"),
    nine(ActionEnum.add, "9"),
    point(ActionEnum.add, "."),
    zero(ActionEnum.add, "0"),
    del(ActionEnum.delete, "del"),
    longdel(ActionEnum.longClick, "longclick");

    public enum ActionEnum {
        add, delete, longClick;
    }

    private ActionEnum type;
    private String value;

    private KeyboardEnum(ActionEnum type, String value) {
        this.type = type;
        this.value = value;
    }

    public ActionEnum getType() {
        return type;
    }

    public void setType(ActionEnum type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value)  {
        this.value = value;
    }
}
