package cn.goodjobs.bluecollar.fragment.makefriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
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
public class MakeFriendsNearFragment extends BaseListFragment {
    View view;
    String pageTime = "0";
    MyLocation myLocation;
    EmptyLayout emptyLayout;
    boolean hasMore; // 是否包含下一页

    public MakeFriendsNearFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.load_more_list_view, container, false);
        mAdapter = new TrendAdapter(getActivity());
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        initList(view);
        startRefresh();
        return view;
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
            emptyLayout.setErrorType(EmptyLayout.NODATA);
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
            emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        }
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
            emptyLayout.setErrorMessage(errorMessage);
        }
        loadMoreListViewContainer.loadMoreFinish(false, hasMore);
        mPtrFrameLayout.refreshComplete();
    }
}
