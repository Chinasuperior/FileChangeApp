package com.healthdesktop.app.filechangeapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Build;

import static com.healthdesktop.app.filechangeapp.MyApplication.countMp4;
import static com.healthdesktop.app.filechangeapp.MyApplication.orderByDate;

/**
 * 项目名称：FileChangeApp
 * 类描述：
 * 创建人：FileChangeApp
 * 创建时间：2019/10/14 0014 12:03
 */

public class CustomToast{

    private Context mContext;
    private WindowManager windowManager;
    private View view;
    private int startX;
    private int startY;
    private WindowManager.LayoutParams params;

    public CustomToast(Context context) {
        this.mContext = context;
    }

    /**
     * 显示自定义的toast
     *
     */
    public void showToast(){

        //将一个veiw对象添加到窗口中显示就可以了
        //1.得到窗口的管理者
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        //2.设置toast的布局样式
        view = View.inflate(mContext, R.layout.alert_window_menu, null);
        //初始化控件
        Button mAddress = (Button) view.findViewById(R.id.alert_window_imagebtn);
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderByDate();
            }
        });
        Button mCount = (Button) view.findViewById(R.id.alert_window_count);
        mCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countMp4();
            }
        });


        //LayoutParams : 使用代码设置控件的属性，效果跟布局文件中控件属性是一样的效果
        //LayoutParams使用规则：view对象添加到那个父控件，就使用那个父控件的LayoutParams
        //比如：将控件添加RelativeLayout可以使用layout_centerVertical属性，但是放到LinearLayout中就没有layout_centerVertical，所以如果控件想要使用layout_centerVertical就必须作为RelativeLayout子子控件才可以使用
        params = new WindowManager.LayoutParams();
        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            params.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; //设置高度，包裹内容
        params.width = WindowManager.LayoutParams.WRAP_CONTENT; // 设置宽度，包裹内容
        params.x = 400;
        params.y = 200;
        params.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //设置不能获取焦点
                        |
                        //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE   // 不能触摸
                        //|
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;  // 保持屏幕长亮
        params.format = PixelFormat.TRANSLUCENT;  //设置背景半透明
        //params.type = WindowManager.LayoutParams.TYPE_TOAST//设置控件的类型是toast类型,toast是天生没有触摸事件，TYPE_PRIORITY_PHONE:优先于电话的类型，可以在电话界面进行操作的类型

        //3.将view对象添加windowManager中显示
        windowManager.addView(view, params);
    }


    /**
     * 隐藏自定义toast
     *
     * 2017-10-24 上午9:23:57
     */
    public void hideToast() {
        if (view != null) {
            // getParent()：获取控件的父控件
            if (view.getParent() != null) {
                windowManager.removeView(view);
            }
            view = null;
            windowManager = null;
        }
    }

}
