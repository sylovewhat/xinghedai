package com.tc.emms.utils;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.tc.emms.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

/**
 * Created by cjh on 2018/1/5.
 * 状态栏的实现封装类
 * - 完成以下两种功能：
 * 1，设置一个纯色，适配4.4并以上，需要处理5.0并以上的阴影效果
 * 2，当顶部是一个图片，上移到状态栏
 */

public class StatusBarUtils {
    private static int mResult = 0;
    public static void setStatusBarMode(Activity activity, boolean bDark) {
        if (mResult == 0){
            mResult =StatusBarLightMode(activity,bDark);
        }else {
            if (bDark){
                StatusBarLightMode(activity,mResult);
            }else {
                StatusBarDarkMode(activity,mResult);
            }
        }
    }


    /**
     * 将acitivity中的activity中的状态栏设置为一个纯色 (设置状态栏颜色)
     * @param activity 需要设置的activity
     * @param color 设置的颜色（一般是titlebar的颜色）
     */
    public static void setColor(Activity activity, int color){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //5.0及以上，不设置透明状态栏，设置会有半透明阴影
//            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //设置statusBar的背景色
//            activity.getWindow().setStatusBarColor(color);
//        } else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusBarView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            //让我们的activity_main。xml中的布局适应屏幕
            setRootView(activity);
        }
    }






    /**
     * 解决部分机型透明状态栏会有一层半透明阴影
     * 兼容当顶部是图片时，是图片显示到状态栏上
     * SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN和SYSTEM_UI_FLAG_LAYOUT_STABLE，注意两个Flag必须要结合在一起使用，表示会让应用的主体内容占用系统状态栏的空间
     * @param activity
     */
    public static void setCompat(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0及以上，不设置透明状态栏，设置会有半透明阴影
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //是activity_main。xml中的图片可以沉浸到状态栏上(设置这个属性底部如果有虚拟按键也会沉浸下去)
//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //设置状态栏颜色透明。
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
//           activity.getWindow().setNavigationBarColor(Color.TRANSPARENT);//设置导航栏(既虚拟按键)透明
        } else {
            //。。。。
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     *状态栏亮色模式，设置状态栏黑色文字、图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity, boolean isT){
        int result=0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(MIUISetStatusBarLightMode(activity, isT)){  //小米系统
                result=1;
            }else if(FlymeSetStatusBarLightMode(activity, isT)){ //魅族系统
                result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //原生系统
//                if (isT){
//                    activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                }else {
//                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                }
                setStatusBarFalgs(activity,isT);
                result=3;
            }else { //5.0

            }
        }
        return result;
    }

    /**
     * 已知系统类型时，设置状态栏黑色文字、图标。
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @param type 1:MIUUI 2:Flyme 3:android6.0
     */
    public static void StatusBarLightMode(Activity activity, int type){
        if(type==1){
            MIUISetStatusBarLightMode(activity, true);
        }else if(type==2){
//            setStatusBarDarkIcon(activity, true);
            FlymeSetStatusBarLightMode(activity, true);
        }else if(type==3){
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            setStatusBarFalgs(activity,true);
        }

    }

    /**
     * 状态栏暗色模式，清除MIUI、flyme或6.0以上版本状态栏黑色文字、图标
     */
    public static void StatusBarDarkMode(Activity activity, int type){
        if(type==1){
            MIUISetStatusBarLightMode(activity, false);
        }else if(type==2){
            FlymeSetStatusBarLightMode(activity,false);
        }else if(type==3){
//            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            setStatusBarFalgs(activity,false);
        }

    }

    /**
     * 设置根布局参数，让跟布局参数适应透明状态栏
     *
     */
    private static void setRootView(Activity activity) {
        //获取到activity_main.xml文件
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        //如果不是设置参数，会使内容显示到状态栏上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            rootView.setFitsSystemWindows(true);
        }
    }

    /**
     * 获取状态栏的高度
     * @param acitivity
     * @return
     */
    private static int getStatusBarHeight(Activity acitivity){

        int resourceId = acitivity.getResources().getIdentifier("status_bar_height", "dimen", "android");

        return acitivity.getResources().getDimensionPixelOffset(resourceId);
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param activity 当前activity
     * @param dark 是否把状态栏文字及图标颜色设置为深色  true为深色 false 为白色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean dark) {
        return new StatusbarColorUtils().setStatusBarDarkIcon(activity, dark, true);
    }

    /**
     * 需要MIUIV6以上
     * @param activity
     * @param dark 是否把状态栏文字及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean dark) {
        boolean result = false;
        Window window=activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
//                    if(dark){
//                        activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//                    }else {
//                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//                    }
                    setStatusBarFalgs(activity,dark);
                }
            }catch (Exception e){

            }
        }
        return result;
    }


    /**
     * Flag只有在使用了FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
     * 并且没有使用 FLAG_TRANSLUCENT_STATUS的时候才有效，也就是只有在状态栏全透明的时候才有效。
     * @param activity
     * @param bDark
     */
    public static void setStatusBarFalgs(Activity activity, boolean bDark) {
        //6.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decorView = activity.getWindow().getDecorView();
            if (decorView != null) {
                int vis = decorView.getSystemUiVisibility();
                if (bDark) {
                    vis |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else {
                    vis &= ~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }

        }
    }

    /**
     * 生成一个和状态栏大小相同的矩形条
     *
     * @param activity 需要设置的activity
     * @param color    状态栏颜色值
     * @return 状态栏矩形条
     */
    private static View createStatusBarView(Activity activity, int color) {
        // 绘制一个和状态栏一样高的矩形
        View statusBarView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        statusBarView.setId(R.id.title_bar_id);
        return statusBarView;
    }

    static class StatusbarColorUtils{
        private Method mSetStatusBarDarkIcon;
        private Field mStatusBarColorFiled;
        private boolean setStatusBarDarkIcon(Activity activity, boolean dark, boolean flag) {
            try {
                mSetStatusBarDarkIcon = Activity.class.getMethod("setStatusBarDarkIcon", boolean.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (mSetStatusBarDarkIcon != null) {
                try {
                    mSetStatusBarDarkIcon.invoke(activity, dark);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                if (flag) {
                    return setStatusBarDarkIcon(activity.getWindow(), dark);
                }
            }
            return false;
        }

        /**
         * 设置状态栏字体图标颜色(只限全屏非activity情况)
         *
         * @param window 当前窗口
         * @param dark   是否深色 true为深色 false 为白色
         */
        public boolean setStatusBarDarkIcon(Window window, boolean dark) {
            if (Build.VERSION.SDK_INT < 23) {
                return changeMeizuFlag(window.getAttributes(), "MEIZU_FLAG_DARK_STATUS_BAR_ICON", dark);
            } else {
                View decorView = window.getDecorView();
                if (decorView != null) {
                    return (setStatusBarDarkIcon(decorView, dark) && setStatusBarColor(window, 0));
                }
            }
            return false;
        }

        private boolean changeMeizuFlag(WindowManager.LayoutParams winParams, String flagName, boolean on) {
            try {
                Field f = winParams.getClass().getDeclaredField(flagName);
                f.setAccessible(true);
                int bits = f.getInt(winParams);
                Field f2 = winParams.getClass().getDeclaredField("meizuFlags");
                f2.setAccessible(true);
                int meizuFlags = f2.getInt(winParams);
                int oldFlags = meizuFlags;
                if (on) {
                    meizuFlags |= bits;
                } else {
                    meizuFlags &= ~bits;
                }
                if (oldFlags != meizuFlags) {
                    f2.setInt(winParams, meizuFlags);
                    return true;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return false;
        }

        /**
         * 设置状态栏颜色
         *
         * @param view
         * @param dark
         */
        private boolean setStatusBarDarkIcon(View view, boolean dark) {
            int oldVis = view.getSystemUiVisibility();
            int newVis = oldVis;
            if (dark) {
                newVis |= SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                newVis &= ~SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            if (newVis != oldVis) {
                view.setSystemUiVisibility(newVis);
                return true;
            }
            return false;
        }
        /**
         * 设置状态栏颜色
         *
         * @param window
         * @param color
         */
        private boolean setStatusBarColor(Window window, int color) {
            try {
                mStatusBarColorFiled = WindowManager.LayoutParams.class.getField("statusBarColor");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            WindowManager.LayoutParams winParams = window.getAttributes();
            if (mStatusBarColorFiled != null) {
                try {
                    int oldColor = mStatusBarColorFiled.getInt(winParams);
                    if (oldColor != color) {
                        mStatusBarColorFiled.set(winParams, color);
                        window.setAttributes(winParams);
                        return true;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }


}
