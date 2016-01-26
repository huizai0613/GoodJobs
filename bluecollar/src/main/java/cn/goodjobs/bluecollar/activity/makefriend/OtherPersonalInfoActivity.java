package cn.goodjobs.bluecollar.activity.makefriend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.adapter.PersonalTrendAdapter;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsCityFragment;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsGuanzhuFragment;
import cn.goodjobs.bluecollar.view.upload.CustomerListView;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyListView;

/**
 * 他人的主页
 * */

public class OtherPersonalInfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    int page = 1;
    String friendID;
    PersonalTrendAdapter personalTrendAdapter;
    CustomerListView myListView;
    TextView btnLook;
    View footView;
    View headView;
    TextView btnMsg;
    boolean hasMore; // 是否包含更多

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_personal_other_info;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle(getIntent().getStringExtra("nickName"));
        friendID = getIntent().getStringExtra("friendID");
        myListView = (CustomerListView) findViewById(R.id.myListView);
        footView = LayoutInflater.from(this).inflate(R.layout.item_loading, null);
        headView = LayoutInflater.from(this).inflate(R.layout.item_other_personal_top, null);
        myListView.addHeaderView(headView);
        myListView.setMyScrollListener(new CustomerListView.MyScrollListener() {
            @Override
            public void toFoot() {
                LogUtil.info("到达底部");
                if (hasMore) {
                    page++;
                    loadTend();
                }
            }
        });
        myListView.setOnItemClickListener(this);
        getDataFromServer();
    }

    private void getDataFromServer() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (!StringUtil.isEmpty(friendID)) {
            params.put("friendID", friendID);
        }
        params.put("tp", "other");
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
            headView.setVisibility(View.VISIBLE);
            personalTrendAdapter = new PersonalTrendAdapter(this);
            personalTrendAdapter.showLoading = true;
            myListView.setAdapter(personalTrendAdapter);
            loadTend();
        } else if (tag.equals(URLS.MAKEFRIEND_TRENDFRIENDLIST)) {
            JSONObject jsonObject = (JSONObject) data;
            personalTrendAdapter.showLoading = false;
            personalTrendAdapter.appendToList(jsonObject.optJSONArray("list"));
            hasMore = jsonObject.optInt("maxPage")>page;
            if (hasMore) {
                if (myListView.getFooterViewsCount() == 0) {
                    myListView.addFooterView(footView);
                }
            } else {
                myListView.removeFooterView(footView);
            }
        } else if (tag.equals(URLS.MAKEFRIEND_FOLLOW)) {
            String look = btnLook.getTag().toString();
            MakeFriendsCityFragment.needRefresh = true;
            MakeFriendsGuanzhuFragment.needRefresh = true;
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
        }
    }

    private void setDataToView(JSONObject jsonObject) {
        SimpleDraweeView headPhoto = (SimpleDraweeView) headView.findViewById(R.id.headPhoto);
        TextView tvAddress = (TextView) headView.findViewById(R.id.tvAddress);
        TextView tvFans = (TextView) headView.findViewById(R.id.tvFans);
        TextView tvLook = (TextView) headView.findViewById(R.id.tvLook);
        TextView tvAge = (TextView) headView.findViewById(R.id.tvAge);
        btnLook = (TextView) headView.findViewById(R.id.btnLook);
        btnMsg = (TextView) headView.findViewById(R.id.btnMsg);

        btnLook.setOnClickListener(this);
        if (!StringUtil.isEmpty(jsonObject.optString("userPhoto"))) {
            Uri uri = Uri.parse(jsonObject.optString("userPhoto"));
            headPhoto.setImageURI(uri);
        }
        tvAddress.setText(jsonObject.optString("cityName"));
        tvFans.setText("粉丝："+jsonObject.optString("fansNum"));
        tvLook.setText("关注："+jsonObject.optString("followNum"));
        tvAge.setText(jsonObject.optString("ageName"));
        if ("女".equals(jsonObject.optString("sexName"))) {
            ImageUtil.setDrawable(this, tvAge, R.mipmap.img_female, 1);
            tvAge.setBackgroundResource(R.drawable.small_button_pink);
        } else {
            ImageUtil.setDrawable(this, tvAge, R.mipmap.img_mail, 1);
            tvAge.setBackgroundResource(R.drawable.small_button_green);
        }
        if ("yes".equals(jsonObject.optString("followHas")) || "all".equals(jsonObject.optString("followHas"))) {
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
            btnLook.setVisibility(View.GONE);
        }
        if ("1".equals(jsonObject.optString("smsHas"))) {
            btnMsg.setVisibility(View.VISIBLE);
            btnMsg.setOnClickListener(this);
        } else {
            btnMsg.setVisibility(View.GONE);
        }
    }

    private void loadTend() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        if (!StringUtil.isEmpty(friendID)) {
            params.put("friendID", friendID);
        }
        params.put("tp", "other");
        HttpUtil.post(URLS.MAKEFRIEND_TRENDFRIENDLIST, params, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            JSONObject jsonObject = personalTrendAdapter.getItem(position-1);
            Intent intent = new Intent(this, TrendDetailActivity.class);
            intent.putExtra("dynamicID", jsonObject.optString("dynamicID"));
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_right) {
            Intent intent = new Intent(this, MakeFriendPersonalInfoActivity.class);
            startActivityForResult(intent, 111);
        } else if (v.getId() == R.id.btnLook) {
            // 添加关注
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("friendID", friendID);
            params.put("type", btnLook.getTag().toString());
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.MAKEFRIEND_FOLLOW, params, this);
        } else if (v.getId() == R.id.btnMsg) {
            Intent intent = new Intent(this, MsgDetailActivity.class);
            intent.putExtra("nickName", getIntent().getStringExtra("nickName"));
            intent.putExtra("friendID", friendID);
            startActivity(intent);
        }
    }
}
