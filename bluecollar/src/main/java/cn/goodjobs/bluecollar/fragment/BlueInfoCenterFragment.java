package cn.goodjobs.bluecollar.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.ItemSettingActivity;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LoginActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.TipsUtil;
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
        tvName.setOnClickListener(this);
        tvTime.setOnClickListener(this);
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
                TipsUtil.show(getActivity(), "发送请求");
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
            TipsUtil.show(getActivity(), "发送请求");
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
        if (i == R.id.btn_login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (i == R.id.myImageview) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else if (i == R.id.itemSetting) {
            Intent intent = new Intent(getActivity(), ItemSettingActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }
}
