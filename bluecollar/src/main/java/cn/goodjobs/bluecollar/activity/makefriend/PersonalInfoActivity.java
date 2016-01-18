package cn.goodjobs.bluecollar.activity.makefriend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.PersonalTrendAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.pulltozoomview.PullToZoomListViewEx;

public class PersonalInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    PullToZoomListViewEx listView;
    RelativeLayout topLayout;
    ImageButton btnEdit;
    ImageButton btnMsg;
    View headView;
    int page = 1;
    PersonalTrendAdapter personalTrendAdapter;
    View footView;
    boolean hasMore; // 是否包含更多
    boolean firstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我的主页");
        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        topLayout.getBackground().setAlpha(0); // 设置背景透明
        listView = (PullToZoomListViewEx) findViewById(R.id.listview);
        btnEdit = (ImageButton) findViewById(R.id.btn_right);
        btnMsg = (ImageButton) findViewById(R.id.btn_msg);
        listView.setTopBarView(topLayout);
        headView = listView.getHeaderView();
        footView = LayoutInflater.from(this).inflate(R.layout.item_loading, null);
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (10.0F * (mScreenWidth / 16.0F)));
        listView.setHeaderLayoutParams(localObject);
        listView.setParallax(false);
        listView.getPullRootView().setOnItemClickListener(this);
        btnEdit.setOnClickListener(this);
        btnMsg.setOnClickListener(this);
        listView.setScrollListener(new PullToZoomListViewEx.ScrollListener() {
            @Override
            public void toFoot() {
                LogUtil.info("到达底部");
                if (hasMore) {
                    page++;
                    loadTend();
                }
            }
        });
        getDataFromServer();
    }

    private void getDataFromServer() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("tp", "my");
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.MAKEFRIEND_FRIEND, params, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.MAKEFRIEND_FRIEND)) {
            JSONObject jsonObject = (JSONObject) data;
            setDataToView(jsonObject);
            if (firstIn) {
                personalTrendAdapter = new PersonalTrendAdapter(this);
                personalTrendAdapter.showLoading = true;
                listView.setAdapter(personalTrendAdapter);
                loadTend();
            }
        } else if (tag.equals(URLS.MAKEFRIEND_TRENDLIST)) {
            firstIn = false;
            JSONObject jsonObject = (JSONObject) data;
            personalTrendAdapter.showLoading = false;
            personalTrendAdapter.appendToList(jsonObject.optJSONArray("list"));
            hasMore = jsonObject.optInt("maxPage")>page;
            if (hasMore) {
                if (listView.getPullRootView().getFooterViewsCount() == 0) {
                    listView.getPullRootView().addFooterView(footView);
                }
            } else {
                listView.getPullRootView().removeFooterView(footView);
            }
        }
    }

    private void setDataToView(JSONObject jsonObject) {
        SimpleDraweeView headPhoto = (SimpleDraweeView) headView.findViewById(R.id.headPhoto);
        TextView tvName = (TextView) headView.findViewById(R.id.tvName);
        TextView tvAddress = (TextView) headView.findViewById(R.id.tvAddress);
        TextView tvTrend = (TextView) headView.findViewById(R.id.tvTrend);
        TextView tvFans = (TextView) headView.findViewById(R.id.tvFans);
        TextView tvLook = (TextView) headView.findViewById(R.id.tvLook);
        TextView tvAge = (TextView) headView.findViewById(R.id.tvAge);
        tvLook.setOnClickListener(this);
        tvFans.setOnClickListener(this);

        if (!StringUtil.isEmpty(jsonObject.optString("userPhoto"))) {
            Uri uri = Uri.parse(jsonObject.optString("userPhoto"));
            headPhoto.setImageURI(uri);
        }
        tvName.setText(jsonObject.optString("nickName"));
        tvAddress.setText(jsonObject.optString("cityName"));
        tvTrend.setText("动态："+jsonObject.optString("dynamicNum"));
        tvFans.setText("粉丝："+jsonObject.optString("fansNum"));
        tvLook.setText("关注："+jsonObject.optString("followNum"));
        tvAge.setText(jsonObject.optString("ageName"));
        if ("女".equals(jsonObject.optString("sexName"))) {
            ImageUtil.setDrawable(this, tvAge, R.mipmap.img_female, 1);
        } else {
            ImageUtil.setDrawable(this, tvAge, R.mipmap.img_mail, 1);
        }
    }

    private void loadTend() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("tp", "my");
        HttpUtil.post(URLS.MAKEFRIEND_TRENDLIST, params, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = personalTrendAdapter.getItem(position-1);
        Intent intent = new Intent(this, TrendDetailActivity.class);
        intent.putExtra("dynamicID", jsonObject.optString("dynamicID"));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_right) {
            Intent intent = new Intent(this, MakeFriendPersonalInfoActivity.class);
            startActivityForResult(intent, 111);
        } else if (v.getId() == R.id.tvLook) {
            Intent intent = new Intent(this, LookActivity.class);
            startActivityForResult(intent, 111);
        } else if (v.getId() == R.id.tvFans) {
            Intent intent = new Intent(this, FansActivity.class);
            startActivityForResult(intent, 111);
        } else if (v.getId() == R.id.btn_msg) {
            Intent intent = new Intent(this, MsgListActivity.class);
            startActivityForResult(intent, 111);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getDataFromServer();
        }
    }
}
