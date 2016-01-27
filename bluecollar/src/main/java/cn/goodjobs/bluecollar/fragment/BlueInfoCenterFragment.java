package cn.goodjobs.bluecollar.fragment;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemApplyActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemCheckActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemCollectActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemInviteActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemResumeActivity;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

public class BlueInfoCenterFragment extends BaseFragment {

    private String url;
    private LinearLayout show;
    private Button btnLogin;
    ImageButton btnRight;
    private TextView tvName, tvTime, tvResume, tvEntrust;
    private SimpleDraweeView myHeadImage, myImageview;
    private SearchItemView itemZhaoping, itemSetting, itemCollection, itemShenqing, itemChakan, itemJianli;
    private boolean status, isFirst = false, isEntrust = false;

    public BlueInfoCenterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blue_info_center, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        show = (LinearLayout) view.findViewById(R.id.ll_show);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnRight = (ImageButton) view.findViewById(R.id.btn_right);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvTime = (TextView) view.findViewById(R.id.tv_update);
        tvResume = (TextView) view.findViewById(R.id.tv_resume);
        tvEntrust = (TextView) view.findViewById(R.id.tv_entrust);
        myHeadImage = (SimpleDraweeView) view.findViewById(R.id.myHeadImage);
        myImageview = (SimpleDraweeView) view.findViewById(R.id.myImageview);
        itemZhaoping = (SearchItemView) view.findViewById(R.id.itemZhaoping);
        itemSetting = (SearchItemView) view.findViewById(R.id.itemSetting);
        itemCollection = (SearchItemView) view.findViewById(R.id.itemCollection);
        itemShenqing = (SearchItemView) view.findViewById(R.id.itemShenqing);
        itemChakan = (SearchItemView) view.findViewById(R.id.itemChakan);
        itemJianli = (SearchItemView) view.findViewById(R.id.itemJianli);

        btnLogin.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        tvResume.setOnClickListener(this);
        tvEntrust.setOnClickListener(this);
        myHeadImage.setOnClickListener(this);
        myImageview.setOnClickListener(this);
        itemZhaoping.setOnClickListener(this);
        itemSetting.setOnClickListener(this);
        itemCollection.setOnClickListener(this);
        itemShenqing.setOnClickListener(this);
        itemChakan.setOnClickListener(this);
        itemJianli.setOnClickListener(this);


