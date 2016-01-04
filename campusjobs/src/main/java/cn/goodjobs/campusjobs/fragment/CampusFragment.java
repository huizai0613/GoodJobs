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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.activity.CampusSearchActivity;
import cn.goodjobs.campusjobs.adapter.CampusAdapter;
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
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CampusFragment extends BaseListFragment {

    private EditText etSearch;
    private MyListView lv_campus;
    private CampusAdapter adapter;
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
        lv_campus = (MyListView) view.findViewById(R.id.lv_campus);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        etSearch.setOnClickListener(this);
        adapter = new CampusAdapter(getActivity());
        lv_campus.setAdapter(adapter);
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isSuccess) {
                LoadingDialog.showDialog(getActivity());
                HttpUtil.post(URLS.API_JOB_CampusIndex, null, this);
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        JSONArray array = object.optJSONArray("list");
        adapter.appendToList(array);
        isSuccess = true;
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.etSearch) {
            Intent intent = new Intent(getActivity(), CampusSearchActivity.class);
            startActivity(intent);
        }
    }
}
