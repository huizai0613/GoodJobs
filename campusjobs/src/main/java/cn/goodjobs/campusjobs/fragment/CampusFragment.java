package cn.goodjobs.campusjobs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.activity.CampusSearchActivity;
import cn.goodjobs.campusjobs.adapter.CampusAdapter;
import cn.goodjobs.campusjobs.adapter.CmapusResultAdapter;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyListView;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CampusFragment extends BaseListFragment {

    private EmptyLayout emptyLayout;
    private ScrollView sv;
    private EditText etSearch;
    private MyListView lv_campus;
    private CampusAdapter adapter;
    private RelativeLayout tipLayout;
    private boolean isSuccess = false;

    public CampusFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_campus, null);
        initView(view);
        return view;
    }

    public void initView(View view) {
        setTopTitle(view, "校园招聘");
        changeLeftBg(view, R.mipmap.icon_campus);
        sv = (ScrollView) view.findViewById(R.id.sv_campus);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.error_layout);
        lv_campus = (MyListView) view.findViewById(R.id.lv_campus);
        adViewPager = (AutoScrollViewPager) view.findViewById(R.id.adViewPager);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        tipLayout = (RelativeLayout) view.findViewById(R.id.tipLayout);
        etSearch.setOnClickListener(this);
        tipLayout.setOnClickListener(this);
        adapter = new CampusAdapter(getActivity());
        lv_campus.setAdapter(adapter);

        Boolean tipChange = SharedPrefUtil.getBoolean("tipChange3");
        if (tipChange == null || tipChange) {
            tipLayout.setVisibility(View.VISIBLE);
        }

        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        HttpUtil.post(URLS.API_JOB_CampusIndex, this);

        emptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFronServer();
            }
        });
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        emptyLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
        HttpUtil.post(URLS.API_JOB_CampusIndex, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        sv.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        JSONObject object = (JSONObject) data;
        JSONArray array = object.optJSONArray("list");
        adapter.appendToList(array);
        initAd(object.optJSONArray("adsList"));
        isSuccess = true;
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        LoadingDialog.hide();
        if (emptyLayout != null)
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        LoadingDialog.hide();
        if (emptyLayout != null)
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.etSearch) {
            Intent intent = new Intent(getActivity(), CampusSearchActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tipLayout) {
            tipLayout.setVisibility(View.INVISIBLE);
            SharedPrefUtil.saveDataToLoacl("tipChange3", false);
        }
    }
}
