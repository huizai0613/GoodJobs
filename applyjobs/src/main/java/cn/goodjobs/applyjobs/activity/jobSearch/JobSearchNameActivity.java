package cn.goodjobs.applyjobs.activity.jobSearch;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.UpdateDataTaskUtils;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.SegmentView;
import cn.goodjobs.common.view.stickylistheaders.ExpandableStickyListHeadersListView;
import cz.msebera.android.httpclient.Header;


public class JobSearchNameActivity extends BaseActivity implements SegmentView.onSegmentViewClickListener, UpdateDataTaskUtils.OnGetDiscussHistoryListener
{
    private MyLocation myLocation;
    private EditText searchBoxET;
    private Button searchBoxBUT;
    private SegmentView searchSwitch;
    private ExpandableStickyListHeadersListView listview;
    private TextView searchTitle;
    private LinearLayout searchContent;
    private TextView btnClear;
    private TextView btnFinish;
    private String key = "searchData";
    private String data;
    private String add;
    private String sal;
    private String job;
    private String ind;
    private String wt;
    private String deg;
    private HashMap hashMap;
    private Map<Long, Map<String, String>> history;
    private ArrayList<String> keyWorld = new ArrayList<>();
    int curSearchNum;
    android.os.Handler handler = new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            refreshHistory((List<Map.Entry<Long, Map<String, String>>>) msg.obj);

        }
    };
    private boolean isLoad;
    private String addId;
    private String salId;
    private String jobId;
    private String indId;
    private String wtId;
    private String degId;
    private String searchKeyWorld;
    private String searchName;
    private String searchID;

    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_searchname_jobs;
    }

    @Override
    protected void initWeightClick()
    {
        searchBoxBUT.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        searchSwitch.setOnSegmentViewClickListener(this);

        searchBoxET.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override


            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String trim = searchBoxET.getText().toString().trim();
                    if (StringUtil.isEmpty(trim)) {
                        TipsUtil.show(getBaseContext(), "关键词不可为空");
                        return false;
                    }
                    saveKeyWord(trim);
                    //跳转到搜索列表
                    hideSoftInputFromWindow();
                    JumpViewUtil.openActivityAndParam(mcontext, JobSearchResultActivity.class, hashMap);
                }
                return false;
            }
        });

        searchBoxET.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }


            @Override
            public void afterTextChanged(Editable s)
            {
                int last = ++curSearchNum;
                if (s.length() > 0) {
                    final String searchKey = s.toString();
                    HashMap<String, Object> requestParams = new HashMap<String, Object>();
                    requestParams.put("keyword", searchKey);
                    HttpUtil.post(URLS.API_JOB_SearchChextended, last + "", requestParams, JobSearchNameActivity.this);
                } else {
                    //没有关键字,显示搜索历史
                    UpdateDataTaskUtils.getHistory(mcontext, UpdateDataTaskUtils.SEARCHJOB, JobSearchNameActivity.this);
                }
            }
        });
    }


    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        if ((curSearchNum + "").equals(tag)) {
            JSONArray result = ((JSONObject) data).optJSONArray("HotWords");
            searchTitle.setText("相关关键字");
            if (result != null && result.length() > 0) {
                addSearchDatra(result);
            } else {
                //没有搜索到
                noSearchData();
            }
        }


    }

    private void noSearchData()
    {
        searchContent.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 30));
        layoutParams.setMargins(DensityUtil.dip2px(this, 10), 0, DensityUtil.dip2px(this, 10), 0);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        TextView view = new TextView(this);
        view.setText("暂无关键字匹配");
        view.setGravity(Gravity.CENTER_VERTICAL);
        searchContent.addView(view, layoutParams);
        View line = new View(this);
        line.setBackgroundResource(R.color.line_color);
        searchContent.addView(line, lineParams);
    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);
        noSearchData();
    }

    private void addSearchDatra(JSONArray result)
    {
        searchContent.removeAllViews();
        btnClear.setClickable(false);
        btnClear.setTextColor(Color.parseColor("#cccccc"));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 30));
        layoutParams.setMargins(DensityUtil.dip2px(this, 10), 0, DensityUtil.dip2px(this, 10), 0);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        boolean isEmp = true;
        for (int i = 0; i < result.length(); i++) {
            JSONObject jsonObject = result.optJSONObject(i);
            final String searchKeyWorld = jsonObject.optString("Word");
            if (!StringUtil.isEmpty(searchKeyWorld)) {
                isEmp = false;
                TextView view = new TextView(this);
                view.setText(searchKeyWorld);
                view.setGravity(Gravity.CENTER_VERTICAL);
                searchContent.addView(view, layoutParams);
                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //跳转搜索列表
                        hashMap.put("searchKeyWorld", searchKeyWorld);
                        saveSearchLock(history, hashMap);
                        JumpViewUtil.openActivityAndParam(mcontext, JobSearchResultActivity.class, hashMap);
                    }
                });
                View line = new View(this);
                line.setBackgroundResource(R.color.line_color);
                searchContent.addView(line, lineParams);
            }
        }


    }


    @Override
    protected void initWeight()
    {
        searchBoxET = (EditText) findViewById(R.id.search_box_et);
        searchBoxBUT = (Button) findViewById(R.id.search_box_but);
        btnClear = (TextView) findViewById(R.id.btn_clear);
        btnFinish = (TextView) findViewById(R.id.btn_finish);
        searchTitle = (TextView) findViewById(R.id.search_title);
        searchContent = (LinearLayout) findViewById(R.id.search_content);
        searchSwitch = (SegmentView) findViewById(R.id.search_switch);


        searchSwitch.setSegmentText("搜全文", 0);
        searchSwitch.setSegmentText("搜公司名", 1);
        searchSwitch.setSegmentText("搜职位名", 2);

        searchSwitch.perClick(0);
    }

    //刷新搜索历史
    private void refreshHistory(List<Map.Entry<Long, Map<String, String>>> msg)
    {
        searchContent.removeAllViews();
        searchTitle.setText("搜索历史记录");
        if (msg != null && msg.size() > 0) {
            btnClear.setTextColor(Color.parseColor("#5f9bf8"));
            btnClear.setClickable(true);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 30));
            layoutParams.setMargins(DensityUtil.dip2px(this, 10), 0, DensityUtil.dip2px(this, 10), 0);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            boolean isEmp = true;
            for (Map.Entry<Long, Map<String, String>> longMapEntry : msg) {
                Map<String, String> value = longMapEntry.getValue();
                final String searchKeyWorld = value.get("searchKeyWorld");
                if (!StringUtil.isEmpty(searchKeyWorld) && !keyWorld.contains(searchKeyWorld)) {
                    keyWorld.add(searchKeyWorld);
                    isEmp = false;
                    TextView view = new TextView(this);
                    view.setText(searchKeyWorld);
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    searchContent.addView(view, layoutParams);
                    view.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            //跳转搜索列表
                            hashMap.put("searchKeyWorld", searchKeyWorld);
                            saveSearchLock(history, hashMap);
                            JumpViewUtil.openActivityAndParam(mcontext, JobSearchResultActivity.class, hashMap);
                        }
                    });
                    View line = new View(this);
                    line.setBackgroundResource(R.color.line_color);
                    searchContent.addView(line, lineParams);
                }
            }
            if (isEmp) {
                btnClear.setClickable(false);
                btnClear.setTextColor(Color.parseColor("#cccccc"));
            }
            keyWorld.clear();
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 30));
            layoutParams.setMargins(DensityUtil.dip2px(this, 10), 0, DensityUtil.dip2px(this, 10), 0);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            TextView view = new TextView(this);
            view.setText("暂无关键字搜索历史");
            view.setGravity(Gravity.CENTER_VERTICAL);
            searchContent.addView(view, layoutParams);
            View line = new View(this);
            line.setBackgroundResource(R.color.line_color);
            searchContent.addView(line, lineParams);
            btnClear.setClickable(false);
            btnClear.setTextColor(Color.parseColor("#cccccc"));
        }
    }

    @Override
    protected void initData()
    {
        Intent intent = getIntent();
        add = intent.getStringExtra("itemAddress");
        sal = intent.getStringExtra("itemSalary");
        job = intent.getStringExtra("itemJobfunc");
        ind = intent.getStringExtra("itemIndtype");
        wt = intent.getStringExtra("itemWorktime");
        deg = intent.getStringExtra("itemDegree");
        searchName = intent.getStringExtra("searchName");
        searchID = intent.getStringExtra("searchID");

        searchKeyWorld = intent.getStringExtra("searchKeyWorld");
        addId = intent.getStringExtra("itemAddressId");
        salId = intent.getStringExtra("itemSalaryId");
        jobId = intent.getStringExtra("itemJobfuncId");
        indId = intent.getStringExtra("itemIndtypeId");
        wtId = intent.getStringExtra("itemWorktimeId");
        degId = intent.getStringExtra("itemDegreeId");

        hashMap = new HashMap();
        if (!StringUtil.isEmpty(add)) {
            hashMap.put("itemAddress", add);
        }

        if (!StringUtil.isEmpty(searchName)) {
            hashMap.put("searchName", searchName);
        }

        if (!StringUtil.isEmpty(searchID)) {
            hashMap.put("searchID", searchID);
        }

        if (!StringUtil.isEmpty(sal)) {
            hashMap.put("itemSalary", sal);
        }
        if (!StringUtil.isEmpty(job)) {
            hashMap.put("itemJobfunc", job);
        }
        if (!StringUtil.isEmpty(ind)) {
            hashMap.put("itemIndtype", ind);
        }
        if (!StringUtil.isEmpty(wt)) {
            hashMap.put("itemWorktime", wt);
        }
        if (!StringUtil.isEmpty(deg)) {
            hashMap.put("itemDegree", deg);
        }

        if (!StringUtil.isEmpty(addId)) {
            hashMap.put("itemAddressId", addId);
        }
        if (!StringUtil.isEmpty(salId)) {
            hashMap.put("itemSalaryId", salId);
        }
        if (!StringUtil.isEmpty(jobId)) {
            hashMap.put("itemJobfuncId", jobId);
        }
        if (!StringUtil.isEmpty(indId)) {
            hashMap.put("itemIndtypeId", indId);
        }
        if (!StringUtil.isEmpty(wtId)) {
            hashMap.put("itemWorktimeId", wtId);
        }
        if (!StringUtil.isEmpty(degId)) {
            hashMap.put("itemDegreeId", degId);
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (searchBoxET.getText().length() <= 0) {
            UpdateDataTaskUtils.getHistory(mcontext, UpdateDataTaskUtils.SEARCHJOB, this);
        }

    }

    @Override
    public void onClick(View v)
    {
        int i = v.getId();
        if (i == R.id.search_box_but) {//点击搜索按钮
            String trim = searchBoxET.getText().toString().trim();
            if (StringUtil.isEmpty(trim)) {
                TipsUtil.show(getBaseContext(), "关键词不可为空");
                return;
            }
            saveKeyWord(trim);
            //跳转到搜索列表
            hideSoftInputFromWindow();
            JumpViewUtil.openActivityAndParam(this, JobSearchResultActivity.class, hashMap);
        } else if (i == R.id.btn_clear) {//点击清除历史记录
            UpdateDataTaskUtils.cleanHistory(mcontext, UpdateDataTaskUtils.SEARCHJOB);
            history.clear();
            refreshHistory(null);
        } else if (i == R.id.btn_finish) {//点击关闭界面
            back();
        }
    }

    //保存搜索记录
    private void saveKeyWord(String trim)
    {
        if (history == null) {
            history = new HashMap<>();
        }
        hashMap.put("searchKeyWorld", trim);
        saveSearchLock(history, hashMap);
    }

    @Override
    public void onSegmentViewClick(View v, int position)
    {
        switch (position) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

    public void saveSearchLock(Map<Long, Map<String, String>> saveData, Map<String, String> put)
    {
        if (saveData == null) {
            saveData = new HashMap<>();
        }

        Set<Map.Entry<Long, Map<String, String>>> entries = saveData.entrySet();

        ArrayList<Long> keys = new ArrayList<>();
        for (Map.Entry<Long, Map<String, String>> en : entries) {
            boolean isEqu = true;
            Map<String, String> value = en.getValue();
            Set<Map.Entry<String, String>> entries1 = value.entrySet();
            for (Map.Entry<String, String> e : entries1) {
                if (!e.getValue().equals(put.get(e.getKey())) || entries1.size() != put.size()) {
                    isEqu = false;
                    break;
                }
            }
            if (isEqu) {
                keys.add(en.getKey());
            }
        }
        for (Long key : keys) {
            saveData.remove(key);
        }

        saveData.put(System.currentTimeMillis(), put);
        UpdateDataTaskUtils.updateHistory(mcontext, saveData, UpdateDataTaskUtils.SEARCHJOB);
    }


    @Override
    public void onGetDiscussHistory(Map<Long, Map<String, String>> history)
    {
        this.history = history;

        List<Map.Entry<Long, Map<String, String>>> infoIds =
                new ArrayList<Map.Entry<Long, Map<String, String>>>(history.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<Long, Map<String, String>>>()
        {
            @Override
            public int compare(Map.Entry<Long, Map<String, String>> lhs, Map.Entry<Long, Map<String, String>> rhs)
            {
                return (rhs.getKey()).compareTo(lhs.getKey());
            }
        });
        Message message = new Message();
        message.obj = infoIds;
        handler.sendMessage(message);
    }

}
