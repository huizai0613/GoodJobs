package cn.goodjobs.common.util;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

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
                     String cancelText, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {
        LinearLayout dialogview = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
        TextView tvTitle = (TextView) dialogview.findViewById(R.id.tvTitle);
        TextView tvMsg = (TextView) dialogview.findViewById(R.id.tvMsg);
        TextView tvOk = (TextView) dialogview.findViewById(R.id.tvOk);
        TextView tvCancel = (TextView) dialogview.findViewById(R.id.tvCancel);

        dialogview.findFocus();
        final AlertDialog dialog = new AlertDialog.Builder(context).create() ;
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(dialogview);
        dialog.setCancelable(true);

        tvTitle.setText(title);
        tvMsg.setText(msg);
        if (!hasCancel) {
            tvCancel.setVisibility(View.GONE);
        } else {
            tvCancel.setText(cancelText);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (cancelListener != null) {
                        cancelListener.onClick(dialog, 1);
                    }
                }
            });
        }
        tvOk.setText(okText);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (okListener != null) {
                    okListener.onClick(dialog, 0);
                }
            }
        });
    }

//    public static void show(Context context, String title, String msg, boolean hasCancel, String okText,
//                            String cancelText, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
////        builder.setIcon(R.drawable.ic_36);
//        builder.setTitle(title);
//        builder.setMessage(msg);
//        builder.setPositiveButton(okText, okListener);
//        if (hasCancel) {
//            builder.setNegativeButton(cancelText, cancelListener);
//        }
//        builder.show();
//    }

//    public static void show(Context context, int title, String msg, boolean hasCancel, String okText,
//                     String cancelText, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
////        builder.setIcon(R.drawable.ic_36);
//        builder.setTitle(title);
//        builder.setMessage(msg);
//        builder.setPositiveButton(okText, okListener);
//        if (hasCancel) {
//            builder.setNegativeButton(cancelText, cancelListener);
//        }
//        builder.show();
//    }

    public static void show(Context context, String msg) {
        show(context, R.string.app_name, msg, false, "确定", "", null, null);
    }

    public static void show(Context context, int title, String msg, boolean hasCancel, String okText,
                                   String cancelText, final DialogInterface.OnClickListener okListener, final DialogInterface.OnClickListener cancelListener) {
        LinearLayout dialogview = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.layout_dialog, null);
        TextView tvTitle = (TextView) dialogview.findViewById(R.id.tvTitle);
        TextView tvMsg = (TextView) dialogview.findViewById(R.id.tvMsg);
        TextView tvOk = (TextView) dialogview.findViewById(R.id.tvOk);
        TextView tvCancel = (TextView) dialogview.findViewById(R.id.tvCancel);

        dialogview.findFocus();
        final AlertDialog dialog = new AlertDialog.Builder(context).create() ;
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(dialogview);
        dialog.setCancelable(true);

        tvTitle.setText(title);
        tvMsg.setText(msg);
        if (!hasCancel) {
            tvCancel.setVisibility(View.GONE);
        } else {
            tvCancel.setText(cancelText);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (cancelListener != null) {
                        cancelListener.onClick(dialog, 1);
                    }
                }
            });
        }
        tvOk.setText(okText);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (okListener != null) {
                    okListener.onClick(dialog, 0);
                }
            }
        });
    }
}
