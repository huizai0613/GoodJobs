package cn.goodjobs.common.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.goodjobs.common.view.MyDatePickerDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

/**
 * Created by wanggang on 2015/12/26 0026.
 */
public class DatePickerUtil implements View.OnClickListener {
    private View clickView;
    private Context context;
    private MyDatePickerDialog datePickerDialog;
    private String format;
    private String date;

    public DatePickerUtil(Context context, View view, String format, String date) {
        this.clickView = view;
        this.context = context;
        this.format = format;
        this.date = date;
        this.clickView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (datePickerDialog == null) {
            final SimpleDateFormat sdf = new SimpleDateFormat(format);
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
            datePickerDialog = new MyDatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.set(Calendar.YEAR, year);
//                    calendar.set(Calendar.MONTH, monthOfYear);
//                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    Date d = new Date(year-1900, monthOfYear, dayOfMonth);
                    date = sdf.format(d);
                    if (clickView instanceof SearchItemView) {
                        ((SearchItemView) clickView).setText(date);
                    }
                }
            }, targetCalendar.get(Calendar.YEAR), targetCalendar.get(Calendar.MONTH), targetCalendar.get(Calendar.DAY_OF_MONTH));
        }
        datePickerDialog.show();
    }
}
