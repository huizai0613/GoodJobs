package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.io.File;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.view.upload.UploadImageAdapter;
import cn.goodjobs.bluecollar.view.upload.UploadImageView;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.baseclass.BaseImageUploadActivity;
import cn.goodjobs.common.baseclass.choosepic.ImgFileListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DatePickerUtil;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

/**
 * Created by zhuli on 2016/1/7.
 */
public class ItemResumeActivity extends BaseImageUploadActivity {

    RelativeLayout headPhotoLayout;
    SimpleDraweeView headPhoto;
    private RadioGroup sexGroup;
    private LinearLayout llBottom;
    private SelectorItemView itemWant, itemAddress, itemDegree, itemCheckIn, itemWorkAddress, itemSalary, itemJobFunc, itemWorktime;
    private SearchItemView itemBirthday;
    private InputItemView itemName;
    private Button btnSave;
    private boolean isContinue = false;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_resume;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我的简历");

        headPhotoLayout = (RelativeLayout) findViewById(R.id.headPhotoLayout);
        headPhoto = (SimpleDraweeView) findViewById(R.id.headPhoto);

        btnSave = (Button) findViewById(R.id.btn_save);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        itemWant = (SelectorItemView) findViewById(R.id.itemWant);
        itemAddress = (SelectorItemView) findViewById(R.id.itemAddress);
        itemBirthday = (SearchItemView) findViewById(R.id.itemBirthday);
        itemName = (InputItemView) findViewById(R.id.itemName);
        sexGroup = (RadioGroup) findViewById(R.id.sexGroup);

        itemDegree = (SelectorItemView) findViewById(R.id.itemDegree);
        itemCheckIn = (SelectorItemView) findViewById(R.id.itemCheckIn);
        itemWorkAddress = (SelectorItemView) findViewById(R.id.itemWorkAddress);
        itemSalary = (SelectorItemView) findViewById(R.id.itemSalary);
        itemJobFunc = (SelectorItemView) findViewById(R.id.itemJobFunc);
        itemWorktime = (SelectorItemView) findViewById(R.id.itemWorktime);

        new DatePickerUtil(this, itemBirthday, "yyyy-MM-dd", null);

        isModify = true;
        headPhotoLayout.setOnClickListener(this);

        getDataFromServer();
    }

    private void getDataFromServer() {
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_BASIC, this);
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        GoodJobsApp.getInstance().resumeJson = (JSONObject) data;
        setDataToView((JSONObject) data);
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }

    public void doSave(View v) {
        if (chooseCondition()) {
            if (isContinue) {
                Intent intent = new Intent(this, ResumeMoreActivity.class);
                startActivity(intent);
            } else {
                //发送请求
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (v.getId() == R.id.headPhotoLayout) {
            showBottomBtns();
        }
    }

    @Override
    protected void onImageFinish(Uri fileUri) {
        super.onImageFinish(fileUri);
        headPhoto.setImageURI(fileUri);
    }

    private void setDataToView(JSONObject jsonObject) {
        if (jsonObject.optString("realName").isEmpty()) {
            llBottom.setVisibility(View.GONE);
            isContinue = true;
        } else {
            llBottom.setVisibility(View.VISIBLE);
            isContinue = false;
        }
        itemName.setText(jsonObject.optString("realName"));
        itemBirthday.setText(jsonObject.optString("birthday"));
        new DatePickerUtil(this, itemBirthday, "yyyy-MM-dd", jsonObject.optString("birthday"));
        itemAddress.setText(jsonObject.optString("cityName"));
        itemAddress.setSelectorIds(jsonObject.optString("cityID"));
        itemDegree.setText(jsonObject.optString("fmtDegree"));
        itemDegree.setSelectorIds(jsonObject.optString("degree"));
        itemWorktime.setText(jsonObject.optString("fmtWorktime"));
        itemWorktime.setSelectorIds(jsonObject.optString("worktime"));
        itemSalary.setText(jsonObject.optString("fmtCurrentSalary"));
        itemSalary.setSelectorIds(jsonObject.optString("currentSalary"));
        if ("女".equals(jsonObject.optString("sex"))) {
            sexGroup.check(R.id.radioWoman);
        } else {
            sexGroup.check(R.id.radioMan);
        }
    }

    public boolean chooseCondition() {

        if (itemName.getText().length() <= 1) {
            TipsUtil.show(this, "姓名项不能少于两个字符");
            return false;
        }
        if (itemBirthday.getText().isEmpty()) {
            TipsUtil.show(this, "出生年月不能为空");
            return false;
        }
        if (itemAddress.getText().isEmpty()) {
            TipsUtil.show(this, "现居住地不能为空");
            return false;
        }
        if (itemWant.getText().isEmpty()) {
            TipsUtil.show(this, "想做什么不能为空");
            return false;
        }
        return true;
    }


}
