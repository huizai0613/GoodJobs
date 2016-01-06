package cn.goodjobs.applyjobs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.util.UpdateDataTaskUtils;

/**
 * Created by yexiangyu on 16/1/6.
 */
public class JobCateFragment extends BaseFragment implements UpdateDataTaskUtils.OnGetDiscussJobFunListener
{

    private View jobsearchBut;
    private ScrollView toolsScrlllview;
    private LinearLayout tools;
    private ViewPager jobsPager;
    private TextView[] toolsTextViews;
    private View[] views;
    private Map<JSONObject, List<JSONObject>> JobFunData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View inflate = inflater.inflate(R.layout.activity_jobcate, null);
        initView(inflate);
        initData();
        return inflate;
    }

    private void initData()
    {
        UpdateDataTaskUtils.selectJobFun(getActivity(), this);

    }


    private void initView(View view)
    {
        setTopTitle(view, "找工作");
        hideBackBtn(view);
        jobsearchBut = view.findViewById(R.id.jobsearch_but);
        toolsScrlllview = (ScrollView) view.findViewById(R.id.tools_scrlllview);
        tools = (LinearLayout) view.findViewById(R.id.tools);
        jobsPager = (ViewPager) view.findViewById(R.id.jobs_pager);
    }


    /**
     * 动态生成显示items中的textview
     */
    private void showToolsView()
    {
        toolsTextViews = new TextView[JobFunData.size()];
        views = new View[JobFunData.size()];
        Set<Map.Entry<JSONObject, List<JSONObject>>> entries = JobFunData.entrySet();
        int index = 0;
        for (Map.Entry<JSONObject, List<JSONObject>> e : entries) {
            View view = View.inflate(getActivity(), R.layout.item_b_top_nav_layout, null);
            view.setId(index);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(e.getKey().optString("name"));
            tools.addView(view);
            toolsTextViews[index] = textView;
            views[index] = view;
            index++;
        }
        changeTextColor(0);
    }



    /**
     * 改变textView的颜色
     * @param id
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if(i!=id){
                toolsTextViews[i].setBackgroundResource(android.R.color.transparent);
                toolsTextViews[i].setTextColor(0xff000000);
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xffff5d5e);
    }

    @Override
    public void onGetDiscussSalaryInfo(Map<JSONObject, List<JSONObject>> JobFunData)
    {
        this.JobFunData = JobFunData;
        showToolsView();

    }
}
