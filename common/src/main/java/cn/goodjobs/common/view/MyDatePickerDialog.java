package cn.goodjobs.common.view;

import android.app.DatePickerDialog;
import android.content.Context;

/**
 * Created by wanggang on 2015/11/13 0013.
 */
public class MyDatePickerDialog extends DatePickerDialog {

    public MyDatePickerDialog(Context context, OnDateSetListener callBack,
                              int year, int monthOfYear, int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    protected void onStop() {
        //super.onStop();
    }
}