package com.chinamworld.bocmbci.utils;

import com.chinamworld.bocmbci.utiltools.ShowDialogToolsManager;

import cfca.mobile.sip.SipBox;
import cfca.mobile.sip.SipResult;

/**
 * Created by wang-pc on 2016/10/12.
 */
public class CommPublicTools extends PublicTools {
    //一个字符串有多少个字符
    public static double getLength(String s) {
        double valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        //获取字符串，如果含有中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            String temp = s.substring(i, i + 1);
            //判断是否为中文
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }

        }
        return valueLength;
    }


    /**
     * 校验CFCA正则
     * @param sipBox
     * @param regexInfo
     * @param message
     * @return
     */
    public static boolean checkCFCAInfo(SipBox sipBox,String regexInfo,String message){
        sipBox.setPasswordRegularExpression(regexInfo);
        try{
            SipResult result=sipBox.getValue();
        }
        catch (Exception e){
            ShowDialogToolsManager.Instance.showInfoMessageDialog(message);
            return false;
        }
        return true;
    }

}
