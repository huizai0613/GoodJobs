package cn.goodjobs.bluecollar.view.listview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;

public class DateUtil {

	public static final String FORMAT_HHMM = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_DEFAULT = "yyyy/MM/dd";

	public static String getYestoday() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, -1); // 得到前一天
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

    public static String getPreWeek() {
        Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -7); // 得到前一天
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static String getPreWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar targetCalendar = Calendar.getInstance();
        if (StringUtil.isEmpty(date)) {
            targetCalendar.setTime(new Date());
        } else {
            try {
                targetCalendar.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        targetCalendar.add(Calendar.DATE, -7); // 得到前一天
        return new SimpleDateFormat("yyyy-MM-dd").format(targetCalendar.getTime());
    }

	public static String getToday() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
	}

    public static String getToday(String format) {
        Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
        return new SimpleDateFormat(format).format(calendar.getTime());
    }

	public static String getWeek(String date, int delay) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar targetCalendar = Calendar.getInstance();
		if (StringUtil.isEmpty(date)) {
			targetCalendar.setTime(new Date());
		} else {
			try {
				targetCalendar.setTime(sdf.parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		targetCalendar.add(Calendar.DATE, delay);
		return sdf.format(targetCalendar.getTime());
	}

	public static String getWeek() {
		return getWeek(null, 0);
	}

	public static String getCurTime() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}

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

	public static int getHourPosition() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		if (hour < 23) {
			return hour + 1;
		} else {
			return 0;
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
		if (dates[2].length() == 1) {
			sb.append("0");
		}
		sb.append(dates[2]);
		return Long.parseLong(sb.toString());
	}

	/**
	 * @Description: 将毫秒转换为默认日期格式时间
	 * @param @param millisecond
	 * @param @return
	 * @return String
	 */
	 public static String mills2DateDefault(long millisecond) {
		 return mills2Date(millisecond, FORMAT_DEFAULT);
	 }

	/**
	 * @Description: 将毫秒转换为指定格式日期时间
	 * @param @param millisecond
	 * @param @param formatStr
	 * @param @return
	 * @return String
	 */
	public static String mills2Date(long millisecond, String formatStr) {
		Date dat = new Date(millisecond);
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(dat);
	}

    // 将传入时间与当前时间进行对比，是否今天昨天
    public static String mills2Date(String dateStr) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = df.parse(dateStr);
            return mills2Date(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

	// 将传入时间与当前时间进行对比，是否今天昨天
	public static String mills2Date(long millisecond) {
		Date date = new Date(millisecond);
		return mills2Date(date);
	}

    // 将传入时间与当前时间进行对比，是否今天昨天
    public static String mills2Date(Date date) {
        String todySDF = "今天";
        String yesterDaySDF = "昨天";
        String otherSDF = "M月d日";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(now);
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        if (dateCalendar.after(targetCalendar)) {
            sfd = new SimpleDateFormat(todySDF);
            time = sfd.format(date);
            return time;
        } else {
            targetCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(targetCalendar)) {
                sfd = new SimpleDateFormat(yesterDaySDF);
                time = sfd.format(date);
                return time;
            }
        }
        sfd = new SimpleDateFormat(otherSDF);
        time = sfd.format(date);
        return time;
    }

	// 判断一个日期是否是@monthStep个月以内的
	public static int month3Compare(Calendar calendar, int monthStep) {
		Calendar currCalendar = Calendar.getInstance(); // 获取当前日期
		if (calendar.getTimeInMillis() < currCalendar.getTimeInMillis()) {
			return -1;
		}
		currCalendar.set(currCalendar.MONTH, currCalendar.get(currCalendar.MONTH)+monthStep);
		if (calendar.getTimeInMillis() > currCalendar.getTimeInMillis()) {
			return 1;
		}
		return 0;
	}

	public static Calendar getCalendar(int monthStep) {
		Calendar currCalendar = Calendar.getInstance(); // 获取当前日期
		currCalendar.set(currCalendar.MONTH, currCalendar.get(currCalendar.MONTH)+monthStep);
		return currCalendar;
	}
}
