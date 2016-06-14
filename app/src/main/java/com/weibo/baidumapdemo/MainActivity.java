package com.weibo.baidumapdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= 23) {
            insertDummyContactWrapper();
        }else {
            searchData();
        }
    }

    private void searchData() {
        List<PersonVo> personVos = ContactEngine.getAllContactInfo(getApplicationContext());
    }

    private void insertDummyContactWrapper() {
        //提示用户需要手动开启的权限集合
        List<String> permissionsNeeded = new ArrayList<>();

        //功能所需权限的集合
        final List<String> permissionsList = new ArrayList<>();
        //若用户拒绝了该权限申请，则将该申请的提示添加到“用户需要手动开启的权限集合”中
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");

        //若在AndroidManiFest中配置了所有所需权限，则让用户逐一赋予应用权限，若权限都被赋予，则执行方法并返回
        if (permissionsList.size() > 0) {
            //若用户赋予了一部分权限，则需要提示用户开启其余权限并返回，该功能将无法执行
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                int size = permissionsNeeded.size();
                for (int i = 1; i < size; i++)
                    message = message + ", " + permissionsNeeded.get(i);
                //弹出对话框，提示用户需要手动开启的权限
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= 23) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        });
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
        searchData();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //判断用户是否授予了所需权限
    private boolean addPermission(List<String> permissionsList, String permission) {
        //若配置了该权限，返回true
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                //若未配置该权限，将其添加到所需权限的集合，返回true
                permissionsList.add(permission);
                // 若用户勾选了“永不询问”复选框，并拒绝了权限，则返回false
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                //初始化Map集合，其中Key存放所需权限，Value存放该权限是否被赋予
                Map<String, Integer> perms = new HashMap<>();
                // 向Map集合中加入元素，初始时所有权限均设置为被赋予（PackageManager.PERMISSION_GRANTED）
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);
                // 将第二个参数回传的所需权限及第三个参数回传的权限结果放入Map集合中，由于Map集合要求Key值不能重复，所以实际的权限结果将覆盖初始值
                int len = permissions.length;
                for (int i = 0; i < len; i++)
                    perms.put(permissions[i], grantResults[i]);
                // 若所有权限均被赋予，则执行方法
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    searchData();
                }
                //否则弹出toast，告知用户需手动赋予权限
                else {
                    Toast.makeText(MainActivity.this, "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
