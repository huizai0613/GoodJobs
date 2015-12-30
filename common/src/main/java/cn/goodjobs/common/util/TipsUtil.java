package cn.goodjobs.common.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.goodjobs.common.R;

public class TipsUtil {

	private static Toast toast;
	private static TextView message;

	public static void show(Context context, String msg) {
		show(context, msg, true) ;
	}

	public static void show(Context context, int resid) {
		show(context, resid, true) ;
	}
	
	public static void show(Context context, String msg, boolean isShort) {
		if (toast == null) {
			setToast(context);
		}
		message.setText(msg);
		if (isShort) {
			toast.setDuration(Toast.LENGTH_SHORT);
		} else {
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.show();
	}
	
	public static void show(Context context, int resid, boolean isShort) {
		if (toast == null) {
			setToast(context);
		}
		message.setText(resid);
		if (isShort) {
			toast.setDuration(Toast.LENGTH_SHORT);
		} else {
			toast.setDuration(Toast.LENGTH_LONG);
		}
		toast.show();
	}

	private static void setToast(Context context) {
		View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);
		message = (TextView) toastRoot.findViewById(R.id.tvToast);

		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(toastRoot);
	}
}
