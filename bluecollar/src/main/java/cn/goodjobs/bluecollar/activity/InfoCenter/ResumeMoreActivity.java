package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.view.View;
import android.widget.Button;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

/**
 * Created by zhuli on 2016/1/8.
 */
public class ResumeMoreActivity extends BaseActivity {

    private SelectorItemView itemDegree, itemCheckIn, itemWorkAddress, itemSalary, itemJobFunc, itemWorktime;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_resume_more;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我的简历");

        itemDegree = (SelectorItemView) findViewById(R.id.itemDegree);
        itemCheckIn = (SelectorItemView) findViewById(R.id.itemCheckIn);
        itemWorkAddress = (SelectorItemView) findViewById(R.id.itemWorkAddress);
        itemSalary = (SelectorItemView) findViewById(R.id.itemSalary);
        itemJobFunc = (SelectorItemView) findViewById(R.id.itemJobFunc);
        itemWorktime = (SelectorItemView) findViewById(R.id.itemWorktime);
    }

    @Override
    protected void initData() {

    }

    public void doSave(View v) {
    }

}
