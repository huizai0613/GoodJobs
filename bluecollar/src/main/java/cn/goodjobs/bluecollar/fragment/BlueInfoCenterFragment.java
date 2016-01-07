package cn.goodjobs.bluecollar.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlueInfoCenterFragment extends BaseFragment {


    public BlueInfoCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blue_info_center, container, false);
    }


}
