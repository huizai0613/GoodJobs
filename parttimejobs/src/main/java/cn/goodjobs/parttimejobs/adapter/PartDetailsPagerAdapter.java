package cn.goodjobs.parttimejobs.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.parttimejobs.fragment.PartJobDetailsFragment;

/**
 * Created by zhuli on 2015/12/26.
 */
public class PartDetailsPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    public List<Integer> data;

    public PartDetailsPagerAdapter(FragmentManager fm, List<Integer> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        PartJobDetailsFragment partJobDetailsFragment = new PartJobDetailsFragment(data.get(position), position);
        fragmentList.add(partJobDetailsFragment);
        return partJobDetailsFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        ((PartJobDetailsFragment) object).onDetach();
        fragmentList.remove(((PartJobDetailsFragment) object));

    }

    @Override
    public int getCount() {
        return data.size();
    }

}
