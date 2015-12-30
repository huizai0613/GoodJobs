package cn.goodjobs.common.activity.personalcenter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.PersonalLookAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * 企业查看记录
 * */

public class PersonalInboxActivity extends BaseListActivity {

    private EmptyLayout emptyLayout;
    boolean hasMore; // 是否包含下一页

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
        setTopTitle("企业来信");
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        mAdapter = new PersonalLookAdapter(this);
        initList();
        startRefresh();
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        HttpUtil.post(URLS.API_USER_INBOX, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_USER_INBOX)) {
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
        if (tag.equals(URLS.API_USER_INBOX)) {
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
        if (tag.equals(URLS.API_USER_INBOX)) {
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NODATA);
                emptyLayout.setErrorMessage(errorMessage);
            }
            loadMoreListViewContainer.loadMoreFinish(false, hasMore);
            mPtrFrameLayout.refreshComplete();
        }
    }
}
