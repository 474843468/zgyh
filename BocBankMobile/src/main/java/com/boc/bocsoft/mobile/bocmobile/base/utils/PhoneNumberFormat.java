package com.boc.bocsoft.mobile.bocmobile.base.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;
import android.widget.EditText;

import com.boc.bocsoft.mobile.bocmobile.base.activity.BussActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 手机号码输入格式化
 * 号码格式为：3 4 4
 * Created by niuguobin on 2016/6/3.
 */
public class PhoneNumberFormat {

    private static String num = "";

    /**
     * 通过通讯录选择联系人
     *
     * @param mContext
     * @param data
     * @return
     */
    @Deprecated
    public static String insertPhonenumber(Activity mContext, Intent data) {
        String phoneNumber = "";
        Uri contactData = data.getData();
        Cursor cursor = mContext.managedQuery(contactData, null, null, null,
                null);
        cursor.moveToFirst();
        phoneNumber = getContactPhoneAll(mContext, cursor);
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return phoneNumber;
    }

    /**
     * 通过通讯录选择联系人
     * 列表中条目的数据为("姓名#手机号")
     * @param mContext
     * @param data
     * @return
     */
    public static List<String> insertPhonenumber1(Activity mContext, Intent data) {
        Uri contactData = data.getData();
//        Cursor cursor = mContext.managedQuery(contactData, null, null, null,
//                null);
//        Cursor cursor =  mContext.getContentResolver().query(contactData, null, null, null, null);
//        cursor.moveToFirst();
        CursorLoader cursorLoader = new CursorLoader(mContext, contactData, null, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        cursor.moveToFirst();
        return getContactPhoneAll1(mContext, cursor);
    }

    /**
     * 此方法不可能正确返回 zhx注
     * 打开手机通讯录选择联系人
     * 如果联系人大于1个则弹出选择框，列出该人名下的所有号码
     *
     * @param mContext
     * @param cursor
     * @return
     */
    @Deprecated
    private static String getContactPhoneAll(Activity mContext, Cursor cursor) {
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = cursor.getInt(phoneColumn);
        final List<String> result = new ArrayList<String>();
        if (phoneNum > 0) {
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);// 获得联系人的ID号
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    String phoneNumber = phone.getString(index);
                    result.add(phoneNumber);
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        num = result.get(0);

        if (result.size() > 1) { // 如果号码多于2个，则弹出对话框选择
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            int size = result.size();
            builder.setTitle("请选择一个号码").setItems(result.toArray(new String[size]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int position = which;
                    num = result.get(position);
                }
            }).create().show();
        }

        return num;
    }

    /**
     * 请使用此方法
     * 打开手机通讯录选择联系人
     * 如果联系人大于1个则弹出选择框，列出该人名下的所有号码
     *
     * @param mContext
     * @param cursor
     * @return
     */
    private static List<String> getContactPhoneAll1(Activity mContext, Cursor cursor) {
//        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
//        int phoneNum = cursor.getInt(phoneColumn);
        final List<String> result = new ArrayList<String>();
        try {
            //        if (phoneNum > 0) {
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);// 获得联系人的ID号
            long contactId = cursor.getLong(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int index1 = phone.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String phoneNumber = phone.getString(index);
                    String displayName = phone.getString(index1);
                    result.add(displayName + "#" + phoneNumber);
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
//                            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ((BussActivity)mContext).showErrorDialog("未能取到手机号，请检查通讯录权限");
        }
        return result;
    }

    // 获取手机号
    public static String getPhoneNum(String phone) {
        return phone.substring(phone.indexOf('#') + 1);
    }

    // 获取手机号对应的姓名
    public static String getPhoneName(String phone) {
        return phone.substring(0, phone.indexOf('#'));
    }

    /**
     * 实时格式化输入的手机号为XXX XXXX XXXX格式
     *
     * @param s
     * @param start
     * @param before
     * @param editText 需要实时格式化的组件
     */
    public static void onEditTextChanged(CharSequence s, int start, int before, EditText editText) {
        if (s == null || s.length() == 0) return;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                stringBuilder.append(s.charAt(i));
                if ((stringBuilder.length() == 4 || stringBuilder.length() == 9) && stringBuilder.charAt(stringBuilder.length() - 1) != ' ') {
                    stringBuilder.insert(stringBuilder.length() - 1, ' ');
                }
            }
        }
        if (!stringBuilder.toString().equals(s.toString())) {
            int index = start + 1;
            if (stringBuilder.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
            editText.setText(stringBuilder.toString());
            editText.setSelection(index);
        }
    }

    /**
     * @param @param  nativeString
     * @param @return
     * @return String
     * @Title: getThreeFourThreeString
     * @Description: 隐藏11位手机号的中间四位
     */
    public static String getThreeFourThreeString(String nativeString) {
        if (nativeString == null) {
            return "-";
        }
        if (nativeString.length() < 7) {
            // 长度小于8直接返回，避免越界
            return nativeString;
        }
        StringBuffer sbShow = new StringBuffer(nativeString.subSequence(0, 3));
        sbShow.append("****");
        sbShow.append(nativeString.substring(nativeString.length() - 4, nativeString.length()));
        return sbShow.toString();
    }
}
