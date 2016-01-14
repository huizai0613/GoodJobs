package cn.goodjobs.bluecollar.activity.makefriend;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.TrendCommentAdapter;
import cn.goodjobs.bluecollar.view.TrendItemView;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.KeyBoardUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyListView;

public class TrendDetailActivity extends BaseActivity implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    SimpleDraweeView headPhoto;
    TextView tvName, tvDistance, tvAge, btnLook, btnMsg;
    TrendItemView viewTrend;
    LinearLayout trendLayout;
    ProgressBar progressBar;
    MyListView myListView;
    int pageTime;
    int page = 1;
    View hLine;
    TrendCommentAdapter trendCommentAdapter;
    TextView btnSend;
    EditText editText;
    JSONObject replyObject;
    int myHas = 0;
    String friendID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_trend_detail;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("查看动态");
        headPhoto = (SimpleDraweeView) findViewById(R.id.headPhoto);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        btnLook = (TextView) findViewById(R.id.btnLook);
        btnMsg = (TextView) findViewById(R.id.btnMsg);
        tvAge = (TextView) findViewById(R.id.tvAge);
        viewTrend = (TrendItemView) findViewById(R.id.viewTrend);
        trendLayout = (LinearLayout) findViewById(R.id.trendLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        myListView = (MyListView) findViewById(R.id.myListView);
        hLine = findViewById(R.id.hLine);
        btnSend = (TextView) findViewById(R.id.btnSend);
        editText = (EditText) findViewById(R.id.editText);

        btnSend.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
        editText.setOnClickListener(this);
        btnLook.setOnClickListener(this);
        getDataFromServer();
    }

    @Override
    protected void initData() {

    }

    private void getDataFromServer() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("dynamicID", getIntent().getStringExtra("dynamicID"));
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.MAKEFRIEND_TRENDDETAIL, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.MAKEFRIEND_TRENDDETAIL)) {
            JSONObject jsonObject = (JSONObject) data;
            if (!StringUtil.isEmpty(jsonObject.optString("userPhoto"))) {
                Uri uri = Uri.parse(jsonObject.optString("userPhoto"));
                headPhoto.setImageURI(uri);
            }
            tvName.setText(jsonObject.optString("nickName"));
            tvDistance.setText(jsonObject.optString("distance"));
            tvAge.setText(jsonObject.optString("ageName"));
            if ("女".equals(jsonObject.optString("sexName"))) {
                ImageUtil.setDrawable(this, tvAge, R.mipmap.img_female, 1);
                tvAge.setBackgroundResource(R.drawable.small_button_pink);
            } else {
                ImageUtil.setDrawable(this, tvAge, R.mipmap.img_mail, 1);
                tvAge.setBackgroundResource(R.drawable.small_button_green);
            }
            if ("yes".equals(jsonObject.optString("followHas"))) {
                btnLook.setText("取消关注");
                btnLook.setBackgroundResource(R.drawable.small_button_grey);
                btnLook.setTextColor(getResources().getColor(R.color.main_color));
                btnLook.setTag("1");
            } else if ("no".equals(jsonObject.optString("followHas"))) {
                btnLook.setText("    关注    ");
                btnLook.setBackgroundResource(R.drawable.small_button_green);
                btnLook.setTextColor(getResources().getColor(R.color.white));
                btnLook.setTag("2");
            } else {
                btnLook.setVisibility(View.INVISIBLE);
            }
            if ("1".equals(jsonObject.optString("SmsHas"))) {
                btnMsg.setVisibility(View.VISIBLE);
            } else {
                btnMsg.setVisibility(View.GONE);
            }
            myHas = jsonObject.optInt("myHas");
            friendID = jsonObject.optString("friendID");
            viewTrend.showView(jsonObject);
            trendLayout.setVisibility(View.VISIBLE);

            trendCommentAdapter = new TrendCommentAdapter(this);
            myListView.setAdapter(trendCommentAdapter);
            if (myHas == 1) {
                myListView.setOnItemClickListener(this);
            }
            getCommentList();
        } else if (tag.equals(URLS.MAKEFRIEND_TRENDCOMMENT)) {
            progressBar.setVisibility(View.INVISIBLE);
            JSONObject jsonObject = (JSONObject) data;
            trendCommentAdapter.appendToList(jsonObject.optJSONArray("list"));
            if (trendCommentAdapter.getCount() > 0) {
                hLine.setVisibility(View.VISIBLE);
                myListView.setVisibility(View.VISIBLE);
            }
            pageTime = jsonObject.optInt("pageTime");
        } else if (tag.equals(URLS.MAKEFRIEND_COMMENT)) {
            editText.setText("");
            page = 1;
            pageTime = 0;
            trendCommentAdapter.clear();
            replyObject = null;
            LoadingDialog.showDialog(this);
            getCommentList();
        } else if (tag.equals(URLS.MAKEFRIEND_FOLLOW)) {
            String look = btnLook.getTag().toString();
            if ("2".equals(look)) {
                btnLook.setText("取消关注");
                btnLook.setBackgroundResource(R.drawable.small_button_grey);
                btnLook.setTextColor(getResources().getColor(R.color.main_color));
                btnLook.setTag("1");
            } else {
                btnLook.setText("    关注    ");
                btnLook.setBackgroundResource(R.drawable.small_button_green);
                btnLook.setTextColor(getResources().getColor(R.color.white));
                btnLook.setTag("2");
            }
        }
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        replyObject = null;
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        replyObject = null;
    }

    private void getCommentList() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("dynamicID", getIntent().getStringExtra("dynamicID"));
        params.put("page", page);
        params.put("pageTime", pageTime);
        HttpUtil.post(URLS.MAKEFRIEND_TRENDCOMMENT, params, this);
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendComment();
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSend) {
            sendComment();
        } else if (v.getId() == R.id.editText) {
            replyObject = null;
        } else if (v.getId() == R.id.btnLook) {
            // 添加关注
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("friendID", friendID);
            params.put("type", btnLook.getTag().toString());
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.MAKEFRIEND_FOLLOW, params, this);
        }
    }

    private void sendComment() {
        String content = editText.getText().toString();
        if (StringUtil.isEmpty(content)) {
            TipsUtil.show(this, "请输入评论内容");
            return;
        }
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("dynamicID", getIntent().getStringExtra("dynamicID"));
        params.put("content", content);
        if (replyObject != null) {
            params.put("parentID", replyObject.optString("replyID"));
        }
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.MAKEFRIEND_COMMENT, params, this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        replyObject = trendCommentAdapter.getItem(position);
        KeyBoardUtil.show(editText);
    }
}
