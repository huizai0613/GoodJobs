package cn.goodjobs.applyjobs.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Handler;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.activity.jobSearch.JobSearchNameActivity;
import cn.goodjobs.applyjobs.activity.jobSearch.JobSearchResultActivity;
import cn.goodjobs.common.AndroidBUSBean;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.UpdateDataTaskUtils;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.SegmentView;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

/**
 * 职位搜索
 */
public class JobSearchFragment extends BaseFragment implements SegmentView.onSegmentViewClickListener, UpdateDataTaskUtils.OnGetDiscussHistoryListener
{
    boolean isLoad; // fragment是否已经加载
    private SelectorItemView itemAddress;
    private SelectorItemView itemJobfunc;
    private SelectorItemView itemIndtype;
    private SelectorItemView itemSalary;
    private SelectorItemView itemWorktime;
    private SelectorItemView itemDegree;


    private MyLocation myLocation;
    private SegmentView searchSwitch;
    private String add;
    private String sal;
    private String job;
    private String ind;
    private String wt;
    private String deg;
    private Button btnSearch;
    private EditText etSearch;
    private String searchKeyWorld;
    private Map<Long, Map<String, String>> history;

    private View searchTitle;
    private EditText searchContent;
    private View addSearchBox;
    private CheckBox searchCheck;
    private JSONArray searchList;

    private String searchId = "";
    private String searchName = "";
    private int curSearchPosition;
    private boolean isRefresh;
    private View clean;


    @Subscriber(tag = URLS.JOB_search_login)
    protected void acceptEventBus(AndroidBUSBean androidBUSBean)
    {
        int status = androidBUSBean.getStatus();

        switch (status) {
            case AndroidBUSBean.STATUSREFRESH:
                //登陆成功刷新
                butLogin.setVisibility(View.GONE);
                addSearchBox.setVisibility(View.VISIBLE);
                getSearchData();
                break;
        }
    }

