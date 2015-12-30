package cn.goodjobs.applyjobs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

public class LoadMoreListViewFragment extends BaseListFragment {
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // set up views
        final View view = inflater.inflate(R.layout.fragment_load_more_list_view, null);

        mAdapter = new MyAdapter(getActivity());
        // list view
        mListView = (ListView) view.findViewById(R.id.load_more_small_image_list_view);

        initList(view, mListView);
        startRefresh();
        return view;
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> strings = new ArrayList<String>();
//                for (int i = 0; i < 40; i++) {
//                    strings.add("" + i);
//                }
                mAdapter.appendToList(strings);
                loadMoreListViewContainer.loadMoreFinish(false, true);
                mPtrFrameLayout.refreshComplete();
            }
        }, 2000);
    }

    class MyAdapter extends JsonArrayAdapterBase<String> {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        protected View getExView(int position, View convertView, ViewGroup parent) {
            TextView view = new TextView(getContext());
            view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            view.setText(getItem(position));
            return view;
        }
    }

}
