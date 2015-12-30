package cn.goodjobs.common.fragemnt;

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

import cn.goodjobs.common.R;
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
    SearchItemView itemLogin,itemSetting, itemSousuo, itemMessage, itemCollection, itemShenqing, itemChakan, itemXiaoyuan, itemJianli;

    JSONObject personalInfo;

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
        itemSousuo = (SearchItemView) view.findViewById(R.id.itemSousuo);
        itemMessage = (SearchItemView) view.findViewById(R.id.itemMessage);
        itemCollection = (SearchItemView) view.findViewById(R.id.itemCollection);
        itemShenqing = (SearchItemView) view.findViewById(R.id.itemShenqing);
        itemChakan = (SearchItemView) view.findViewById(R.id.itemChakan);
        itemXiaoyuan = (SearchItemView) view.findViewById(R.id.itemXiaoyuan);
        itemJianli = (SearchItemView) view.findViewById(R.id.itemJianli);

        itemLogin.setOnClickListener(this);
        itemSetting.setOnClickListener(this);
        itemSousuo.setOnClickListener(this);
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
            LoadingDialog.showDialog(getActivity());
            HttpUtil.post(URLS.API_PERSON, this);
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_PERSON)) {
            personalInfo = (JSONObject) data;
            setDataToView();
        }
    }

    // 将数据显示到界面上
    private void setDataToView() {
        Uri uri = Uri.parse(personalInfo.optString("pic"));
        myImageview.setImageURI(uri);
        tvUsername.setText("用  户  名：" + personalInfo.optString("username"));
        tvPhone.setText("手  机  号："+personalInfo.optString("mb"));
        tvUpdatetime.setText("更新时间：" + personalInfo.optString("updateTime"));
        itemChakan.setHint(personalInfo.optString("countCorpLook") + "条");
        itemShenqing.setHint(personalInfo.optString("countJobApply") + "条");
        itemCollection.setHint(personalInfo.optString("countBookmark") + "条");
        itemMessage.setHint(personalInfo.optString("countInbox") + "条");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.itemChakan) {
            intent.setClass(getActivity(), PersonalLookActivity.class);
        } else if (v.getId() == R.id.itemShenqing) {
            intent.setClass(getActivity(), PersonalApplyActivity.class);
            intent.putExtra("title", "职位申请记录");
            intent.putExtra("url", URLS.API_USER_OUTBOX);
        } else if (v.getId() == R.id.itemCollection) {
            intent.setClass(getActivity(), PersonalApplyActivity.class);
            intent.putExtra("title", "收藏的职位");
            intent.putExtra("url", URLS.API_USER_USERFAVORITE);
        } else if (v.getId() == R.id.itemMessage) {
            intent.setClass(getActivity(), PersonalInboxActivity.class);
        } else if (v.getId() == R.id.itemJianli) {
            intent.setClass(getActivity(), MyResumeActivity.class);
        } else {
            intent.setClass(getActivity(), PersonalLookActivity.class);
        }
        startActivity(intent);
    }
}
