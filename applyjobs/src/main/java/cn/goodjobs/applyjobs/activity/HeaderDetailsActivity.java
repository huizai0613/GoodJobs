package cn.goodjobs.applyjobs.activity;

/**
 * Created by zhuli on 2015/12/21.
 */


import android.content.Intent;
import android.os.Bundle;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.adapter.HeaderDetailsAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;


public class HeaderDetailsActivity extends BaseListActivity {

    private EmptyLayout emptyLayout;
    private String type;
    private int catalogID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        mAdapter = new HeaderDetailsAdapter(this);
        initList();
        startRefresh();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_headerdetails;
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        if (type.equals("招聘会")) {
            params.put("type", "jobfair");
            params.put("catalogID", catalogID);
            params.put("page", page);

        } else if (type.equals("职场资讯")) {
            params.put("type", "cvjob");
            params.put("catalogID", catalogID);
            params.put("page", page);
        }
        HttpUtil.post(URLS.API_JOB_FairList, params, this);
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        catalogID = intent.getIntExtra("catalogID", 0);
        setTopTitle(type);
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
    }


    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        try {
            mAdapter.appendToList(object.getJSONArray("list"));
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NODATA);
            }

            loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage") > page);
            mPtrFrameLayout.refreshComplete();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }
}

