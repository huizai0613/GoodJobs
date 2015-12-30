package cn.goodjobs.parttimejobs.activity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.parttimejobs.R;
import cn.goodjobs.parttimejobs.adapter.PartTimeJobAdapter;

public class PartTimeJobActivity extends BaseListActivity {

    private EmptyLayout emptyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mAdapter = new PartTimeJobAdapter(this);
        initList();
        mListView.setEmptyView(emptyLayout);
        startRefresh();
    }

    private void initView() {
        setTopTitle("兼职信息");
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_parttimejob;
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        HttpUtil.post(URLS.API_JOB_ParttimeJob, params, this);
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
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

}
