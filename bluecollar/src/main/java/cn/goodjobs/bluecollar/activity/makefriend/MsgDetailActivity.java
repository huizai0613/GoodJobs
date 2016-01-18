package cn.goodjobs.bluecollar.activity.makefriend;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.MsgDetailAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;

public class MsgDetailActivity extends BaseListActivity implements TextView.OnEditorActionListener {

    int type = 1; // 类型(1历史记录 2最新记录)
    String friendID;
    int pageTime;
    int refreshTime; // 加载最新的时间
    int loadMoreTime; // 加载更多的时间
    boolean isRefush = true;
    boolean firstIn = true;

    TextView btnSend;
    EditText editText;
    int sendIndex;
    HashMap<String, JSONObject> sendMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void initWeight() {
        super.initWeight();
        setTopTitle(getIntent().getStringExtra("nickName"));
        btnSend = (TextView) findViewById(R.id.btnSend);
        editText = (EditText) findViewById(R.id.editText);
        friendID = getIntent().getStringExtra("friendID");
        mAdapter = new MsgDetailAdapter(this);
        sendMap = new HashMap<String, JSONObject>();
        page = 0;
        initList();
        startRefresh();
    }

    @Override
    protected void initWeightClick() {
        super.initWeightClick();
        btnSend.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page+1);
        params.put("type", type);
        params.put("friendID", friendID);
        params.put("pageTime", pageTime);

        HttpUtil.post(URLS.MAKEFRIEND_SMSSHOW, params, this);
    }

    protected void refresh() {
        pageTime = loadMoreTime;
        isRefush = false;
        type = 1;
        getDataFronServer();
    }

    protected void loadMore() {
        pageTime = refreshTime;
        isRefush = true;
        type = 2;
        getDataFronServer();
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.endsWith(URLS.MAKEFRIEND_SMSSHOW)) {
            JSONObject jsonObject = (JSONObject) data;
            if (mAdapter.getCount() == 0) {
                ((MsgDetailAdapter) mAdapter).myUserPhoto = jsonObject.optString("MyUserPhoto");
                ((MsgDetailAdapter) mAdapter).friendUserPhoto = jsonObject.optString("FriendUserPhoto");
            }
            if (firstIn) {
                mAdapter.appendToList(jsonObject.optJSONArray("list"));
            } else {
                if (isRefush) {
                    mAdapter.appendToList(jsonObject.optJSONArray("list"));
                } else {
                    mAdapter.appendToTopList(jsonObject.optJSONArray("list"));
                }
            }
            if (mAdapter.getCount() > 0) {
                if (firstIn) {
                    JSONObject jsonObject1 = (JSONObject) mAdapter.getItem(mAdapter.getCount()-1);
                    refreshTime = jsonObject1.optInt("sendTime");
                    page = jsonObject.optInt("page");
                    loadMoreTime = jsonObject.optInt("pageTime");
                } else {
                    if (isRefush) {
                        JSONObject jsonObject1 = (JSONObject) mAdapter.getItem(mAdapter.getCount()-1);
                        refreshTime = jsonObject1.optInt("sendTime");
                    } else {
                        if (jsonObject.optJSONArray("list").length() > 0) {
                            page = jsonObject.optInt("page");
                        } else {
                            page--;
                        }
                        loadMoreTime = jsonObject.optInt("pageTime");
                    }
                }
                firstIn = false;
            } else {
                mEmptyLayout.setErrorType(EmptyLayout.NODATA);
            }
            loadMoreListViewContainer.loadMoreFinish(false, true);
            mPtrFrameLayout.refreshComplete();
        } else {
            JSONObject jsonObject = (JSONObject) data;
            TipsUtil.show(this, jsonObject.optString("message"));
            mAdapter.removeObject(sendMap.get(tag));
            mAdapter.appendToList(jsonObject.optJSONArray("list"));
            editText.setText("");
            JSONObject jsonObject1 = (JSONObject) mAdapter.getItem(mAdapter.getCount()-1);
            refreshTime = jsonObject1.optInt("sendTime");
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        if (tag.endsWith(URLS.MAKEFRIEND_SMSSHOW)) {
            if (mAdapter.getCount() == 0) {
                mEmptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            }
            loadMoreListViewContainer.loadMoreFinish(false, true);
            mPtrFrameLayout.refreshComplete();
        } else {
            JSONObject jsonObject = sendMap.get(tag);
            try {
                jsonObject.put("status", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        if (tag.endsWith(URLS.MAKEFRIEND_SMSSHOW)) {
            if (mAdapter.getCount() == 0) {
                mEmptyLayout.setErrorType(EmptyLayout.NODATA);
                mEmptyLayout.setErrorMessage(errorMessage);
            }
            loadMoreListViewContainer.loadMoreFinish(false, true);
            mPtrFrameLayout.refreshComplete();
        } else {
            JSONObject jsonObject = sendMap.get(tag);
            try {
                jsonObject.put("status", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void sendMsg() {
        String content = editText.getText().toString();
        if (StringUtil.isEmpty(content)) {
            TipsUtil.show(this, "请输入内容");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("friendID", friendID);
        params.put("content", content);
        params.put("pageTime", refreshTime);
        String tag = "sendmsg"+sendIndex++;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("tag", tag); //标志本条是临时数据
            jsonObject.put("myHas", "1");
            jsonObject.put("content", content);
            jsonObject.put("status", 1); // 1正在发送  2发送失败
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMap.put(tag, jsonObject);
        mAdapter.append(jsonObject);
        mListView.smoothScrollToPosition(mAdapter.getCount() - 1);
        HttpUtil.post(URLS.MAKEFRIEND_SMSSEND, tag, params, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSend) {
            sendMsg();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendMsg();
        }
        return false;
    }
}
