package cn.goodjobs.parttimejobs.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.update.UmengUpdateAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.Constant;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.ExpandTabSuper.ExpandTabView;
import cn.goodjobs.common.view.ExpandTabSuper.SingleLevelMenuView;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;
import cn.goodjobs.parttimejobs.R;
import cn.goodjobs.parttimejobs.adapter.PartTimeJobAdapter;

public class PartTimeJobActivity extends BaseListActivity {

    private long backTime = 2000;
    private long curTime;
    private Map<String, String> schoolData = new LinkedHashMap<String, String>();
    private Map<String, String> dateData = new LinkedHashMap<String, String>();
    private SingleLevelMenuView dateInfo, schoolInfo;
    private ExpandTabView etv_career;
    private LinearLayout search;
    private ImageButton btnClear;
    private EditText et;
    private EmptyLayout emptyLayout;
    private RelativeLayout tipLayout;
    private boolean isSuccess = false;
    private String keyword = "", runType = "", schoolType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefUtil.saveDataToLoacl("defaultModule", Constant.module.Jianzhi.toString()); // 保存当前模块为默认模块

        UmengUpdateAgent.update(this); // 检测版本更新
        initView();
        mAdapter = new PartTimeJobAdapter(this);
        initList();
        mListView.setEmptyView(emptyLayout);
        startRefresh();
    }

    private void initView() {
        setTopTitle("兼职信息");
        changeLeftBg(R.mipmap.icon_part);
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        et = (EditText) findViewById(R.id.et_career);
        btnClear = (ImageButton) findViewById(R.id.ib_clear);
        etv_career = (ExpandTabView) findViewById(R.id.etv_career);
        tipLayout = (RelativeLayout) findViewById(R.id.tipLayout);
        search = (LinearLayout) findViewById(R.id.ll_search);
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        search.setOnClickListener(this);
        schoolInfo = new SingleLevelMenuView(this);
        dateInfo = new SingleLevelMenuView(this);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString())) {
                    btnClear.setVisibility(View.VISIBLE);
                } else {
                    btnClear.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnClear.setOnClickListener(this);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("工作时间");
        strings.add("发布日期");
        ArrayList<View> views = new ArrayList<>();
        views.add(dateInfo);
        views.add(schoolInfo);
        ArrayList<Integer> integers = new ArrayList<Integer>();
        int i = DensityUtil.dip2px(this, 45);
        integers.add(5 * i);
        integers.add(9 * i);
        initSearch();
        etv_career.setValue(strings, views, integers);
        schoolInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                schoolType = selectedKey;
                mAdapter.clear();
                startRefresh();
                etv_career.setTitle(showString, 1);
            }
        });
        dateInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                runType = selectedKey;
                mAdapter.clear();
                startRefresh();
                etv_career.setTitle(showString, 0);
            }
        });

        tipLayout.setOnClickListener(this);
        Boolean tipChange = SharedPrefUtil.getBoolean("tipChange5");
        if (tipChange == null || tipChange) {
            tipLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_parttimejob;
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("ttype", schoolType);
        params.put("ptime", runType);
        params.put("keyword", et.getText().toString());
        HttpUtil.post(URLS.API_JOB_ParttimeJob, params, this);
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        mAdapter.appendToList(object.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }

        loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage") > page);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    private void initSearch() {
        JSONArray jarray = (JSONArray) JsonMetaUtil.getObject("parttime_worktime");
        String key1 = "";
        for (int j = 0; j < jarray.length(); j++) {
            JSONObject o = jarray.optJSONObject(j);
            if (j == 0) {
                key1 = o.optString("id");
            }
            dateData.put(o.optString("id"), o.optString("name"));
        }
        dateInfo.setValue(dateData, key1);

        JSONArray array = (JSONArray) JsonMetaUtil.getObject("pubdate");
        String key = "0";
        schoolData.put("0", "日期不限");
        for (int j = 0; j < array.length(); j++) {
            JSONObject o = array.optJSONObject(j);
            schoolData.put(o.optString("id"), o.optString("name"));
        }
        schoolInfo.setValue(schoolData, key);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ll_search) {
            keyword = et.getText().toString();
            mAdapter.clear();
            startRefresh();
        } else if (v.getId() == R.id.ib_clear) {
            et.setText("");
        } else if (v.getId() == R.id.tipLayout) {
            tipLayout.setVisibility(View.INVISIBLE);
            SharedPrefUtil.saveDataToLoacl("tipChange5", false);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        String defaultModule = SharedPrefUtil.getDataFromLoacl("defaultModule"); //默认打开的模块
        if (!StringUtil.isEmpty(defaultModule) && Constant.module.Jianzhi.toString().equals(defaultModule)) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (System.currentTimeMillis() - curTime > backTime) {
                    TipsUtil.show(PartTimeJobActivity.this, R.string.exit_app);
                    curTime = System.currentTimeMillis();
                } else {
                    ScreenManager.getScreenManager().popAllActivityExceptOne(this);
                    onBackPressed();
                }
            }
        } else {
            back();
        }
        return true;
    }
}
