package com.boc.bocsoft.mobile.bocmobile.base.utils;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 * Created by zhx on 2016/7/19
 */
public class PinYinUtil {
    /**
     * 将汉字转为拼音,内部是查询xml文件，会消耗一定效率，所以此方法不能被频繁调用
     *
     * @param chinese
     * @return
     */
    public static String getPinYin(String chinese) {
        if (TextUtils.isEmpty(chinese)) return "";

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();//设置转换的拼音格式
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//转完的拼音是大写字母
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//转换的拼音不带声调

        //1.由于只能对单个汉字进行转换，所以应该将汉字拆开转换，将汉字转成字符数组
        char[] charArray = chinese.toCharArray();

        //2.遍历所有字符，进行转换，然后将每个拼音拼接到一起
        String pinyin = "";
        //西    安   XI1AN1---空格的情况
        //a西c安   aXIcAN---空格的情况
        for (int i = 0; i < charArray.length; i++) {
            //忽略空格，进行下次遍历
            if (Character.isWhitespace(charArray[i])) {
                continue;
            }

            // 判断是否是汉字,字符的范围(-128~127);汉字是2个字节,所以汉字肯定大于127
            if (charArray[i] > 127) {
                // 有可能汉字
                try {
                    // 由于多音字的存在，  单： dan  shan
                    String[] pinyinArr = PinyinHelper.toHanyuPinyinStringArray(charArray[i], format);
                    if (pinyinArr == null) {
                        // 如果返回的拼音是空，可以选择忽略或者拼接
                        pinyin += "";
                    } else {
                        // 只能取第一个拼音，
                        pinyin += pinyinArr[0];
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                    // 说明不是汉字，比如O(∩_∩)O~，则可以忽略或者拼接
                    pinyin += "";
                }
            } else {
                // 此时可能是字母，或者%&*之类
                if(Character.isLetter(charArray[i])) {
                    pinyin += charArray[i];
                }
            }
        }

        return pinyin;
    }

}
