package cn.goodjobs.bluecollar.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemApplyActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemCheckActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemCollectActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemMoreActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemResumeActivity;
import cn.goodjobs.bluecollar.activity.InfoCenter.ItemSettingActivity;
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

    private LinearLayout show;
    private Button btnLogin;
    private TextView tvName, tvTime, tvResume, tvEntrust;
    private SimpleDraweeView myHeadImage, myImageview;
    private SearchItemView itemZhaoping, itemSetting, itemMore, itemCollection, itemShenqing, itemChakan, itemJianli;
    private boolean status, isFirst = false;

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
        itemMore = (SearchItemView) view.findViewById(R.id.itemMore);

        btnLogin.setOnClickListener(this);
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
        itemMore.setOnClickListener(this);


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
                LoadingDialog.showDialog(getActivity());
                HttpUtil.post(URLS.API_PERSON, this);
                isFirst = false;
            }
        }
    }


    public void changView() {
        if (GoodJobsApp.getInstance().isLogin()) {
            myImageview.setVisibility(View.INVISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            show.setVisibility(View.VISIBLE);
            myHeadImage.setVisibility(View.VISIBLE);
            //发送请求
            LoadingDialog.showDialog(getActivity());
            HttpUtil.post(URLS.API_PERSON, this);
            status = true;
        } else {
            myImageview.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            show.setVisibility(View.INVISIBLE);
            myHeadImage.setVisibility(View.INVISIBLE);
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
            intent.setClass(getActivity(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        if (i == R.id.btn_login) {
            intent.setClass(getActivity(), LoginActivity.class);
        } else if (i == R.id.myImageview) {
            intent.setClass(getActivity(), LoginActivity.class);
        } else if (i == R.id.itemSetting) {
            intent.setClass(getActivity(), ItemSettingActivity.class);
        } else if (i == R.id.itemMore) {
            intent.setClass(getActivity(), ItemMoreActivity.class);
        } else if (i == R.id.itemJianli) {
            intent.setClass(getActivity(), ItemResumeActivity.class);
        } else if (i == R.id.itemChakan) {
            intent.setClass(getActivity(), ItemCheckActivity.class);
        } else if (i == R.id.itemShenqing) {
            intent.setClass(getActivity(), ItemApplyActivity.class);
        } else if (i == R.id.itemCollection) {
            intent.setClass(getActivity(), ItemCollectActivity.class);
        } else if (i == R.id.itemZhaoping) {
            return;
        } else if (i == R.id.tv_entrust) {
            AlertDialogUtil.show(getActivity(), "委托投递", "设置委托投递成功", false, "我知道了", null, null, null);
            return;
        } else if (i == R.id.tv_resume) {
            return;
        } else if (i == R.id.myHeadImage) {
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_PERSON)) {
            GoodJobsApp.getInstance().personalInfo = (JSONObject) data;
            setDataToView();
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
        Uri uri = Uri.parse(GoodJobsApp.getInstance().personalInfo.optString("pic"));
        myImageview.setImageURI(uri);
        tvName.setText(GoodJobsApp.getInstance().personalInfo.optString("username"));
        tvTime.setText(GoodJobsApp.getInstance().personalInfo.optString("updateTime"));
        itemChakan.setHint(GoodJobsApp.getInstance().personalInfo.optString("countCorpLook") + "条");
        itemShenqing.setHint(GoodJobsApp.getInstance().personalInfo.optString("countJobApply") + "条");
        itemCollection.setHint(GoodJobsApp.getInstance().personalInfo.optString("countBookmark") + "条");
        itemJianli.setHint(GoodJobsApp.getInstance().personalInfo.optString("viewHistoryCount") + "次被浏览");
    }
}
