package cn.goodjobs.common.baseclass;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;

/**
 * Created by 王刚 on 2015/12/22 0022.
 * 包含下拉刷新列表基类
 */

public class BaseListActivity extends BaseActivity {

    protected PtrFrameLayout mPtrFrameLayout;
    protected EmptyLayout mEmptyLayout;
    protected LoadMoreListViewContainer loadMoreListViewContainer;
    protected int page = 1; // 分页索引
    protected JsonArrayAdapterBase mAdapter;
    protected ListView mListView;
    protected boolean isRefresh; //  是否是刷新操作

    protected void initList(ListView listView) {
        // pull to refresh
        mPtrFrameLayout = (PtrFrameLayout) findViewById(R.id.load_more_list_view_ptr_frame);
        loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
        mEmptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        mListView = listView;
        if (mListView == null) {
            mListView = (ListView) findViewById(R.id.list_view);
        }
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                // here check list view, not content.
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });

        // load more container

        loadMoreListViewContainer.useDefaultHeader();

        loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
            @Override
            public void onLoadMore(LoadMoreContainer loadMoreContainer) {
                loadMore();
            }
        });

        mEmptyLayout.setOnLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRefresh();
            }
        });

        mListView.setAdapter(mAdapter);
    }

    protected void initList() {
        initList(null);
    }

    protected void refresh() {
        LogUtil.info("开始下拉刷新...");
        page = 1;
        isRefresh = true;
        getDataFronServer();
    }

    protected void loadMore() {
        LogUtil.info("开始加载下一页...");
        page++;
        getDataFronServer();
    }

    /**
     * 获取网络数据
     */
    protected void getDataFronServer() {

    }

    protected void startRefresh() {
        if (mEmptyLayout != null) {
            mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 250);
    }

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {

    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);

        if (isRefresh) {
            if (mAdapter != null) {
                mAdapter.clear();
            }
            isRefresh = false;
        }
        JSONObject jsonObject = (JSONObject) data;

        int maxPage = Integer.parseInt(jsonObject.optString("maxPage"));
        int count = Integer.parseInt(jsonObject.optString("count"));
        if (count > 0) {
            if (page >= maxPage) {
                loadMoreListViewContainer.loadMoreFinish(false, false);
            } else {
                loadMoreListViewContainer.loadMoreFinish(false, true);
            }
        } else {
            mEmptyLayout.setErrorType(EmptyLayout.NODATA);
            loadMoreListViewContainer.loadMoreFinish(true, false);
        }
        mPtrFrameLayout.refreshComplete();

    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        mPtrFrameLayout.refreshComplete();
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        mPtrFrameLayout.refreshComplete();
        mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
        super.onFailure(statusCode, tag);
    }
}
