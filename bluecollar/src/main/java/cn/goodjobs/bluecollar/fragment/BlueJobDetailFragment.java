package cn.goodjobs.bluecollar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueJobDetailActivity;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.BaseViewPagerFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.BabushkaText;
import cn.goodjobs.common.view.XCFlowLayout;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by yexiangyu on 15/12/28.
 */
public class BlueJobDetailFragment extends BaseViewPagerFragment
{

    private BabushkaText bolong;
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
    private EmptyLayout error_layout;
    private BlueJobDetailActivity activity;
    private int id;
    private TextView jobStore;
    private TextView jobSend;
    private int memCorpID;
    private BabushkaText jobAddress;


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

        jobBrightBox = inflate.findViewById(R.id.job_bright_box);
        jobSimilarBox = (LinearLayout) inflate.findViewById(R.id.job_similar_box);
        jobBright = (XCFlowLayout) inflate.findViewById(R.id.job_bright);

        jobContent = (TextView) inflate.findViewById(R.id.job_content);
        jobName = (TextView) inflate.findViewById(R.id.job_name);
        jobCro = (TextView) inflate.findViewById(R.id.job_cro);

        jobStore = (TextView) inflate.findViewById(R.id.job_store);
        jobSend = (TextView) inflate.findViewById(R.id.job_send);

        jobStore.setOnClickListener(this);
        jobSimilarBox.setOnClickListener(this);
        jobCro.setOnClickListener(this);
        jobSend.setOnClickListener(this);

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
            errorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("jobID", id);
            HttpUtil.post(URLS.API_JOB_JobShow, hashMap, this);
        }
    }


    public void setData(JSONObject dataJson)
    {
        activity.getCacheData().put(id, dataJson);
        JSONArray treatmentArr = dataJson.optJSONArray("treatmentArr");
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
                TextView view = new TextView(getContext());
                view.setText(treatmentArr.optString(i));
                view.setGravity(Gravity.CENTER);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_small));
                view.setPadding(DensityUtil.dip2px(getContext(), 10), 0, DensityUtil.dip2px(getContext(), 10), 0);
                view.setHeight(DensityUtil.dip2px(getContext(), 25));
                view.setBackgroundResource(R.drawable.bright_bg);
                view.setTextColor(getResources().getColor(R.color.light_color));
                jobBright.addView(view, lp);
            }
        }
        setStrng2Bab(jobWorkSalary, "部门: ", dataJson.optString("deptName"));
        setStrng2Bab(jobSalary, "月薪: ", dataJson.optString("salary"));
        setStrng2Bab(jobWorkTime, "经验: ", dataJson.optString("worktime"));
        setStrng2Bab(jobNum, "学历: ", dataJson.optString("degree"));
        setStrng2Bab(jobRequirement, "性别: ", dataJson.optString("age"));
        setStrng2Bab(jobPhone, "年龄: ", dataJson.optString("sex"));
        setStrng2Bab(jobTime, "地点: ", dataJson.optString("cityName"));
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
            for (int i = 0; i < list.length(); i++) {
                builder.append(list.optJSONObject(i).optInt("jobID") + ",");
            }
            final String charSequence = builder.subSequence(0, builder.length() - 1).toString();

//            for (int i = 0; i < list.length(); i++) {
//
//                final int curPosition = i;
//
//                View inflate = View.inflate(getContext(), R.layout.item_bluejob, null);

//                TextView title = ViewHolderUtil.get(inflate, R.id.item_title);
//                TextView address = ViewHolderUtil.get(inflate, R.id.item_address);
//                TextView name = ViewHolderUtil.get(inflate, R.id.item_name);
//                BabushkaText salary = ViewHolderUtil.get(inflate, R.id.item_salary);
//                TextView time = ViewHolderUtil.get(inflate, R.id.item_time);
//                CheckBox check = ViewHolderUtil.get(inflate, R.id.item_check);
//                check.setVisibility(View.GONE);
//
//                final JSONObject item = list.optJSONObject(i);
//
//                title.setText(item.optString("jobName"));
//                address.setText(item.optString("jobCity"));
//                name.setText(item.optString("corpName"));
//                salary.setText(item.optString("jobName"));
//                time.setText(item.optString("pubDate"));
//                salary.reset();
//                salary.addPiece(new BabushkaText.Piece.Builder("月薪: ")
//                        .textColor(Color.parseColor("#999999"))
//                        .build());
//
//                // Add the second piece "1.2 mi"
//                salary.addPiece(new BabushkaText.Piece.Builder(item.optString("salary"))
//                        .textColor(Color.parseColor("#ff0000"))
//                        .textSizeRelative(1.0f)
//                        .build());
//
//                salary.display();
//
//
//                inflate.setOnClickListener(new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        HashMap<String, Object> param = new HashMap<>();
//                        param.put("POSITION", curPosition);
//                        param.put("IDS", charSequence);
//                        JumpViewUtil.openActivityAndParam(getContext(), JobDetailActivity.class, param);
//                    }
//                });
//
//                jobSimilarBox.addView(inflate, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//            }

        }

    }


    private void setStrng2Bab(BabushkaText tv, String title, String content)
    {

        if (StringUtil.isEmpty(content)) {
            tv.setVisibility(View.GONE);
            return;
        }

        tv.addPiece(new BabushkaText.Piece.Builder(title)
                .textColor(Color.parseColor("#999999"))
                .build());
        // Add the second piece "1.2 mi"
        tv.addPiece(new BabushkaText.Piece.Builder(content)
                .textColor(Color.parseColor("#000000"))
                .build());

        tv.display();


    }


    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        JSONObject jsonObject = (JSONObject) data;
        setData(jsonObject);

    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);
        errorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
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
            param.put("jobID", id);
            HttpUtil.post(URLS.API_JOB_favorite, param, new HttpResponseHandler()
            {
                @Override
                public void onFailure(int statusCode, String tag)
                {
                    TipsUtil.show(getContext(), "收藏失败");
                }

                @Override
                public void onSuccess(String tag, Object data)
                {
                    TipsUtil.show(getContext(), ((JSONObject) data).optString("message"));
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
            param.put("jobID", id);
            param.put("ft", 2);
            HttpUtil.post(URLS.API_JOB_apply, param, new HttpResponseHandler()
            {
                @Override
                public void onFailure(int statusCode, String tag)
                {
                    TipsUtil.show(getContext(), "简历投递失败");
                }

                @Override
                public void onSuccess(String tag, Object data)
                {
                    TipsUtil.show(getContext(), ((JSONObject) data).optString("message"));
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
        } else if (i == R.id.job_cro) {

            HashMap<String, Object> param = new HashMap<>();
            param.put("corpID", memCorpID);
//            JumpViewUtil.openActivityAndParam(getContext(), BlueJobCompanyDetailActivity.class, param);

        }
    }
}