package cn.goodjobs.campusjobs.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.adapter.CareerTalkAdapter;
import cn.goodjobs.campusjobs.adapter.SJobFairAdapter;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.ExpandTabSuper.ExpandTabView;
import cn.goodjobs.common.view.ExpandTabSuper.SingleLevelMenuView;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;

/**
 * Created by zhuli on 2015/12/29.
 */
public class SJobFairFragment extends BaseListFragment {

    private EmptyLayout emptyLayout;
    private boolean isSuccess = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sjobfair, null);
        initView(view);
        mAdapter = new SJobFairAdapter(getActivity());
        initList(view);
        return view;
    }

    private void initView(View view) {
        setTopTitle(view, "招聘会");
        hideBackBtn(view);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("jt", "jobfair");
        HttpUtil.post(URLS.API_JOB_Jobfairlist, params, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (!isSuccess) {
                startRefresh();
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        mAdapter.appendToList(object.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }

        loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage") > page);
        mPtrFrameLayout.refreshComplete();
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
    }
}
