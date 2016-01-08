package cn.goodjobs.bluecollar.activity;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.adapter.BlueJobSearchResultAdapter;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
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
import cn.goodjobs.common.view.ExpandTabSuper.ExpandTabView;
import cn.goodjobs.common.view.ExpandTabSuper.SingleLevelMenuView;
import cn.goodjobs.common.view.ExpandTabSuper.TwoLevelMenuView;

/**
 * Created by yexiangyu on 15/12/23.
 */
public class BlueJobSearchResultActivity extends BaseListActivity implements UpdateDataTaskUtils.OnGetDiscussJobTypeListener, UpdateDataTaskUtils.OnGetDiscussCityInfoListener, UpdateDataTaskUtils.OnGetDiscussSalaryInfoListener, UpdateDataTaskUtils.OnGetDiscussMoreInfoListener

{

    private ExpandTabView etvMenu;
    private int cityId;
    private Map<String, Map<String, String>> twoCate;


    Handler handler = new Handler()
    {


        @Override
        public void dispatchMessage(Message msg)
        {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case UpdateDataTaskUtils.CITYDATA:
                    ArrayList<JSONObject> obj = (ArrayList<JSONObject>) msg.obj;
                    values = new ArrayList<>();
                    for (int i = 0; i < obj.size(); i++) {
                        try {
                            values.add(obj.get(i).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Map<String, String> oneCate = new TreeMap<String, String>();
                    oneCate.put("0", "地区筛选");
                    oneCate.put("1", "附近筛选");

                    twoCate = new TreeMap<String, Map<String, String>>();
                    twoCate_one = new LinkedHashMap<String, String>();
                    for (int i = 0; i < values.size(); i++) {
                        twoCate_one.put(i + "", values.get(i));
                    }

                    Map<String, String> twoCate_two = new TreeMap<String, String>();

                    twoCate_two.put("1000", "无法获取当前地理位置");
                    twoCate.put("0", twoCate_one);
                    twoCate.put("1", twoCate_two);
                    cityInfo.setValue(oneCate, twoCate, 0 + "", 0 + "", new int[]{0, 0});

                    LocationUtil.newInstance(BlueJobSearchResultActivity.this.getApplication()).startLoction(new MyLocationListener()
                    {
                        @Override
                        public void loaction(MyLocation location)
                        {
                            LogUtil.info(location.toString());
                            SharedPrefUtil.saveObjectToLoacl("location", location);
                            myLocation = location;
                            Map<String, String> oneCate = new TreeMap<String, String>();
                            oneCate.put("0", "地区筛选");
                            oneCate.put("1", "附近筛选");
                            Map<String, String> twoCate_two = new TreeMap<String, String>();
                            twoCate_two.put("0", "不限");
                            twoCate_two.put("1", "500M");
                            twoCate_two.put("2", "1000M");
                            twoCate_two.put("3", "2000M");
                            twoCate_two.put("4", "3000M");

                            twoCate.put("1", twoCate_two);
                            twoCate.put("0", twoCate_one);
                            cityInfo.setValue(oneCate, twoCate, 0 + "", 0 + "", new int[]{0, 0});
                            isLoad = true;
                            lat = location.latitude;
                            lng = location.longitude;
                        }
                    });


                    break;
                case UpdateDataTaskUtils.SALARYDATA:
                    ArrayList<JSONObject> objs = (ArrayList<JSONObject>) msg.obj;
                    LinkedHashMap<String, String> linkMap = new LinkedHashMap<String, String>();
                    for (int i = 0; i < objs.size(); i++) {
                        try {
                            linkMap.put(objs.get(i).getString("id"), objs.get(i).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    salaryInfo.setValue(linkMap, itemSalaryId != null ? itemSalaryId : "");

                    break;
                case UpdateDataTaskUtils.JOBTYPEDATA:
                    ArrayList<JSONObject> jobobjs = (ArrayList<JSONObject>) msg.obj;
                    LinkedHashMap<String, String> jobMap = new LinkedHashMap<String, String>();
                    for (int i = 0; i < jobobjs.size(); i++) {
                        try {
                            jobMap.put(jobobjs.get(i).getString("id"), jobobjs.get(i).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    jobType.setValue(jobMap, itemSalaryId != null ? itemSalaryId : "");

                    break;
                case UpdateDataTaskUtils.MOREDATA:
                    Map<String, List<JSONObject>> objMore = (Map<String, List<JSONObject>>) msg.obj;
                    Set<Map.Entry<String, List<JSONObject>>> entries = objMore.entrySet();


                    Map<String, String> oneCate1 = new TreeMap<String, String>();
                    Map<String, Map<String, String>> twoCate = new TreeMap<String, Map<String, String>>();
                    int index = 0;
                    for (Map.Entry<String, List<JSONObject>> entry : entries) {
                        try {
                            oneCate1.put(index + "", entry.getKey());
                            LinkedHashMap<String, String> strings = new LinkedHashMap<>();
                            for (int i = 0; i < entry.getValue().size(); i++) {
                                strings.put(entry.getValue().get(i).getString("id"), entry.getValue().get(i).getString("name"));
                            }
                            twoCate.put(index + "", strings);
                            index++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    moreInfo.setValue(oneCate1, twoCate, 0 + "", 0 + "", null);


                    break;
            }

        }
    };
    private TwoLevelMenuView cityInfo;
    private SingleLevelMenuView salaryInfo;
    private SingleLevelMenuView jobType;
    private TwoLevelMenuView moreInfo;
    private List<JSONObject> cityData;
    private List<JSONObject> salaryData;
    private Map<String, List<JSONObject>> moreData;
    private MyLocation myLocation;
    private boolean isLoad;
    private Map<String, String> twoCate_one;
    private ArrayList<String> values;

    private boolean isCur;
    private boolean isMuCheck;


    private String searchKeyWorld;
    private String itemAddress;
    private String itemSalary;
    private String itemJobfunc;
    private String itemIndtype;
    private String itemWorktime;
    private String itemDegree;
    private boolean isPro;
    private double lat, lng;


    private String itemAddressId;
    private int dis;
    private String itemSalaryId = "";
    private String itemJobfuncId;
    private String itemIndtypeId;
    private String corpkindId;
    private String jobTypeId;
    private String itemWorktimeId;
    private String itemDegreeId;

    private TextView store;
    private TextView send;
    private LinearLayout bottomBar;
    private String searchName;
    private String searchID;
    private String itemJobfuncPrantId;
    private List<JSONObject> jobTypeyData;


    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_bluesearchresult;
    }

    @Override
    protected void initWeightClick()
    {

        etvMenu.setmOnExpandTabDismissListener(new ExpandTabView.OnExpandTabDismissListener()
        {
            @Override
            public void expandTabDismiss(int position)
            {
                if (position == 2 && isMuCheck) {
                    startRefresh();
                    isMuCheck = false;
                }
            }
        });


        //区域选择
        cityInfo.setOnSelectListener(new TwoLevelMenuView.OnSelectListener()
        {
            @Override
            public void onSelected(String firstLevelKey, String secondLevelKey, String showString)
            {
                if (firstLevelKey.equals("0")) {
                    isCur = false;
                    if (isPro && !secondLevelKey.equals("0")) {
                        UpdateDataTaskUtils.selectCityInfo(mcontext, cityData.get(Integer.parseInt(secondLevelKey)).optString("name"), BlueJobSearchResultActivity.this);
                        isPro = false;
                    }
                    itemAddressId = cityData.get(Integer.parseInt(secondLevelKey)).optString("id");
                } else {
                    isCur = true;
                    int i = Integer.parseInt(secondLevelKey);
                    switch (i) {
                        case 0:
                            dis = 0;
                            break;
                        case 1:
                            dis = 500;
                            break;
                        case 2:
                            dis = 1000;
                            break;
                        case 3:
                            dis = 2000;
                            break;
                        case 4:
                            dis = 3000;
                            break;
                    }
                }
                etvMenu.setTitle(showString, 0);
                startRefresh();
            }

            @Override
            public void onSelectedMuilt(HashMap<String, String> muiltMap)
            {
            }
        });
        //区域薪资
        salaryInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener()
        {
            @Override
            public void onSelected(String selectedKey, String showString)
            {
                itemSalaryId = selectedKey;
                etvMenu.setTitle(showString, 1);
                startRefresh();
            }
        });
        //更多选择
        moreInfo.setOnSelectListener(new TwoLevelMenuView.OnSelectListener()
        {


            @Override
            public void onSelected(String firstLevelKey, String secondLevelKey, String showString)
            {
            }

            @Override
            public void onSelectedMuilt(HashMap<String, String> muiltMap)
            {
                Set<Map.Entry<String, String>> entries = muiltMap.entrySet();

                for (Map.Entry<String, String> entry : entries) {

                    String key = entry.getKey();
                    if (key.equals("0")) {
                        itemDegreeId = entry.getValue();
                    }
                    if (key.equals("1")) {
                        itemWorktimeId = entry.getValue();
                    }
                    if (key.equals("2")) {
                        jobTypeId = entry.getValue();
                    }
                    if (key.equals("3")) {
                        corpkindId = entry.getValue();
                    }
                }
                isMuCheck = true;

            }
        });

    }

    @Override
    protected void initWeight()
    {
        mAdapter = new BlueJobSearchResultAdapter(this);
        ((BlueJobSearchResultAdapter) mAdapter).setJobSearchResultActivity(this);
        initList();
        etvMenu = (ExpandTabView) findViewById(R.id.etv_menu);
        bottomBar = (LinearLayout) findViewById(R.id.bottom_bar);

        cityInfo = new TwoLevelMenuView(mcontext);
        salaryInfo = new SingleLevelMenuView(mcontext);
        jobType = new SingleLevelMenuView(mcontext);
        moreInfo = new TwoLevelMenuView(mcontext);
        moreInfo.setIsMultiCheck(true);
        HashMap<String, String> multiCheckMap = new HashMap<>();
        multiCheckMap.put("0", itemDegreeId == null ? "0" : itemDegreeId);
        multiCheckMap.put("1", itemWorktimeId == null ? "0" : itemWorktimeId);
        multiCheckMap.put("2", jobTypeId == null ? "0" : jobTypeId);
        multiCheckMap.put("3", corpkindId == null ? "0" : corpkindId);
        moreInfo.setCheckMult(multiCheckMap);

        store = (TextView) findViewById(R.id.item_store);
        send = (TextView) findViewById(R.id.item_send);
        store.setOnClickListener(this);
        send.setOnClickListener(this);

        Drawable drawable = getResources().getDrawable(R.drawable.store);

        drawable.setBounds(0, 0, DensityUtil.dip2px(mcontext, 20), DensityUtil.dip2px(mcontext, 20));

        store.setCompoundDrawables(null, drawable, null, null);


        ArrayList<String> strings = new ArrayList<>();
        strings.add("区域筛选");
        strings.add(itemJobfunc);
        strings.add(StringUtil.isEmpty(itemSalary) ? "薪资要求" : itemSalary);
        strings.add("更多筛选");

        ArrayList<View> views = new ArrayList<>();
        views.add(cityInfo);
        views.add(jobType);
        views.add(salaryInfo);
        views.add(moreInfo);

        ArrayList<Integer> integers = new ArrayList<Integer>();
        int i = DensityUtil.dip2px(mcontext, 45);
        integers.add(5 * i);
        integers.add(5 * i);
        integers.add(5 * i);
        integers.add(5 * i);
        etvMenu.setValue(strings, views, integers);

        etvMenu.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                startRefresh();
            }
        }, 500);
    }

    @Override
    protected void getDataFronServer()
    {

        HashMap<String, Object> Object = new HashMap<String, Object>();
        Object.put("page", page);

        if (page != 1)
            Object.put("cepage", page);

        {
            if (!StringUtil.isEmpty(searchKeyWorld))//关键字
                Object.put("keyword", searchKeyWorld);
        }

        if (isCur) {
            if (dis != 0)//地址
                Object.put("radius", dis);

            if (lat != 0)//地址
                Object.put("lat", lat);

            if (lng != 0)//地址
                Object.put("lng", lng);

        } else {
            if (!StringUtil.isEmpty(itemAddressId))//地址
                Object.put("jobcity", itemAddressId);
        }

        if (!StringUtil.isEmpty(itemIndtypeId))//行业
            Object.put("industry", itemIndtypeId.replaceAll("#", ","));

        if (!StringUtil.isEmpty(itemJobfuncId))//岗位
            Object.put("respon", itemJobfuncId.replaceAll("#", ","));

        if (!StringUtil.isEmpty(itemSalaryId))//薪资
            Object.put("salary", itemSalaryId);

        if (!StringUtil.isEmpty(itemWorktimeId))//工作时间
            Object.put("worktime", itemWorktimeId);

        if (!StringUtil.isEmpty(itemDegreeId))//学历
            Object.put("degree", itemDegreeId);

        if (!StringUtil.isEmpty(jobTypeId))//工作性质
            Object.put("jobType", jobTypeId);

        if (!StringUtil.isEmpty(corpkindId))//企业性质
            Object.put("corpkind", corpkindId);

        if (!StringUtil.isEmpty(searchName))//搜索器名称
            Object.put("searchName", searchName);

        if (!StringUtil.isEmpty(searchID))//搜索器ID
            Object.put("searchID", searchID);


        HttpUtil.post(URLS.API_JOB_JobList, Object, this);
    }


    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);

        JSONObject object = (JSONObject) data;
        if (checkJsonError(object))
            return;
        String totalNum = object.optString("totalNum");
        setTopTitle("共" + totalNum + "条");

        try {
            mAdapter.appendToList(object.getJSONArray("list"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void initData()
    {
        searchKeyWorld = getIntent().getStringExtra("searchKeyWorld");
        itemAddress = getIntent().getStringExtra("itemAddress");
        itemSalary = getIntent().getStringExtra("itemSalary");
        itemJobfunc = getIntent().getStringExtra("itemJobfunc");
        itemIndtype = getIntent().getStringExtra("itemIndtype");
        itemWorktime = getIntent().getStringExtra("itemWorktime");
        itemDegree = getIntent().getStringExtra("itemDegree");

        itemAddressId = getIntent().getStringExtra("itemAddressId");
        itemSalaryId = getIntent().getStringExtra("itemSalaryId");
        itemJobfuncId = getIntent().getStringExtra("itemJobfuncId");
        itemJobfuncPrantId = getIntent().getStringExtra("jobpraentId");
        itemIndtypeId = getIntent().getStringExtra("itemIndtypeId");
        itemWorktimeId = getIntent().getStringExtra("itemWorktimeId");
        itemDegreeId = getIntent().getStringExtra("itemDegreeId");
        searchName = getIntent().getStringExtra("searchName");
        searchID = getIntent().getStringExtra("searchID");

        if (!StringUtil.isEmpty(itemAddressId) && itemAddressId.startsWith("-1")) {
            UpdateDataTaskUtils.selectProInfo(this, itemAddress, this);
            isPro = true;
        } else {
            isPro = false;
            UpdateDataTaskUtils.selectCityInfo(this, itemAddress, this);
        }
        UpdateDataTaskUtils.selectSalaryInfo(this, this);
        UpdateDataTaskUtils.selectMoreInfo(this, this);
        UpdateDataTaskUtils.selectJobType(this, itemJobfuncPrantId, this);


    }

    @Override
    public void onGetDiscussCityInfo(List<JSONObject> cityData, int CityId)
    {
        Message message = new Message();
        message.what = UpdateDataTaskUtils.CITYDATA;
        message.obj = cityData;
        itemAddressId = CityId + "";
        this.cityData = cityData;
        handler.sendMessage(message);
    }

    @Override
    public void onGetDiscussSalaryInfo(List<JSONObject> salaryData)
    {
        Message message = new Message();
        message.what = UpdateDataTaskUtils.SALARYDATA;
        message.obj = salaryData;
        this.salaryData = salaryData;
        handler.sendMessage(message);
    }

    @Override
    public void onGetDiscussJobTypeo(List<JSONObject> jobTypeyData)
    {
        Message message = new Message();
        message.what = UpdateDataTaskUtils.JOBTYPEDATA;
        message.obj = jobTypeyData;
        this.jobTypeyData = jobTypeyData;
        handler.sendMessage(message);
    }


    @Override
    public void onGetDiscussMoreInfo(Map<String, List<JSONObject>> MoreData)
    {
        Message message = new Message();
        message.what = UpdateDataTaskUtils.MOREDATA;
        message.obj = MoreData;
        this.moreData = MoreData;
        handler.sendMessage(message);
    }


    public void setBottomVisible(Boolean isShow)
    {
        bottomBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        ArrayList<Integer> checkPosition = ((BlueJobSearchResultAdapter) mAdapter).getCheckPosition();
        List<JSONObject> list = mAdapter.getList();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < checkPosition.size(); i++) {
            builder.append(list.get(checkPosition.get(i)).optInt("jobID") + ",");
        }
        String ids = builder.subSequence(0, builder.length() - 1).toString();

        int i = v.getId();
        if (i == R.id.item_store) {
            //收藏职位Fs
            if (!GoodJobsApp.getInstance().checkLogin(mcontext))
                return;

            HashMap<String, Object> param = new HashMap<>();
            param.put("jobID", ids);
            HttpUtil.post(URLS.API_JOB_favorite, param, new HttpResponseHandler()
            {
                @Override
                public void onFailure(int statusCode, String tag)
                {
                    TipsUtil.show(mcontext, "收藏失败");
                }

                @Override
                public void onSuccess(String tag, Object data)
                {
                    TipsUtil.show(mcontext, ((JSONObject) data).optString("message"));
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

        } else if (i == R.id.item_send) {
            //投递简历
            if (!GoodJobsApp.getInstance().checkLogin(mcontext))
                return;

            HashMap<String, Object> param = new HashMap<>();
            param.put("jobID", ids);
            param.put("ft", 2);
            HttpUtil.post(URLS.API_JOB_apply, param, new HttpResponseHandler()
            {
                @Override
                public void onFailure(int statusCode, String tag)
                {
                    TipsUtil.show(mcontext, "简历投递失败");
                }

                @Override
                public void onSuccess(String tag, Object data)
                {
                    TipsUtil.show(mcontext, ((JSONObject) data).optString("message"));
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
    }

}
