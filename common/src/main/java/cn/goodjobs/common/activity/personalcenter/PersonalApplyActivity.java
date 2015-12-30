package cn.goodjobs.common.activity.personalcenter;

import android.os.Bundle;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.PersonalApplyAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * 职位申请和职位收藏
 * */

public class PersonalApplyActivity extends BaseListActivity {

    private EmptyLayout emptyLayout;
    boolean hasMore; // 是否包含下一页

    String title;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_personal_list;
    }

    @Override
    protected void initWeight() {
        super.initWeight();
        setTopTitle(getIntent().getStringExtra("title"));
        url = getIntent().getStringExtra("url");
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        mAdapter = new PersonalApplyAdapter(this);
        initList();
        startRefresh();
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        HttpUtil.post(url, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(url)) {
            JSONObject object = (JSONObject) data;
            mAdapter.appendToList(object.optJSONArray("list"));
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NODATA);
            }
            hasMore = object.optInt("maxPage")>page;
            loadMoreListViewContainer.loadMoreFinish(false, hasMore);
            mPtrFrameLayout.refreshComplete();
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        if (tag.equals(url)) {
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
            loadMoreListViewContainer.loadMoreFinish(false, hasMore);
            mPtrFrameLayout.refreshComplete();
        }
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        if (tag.equals(url)) {
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                emptyLayout.setErrorMessage(errorMessage);
            }
            loadMoreListViewContainer.loadMoreFinish(false, hasMore);
            mPtrFrameLayout.refreshComplete();
        }
    }
}
