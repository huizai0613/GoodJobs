package cn.goodjobs.campusjobs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.adapter.CareerDetailsPagerAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CareerTalkDetailsActivity extends BaseActivity {


    public ViewPager vp;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<Integer> data = new ArrayList<Integer>();
    private int item;
    public JSONObject[] jsonObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_careerdetails;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("宣讲会详情");
        vp = (ViewPager) findViewById(R.id.vp_careerdetails);
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new CareerDetailsPagerAdapter(getSupportFragmentManager(), data));
        vp.setCurrentItem(item);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        item = intent.getIntExtra("item", -1);
        data = intent.getIntegerArrayListExtra("idList");

        jsonObjects = new JSONObject[data.size()];
    }


}
