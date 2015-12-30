package cn.goodjobs.headhuntingjob.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.ListInputItemView;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.headhuntingjob.R;

/**
 * Created by zhuli on 2015/12/25.
 */
public class RecommendActivity extends BaseActivity {

    private ListInputItemView realName, mobile, bName, bAge, bPhone, bMobile, bEmail, bIndustry, bNowjob, bOther;
    private EditText bJobhistory;
    private RadioButton man, woman;
    private CheckBox point;
    private Button commit;
    private String youName, number;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_recommend;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("推荐人才");
        point = (CheckBox) findViewById(R.id.cb_point);
        man = (RadioButton) findViewById(R.id.rb_man);
        woman = (RadioButton) findViewById(R.id.rb_woman);
        bJobhistory = (EditText) findViewById(R.id.et_bJobhistory);
        realName = (ListInputItemView) findViewById(R.id.liv_realName);
        mobile = (ListInputItemView) findViewById(R.id.liv_mobile);
        bName = (ListInputItemView) findViewById(R.id.liv_bName);
        bAge = (ListInputItemView) findViewById(R.id.liv_bAge);
        bPhone = (ListInputItemView) findViewById(R.id.liv_bPhone);
        bMobile = (ListInputItemView) findViewById(R.id.liv_bMobile);
        bEmail = (ListInputItemView) findViewById(R.id.liv_bEmail);
        bIndustry = (ListInputItemView) findViewById(R.id.liv_bIndustry);
        bNowjob = (ListInputItemView) findViewById(R.id.liv_bNowjob);
        bOther = (ListInputItemView) findViewById(R.id.liv_bOther);
        commit = (Button) findViewById(R.id.btn_commit);
        commit.setEnabled(false);
        commit.setOnClickListener(this);
        point.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    commit.setEnabled(true);
                } else {
                    commit.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initData() {
        id = getIntent().getIntExtra("jobID", -1);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("jobID", id);
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_JOB_RecommendData, params, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_commit) {
            if (chooseCondition()) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("jobID", id);
                params.put("realName", realName.getText());
                params.put("mobile", mobile.getText());
                params.put("bName", bName.getText());
                params.put("bAge", bAge.getText());
                params.put("bPhone", bPhone.getText());
                params.put("bMobile", bMobile.getText());
                params.put("bEmail", bEmail.getText());
                params.put("bIndustry", bIndustry.getText());
                params.put("bNowjob", bNowjob.getText());
                params.put("bOther", bOther.getText());
                params.put("bJobhistory", bJobhistory.getText().toString());
                if (man.isChecked()) {
                    params.put("bGender", "男");
                } else if (woman.isChecked()) {
                    params.put("bGender", "女");
                }
                LoadingDialog.showDialog(this);
                HttpUtil.post(URLS.API_JOB_Recommendsave, params, this);
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_JOB_RecommendData)) {
            JSONObject object = (JSONObject) data;
            youName = object.optString("realName");
            number = object.optString("mobile");

            if (youName.length() != 0 && number.length() != 0) {
                realName.setText(youName);
                mobile.setText(number);
            }
        } else if (tag.equals(URLS.API_JOB_Recommendsave)) {
            TipsUtil.show(this, (String) data);
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }

    public boolean chooseCondition() {

        if (realName.getText().length() <= 1) {
            TipsUtil.show(this, "您的姓名项不能少于两个字符");
            return false;
        }
        if (mobile.getText().length() != 11 && mobile.getText().startsWith("1")) {
            TipsUtil.show(this, "您的手机项不符合号码格式");
            return false;
        }
        if (bName.getText().length() <= 1) {
            TipsUtil.show(this, "姓名项不能少于两个字符");
            return false;
        }
        if (bMobile.getText().length() != 11 && bMobile.getText().startsWith("1")) {
            TipsUtil.show(this, "手机项不符合号码格式");
            return false;
        }
        if (bMobile.getText().length() <= 1) {
            TipsUtil.show(this, "目前职位项不能少于两个字符");
            return false;
        }
        return true;
    }

}
