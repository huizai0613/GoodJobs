package cn.goodjobs.client.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.applyjobs.activity.ApplyJobsActivity;
import cn.goodjobs.campusjobs.activity.CampusActivity;
import cn.goodjobs.client.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.imagemenu.MainMenuEntity;
import cn.goodjobs.common.view.imagemenu.MyImageView;
import cn.goodjobs.headhuntingjob.activity.HeadHuntingActivity;
import cn.goodjobs.parttimejobs.activity.PartJobDetailsActivity;
import cn.goodjobs.headhuntingjob.activity.HeadHuntingActivity;
import cn.goodjobs.parttimejobs.activity.PartTimeJobActivity;

public class MainActivity extends BaseActivity
{

    private long backTime = 2000;
    private long curTime;
    int menuWidth;
    int menuHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
        MyImageView menuQuanzhi = (MyImageView) findViewById(R.id.menuQuanzhi);
        MyImageView menuLanling = (MyImageView) findViewById(R.id.menuLanling);
        MyImageView menuXiaoyuan = (MyImageView) findViewById(R.id.menuXiaoyuan);
        MyImageView menuLietou = (MyImageView) findViewById(R.id.menuLietou);
        MyImageView menuJianzhi = (MyImageView) findViewById(R.id.menuJianzhi);
        LinearLayout lvMenu = (LinearLayout) findViewById(R.id.lv_menu);
        menuWidth = (getWindowManager().getDefaultDisplay().getWidth() - DensityUtil.dip2px(this, 24)) / 2;
        menuHeight = getWindowManager().getDefaultDisplay().getHeight();
        menuHeight = menuHeight - DensityUtil.dip2px(this, 100);
        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, menuHeight);
        lvMenu.setLayoutParams(menuParams);

        MyImageView[] menuImgs = new MyImageView[] { menuQuanzhi, menuLanling, menuXiaoyuan, menuLietou, menuJianzhi};
        for (int i = 0; i < menuImgs.length; ++i) {
            MainMenuEntity curMenuEntity = getMenuEntitys().get(i);
            initMyImageWithStyle(menuImgs[i], curMenuEntity);
            menuImgs[i].curMenuEntity = curMenuEntity;
            menuImgs[i].setOnClickIntent(new MyImageView.OnViewClick() {
                @Override
                public void onClick(MainMenuEntity menuEntity) {
                    if (menuEntity.intent != null) {
                        menuEntity.intent.setClass(MainActivity.this, menuEntity.toClass);
                        startActivity(menuEntity.intent);
                    } else {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, menuEntity.toClass);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void initMyImageWithStyle(MyImageView menuImg, MainMenuEntity menuEntity) {
        View view = LayoutInflater.from(this).inflate(R.layout.menu_layout, null);
        int height = (int) ((menuHeight-menuEntity.padding)*menuEntity.scale);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(menuWidth, height);
        view.setLayoutParams(viewParams);
        menuImg.setLayoutParams(viewParams);
        view.setBackgroundColor(Color.parseColor(menuEntity.color));
        ImageView imgText = (ImageView) view.findViewById(R.id.imgText);
        ImageView imageview = (ImageView) view.findViewById(R.id.imageview);
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(menuEntity.imgW, menuEntity.imgH);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        imageParams.leftMargin = menuWidth - menuEntity.imgW - DensityUtil.dip2px(this, 12);
        imageview.setLayoutParams(imageParams);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(menuWidth - DensityUtil.dip2px(this, 24), RelativeLayout.LayoutParams.WRAP_CONTENT);
        contentParams.addRule(RelativeLayout.BELOW, R.id.tvTitle);
        tvContent.setLayoutParams(contentParams);
        imageview.setImageResource(menuEntity.iconRes);
        if (menuEntity.showNew) {
            imgText.setVisibility(View.VISIBLE);
        }
        tvTitle.setText(menuEntity.title);
        tvContent.setText(menuEntity.content);
        menuImg.setView(view);
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

    public List<MainMenuEntity> getMenuEntitys() {
        List<MainMenuEntity> menuEntitys = new ArrayList<MainMenuEntity>();
        MainMenuEntity menuEntity = null;

        menuEntity = new MainMenuEntity();
        menuEntity.color = "#DB6C5B";
        menuEntity.iconRes = R.mipmap.full_img;
        menuEntity.padding = DensityUtil.dip2px(this, 24);
        menuEntity.scale = 0.6f;
        menuEntity.title = "全职招聘";
        menuEntity.content = "30000家企业、80000热招职位任你搜索，总有一款适合你 ";
        menuEntity.toClass = ApplyJobsActivity.class;
        menuEntity.imgW = DensityUtil.dip2px(this, 55);
        menuEntity.imgH = DensityUtil.dip2px(this, 46);
        menuEntitys.add(menuEntity);

        menuEntity = new MainMenuEntity();
        menuEntity.color = "#4d9ae0";
        menuEntity.iconRes = R.mipmap.blue_img;
        menuEntity.padding = DensityUtil.dip2px(this, 24);
        menuEntity.scale = 0.4f;
        menuEntity.title = "蓝领招聘";
        menuEntity.content = "站着工作，你的辛勤付出必有丰厚回报。";
        menuEntity.toClass = ApplyJobsActivity.class;
        menuEntity.imgW = DensityUtil.dip2px(this, 40);
        menuEntity.imgH = DensityUtil.dip2px(this, 66);
        menuEntitys.add(menuEntity);

        menuEntity = new MainMenuEntity();
        menuEntity.color = "#7150b9";
        menuEntity.iconRes = R.mipmap.liepin_img;
        menuEntity.padding = DensityUtil.dip2px(this, 32);
        menuEntity.scale = 0.36f;
        menuEntity.title = "新安猎聘";
        menuEntity.content = "千军易得，一将难求。\n够分量，你就来这里";
        menuEntity.toClass = HeadHuntingActivity.class;
        menuEntity.imgW = DensityUtil.dip2px(this, 75);
        menuEntity.imgH = DensityUtil.dip2px(this, 45);
        menuEntitys.add(menuEntity);

        menuEntity = new MainMenuEntity();
        menuEntity.color = "#87C500";
        menuEntity.iconRes = R.mipmap.school_img;
        menuEntity.padding = DensityUtil.dip2px(this, 32);
        menuEntity.scale = 0.36f;
        menuEntity.title = "校园招聘";
        menuEntity.content = "追逐梦想，职业生涯第一份工作从这里开始。";
        menuEntity.toClass = CampusActivity.class;
        menuEntity.imgW = DensityUtil.dip2px(this, 54);
        menuEntity.imgH = DensityUtil.dip2px(this, 44);
        menuEntitys.add(menuEntity);

        menuEntity = new MainMenuEntity();
        menuEntity.color = "#FD9B40";
        menuEntity.iconRes = R.mipmap.part_img;
        menuEntity.padding = DensityUtil.dip2px(this, 32);
        menuEntity.scale = 0.28f;
        menuEntity.title = "兼职";
        menuEntity.content = "自由灵活的“第二职业。”";
        menuEntity.toClass = PartTimeJobActivity.class;
        menuEntity.imgW = DensityUtil.dip2px(this, 45);
        menuEntity.imgH = DensityUtil.dip2px(this, 45);
        menuEntitys.add(menuEntity);

        return menuEntitys;
    }
}
