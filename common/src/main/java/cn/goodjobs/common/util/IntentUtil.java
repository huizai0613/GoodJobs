package cn.goodjobs.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import cn.goodjobs.common.activity.LoginActivity;

/**
 * Created by wanggang on 2015/10/12 0012.
 */
public class IntentUtil {

    /**
     * 退出登录或者帐号在其他地方登录
     * */
    public static void toLoginActivity() {
        Intent intent = new Intent(ScreenManager.getScreenManager().currentActivity(), LoginActivity.class);
        ScreenManager.getScreenManager().currentActivity().startActivity(intent);
    }

    public static void toLanlingPersonalActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setClassName(ScreenManager.getScreenManager().currentActivity(), "cn.goodjobs.bluecollar.activity.makefriend.MakeFriendPersonalInfoActivity");
        activity.startActivityForResult(intent, 111);
    }

    /**
     * 调用系统打电话的界面
     * */
    public static void toCallActivity(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 界面跳转
     * */
    public static void toActivity(Class cls) {
        Intent intent = new Intent(ScreenManager.getScreenManager().currentActivity(), cls);
        ScreenManager.getScreenManager().currentActivity().startActivity(intent);
    }
}
