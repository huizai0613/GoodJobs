package cn.goodjobs.bluecollar.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueJobCompanyDetailActivity;
import cn.goodjobs.bluecollar.activity.BlueJobDetailActivity;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.activity.LsMapActivity;
import cn.goodjobs.common.baseclass.BaseViewPagerFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.GeoUtils;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.PhoneUtils;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.UMShareUtil;
import cn.goodjobs.common.util.UpdateDataTaskUtils;
import cn.goodjobs.common.util.ViewHolderUtil;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.BabushkaText;
import cn.goodjobs.common.view.ExtendedTouchView;
import cn.goodjobs.common.view.XCFlowLayout;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by yexiangyu on 15/12/28.
 */
public class BlueJobDetailFragment extends BaseViewPagerFragment
{

    private BabushkaText jobWorkSalary;
    private BabushkaText jobSalary;
    private BabushkaText jobWorkTime;
    private BabushkaText jobNum;
    private BabushkaText jobPhone;
    private BabushkaText jobRequirement;
    private BabushkaText jobTime;
    private View jobBrightBox;
    private XCFlowLayout jobBright;
    private TextView jobContent;
    private TextView jobOther;
    private View jobOtherBox;
    private LinearLayout jobSimilarBox;
    private TextView jobName;
    private TextView jobCro;
    private BlueJobDetailActivity activity;
    private int id;
    private TextView jobStore;
    private TextView jobSend;
    private int memCorpID;
    private BabushkaText jobAddress;
    private TextView jobShare;
    private ImageView itemCertify;
    private ImageView itemVip;
    private String lng;
    private String lat;
    private JSONObject jobDetailJson;
    private View jobCroBox;


