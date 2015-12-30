package cn.goodjobs.common.view.ExpandTab;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.goodjobs.common.R;

/**
 * Created by zhuli on 2015/12/15.
 */
public class SingleListView extends RelativeLayout implements ViewBaseAction{
    private ListView mListView;
    private  String[] items=new String[2];//显示字段
    private  String[] itemsVaule=new String[2];//隐藏id
    private OnSelectListener mOnSelectListener;
    private TextAdapter adapter;
    private String mDistance;
    private String showText = "item1";
    private Context mContext;

    public String getShowText() {
        return showText;
    }

    public SingleListView(Context context) {
        super(context);
        mContext = context;
    }

    public SingleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public SingleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_distance, this, true);
        mListView = (ListView) findViewById(R.id.listView);
        adapter = new TextAdapter(context, items, R.drawable.choose_item_right, R.drawable.choose_eara_item_selector);
        //设置字体大小
        adapter.setTextSize(15);
        if (mDistance != null) {
            for (int i = 0; i < itemsVaule.length; i++) {
                if (itemsVaule[i].equals(mDistance)) {
                    adapter.setSelectedPositionNoNotify(i);
                    showText = items[i];
                    break;
                }
            }
        }
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {

                if (mOnSelectListener != null) {
                    showText = items[position];
                    mOnSelectListener.getValue(itemsVaule[position], items[position]);
                }
            }
        });
    }

    //设置初始值
    public void setInitValue(ArrayList<String> value){
        items=new String[value.size()];
        itemsVaule=new String[value.size()];
        for (int i = 0; i < value.size(); i++) {
            items[i]=value.get(i);
            itemsVaule[i]=String.valueOf(i);
        }
        init(mContext);
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener {
        public void getValue(String distance, String showText);
    }
    @Override
    public void hide() {

    }

    @Override
    public void show() {

    }
}
