package cn.goodjobs.common.view.ExpandTab;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.LogUtil;

public class TextAdapter extends ArrayAdapter<String> {

	private Context mContext;
	private List<String> mListData;
	private String[] mArrayData;
	private int selectedPos = -1;
	private String selectedText = "";
	private int normalDrawbleId;
	private Drawable selectedDrawble;
	private float textSize;
	private OnClickListener onClickListener;
	private OnItemClickListener mOnItemClickListener;
	private HashMap<String,String>select;//记录选中的值，以便于下次切换时保存状态（对于两级）
	private String selectedData="";//被选中的group值
	private int count=0;

	public TextAdapter(Context context, List<String> listData, int sId, int nId) {
		super(context, R.layout.choose_item, listData);
		mContext = context;
		mListData = listData;
		if(sId== R.drawable.choose_item_right) {
			selectedDrawble = mContext.getResources().getDrawable(sId);
		}else{
			selectedDrawble=null;
		}
		normalDrawbleId = nId;
		select=new HashMap<String,String>();
		init();
	}

	private void init() {
		onClickListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				selectedPos = (Integer) view.getTag();
				setSelectedPosition(selectedPos);
				if (mOnItemClickListener != null) {
					mOnItemClickListener.onItemClick(view, selectedPos);
				}
			}
		};
	}

	public TextAdapter(Context context, String[] arrayData, int sId, int nId) {
		super(context,R.layout.choose_item,arrayData);
		mContext = context;
		mArrayData = arrayData;
		if(sId== R.drawable.choose_item_right) {
			selectedDrawble = mContext.getResources().getDrawable(sId);
		}else{
			selectedDrawble=null;
		}
		normalDrawbleId = nId;
		select=new HashMap<String,String>();
		init();
	}

	/**
	 * 设置选中的position,并通知列表刷新
	 */
	public void setSelectedPosition(int pos) {
		if (mListData != null && pos < mListData.size()) {
			selectedPos = pos;
			selectedText = mListData.get(pos);
			notifyDataSetChanged();
		} else if (mArrayData != null && pos < mArrayData.length) {
			selectedPos = pos;
			selectedText = mArrayData[pos];
			notifyDataSetChanged();
		}

	}

	public void setSelectedPositionNoNotify(String data,int pos) {
		if (mListData != null && pos < mListData.size()) {
			selectedPos = pos;
			selectedText = mListData.get(pos);
			notifyDataSetChanged();
		} else if (mArrayData != null && pos < mArrayData.length) {
			selectedPos = pos;
			selectedData=data;
			selectedText = mArrayData[pos];
			select.put(data, selectedText);
		}
		count=1;
	}

	public void setSelectedData(String data){
		selectedData=data;
		selectedText=select.get(selectedData);
		count=1;
	}


	/**
	 * 设置选中的position,但不通知刷新
	 */
	public void setSelectedPositionNoNotify(int pos) {
		selectedPos = pos;
		if (mListData != null && pos < mListData.size()) {
			selectedText = mListData.get(pos);
		} else if (mArrayData != null && pos < mArrayData.length) {
			selectedText = mArrayData[pos];
		}
	}

	/**
	 * 获取选中的position
	 */
	public int getSelectedPosition() {
		if (mArrayData != null && selectedPos < mArrayData.length) {
			return selectedPos;
		}
		if (mListData != null && selectedPos < mListData.size()) {
			return selectedPos;
		}
		return -1;
	}

	/**
	 * 设置列表字体大小
	 */
	public void setTextSize(float tSize) {
		textSize = tSize;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view;
		if (convertView == null) {
			view = (TextView) LayoutInflater.from(mContext).inflate(R.layout.choose_item, parent, false);
		} else {
			view = (TextView) convertView;
		}
		view.setTag(position);
		String mString = "";
		if (mListData != null) {
			if (position < mListData.size()) {
				mString = mListData.get(position);
			}
		} else if (mArrayData != null) {
			if (position < mArrayData.length) {
				mString = mArrayData[position];
			}
		}
		if (mString.contains("全部"))
			view.setText("全部");
		else
			view.setText(mString);
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

		if(count==1){
			if(select.get(selectedData)==null){
				if(selectedData!=null)
				select.put(selectedData, mListData.get(0));
				LogUtil.info(select.get(selectedData));
				if (select.get(selectedData).equals(mString)) {
					if(selectedDrawble!=null) {
						view.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
					}else{
						view.setBackgroundColor(mContext.getResources().getColor(R.color.choose_eara_item_press_color));
					}
				} else {
					view.setBackgroundDrawable(mContext.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图片
				}

			}else{
				select.put(selectedData, selectedText);
				if (mString.equals(select.get(selectedData))) {
					if(selectedDrawble!=null) {
						view.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
					}else{
						view.setBackgroundColor(mContext.getResources().getColor(R.color.choose_eara_item_press_color));
					}
				} else {
					view.setBackgroundDrawable(mContext.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图片
				}
			}
		}else{
			if (selectedText != null && selectedText.equals(mString)) {
				if(selectedDrawble!=null) {
					view.setBackgroundDrawable(selectedDrawble);//设置选中的背景图片
				}else{
					view.setBackgroundColor(mContext.getResources().getColor(R.color.choose_eara_item_press_color));
				}
			} else {
				view.setBackgroundDrawable(mContext.getResources().getDrawable(normalDrawbleId));//设置未选中状态背景图片
			}
		}
		view.setPadding(20, 0, 0, 0);
		view.setOnClickListener(onClickListener);
		return view;
	}

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	/**
	 * 重新定义菜单选项单击接口
	 */
	public interface OnItemClickListener {
		public void onItemClick(View view, int position);
	}

}
