package cn.goodjobs.campusjobs.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.adapter.CareerTalkAdapter;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CareerTalkFragment extends BaseListFragment {

    private EmptyLayout emptyLayout;
    private boolean isSuccess = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_career, null);
        initView(view);
        mAdapter = new CareerTalkAdapter(getActivity());
        initList(view);
        return view;
    }

    private void initView(View view) {
        setTopTitle(view, "宣讲会");
        hideBackBtn(view);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("jt", "careertalk");
        HttpUtil.post(URLS.API_JOB_Jobfairlist, params, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            startRefresh();
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        isSuccess = true;
        JSONObject object = (JSONObject) data;
        mAdapter.appendToList(object.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }

        loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage") > page);
        mPtrFrameLayout.refreshComplete();
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
    }
}