        if (GoodJobsApp.getInstance().isLogin()) {
            myImageview.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            show.setVisibility(View.VISIBLE);
            myHeadImage.setVisibility(View.VISIBLE);
            status = true;
            isFirst = true;
        } else {
            myImageview.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            show.setVisibility(View.INVISIBLE);
            myHeadImage.setVisibility(View.INVISIBLE);
            status = false;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!status == GoodJobsApp.getInstance().isLogin()) {
                changView();
            }
            if (isFirst) {
                //发送请求
                getDataFromServer();
                isFirst = false;
            }
        }
    }

    public void getDataFromServer() {
        LoadingDialog.showDialog(getActivity());
        HttpUtil.post(URLS.API_JOB_BlueMy, this);
    }


    public void changView() {
        if (GoodJobsApp.getInstance().isLogin()) {
            myImageview.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            show.setVisibility(View.VISIBLE);
            myHeadImage.setVisibility(View.VISIBLE);
            //发送请求
            LoadingDialog.showDialog(getActivity());
            HttpUtil.post(URLS.API_JOB_BlueMy, this);
            status = true;
        } else {
            myImageview.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            show.setVisibility(View.INVISIBLE);
            myHeadImage.setVisibility(View.INVISIBLE);
            setDataToNull();
            status = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!status == GoodJobsApp.getInstance().isLogin()) {
            changView();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        Intent intent = new Intent();
        boolean isLogin = GoodJobsApp.getInstance().isLogin();
        if (!isLogin) {
            if (i == R.id.itemZhaoping) {
                intent.setClass(getActivity(), ItemInviteActivity.class);
                intent.putExtra("url", url);
            } else {
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                return;
            }
        } else {
            if (i == R.id.btn_login) {
                intent.setClass(getActivity(), LoginActivity.class);
            } else if (i == R.id.myImageview) {
                intent.setClass(getActivity(), LoginActivity.class);
            }  else if (i == R.id.itemSetting) {
                intent.setClassName(getActivity(), "cn.goodjobs.common.activity.personalcenter.UpdateUserInfoActivity");
            } else if (i == R.id.itemJianli) {
                intent.setClass(getActivity(), ItemResumeActivity.class);
                startActivityForResult(intent, Activity.RESULT_FIRST_USER);
                return;
            } else if (i == R.id.itemChakan) {
                intent.setClass(getActivity(), ItemCheckActivity.class);
            } else if (i == R.id.itemShenqing) {
                intent.setClass(getActivity(), ItemApplyActivity.class);
            } else if (i == R.id.itemCollection) {
                intent.setClass(getActivity(), ItemCollectActivity.class);
            } else if (i == R.id.itemZhaoping) {
                intent.setClass(getActivity(), ItemInviteActivity.class);
                intent.putExtra("url", url);
            } else if (i == R.id.tv_entrust) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                if (isEntrust) {
                    params.put("type", "del");
                } else {
                    params.put("type", "add");
                }
                LoadingDialog.showDialog(getActivity());
                HttpUtil.post(URLS.API_JOB_BlueuserEntrustcv, params, this);
                return;
            } else if (i == R.id.tv_resume) {
                LoadingDialog.showDialog(getActivity());
                HttpUtil.post(URLS.API_JOB_UserUpdate, this);
                return;
            } else if (i == R.id.btn_right) {
                LoadingDialog.showDialog(getActivity());
                HttpUtil.post(URLS.API_JOB_BlueMy, this);
                return;
            } else if (i == R.id.myHeadImage) {
                return;
            }
        }
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Activity.RESULT_FIRST_USER && resultCode == 22) {
            LoadingDialog.showDialog(getActivity());
            HttpUtil.post(URLS.API_JOB_BlueMy, this);
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_JOB_BlueMy)) {
            GoodJobsApp.getInstance().bluePersonalInfo = (JSONObject) data;
            setDataToView();
        } else if (tag.equals(URLS.API_JOB_UserUpdate)) {
            TipsUtil.show(getActivity(), (String) data);
        } else if (tag.equals(URLS.API_JOB_BlueuserEntrustcv)) {
            if (isEntrust) {
                tvEntrust.setText("委托投递");
                isEntrust = false;
                AlertDialogUtil.show(getActivity(), "委托投递", ((JSONObject) data).optString("message"), false, "我知道了", null, null, null);
            } else {
                tvEntrust.setText("取消委托");
                isEntrust = true;
                AlertDialogUtil.show(getActivity(), "委托投递", "委托投递设置成功！\n  7天内，系统根据您的求职意向针对合适岗位自动投递简历！请保持手机畅通。", false, "我知道了", null, null, null);
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }

    private void setDataToView() {
        url = GoodJobsApp.getInstance().bluePersonalInfo.optString("jobFairUrl");
        Uri uri = Uri.parse(GoodJobsApp.getInstance().bluePersonalInfo.optString("userLogo"));
        myHeadImage.setImageURI(uri);
        tvName.setText(GoodJobsApp.getInstance().bluePersonalInfo.optString("userName"));
        tvTime.setText("更新时间  :  " + GoodJobsApp.getInstance().bluePersonalInfo.optString("updateDate"));
        itemChakan.setHint(GoodJobsApp.getInstance().bluePersonalInfo.optString("countCorpLook") + "条");
        itemShenqing.setHint(GoodJobsApp.getInstance().bluePersonalInfo.optString("countJobApply") + "条");
        itemCollection.setHint(GoodJobsApp.getInstance().bluePersonalInfo.optString("countBookmark") + "条");
        itemJianli.setHint(GoodJobsApp.getInstance().bluePersonalInfo.optString("clickNum") + "次被浏览");
        if ("0".equals(GoodJobsApp.getInstance().bluePersonalInfo.optString("autoSend"))) {
            tvEntrust.setText("委托投递");
            isEntrust = false;
        } else if ("1".equals(GoodJobsApp.getInstance().bluePersonalInfo.optString("autoSend"))) {
            tvEntrust.setText("取消委托");
            isEntrust = true;
        }
    }

    private void setDataToNull() {
        tvName.setText("");
        tvTime.setText("");
        itemChakan.setHint("");
        itemShenqing.setHint("");
        itemCollection.setHint("");
        itemJianli.setHint("");
    }
}
