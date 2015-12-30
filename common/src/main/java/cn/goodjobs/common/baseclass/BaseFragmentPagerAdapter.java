package cn.goodjobs.common.baseclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 王刚 on 2015/12/15 0015.
 *
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {

    public List<Fragment> fragmentList;

    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
