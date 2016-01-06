package cn.goodjobs.headhuntingjob.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;

import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.headhuntingjob.R;

/**
 * Created by zhuli on 2016/1/5.
 */
public class ClauseActivity extends BaseActivity {

    private String clause;
    private WebView wv_clause;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_clause;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("推荐人才条款");
        wv_clause = (WebView) findViewById(R.id.wv_clause);
        WebSettings wSet = wv_clause.getSettings();
        wSet.setJavaScriptEnabled(true);
        wv_clause.loadDataWithBaseURL(null, clause, "text/html", "UTF-8", null);
    }

    @Override
    protected void initData() {
        clause = getIntent().getStringExtra("clause");
    }
}
