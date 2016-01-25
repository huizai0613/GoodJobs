package cn.goodjobs.common.activity;

import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

/**
 * Created by zhuli on 2016/1/22.
 */
public class AboutActivity extends BaseActivity {

    private TextView tvVersion;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_about;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("关于我们");
        tvVersion = (TextView) findViewById(R.id.tv_version);
        tvVersion.setText("版本号:  " + SharedPrefUtil.getDataFromLoacl("versionName"));
    }

    @Override
    protected void initData() {

    }
}
