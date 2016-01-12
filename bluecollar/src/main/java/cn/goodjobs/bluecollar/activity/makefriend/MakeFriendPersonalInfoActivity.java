package cn.goodjobs.bluecollar.activity.makefriend;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.baseclass.BaseImageUploadActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DatePickerUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

public class MakeFriendPersonalInfoActivity extends BaseImageUploadActivity {

    RelativeLayout headPhotoLayout;
    SimpleDraweeView headPhoto;
    InputItemView itemName;
    RadioGroup sexGroup;
    SearchItemView itemBirthday;
    SelectorItemView itemAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_make_friend_personal_info;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("个人资料");
        headPhotoLayout = (RelativeLayout) findViewById(R.id.headPhotoLayout);
        headPhoto = (SimpleDraweeView) findViewById(R.id.headPhoto);
        itemName = (InputItemView) findViewById(R.id.itemName);
        sexGroup = (RadioGroup) findViewById(R.id.sexGroup);
        itemBirthday = (SearchItemView) findViewById(R.id.itemBirthday);
        itemAddress = (SelectorItemView) findViewById(R.id.itemAddress);

        isModify = true;
        headPhotoLayout.setOnClickListener(this);

        getDataFromServer();
    }

    private void getDataFromServer() {
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.MAKEFRIEND_BASICINFO, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.MAKEFRIEND_BASICINFO)) {
            JSONObject jsonObject = (JSONObject) data;
            if (!StringUtil.isEmpty(jsonObject.optString("userPhoto"))) {
                Uri uri = Uri.parse(jsonObject.optString("userPhoto"));
                headPhoto.setImageURI(uri);
            }
            itemName.setText(jsonObject.optString("nickName"));
            String birthday = jsonObject.optString("birthday");
            itemBirthday.setText(birthday);
            new DatePickerUtil(this, itemBirthday, "yyyy-MM-dd", birthday);
            if ("女".equals(jsonObject.optString("sexName"))) {
                sexGroup.check(R.id.radioWuman);
            } else {
                sexGroup.check(R.id.radioMan);
            }
            itemAddress.setText(jsonObject.optString("cityName"));
            itemAddress.setSelectorIds(jsonObject.optString("cityID"));
        } else if (tag.equals(URLS.MAKEFRIEND_BASICSAVE)) {
            JSONObject jsonObject = (JSONObject) data;
            TipsUtil.show(this, jsonObject.optString("message"));
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.headPhotoLayout) {
            showBottomBtns();
        }
    }

    @Override
    protected void onImageFinish(Uri fileUri) {
        super.onImageFinish(fileUri);
        headPhoto.setImageURI(fileUri);
    }

    public void doSave(View view) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        if (itemName.isEmpty()) {
            return;
        }
        params.put("nickName", itemName.getText());
        if (sexGroup.getCheckedRadioButtonId() == R.id.radioMan) {
            params.put("sex", "1");
        } else {
            params.put("sex", "2");
        }
        params.put("birthday", itemBirthday.getText());
        params.put("cityID", itemAddress.getSelectorIds());
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.MAKEFRIEND_BASICSAVE, params, this);
    }
}
