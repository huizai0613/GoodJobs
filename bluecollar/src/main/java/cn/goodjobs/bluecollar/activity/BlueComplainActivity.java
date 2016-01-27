package cn.goodjobs.bluecollar.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;

/**
 * Created by yexiangyu on 16/1/14.
 */
public class BlueComplainActivity extends BaseActivity
{

    private String cropName;
    private String jobName;
    private String blueJobID;
    private EditText content;
    private Button btnSubmit;
    private CheckBox radio0;
    private CheckBox radio1;
    private CheckBox radio2;
    private String radio = "";
    private int maxContent = 200;
    private TextView text;

    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_complain;
    }

    @Override
    protected void initWeightClick()
    {
        content.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                int length = s.length();
                text.setText(maxContent - length + "/" + maxContent);

            }
        });
    }

    @Override
    protected void initWeight()
    {
        setTopTitle("我要投诉");
        TextView name = (TextView) findViewById(R.id.name);
        text = (TextView) findViewById(R.id.text);
        content = (EditText) findViewById(R.id.content);
        radio0 = (CheckBox) findViewById(R.id.rb1);
        radio1 = (CheckBox) findViewById(R.id.rb2);
        radio2 = (CheckBox) findViewById(R.id.rb3);
        findViewById(R.id.btnSubmit).setOnClickListener(this);
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckBox cur = (CheckBox) v;

                if (cur.isChecked()) {
                    int checkedRadioButtonId = v.getId();
                    if (checkedRadioButtonId == R.id.rb1) {
                        radio1.setChecked(false);
                        radio2.setChecked(false);
                        radio = "虚假招聘";
                    } else if (checkedRadioButtonId == R.id.rb2) {
                        radio0.setChecked(false);
                        radio2.setChecked(false);
                        radio = "额外收费";
                    } else if (checkedRadioButtonId == R.id.rb3) {
                        radio0.setChecked(false);
                        radio1.setChecked(false);
                        radio = "过期信息";
                    }
                } else {
                    radio = "";
                }


            }
        };
        radio0.setOnClickListener(onClickListener);
        radio1.setOnClickListener(onClickListener);
        radio2.setOnClickListener(onClickListener);


        name.setText(cropName);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();

        if (id == R.id.btnSubmit) {
            String s = content.getText().toString();
            if (StringUtil.isEmpty(radio) && StringUtil.isEmpty(s)) {
                TipsUtil.show(mcontext, "请填写您要投诉的内容");
                return;
            }
            String info = "职位\"" + jobName + "\"(职位ID:" + blueJobID + ")," + radio + "," + s + ")";
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();

            stringObjectHashMap.put("fromType", 1);
            try {
                stringObjectHashMap.put("content", URLEncoder.encode(info, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            HttpUtil.post(URLS.API_BLUEJOB_Feedback, stringObjectHashMap, this);
        }

    }


    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        TipsUtil.show(mcontext, "感谢您的反馈,我们会尽快对您的反馈进行处理!");
        finish();
    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);

    }

    @Override
    protected void initData()
    {
        Intent intent = getIntent();

        cropName = intent.getStringExtra("cropName");
        jobName = intent.getStringExtra("jobName");
        blueJobID = intent.getStringExtra("blueJobID");
    }
}
