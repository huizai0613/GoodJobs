package cn.goodjobs.bluecollar.fragment.makefriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.view.LoadingDialog;

/**
 * 附近的人
 */
public class MakeFriendsNearFragment extends BaseListFragment {
    View view;

    public MakeFriendsNearFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.load_more_list_view, container, false);
        initList(view);
        return view;
    }

    private void loadView() {
        initList(view);
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        LoadingDialog.showDialog(getActivity());
    }
}
