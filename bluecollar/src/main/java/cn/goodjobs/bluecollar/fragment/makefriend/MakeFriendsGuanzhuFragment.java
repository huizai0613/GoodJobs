package cn.goodjobs.bluecollar.fragment.makefriend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.util.LogUtil;

/**
 * 同城
 */
public class MakeFriendsGuanzhuFragment extends BaseListFragment {
    View view;

    public MakeFriendsGuanzhuFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.load_more_list_view, container, false);
        return view;
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("MakeFriendsGuanzhuFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(!isLoad) {
                loadView();
                isLoad=true;
            }
        }
    }

    private void loadView() {
        initList(view);
    }

}
