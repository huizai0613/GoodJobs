package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

/**
 * Created by zhuli on 2016/1/8.
 */
public class ResumeMoreActivity extends BaseActivity {

    private SelectorItemView itemDegree, itemCheckIn, itemWorkAddress, itemSalary, itemJobFunc, itemWorktime;
    private String realname, sex, birthday, cityID, sFunction, autoSend, degree, salary, ckTime, sWorkPlace, jobName, worktime, selfIntro;
    private EditText etContent;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_resume_more;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我的简历");

        itemDegree = (SelectorItemView) findViewById(R.id.itemDegree);
        itemCheckIn = (SelectorItemView) findViewById(R.id.itemCheckIn);
        itemWorkAddress = (SelectorItemView) findViewById(R.id.itemWorkAddress);
        itemSalary = (SelectorItemView) findViewById(R.id.itemSalary);
        itemJobFunc = (SelectorItemView) findViewById(R.id.itemJobFunc);
        itemWorktime = (SelectorItemView) findViewById(R.id.itemWorktime);
        etContent = (EditText) findViewById(R.id.etContent);

        Intent intent = getIntent();
        realname = intent.getStringExtra("realname");
        sex = intent.getStringExtra("sex");
        birthday = intent.getStringExtra("birthday");
        cityID = intent.getStringExtra("cityID");
        sFunction = intent.getStringExtra("sFunction");
        autoSend = intent.getStringExtra("autoSend");
    }

    @Override
    protected void initData() {

    }

    public void doSave(View v) {

        degree = itemDegree.getSelectorIds();
        ckTime = itemCheckIn.getSelectorIds();
        salary = itemSalary.getSelectorIds();
        sWorkPlace = itemWorkAddress.getSelectorIds();
        jobName = itemJobFunc.getSelectorIds();
        worktime = itemWorktime.getSelectorIds();
        selfIntro = etContent.getText().toString();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("cvType", 2);
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
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        TipsUtil.show(this, ((JSONObject) data).optString("message"));
    }
}
