package cn.goodjobs.common.fragemnt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.activity.personalcenter.PersonalCollectionActivity;
import cn.goodjobs.common.activity.personalcenter.ResumeOpenSettingActivity;
import cn.goodjobs.common.activity.personalcenter.UpdateMobileActivity;
import cn.goodjobs.common.activity.personalcenter.UpdateUserInfoActivity;
import cn.goodjobs.common.activity.resume.MyResumeActivity;
import cn.goodjobs.common.activity.personalcenter.PersonalApplyActivity;
import cn.goodjobs.common.activity.personalcenter.PersonalInboxActivity;
import cn.goodjobs.common.activity.personalcenter.PersonalLookActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

/**
 * Created by wanggang on 2015/12/22 0022.
 */
public class PersonalCenterFragment extends BaseFragment {

    SimpleDraweeView myImageview;
    TextView tvUsername, tvPhone, tvUpdatetime;
    ImageButton btnYanzheng, btnRefresh;
    SearchItemView itemLogin, itemSetting, itemMessage, itemCollection, itemShenqing, itemChakan, itemXiaoyuan, itemJianli;

    public PersonalCenterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        setTopTitle(view, "个人中心");
        hideBackBtn(view);

        myImageview = (SimpleDraweeView) view.findViewById(R.id.myImageview);
        tvUsername = (TextView) view.findViewById(R.id.tvUsername);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        tvUpdatetime = (TextView) view.findViewById(R.id.tvUpdatetime);
        btnYanzheng = (ImageButton) view.findViewById(R.id.btnYanzheng);
        btnRefresh = (ImageButton) view.findViewById(R.id.btnRefresh);
        itemLogin = (SearchItemView) view.findViewById(R.id.itemLogin);
        itemSetting = (SearchItemView) view.findViewById(R.id.itemSetting);
        itemMessage = (SearchItemView) view.findViewById(R.id.itemMessage);
        itemCollection = (SearchItemView) view.findViewById(R.id.itemCollection);
        itemShenqing = (SearchItemView) view.findViewById(R.id.itemShenqing);
        itemChakan = (SearchItemView) view.findViewById(R.id.itemChakan);
        itemXiaoyuan = (SearchItemView) view.findViewById(R.id.itemXiaoyuan);
        itemJianli = (SearchItemView) view.findViewById(R.id.itemJianli);

        itemLogin.setOnClickListener(this);
        itemSetting.setOnClickListener(this);
        itemMessage.setOnClickListener(this);
        itemCollection.setOnClickListener(this);
        itemShenqing.setOnClickListener(this);
        itemChakan.setOnClickListener(this);
        itemXiaoyuan.setOnClickListener(this);
        itemJianli.setOnClickListener(this);
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("PersonalCenterFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (!isLoad && isVisibleToUser) {
            isLoad = true;
            getDataFromServer();
        }
    }

    public void getDataFromServer() {
        LoadingDialog.showDialog(getActivity());
        HttpUtil.post(URLS.API_PERSON, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_PERSON)) {
            GoodJobsApp.getInstance().personalInfo = (JSONObject) data;
            setDataToView();
        }
    }

    // 将数据显示到界面上
    private void setDataToView() {
        Uri uri = Uri.parse(GoodJobsApp.getInstance().personalInfo.optString("pic"));
        myImageview.setImageURI(uri);
        tvUsername.setText("用  户  名：" + GoodJobsApp.getInstance().personalInfo.optString("username"));
        tvPhone.setText("手  机  号：" + GoodJobsApp.getInstance().personalInfo.optString("mb"));
        tvUpdatetime.setText("更新时间：" + GoodJobsApp.getInstance().personalInfo.optString("updateTime"));
        itemChakan.setHint(GoodJobsApp.getInstance().personalInfo.optString("countCorpLook") + "条");
        itemShenqing.setHint(GoodJobsApp.getInstance().personalInfo.optString("countJobApply") + "条");
        itemCollection.setHint(GoodJobsApp.getInstance().personalInfo.optString("countBookmark") + "条");
        itemMessage.setHint(GoodJobsApp.getInstance().personalInfo.optString("countInbox") + "条");

        btnYanzheng.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        if ("0".equals(GoodJobsApp.getInstance().personalInfo.optString("ismb"))) {
            btnYanzheng.setImageResource(R.drawable.wyz);
            btnYanzheng.setOnClickListener(this);
        } else {
            btnYanzheng.setImageResource(R.drawable.yyz);
        }
        itemJianli.setHint(GoodJobsApp.getInstance().personalInfo.optString("viewHistoryCount") + "次被浏览");
        itemSetting.setHint(GoodJobsApp.getInstance().personalInfo.optString("pubLevel"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.itemChakan) {
            intent.setClass(getActivity(), PersonalLookActivity.class);
        } else if (v.getId() == R.id.itemShenqing) {
            intent.setClass(getActivity(), PersonalApplyActivity.class);
        } else if (v.getId() == R.id.itemCollection) {
            intent.setClass(getActivity(), PersonalCollectionActivity.class);
        } else if (v.getId() == R.id.itemMessage) {
            intent.setClass(getActivity(), PersonalInboxActivity.class);
        } else if (v.getId() == R.id.itemJianli) {
            intent.setClass(getActivity(), MyResumeActivity.class);
        } else if (v.getId() == R.id.itemSetting) {
            intent.setClass(getActivity(), ResumeOpenSettingActivity.class);
        } else if (v.getId() == R.id.itemXiaoyuan) {
            intent.setClassName(getActivity(), "cn.goodjobs.campusjobs.activity.MyCampusActivity");
        } else if (v.getId() == R.id.itemLogin) {
            intent.setClass(getActivity(), UpdateUserInfoActivity.class);
        } else if (v.getId() == R.id.btnYanzheng){
            intent.setClass(getActivity(), UpdateMobileActivity.class);
        }
        startActivityForResult(intent, 111);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            LoadingDialog.showDialog(getActivity());
            HttpUtil.post(URLS.API_PERSON, this);
        }
    }
}
