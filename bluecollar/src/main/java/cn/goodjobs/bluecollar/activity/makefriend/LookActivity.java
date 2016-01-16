package cn.goodjobs.bluecollar.activity.makefriend;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsGuanzhuFragment;
import cn.goodjobs.common.baseclass.BaseActivity;

public class LookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_look;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我关注的人");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        MakeFriendsGuanzhuFragment fragment = new MakeFriendsGuanzhuFragment();
        fragment.firstLoad = true;
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    protected void initData() {

    }
}
