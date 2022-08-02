package com.healthdesktop.app.filechangeapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.widget.Toast;

import com.healthdesktop.app.filechangeapp.bean.VideoBean;

import org.litepal.LitePal;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 项目名称：FileChangeApp
 * 类描述：
 * 创建人：FileChangeApp
 * 创建时间：2019/10/14 0014 10:12
 */

public class MyApplication extends Application {


    public static SharedPreferences preferences;

    public static Context _context;


    @Override
    public void onCreate() {
        super.onCreate();

        preferences = getSharedPreferences("user", Context.MODE_PRIVATE);

        _context = getApplicationContext();

        LitePal.initialize(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            android.os.StrictMode.VmPolicy.Builder builder = new android.os.StrictMode.VmPolicy.Builder();
            android.os.StrictMode.setVmPolicy(builder.build());
        }

    }

    public static Boolean getHasOk() {
        return preferences.getBoolean("HasOk", false);
    }

    public static void setHasOk(Boolean b) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("HasOk", b);
        editor.apply();
    }


    /* 列表获取 */
    public static String getListData() {
        return preferences.getString("ListData", "/storage/emulated/0/SmallVideo");
    }

    /* 列表设置 */
    public static void setListData(String d) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ListData", d);
        editor.apply();
    }

     /* 列表统计获取 */
    public static Integer getAllmp4() {
        return preferences.getInt("allmp4", 0);
    }

    /* 列表统计设置 */
    public static void setAllmp4(Integer d) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("allmp4", d);
        editor.apply();
    }




    //按日期排序
    public static void orderByDate() {
        File file = new File(getListData());
        File[] fs = file.listFiles();
        int num = 0;
        if (fs != null && fs.length > 0) {
            Arrays.sort(fs, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;
                }

                public boolean equals(Object obj) {
                    return true;
                }

            });
            if (fs[fs.length - 1].getName().endsWith("mp4")) {
                if (fs[fs.length - 1].isFile()) {
                    fs[fs.length - 1].delete();
                    num++;
                }

            } else {
                if (fs.length == 1 && fs[fs.length - 1].isFile()) {
                    fs[fs.length - 1].delete();
                    num++;
                } else {
                    if (fs[fs.length - 2].getName().startsWith(fs[fs.length - 1].getName()) || fs[fs.length - 1].getName().startsWith(fs[fs.length - 2].getName())) {
                        if (fs[fs.length - 1].isFile()) {
                            fs[fs.length - 1].delete();
                            num++;
                        }
                        if (fs[fs.length - 2].isFile()) {
                            fs[fs.length - 2].delete();
                            num++;
                        }
                    } else {
                        fs[fs.length - 1].delete();
                        num++;
                    }
                }
            }
            for (File fff :
                    fs) {
                if (fff.getName().endsWith("download")) {
                    if (fff.isFile()) {
                        fff.delete();
                        num++;
                    }
                }
            }
        }
        Toast.makeText(_context, "OK-" + num, Toast.LENGTH_SHORT).show();

    }


    public static void countMp4() {
        File file = new File(getListData());
        File[] fs = file.listFiles();
        int num = 0;
        if (fs != null && fs.length > 0) {
            Arrays.sort(fs, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return 1;
                    else if (diff == 0)
                        return 0;
                    else
                        return -1;
                }

                public boolean equals(Object obj) {
                    return true;
                }

            });
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].getName().toLowerCase().endsWith(".mp4")) {
                    if (i>=(fs.length-2)){
                        if (LitePal.where("videoid=?",fs[i].getName().substring(0,fs[i].getName().indexOf("."))).find(VideoBean.class).size()>0){
                            fs[i].delete();
                            Toast.makeText(_context,"已存在，已删除！", Toast.LENGTH_SHORT).show();
                        }else{
                            num++;
                        }
                    }else{
                        num++;
                    }
                }
            }
        }
        int oldCount = getAllmp4();
        if (oldCount==num){
            Toast.makeText(_context, "NO" + num, Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(_context, "NEW-" + (num-oldCount), Toast.LENGTH_SHORT).show();
            setAllmp4(num);
        }
    }
}
