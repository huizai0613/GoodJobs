package cn.goodjobs.common.baseclass.choosepic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.TipsUtil;

public class ImgsActivity extends BaseActivity implements OnItemClickListener {

	Bundle bundle;
	FileTraversal fileTraversal;
	GridView imgGridView;
	LinearLayout select_layout;
	Util util;
	RelativeLayout relativeLayout2;
	HashMap<Integer, ImageView> hashImage;
	Button choise_button;
	ImgsAdapter imgsAdapter;
	private int imageCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getLayoutID() {
		return R.layout.photogrally;
	}

	@Override
	protected void initWeightClick() {

	}

	@Override
	protected void initWeight() {
		try {
			imgGridView=(GridView) findViewById(R.id.gridview);
			bundle= getIntent().getExtras();
			fileTraversal=bundle.getParcelable("data");
			setTopTitle(fileTraversal.filename);

			imgsAdapter = new ImgsAdapter(this);
			imgGridView.setAdapter(imgsAdapter);
			imgsAdapter.appendToList(fileTraversal.filecontent);

			select_layout=(LinearLayout) findViewById(R.id.selected_image_layout);
			relativeLayout2=(RelativeLayout) findViewById(R.id.bottomLayout);
			choise_button=(Button) findViewById(R.id.selected_btn);
			hashImage=new HashMap<Integer, ImageView>();
			util=new Util(this);

			imgGridView.setOnItemClickListener(this);
			imageCount = getIntent().getIntExtra("imageCount", 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initData() {

	}


	@SuppressLint("NewApi")
	public ImageView iconImage(String filepath,int index){
		LayoutParams params=new LayoutParams(relativeLayout2.getMeasuredHeight()-10, relativeLayout2.getMeasuredHeight()-10);
		params.leftMargin = 5;
		ImageView imageView=new ImageView(this);
		imageView.setLayoutParams(params);
		float alpha=1.0f;
		imageView.setAlpha(alpha);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageBitmap(imgsAdapter.bitmaps[index]);
		return imageView;
	}
	
	/**
	 * FIXME
	 * 亲只需要在这个方法把选中的文档目录已list的形式传过去即可
	 * @param view
	 */
	public void doSelected(View view){
		Intent intent = new Intent();
		Bundle bundle=new Bundle();
		bundle.putString("files", imgsAdapter.files);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		if (imgsAdapter.check(position)) {
			imageCount--;
			imgsAdapter.itemClick(position);
			select_layout.removeView(hashImage.get(position));
			choise_button.setText("已选择("+imgsAdapter.getFilesCount()+")张");
		} else if (imageCount<4) {
			imageCount++;
			String file = imgsAdapter.itemClick(position);
			ImageView imageView=iconImage(file, position);
			if (imageView !=null) {
				hashImage.put(position, imageView);
				select_layout.addView(imageView);
				choise_button.setText("已选择("+imgsAdapter.getFilesCount()+")张");
			}
		} else {
			TipsUtil.show(this, "您最多可以上传4张图片");
		}
	}
}
