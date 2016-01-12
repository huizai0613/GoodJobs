package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.view.View;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.view.searchItem.SearchItemView;

/**
 * Created by zhuli on 2016/1/7.
 */
public class ItemMoreActivity extends BaseActivity {

    private SearchItemView itemGuide, itemFeedBack, itemClear, itemCheck, itemHelp, itemAbout;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_more;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("更多");

        itemGuide = (SearchItemView) findViewById(R.id.itemGuide);
        itemFeedBack = (SearchItemView) findViewById(R.id.itemFeedBack);
        itemClear = (SearchItemView) findViewById(R.id.itemClear);
        itemCheck = (SearchItemView) findViewById(R.id.itemCheck);
        itemHelp = (SearchItemView) findViewById(R.id.itemHelp);
        itemAbout = (SearchItemView) findViewById(R.id.itemAbout);

        itemGuide.setOnClickListener(this);
        itemFeedBack.setOnClickListener(this);
        itemClear.setOnClickListener(this);
        itemCheck.setOnClickListener(this);
        itemHelp.setOnClickListener(this);
        itemAbout.setOnClickListener(this);

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
