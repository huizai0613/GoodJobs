package cn.goodjobs.common.activity.resume;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONObject;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

public class MyResumeActivity extends BaseActivity {

    Button btnMore;
    ImageButton btnRight;
    SearchItemView itemBasicInfo, itemEducate, itemWork, itemWill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus(); // 刷新界面状态
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我的简历");
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnRight.setImageResource(R.drawable.icon_view);
        btnRight.setVisibility(View.VISIBLE);
        btnMore = (Button) findViewById(R.id.btnMore);
        itemBasicInfo = (SearchItemView) findViewById(R.id.itemBasicInfo);
        itemEducate = (SearchItemView) findViewById(R.id.itemEducate);
        itemWork = (SearchItemView) findViewById(R.id.itemWork);
        itemWill = (SearchItemView) findViewById(R.id.itemWill);

        btnMore.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        itemBasicInfo.setOnClickListener(this);
        itemEducate.setOnClickListener(this);
        itemWork.setOnClickListener(this);
        itemWill.setOnClickListener(this);
        getDataFromServer();
    }

    private void getDataFromServer() {
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_INDEX, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.btnMore) {
            intent.setClass(this, MyResumeMoreActivity.class);
        } else if (v.getId() == R.id.btn_right) {
            intent.setClass(this, MyResumePreviewActivity.class);
        } else if (v.getId() == R.id.itemBasicInfo) {
            intent.setClass(this, MyResumeBaseInfoActivity.class);
        } else if (v.getId() == R.id.itemEducate) {
            intent.setClass(this, MyResumeEduActivity.class);
        } else if (v.getId() == R.id.itemWork) {
            intent.setClass(this, MyResumeWorkActivity.class);
        } else if (v.getId() == R.id.itemWill) {
            intent.setClass(this, MyResumeWillActivity.class);
        }
        startActivityForResult(intent, 111);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        GoodJobsApp.getInstance().resumeJson = (JSONObject) data;
        setStatus();
    }

    private void setStatus() {
        if (GoodJobsApp.getInstance().resumeJson != null) {
            setStatus(itemBasicInfo, GoodJobsApp.getInstance().resumeJson.optString("basicStatus"));
            setStatus(itemEducate, GoodJobsApp.getInstance().resumeJson.optString("eduStatus"));
            setStatus(itemWork, GoodJobsApp.getInstance().resumeJson.optString("workStatus"));
            setStatus(itemWill, GoodJobsApp.getInstance().resumeJson.optString("willStatus"));
        }
    }

    private void setStatus(SearchItemView searchItemView, String status) {
        if ("1".equals(status)) {
            searchItemView.setHint("完整");
        } else {
            searchItemView.setHint("不完整");
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
