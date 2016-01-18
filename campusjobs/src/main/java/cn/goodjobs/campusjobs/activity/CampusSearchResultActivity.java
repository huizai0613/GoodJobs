package cn.goodjobs.campusjobs.activity;

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

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.adapter.CmapusResultAdapter;
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
 * Created by zhuli on 2015/12/31.
 */
public class CampusSearchResultActivity extends BaseListActivity implements UpdateDataTaskUtils.OnGetCompanyInfoListener, UpdateDataTaskUtils.OnGetDiscussSalaryInfoListener, UpdateDataTaskUtils.OnGetDiscussMoreInfoListener

{

    private ExpandTabView etvMenu;
    private int cityId;
    private Map<String, Map<String, String>> twoCate;


    Handler handler = new Handler() {


        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case UpdateDataTaskUtils.COMPANYDATA:

                    ArrayList<JSONObject> objs = (ArrayList<JSONObject>) msg.obj;
                    LinkedHashMap<String, String> linkMap1 = new LinkedHashMap<String, String>();
                    for (int i = 0; i < objs.size(); i++) {
                        try {
                            linkMap1.put(objs.get(i).getString("id"), objs.get(i).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    companyInfo.setValue(linkMap1, 0 + "");

                    break;
                case UpdateDataTaskUtils.SALARYDATA:
                    ArrayList<JSONObject> cobjs = (ArrayList<JSONObject>) msg.obj;
                    LinkedHashMap<String, String> linkMap = new LinkedHashMap<String, String>();
                    for (int i = 0; i < cobjs.size(); i++) {
                        try {
                            linkMap.put(cobjs.get(i).getString("id"), cobjs.get(i).getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    salaryInfo.setValue(linkMap, 0 + "");

                    break;
                case UpdateDataTaskUtils.CAMPUSMOREDATA:
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
    private SingleLevelMenuView salaryInfo, companyInfo;
    private TwoLevelMenuView moreInfo;
    private List<JSONObject> companyData;
    private List<JSONObject> salaryData;
    private Map<String, List<JSONObject>> moreData;

    private String searchKeyWorld;
    private String itemAddress;
    private String itemSalary;
    private String itemJobfunc;
    private String itemIndtype;
    private String itemJobfuncId;
    private String itemIndtypeId;
    private boolean isPro;
    private double lat, lng;


    private String itemAddressId;
    private String itemSalaryId = "";
    private String corpkindId;
    private String jobTypeId;
    private String itemDegreeId;

    private TextView store;
    private TextView send;
    private LinearLayout bottomBar;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_campusresult;
    }

    @Override
    protected void initWeightClick() {


        companyInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                corpkindId = selectedKey;
                etvMenu.setTitle(showString, 1);
                startRefresh();
            }
        });

        //薪资
        salaryInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                itemSalaryId = selectedKey;
                etvMenu.setTitle(showString, 0);
                startRefresh();
            }
        });
        //更多选择
        moreInfo.setOnSelectListener(new TwoLevelMenuView.OnSelectListener() {


            @Override
            public void onSelected(String firstLevelKey, String secondLevelKey, String showString) {
            }

            @Override
            public void onSelectedMuilt(HashMap<String, String> muiltMap) {
                Set<Map.Entry<String, String>> entries = muiltMap.entrySet();

                for (Map.Entry<String, String> entry : entries) {

                    String key = entry.getKey();
                    if (key.equals("0")) {
                        itemDegreeId = entry.getValue();
                    }
                    if (key.equals("1")) {
                        jobTypeId = entry.getValue();
                    }
                    if (key.equals("2")) {
                        if (isPro) {
                            if (!entry.getValue().equals("0")) {
                                itemAddressId = entry.getValue();
                                UpdateDataTaskUtils.selectCompusInfo(CampusSearchResultActivity.this, false, itemAddressId, CampusSearchResultActivity.this);
                            }
                        } else {
                            if (!entry.getValue().equals("0")) {
                                itemAddressId = entry.getValue();
                            }
                        }
                    }
                }
                startRefresh();
            }
        });

    }

    @Override
    protected void initWeight() {
        setTopTitle("共    条");
        mAdapter = new CmapusResultAdapter(this);
        ((CmapusResultAdapter) mAdapter).setCmapusResultAdapter(this);
        initList();
        etvMenu = (ExpandTabView) findViewById(R.id.etv_menu);
        bottomBar = (LinearLayout) findViewById(R.id.bottom_bar);

        companyInfo = new SingleLevelMenuView(mcontext);
        salaryInfo = new SingleLevelMenuView(mcontext);
        moreInfo = new TwoLevelMenuView(mcontext);
        moreInfo.setIsMultiCheck(true);

        store = (TextView) findViewById(R.id.item_store);
        send = (TextView) findViewById(R.id.item_send);
        store.setOnClickListener(this);
        send.setOnClickListener(this);

        Drawable drawable = getResources().getDrawable(R.drawable.store);

        drawable.setBounds(0, 0, DensityUtil.dip2px(mcontext, 20), DensityUtil.dip2px(mcontext, 20));

        store.setCompoundDrawables(null, drawable, null, null);


        ArrayList<String> strings = new ArrayList<>();
        strings.add(StringUtil.isEmpty(itemSalary) ? "薪资要求" : itemSalary);
        strings.add("企业性质");
        strings.add("更多筛选");

        ArrayList<View> views = new ArrayList<>();
        views.add(salaryInfo);
        views.add(companyInfo);
        views.add(moreInfo);

        ArrayList<Integer> integers = new ArrayList<Integer>();
        int i = DensityUtil.dip2px(mcontext, 45);
        integers.add(5 * i);
        integers.add(5 * i);
        integers.add(5 * i);
        etvMenu.setValue(strings, views, integers);

        etvMenu.postDelayed(new Runnable() {
            @Override
            public void run() {
                startRefresh();
            }
        }, 500);
    }

