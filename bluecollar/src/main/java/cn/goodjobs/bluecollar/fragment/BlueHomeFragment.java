package cn.goodjobs.bluecollar.fragment;


import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueCollarActivity;
import cn.goodjobs.bluecollar.activity.BlueJobDetailActivity;
import cn.goodjobs.bluecollar.activity.BlueSearchActivity;
import cn.goodjobs.common.AndroidBUSBean;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.GeoUtils;
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
import cn.goodjobs.common.view.ExtendedTouchView;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.highlight.HighLight;
import cn.goodjobs.common.view.searchItem.SelectorItemView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlueHomeFragment extends BaseFragment
{


    private EditText etSearch;
    private LinearLayout historyLayout;
    private LinearLayout historyLayout2;
    private SelectorItemView itemAddress;
    private SelectorItemView itemJobfunc;
    private Button btnSearch;
    private View blueSearchBut;
    private LinearLayout jobBox;
    ArrayList<Integer> selectJobIds = new ArrayList<Integer>();
    private View applyjobBut;
    private JSONObject jsonObject;
    private MyLocation myLocation;
    private BlueCollarActivity activity;
    private boolean isLoadSuccess;
    private HighLight mHightLight;


    @Subscriber(tag = URLS.JOB_bluehome_login)
    protected void acceptEventBus(AndroidBUSBean androidBUSBean)
    {
        int status = androidBUSBean.getStatus();

        switch (status) {
            case AndroidBUSBean.STATUSREFRESH:
                //登陆成功刷新
                getDataFromServer();
                break;
        }
    }

    @Subscriber(tag = URLS.JOB_bluehome_unlogin)
    protected void acceptunEventBus(AndroidBUSBean androidBUSBean)
    {
        int status = androidBUSBean.getStatus();

        switch (status) {
            case AndroidBUSBean.STATUSREFRESH:
                //登陆成功刷新
                getDataFromServer();
                break;
        }
    }

    private void getDataFromServer()
    {
        LoadingDialog.showDialog(getActivity());

        HashMap<String, Object> param = new HashMap<>();
        if (myLocation != null) {
            param.put("lat", myLocation.latitude);
            param.put("lng", myLocation.longitude);
        }
        HttpUtil.post(URLS.API_BLUEJOB_Index, param, this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        LogUtil.info("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        activity = (BlueCollarActivity) mActivity;
        LocationUtil.newInstance(mActivity.getApplication()).startLoction(new MyLocationListener()
        {
            @Override
            public void loaction(MyLocation location)
            {
                LogUtil.info(location.toString());
                SharedPrefUtil.saveObjectToLoacl("location", location);
                myLocation = location;
                if (jsonObject != null) {
                    runMainThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            HashMap<String, Object> param = new HashMap<>();
                            if (myLocation != null) {
                                param.put("lat", myLocation.latitude);
                                param.put("lng", myLocation.longitude);
                            }
                            HttpUtil.post(URLS.API_BLUEJOB_Index, param, BlueHomeFragment.this);
                        }
                    });

                }
                GoodJobsApp.getInstance().setMyLocation(location);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_blue_home, container, false);
        initView(view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isLoadSuccess) {
            getDataFromServer();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        isLoadSuccess = true;
        jsonObject = (JSONObject) data;
        activity.setCurSelectJobCate(jsonObject.optString("getBlueFunID"));
        JSONArray adsList = jsonObject.optJSONArray("adsList");
        JSONArray hotJob = jsonObject.optJSONArray("hotJob");
        JSONArray likeJob = jsonObject.optJSONArray("likeJob");

        initAd(adsList);
        initBuleAd(hotJob);
        initnterest(likeJob);
    }

    private void initnterest(JSONArray data)
    {
        if (data != null) {
            jobBox.removeAllViews();
            int length = data.length();
            if (length > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    builder.append(data.optJSONObject(i).optInt("blueJobID") + ",");
                }
                String charSequence = builder.subSequence(0, builder.length() - 1).toString();

                for (int i = 0; i < length; i++) {
                    View inflate = View.inflate(mActivity, R.layout.item_bluejob, null);
                    initnterestView(data.optJSONObject(i), inflate, charSequence, i);
                    jobBox.addView(inflate);
                    if (i == 0) {
                        Boolean blue_first = SharedPrefUtil.getBoolean(mActivity, "blue_first");
                        if (blue_first == null || !blue_first)
                            showTipMask(inflate);
                        SharedPrefUtil.saveDataToLoacl("blue_first", true);
                    }
                }
            }
        }
    }

    private void initnterestView(final JSONObject data, View view, final String ids, final int position)
    {
        ExtendedTouchView itemCheck = (ExtendedTouchView) view.findViewById(R.id.item_check);
        final CheckBox itemC = (CheckBox) view.findViewById(R.id.item_c);
        TextView item_title = (TextView) view.findViewById(R.id.item_title);
        TextView item_salary = (TextView) view.findViewById(R.id.item_salary);
        TextView item_company_name = (TextView) view.findViewById(R.id.item_company_name);
        TextView item_dis = (TextView) view.findViewById(R.id.item_dis);
        TextView item_time = (TextView) view.findViewById(R.id.item_time);
        LinearLayout item_treatment_box = (LinearLayout) view.findViewById(R.id.item_treatment_box);
        ImageView item_certify = (ImageView) view.findViewById(R.id.item_certify);
        ImageView item_vip = (ImageView) view.findViewById(R.id.item_vip);


        item_title.setText(data.optString("jobName"));
        item_salary.setText(data.optString("salaryName"));
        item_company_name.setText(data.optString("corpName"));
        item_time.setText(data.optString("pubDate"));

        String mapLng = data.optString("mapLng");
        String mapLat = data.optString("mapLat");
        String cityName = data.optString("cityName");

        MyLocation myLocation = GoodJobsApp.getInstance().getMyLocation();


        if (!StringUtil.isEmpty(mapLng) && !StringUtil.isEmpty(mapLat) && myLocation != null) {
            Drawable iconDis = mActivity.getResources().getDrawable(R.mipmap.icon_bluedis);
            iconDis.setBounds(0, 0, DensityUtil.dip2px(mActivity, 18), DensityUtil.dip2px(mActivity, 18));
            item_dis.setCompoundDrawables(iconDis, null, null, null);
            double distance = GeoUtils.
                    distance(myLocation.latitude, myLocation.longitude, Double.parseDouble(mapLat), Double.parseDouble(mapLng));
            item_dis.setText(" " + GeoUtils.friendlyDistance(distance));
        } else {
            item_dis.setText(cityName);

        }

        String certStatus = data.optString("certStatus");
        String blueFlag = data.optString("blueFlag");


        if ("1".equals(certStatus)) {
            item_certify.setImageResource(R.mipmap.icon_certify);
        } else {
            item_certify.setImageResource(R.mipmap.icon_uncertify);
        }

        if ("0".equals(certStatus)) {
            item_vip.setVisibility(View.VISIBLE);
        } else {
            item_vip.setVisibility(View.GONE);
        }


        JSONArray treatment = data.optJSONArray("treatment");

        if (treatment != null) {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i1 = DensityUtil.dip2px(mActivity, 1);
            for (int i = 0; i < (treatment.length() > 3 ? 3 : treatment.length()); i++) {

                TextView item = new TextView(mActivity);
                item.setPadding(i1, i1, i1, i1);
                item.setBackgroundResource(R.drawable.bg_welfare);
                item.setGravity(Gravity.CENTER);
                if (i == 2) {
                    item.setText("·  ·  ·");
                } else {
                    item.setText(treatment.optString(i));
                }

                if (i == 1) {
                    p.rightMargin = p.leftMargin = DensityUtil.dip2px(mActivity, 2);
                }

                item.setTextColor(Color.parseColor("#6bbd00"));
                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_litter));
                item_treatment_box.addView(item, p);
            }
        }


        itemCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemC.setChecked(!itemC.isChecked());
                if (itemC.isChecked()) {
                    selectJobIds.add((Integer) data.optInt("blueJobID"));
                } else {
                    selectJobIds.remove((Integer) data.optInt("blueJobID"));
                }
                if (selectJobIds.size() > 0) {
                    applyjobBut.setVisibility(View.VISIBLE);
                } else {
                    applyjobBut.setVisibility(View.GONE);
                }

            }
        });

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HashMap<String, Object> param = new HashMap<>();
                param.put("POSITION", position);
                param.put("IDS", ids);
                JumpViewUtil.openActivityAndParam(mActivity, BlueJobDetailActivity.class, param);
            }
        });

    }

    private void initBuleAd(JSONArray data)
    {
        if (data != null && data.length() > 0) {
            historyLayout.removeAllViews();
            historyLayout2.removeAllViews();
            int length = data.length();
            if (length > 0) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < length; i++) {
                    builder.append(data.optJSONObject(i).optInt("blueJobID") + ",");
                }
                String charSequence = builder.subSequence(0, builder.length() - 1).toString();


                historyLayout.setVisibility(View.VISIBLE);

                int screenW = DensityUtil.getScreenW(mActivity);
//                int width = historyLayout.getWidth();
                int itemW = 0;
                int padding = (int) getResources().getDimension(R.dimen.padding_default);
//                if (width == 0) {
                itemW = (screenW - 4 * padding) / 3;
//                } else {
//                    itemW = (width - 2 * padding) / 3;
//                }

                LinearLayout.LayoutParams paramL = new LinearLayout.LayoutParams(itemW, LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams paramR = new LinearLayout.LayoutParams(itemW, LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams paramM = new LinearLayout.LayoutParams(itemW, LinearLayout.LayoutParams.MATCH_PARENT);
                paramM.leftMargin = padding;
                paramL.leftMargin = padding;
                paramM.rightMargin = padding;
                paramR.rightMargin = padding;
                for (int i = 0; i < 3; i++) {
                    View inflate = View.inflate(mActivity, R.layout.item_bluehome_ad, null);
                    initItem(inflate, itemW, data.optJSONObject(i), charSequence, i);
                    if (i == 1) {
                        historyLayout.addView(inflate, paramM);
                    } else if (i == 0) {
                        historyLayout.addView(inflate, paramL);
                    } else if (i == 2) {
                        historyLayout.addView(inflate, paramR);
                    }
                }

                if (length > 3) {
                    historyLayout2.setVisibility(View.VISIBLE);
                    for (int i = 3; i < 6; i++) {
                        View inflate = View.inflate(mActivity, R.layout.item_bluehome_ad, null);
                        initItem(inflate, itemW, data.optJSONObject(i), charSequence, i);
                        if (i == 4) {
                            historyLayout2.addView(inflate, paramM);
                        } else if (i == 3) {
                            historyLayout2.addView(inflate, paramL);
                        } else if (i == 5) {
                            historyLayout2.addView(inflate, paramR);
                        }
                    }
                }
            } else {
                historyLayout.setVisibility(View.GONE);
            }
        }
    }

    private void initItem(View view, int itemW, JSONObject data, final String ids, final int position)
    {
        SimpleDraweeView itemIv = (SimpleDraweeView) view.findViewById(R.id.item_iv);
        TextView itemTv = (TextView) view.findViewById(R.id.item_tv);
        itemIv.getLayoutParams().width = itemW;
        itemTv.getLayoutParams().width = itemW;
        itemIv.getLayoutParams().height = itemW / 3;

        itemTv.setText(data.optString("jobName"));

        itemIv.setImageURI(Uri.parse(data.optString("corpLogo")));

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//  跳转职位详情
                HashMap<String, Object> param = new HashMap<>();
                param.put("POSITION", position);
                param.put("IDS", ids);
                JumpViewUtil.openActivityAndParam(mActivity, BlueJobDetailActivity.class, param);
            }
        });
    }

    private void initView(final View view)
    {
        setTopTitle(view, "蓝领招聘");
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_left);
        backBtn.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        ((RelativeLayout.LayoutParams) backBtn.getLayoutParams()).leftMargin = DensityUtil.dip2px(mActivity, 10);
        ((RelativeLayout.LayoutParams) backBtn.getLayoutParams()).rightMargin = DensityUtil.dip2px(mActivity, 5);
        backBtn.setImageResource(R.mipmap.icon_blue_home_logo);
        adViewPager = (AutoScrollViewPager) view.findViewById(R.id.adViewPager);


        blueSearchBut = view.findViewById(R.id.blue_search_but);
        historyLayout = (LinearLayout) view.findViewById(R.id.historyLayout);
        historyLayout2 = (LinearLayout) view.findViewById(R.id.historyLayout2);
        jobBox = (LinearLayout) view.findViewById(R.id.job_box);
        blueSearchBut.setOnClickListener(this);
        applyjobBut = view.findViewById(R.id.applyjob_but);
        applyjobBut.setOnClickListener(this);


    }


    private void showTipMask(View view)
    {
        if (mHightLight != null) {
            mHightLight.remove();
            mHightLight = null;
        }
        mHightLight = new HighLight(mActivity)//
                .addHighLight(R.id.item_certify, R.layout.blue_introduce,
                        new HighLight.OnPosCallback()
                        {
                            @Override
                            public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo)
                            {
                                marginInfo.leftMargin = rectF.left - DensityUtil.dip2px(mActivity, 200);
                                marginInfo.topMargin = rectF.bottom - DensityUtil.dip2px(mActivity, 170);
                            }
                        });
