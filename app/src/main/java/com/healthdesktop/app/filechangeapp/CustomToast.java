package com.healthdesktop.app.filechangeapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.os.Build;

import static com.healthdesktop.app.filechangeapp.MyApplication.orderByDate;

/**
 * 项目名称：FileChangeApp
 * 类描述：
 * 创建人：FileChangeApp
 * 创建时间：2019/10/14 0014 12:03
 */

public class CustomToast implements View.OnTouchListener {

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
        params.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //设置不能获取焦点
                        |
                        //WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE   // 不能触摸
                        //|
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;  // 保持屏幕长亮
        params.format = PixelFormat.TRANSLUCENT;  //设置背景半透明
        //params.type = WindowManager.LayoutParams.TYPE_TOAST//设置控件的类型是toast类型,toast是天生没有触摸事件，TYPE_PRIORITY_PHONE:优先于电话的类型，可以在电话界面进行操作的类型

        //设置Toast的触摸监听事件
        view.setOnTouchListener(this);


        //3.将view对象添加windowManager中显示
        windowManager.addView(view, params);
    }




    /**
     * 控件的触摸操作
     *
     * @param v
     *            ： 触摸的控件的view对象
     * @param event
     *            ： 触摸的事件
     *
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // getAction() : 获取当前的触摸事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                //1.按住Toast,获取开始的x和y的坐标
                startX = (int) event.getRawX();
                startY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                //移动
                //2.获取移动到的x和y的坐标
                int newX = (int) event.getRawX();
                int newY = (int) event.getRawY();
                //3.计算移动的距离
                int dX=newX-startX;
                int dY=newY-startY;
                //4.将控件移动计算的距离
                //params.x : 控件的x的坐标
                params.x+=dX;
                params.y+=dY;
                //移动完距离之后，还要将控件在新的位置刷新出来
                windowManager.updateViewLayout(view, params);//更新控件的操作
                //5.更新开始移动的位置
                startX=newX;
                startY=newY;
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                break;
        }
        //True if the listener has consumed the event, false otherwise.
        //如果想让事件执行，返回true;事件不执行，返回false
        return true;
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
