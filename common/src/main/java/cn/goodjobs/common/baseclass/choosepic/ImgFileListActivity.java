package cn.goodjobs.common.baseclass.choosepic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;

public class ImgFileListActivity extends BaseActivity implements OnItemClickListener{

	ListView listView;
	ImgFileListAdapter fileListAdapter;
	Util util;
	List<FileTraversal> locallist;
	private int imageCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getLayoutID() {
		return R.layout.imgfilelist;
	}

	@Override
	protected void initWeightClick() {

	}

	@Override
	protected void initWeight() {
		setTopTitle("选择图片");
		imageCount = getIntent().getIntExtra("imageCount", 0);
		listView = (ListView) findViewById(R.id.listView);
		fileListAdapter = new ImgFileListAdapter(this);
		listView.setOnItemClickListener(this);
		listView.setAdapter(fileListAdapter);

		util=new Util(this);
		locallist=util.LocalImgFileList();
		List<HashMap<String, String>> listdata=new ArrayList<HashMap<String,String>>();
		Bitmap bitmap[] = null;
		if (locallist!=null) {
			bitmap=new Bitmap[locallist.size()];
			for (int i = 0; i < locallist.size(); i++) {
				HashMap<String, String> map=new HashMap<String, String>();
				map.put("filecount", locallist.get(i).filecontent.size()+"张");
				map.put("imgpath", locallist.get(i).filecontent.get(0)==null?null:(locallist.get(i).filecontent.get(0)));
				map.put("filename", locallist.get(i).filename);
				listdata.add(map);
			}
		}
		fileListAdapter.appendToList(listdata);
	}

	@Override
	protected void initData() {

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 110 && resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			this.finish();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent=new Intent(this,ImgsActivity.class);
		Bundle bundle=new Bundle();
		bundle.putParcelable("data", locallist.get(position));
		intent.putExtra("imageCount", imageCount);
		intent.putExtras(bundle);
		startActivityForResult(intent, 110);
	}
}
