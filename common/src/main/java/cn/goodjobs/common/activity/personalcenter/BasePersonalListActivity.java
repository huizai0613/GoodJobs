package cn.goodjobs.common.activity.personalcenter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.PersonalListAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by 王刚 on 2016/1/5 0005.
 * 个人中心列表管理界面
 */
public class BasePersonalListActivity extends BaseListActivity {

    EmptyLayout emptyLayout;
    boolean hasMore; // 是否包含下一页
    protected String url;
    protected String delUrl;
    protected int adapterRes;
    protected int[] resIDs;
    protected String[] keys;
    protected String idKey; // id对应的key
    protected String paramKey;
    protected HashMap<String, String> strMap; // 字段转义
    protected PersonalListAdapter.TextStatus textStatus; // 字段状态选择

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        mAdapter = new PersonalListAdapter(this);
        ((PersonalListAdapter) mAdapter).adapterRes = adapterRes;
        ((PersonalListAdapter) mAdapter).resIDs = resIDs;
        ((PersonalListAdapter) mAdapter).keys = keys;
        ((PersonalListAdapter) mAdapter).idKey = idKey;
        if (strMap != null) {
            ((PersonalListAdapter) mAdapter).setStrMap(strMap);
        }
        if (textStatus != null) {
            ((PersonalListAdapter) mAdapter).textStatus = textStatus;
        }
        if (StringUtil.isEmpty(delUrl)) {
            // 不包含删除
            ((PersonalListAdapter) mAdapter).hasCheck = false;
        } else {
            ImageButton btnRight = (ImageButton) findViewById(R.id.btn_right);
            btnRight.setImageResource(R.drawable.icon_delete);
            btnRight.setVisibility(View.VISIBLE);
            btnRight.setOnClickListener(this);
        }
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
        if (tag.equals(url)) {
            super.onSuccess(tag, data);
            JSONObject object = (JSONObject) data;
            mAdapter.appendToList(object.optJSONArray("list"));
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NODATA);
            }
            hasMore = object.optInt("maxPage") > page;
            loadMoreListViewContainer.loadMoreFinish(false, hasMore);
            mPtrFrameLayout.refreshComplete();
        } else if (tag.equals(delUrl)) {
            if (data instanceof String) {
                TipsUtil.show(this, data + "");
            } else if (data instanceof JSONObject) {
                TipsUtil.show(this, ((JSONObject) data).optString("message"));
            }
            mAdapter.clear();
            page = 1;
            startRefresh();
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_right) {
            String ids = ((PersonalListAdapter) mAdapter).getSelectedIDs();
            if (StringUtil.isEmpty(ids)) {
                TipsUtil.show(this, "请至少选择一项删除");
                return;
            }
            AlertDialogUtil.show(this, R.string.app_name, "删除后无法恢复，您确定删除吗？", true, "确定", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put(paramKey, ((PersonalListAdapter) mAdapter).getSelectedIDs());
                    LoadingDialog.showDialog(BasePersonalListActivity.this);
                    HttpUtil.post(delUrl, params, BasePersonalListActivity.this);
                }
            }, null);

        }
    }
}
