package cn.goodjobs.common.activity.resume;

import android.os.Bundle;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;

public class MyResumeMoreActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    protected void initData() {

    }
}