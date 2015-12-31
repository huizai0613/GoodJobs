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
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.PartTimeItemView;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CareerDetailsFragment extends BaseFragment {

    private int id;
    private int position;
    private View view;
    public CareerTalkDetailsActivity careerTalkDetailsActivity;
    private TextView corpName, pubDate, schoolAddress, originName, fairWeek, fairClick, schoolName;
    private WebView wv;


    public CareerDetailsFragment() {
    }

    public CareerDetailsFragment(int id, int position) {
        this.id = id;
        this.position = position;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_careerdetails, null);
        initView(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        careerTalkDetailsActivity = (CareerTalkDetailsActivity) context;
    }

    public void initView(View view) {
        view.setVisibility(View.INVISIBLE);
        corpName = (TextView) view.findViewById(R.id.tv_corpName);
        pubDate = (TextView) view.findViewById(R.id.tv_pubDate);
        schoolAddress = (TextView) view.findViewById(R.id.tv_schoolAddress);
        originName = (TextView) view.findViewById(R.id.tv_originName);
        fairWeek = (TextView) view.findViewById(R.id.tv_fairWeek);
        fairClick = (TextView) view.findViewById(R.id.tv_fairClick);
        schoolName = (TextView) view.findViewById(R.id.tv_schoolName);
        wv = (WebView) view.findViewById(R.id.wv_description);
        WebSettings wSet = wv.getSettings();
        wSet.setJavaScriptEnabled(true);

        if (careerTalkDetailsActivity.jsonObjects[position] != null) {
            initJsonData(careerTalkDetailsActivity.jsonObjects[position]);
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (careerTalkDetailsActivity.jsonObjects[position] == null) {
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
        careerTalkDetailsActivity.jsonObjects[position] = object;
        initJsonData(object);
    }

    private void initJsonData(JSONObject object) {
        corpName.setText(object.optString("corpName"));
        pubDate.setText(object.optString("pubDate"));
        schoolAddress.setText(object.optString("address"));
        originName.setText(object.optString("originName"));
        fairWeek.setText(object.optString("fairWeek"));
        fairClick.setText(object.optString("fairClick"));
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
