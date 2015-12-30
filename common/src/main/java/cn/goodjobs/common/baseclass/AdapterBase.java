package cn.goodjobs.common.baseclass;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class AdapterBase<T> extends BaseAdapter {
	
	private final List<T> mList = new LinkedList<T>();
	protected Context context ;
	
	public AdapterBase(Context context) {
		this.context = context ;
	}
	
	public List<T> getList(){
		return mList;
	}
	
	public void appendToList(List<T> list) {
		if (list == null) {
			notifyDataSetChanged();
			return;
		}
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void appendToTopList(List<T> list) {
		if (list == null) {
			return;
		}
		mList.addAll(0, list);
		notifyDataSetChanged();
	}
	
	public void removeItem(int position){
		mList.remove(position);
	}
	
	public void insertItem(int position,T t){
		mList.add(position, t);
	}
	
	public void updateItem(int position,T t){
		mList.set(position, t);
	}

	public void clear() {
		mList.clear();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		if(position > mList.size()-1){
			return null;
		}
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == getCount() - 1) {
			onReachBottom();
		}
		return getExView(position, convertView, parent);
	}
	

	protected View getExView(int position, View convertView, ViewGroup parent){
		return null ;
	};
	
	protected void onReachBottom(){};


}
