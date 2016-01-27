package cn.goodjobs.common.util;

import android.content.Context;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import cn.goodjobs.common.R;
import cn.goodjobs.common.constants.Constant;

/**
 * Created by 王刚 on 2015/10/30 0030.
 * 友盟分享配置
 */
public class UMShareUtil {

    private static UMSocialService mController = null;

    public static UMSocialService getUMSocialService() {
        if (mController == null) {
            mController = UMServiceFactory.getUMSocialService("com.umeng.share");
            init();
        }
        return mController;
    }

    private static void init() {
        mController.getConfig().removePlatform(SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(ScreenManager.getScreenManager().currentActivity(), "1102002463","NuOwPqTCqoM4I7UW");
        qZoneSsoHandler.addToSocialSDK();

        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ScreenManager.getScreenManager().currentActivity(), "1102002463","NuOwPqTCqoM4I7UW");
        qqSsoHandler.addToSocialSDK();

        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(ScreenManager.getScreenManager().currentActivity(),"wx10c4755ae0342d2c","106205cc88c8f285bfe813b2634e38b6");
        wxHandler.addToSocialSDK();

        // 添加微信朋友圈平台
        UMWXHandler wxCircleHandler = new UMWXHandler(ScreenManager.getScreenManager().currentActivity(),"wx10c4755ae0342d2c","106205cc88c8f285bfe813b2634e38b6");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    // 设置职位分享的文字图片信息
    public static void setShareText(Context context, String jobName, String jobID) {
        setShareText(context, Constant.SHARE_JOBDETAIL, jobName, jobID);
    }

    // 设置职位分享的文字图片信息
    public static void setShareText(Context context, String url, String jobName, String jobID) {
        if (mController == null) {
            mController = UMServiceFactory.getUMSocialService("com.umeng.share");
            init();
        }
        String shareContent = "HI，我在新安人才网发现这条招聘信息，快来看看吧！" + jobName
                + "(找好工作就上新安人才网)" ;
        // 设置分享内容
        mController.setShareContent(shareContent + "," + url + jobID);

        //设置QQ分享文字
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareContent);
        qqShareContent.setTitle(jobName); //设置分享title
        qqShareContent.setShareImage(new UMImage(context, R.drawable.ic_launcher)); //设置分享图片
        qqShareContent.setTargetUrl(url + jobID); //设置点击分享内容的跳转链接
        mController.setShareMedia(qqShareContent);

        //设置QQ空间分享文字
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(shareContent);
        qzone.setTargetUrl(url + jobID); //设置点击消息的跳转URL
        qzone.setTitle(jobName); //设置分享内容的标题
        qzone.setShareImage(new UMImage(context, R.drawable.ic_launcher)); //设置分享图片
        mController.setShareMedia(qzone);

        //设置新浪微博分享文字
//        SinaShareContent sinaShareContent=new SinaShareContent();
//        sinaShareContent.setShareContent(shareContent);
//        sinaShareContent.setTitle(jobName); //设置分享title
//        sinaShareContent.setShareImage(new UMImage(context, R.drawable.ic_launcher)); //设置分享图片
//        sinaShareContent.setTargetUrl(url + jobID); //设置点击分享内容的跳转链接
//        mController.setShareMedia(sinaShareContent);
    }
}
