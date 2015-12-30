package cn.goodjobs.common.view.ExpandTab;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import cn.goodjobs.common.R;

/**
 * Created by zhuli on 2015/12/15.
 */
public class DoubleListView extends LinearLayout implements ViewBaseAction
{
    private ListView regionListView;
    private ListView plateListView;
    private ArrayList<String> groups = new ArrayList<String>();
    private LinkedList<String> childrenItem = new LinkedList<String>();
    private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
    private TextAdapter plateListViewAdapter;
    private TextAdapter earaListViewAdapter;
    private OnSelectListener mOnSelectListener;
    private int tEaraPosition = 0;//记录选中的组位置
    private int tBlockPosition = 0;//记录选中的子位置
    private String showString = "不限";//返回组名+子名，方便操作
    private Context mContext;

    public DoubleListView(Context context)
    {
        super(context);
        mContext = context;
    }

    public DoubleListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
    }


    private void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_region, this, true);
        regionListView = (ListView) findViewById(R.id.listView);
        plateListView = (ListView) findViewById(R.id.listView2);
        earaListViewAdapter = new TextAdapter(context, groups,
                R.drawable.choose_item_selected,
                R.drawable.choose_eara_item_selector);
        plateListViewAdapter = new TextAdapter(context, childrenItem,
                R.drawable.choose_item_right,
                R.drawable.choose_plate_item_selector);
        earaListViewAdapter.setTextSize(15);
//        plateListViewAdapter.setSelectedData(groups.get(tEaraPosition));
        earaListViewAdapter.setSelectedPositionNoNotify(groups.get(tEaraPosition), tEaraPosition);
        regionListView.setAdapter(earaListViewAdapter);
        earaListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener()
                {

                    @Override
                    public void onItemClick(View view, int position)
                    {
                        if (position < groups.size()) {
                            tEaraPosition = position;
                            childrenItem.clear();
                            childrenItem.addAll(children.get(position));
                            plateListViewAdapter.setSelectedData(groups.get(position));
                            plateListViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
        if (tEaraPosition < children.size())
            childrenItem.addAll(children.get(tEaraPosition));
        plateListViewAdapter.setTextSize(15);
        plateListViewAdapter.setSelectedData(groups.get(tEaraPosition));
        plateListViewAdapter.setSelectedPositionNoNotify(groups.get(tEaraPosition), tBlockPosition);
        plateListView.setAdapter(plateListViewAdapter);
        plateListViewAdapter
                .setOnItemClickListener(new TextAdapter.OnItemClickListener()
                {

                    @Override
                    public void onItemClick(View view, final int position)
                    {
                        tBlockPosition = position;
                        plateListViewAdapter.setSelectedPositionNoNotify(childrenItem.get(tBlockPosition), position);
                        plateListViewAdapter.notifyDataSetChanged();

                        showString = groups.get(tEaraPosition) + childrenItem.get(position);
                        hide();
                        if (mOnSelectListener != null) {
                            mOnSelectListener.getValue(showString);
                        }

                    }
                });
        if (tBlockPosition < childrenItem.size())
            showString = childrenItem.get(tBlockPosition);
        if (showString.contains("不限")) {
            showString = showString.replace("不限", "");
        }
        setDefaultSelect();

    }

    //设置初始值
    public void setInitValue(Map<String, LinkedList<String>> value)
    {
        int i = 0;
        for (String group : value.keySet()) {
            groups.add(group);
            children.put(i, value.get(group));
            i++;
        }
        init(mContext);
    }


    public void setDefaultSelect()
    {
        regionListView.setSelection(tEaraPosition);
        plateListView.setSelection(3);
        plateListViewAdapter.setSelectedData(groups.get(tEaraPosition));
        plateListViewAdapter.setSelectedPositionNoNotify(groups.get(tEaraPosition), tBlockPosition);
        plateListViewAdapter.notifyDataSetChanged();
    }

    public String getShowText()
    {
        return showString;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener)
    {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener
    {
        public void getValue(String showText);
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

}
