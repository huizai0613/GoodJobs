package cn.goodjobs.common.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class KeyBoardUtil {
	// 隐藏键盘
	public static boolean hide(Activity activity) {
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (activity.getCurrentFocus() != null
				&& activity.getCurrentFocus().getWindowToken() != null) {
			boolean value = manager.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			return value;
		}
		return false;
	}

	// 自动弹出键盘
	public static void show(EditText edit) {
		edit.requestFocus();
		InputMethodManager imm = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED); 
	}
}
