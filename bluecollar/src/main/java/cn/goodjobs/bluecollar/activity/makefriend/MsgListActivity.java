package cn.goodjobs.bluecollar.activity.makefriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.MsgListAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * 私信列表
 * */

public class MsgListActivity extends BaseListActivity implements AdapterView.OnItemClickListener {
    int pageTime;
    boolean hasMore; // 是否包含下一页
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_msg_list;
    }

    @Override
    protected void initWeight() {
        super.initWeight();
        setTopTitle("聊天记录");
        mAdapter = new MsgListAdapter(this);
        initList();
        mListView.setOnItemClickListener(this);
        startRefresh();
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("pageTime", pageTime);
        HttpUtil.post(URLS.MAKEFRIEND_SMSLIST, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject jsonObject = (JSONObject) data;
        mAdapter.appendToList(jsonObject.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            mEmptyLayout.setErrorType(EmptyLayout.NODATA);
        }
        hasMore = jsonObject.optInt("maxPage")>page;
        pageTime = jsonObject.optInt("pageTime");
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent(this, MsgDetailActivity.class);
        intent.putExtra("nickName", jsonObject.optString("nickName"));
        intent.putExtra("friendID", jsonObject.optString("friendID"));
        startActivity(intent);
    }
}
