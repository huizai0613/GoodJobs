package cn.goodjobs.common.util.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.SerializeUtil;
import cn.goodjobs.common.util.StringUtil;

public class SharedPrefUtil
{

    public static String sharePref = "goodjobs"; // 默认文件名

    public static void saveDataToLoacl(Context context, String key, Object value)
    {
        saveDataToLoacl(context, sharePref, key, value);
    }

    public static void saveDataToLoacl(String key, Object value)
    {
        saveDataToLoacl(sharePref, key, value);
    }

    public static String getDataFromLoacl(Context context, String key)
    {
        return getDataFromLoacl(context, sharePref, key);
    }

    public static String getDataFromLoacl(String key)
    {
        return getDataFromLoacl(sharePref, key);
    }

    public static void clearLoaclData(Context context)
    {
        clearLoaclData(context, sharePref);
    }

    public static void clearLoaclDataByKey(Context context, String key)
    {
        clearLoaclDataByKey(context, sharePref, key);
    }

    /**
     * 保存一条数据,对value进行了DES加密
     *
     * @param sp    文件名称
     * @param key
     * @param value
     */
    public static void saveDataToLoacl(Context context, String sp, String key, Object value)
    {
        // 加密
        SharedPreferences settings = context.getSharedPreferences(sp, 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putString(key, DesEncrypt.getEncString(value.toString()));
        localEditor.commit();
    }

    public static void saveDataToLoacl(String sp, String key, Object value)
    {
        // 加密
        SharedPreferences settings = ScreenManager.getScreenManager().currentActivity().getSharedPreferences(sp, 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.putString(key, DesEncrypt.getEncString(value.toString()));
        localEditor.commit();
    }

    /**
     * 保存对象
     */
    public static void saveObjectToLoacl(Context context, String sp, String key, Object value)
    {
        // 加密
        SharedPreferences settings = context.getSharedPreferences(sp, 0);
        SharedPreferences.Editor localEditor = settings.edit();
        try {
            localEditor.putString(key, SerializeUtil.serialize(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
        localEditor.commit();
    }

    public static void saveObjectToLoacl(String key, Object value)
    {
        if (ScreenManager.getScreenManager().activityLength() > 0) {
            saveObjectToLoacl(ScreenManager.getScreenManager().currentActivity(), sharePref, key, value);
        }
    }

    public static Object getObject(Context context, String sp, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(sp, 0);
        String str = settings.getString(key, "");
        try {
            return SerializeUtil.deSerialization(str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getObject(String key)
    {
        return getObject(ScreenManager.getScreenManager().currentActivity(), sharePref, key);
    }

    /**
     * 获取一条数据,并对value解密
     *
     * @param sp  文件名称
     * @param key
     */
    public static String getDataFromLoacl(Context context, String sp, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(sp, 0);
        return DesEncrypt.getDesString(settings.getString(key, ""));
    }

    public static String getDataFromLoacl(String sp, String key)
    {
        SharedPreferences settings = ScreenManager.getScreenManager().currentActivity().getSharedPreferences(sp, 0);
        return DesEncrypt.getDesString(settings.getString(key, ""));
    }

    /**
     * 清除所有数据
     */
    public static void clearLoaclData(Context context, String sp)
    {
        SharedPreferences settings = context.getSharedPreferences(sp, 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.clear().commit();
    }

    /**
     * 清除某一条数据
     */
    public static void clearLoaclDataByKey(Context context, String sp, String key)
    {
        SharedPreferences settings = context.getSharedPreferences(sp, 0);
        SharedPreferences.Editor localEditor = settings.edit();
        localEditor.remove(key).commit();
    }

    public static Integer getInt(Context context, String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(context, args[0]);
        } else {
            temp = getDataFromLoacl(context, args[0], args[1]);
        }
        return StringUtil.isEmpty(temp) ? null : Integer.parseInt(temp);
    }

    public static Integer getInt(String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(args[0]);
        } else {
            temp = getDataFromLoacl(args[0], args[1]);
        }
        return StringUtil.isEmpty(temp) ? null : Integer.parseInt(temp);
    }

    public static Long getLong(Context context, String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(context, args[0]);
        } else {
            temp = getDataFromLoacl(context, args[0], args[1]);
        }
        return StringUtil.isEmpty(temp) ? null : Long.parseLong(temp);
    }

    public static Long getLong(String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(args[0]);
        } else {
            temp = getDataFromLoacl(args[0], args[1]);
        }
        return StringUtil.isEmpty(temp) ? null : Long.parseLong(temp);
    }

    public static Boolean getBoolean(Context context, String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(context, args[0]);
        } else {
            temp = getDataFromLoacl(context, args[0], args[1]);
        }
        return StringUtil.isEmpty(temp) ? null : Boolean.parseBoolean(temp);
    }

    public static Boolean getBoolean(String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(args[0]);
        } else {
            temp = getDataFromLoacl(args[0], args[1]);
        }
        return StringUtil.isEmpty(temp) ? null : Boolean.parseBoolean(temp);
    }

    public static JSONObject getJSONObject(Context context, String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(context, args[0]);
        } else {
            temp = getDataFromLoacl(context, args[0], args[1]);
        }
        try {
            return StringUtil.isEmpty(temp) ? null : new JSONObject(temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getJSONObject(String... args)
    {
        String temp;
        if (args.length == 1) {
            temp = getDataFromLoacl(args[0]);
        } else {
            temp = getDataFromLoacl(args[0], args[1]);
        }
        try {
            return StringUtil.isEmpty(temp) ? null : new JSONObject(temp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean check(String sp, String key)
    {
        SharedPreferences settings = ScreenManager.getScreenManager().currentActivity().getSharedPreferences(sp, 0);
        return settings.contains(key);
    }

}
