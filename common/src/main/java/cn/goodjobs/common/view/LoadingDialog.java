package cn.goodjobs.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.goodjobs.common.R;

public class LoadingDialog {
	private static Dialog dialog;

	public static void showDialog(Activity context) {
		showDialog(context, "请稍候...");
	}

	public static void showDialog(Activity context,String strContent) {
		if (context != null) {
			if (dialog == null) {
				dialog = new Dialog(context, R.style.Custom_Progress);
				View view = LayoutInflater.from(context).inflate(R.layout.loading_view, null);
				TextView tipTextView = (TextView) view.findViewById(R.id.tipTextView);
				tipTextView.setText(strContent);
				dialog.setContentView(view);
				dialog.setCanceledOnTouchOutside(false);
			}
			dialog.show();
		}
	}

	public static void hide() {
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
}
