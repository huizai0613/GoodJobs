package cn.goodjobs.common.view.ExpandTabSuper;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.goodjobs.common.R;


public class TwoLevelMenuView extends LinearLayout implements
        MultiLevelMenuViewBaseAction
{
    private boolean isMultiCheck;//是否可多选
    private HashMap<String, String> multiCheckMap;
    private ListView mFirstLevelListView;
    private ListView mSecondLevelListView;
    private MultiFirstLevelMenuAdapter mFirstLevelAdatper;
    private MultiLevelMenuAdapter mSecondLevelAdapter;
    private String mFirstLevelKey = "";
    private String mSecondLevelKey = "";
    private String mTempFirstLevelKey = "";
    private OnSelectListener mOnSelectListener;
    private int[] firstLevelImg;

    public void setIsMultiCheck(boolean isMultiCheck)
    {
        this.isMultiCheck = isMultiCheck;

    }

    // 第一级菜单数据Map中的数据为{0 : "分类1", 1: "分类2",.....},即key是分类id, value是分类名
    private Map<String, String> mFristLevelData = new LinkedHashMap<String, String>();
    // 第二级菜单数据Map中的数据为{0:{0 : "子分类1", 1: "子分类2",.....}}
    private Map<String, Map<String, String>> mSecondLevelData = new LinkedHashMap<String, Map<String, String>>();
    private Map<String, String> mCurrentChildData = new LinkedHashMap<String, String>();

    public TwoLevelMenuView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }

    public TwoLevelMenuView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public TwoLevelMenuView(Context context)
    {
        this(context, null);
    }

    private void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.v_two_level_menu_layout, this, true);
        mFirstLevelListView = (ListView) findViewById(R.id.lv_first_level_menu);
        mSecondLevelListView = (ListView) findViewById(R.id.lv_second_level_menu);
    }


    public void setCheckMult(HashMap<String, String> multiCheckMap)
    {
        this.multiCheckMap = multiCheckMap;
    }

    public void setValue(Map<String, String> firstLevelData,
                         Map<String, Map<String, String>> secondLevelData, String firstLevelSelcetKey, String secondLevelSelectKey, int[] firstLevelImg)
    {


        this.mFristLevelData = firstLevelData;
        this.firstLevelImg = firstLevelImg;
        this.mSecondLevelData = secondLevelData;
        this.mFirstLevelKey = firstLevelSelcetKey;
        this.mSecondLevelKey = secondLevelSelectKey;

        if (multiCheckMap != null) {
            this.mSecondLevelKey = multiCheckMap.get(this.mFirstLevelKey);
        }

        mFirstLevelAdatper = new MultiFirstLevelMenuAdapter(getContext(),
                firstLevelData,
                R.drawable.choose_frist_select_bg,
                R.drawable.choose_first_level_item_selector, R.color.main_color, firstLevelImg);
        mFirstLevelAdatper.setTextSize(15);
        mFirstLevelAdatper.setSelectedKeyNoNotify(mFirstLevelKey);
        mCurrentChildData.clear();
        mCurrentChildData.putAll(mSecondLevelData.get(mFirstLevelAdatper
                .getSelectedKey()));
        mFirstLevelListView.setAdapter(mFirstLevelAdatper);
        mFirstLevelAdatper.setOnItemClickListener(new MultiFirstLevelMenuAdapter.OnItemClickListener()
        {

            @Override
            public void onItemClick(View view, String key)
            {
                mTempFirstLevelKey = key;
                if (mSecondLevelData.get(key) != null) {
                    mSecondLevelListView.setVisibility(View.VISIBLE);
                    if (mCurrentChildData != null) {
                        mCurrentChildData.clear();
                        mCurrentChildData.putAll(mSecondLevelData.get(key));
                        if (isMultiCheck) {
                            if (multiCheckMap == null) {
                                multiCheckMap = new HashMap<>();
                                for (int i = 0; i < mFristLevelData.size(); i++) {
                                    multiCheckMap.put(i + "", "0");
                                }
                            }
                            mSecondLevelAdapter.setSelectedKeyNoNotify(multiCheckMap.get(mTempFirstLevelKey));
                        } else {
                            if (!mFirstLevelKey.equals(mTempFirstLevelKey)) {
                                mSecondLevelAdapter.setSelectedKeyNoNotify("");
                            } else {
                                mSecondLevelAdapter.setSelectedKeyNoNotify(mSecondLevelKey);
                            }
                        }

                        mSecondLevelAdapter.init();
                        mSecondLevelAdapter.notifyDataSetChanged();
                    }
                    if (mCurrentChildData != null && mCurrentChildData.size() <= 0) {
                        ExpandTabView tag = (ExpandTabView) TwoLevelMenuView.this.getTag();
                        if (tag != null) {
                            tag.onPressBack();
                        }
                        if (mOnSelectListener != null) {
                            mOnSelectListener.onSelected(key, "", mFristLevelData.get(key));
                        }
                    }
                } else {
                    mSecondLevelListView.setVisibility(View.GONE);
                    if (mOnSelectListener != null) {
                        mOnSelectListener.onSelected(key, "", mFristLevelData.get(key));
                    }
                }
            }
        });
        mTempFirstLevelKey = mFirstLevelAdatper.getSelectedKey();
        mFirstLevelKey = mFirstLevelAdatper.getSelectedKey();
        mSecondLevelAdapter = new MultiLevelMenuAdapter(getContext(),
                mCurrentChildData, R.drawable.choose_second_level_item_selector,
                R.drawable.choose_second_level_item_selector, R.color.main_color);
        mSecondLevelAdapter.setTextSize(15);
        mSecondLevelAdapter.setSelectedKey(mSecondLevelKey);
        mSecondLevelListView.setAdapter(mSecondLevelAdapter);
        mSecondLevelAdapter.setOnItemClickListener(new MultiLevelMenuAdapter.OnItemClickListener()
        {

            @Override
            public void onItemClick(View view, String key)
            {
                if (!"1000".equals(key)) {
                    mSecondLevelKey = key;
                    ExpandTabView tag = (ExpandTabView) TwoLevelMenuView.this.getTag();
                    if (tag != null && !isMultiCheck) {
                        tag.onPressBack();
                    }
                    if (mFirstLevelKey != mTempFirstLevelKey) {
                        mFirstLevelKey = mTempFirstLevelKey;
                    }

                    if (isMultiCheck) {
                        multiCheckMap.put(mFirstLevelKey, mSecondLevelKey);
                    }

                    if (mOnSelectListener != null) {
                        if (!isMultiCheck) {
                            if (mSecondLevelKey.equals("0")) {
                                mOnSelectListener.onSelected(mFirstLevelKey, mSecondLevelKey, mFristLevelData.get(mFirstLevelKey));
                            } else {
                                mOnSelectListener.onSelected(mFirstLevelKey, mSecondLevelKey, mCurrentChildData.get(key));
                            }
                        } else {
                            mOnSelectListener.onSelectedMuilt(multiCheckMap);
                        }
                    }
                }
            }
        });
        mSecondLevelKey = mSecondLevelAdapter.getSelectedKey();

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

    public void setOnSelectListener(OnSelectListener onSelectListener)
    {
        this.mOnSelectListener = onSelectListener;
    }

    public interface OnSelectListener
    {
        public void onSelected(String firstLevelKey, String secondLevelKey,
                               String showString);

        public void onSelectedMuilt(HashMap<String, String> muiltMap);
    }

}
