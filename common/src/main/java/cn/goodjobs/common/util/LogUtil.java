package cn.goodjobs.common.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.util.Log;

public class LogUtil
{
    private final static String TAG = "goodjobs";
    private final static boolean isDebug = true;

    public static void info(String msg)
    {
        if (!StringUtil.isEmpty(msg) && isDebug) {
            Log.i(TAG, msg);
        }
    }

    public static void logUrl(String url, Map<String, ?> rawParams)
    {
        if (rawParams == null) {
            info(url);
            return;
        }
        Set<String> keys = rawParams.keySet();
        StringBuffer returnUrl = new StringBuffer(url);
        String temp = "?";
        for (Iterator<String> it = keys.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            returnUrl.append(temp);
            returnUrl.append(key + "=" + rawParams.get(key).toString());
            temp = "&";
        }
        info(returnUrl.toString());
    }
}
