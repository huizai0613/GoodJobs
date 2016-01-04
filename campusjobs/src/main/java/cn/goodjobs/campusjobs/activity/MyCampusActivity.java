package cn.goodjobs.campusjobs.activity;

import android.os.Bundle;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.baseclass.BaseActivity;

public class MyCampusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_campus);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_campus;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我的校园");
    }

    @Override
    protected void initData() {

    }
}
