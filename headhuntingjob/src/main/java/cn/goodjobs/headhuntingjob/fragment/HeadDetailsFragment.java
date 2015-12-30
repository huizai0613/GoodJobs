package cn.goodjobs.headhuntingjob.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LabelView;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.headhuntingjob.R;
import cn.goodjobs.headhuntingjob.activity.HeadDetailsActivity;

/**
 * Created by zhuli on 2015/12/24.
 */
public class HeadDetailsFragment extends BaseFragment {

    private TextView jobName, companyName, jobSalary, jobOfferNum, workTimeName, degreeName, LanguageName, ageName, sexName, cityName, jobDetail;
    private LabelView labelView;
    private RelativeLayout point;
    private List<String> treatment = new ArrayList<String>();
    private int id;
    private int position;
    private View view;
    private HeadDetailsActivity headDetailsActivity;

    public HeadDetailsFragment() {
    }

    public HeadDetailsFragment(int id, int position) {
        this.id = id;
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_headdetails, null);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        headDetailsActivity = (HeadDetailsActivity) context;
    }

    public void initView(View view) {
        view.setVisibility(View.INVISIBLE);
        jobName = (TextView) view.findViewById(R.id.tv_jobName);
        companyName = (TextView) view.findViewById(R.id.tv_companyName);
        jobSalary = (TextView) view.findViewById(R.id.tv_jobSalary);
        jobOfferNum = (TextView) view.findViewById(R.id.tv_jobOfferNum);
        workTimeName = (TextView) view.findViewById(R.id.tv_workTimeName);
        degreeName = (TextView) view.findViewById(R.id.tv_degreeName);
        LanguageName = (TextView) view.findViewById(R.id.tv_LanguageName);
        ageName = (TextView) view.findViewById(R.id.tv_ageName);
        sexName = (TextView) view.findViewById(R.id.tv_sexName);
        cityName = (TextView) view.findViewById(R.id.tv_cityName);
        jobDetail = (TextView) view.findViewById(R.id.tv_jobDetail);
        labelView = (LabelView) view.findViewById(R.id.label_details);
        point = (RelativeLayout) view.findViewById(R.id.rl_point);

        if (headDetailsActivity.jsonObjects[position] != null) {
            initJsonData(headDetailsActivity.jsonObjects[position]);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("HeadDetailsFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (headDetailsActivity.jsonObjects[position] == null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("jobID", id);
                LoadingDialog.showDialog(getActivity());
                HttpUtil.post(URLS.API_JOB_Show, params, this);
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        headDetailsActivity.jsonObjects[position] = object;
        initJsonData(object);
    }

    private void initJsonData(JSONObject object) {
        jobName.setText(object.optString("jobName"));
        companyName.setText(object.optString("companyName"));
        jobSalary.setText(object.optString("jobSalary"));
        jobOfferNum.setText(object.optString("jobOfferNum"));
        workTimeName.setText(object.optString("workTimeName"));
        degreeName.setText(object.optString("degreeName"));
        LanguageName.setText(object.optString("LanguageName"));
        ageName.setText(object.optString("ageName"));
        sexName.setText(object.optString("sexName"));
        cityName.setText(object.optString("cityName"));
        jobDetail.setText(object.optString("jobDetail"));
        JSONArray array = object.optJSONArray("treatmentArr");
        for (int i = 0; i < array.length(); i++) {
            try {
                treatment.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (treatment.size() == 0) {
            point.setVisibility(View.GONE);
        } else {
            initChildViews();
        }
        view.setVisibility(View.VISIBLE);
    }


    private void initChildViews() {
        MarginLayoutParams lp = new MarginLayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 16;
        lp.rightMargin = 16;
        lp.topMargin = 16;
        lp.bottomMargin = 16;
        for (int i = 0; i < treatment.size(); i++) {
            TextView tview = new TextView(getActivity());
            tview.setText(treatment.get(i));
            tview.setTextColor(getResources().getColor(R.color.main_color));
            tview.setBackgroundDrawable(getResources().getDrawable(R.drawable.item_labelview));
            labelView.addView(tview, lp);
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }
}
