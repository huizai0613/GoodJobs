package cn.goodjobs.bluecollar.activity.makefriend;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.MsgDetailAdapter;
import cn.goodjobs.bluecollar.view.listview.BaseXListViewActivity;
import cn.goodjobs.bluecollar.view.listview.XListView;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.KeyBoardUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;

public class MsgDetailActivity extends BaseXListViewActivity implements TextView.OnEditorActionListener {

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
    MsgDetailAdapter msgDetailAdapter;

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
        listView = (XListView) findViewById(R.id.listView);
        friendID = getIntent().getStringExtra("friendID");
        pageIndex = 0;
        msgDetailAdapter = new MsgDetailAdapter(this);
        listView.setAdapter(msgDetailAdapter);
        sendMap = new HashMap<String, JSONObject>();
        initXListView();
        listView.hideFootText();
        listView.setPullLoadEnable(true);
    }

    @Override
    protected void initWeightClick() {
        super.initWeightClick();
        btnSend.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
    }

    @Override
    protected void getDataFromServer() {
        super.getDataFromServer();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", pageIndex+1);
        params.put("type", type);
        params.put("friendID", friendID);
        params.put("pageTime", pageTime);

        HttpUtil.post(URLS.MAKEFRIEND_SMSSHOW, params, this);
    }

    @Override
    public void onRefresh() {
        pageTime = loadMoreTime;
        isRefush = false;
        type = 1;
        getDataFromServer();
    }

    @Override
    public void onLoadMore() {
        pageTime = refreshTime;
        type = 2;
        isRefresh = true;
        getDataFromServer();
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.endsWith(URLS.MAKEFRIEND_SMSSHOW)) {
            JSONObject jsonObject = (JSONObject) data;
            if (msgDetailAdapter.getCount() == 0) {
                msgDetailAdapter.myUserPhoto = jsonObject.optString("MyUserPhoto");
                msgDetailAdapter.friendUserPhoto = jsonObject.optString("FriendUserPhoto");
            }
            if (firstIn) {
                msgDetailAdapter.appendToList(jsonObject.optJSONArray("list"));
                listView.smoothScrollToPosition(msgDetailAdapter.getCount());
            } else {
                if (jsonObject.optJSONArray("list").length() > 0) {
                    if (isRefush) {
                        msgDetailAdapter.appendToList(jsonObject.optJSONArray("list"));
                        listView.smoothScrollToPosition(msgDetailAdapter.getCount());
                    } else {
                        msgDetailAdapter.appendToTopList(jsonObject.optJSONArray("list"));
                    }
                }
            }
            if (msgDetailAdapter.getCount() > 0) {
                if (firstIn) {
                    JSONObject jsonObject1 = (JSONObject) msgDetailAdapter.getItem(msgDetailAdapter.getCount()-1);
                    refreshTime = jsonObject1.optInt("sendTime");
                    pageIndex = jsonObject.optInt("page");
                    loadMoreTime = jsonObject.optInt("pageTime");
                } else {
                    if (isRefush) {
                        JSONObject jsonObject1 = (JSONObject) msgDetailAdapter.getItem(msgDetailAdapter.getCount()-1);
                        refreshTime = jsonObject1.optInt("sendTime");
                    } else {
                        if (jsonObject.optJSONArray("list").length() > 0) {
                            pageIndex++;
                        }
                        loadMoreTime = jsonObject.optInt("pageTime");
                    }
                }
                firstIn = false;
            }
            listView.stopRefresh();
            listView.stopLoadMore();
        } else {
            JSONObject jsonObject = (JSONObject) data;
            TipsUtil.show(this, jsonObject.optString("message"));
            msgDetailAdapter.removeObject(sendMap.get(tag));
            msgDetailAdapter.appendToList(jsonObject.optJSONArray("list"));
            JSONObject jsonObject1 = (JSONObject) msgDetailAdapter.getItem(msgDetailAdapter.getCount()-1);
            refreshTime = jsonObject1.optInt("sendTime");
            msgDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        if (tag.endsWith(URLS.MAKEFRIEND_SMSSHOW)) {

        } else {
            JSONObject jsonObject = sendMap.get(tag);
            try {
                jsonObject.put("status", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            msgDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        if (tag.endsWith(URLS.MAKEFRIEND_SMSSHOW)) {

        } else {
            JSONObject jsonObject = sendMap.get(tag);
            try {
                jsonObject.put("status", 2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            msgDetailAdapter.notifyDataSetChanged();
        }
    }

    private void sendMsg() {
        String content = editText.getText().toString();
        editText.setText("");
        if (StringUtil.isEmpty(content)) {
            TipsUtil.show(this, "请输入内容");
            return;
        }
        KeyBoardUtil.hide(this); // 隐藏键盘
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
        msgDetailAdapter.append(jsonObject);
        listView.smoothScrollToPosition(msgDetailAdapter.getCount());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = (JSONObject) msgDetailAdapter.getItem(position-1);
        if (2 == jsonObject.optInt("status")) {
            // 重新上传
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("friendID", friendID);
            params.put("content", jsonObject.optString("content"));
            params.put("pageTime", refreshTime);
            String tag = jsonObject.optString("tag");
            try {
                jsonObject.put("status", 1);
                msgDetailAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            HttpUtil.post(URLS.MAKEFRIEND_SMSSEND, tag, params, this);
        }
    }
}
