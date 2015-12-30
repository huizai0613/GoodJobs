package cn.goodjobs.common.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import cn.goodjobs.common.R;

/**
 * Created by Administrator on 2015/10/12 0012.
 */
public class AlertDialogUtil {

    /**
     * 显示系统对话框
     * @param title dialog标题
     * @param msg 提示信息
     * @param hasCancel 是否包含取消按钮
     * @param okText 确定按钮文本
     * @param cancelText 取消按钮文本
     * @param okListener 确定按钮回调
     * @param cancelListener 取消按钮回调
     * */
    public static void show(Context context, String title, String msg, boolean hasCancel, String okText,
                     String cancelText, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_36);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(okText, okListener);
        if (hasCancel) {
            builder.setNegativeButton(cancelText, cancelListener);
        }
        builder.show();
    }

    public static void show(Context context, int title, String msg, boolean hasCancel, String okText,
                     String cancelText, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.ic_36);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(okText, okListener);
        if (hasCancel) {
            builder.setNegativeButton(cancelText, cancelListener);
        }
        builder.show();
    }

    public static void show(Context context, String msg) {
        show(context, R.string.app_name, msg, false, "确定", "", null, null);
    }
}
