package cn.goodjobs.common.baseclass;

import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
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

public class BaseListFragment extends BaseFragment {

    protected PtrFrameLayout mPtrFrameLayout;
    protected LoadMoreListViewContainer loadMoreListViewContainer;
    protected int page = 1; // 分页索引
    protected JsonArrayAdapterBase mAdapter;
    protected ListView mListView;
    public Handler mTestHandler;
    protected EmptyLayout mEmptyLayout;

    protected void initList(View view, ListView listView) {
        // pull to refresh
        mPtrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.load_more_list_view_ptr_frame);
        loadMoreListViewContainer = (LoadMoreListViewContainer) view.findViewById(R.id.load_more_list_view_container);
        mListView = listView;
        if (mListView == null) {
            mListView = (ListView) view.findViewById(R.id.list_view);
        }
        mEmptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
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
                mEmptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                startRefresh();
            }
        });
        mListView.setAdapter(mAdapter);
        mTestHandler = new Handler();
    }

    protected void initList(View view) {
        initList(view, null);
    }

    protected void refresh() {
        LogUtil.info("开始下拉刷新...");
        page = 1;
        mAdapter.clear();
        getDataFronServer();
    }

    protected void loadMore() {
        LogUtil.info("开始加载下一页...");
        page ++;
        getDataFronServer();
    }

    /**
     * 获取网络数据
     * */
    protected void getDataFronServer() {

    }

    protected void startRefresh() {
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh(false);
            }
        }, 150);
    }

    protected Runnable testRun = new Runnable() {
        @Override
        public void run() {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;

            jsonObject = new JSONObject();
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();
            jsonArray.put(jsonObject);
            jsonObject = new JSONObject();
            jsonArray.put(jsonObject);

            mAdapter.appendToList(jsonArray);

            loadMoreListViewContainer.loadMoreFinish(false, true);
            mPtrFrameLayout.refreshComplete();
        }
    };
}
