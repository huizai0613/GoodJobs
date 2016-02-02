package cn.goodjobs.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by wanggang on 2016/1/20 0020.
 */

public class DateUtil {

    public static int compare(String date1, String date2) {
        if (StringUtil.isEmpty(date1) || StringUtil.isEmpty(date2)) {
            return -2;
        }
        if (date2number(date1) == date2number(date2)) {
            return 0;
        } else if (date2number(date1) > date2number(date2)) {
            return 1;
        } else {
            return -1;
        }
    }

    public static long date2number(String date) {
        LogUtil.info(date);
        String[] dates = date.split("-");
        StringBuffer sb = new StringBuffer(dates[0]);
        if (dates[1].length() == 1) {
            sb.append("0");
        }
        sb.append(dates[1]);
        if (dates.length > 2) {
            if (dates[2].length() == 1) {
                sb.append("0");
            }
            sb.append(dates[2]);
        }
        return Long.parseLong(sb.toString());
    }

    public static String getToday(String format) {
        Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
        return new SimpleDateFormat(format).format(calendar.getTime());
    }
}
