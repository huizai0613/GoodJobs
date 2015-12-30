package cn.goodjobs.common.activity.resume;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SearchItemView;

public class MyResumeMoreActivity extends BaseActivity {

    ImageButton btnRight;
    SearchItemView itemShijian, itemPeixun, itemLanguage;

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
        return R.layout.activity_my_resume_more;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("更多简历内容");
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnRight.setImageResource(R.drawable.icon_view);
        btnRight.setVisibility(View.VISIBLE);
        itemShijian = (SearchItemView) findViewById(R.id.itemShijian);
        itemPeixun = (SearchItemView) findViewById(R.id.itemPeixun);
        itemLanguage = (SearchItemView) findViewById(R.id.itemLanguage);
        btnRight.setOnClickListener(this);
        itemShijian.setOnClickListener(this);
        itemPeixun.setOnClickListener(this);
        itemLanguage.setOnClickListener(this);
    }

    private void getDataFromServer() {
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_INDEX, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        if (v.getId() == R.id.btn_right) {
            intent.setClass(this, MyResumePreviewActivity.class);
        } else if (v.getId() == R.id.itemShijian) {
            intent.setClass(this, MyResumePreviewActivity.class);
        } else if (v.getId() == R.id.itemPeixun) {
            intent.setClass(this, MyResumeTrainActivity.class);
        } else if (v.getId() == R.id.itemLanguage) {
            intent.setClass(this, MyResumeLanActivity.class);
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
            setStatus(itemShijian, GoodJobsApp.getInstance().resumeJson.optString("stuStatus"));
            setStatus(itemPeixun, GoodJobsApp.getInstance().resumeJson.optString("trainStatus"));
            setStatus(itemLanguage, GoodJobsApp.getInstance().resumeJson.optString("langStatus"));
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

    @Override
    protected void initData() {

    }
}