    public JSONObject getJobDetailJson()
    {
        return jobDetailJson;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = (BlueJobDetailActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View inflate = inflater.inflate(R.layout.fragment_bluejob_detail, null);

        jobSalary = (BabushkaText) inflate.findViewById(R.id.job_salary);
        jobWorkSalary = (BabushkaText) inflate.findViewById(R.id.job_work_salary);
        jobWorkTime = (BabushkaText) inflate.findViewById(R.id.job_worktime);
        jobNum = (BabushkaText) inflate.findViewById(R.id.job_num);
        jobPhone = (BabushkaText) inflate.findViewById(R.id.job_phone);
        jobRequirement = (BabushkaText) inflate.findViewById(R.id.job_requirement);
        jobTime = (BabushkaText) inflate.findViewById(R.id.job_time);
        jobAddress = (BabushkaText) inflate.findViewById(R.id.job_address);

        itemCertify = (ImageView) inflate.findViewById(R.id.item_certify);
        itemVip = (ImageView) inflate.findViewById(R.id.item_vip);

        jobBrightBox = inflate.findViewById(R.id.job_bright_box);
        jobSimilarBox = (LinearLayout) inflate.findViewById(R.id.job_similar_box);
        jobBright = (XCFlowLayout) inflate.findViewById(R.id.job_bright);

        jobContent = (TextView) inflate.findViewById(R.id.job_content);
        jobName = (TextView) inflate.findViewById(R.id.job_name);
        jobCro = (TextView) inflate.findViewById(R.id.job_cro);
        jobCroBox = inflate.findViewById(R.id.job_cro_box);

        jobStore = (TextView) inflate.findViewById(R.id.job_store);
        jobShare = (TextView) inflate.findViewById(R.id.job_share);
        jobSend = (TextView) inflate.findViewById(R.id.job_send);

        jobStore.setOnClickListener(this);
        jobShare.setOnClickListener(this);
        jobSimilarBox.setOnClickListener(this);
        jobCroBox.setOnClickListener(this);
        jobSend.setOnClickListener(this);
        jobPhone.setOnClickListener(this);


        Drawable iconPhone = getResources().getDrawable(R.mipmap.phone);
        iconPhone.setBounds(0, 0, DensityUtil.dip2px(mActivity, 25), DensityUtil.dip2px(mActivity, 25));
        jobPhone.setCompoundDrawables(null, null, iconPhone, null);

        Drawable iconStore = getResources().getDrawable(R.drawable.icon_wite_store);
        iconStore.setBounds(0, 0, DensityUtil.dip2px(mActivity, 30), DensityUtil.dip2px(mActivity, 30));
        jobStore.setCompoundDrawables(null, iconStore, null, null);

        Drawable iconShare = getResources().getDrawable(R.drawable.share);
        iconShare.setBounds(0, 0, DensityUtil.dip2px(mActivity, 30), DensityUtil.dip2px(mActivity, 30));
        jobShare.setCompoundDrawables(null, iconShare, null, null);

        jobOtherBox = inflate.findViewById(R.id.job_other_box);
        jobOther = (TextView) inflate.findViewById(R.id.job_other);
        errorLayout = (EmptyLayout) inflate.findViewById(R.id.error_layout);
        errorLayout.setOnLayoutClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getData();
            }
        });
        getData();
        return inflate;
    }


    public void getData()
    {
        id = this.getArguments().getInt("ID");
        if (activity.getCacheData().containsKey(id)) {
            errorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            setData(activity.getCacheData().get(id));
        } else {
            errorLayout.setOnLayoutClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    errorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("blueJobID", id);
                    HttpUtil.post(URLS.API_BLUEJOB_Jobshow, hashMap, BlueJobDetailFragment.this);
                }
            });
            errorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("blueJobID", id);
            HttpUtil.post(URLS.API_BLUEJOB_Jobshow, hashMap, this);
        }
    }


    public void setData(JSONObject dataJson)
    {
        activity.getCacheData().put(id, dataJson);
        JSONArray treatmentArr = dataJson.optJSONArray("treatment");
        if (treatmentArr != null && treatmentArr.length() > 0) {
            jobBrightBox.setVisibility(View.VISIBLE);
            jobBright.removeAllViews();
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = 5;
            lp.rightMargin = 5;
            lp.topMargin = 5;
            lp.bottomMargin = 5;
            for (int i = 0; i < treatmentArr.length(); i++) {
                TextView view = new TextView(mActivity);
                view.setText(treatmentArr.optString(i));
                view.setGravity(Gravity.CENTER);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_small));
                view.setPadding(DensityUtil.dip2px(mActivity, 10), 0, DensityUtil.dip2px(mActivity, 10), 0);
                view.setHeight(DensityUtil.dip2px(mActivity, 25));
                view.setBackgroundResource(R.drawable.bright_bg);
                view.setTextColor(getResources().getColor(R.color.main_color));
                jobBright.addView(view, lp);
            }
        }


        itemCertify.setImageResource(dataJson.optInt("certStatus") == 0 ? R.mipmap.icon_uncertify : R.mipmap.icon_certify);
        itemCertify.setVisibility(dataJson.optInt("blueFlag") == 0 ? View.VISIBLE : View.GONE);


        setStrng2Bab(jobWorkSalary, "加班补贴: ", dataJson.optString("extraWorkFeeName"));

        if (StringUtil.isEmpty(dataJson.optString("salaryName"))) {
            jobSalary.setVisibility(View.GONE);
            return;
        }
        jobSalary.setVisibility(View.VISIBLE);
        jobSalary.addPiece(new BabushkaText.Piece.Builder("月        薪:")
                .textColor(Color.parseColor("#999999"))
                .build());
        // Add the second piece "1.2 mi"
        jobSalary.addPiece(new BabushkaText.Piece.Builder(dataJson.optString("salaryName"))
                .textColor(Color.parseColor("#ff0000"))
                .build());

        jobSalary.display();

        setStrng2Bab(jobWorkTime, "作息时间: ", dataJson.optString("relaxTime"));
        setStrng2Bab(jobNum, "招聘人数: ", dataJson.optString("jobOfferNum"));
        setStrng2Bab(jobRequirement, "职位要求: ", dataJson.optString("jobTypeName"));
        setStrng2Bab(jobPhone, "联系电话: ", dataJson.optString("phone") + " ");
        setStrng2Bab(jobTime, "招聘日期: ", dataJson.optString("jobfairDate"));
        setStrng2Bab(jobAddress, "工作地点: ", dataJson.optString("address"));


        lng = dataJson.optString("lng");
        lat = dataJson.optString("lat");
        if (!StringUtil.isEmpty(lng) && !StringUtil.isEmpty(lat)) {
            Drawable iconMap = getResources().getDrawable(R.drawable.mapm);
            iconMap.setBounds(0, 0, DensityUtil.dip2px(mActivity, 15), DensityUtil.dip2px(mActivity, 15));
            jobAddress.setCompoundDrawables(null, null, iconMap, null);
            jobAddress.setOnClickListener(this);
        }


        memCorpID = dataJson.optInt("memCorpID");
        jobContent.setText(dataJson.optString("jobDetail"));
        String other = dataJson.optString("other");
        if (StringUtil.isEmpty(other)) {
            jobOtherBox.setVisibility(View.GONE);
        } else {
            jobOtherBox.setVisibility(View.VISIBLE);
            jobOther.setText(other);
        }

        jobName.setText(dataJson.optString("jobName"));
        jobCro.setText(dataJson.optString("corpName"));

        final JSONArray list = dataJson.optJSONArray("list");

        if (list != null && list.length() > 0) {
            jobSimilarBox.removeAllViews();
            StringBuilder builder = new StringBuilder();
            int length = list.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    builder.append(list.optJSONObject(i).optString("blueJobID") + ",");
                }
                final String charSequence = builder.subSequence(0, builder.length() - 1).toString();

                for (int i = 0; i < list.length(); i++) {

                    final int curPosition = i;

                    View inflate = View.inflate(mActivity, R.layout.item_bluejob, null);

                    ExtendedTouchView itemCheck = ViewHolderUtil.get(inflate, R.id.item_check);
                    final CheckBox itemC = ViewHolderUtil.get(inflate, R.id.item_c);
                    TextView item_title = ViewHolderUtil.get(inflate, R.id.item_title);
                    TextView item_salary = ViewHolderUtil.get(inflate, R.id.item_salary);
                    TextView item_company_name = ViewHolderUtil.get(inflate, R.id.item_company_name);
                    TextView item_dis = ViewHolderUtil.get(inflate, R.id.item_dis);
                    LinearLayout item_treatment_box = ViewHolderUtil.get(inflate, R.id.item_treatment_box);
                    ImageView item_certify = ViewHolderUtil.get(inflate, R.id.item_certify);
                    ImageView item_vip = ViewHolderUtil.get(inflate, R.id.item_vip);
                    TextView item_time = ViewHolderUtil.get(inflate, R.id.item_time);

                    JSONObject data = list.optJSONObject(i);

                    item_title.setText(data.optString("jobName"));
                    item_salary.setText(data.optString("salaryName"));
                    item_company_name.setText(data.optString("corpName"));
                    item_time.setText(data.optString("pubDate"));

                    String mapLng = data.optString("mapLng");
                    String mapLat = data.optString("mapLat");
                    String cityName = data.optString("cityName");

                    if (!StringUtil.isEmpty(mapLng) && !StringUtil.isEmpty(mapLat) && activity.myLocation != null) {
                        Drawable iconDis = mActivity.getResources().getDrawable(R.mipmap.icon_bluedis);
                        iconDis.setBounds(0, 0, DensityUtil.dip2px(mActivity, 18), DensityUtil.dip2px(mActivity, 18));
                        item_dis.setCompoundDrawables(iconDis, null, null, null);


                        double distance = GeoUtils.
                                distance(activity.myLocation.latitude, activity.myLocation.longitude, Double.parseDouble(mapLat), Double.parseDouble(mapLng));
                        if (distance > 1000) {
                            item_dis.setText(distance / 1000 + "千米");
                        } else {
                            item_dis.setText(distance + "米");
                        }

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
                    item_treatment_box.removeAllViews();
                    if (treatment != null) {
                        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        int i1 = DensityUtil.dip2px(mActivity, 1);
                        for (int j = 0; j < (treatment.length() > 3 ? 3 : treatment.length()); j++) {
                            TextView item = new TextView(mActivity);
                            item.setPadding(i1, i1, i1, i1);
                            item.setBackgroundResource(R.drawable.bg_welfare);
                            item.setGravity(Gravity.CENTER);
                            if (j == 2) {
                                item.setText("·  ·  ·");
                            } else {
                                item.setText(treatment.optString(i));
                            }

                            if (j == 1) {
                                p.rightMargin = p.leftMargin = DensityUtil.dip2px(mActivity, 2);
                            }

                            item.setTextColor(Color.parseColor("#6bbd00"));
                            item.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActivity.getResources().getDimension(R.dimen.text_litter));
                            item_treatment_box.addView(item, p);
                        }
                    }


                    itemCheck.setVisibility(View.GONE);
                    itemC.setVisibility(View.GONE);

                    inflate.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {

                            HashMap<String, Object> param = new HashMap<>();
                            param.put("POSITION", curPosition);
                            param.put("IDS", charSequence);
                            JumpViewUtil.openActivityAndParam(mActivity, BlueJobDetailActivity.class, param);
                        }
                    });


                    jobSimilarBox.addView(inflate, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }

            }
        }

    }


    private void setStrng2Bab(BabushkaText tv, String title, String content)
    {

        if (StringUtil.isEmpty(content)) {
            tv.setVisibility(View.GONE);
            return;
        }
        tv.setVisibility(View.VISIBLE);
        tv.addPiece(new BabushkaText.Piece.Builder(title)
                .textColor(Color.parseColor("#999999"))
                .build());
        // Add the second piece "1.2 mi"
        tv.addPiece(new BabushkaText.Piece.Builder(content)
                .textColor(Color.parseColor("#606060"))
                .build());

        tv.display();


    }


    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        jobDetailJson = (JSONObject) data;
        setData(jobDetailJson);

    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);
        errorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage)
    {
        super.onError(errorCode, tag, errorMessage);
        errorLayout.setErrorType(EmptyLayout.NODATA);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.job_store) {
            //收藏职位
            if (!GoodJobsApp.getInstance().checkLogin(activity))
                return;
            HashMap<String, Object> param = new HashMap<>();
            param.put("blueJobID", id);
            HttpUtil.post(URLS.API_BLUEJOB_Addfavorite, param, new HttpResponseHandler()
            {
                @Override
                public void onFailure(int statusCode, String tag)
                {
                    TipsUtil.show(mActivity, "收藏失败");
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

        } else if (i == R.id.job_send) {
            //投递简历
            if (!GoodJobsApp.getInstance().checkLogin(activity))
                return;
            HashMap<String, Object> param = new HashMap<>();
            param.put("blueJobID", id);
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
        } else if (i == R.id.job_cro_box) {

            HashMap<String, Object> param = new HashMap<>();
            param.put("corpID", memCorpID);
            JumpViewUtil.openActivityAndParam(mActivity, BlueJobCompanyDetailActivity.class, param);

        } else if (i == R.id.job_share) {
            //分享
            UMShareUtil.setShareText(getActivity(), jobName.getText().toString(), id + "");
            UMShareUtil.getUMSocialService().openShare(getActivity(), false);
        } else if (i == R.id.job_phone) {
            PhoneUtils.makeCall(jobPhone.getText().toString(), mActivity);
        } else if (i == R.id.job_address) {
            //进入地图
            LsMapActivity.openMap(mActivity, Double.parseDouble(lng), Double.parseDouble(lat),
                    jobDetailJson.optString("corpName"), jobDetailJson.optString("address"));
        }

    }
}
