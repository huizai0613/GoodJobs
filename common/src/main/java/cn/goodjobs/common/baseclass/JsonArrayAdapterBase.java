package cn.goodjobs.common.baseclass;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class JsonArrayAdapterBase<T> extends BaseAdapter {

	public final List<T> mList = new LinkedList<T>();

	protected Context context ;

	public JsonArrayAdapterBase(Context context) {
		this.context = context ;
	}
	
	public List<T> getList(){
		return mList;
	}
	
	public void appendToList(JSONArray jsonArray)  {
		if (jsonArray == null) {
			return;
		}
		int size = jsonArray.length();
		try {
			for (int i=0; i< size; i++) {
				mList.add((T) jsonArray.get(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}

	public void append(T object)  {
		if (object == null) {
			return;
		}
		mList.add(object);
		notifyDataSetChanged();
	}

	public void appendToList(List<T> list)  {
		if (list == null) {
			return;
		}
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public void appendToTopList(JSONArray jsonArray)  {
		if (jsonArray == null) {
			return;
		}
		int size = jsonArray.length();
		try {
			for (int i=size; i> 0; i--) {
				mList.add(0, (T) jsonArray.get(i-1));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		notifyDataSetChanged();
	}
	
	public void removeItem(int position){
		mList.remove(position);
	}

	public void removeObject(T object){
		mList.remove(object);
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

	protected View getExView(int position, View convertView, ViewGroup parent) {
		return null ;
	};
	
	protected void onReachBottom(){};




}
