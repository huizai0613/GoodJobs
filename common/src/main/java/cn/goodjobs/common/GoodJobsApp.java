package cn.goodjobs.common;

import android.app.Activity;
import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

/**
 * Created by 王刚 on 2015/12/14 0014.
 */
public class GoodJobsApp extends Application
{


    private static GoodJobsApp instance;
    private LoginInfo loginInfo;
    private boolean isLogin; // 是否登录

    @Override
    public void onCreate()
    {
        super.onCreate();

        instance = this;

        Fresco.initialize(this);
    }

    public static GoodJobsApp getInstance()
    {
        return instance;
    }

    public LoginInfo getLoginInfo()
    {
        if (loginInfo == null && SharedPrefUtil.getObject("loginInfo") != null) {
            loginInfo = (LoginInfo) SharedPrefUtil.getObject("loginInfo");
        }
        return loginInfo;
    }

    public boolean isLogin()
    {
        return SharedPrefUtil.getBoolean("isLogin") != null ? SharedPrefUtil.getBoolean("isLogin") : false;
    }

    public void setLoginInfo(LoginInfo loginInfo)
    {
        this.loginInfo = loginInfo;
    }


    public boolean checkLogin(Activity activity, String tag)
    {
        if (isLogin()) {
            return true;
        } else {
            JumpViewUtil.openActivityAndParam(activity, LoginActivity.class, tag);
            return false;
        }
    }

    public boolean checkLogin(Activity activity)
    {
        if (isLogin()) {
            return true;
        } else {
            JumpViewUtil.openActivityAndParam(activity, LoginActivity.class);
            return false;
        }
    }

}
