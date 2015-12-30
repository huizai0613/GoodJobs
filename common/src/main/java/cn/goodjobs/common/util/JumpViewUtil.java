package cn.goodjobs.common.util;

import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by yexiangyu on 15/12/23.
 */
public class JumpViewUtil
{

    public static void openActivityAndParam(Context context, Class clazz, Map<String, Object> param)
    {

        Intent intent = new Intent();

        Set<Map.Entry<String, Object>> entries = param.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getValue() instanceof Integer) {
                intent.putExtra(entry.getKey(), (int) entry.getValue());
            } else if (entry.getValue() instanceof Float) {
                intent.putExtra(entry.getKey(), (float) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                intent.putExtra(entry.getKey(), (double) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                intent.putExtra(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Serializable) {
                intent.putExtra(entry.getKey(), (Serializable) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                intent.putExtra(entry.getKey(), (long) entry.getValue());
            }
        }
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }

    public static void openActivityAndParam(Context context, Class clazz, String jsonString)
    {

        Intent intent = new Intent();
        intent.putExtra("tag", jsonString);
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }

    public static void openActivityAndParam(Context context, Class clazz)
    {

        Intent intent = new Intent();
        intent.setClass(context, clazz);
        context.startActivity(intent);
    }


}
