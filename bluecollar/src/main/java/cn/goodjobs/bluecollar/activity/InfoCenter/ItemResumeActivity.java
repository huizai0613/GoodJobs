package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

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
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.CryptUtils;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.InputItemView;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

/**
 * Created by zhuli on 2016/1/7.
 */
public class ItemResumeActivity extends BaseImageUploadActivity {

    Uri fileUri;
    RelativeLayout headPhotoLayout;
    SimpleDraweeView headPhoto;
    private LinearLayout llTop;
    private Intent intent;
    private RadioGroup sexGroup;
    private LinearLayout llBottom;
    private SelectorItemView itemWant, itemAddress, itemDegree, itemCheckIn, itemWorkAddress, itemSalary, itemJobFunc, itemWorktime;
    private SearchItemView itemBirthday;
    private InputItemView itemName;
    private Button btnSave;
    private CheckBox cbCheck;
    private boolean isContinue = false;
    private EditText etContent;
    private String realname, sex, birthday, cityID, sFunction, autoSend, degree, salary, ckTime, sWorkPlace, jobName, worktime, selfIntro;

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

        intent = new Intent();

        headPhotoLayout = (RelativeLayout) findViewById(R.id.headPhotoLayout);
        headPhoto = (SimpleDraweeView) findViewById(R.id.headPhoto);

        btnSave = (Button) findViewById(R.id.btn_save);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        itemWant = (SelectorItemView) findViewById(R.id.itemWant);
        itemAddress = (SelectorItemView) findViewById(R.id.itemAddress);
        itemBirthday = (SearchItemView) findViewById(R.id.itemBirthday);
        itemName = (InputItemView) findViewById(R.id.myName);
        sexGroup = (RadioGroup) findViewById(R.id.sexGroup);
        cbCheck = (CheckBox) findViewById(R.id.cb_check);
        etContent = (EditText) findViewById(R.id.etContent);

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
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("cvType", 0);
        HttpUtil.post(URLS.API_JOB_BlueBasic, params, this);
    }


    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_JOB_BlueBasic)) {
            GoodJobsApp.getInstance().bluePersonalInfo = (JSONObject) data;
            setDataToView((JSONObject) data);
        } else if (tag.equals(URLS.API_JOB_BlueBasicsave)) {
            if (isContinue) {
                Intent intent = new Intent(this, ResumeMoreActivity.class);
                startActivity(intent);
            } else {
                TipsUtil.show(this, ((JSONObject) data).optString("message"));
            }
        } else if (tag.equals(URLS.API_JOB_CvPhotosave)) {
            if (data instanceof String) {
                TipsUtil.show(this, (String) data);
            } else if (data instanceof JSONObject) {
                TipsUtil.show(this, ((JSONObject) data).optString("message"));
                headPhoto.setImageURI(Uri.parse(((JSONObject) data).optString("photo")));
            }
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        TipsUtil.show(this, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        TipsUtil.show(this, errorMessage);
    }

    public void doSave(View v) {
        if (chooseCondition()) {
            if (isContinue) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("cvType", 1);
                params.put("realname", realname);
                params.put("sex", sex);
                params.put("birthday", birthday);
                params.put("cityID", cityID);
                params.put("sFunction", sFunction);
                params.put("autoSend", autoSend);
                LoadingDialog.showDialog(this);
                HttpUtil.post(URLS.API_JOB_BlueBasicsave, params, this);
                this.setResult(22, intent);
            } else {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("cvType", 0);
                params.put("realname", realname);
                params.put("sex", sex);
                params.put("birthday", birthday);
                params.put("cityID", cityID);
                params.put("sFunction", sFunction);
                params.put("autoSend", autoSend);
                params.put("degree", degree);
                params.put("ckTime", ckTime);
                params.put("salary", salary);
                params.put("sWorkPlace", sWorkPlace);
                params.put("jobName", jobName);
                params.put("worktime", worktime);
                params.put("selfIntro", selfIntro);
                LoadingDialog.showDialog(this);
                HttpUtil.post(URLS.API_JOB_BlueBasicsave, params, this);
                this.setResult(22, intent);
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
        this.fileUri = fileUri;
        RequestParams requestParams = new RequestParams();
        try {
            requestParams.put("portrait", ImageUtil.scal(fileUri, 10240));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LoadingDialog.showDialog(this);
        HttpUtil.uploadFile(URLS.API_JOB_CvPhotosave, URLS.API_JOB_CvPhotosave, requestParams, this);
        this.setResult(22, intent);
    }

    private void setDataToView(JSONObject jsonObject) {
        llTop.setVisibility(View.VISIBLE);
        headPhoto.setImageURI(Uri.parse(jsonObject.optString("userLogo")));
        if (StringUtil.isEmpty(jsonObject.optString("realName"))) {
            llBottom.setVisibility(View.GONE);
            btnSave.setText("保存并完善简历");
            isContinue = true;
        } else {
            llBottom.setVisibility(View.VISIBLE);
            btnSave.setText("保存");
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
        itemCheckIn.setText(jsonObject.optString("fmtWorktime"));
        itemCheckIn.setSelectorIds(jsonObject.optString("checkInTime"));
        itemSalary.setText(jsonObject.optString("salaryName"));
        itemSalary.setSelectorIds(jsonObject.optString("salary"));
        itemWant.setText(jsonObject.optString("fmtPosition"));
        itemWant.setSelectorIds(jsonObject.optString("position"));
        itemWorkAddress.setText(jsonObject.optString("fmtWorkPlace"));
        itemWorkAddress.setSelectorIds(jsonObject.optString("workPlace"));
        itemJobFunc.setText(jsonObject.optString("fmtJobName"));
        itemJobFunc.setSelectorIds(jsonObject.optString("jobName"));
        etContent.setText(jsonObject.optString("selfIntro"));
        if ("女".equals(jsonObject.optString("sex"))) {
            sexGroup.check(R.id.radioWoman);
        } else {
            sexGroup.check(R.id.radioMan);
        }
        if ("0".equals(jsonObject.optString("autoSend"))) {
            cbCheck.setChecked(false);
        } else if ("1".equals(jsonObject.optString("autoSend"))) {
            cbCheck.setChecked(true);
        }
    }

    public boolean chooseCondition() {

        if (itemName.getText().length() <= 1) {
            TipsUtil.show(this, "姓名项不能少于两个字符");
            return false;
        } else {
            realname = itemName.getText();
        }
        if (itemBirthday.getText().isEmpty()) {
            TipsUtil.show(this, "出生年月不能为空");
            return false;
        } else {
            birthday = itemBirthday.getText();
        }
        if (itemAddress.getText().isEmpty()) {
            TipsUtil.show(this, "现居住地不能为空");
            return false;
        } else {
            cityID = itemAddress.getSelectorIds();
        }
        if (itemWant.getText().isEmpty()) {
            TipsUtil.show(this, "想做什么不能为空");
            return false;
        } else {
            sFunction = itemWant.getSelectorIds();
        }
        if (sexGroup.getCheckedRadioButtonId() == R.id.radioWoman) {
            sex = "女";
        } else {
            sex = "男";
        }
        if (cbCheck.isChecked()) {
            autoSend = "1";
        } else {
            autoSend = "0";
        }
        degree = itemDegree.getSelectorIds();
        ckTime = itemCheckIn.getSelectorIds();
        salary = itemSalary.getSelectorIds();
        sWorkPlace = itemWorkAddress.getSelectorIds();
        jobName = itemJobFunc.getSelectorIds();
        worktime = itemWorktime.getSelectorIds();
        selfIntro = etContent.getText().toString();
        return true;
    }


}