    @Override
    protected void getDataFronServer() {

        HashMap<String, Object> Object = new HashMap<String, Object>();
        Object.put("page", page);
        Object.put("jobFrom", "2");


        {
            if (!StringUtil.isEmpty(searchKeyWorld))//关键字
                Object.put("keyword", searchKeyWorld);
        }

        if (!StringUtil.isEmpty(itemIndtypeId))//行业
            Object.put("industry", itemIndtypeId.replaceAll("#", ","));

        if (!StringUtil.isEmpty(itemJobfuncId))//岗位
            Object.put("respon", itemJobfuncId.replaceAll("#", ","));

        if (!StringUtil.isEmpty(itemSalaryId))//薪资
            Object.put("salary", itemSalaryId);

        if (!StringUtil.isEmpty(itemDegreeId))//学历
            Object.put("degree", itemDegreeId);

        if (!StringUtil.isEmpty(jobTypeId))//工作性质
            Object.put("jobType", jobTypeId);


        if (!StringUtil.isEmpty(corpkindId))//企业性质
            Object.put("corpkind", corpkindId);

        if (!StringUtil.isEmpty(itemAddressId)) {
            if (itemAddressId.startsWith("-1")) {
                Object.put("jobCity", itemAddressId.substring(3, itemAddressId.length()));
            } else {
                Object.put("jobCity", itemAddressId);
            }
        }
        HttpUtil.post(URLS.API_JOB_CampusJoblist, Object, this);
    }


    @Override
    public void onSuccess(String tag, Object data) {
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
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        setTopTitle("共0条");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        searchKeyWorld = getIntent().getStringExtra("searchKeyWorld");
        itemAddress = getIntent().getStringExtra("itemAddress");
        itemJobfunc = getIntent().getStringExtra("itemJobfunc");
        itemIndtype = getIntent().getStringExtra("itemIndtype");

        itemAddressId = getIntent().getStringExtra("itemAddressId");
        itemJobfuncId = getIntent().getStringExtra("itemJobfuncId");
        itemIndtypeId = getIntent().getStringExtra("itemIndtypeId");

        if (!StringUtil.isEmpty(itemAddressId) && itemAddressId.startsWith("-1")) {
            isPro = true;
        } else {
            isPro = false;
        }
        UpdateDataTaskUtils.selectSalaryInfo(this, this);
        UpdateDataTaskUtils.selectCompanyData(this, this);
        UpdateDataTaskUtils.selectCompusInfo(this, isPro, itemAddressId, this);
    }

    @Override
    public void onGetCompanyInfo(List<JSONObject> CompanyData) {
        Message message = new Message();
        message.what = UpdateDataTaskUtils.COMPANYDATA;
        message.obj = CompanyData;
        corpkindId = corpkindId + "";
        handler.sendMessage(message);
    }

    @Override
    public void onGetDiscussSalaryInfo(List<JSONObject> salaryData) {
        Message message = new Message();
        message.what = UpdateDataTaskUtils.SALARYDATA;
        message.obj = salaryData;
        this.salaryData = salaryData;
        handler.sendMessage(message);
    }

    @Override
    public void onGetDiscussMoreInfo(Map<String, List<JSONObject>> MoreData) {
        Message message = new Message();
        message.what = UpdateDataTaskUtils.CAMPUSMOREDATA;
        message.obj = MoreData;
        this.moreData = MoreData;
        handler.sendMessage(message);
    }


    public void setBottomVisible(Boolean isShow) {
        bottomBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        ArrayList<Integer> checkPosition = ((CmapusResultAdapter) mAdapter).getCheckPosition();
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
            HttpUtil.post(URLS.API_JOB_favorite, param, new HttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, String tag) {
                    TipsUtil.show(mcontext, "收藏失败");
                }

                @Override
                public void onSuccess(String tag, Object data) {
                    TipsUtil.show(mcontext, ((JSONObject) data).optString("message"));
                }

                @Override
                public void onError(int errorCode, String tag, String errorMessage) {
                }

                @Override
                public void onProgress(String tag, int progress) {
                }
            });

        } else if (i == R.id.item_send) {
            //投递简历
            if (!GoodJobsApp.getInstance().checkLogin(mcontext))
                return;

            HashMap<String, Object> param = new HashMap<>();
            param.put("jobID", ids);
            param.put("ft", 2);
            HttpUtil.post(URLS.API_JOB_apply, param, new HttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, String tag) {
                    TipsUtil.show(mcontext, "简历投递失败");
                }

                @Override
                public void onSuccess(String tag, Object data) {
                    TipsUtil.show(mcontext, ((JSONObject) data).optString("message"));
                }

                @Override
                public void onError(int errorCode, String tag, String errorMessage) {
                }

                @Override
                public void onProgress(String tag, int progress) {
                }
            });
        }
    }

}
