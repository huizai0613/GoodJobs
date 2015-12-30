package cn.goodjobs.common.util;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class PhoneUtils
{


    /**
     * 发送短信
     *
     * @param smsBody
     */
    public static void sendSMS(String mobile, String content, Context context)
    {
// Uri smsToUri = Uri.parse("smsto:10000");
// Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
// context.startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("address", mobile);
        intent.putExtra("sms_body", content);
        intent.setType("vnd.android-dir/mms-sms");
        context.startActivity(intent);

    }

    /**
     * 群发
     *
     * @param mobile
     * @param context
     */
    public static void sendSMSAll(String mobile, String content, Context context)
    {
// Uri smsToUri = Uri.parse("smsto:10000");
// Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
// context.startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("vnd.android-dir/mms-sms");
        intent.setData(Uri.parse("smsto:" + mobile));
        intent.putExtra("sms_body", content);
        intent.putExtra("address", mobile);
        context.startActivity(intent);


    }

    /**
     * 拨打电话
     */
    public static void makeCall(String phoneNum, Context context)
    {

// 调用系统的拨号服务实现电话拨打功能

        phoneNum = phoneNum.trim();// 删除字符串首部和尾部的空格

        if (phoneNum != null && !phoneNum.equals("")) {
// 调用系统的拨号服务实现电话拨打功能
// 封装一个拨打电话的intent，并且将电话号码包装成一个Uri对象传入
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                    + phoneNum));
            context.startActivity(intent);// 内部类
        }
    }
}
