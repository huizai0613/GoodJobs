package cn.goodjobs.parttimejobs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.PartTimeItemView;
import cn.goodjobs.parttimejobs.R;
import cn.goodjobs.parttimejobs.activity.PartJobDetailsActivity;

/**
 * Created by zhuli on 2015/12/25.
 */
public class PartJobDetailsFragment extends BaseFragment {

    private int id;
    private int position;
    private View view;
    private TextView tvDetails;
    public PartJobDetailsActivity partJobDetailsActivity;
    private PartTimeItemView pivJob, pivCompany, pivSalary, pivPlace, pivDate, pivEndDate, pivConnect, pivPhone, pivWorktime;


    public PartJobDetailsFragment() {
    }

    public PartJobDetailsFragment(int id, int position) {
        this.id = id;
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_partjob_details, null);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        partJobDetailsActivity = (PartJobDetailsActivity) context;
    }

    public void initView(View view) {
        view.setVisibility(View.INVISIBLE);
        pivWorktime = (PartTimeItemView) view.findViewById(R.id.piv_worktime);
        pivJob = (PartTimeItemView) view.findViewById(R.id.piv_job);
        pivCompany = (PartTimeItemView) view.findViewById(R.id.piv_company);
        pivSalary = (PartTimeItemView) view.findViewById(R.id.piv_salary);
        pivPlace = (PartTimeItemView) view.findViewById(R.id.piv_place);
        pivDate = (PartTimeItemView) view.findViewById(R.id.piv_date);
        pivEndDate = (PartTimeItemView) view.findViewById(R.id.piv_enddate);
        pivConnect = (PartTimeItemView) view.findViewById(R.id.piv_connect);
        pivPhone = (PartTimeItemView) view.findViewById(R.id.piv_phone);
        tvDetails = (TextView) view.findViewById(R.id.tv_details);

        if (partJobDetailsActivity.jsonObjects[position] != null) {
            initJsonData(partJobDetailsActivity.jsonObjects[position]);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("HeadDetailsFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (partJobDetailsActivity.jsonObjects[position] == null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("jobID", id);
                HttpUtil.post(URLS.API_JOB_ParttimeShow, params, this);
                LoadingDialog.showDialog(getActivity());
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        partJobDetailsActivity.jsonObjects[position] = object;
        initJsonData(object);
    }

    private void initJsonData(JSONObject object) {
        pivWorktime.setText(object.optString("workTypeName"));
        pivJob.setText(object.optString("jobName"));
        pivCompany.setText(object.optString("corpName"));
        pivSalary.setText(object.optString("jobSalary"));
        pivPlace.setText(object.optString("workArea"));
        pivDate.setText(object.optString("pubDate"));
        pivConnect.setText(object.optString("linkMan"));
        pivEndDate.setText(object.optString("endDate"));
        pivPhone.setText(object.optString("phone"));
        tvDetails.setText(object.optString("jobDetail"));
        view.setVisibility(View.VISIBLE);
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
