package com.weibo.baidumapdemo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/6/10.
 * 获取查询练习人的操作
 */
public class ContactEngine {
    /**
     * 实现获取系统联系人
     *
     * @return
     */
    public static List<PersonVo> getAllContactInfo(Context context) {
        ArrayList<PersonVo> personVos = null;
        //获取内容解析着对象
        ContentResolver cr = context.getContentResolver();
        //获取联系人的uri和游标
        Cursor c = cr.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                },
                null, null, null);
        if (c == null){
            return null;
        }else {
            personVos = new ArrayList<>();
        }
        int displayName = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int number = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        PersonVo personVo = null;
        //遍历游标,获取联系人
        while (c.moveToNext()) {
            personVo = new PersonVo();
            personVo.setName(c.getString(displayName));
            personVo.setTel(c.getString(number));
            personVos.add(personVo);
            personVo = null;
        }
        //数据库打开一定要关闭,否则会出现内存溢出
        c.close();
        return personVos;
    }
}
