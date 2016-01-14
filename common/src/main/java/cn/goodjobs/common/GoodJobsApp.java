package cn.goodjobs.common;

import android.app.Activity;
import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.imagemenu.MainMenuEntity;

/**
 * Created by 王刚 on 2015/12/14 0014.
 */
public class GoodJobsApp extends Application
{


    private static GoodJobsApp instance;
    private LoginInfo loginInfo;
    private MyLocation myLocation;
    private boolean isLogin; // 是否登录
    public JSONObject resumeJson; // 我的简历状态信息
    public JSONObject personalInfo; // 用户基本信息
    public JSONObject bluePersonalInfo; // 蓝领用户基本信息
    private List<MainMenuEntity> menuEntitys;


    public MyLocation getMyLocation()
    {
        return myLocation;
    }

    public void setMyLocation(MyLocation myLocation)
    {
        this.myLocation = myLocation;
    }

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
