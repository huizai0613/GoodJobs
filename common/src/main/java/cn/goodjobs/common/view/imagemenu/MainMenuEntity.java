package cn.goodjobs.common.view.imagemenu;

import android.content.Intent;

public class MainMenuEntity {
	public String title;
	public String content;
	public int iconRes;
	public String color;
	public boolean showNew;
	public String toClass; //  跳转的activity
	public Intent intent;
	public int padding; // 间距大小
	public float scale; // 竖直方向所占比例
	public int imgW;
	public int imgH;
}