    android.os.Handler handler = new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            disPlayerSearchUI((List<Map.Entry<Long, Map<String, String>>>) msg.obj);

        }
    };
    private LinearLayout searchHeistory;
    private LinearLayout searchHeistoryLogin;
    private TextView butLogin;

    //最近搜索记录
    private void disPlayerSearchUI(List<Map.Entry<Long, Map<String, String>>> obj)
    {
        searchHeistory.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(DensityUtil.dip2px(mActivity, 10), 0, DensityUtil.dip2px(mActivity, 10), 0);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        if (obj != null && obj.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Long, Map<String, String>> longMapEntry : obj) {
                final Map<String, String> value = longMapEntry.getValue();
                TextView view = new TextView(mActivity);

                builder.delete(0, builder.length());

                builder.append(StringUtil.isEmpty(value.get("searchKeyWorld")) ? "" : value.get("searchKeyWorld") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemAddress")) ? "" : value.get("itemAddress") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemJobfunc")) ? "" : value.get("itemJobfunc") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemIndtype")) ? "" : value.get("itemIndtype") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemSalary")) ? "" : value.get("itemSalary") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemWorktime")) ? "" : value.get("itemWorktime") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemDegree")) ? "" : value.get("itemDegree") + " + ");

                if (builder.length() == 0) {
                    continue;
                }
                CharSequence charSequence = builder.subSequence(0, builder.length() - 3);
                view.setPadding(0, 20, 0, 20);
                view.setSingleLine();
                view.setEllipsize(TextUtils.TruncateAt.END);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                view.setText(charSequence);
                view.setBackgroundResource(R.drawable.list_item_bg);
                view.setGravity(Gravity.CENTER_VERTICAL);
                searchHeistory.addView(view, layoutParams);
                view.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //填充数据
                        String itemAddressId = value.get("itemAddressId");
                        String itemAddressS = value.get("itemAddress");
                        String searchKeyWorld = value.get("searchKeyWorld");

                        String itemJobfuncId = value.get("itemJobfuncId");
                        String itemIndtypeId = value.get("itemIndtypeId");
                        String itemSalaryId = value.get("itemSalaryId");
                        String itemWorktimeId = value.get("itemWorktimeId");
                        String itemDegreeId = value.get("itemDegreeId");


                        String itemJobfuncStr = value.get("itemJobfunc");
                        String itemIndtypeStr = value.get("itemIndtype");
                        String itemSalaryStr = value.get("itemSalary");
                        String itemWorktimeStr = value.get("itemWorktime");
                        String itemDegreeStr = value.get("itemDegree");

                        if (!StringUtil.isEmpty(itemAddressId)) {
                            itemAddress.setSelectorIds(itemAddressId);
                            itemAddress.setText(itemAddressS);
                        } else {
                            itemAddress.setSelectorIds("");
                            itemAddress.setText(itemAddressS);
                        }

                        if (!StringUtil.isEmpty(itemJobfuncStr)) {
                            itemJobfunc.setSelectorIds(itemJobfuncId);
                            itemJobfunc.setText(itemJobfuncStr);
                        } else {
                            itemJobfunc.setSelectorIds("");
                            itemJobfunc.setText("");
                        }

                        if (!StringUtil.isEmpty(itemIndtypeStr)) {
                            itemIndtype.setSelectorIds(itemIndtypeId);
                            itemIndtype.setText(itemIndtypeStr);
                        } else {
                            itemIndtype.setSelectorIds("");
                            itemIndtype.setText("");
                        }

                        if (!StringUtil.isEmpty(itemSalaryStr)) {
                            itemSalary.setSelectorIds(itemSalaryId);
                            itemSalary.setText(itemSalaryStr);
                        } else {
                            itemSalary.setSelectorIds("");
                            itemSalary.setText("");
                        }

                        if (!StringUtil.isEmpty(itemWorktimeStr)) {
                            itemWorktime.setSelectorIds(itemWorktimeId);
                            itemWorktime.setText(itemWorktimeStr);
                        } else {
                            itemWorktime.setSelectorIds("");
                            itemWorktime.setText("");
                        }

                        if (!StringUtil.isEmpty(itemDegreeStr)) {
                            itemDegree.setSelectorIds(itemDegreeId);
                            itemDegree.setText(itemDegreeStr);
                        } else {
                            itemDegree.setSelectorIds("");
                            itemDegree.setText("");
                        }

                        if (!StringUtil.isEmpty(searchKeyWorld)) {
                            etSearch.setText(searchKeyWorld);
                        } else {
                            etSearch.setText("");
                        }
                        btnSearch.performClick();
                    }
                });
                View line = new View(mActivity);
                line.setBackgroundResource(R.color.line_color);
                searchHeistory.addView(line, lineParams);
            }
            TextView clean = new TextView(mActivity);
            clean.setPadding(0, 20, 0, 20);
            clean.setSingleLine();
            clean.setEllipsize(TextUtils.TruncateAt.END);
            clean.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            clean.setText("清除历史记录");
            clean.setTextColor(Color.parseColor("#5f9bf8"));
            clean.setBackgroundResource(R.drawable.list_item_bg);
            clean.setGravity(Gravity.CENTER);
            clean.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    UpdateDataTaskUtils.cleanHistory(mActivity, UpdateDataTaskUtils.SEARCHJOB);
                    history.clear();
                    disPlayerSearchUI(null);
                }
            });
            searchHeistory.addView(clean, layoutParams);
            View line = new View(mActivity);
            line.setBackgroundResource(R.color.line_color);
            searchHeistory.addView(line, lineParams);
        } else {
            TextView clean = new TextView(mActivity);
            clean.setPadding(0, 20, 0, 20);
            clean.setSingleLine();
            clean.setEllipsize(TextUtils.TruncateAt.END);
            clean.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            clean.setText("暂无历史记录");
            clean.setBackgroundResource(R.drawable.list_item_bg);
            clean.setGravity(Gravity.CENTER_VERTICAL);
            clean.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    UpdateDataTaskUtils.cleanHistory(mActivity, UpdateDataTaskUtils.SEARCHJOB);
                    history.clear();
                    disPlayerSearchUI(null);
                }
            });
            searchHeistory.addView(clean, layoutParams);
            View line = new View(mActivity);
            line.setBackgroundResource(R.color.line_color);
            searchHeistory.addView(line, lineParams);
        }
    }

    //个人搜索器

    private void disPlayerUserSearchUI(JSONArray jsonArray)
    {
        searchHeistoryLogin.removeAllViews();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        StringBuilder builder = new StringBuilder();

        if (jsonArray.length() <= 0) {
            TextView view = new TextView(mActivity);
            view.setPadding(0, 20, 0, 20);
            view.setSingleLine();
            view.setEllipsize(TextUtils.TruncateAt.END);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            view.setText("您还没有添加搜索器");
            view.setGravity(Gravity.CENTER);
            searchHeistoryLogin.addView(view, layoutParams);
            return;
        }


        for (int i = 0; i < jsonArray.length(); i++) {
            final int j = i;
            final JSONObject jsonObject = jsonArray.optJSONObject(i);

            if (jsonObject != null) {
                builder.delete(0, builder.length());
                builder.append(StringUtil.isEmpty(jsonObject.optString("searchName")) ? "" : jsonObject.optString("searchName") + " : ");
                builder.append(StringUtil.isEmpty(jsonObject.optString("workplaceSelectedName")) ? "" : jsonObject.optString("workplaceSelectedName") + " + ");
                builder.append(StringUtil.isEmpty(jsonObject.optString("functionSelectedName")) ? "" : jsonObject.optString("functionSelectedName") + " + ");
                builder.append(StringUtil.isEmpty(jsonObject.optString("industrySelectedName")) ? "" : jsonObject.optString("industrySelectedName") + " + ");
                builder.append(StringUtil.isEmpty(jsonObject.optString("salarySelecetdName")) ? "" : jsonObject.optString("salarySelecetdName") + " + ");
                builder.append(StringUtil.isEmpty(jsonObject.optString("workTimeSelecetdName")) ? "" : jsonObject.optString("workTimeSelecetdName") + " + ");
                builder.append(StringUtil.isEmpty(jsonObject.optString("degreeSelectedName")) ? "" : jsonObject.optString("degreeSelectedName") + " + ");

                if (builder.length() == 0) {
                    continue;
                }
                CharSequence charSequence = builder.subSequence(0, builder.length() - 3);

                View inflate = View.inflate(mActivity, R.layout.item_user_search, null);
                TextView itemContent = (TextView) inflate.findViewById(R.id.item_content);
                View itemClean = inflate.findViewById(R.id.item_clean);
                itemContent.setText(charSequence);

                searchHeistoryLogin.addView(inflate, layoutParams);
                final int finalI = i;

                itemClean.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //删除搜索器

                        AlertDialogUtil.show(mActivity, R.string.app_name, "您确定删除这个搜索器么？", true, "确定", "取消", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                HashMap<String, Object> param = new HashMap<String, Object>();
                                param.put("searchID", jsonObject.optInt("searchID"));
                                HttpUtil.post(URLS.API_JOB_Searcherdel, param, new HttpResponseHandler()
                                {
                                    @Override
                                    public void onFailure(int statusCode, String tag)
                                    {
                                    }

                                    @Override
                                    public void onSuccess(String tag, Object data)
                                    {
                                        try {
                                            searchList.put(j, null);
                                            disPlayerUserSearchUI(searchList);
//                                    getSearchData();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(int errorCode, String tag, String errorMessage)
                                    {
                                    }

                                    @Override
                                    public void onProgress(String tag, int progress)
                                    {

                                    }
                                });
                            }
                        }, null);

                    }
                });

                itemContent.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //填补条件
                        int itemAddressId = jsonObject.optInt("workplaceSelected");
                        String itemAddressS = jsonObject.optString("workplaceSelectedName");
                        String searchKeyWorld = jsonObject.optString("jobKeyEntered");

                        String itemJobfuncId = jsonObject.optString("functionSelected");
                        String itemIndtypeId = jsonObject.optString("industrySelected");
                        int itemSalaryId = jsonObject.optInt("salarySelecetd");
                        int itemWorktimeId = jsonObject.optInt("workTimeSelecetd");
                        int itemDegreeId = jsonObject.optInt("degreeSelected");


                        String itemJobfuncStr = jsonObject.optString("functionSelectedName");
                        String itemIndtypeStr = jsonObject.optString("industrySelectedName");
                        String itemSalaryStr = jsonObject.optString("salarySelecetdName");
                        String itemWorktimeStr = jsonObject.optString("workTimeSelecetdName");
                        String itemDegreeStr = jsonObject.optString("degreeSelectedName");

                        if (itemAddressId != 0) {
                            itemAddress.setSelectorIds(itemAddressId + "");
                            itemAddress.setText(itemAddressS);
                        } else {
                            itemAddress.setSelectorIds("");
                            itemAddress.setText(itemAddressS);
                        }

                        if (!StringUtil.isEmpty(itemJobfuncStr)) {
                            itemJobfunc.setSelectorIds(itemJobfuncId);
                            itemJobfunc.setText(itemJobfuncStr);
                        } else {
                            itemJobfunc.setSelectorIds("");
                            itemJobfunc.setText("");
                        }

                        if (!StringUtil.isEmpty(itemIndtypeStr)) {
                            itemIndtype.setSelectorIds(itemIndtypeId);
                            itemIndtype.setText(itemIndtypeStr);
                        } else {
                            itemIndtype.setSelectorIds("");
                            itemIndtype.setText("");
                        }

                        if (!StringUtil.isEmpty(itemSalaryStr)) {
                            itemSalary.setSelectorIds(itemSalaryId + "");
                            itemSalary.setText(itemSalaryStr);
                        } else {
                            itemSalary.setSelectorIds("");
                            itemSalary.setText("");
                        }

                        if (!StringUtil.isEmpty(itemWorktimeStr)) {
                            itemWorktime.setSelectorIds(itemWorktimeId + "");
                            itemWorktime.setText(itemWorktimeStr);
                        } else {
                            itemWorktime.setSelectorIds("");
                            itemWorktime.setText("");
                        }

                        if (!StringUtil.isEmpty(itemDegreeStr)) {
                            itemDegree.setSelectorIds(itemDegreeId + "");
                            itemDegree.setText(itemDegreeStr);
                        } else {
                            itemDegree.setSelectorIds("");
                            itemDegree.setText("");
                        }

                        if (!StringUtil.isEmpty(searchKeyWorld)) {
                            etSearch.setText(searchKeyWorld);
                        } else {
                            etSearch.setText("");
                        }

                        curSearchPosition = j;
                        searchName = jsonObject.optString("searchName");
                        searchContent.setText(searchName);
                        searchId = jsonObject.optInt("searchID") + "";
                    }
                });
            }

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        EventBus.getDefault().register(this);
        View view = inflater.inflate(R.layout.fragment_job_search, container, false);
        initView(view);
        return view;
    }

    private void getSearchData()
    {
        if (GoodJobsApp.getInstance().isLogin()) {
            HttpUtil.post(URLS.API_JOB_Searcher, null, this);
        }
    }


    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        searchList = ((JSONObject) data).optJSONArray("list");
        setSearchData();
    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);
    }

    private void setSearchData()
    {
        if (searchList != null) {
            disPlayerUserSearchUI(searchList);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        UpdateDataTaskUtils.getHistory(mActivity, UpdateDataTaskUtils.SEARCHJOB, this);
        LogUtil.info("JobSearchFragment------onResume");
        if (isRefresh) {
            getSearchData();
            isRefresh = false;
        }
//        searchCheck.setChecked(false);
    }

    @Override
    public void onSegmentViewClick(View v, int position)
    {
        switch (position) {
            case 0:
                searchHeistory.setVisibility(View.VISIBLE);
                searchHeistoryLogin.setVisibility(View.GONE);
                addSearchBox.setVisibility(View.GONE);
                searchCheck.setChecked(false);
                break;
            case 1:
                searchHeistory.setVisibility(View.GONE);
                searchHeistoryLogin.setVisibility(View.VISIBLE);

                if (!GoodJobsApp.getInstance().isLogin()) {
                    butLogin.setVisibility(View.VISIBLE);
                    addSearchBox.setVisibility(View.GONE);
                } else {
                    butLogin.setVisibility(View.GONE);
                    addSearchBox.setVisibility(View.VISIBLE);
                    if (searchList == null) {
                        getSearchData();
                    }
                }


                break;
        }
    }

    private void initView(View view)
    {
        itemSalary = (SelectorItemView) view.findViewById(R.id.item_salary);
        itemAddress = (SelectorItemView) view.findViewById(R.id.item_address);
        itemJobfunc = (SelectorItemView) view.findViewById(R.id.item_jobfunc);
        itemIndtype = (SelectorItemView) view.findViewById(R.id.item_indtype);
        itemWorktime = (SelectorItemView) view.findViewById(R.id.item_worktime);
        itemDegree = (SelectorItemView) view.findViewById(R.id.item_degree);
        searchHeistory = (LinearLayout) view.findViewById(R.id.search_heistory);
        searchHeistoryLogin = (LinearLayout) view.findViewById(R.id.search_heistory_login);
        butLogin = (TextView) view.findViewById(R.id.but_login);
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        clean = view.findViewById(R.id.clean);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);

        addSearchBox = view.findViewById(R.id.add_search_box);
        searchTitle = view.findViewById(R.id.search_title);
        searchContent = (EditText) view.findViewById(R.id.search_content);
        searchCheck = (CheckBox) view.findViewById(R.id.search_check);
        searchCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                searchContent.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                searchTitle.setVisibility(isChecked ? View.GONE : View.VISIBLE);
            }
        });

        clean.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                etSearch.setText("");
            }
        });
        etSearch.addTextChangedListener(new TextWatcher()
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
                if (s.length() > 0) {
                    clean.setVisibility(View.VISIBLE);
                } else {
                    clean.setVisibility(View.GONE);
                }

            }
        });

        btnSearch.setOnClickListener(this);
        searchSwitch = (SegmentView) view.findViewById(R.id.search_switch);
        searchSwitch.setSegmentText("近期搜索记录", 0);
        searchSwitch.setSegmentText("个人搜索器", 1);
        searchSwitch.setTextRes(R.drawable.seg_text_color_selector2);
        searchSwitch.setBgRes(R.drawable.seg_search);
        searchSwitch.multiType(false);
        searchSwitch.setOnSegmentViewClickListener(this);
        searchSwitch.perClick(0);

        butLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //跳转登陆界面
                GoodJobsApp.getInstance().checkLogin(mActivity, URLS.JOB_search_login);
            }
        });

        view.findViewById(R.id.etSearch).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                add = itemAddress.getText();
                if (StringUtil.isEmpty(add)) {
                    TipsUtil.show(mActivity, "请选择工作地点");
                    return;
                }
                LinkedHashMap hashMap = getSearchHashMap();

                if (searchCheck.isChecked()) {
                    String s = searchContent.getText().toString();
                    if (!StringUtil.isEmpty(s)) {
                        hashMap.put("searchName", s);
                        if (!s.equals(searchName)) {
                            hashMap.put("searchID", "");
                        } else {
                            hashMap.put("searchID", searchId);
                        }
                        searchContent.setText("");
                        isRefresh = true;
                    } else {
                        TipsUtil.show(mActivity, "请输入搜索器名称");
                        return;
                    }
                }


                JumpViewUtil.openActivityAndParam(mActivity, JobSearchNameActivity.class, hashMap);
            }
        });
        setTopTitle(view, "职位搜索");
        hideBackBtn(view);

        Object object = SharedPrefUtil.getObject("location");
        if (object != null) {
            myLocation = (MyLocation) object;
            itemAddress.setText(myLocation.city);
        }
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btnSearch) {
            if (history == null) {
                history = new HashMap<>();
            }
            add = itemAddress.getText();
            if (StringUtil.isEmpty(add)) {
                TipsUtil.show(mActivity, "请选择工作地点");
                return;
            }
            LinkedHashMap searchHashMap = getSearchHashMap();
            saveSearchLock(history, searchHashMap);
            if (searchCheck.isChecked()) {
                String s = searchContent.getText().toString();
                if (!StringUtil.isEmpty(s)) {
                    searchHashMap.put("searchName", s);
                    if (!s.equals(searchName)) {
                        searchHashMap.put("searchID", "");
                    } else {
                        searchHashMap.put("searchID", searchId);
                    }
                    searchContent.setText("");
                    isRefresh = true;
                } else {
                    TipsUtil.show(mActivity, "请输入搜索器名称");
                    return;
                }
            }
            //跳转搜索列表
            JumpViewUtil.openActivityAndParam(mActivity, JobSearchResultActivity.class, searchHashMap);
        }

    }

    private LinkedHashMap getSearchHashMap()
    {
        searchKeyWorld = etSearch.getText().toString();
        add = itemAddress.getText();
        sal = itemSalary.getText();
        job = itemJobfunc.getText();
        ind = itemIndtype.getText();
        wt = itemWorktime.getText();
        deg = itemDegree.getText();

        LinkedHashMap hashMap = new LinkedHashMap();
        if (!StringUtil.isEmpty(searchKeyWorld)) {
            hashMap.put("searchKeyWorld", searchKeyWorld);
        }
        if (!StringUtil.isEmpty(add)) {
            hashMap.put("itemAddress", add);
        }
        if (!StringUtil.isEmpty(job)) {
            hashMap.put("itemJobfunc", job);
        }
        if (!StringUtil.isEmpty(ind)) {
            hashMap.put("itemIndtype", ind);
        }
        if (!StringUtil.isEmpty(sal)) {
            hashMap.put("itemSalary", sal);
        }
        if (!StringUtil.isEmpty(wt)) {
            hashMap.put("itemWorktime", wt);
        }
        if (!StringUtil.isEmpty(deg)) {
            hashMap.put("itemDegree", deg);
        }
        hashMap.putAll(getSearchHashMapID());
        return hashMap;
    }

    private LinkedHashMap getSearchHashMapID()
    {
        String addId = (String) itemAddress.getSelectorIds();
        String salId = (String) itemSalary.getSelectorIds();
        String jobId = (String) itemJobfunc.getSelectorIds();
        String indId = (String) itemIndtype.getSelectorIds();
        String wtId = (String) itemWorktime.getSelectorIds();
        String degId = (String) itemDegree.getSelectorIds();

        LinkedHashMap hashMap = new LinkedHashMap();
        if (!StringUtil.isEmpty(addId)) {
            hashMap.put("itemAddressId", addId);
        }
        if (!StringUtil.isEmpty(jobId)) {

            String[] split = jobId.split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if (split[i].startsWith("-1")) {
                    split[i] = split[i].split("#")[1];
                }
                builder.append(split[i] + ",");
            }
            CharSequence charSequence = builder.subSequence(0, builder.length() - 1);

            hashMap.put("itemJobfuncId", charSequence.toString());
        }
        if (!StringUtil.isEmpty(indId)) {
            hashMap.put("itemIndtypeId", indId);
        }
        if (!StringUtil.isEmpty(salId)) {
            hashMap.put("itemSalaryId", salId);
        }
        if (!StringUtil.isEmpty(wtId)) {
            hashMap.put("itemWorktimeId", wtId);
        }
        if (!StringUtil.isEmpty(degId)) {
            hashMap.put("itemDegreeId", degId);
        }


        return hashMap;
    }


    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        LogUtil.info("JobSearchFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoad) {
            UpdateDataTaskUtils.getHistory(mActivity, UpdateDataTaskUtils.SEARCHJOB, this);
            LocationUtil.newInstance(mActivity.getApplication()).startLoction(new MyLocationListener()
            {
                @Override
                public void loaction(final MyLocation location)
                {
                    mActivity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            LogUtil.info(location.toString());
                            SharedPrefUtil.saveObjectToLoacl("location", location);
                            myLocation = location;
                            itemAddress.setText(myLocation.city);
                            itemAddress.setSelectorIds(myLocation.cityID);
                            isLoad = true;
                        }
                    });

                }
            });
        }

        if (isRefresh) {
            getSearchData();
            isRefresh = false;
        }
    }


    public void saveSearchLock(Map<Long, Map<String, String>> saveData, Map<String, String> put)
    {
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
        UpdateDataTaskUtils.updateHistory(mActivity, saveData, UpdateDataTaskUtils.SEARCHJOB);
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
        if (infoIds != null && infoIds.size() > 0) {
            message.obj = infoIds;
        }
        handler.sendMessage(message);
    }
}
