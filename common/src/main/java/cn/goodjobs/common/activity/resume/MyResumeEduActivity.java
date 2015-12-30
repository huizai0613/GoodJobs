package cn.goodjobs.common.activity.resume;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.ResumeEduAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

public class MyResumeEduActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ImageButton btnRight;
    ResumeEduAdapter resumeEduAdapter;
    int delPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_edu;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("教育经历");
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnRight.setImageResource(R.drawable.icon_add);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        resumeEduAdapter = new ResumeEduAdapter(this);
        listView.setAdapter(resumeEduAdapter);
        listView.setOnItemClickListener(this);

        getDataFromServer();
    }

    private void getDataFromServer() {
        resumeEduAdapter.clear();
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_EDUCATION, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_EDUCATION)) {
            JSONObject jsonObject = (JSONObject) data;
            resumeEduAdapter.appendToList(jsonObject.optJSONArray("list"));
        } else if (tag.equals(URLS.API_CV_DEL_EDU)) {
            resumeEduAdapter.removeItem(delPosition);
            resumeEduAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_right) {
            Intent intent = new Intent(this, MyResumeEduAddActivity.class);
            startActivityForResult(intent, 111);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = resumeEduAdapter.getItem(position);
        Intent intent = new Intent(this, MyResumeEduAddActivity.class);
        intent.putExtra("eduID", jsonObject.optString("eduID"));
        startActivityForResult(intent, 111);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getDataFromServer();
        }
    }

    public void doDelete(View view) {
        delPosition = (int) view.getTag();
        AlertDialogUtil.show(this, R.string.app_name, "您确定删除这条记录吗？", true, "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject jsonObject = resumeEduAdapter.getItem(delPosition);
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("eduID", jsonObject.optString("eduID"));
                LoadingDialog.showDialog(MyResumeEduActivity.this);
                HttpUtil.post(URLS.API_CV_DEL_EDU, params, MyResumeEduActivity.this);
            }
        }, null);
    }
}