//                .addHighLight(R.id.id_btn_amazing, R.layout.info_down, new HighLight.OnPosCallback()
//                {
//                    /**
//                     * @param rightMargin  高亮view在anchor中的右边距
//                     * @param bottomMargin 高亮view在anchor中的下边距
//                     * @param rectF        高亮view的l,t,r,b,w,h都有
//                     * @param marginInfo   设置你的布局的位置，一般设置l,t或者r,b
//                     */
//                    @Override
//                    public void getPos(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo)
//                    {
//                        marginInfo.rightMargin = rightMargin + rectF.width() / 2;
//                        marginInfo.bottomMargin = bottomMargin + rectF.height();
//                    }
//
//                });

        mHightLight.show();
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.blue_search_but) {
            JumpViewUtil.openActivityAndParam(mActivity, BlueSearchActivity.class, new HashMap<String, Object>());
        } else if (i == R.id.applyjob_but) {
            //申请职位
            if (selectJobIds.size() > 0) {
                StringBuilder builder = new StringBuilder();

                for (Integer selectJobId : selectJobIds) {
                    builder.append(selectJobId + ",");
                }
                String ids = builder.subSequence(0, builder.length() - 1).toString();
                if (!GoodJobsApp.getInstance().checkLogin(mActivity))
                    return;

                HashMap<String, Object> param = new HashMap<>();
                param.put("blueJobID", ids);
                param.put("ft", 2);
                HttpUtil.post(URLS.API_BLUEJOB_Addapply, param, new HttpResponseHandler()
                {
                    @Override
                    public void onFailure(int statusCode, String tag)
                    {
                        TipsUtil.show(mActivity, "简历投递失败");
                    }

                    @Override
                    public void onSuccess(String tag, Object data)
                    {
                        TipsUtil.show(mActivity, ((JSONObject) data).optString("message"));
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
            } else {
                TipsUtil.show(mActivity, "您未选择职位");
            }


        }
    }
}
