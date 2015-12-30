package cn.goodjobs.headhuntingjob.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.headhuntingjob.fragment.HeadDetailsFragment;


/**
 * Created by zhuli on 2015/12/26.
 */
public class HeadDetailsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<Integer> data;

    public HeadDetailsPagerAdapter(FragmentManager fm, List<Integer> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        HeadDetailsFragment headDetailsFragment = new HeadDetailsFragment(data.get(position), position);
        fragmentList.add(headDetailsFragment);
        return headDetailsFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        ((HeadDetailsFragment) object).onDetach();
        fragmentList.remove(((HeadDetailsFragment) object));

    }

    @Override
    public int getCount() {
        return data.size();
    }

}