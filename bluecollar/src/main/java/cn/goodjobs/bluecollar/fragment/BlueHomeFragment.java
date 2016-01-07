package cn.goodjobs.bluecollar.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.LinkedHashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.searchItem.SelectorItemView;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlueHomeFragment extends BaseFragment
{


    private EditText etSearch;
    private View historyLayout;
    private SelectorItemView itemAddress;
    private SelectorItemView itemJobfunc;
    private Button btnSearch;


    private void getDataFromServer()
    {
        LoadingDialog.showDialog(getActivity());
        HttpUtil.post(URLS.API_IMG_AD, this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        LogUtil.info("onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        LocationUtil.newInstance(getActivity().getApplication()).startLoction(new MyLocationListener()
        {
            @Override
            public void loaction(MyLocation location)
            {
                LogUtil.info(location.toString());
                SharedPrefUtil.saveObjectToLoacl("location", location);

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_blue_home, container, false);
        initView(view);
        LogUtil.info("onCreateView");
        getDataFromServer();

        return view;
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

    private void initView(View view)
    {
        setTopTitle(view, "蓝领招聘");
        ImageButton backBtn = (ImageButton) view.findViewById(R.id.btn_left);
        backBtn.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        ((RelativeLayout.LayoutParams) backBtn.getLayoutParams()).leftMargin = DensityUtil.dip2px(getContext(), 10);
        ((RelativeLayout.LayoutParams) backBtn.getLayoutParams()).rightMargin = DensityUtil.dip2px(getContext(), 5);
        backBtn.setImageResource(R.mipmap.icon_blue_home_logo);
        adViewPager = (AutoScrollViewPager) view.findViewById(R.id.adViewPager);

        etSearch = (EditText) view.findViewById(R.id.etSearch);


        historyLayout = view.findViewById(R.id.historyLayout);
        btnSearch = (Button) view.findViewById(R.id.btnSearch);


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
                }

            }
        });
        etSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        btnSearch.setOnClickListener(this);
    }


}
