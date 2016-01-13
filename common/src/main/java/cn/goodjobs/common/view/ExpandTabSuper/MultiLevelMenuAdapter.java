package cn.goodjobs.common.view.ExpandTabSuper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.goodjobs.common.R;


public class MultiLevelMenuAdapter extends BaseAdapter
{
    private Context mContext;
    private Map<String, String> mData;
    private List<String> mKey;
    private OnClickListener mOnClickListener;
    private OnItemClickListener mOnItemClickListener;
    private String mSelectedKey = "";
    private float mTextSize;
    private int mSelectedDrawbleId;
    private int mNormalDrawbleId;
    private int textSelectColor;

    public MultiLevelMenuAdapter(Context context, Map<String, String> data,
                                 int sId, int nId, int textSelectColor)
    {
        super();
        this.mContext = context;
        this.mData = data;
        this.mSelectedDrawbleId = sId;
        this.mNormalDrawbleId = nId;
        this.textSelectColor = textSelectColor;
        init();
    }


    public void init()
    {
        Set<String> keys = mData.keySet();
        if (mKey == null) {
            mKey = new ArrayList<String>();
        }
        mKey.clear();
        Iterator<String> it = keys.iterator();

        while (it.hasNext()) {
            mKey.add(it.next());
        }
        mOnClickListener = new OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                mSelectedKey = mKey.get((Integer) view.getTag());
                setSelectedKey(mSelectedKey);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view,
                            mSelectedKey);
                }
            }
        };
    }

    @Override
    public int getCount()
    {
        return mData.size();
    }

    @Override
    public String getItem(int position)
    {
        return mData.get(mKey.get(position));
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        TextView view;
        if (convertView == null) {
            view = (TextView) LayoutInflater.from(mContext).inflate(
                    R.layout.v_multi_menu_choose_item, parent, false);
        } else {
            view = (TextView) convertView;
        }
        String s = mKey.get(position);
        if (mData != null && mKey != null) {
            view.setText(mData.get(s));
        }
        if (mSelectedKey.equalsIgnoreCase(mKey.get(position))) {
            if (mSelectedDrawbleId != 0) {
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    view.setBackground(mContext.getResources().getDrawable(
                            mSelectedDrawbleId));
                } else {
                    view.setBackgroundDrawable(mContext.getResources().getDrawable(
                            mSelectedDrawbleId));
                }

                view.setTextColor(mContext.getResources().getColor(textSelectColor));

                Drawable drawable = parent.getResources().getDrawable(R.drawable.set_2);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                view.setCompoundDrawables(null, null, drawable, null);
            }

        } else {
            if (mNormalDrawbleId != 0) {
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    view.setBackground(mContext.getResources().getDrawable(
                            mNormalDrawbleId));
                } else {
                    view.setBackgroundDrawable(mContext.getResources().getDrawable(
                            mNormalDrawbleId));
                }
                view.setTextColor(mContext.getResources().getColor(R.color.main_color));
                view.setCompoundDrawables(null, null, null, null);
            }
        }
//        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        view.setTag(position);
        view.setPadding(20, 0, 0, 0);
        view.setOnClickListener(mOnClickListener);
        if (s.equals("1000")) {
            view.setClickable(false);
        }
        return view;
    }

    public void setOnItemClickListener(OnItemClickListener l)
    {
        mOnItemClickListener = l;
    }

    /**
     * 设置选中的position,但不通知刷新
     */
    public void setSelectedKeyNoNotify(String key)
    {
        if (mData != null) {
            mSelectedKey = key;
        }
    }

    public String getSelectedKey()
    {
        return mSelectedKey;
    }

    /**
     * 设置选中的Key,并通知列表刷新
     */
    public void setSelectedKey(String key)
    {
        if (mData != null) {
            mSelectedKey = key;
            notifyDataSetChanged();
        }
    }

    /**
     * 设置列表字体大小
     */
    public void setTextSize(float tSize)
    {
        mTextSize = tSize;
    }

    @Override
    public void notifyDataSetChanged()
    {
        super.notifyDataSetChanged();
    }

    /**
     * 重新定义菜单选项单击接口
     */
    public interface OnItemClickListener
    {
        public void onItemClick(View view, String key);
    }
}
