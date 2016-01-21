package cn.goodjobs.client.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import cn.goodjobs.client.R;
import cn.goodjobs.common.activity.HelpActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.http.MetaDataUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

public class LauncherActivity extends BaseActivity {

    Button btnSkip;
    long skipTime = 3000; // 默认停留3秒
    boolean canStart; // 是否可以进入主界面
    long updateMetaDuration = 24 * 3600 * 1000; // 更新数据超时时间，默认1天
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不显示系统的标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        try {
            btnSkip = (Button) findViewById(R.id.btnSkip);
            // 上次更新公共数据的时间， 客户端超过一天更新一次公共数据
            Long updateTime = SharedPrefUtil.getLong("updateMetaTime");
            if (updateTime == null) {
                btnSkip.setVisibility(View.INVISIBLE);
                updateMetaData(true);
            } else {
                if (System.currentTimeMillis() - updateTime > updateMetaDuration) {
                    // 更新超过一天
                    updateMetaData(false);
                } else {
                    btnSkip.setVisibility(View.VISIBLE);
                    canStart = true;
                }
            }
            mHandler = new Handler();
            mHandler.postDelayed(startAppRunnable, skipTime);

            btnSkip.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {

    }

    @Override
    protected void initData() {

    }

    /**
     * 更新公共数据
     *
     * @param flag 是否强制更新，更新失败处理机制
     */
    private void updateMetaData(final boolean flag) {
        MetaDataUtil.getInstanse().initMetaData(new MetaDataUtil.MetaDataDownloadListener() {
            @Override
            public void success() {
                SharedPrefUtil.saveDataToLoacl("updateMetaTime", System.currentTimeMillis());
                btnSkip.setVisibility(View.VISIBLE);
                startApp();
            }

            @Override
            public void failure() {
                if (flag) {
                    AlertDialogUtil.show(LauncherActivity.this, R.string.app_name, "更新公共数据失败，再试一次吧", true, "重试", "退出APP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateMetaData(flag);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ScreenManager.getScreenManager().popActivity();
                        }
                    });
                } else {
                    SharedPrefUtil.saveDataToLoacl("updateMetaTime", System.currentTimeMillis());
                    btnSkip.setVisibility(View.VISIBLE);
                    startApp();
                }
            }
        });
    }

    private void startApp() {
        if (canStart) {
            String versionName = SharedPrefUtil.getDataFromLoacl("versionName");
            if (HttpUtil.getPackageInfo().versionName.equals(versionName)) {
                String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
                Intent intent = new Intent();
                if (StringUtil.isEmpty(defaultModule)) {
                    intent.setClass(LauncherActivity.this, MainActivity.class);
                } else {
                    if (defaultModule.equals(Constant.module.ApplyJobs.toString())) {
                        intent.setClassName(LauncherActivity.this, "cn.goodjobs.applyjobs.activity.ApplyJobsActivity");
                    } else if (defaultModule.equals(Constant.module.Xiaoyuan.toString())) {
                        intent.setClassName(LauncherActivity.this, "cn.goodjobs.campusjobs.activity.CampusActivity");
                    } else if (defaultModule.equals(Constant.module.Liepin.toString())) {
                        intent.setClassName(LauncherActivity.this, "cn.goodjobs.headhuntingjob.activity.HeadHuntingActivity");
                    } else if (defaultModule.equals(Constant.module.Jianzhi.toString())) {
                        intent.setClassName(LauncherActivity.this, "cn.goodjobs.parttimejobs.activity.PartTimeJobActivity");
                    } else if (defaultModule.equals(Constant.module.Lanling.toString())) {
                        intent.setClassName(LauncherActivity.this, "cn.goodjobs.bluecollar.activity.BlueCollarActivity");
                    }
                }
                startActivity(intent);
                return;
            } else {
                SharedPrefUtil.saveDataToLoacl("versionName", HttpUtil.getPackageInfo().versionName);
                Intent intent = new Intent(this, HelpActivity.class);
                startActivity(intent);
                return;
            }

        }
        canStart = true;
    }

    private Runnable startAppRunnable = new Runnable() {
        @Override
        public void run() {
            startApp();
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        canStart = true;
        mHandler.removeCallbacks(startAppRunnable);
        startApp();
    }
}
