package cn.goodjobs.bluecollar.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueSearchActivity;
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
import cn.goodjobs.common.view.ExtendedTouchView;
import cn.goodjobs.common.view.LoadingDialog;
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
        initBuleAd((JSONArray) data);
        initnterest((JSONArray) data);
    }

    private void initnterest(JSONArray data)
    {
        if (data != null) {
            int length = 6;
            for (int i = 0; i < length; i++) {
                View inflate = View.inflate(getContext(), R.layout.item_bluejob, null);
                initnterestView(data.optJSONObject(i), inflate);
                jobBox.addView(inflate);
            }
        }
    }

    private void initnterestView(JSONObject data, View view)
    {
        ExtendedTouchView itemCheck = (ExtendedTouchView) view.findViewById(R.id.item_check);
        final CheckBox itemC = (CheckBox) view.findViewById(R.id.item_c);

        itemCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemC.setChecked(!itemC.isChecked());
            }
        });


    }

    private void initBuleAd(JSONArray data)
    {
        if (data != null) {
            int length = 6;
            if (length > 0) {
                historyLayout.setVisibility(View.VISIBLE);
                int screenW = DensityUtil.getScreenW(getContext());
                int width = historyLayout.getWidth();
                int itemW = 0;
                int padding = (int) getResources().getDimension(R.dimen.padding_default);
                if (width == 0) {
                    itemW = (screenW - 4 * padding) / 3;
                } else {
                    itemW = (width - 2 * padding) / 3;
                }

                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(itemW, LinearLayout.LayoutParams.MATCH_PARENT);
                LinearLayout.LayoutParams paramM = new LinearLayout.LayoutParams(itemW, LinearLayout.LayoutParams.MATCH_PARENT);
                paramM.leftMargin = padding;
                paramM.rightMargin = padding;
                for (int i = 0; i < 3; i++) {
                    View inflate = View.inflate(getContext(), R.layout.item_bluehome_ad, null);
                    initItem(inflate, itemW);
                    if (i == 1) {
                        historyLayout.addView(inflate, paramM);
                    } else {
                        historyLayout.addView(inflate, param);
                    }
                }

                if (length > 3) {
                    historyLayout2.setVisibility(View.VISIBLE);
                    for (int i = 3; i < 6; i++) {
                        View inflate = View.inflate(getContext(), R.layout.item_bluehome_ad, null);
                        initItem(inflate, itemW);
                        if (i == 4) {
                            historyLayout2.addView(inflate, paramM);
                        } else {
                            historyLayout2.addView(inflate, param);
                        }
                    }
                }
            }
        }
    }

    private void initItem(View view, int itemW)
    {
        SimpleDraweeView itemIv = (SimpleDraweeView) view.findViewById(R.id.item_iv);
        TextView itemTv = (TextView) view.findViewById(R.id.item_tv);
        itemIv.getLayoutParams().width = itemW;
        itemTv.getLayoutParams().width = itemW;
        itemIv.getLayoutParams().height = itemW / 3;
        view.invalidate();

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

        blueSearchBut = view.findViewById(R.id.blue_search_but);
        historyLayout = (LinearLayout) view.findViewById(R.id.historyLayout);
        historyLayout2 = (LinearLayout) view.findViewById(R.id.historyLayout2);
        jobBox = (LinearLayout) view.findViewById(R.id.job_box);
        blueSearchBut.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.blue_search_but) {
            JumpViewUtil.openActivityAndParam(getContext(), BlueSearchActivity.class, new HashMap<String, Object>());
        }
    }
}
