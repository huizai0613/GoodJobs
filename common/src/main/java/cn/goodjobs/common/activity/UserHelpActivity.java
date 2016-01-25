package cn.goodjobs.common.activity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import org.json.JSONArray;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.HelpAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

public class UserHelpActivity extends BaseActivity {
    ExpandableListView expandListView;
    HelpAdapter helpAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_user_help;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("使用帮助");
        expandListView = (ExpandableListView) findViewById(R.id.expandListView);
        expandListView.setGroupIndicator(null); // 去掉默认箭头
        helpAdapter = new HelpAdapter(this);
        expandListView.setAdapter(helpAdapter);
        getDataFromServer();
    }

    @Override
    protected void initData() {

    }

    private void getDataFromServer() {
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.HELP, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        helpAdapter.jsonArray = (JSONArray) data;
        helpAdapter.notifyDataSetChanged();
    }
}
