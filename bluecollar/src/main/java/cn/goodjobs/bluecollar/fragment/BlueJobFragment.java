package cn.goodjobs.bluecollar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueSearchActivity;
import cn.goodjobs.bluecollar.fragment.BlueJob.Fragment_pro_type;
import cn.goodjobs.bluecollar.fragment.BlueJob.Type;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.UpdateDataTaskUtils;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

/**
 * Created by yexiangyu on 16/1/6.
 */
public class BlueJobFragment extends BaseFragment implements UpdateDataTaskUtils.OnGetDiscussJobFunListener
{
    private final static String LASTSELECT = "lastSelect";
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    private int currentItem = 0;
    private View jobsearchBut;
    private ScrollView toolsScrlllview;
    private LinearLayout tools;
    private ViewPager jobsPager;
    private TextView[] toolsTextViews;
    private View[] toolsViews;
    private View[] views;
    private Map<JSONObject, List<JSONObject>> JobFunData;
    private ArrayList<JSONObject> cates;
    private ArrayList<List<JSONObject>> cates2Child;
    private ShopAdapter shopAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View inflate = inflater.inflate(R.layout.activity_jobcate, null);
        initView(inflate);
        return inflate;
    }


    private void initView(View view)
    {
        setTopTitle(view, "找工作");
        hideBackBtn(view);
        jobsearchBut = view.findViewById(R.id.jobsearch_but);
        toolsScrlllview = (ScrollView) view.findViewById(R.id.tools_scrlllview);
        tools = (LinearLayout) view.findViewById(R.id.tools);
        jobsPager = (ViewPager) view.findViewById(R.id.jobs_pager);
        jobsearchBut.setOnClickListener(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            UpdateDataTaskUtils.selectJobFun(getActivity(), this);
        }
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        int id = v.getId();
        if (id == R.id.jobsearch_but) {
            JumpViewUtil.openActivityAndParam(getContext(), BlueSearchActivity.class, new HashMap<String, Object>());
        }
    }


    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView()
    {
        toolsTextViews = new TextView[JobFunData.size()];
        toolsViews = new View[JobFunData.size()];
        views = new View[JobFunData.size()];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getContext(), 41));

        for (int i = 0; i < cates.size(); i++) {
            View view = View.inflate(getActivity(), R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(cates.get(i).optString("name"));
            tools.addView(view, layoutParams);
            toolsTextViews[i] = textView;
            toolsViews[i] = view;
            views[i] = view;
        }

        String dataFromLoacl = SharedPrefUtil.getDataFromLoacl(getContext(), LASTSELECT);
        if (!StringUtil.isEmpty(dataFromLoacl)) {
            toolsViews[Integer.parseInt(dataFromLoacl)].performClick();
        } else {
            changeTextColor(0);
        }

    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            changeTextColor(v.getId());
            changeTextLocation(v.getId());
            SharedPrefUtil.saveDataToLoacl(LASTSELECT, v.getId());
            jobsPager.setCurrentItem(v.getId());
        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager()
    {
        jobsPager.setAdapter(shopAdapter);
        jobsPager.setOnPageChangeListener(onPageChangeListener);
    }


    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
    {
        @Override
        public void onPageSelected(int arg0)
        {
            if (jobsPager.getCurrentItem() != arg0) jobsPager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
        }
    };


    /**
     * 改变栏目位置
     *
     * @param clickPosition
     */
    private void changeTextLocation(int clickPosition)
    {

        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        toolsScrlllview.smoothScrollTo(0, x);
    }

    /**
     * ViewPager 加载选项卡
     *
     * @author Administrator
     */
    private class ShopAdapter extends FragmentPagerAdapter
    {
        public ShopAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0)
        {
            Fragment_pro_type fragment = new Fragment_pro_type();
            ArrayList<Type> types = new ArrayList<>();
            JSONObject jsonObject = cates.get(arg0);
            for (int i = 0; i < cates2Child.get(arg0).size(); i++) {
                Type t = new Type();
                t.setId(cates2Child.get(arg0).get(i).optInt("id"));
                t.setTypename(cates2Child.get(arg0).get(i).optString("name"));
                t.setPranetId(jsonObject.optInt("id"));
                types.add(t);
            }
            fragment.setTypeList(getContext(), types);

            return fragment;
        }

        @Override
        public int getCount()
        {
            return cates.size();
        }
    }


    /**
     * 改变textView的颜色
     *
     * @param id
     */
    private void changeTextColor(int id)
    {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if (i != id) {
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(Color.parseColor("#454545"));
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(Color.parseColor("#3492e9"));
    }

    @Override
    public void onGetDiscussJobFunInfo(Map<JSONObject, List<JSONObject>> JobFunData)
    {
        this.JobFunData = JobFunData;
        Set<Map.Entry<JSONObject, List<JSONObject>>> entries = JobFunData.entrySet();
        cates = new ArrayList<>();
        cates2Child = new ArrayList<>();

        for (Map.Entry<JSONObject, List<JSONObject>> entry : entries) {
            cates.add(entry.getKey());
            cates2Child.add(entry.getValue());
        }

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                shopAdapter = new ShopAdapter(getActivity().getSupportFragmentManager());
                initPager();
                showToolsView();
            }
        });


    }

    /**
     * 返回scrollview的中间位置
     *
     * @return
     */
    private int getScrollViewMiddle()
    {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     *
     * @return
     */
    private int getScrollViewheight()
    {
        if (scrllViewWidth == 0)
            scrllViewWidth = toolsScrlllview.getBottom() - toolsScrlllview.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     *
     * @param view
     * @return
     */
    private int getViewheight(View view)
    {
        return view.getBottom() - view.getTop();
    }
}
