package com.miao.android.mylibrary;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * Created by Administrator on 2016/12/5.
 */

public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager(){}

    //单一实例
    public static ActivityManager getInstance(){
        if (instance == null){
            instance = new ActivityManager();
        }
        return instance;
    }

    //添加指定activity到堆栈
    public void addActivity(Activity activity){
        if (activityStack == null){
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    //获取当前activity
    public Activity currentActivity(){
        Activity activity = activityStack.lastElement();
        return activity;
    }

    //结束指定的activity
    public void finishActivity(Activity activity){
        if (activity != null){
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    //结束当前activity
    public void finishActivity(){
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    //结束指定类名的Activity
    public void finishActivity(Class<?> cls){
        for (Activity activity : activityStack){
            if (activity.getClass().equals(cls)){
                finishActivity(activity);
                return;
            }
        }
    }

    //结束全部的Activity
    public void finishAllActivity(){
        for (int i = 0; i < activityStack.size(); i++){
            if (activityStack.get(i) != null){
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    //退出应用
    public void appExit(Context context){
        try {
            finishAllActivity();
            android.app.ActivityManager manager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            manager.restartPackage(context.getPackageName());
            System.exit(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
