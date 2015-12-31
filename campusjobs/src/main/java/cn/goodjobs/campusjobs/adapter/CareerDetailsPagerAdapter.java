package cn.goodjobs.campusjobs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.campusjobs.fragment.CareerDetailsFragment;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CareerDetailsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<Integer> data;

    public CareerDetailsPagerAdapter(FragmentManager fm, List<Integer> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        CareerDetailsFragment partJobDetailsFragment = new CareerDetailsFragment(data.get(position), position);
        fragmentList.add(partJobDetailsFragment);
        return partJobDetailsFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        ((CareerDetailsFragment) object).onDetach();
        fragmentList.remove(((CareerDetailsFragment) object));

    }

    @Override
    public int getCount() {
        return data.size();
    }

}

