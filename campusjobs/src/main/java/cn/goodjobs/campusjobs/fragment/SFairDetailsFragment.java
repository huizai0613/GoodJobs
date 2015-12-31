package cn.goodjobs.campusjobs.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.activity.CareerTalkDetailsActivity;
import cn.goodjobs.campusjobs.activity.SJobFairDetailsActivity;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

/**
 * Created by zhuli on 2015/12/29.
 */
public class SFairDetailsFragment extends BaseFragment {

    private int id;
    private int position;
    private View view;
    public SJobFairDetailsActivity sJobFairDetailsActivity;
    private TextView corpName, pubDate, schoolAddress, fairWeek, schoolName;
    private WebView wv;


    public SFairDetailsFragment() {
    }

    public SFairDetailsFragment(int id, int position) {
        this.id = id;
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sfairdetails, null);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sJobFairDetailsActivity = (SJobFairDetailsActivity) context;
    }

    public void initView(View view) {
        view.setVisibility(View.INVISIBLE);
        corpName = (TextView) view.findViewById(R.id.tv_corpName);
        pubDate = (TextView) view.findViewById(R.id.tv_pubDate);
        schoolAddress = (TextView) view.findViewById(R.id.tv_schoolAddress);
        fairWeek = (TextView) view.findViewById(R.id.tv_fairWeek);
        schoolName = (TextView) view.findViewById(R.id.tv_schoolName);
        wv = (WebView) view.findViewById(R.id.wv_description);
        WebSettings wSet = wv.getSettings();
        wSet.setJavaScriptEnabled(true);

        if (sJobFairDetailsActivity.jsonObjects[position] != null) {
            initJsonData(sJobFairDetailsActivity.jsonObjects[position]);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (sJobFairDetailsActivity.jsonObjects[position] == null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("fairID", id);
                LoadingDialog.showDialog(getActivity());
                HttpUtil.post(URLS.API_JOB_Jobfairshow, params, this);
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        sJobFairDetailsActivity.jsonObjects[position] = object;
        initJsonData(object);
    }

    private void initJsonData(JSONObject object) {
        corpName.setText(object.optString("corpName"));
        pubDate.setText(object.optString("pubDate"));
        schoolAddress.setText(object.optString("address"));
        fairWeek.setText(object.optString("fairWeek"));
        schoolName.setText(object.optString("schoolName"));
        wv.loadDataWithBaseURL(null, object.optString("description"), "text/html", "UTF-8", null);
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
