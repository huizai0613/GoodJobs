package cn.goodjobs.common.activity.personalcenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.OpenSetAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyListView;

public class ResumeOpenSettingActivity extends BaseActivity {

    RadioGroup radioGroup;
    LinearLayout hideLayout;
    MyListView myListView;
    String status; // y 公开   m 部分隐藏   n 完全隐藏
    OpenSetAdapter openSetAdapter;
    int delPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_resume_open_setting;
    }

    @Override
    protected void initWeightClick() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio0) {
                    status = "y";
                } else if (checkedId == R.id.radio1) {
                    status = "m";
                } else {
                    status = "n";
                }
                setStatus();
            }
        });
    }

    @Override
    protected void initWeight() {
        setTopTitle("简历开放设置");
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        hideLayout = (LinearLayout) findViewById(R.id.hideLayout);
        myListView = (MyListView) findViewById(R.id.myListView);

        openSetAdapter = new OpenSetAdapter(this);
        myListView.setAdapter(openSetAdapter);
        getDataFromServer();
    }

    public void getDataFromServer() {
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_USER_OPENSET, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_USER_OPENSET)) {
            JSONObject jsonObject = (JSONObject) data;
            if (StringUtil.isEmpty(status)) {
                status = jsonObject.optString("openStatus");
                setStatus();
            }
            openSetAdapter.appendToList(jsonObject.optJSONArray("list"));
        } else if (tag.equals(URLS.API_USER_OPENDEL)) {
            openSetAdapter.removeItem(delPosition);
            openSetAdapter.notifyDataSetChanged();
        } else if (tag.equals(URLS.API_USER_OPENUPDATE)) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private void setStatus() {
        if ("y".equals(status)) {
            radioGroup.check(R.id.radio0);
            hideLayout.setVisibility(View.GONE);
        } else if ("m".equals(status)) {
            radioGroup.check(R.id.radio1);
            hideLayout.setVisibility(View.VISIBLE);
        } else {
            radioGroup.check(R.id.radio2);
            hideLayout.setVisibility(View.GONE);
        }
    }

    public void doDelete(View view) {
        delPosition = (int) view.getTag();
        AlertDialogUtil.show(this, R.string.app_name, "您确定删除这个公司吗？", true, "确定", "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                JSONObject jsonObject = openSetAdapter.getItem(delPosition);
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("corpID", jsonObject.optString("memCorpID"));
                LoadingDialog.showDialog(ResumeOpenSettingActivity.this);
                HttpUtil.post(URLS.API_USER_OPENDEL, params, ResumeOpenSettingActivity.this);
            }
        }, null);
    }

    public void doAdd(View view) {
        Intent intent = new Intent(this, AddOpenSettingActivity.class);
        startActivityForResult(intent, 111);
    }

    // 保存更改
    public void doSave(View view) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("openStatus", status);
        LoadingDialog.showDialog(ResumeOpenSettingActivity.this);
        HttpUtil.post(URLS.API_USER_OPENUPDATE, params, ResumeOpenSettingActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            openSetAdapter.clear();
            getDataFromServer();
        }
    }

}
