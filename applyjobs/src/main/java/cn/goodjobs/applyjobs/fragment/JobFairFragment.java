package cn.goodjobs.applyjobs.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.adapter.DividerItemDecoration;
import cn.goodjobs.applyjobs.adapter.JobFairRecyclerAdapter;
import cn.goodjobs.applyjobs.bean.JobFairChild;
import cn.goodjobs.applyjobs.bean.JobfairGroup;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyRecycler.MyRecyclerView;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by zhuli on 2015/12/16.
 */
public class JobFairFragment extends BaseFragment implements View.OnClickListener {

    private ListView lv;
    private EmptyLayout layout;
    private List<JobfairGroup> group_list;
    private List<JobFairChild> child_list;
    private JobFairRecyclerAdapter adapter;
    private boolean isSuccess = false;  //判断是否已经加载成功了，如果成功了，则不需要再次加载
    private int type;                 //0是招聘会，1是职场资讯；


    public JobFairFragment() {
    }

    public JobFairFragment(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_fair, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        group_list = new ArrayList<JobfairGroup>();
        child_list = new ArrayList<JobFairChild>();
        lv = (ListView) view.findViewById(R.id.lv_fair);
        layout = (EmptyLayout) view.findViewById(R.id.empty_view);
        adapter = new JobFairRecyclerAdapter(getActivity(), group_list, child_list,type);
        lv.setAdapter(adapter);
        lv.setEmptyView(layout);
    }

    // 当fragment可见时调用,加载数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("HomeFragment--------setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        Map<String, Object> params = new HashMap<String, Object>();
        if (type == 0) {
            params.put("type", "jobfair");
            params.put("perpage", "");
        } else if (type == 1) {
            params.put("type", "cvjob");
        }
        if (isVisibleToUser) {
            if (!isSuccess) {
                HttpUtil.post(URLS.API_JOB_Newindex, params, this);
                LoadingDialog.showDialog(getActivity());
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONArray array = (JSONArray) data;
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.optJSONObject(i);
            JobfairGroup group = new JobfairGroup();
            String name = jsonObject.optString("name");
            String catalogID = jsonObject.optString("catalogID");
            group.setName(name);
            group.setCatalogID(catalogID);
            JSONArray jsonArray = null;
            try {
                jsonArray = jsonObject.getJSONArray("list");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            List<JobFairChild> childlist = new ArrayList<JobFairChild>();
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object2 = jsonArray.optJSONObject(j);
                JobFairChild child = new JobFairChild();
                String newsID = object2.optString("newsID");
                String newstitle = object2.optString("newstitle");
                String newsdate = object2.optString("newsdate");
                String typeName = object2.optString("typeName");
                child.setNewsID(newsID);
                child.setNewsdate(newsdate);
                child.setNewstitle(newstitle);
                child.setTypeName(typeName);
                childlist.add(child);
                child_list.add(child);
                group.setList(childlist);
            }
            group_list.add(group);
        }
        if(group_list.size()==0) {
            layout.setErrorType(EmptyLayout.NODATA);
        }
        adapter.notifyDataSetChanged();
        isSuccess = true;
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        LoadingDialog.hide();
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        LoadingDialog.hide();
    }

    @Override
    public void onClick(View v) {

    }

}
