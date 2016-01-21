package cn.goodjobs.applyjobs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.activity.jobSearch.JobSearchNameActivity;
import cn.goodjobs.applyjobs.activity.jobSearch.JobSearchResultActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
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
import cn.goodjobs.common.view.searchItem.SelectorItemView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * 求职端主界面
 */
public class HomeFragment extends BaseFragment implements UpdateDataTaskUtils.OnGetDiscussHistoryListener
{

    private Map<Long, Map<String, String>> longMapMap;

    private String add;

    android.os.Handler handler = new android.os.Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            List<Map.Entry<Long, Map<String, String>>> infoIds = (List<Map.Entry<Long, Map<String, String>>>) msg.obj;

            if (infoIds != null) {
                historyLayout.setVisibility(View.VISIBLE);
                final Map<String, String> value = infoIds.get(0).getValue();

                StringBuilder builder = new StringBuilder();
                builder.append(StringUtil.isEmpty(value.get("searchKeyWorld")) ? "" : value.get("searchKeyWorld") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemAddress")) ? "" : value.get("itemAddress") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemJobfunc")) ? "" : value.get("itemJobfunc") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemIndtype")) ? "" : value.get("itemIndtype") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemSalary")) ? "" : value.get("itemSalary") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemWorktime")) ? "" : value.get("itemWorktime") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemDegree")) ? "" : value.get("itemDegree") + " + ");
                CharSequence charSequence = builder.subSequence(0, builder.length() - 3);
                tvHistory.setText(charSequence);

                tvHistory.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String itemAddressId = value.get("itemAddressId");
                        String itemAddressS = value.get("itemAddress");
                        String searchKeyWorld = value.get("searchKeyWorld");
                        String itemJobfuncStr = value.get("itemJobfunc");
                        String itemJobfuncId = value.get("itemJobfuncId");
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
                        if (!StringUtil.isEmpty(searchKeyWorld)) {
                            etSearch.setText(searchKeyWorld);
                        } else {
                            etSearch.setText("");
                        }

                        LinkedHashMap searchHashMap = getSearchHashMap();
                        //跳转搜索列表
                        JumpViewUtil.openActivityAndParam(mActivity, JobSearchResultActivity.class, searchHashMap);
                    }
                });
            } else {
                historyLayout.setVisibility(View.GONE);
            }

        }
    };
    private View historyLayout;
    private TextView tvHistory;
    private SelectorItemView itemAddress;
    private SelectorItemView itemJobfunc;
    private EditText etSearch;
    private View clean;
    private String job;
    private String searchKeyWorld;
    private MyLocation myLocation;
    private Button btnSearch;
    private Map<Long, Map<String, String>> history;

    public HomeFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        LogUtil.info("onCreateView");
        getDataFromServer();
        return view;
    }

    private void getDataFromServer()
    {
        LoadingDialog.showDialog(mActivity);
        HttpUtil.post(URLS.API_IMG_AD, this);
    }

    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        initAd((JSONArray) data);
        /** 测试代码========================     */
//        JSONArray jsonArray = new JSONArray();
//        JSONObject jsonObject;
//        try {
//            jsonObject = new JSONObject();
//            jsonObject.put("image", "http://hd.shijue.cvidea.cn/tf/140826/2348436/53fc93183dfae9381b000001.GIF");
//            jsonObject.put("url", "http://m.goodjobs.cn/");
//            jsonObject.put("title", "test");
//            jsonArray.put(jsonObject);
//
//            jsonObject = new JSONObject();
//            jsonObject.put("image", "http://pic33.nipic.com/20131011/8636861_091803753113_2.jpg");
//            jsonObject.put("url", "http://m.goodjobs.cn/");
//            jsonObject.put("title", "test2");
//            jsonArray.put(jsonObject);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        initAd(jsonArray);
        /** 测试代码========================     */
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        LogUtil.info("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        LocationUtil.newInstance(mActivity.getApplication()).startLoction(new MyLocationListener()
        {
            @Override
            public void loaction(MyLocation location)
            {
                LogUtil.info(location.toString());
                SharedPrefUtil.saveObjectToLoacl("location", location);

            }
        });
    }

    private void initView(View view)
    {
        adViewPager = (AutoScrollViewPager) view.findViewById(R.id.adViewPager);

        etSearch = (EditText) view.findViewById(R.id.etSearch);
        clean = view.findViewById(R.id.search_clean);


        historyLayout = view.findViewById(R.id.historyLayout);
        tvHistory = (TextView) view.findViewById(R.id.tvHistory);
        itemAddress = (SelectorItemView) view.findViewById(R.id.item_address);
        itemJobfunc = (SelectorItemView) view.findViewById(R.id.item_jobfunc);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);


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
        etSearch.setOnClickListener(new View.OnClickListener()
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

                JumpViewUtil.openActivityAndParam(mActivity, JobSearchNameActivity.class, hashMap);
            }
        });
        btnSearch.setOnClickListener(this);
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
            //跳转搜索列表
            JumpViewUtil.openActivityAndParam(mActivity, JobSearchResultActivity.class, searchHashMap);
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

    private LinkedHashMap getSearchHashMap()
    {
        searchKeyWorld = etSearch.getText().toString();
        add = itemAddress.getText();
        job = itemJobfunc.getText();

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
        hashMap.putAll(getSearchHashMapID());
        return hashMap;
    }

    private LinkedHashMap getSearchHashMapID()
    {
        String addId = (String) itemAddress.getSelectorIds();
        String jobId = (String) itemJobfunc.getSelectorIds();

        LinkedHashMap hashMap = new LinkedHashMap();
        if (!StringUtil.isEmpty(addId)) {
            hashMap.put("itemAddressId", addId);
        }
        if (!StringUtil.isEmpty(jobId)) {

            String[] split = jobId.split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                if (split[i].startsWith("-1")) {
                    split[i] = split[i].split(SelectorItemView.spitStr)[1];
                }
                builder.append(split[i] + ",");
            }
            CharSequence charSequence = builder.subSequence(0, builder.length() - 1);

            hashMap.put("itemJobfuncId", charSequence.toString());
        }
        return hashMap;
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        LogUtil.info("HomeFragment--------setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mActivity != null) {
            UpdateDataTaskUtils.getHistory(mActivity, UpdateDataTaskUtils.SEARCHJOB, this);
        }

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
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!isLoad) {
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
        UpdateDataTaskUtils.getHistory(mActivity, UpdateDataTaskUtils.SEARCHJOB, this);
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
