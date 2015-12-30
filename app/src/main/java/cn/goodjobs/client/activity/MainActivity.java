package cn.goodjobs.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;

import cn.goodjobs.applyjobs.activity.ApplyJobsActivity;
import cn.goodjobs.campusjobs.activity.CampusActivity;
import cn.goodjobs.client.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.headhuntingjob.activity.HeadHuntingActivity;
import cn.goodjobs.parttimejobs.activity.PartJobDetailsActivity;
import cn.goodjobs.headhuntingjob.activity.HeadHuntingActivity;
import cn.goodjobs.parttimejobs.activity.PartTimeJobActivity;

public class MainActivity extends BaseActivity
{

    private long backTime = 2000;
    private long curTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTopTitle("新安人才网");
        ImageButton backBtn = (ImageButton) findViewById(R.id.btn_left);
        backBtn.setVisibility(View.INVISIBLE);
    }


    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_main;
    }

    @Override
    protected void initWeightClick()
    {

    }

    @Override
    protected void initWeight()
    {

    }

    @Override
    protected void initData()
    {

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - curTime > backTime) {
                TipsUtil.show(MainActivity.this, R.string.exit_app);
                curTime = System.currentTimeMillis();
            } else {
                ScreenManager.getScreenManager().popAllActivityExceptOne(this);
                onBackPressed();
            }
        }
        return true;
    }

    public void menuClick(View view)
    {
        String tag = (String) view.getTag();
        Intent intent = new Intent();
        switch (tag) {
            case "0":
                intent.setClass(this, ApplyJobsActivity.class);
                break;
            case "1":
                intent.setClass(this, CampusActivity.class);
                break;
            case "2":
                intent.setClass(this, HeadHuntingActivity.class);
                break;
            case "3":
                intent.setClass(this, PartTimeJobActivity.class);
                break;
        }
        startActivity(intent);
    }
}
