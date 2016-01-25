package cn.goodjobs.bluecollar.activity.makefriend;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.PersonalTrendAdapter;
import cn.goodjobs.bluecollar.adapter.TrendCommentAdapter;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsCityFragment;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsNearFragment;
import cn.goodjobs.bluecollar.view.TrendItemView;
import cn.goodjobs.bluecollar.view.upload.CustomerListView;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.GeoUtils;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.KeyBoardUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyListView;

public class TrendDetailActivity extends BaseActivity implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    CustomerListView myListView;
    int pageTime;
    int page = 1;
    TrendCommentAdapter trendCommentAdapter;
    TextView btnSend;
    TextView btnLook;
    EditText editText;
    TextView btnMsg;
    ImageButton btnDel;
    View spitLine;
    JSONObject replyObject;
    int myHas = 0;
    String friendID;
    String nickName;

    View footView;
    View headView;
    boolean hasMore; // 是否包含更多
    MyLocation myLocation;
    String dynamicID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLocation = (MyLocation) SharedPrefUtil.getObject("location");
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

        myListView = (CustomerListView) findViewById(R.id.myListView);
        btnSend = (TextView) findViewById(R.id.btnSend);
        editText = (EditText) findViewById(R.id.editText);

        dynamicID = getIntent().getStringExtra("dynamicID");
        btnSend.setOnClickListener(this);
        editText.setOnEditorActionListener(this);
        editText.setOnClickListener(this);
        footView = LayoutInflater.from(this).inflate(R.layout.item_loading, null);
        headView = LayoutInflater.from(this).inflate(R.layout.item_trend_top, null);
        myListView.addHeaderView(headView);
        myListView.setMyScrollListener(new CustomerListView.MyScrollListener() {
            @Override
            public void toFoot() {
                LogUtil.info("到达底部");
                if (hasMore) {
                    page++;
                    getCommentList();
                }
            }
        });
        getDataFromServer();
    }

    @Override
    protected void initData() {

    }

    private void getDataFromServer() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("dynamicID", dynamicID);
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.MAKEFRIEND_TRENDDETAIL, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.MAKEFRIEND_TRENDDETAIL)) {
            JSONObject jsonObject = (JSONObject) data;
            setDataToView(jsonObject);
            headView.setVisibility(View.VISIBLE);
            trendCommentAdapter = new TrendCommentAdapter(this);
            trendCommentAdapter.showLoading = true;
            myListView.setAdapter(trendCommentAdapter);
            if (myHas == 1) {
                myListView.setOnItemClickListener(this);
            }
            getCommentList();
        } else if (tag.equals(URLS.MAKEFRIEND_TRENDCOMMENT)) {
            JSONObject jsonObject = (JSONObject) data;
            trendCommentAdapter.showLoading = false;
            trendCommentAdapter.appendToList(jsonObject.optJSONArray("list"));
            hasMore = jsonObject.optInt("maxPage")>page;
            if (hasMore) {
                if (myListView.getFooterViewsCount() == 0) {
                    myListView.addFooterView(footView);
                }
            } else {
                myListView.removeFooterView(footView);
            }
            pageTime = jsonObject.optInt("pageTime");
            if (trendCommentAdapter.getCount() == 0) {
                spitLine.setVisibility(View.GONE);
            } else {
                spitLine.setVisibility(View.VISIBLE);
            }
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
                JSONObject jsonObject = (JSONObject) data;
                if (jsonObject.has("arr")) {
                    JSONObject jsonObject1 = jsonObject.optJSONObject("arr").optJSONObject(friendID);
                    if ("1".equals(jsonObject1.optString("smsHas"))) {
                        btnMsg.setVisibility(View.VISIBLE);
                        btnMsg.setOnClickListener(this);
                    } else {
                        btnMsg.setVisibility(View.GONE);
                    }
                }
            } else {
                btnLook.setText("    关注    ");
                btnLook.setBackgroundResource(R.drawable.small_button_green);
                btnLook.setTextColor(getResources().getColor(R.color.white));
                btnLook.setTag("2");
                btnMsg.setVisibility(View.GONE);
            }
        } else if (tag.equals(URLS.MAKEFRIEND_TRENDDEL)) {
            JSONObject jsonObject = (JSONObject) data;
            TipsUtil.show(this, jsonObject.optString("message"));
            MakeFriendsNearFragment.needRefresh = true;
            setResult(RESULT_OK);
            finish();
        }
    }

    private void setDataToView(JSONObject jsonObject) {
        SimpleDraweeView headPhoto = (SimpleDraweeView) headView.findViewById(R.id.headPhoto);
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvDistance = (TextView) findViewById(R.id.tvDistance);
        btnLook = (TextView) findViewById(R.id.btnLook);
        btnDel = (ImageButton) findViewById(R.id.btnDel);
        spitLine = findViewById(R.id.spitLine);
        btnMsg = (TextView) findViewById(R.id.btnMsg);
        TextView tvAge = (TextView) findViewById(R.id.tvAge);
        TrendItemView viewTrend = (TrendItemView) findViewById(R.id.viewTrend);

        btnLook.setOnClickListener(this);
        if (!StringUtil.isEmpty(jsonObject.optString("userPhoto"))) {
            Uri uri = Uri.parse(jsonObject.optString("userPhoto"));
            headPhoto.setImageURI(uri);
        }
        nickName = jsonObject.optString("nickName");
        tvName.setText(nickName);

        tvAge.setText(jsonObject.optString("ageName"));
        if ("女".equals(jsonObject.optString("sexName"))) {
            ImageUtil.setDrawable(this, tvAge, R.mipmap.img_female, 1);
            tvAge.setBackgroundResource(R.drawable.small_button_pink);
        } else {
            ImageUtil.setDrawable(this, tvAge, R.mipmap.img_mail, 1);
            tvAge.setBackgroundResource(R.drawable.small_button_green);
        }
        myHas = jsonObject.optInt("myHas");
        if (myHas == 1) {
            // 当前是自己发表的动态
            btnLook.setVisibility(View.INVISIBLE);
            btnMsg.setVisibility(View.GONE);
            tvDistance.setVisibility(View.GONE);
            btnDel.setVisibility(View.VISIBLE);
            btnDel.setOnClickListener(this);
        } else {
            if (myLocation != null && jsonObject.optDouble("fdLng") != 0 && jsonObject.optDouble("fdLat") != 0) {
                tvDistance.setVisibility(View.VISIBLE);
                tvDistance.setText(GeoUtils.friendlyDistance(GeoUtils.distance(myLocation.latitude, myLocation.longitude, jsonObject.optDouble("fdLat"), jsonObject.optDouble("fdLng"))));
            } else {
                tvDistance.setVisibility(View.GONE);
            }
            if ("yes".equals(jsonObject.optString("followHas")) || "all".equals(jsonObject.optString("followHas"))) {
                btnLook.setText("取消关注");
                btnLook.setBackgroundResource(R.drawable.small_button_grey);
                btnLook.setTextColor(getResources().getColor(R.color.main_color));
                btnLook.setTag("1");
                if ("1".equals(jsonObject.optString("smsHas"))) {
                    btnMsg.setVisibility(View.VISIBLE);
                    btnMsg.setOnClickListener(this);
                } else {
                    btnMsg.setVisibility(View.GONE);
                }
            } else if ("no".equals(jsonObject.optString("followHas"))) {
                btnLook.setText("    关注    ");
                btnLook.setBackgroundResource(R.drawable.small_button_green);
                btnLook.setTextColor(getResources().getColor(R.color.white));
                btnLook.setTag("2");
            } else {
                btnLook.setVisibility(View.INVISIBLE);
            }

        }
        friendID = jsonObject.optString("friendID");
        viewTrend.showView(jsonObject);
    }

    private void getCommentList() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("dynamicID", dynamicID);
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
        } else if (v.getId() == R.id.btnMsg) {
            Intent intent = new Intent(this, MsgDetailActivity.class);
            intent.putExtra("nickName", nickName);
            intent.putExtra("friendID", friendID);
            startActivity(intent);
        } else if (v.getId() == R.id.btnDel) {
            // 删除动态
            AlertDialogUtil.show(this, R.string.app_name, "您确定删除这条动态吗？", true, "确定", "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    HashMap<String, Object> params = new HashMap<String, Object>();
                    params.put("dynamicID", dynamicID);
                    LoadingDialog.showDialog(TrendDetailActivity.this);
                    HttpUtil.post(URLS.MAKEFRIEND_TRENDDEL, params, TrendDetailActivity.this);
                }
            }, null);
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
        if (position > 0) {
            replyObject = trendCommentAdapter.getItem(position-1);
            KeyBoardUtil.show(editText);
        }
    }
}
