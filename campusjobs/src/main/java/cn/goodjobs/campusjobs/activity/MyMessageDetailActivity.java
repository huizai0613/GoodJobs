package cn.goodjobs.campusjobs.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

public class MyMessageDetailActivity extends BaseActivity {

    TextView tvCorpName, tvTime, tvTitle, tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_message_detail;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("信息内容");
        tvCorpName = (TextView) findViewById(R.id.tvCorpName);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvContent = (TextView) findViewById(R.id.tvContent);

        getDataFromServer();
    }

    private void getDataFromServer() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("messageID", getIntent().getStringExtra("messageID"));
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.CAMPUS_MSGSHOW, params, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject jsonObject = (JSONObject) data;
        tvCorpName.setText("单位名称："+jsonObject.optString("fromComName"));
        tvTime.setText("收件日期："+jsonObject.optString("saveDate"));
        tvTitle.setText("信件标题："+jsonObject.optString("title"));
        tvContent.setText(Html.fromHtml(jsonObject.optString("content")));
    }

    public void toCrop(View view) {

    }
}
