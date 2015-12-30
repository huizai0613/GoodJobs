package cn.goodjobs.common.view.ExpandTabSuper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.Map;

import cn.goodjobs.common.R;


public class SingleLevelMenuView extends RelativeLayout implements
        MultiLevelMenuViewBaseAction
{

    private ListView mListView;
    private MultiLevelMenuAdapter mAdapter;
    private Map<String, String> mData;
    private String mSelectedKey;
    private OnSelectListener mOnSelectListener;

    public SingleLevelMenuView(Context context)
    {
        this(context, null);
    }

    public SingleLevelMenuView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SingleLevelMenuView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.v_single_level_menu, this, true);
        mListView = (ListView) findViewById(R.id.lv_single_level);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener)
    {
        this.mOnSelectListener = onSelectListener;
    }

    public void setValue(Map<String, String> data, String selectedKey)
    {
        this.mData = data;
        this.mSelectedKey = selectedKey;
        mAdapter = new MultiLevelMenuAdapter(getContext(), mData, R.drawable.withe,
                R.drawable.withe, R.color.main_color);
        mAdapter.setSelectedKeyNoNotify(mSelectedKey);
        mAdapter.setTextSize(14);
        mListView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiLevelMenuAdapter.OnItemClickListener()
        {

            @Override
            public void onItemClick(View view, String key)
            {
                mSelectedKey = key;
                ExpandTabView tag = (ExpandTabView) SingleLevelMenuView.this.getTag();
                if (tag != null) {
                    tag.onPressBack();
                }
                if (mOnSelectListener != null) {
                    mOnSelectListener.onSelected(key, mData.get(key));
                }
            }
        });
    }

    @Override
    public void hide()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void show()
    {
        // TODO Auto-generated method stub

    }

    public interface OnSelectListener
    {
        public void onSelected(String selectedKey, String showString);
    }

}
