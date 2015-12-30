package cn.goodjobs.common.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by wanggang on 2015/10/7 0007.
 * 自定义栈对activity进行管理
 */
public class ScreenManager {
    private Stack<Activity> activityStack;
    private static ScreenManager instance;
    private  ScreenManager(){
    }
    public static ScreenManager getScreenManager(){
        if(instance==null){
            instance=new ScreenManager();
        }
        return instance;
    }
    public void popActivity(){
        Activity activity=activityStack.lastElement();
        if(activity!=null){
            activity.finish();
            activity=null;
        }
    }
    public void popActivity(Activity activity){
        if(activity!=null){
            activityStack.remove(activity);
            activity.finish();
            activity=null;
        }
    }
    public Activity currentActivity(){
        LogUtil.info("activityStack:"+activityStack);
        Activity activity=activityStack.lastElement();
        return activity;
    }
    public void pushActivity(Activity activity){
        LogUtil.info("pushActivity:"+activityStack);
        if(activityStack==null){
            activityStack=new Stack<Activity>();
        }
        activityStack.add(activity);
        LogUtil.info("pushActivity:" + activityStack);
    }
    public void popAllActivityExceptOne(Activity a){
        for (Activity activity:activityStack) {
            if(activity!=null && !activity.getClass().equals(a.getClass())){
                activity.finish();
                activity=null;
            }
        }
        activityStack.clear();
        activityStack.add(a);
    }

    public void popActivityByClass(Class cls, boolean setResule){
        for (Activity activity:activityStack) {
            if(activity!=null && activity.getClass().equals(cls)){
                if (setResule) {
                    activity.setResult(activity.RESULT_OK);
                }
                activity.finish();
                activity=null;
            }
        }
    }
}
