package cn.goodjobs.bluecollar.view.listview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import cn.goodjobs.common.baseclass.BaseActivity;

public class BaseXListViewActivity extends BaseActivity implements XListView.IXListViewListener, OnItemClickListener {
	protected XListView listView;
	protected int pageIndex = 1;
	protected boolean isRefresh = true; // 是否刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getLayoutID() {
		return 0;
	}

	@Override
	protected void initWeightClick() {

	}

	@Override
	protected void initWeight() {

	}

	@Override
	protected void initData() {

	}

	protected void initXListView() {
		listView.setPullLoadEnable(false);
		listView.setPullRefreshEnable(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
		// 加载数据
		listView.setPullRefreshEnable(true);
		listView.startRefreshing();
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		isRefresh = true;
        getDataFromServer();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		isRefresh = false;
        getDataFromServer();
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {

	}

	protected void getDataFromServer() {

	}
}
