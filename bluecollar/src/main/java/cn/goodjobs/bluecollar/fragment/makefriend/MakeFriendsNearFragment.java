package cn.goodjobs.bluecollar.fragment.makefriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.makefriend.AddTrendActivity;
import cn.goodjobs.bluecollar.activity.makefriend.TrendDetailActivity;
import cn.goodjobs.bluecollar.adapter.TrendAdapter;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * 附近的人
 */
public class MakeFriendsNearFragment extends BaseListFragment implements AdapterView.OnItemClickListener {
    View view;
    Button btnAdd;
    String pageTime = "0";
    MyLocation myLocation;
    boolean hasMore; // 是否包含下一页
    public static boolean needRefresh; // 是否需要刷新

    public MakeFriendsNearFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.makefriend_near, container, false);
        mAdapter = new TrendAdapter(getActivity());
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        initList(view);
        mListView.setOnItemClickListener(this);
        startRefresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (needRefresh) {
            startRefresh();
        }
        needRefresh = false;
    }

    @Override
    protected void refresh() {
        pageTime = "0";
        super.refresh();
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("serType", "near");
        params.put("pageTime", pageTime);
        if (myLocation == null) {
            myLocation = (MyLocation) SharedPrefUtil.getObject("location");
        }
        if (myLocation != null) {
            params.put("lng", myLocation.longitude);
            params.put("lat", myLocation.latitude);
        }
        HttpUtil.post(URLS.MAKEFRIEND_TRENDLIST, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject jsonObject = (JSONObject) data;
        mAdapter.appendToList(jsonObject.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            mEmptyLayout.setErrorType(EmptyLayout.NODATA);
        }
        pageTime = jsonObject.optString("pageTime");
        hasMore = jsonObject.optInt("maxPage")>page;
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        if (mAdapter.getCount() == 0) {
            mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        if (mAdapter.getCount() == 0) {
            mEmptyLayout.setErrorType(EmptyLayout.NODATA);
            mEmptyLayout.setErrorMessage(errorMessage);
        }
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.empty_view) {
            LoadingDialog.showDialog(getActivity());
            getDataFronServer();
        } else if (v.getId() == R.id.btnAdd) {
            Intent intent = new Intent(getActivity(), AddTrendActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), TrendDetailActivity.class);
        intent.putExtra("dynamicID", jsonObject.optString("dynamicID"));
        startActivity(intent);
    }
}
