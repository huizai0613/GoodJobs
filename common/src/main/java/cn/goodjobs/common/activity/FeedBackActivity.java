package cn.goodjobs.common.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;

/**
 * Created by zhuli on 2016/1/18.
 */
public class FeedBackActivity extends BaseActivity {

    private EditText etTitle, etContent, etName, etMobile;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("意见反馈");
        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);
        etName = (EditText) findViewById(R.id.etName);
        etMobile = (EditText) findViewById(R.id.etMoblie);

        if (!GoodJobsApp.getInstance().isLogin()) {
            etName.setVisibility(View.VISIBLE);
            etMobile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData() {

    }

    public void doCommit(View v) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("fromType", 1);
        if (!GoodJobsApp.getInstance().isLogin()) {
            String name = etName.getText().toString();
            if (StringUtil.isEmpty(name)) {
                TipsUtil.show(this, "请输入您的姓名");
                return;
            }
            params.put("name", name);
            String phone = etMobile.getText().toString();
            if (StringUtil.isEmpty(phone)) {
                TipsUtil.show(this, "请输入您的手机号");
                return;
            }
            params.put("phone", phone);
        }
        String title = etTitle.getText().toString();
        if (StringUtil.isEmpty(title)) {
            TipsUtil.show(this, "请输入标题");
            return;
        }
        params.put("title", title);
        String content = etContent.getText().toString();
        if (StringUtil.isEmpty(content)) {
            TipsUtil.show(this, "请输入反馈内容");
            return;
        }
        params.put("content", content);
        HttpUtil.post(URLS.API_JOB_UserFeedback, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (data instanceof JSONObject) {
            TipsUtil.show(this, ((JSONObject) data).optString("message"));
            finish();
        } else if (data instanceof String) {
            TipsUtil.show(this, (String) data);
            finish();
        }
    }
}
