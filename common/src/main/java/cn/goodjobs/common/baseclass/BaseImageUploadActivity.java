package cn.goodjobs.common.baseclass;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.TipsUtil;

public class BaseImageUploadActivity extends BaseActivity {
	protected PopupWindow window;
	protected String saveName = "goodjobs.jpg" ;
	protected String savePath ;

	public static final int FLAG_CHOOSE_IMG = 5;
	public static final int FLAG_CHOOSE_PHONE = 6;
	public static final int FLAG_MODIFY_FINISH = 7;
	protected int zoomW = 1 ;
	protected int zoomH = 1 ;
	protected boolean isModify;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savePath = "file:///sdcard/" + saveName;
	}

	@Override
	protected int getLayoutID() {
		return 0;
	}

	@Override
	protected void initWeightClick() {

	}

	@Override
	protected void initWeight() {

	}

	@Override
	protected void initData() {

	}

	public void showBottomBtns() {
		if (isSDExist()) {
			LayoutInflater lay = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View photoView = lay.inflate(R.layout.bottom_btns, null);
			window = new PopupWindow(photoView, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			window.setAnimationStyle(R.style.AnimBottom);
			window.setFocusable(true);
			window.setTouchable(true);
			window.setBackgroundDrawable(new BitmapDrawable());
			window.setOutsideTouchable(true);
			window.showAtLocation(photoView, Gravity.BOTTOM, 0, 0);
			window.update();
		} else {
			TipsUtil.show(this, "未检测到存储卡!") ;
		}
	}

	/**
	 * 判断SD卡是否可用
	 * 
	 * @return
	 */
	private boolean isSDExist() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 拍照
	 * @param v
	 */
	public void capturePhoto(View v) {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				if (isModify) {
					ImageUtil.getPicFromCaptureOrLocal(this, Uri.parse(savePath), true,
							FLAG_CHOOSE_PHONE) ;
				} else {
					ImageUtil.getPicFromCaptureOrLocal(this, Uri.parse(savePath), true,
							FLAG_CHOOSE_PHONE) ;
				}
				window.dismiss();
			} catch (ActivityNotFoundException e) {
			}
		}
	}

	/**
	 * 从本地获取图片
	 * 
	 * @param v
	 */
	public void pickPhotoFromLocal(View v) {
		ImageUtil.getPicFromCaptureOrLocal(this, null, false, FLAG_CHOOSE_IMG);
		window.dismiss();
	}

	/**
	 * 取消
	 * 
	 * @param v
	 */
	public void cancel(View v) {
		window.dismiss();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				if (isModify) {
					Uri uri = data.getData();
					ImageUtil.startPhotoZoom(this, uri,Uri.parse(savePath), zoomW, zoomH,
							FLAG_MODIFY_FINISH);
				} else {
					onImageFinish(data.getStringExtra("files"));
				}
			}
		} else if (requestCode == FLAG_CHOOSE_PHONE && resultCode == RESULT_OK) {
			if (isModify) {
				ImageUtil.startPhotoZoom(this, Uri.parse(savePath),Uri.parse(savePath)
						,zoomW, zoomH, FLAG_MODIFY_FINISH);
			} else {
				onImageFinish(Uri.parse(savePath));
			}
		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				onImageFinish(Uri.parse(savePath));
			}
		}
	}
	
	protected void onImageFinish(Uri fileUri) {
		
	}
	
	protected void onImageFinish(String files) {
		
	}
}