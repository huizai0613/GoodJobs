package cn.goodjobs.bluecollar.fragment.makefriend;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.makefriend.OtherPersonalInfoActivity;
import cn.goodjobs.bluecollar.adapter.FriendsAdapter;
import cn.goodjobs.bluecollar.adapter.LookAdapter;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * 同城
 */
public class MakeFriendsGuanzhuFragment extends BaseListFragment implements AdapterView.OnItemClickListener {
    View view;
    private EmptyLayout emptyLayout;
    public ViewPager viewPager;
    public int preItem = 1;
    boolean hasMore; // 是否包含下一页
    public boolean firstLoad; // 是否直接加载数据

    public MakeFriendsGuanzhuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.load_more_list_view, container, false);
        if (firstLoad) {
            if(!isLoad) {
                loadView();
                isLoad=true;
            }
        }
        return view;
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("MakeFriendsGuanzhuFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if (!GoodJobsApp.getInstance().isLogin()) {
                AlertDialogUtil.show(getActivity(), R.string.app_name, "您尚未登录，是否去登录？", true, "去登录", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewPager.setCurrentItem(preItem);
                    }
                });
                return;
            }
            if(!isLoad) {
                loadView();
                isLoad=true;
            }
        }
    }

    private void loadView() {
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        mAdapter = new LookAdapter(getActivity());
        ((LookAdapter) mAdapter).lookListener = new LookAdapter.LookListener() {
            @Override
            public void remove() {
                startRefresh();
            }
        };
        initList(view);
        mListView.setOnItemClickListener(this);
        startRefresh();
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        HttpUtil.post(URLS.MAKEFRIEND_LOOKLIST, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject jsonObject = (JSONObject) data;
        mAdapter.appendToList(jsonObject.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(!isLoad) {
                loadView();
                isLoad=true;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) mAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), OtherPersonalInfoActivity.class);
        intent.putExtra("friendID", jsonObject.optString("friendID"));
        intent.putExtra("nickName", jsonObject.optString("nickName"));
        startActivity(intent);
    }
}
