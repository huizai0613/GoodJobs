package cn.goodjobs.common.baseclass.choosepic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.AdapterBase;
import cn.goodjobs.common.util.ImageUtil;

public class ImgFileListAdapter extends AdapterBase<HashMap<String, String>> {

	String filecount="filecount";
	String filename="filename";
	String imgpath="imgpath";
	Bitmap[] bitmaps;
	
	public ImgFileListAdapter(Context context) {
		super(context);
	}
	
	@Override
	public void appendToList(List<HashMap<String, String>> list) {
		super.appendToList(list);
		bitmaps = new Bitmap[list.size()];
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.imgfileadapter, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.filephoto_imgview);
			viewHolder.fileCount = (TextView) convertView.findViewById(R.id.filecount_textview);
			viewHolder.fileName = (TextView) convertView.findViewById(R.id.filename_textview);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		HashMap<String,String> map = getItem(position);
		if (bitmaps[position] == null) {
			bitmaps[position] = ImageUtil.compressImageFromFile(map.get(imgpath), 200, 200);
		}
		viewHolder.imageView.setImageBitmap(bitmaps[position]);
		viewHolder.fileCount.setText(map.get(filecount));
		viewHolder.fileName.setText(map.get(filename));
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		TextView fileCount;
		TextView fileName;
	}
}
