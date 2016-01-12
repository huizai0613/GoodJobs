package cn.goodjobs.common.baseclass.choosepic;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.AdapterBase;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.StringUtil;

public class ImgsAdapter extends AdapterBase<String> {
	Bitmap[] bitmaps;
	public String files = "";
	public ImgsAdapter(Context context) {
		super(context);
	}
	
	@Override
	public void appendToList(List<String> list) {
		super.appendToList(list);
		bitmaps = new Bitmap[list.size()];
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.imgsitem, null);
			viewHolder = new ViewHolder();
			viewHolder.imageview = (ImageView) convertView.findViewById(R.id.imageview);
			viewHolder.selected = (ImageView) convertView.findViewById(R.id.selected);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String file = getItem(position);
		if (bitmaps[position] == null) {
			bitmaps[position] = ImageUtil.compressImageFromFile(file, 200, 200);
		}
		if (files.contains(file+";")) {
			viewHolder.selected.setVisibility(View.VISIBLE);
		} else {
			viewHolder.selected.setVisibility(View.INVISIBLE);
		}
		viewHolder.imageview.setImageBitmap(bitmaps[position]);
		return convertView;
	}

	static class ViewHolder {
		ImageView imageview, selected;
	}
	
	public String itemClick(int position) {
		String file = getItem(position);
		if (files.contains(file+";")) {
			files = files.replaceAll(file+";", "");
		} else {
			files = files + file + ";";
		}
		notifyDataSetChanged();
		return file;
	}
	
	public boolean check(int position) {
		String file = getItem(position);
		return files.contains(file+";");
	}
	
	public String[] getFiles() {
		return files.split(";");
	}
	
	public int getFilesCount() {
		if (StringUtil.isEmpty(files)) {
			return 0;
		}
		return getFiles().length;
	}
	
}
