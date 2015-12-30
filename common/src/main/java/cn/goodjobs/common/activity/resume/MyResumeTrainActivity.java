package cn.goodjobs.common.activity.resume;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.ResumeStuAdapter;
import cn.goodjobs.common.adapter.ResumeTrainAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

public class MyResumeTrainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    ImageButton btnRight;
    ResumeTrainAdapter resumeTrainAdapter;
    int delPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_train;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("培训经历");
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnRight.setImageResource(R.drawable.icon_add);
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        resumeTrainAdapter = new ResumeTrainAdapter(this);
        listView.setAdapter(resumeTrainAdapter);
        listView.setOnItemClickListener(this);

        getDataFromServer();
    }

    private void getDataFromServer() {
        resumeTrainAdapter.clear();
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_TRAINING, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_CV_TRAINING)) {
            JSONObject jsonObject = (JSONObject) data;
            resumeTrainAdapter.appendToList(jsonObject.optJSONArray("list"));
        } else if (tag.equals(URLS.API_CV_DEL_TRA)) {
            resumeTrainAdapter.removeItem(delPosition);
            resumeTrainAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_right) {
            Intent intent = new Intent(this, MyResumeTrainAddActivity.class);
            startActivityForResult(intent, 111);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = resumeTrainAdapter.getItem(position);
        Intent intent = new Intent(this, MyResumeTrainAddActivity.class);
        intent.putExtra("trainID", jsonObject.optString("trainID"));
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
                JSONObject jsonObject = resumeTrainAdapter.getItem(delPosition);
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("trainID", jsonObject.optString("trainID"));
                LoadingDialog.showDialog(MyResumeTrainActivity.this);
                HttpUtil.post(URLS.API_CV_DEL_TRA, params, MyResumeTrainActivity.this);
            }
        }, null);
    }
}