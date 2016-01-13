package cn.goodjobs.campusjobs.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.adapter.CareerTalkAdapter;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.http.MetaDataUtil;
import cn.goodjobs.common.view.ExpandTabSuper.ExpandTabView;
import cn.goodjobs.common.view.ExpandTabSuper.SingleLevelMenuView;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.common.view.searchItem.JsonMetaUtil;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CareerTalkFragment extends BaseListFragment {

    private Map<String, String> schoolData = new LinkedHashMap<String, String>();
    private Map<String, String> dateData = new LinkedHashMap<String, String>();
    private SingleLevelMenuView dateInfo, schoolInfo;
    private ExpandTabView etv_career;
    private LinearLayout search;
    private ImageButton btnClear;
    private EditText et;
    private EmptyLayout emptyLayout;
    private boolean isSuccess = false;
    private String keyword = "", runType = "", schoolType = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_career, null);
        initView(view);
        mAdapter = new CareerTalkAdapter(getActivity());
        initList(view);
        return view;
    }

    private void initView(View view) {
        setTopTitle(view, "宣讲会");
        hideBackBtn(view);
        et = (EditText) view.findViewById(R.id.et_career);
        etv_career = (ExpandTabView) view.findViewById(R.id.etv_career);
        btnClear = (ImageButton) view.findViewById(R.id.ib_clear);
        search = (LinearLayout) view.findViewById(R.id.ll_search);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        search.setOnClickListener(this);
        schoolInfo = new SingleLevelMenuView(getActivity());
        dateInfo = new SingleLevelMenuView(getActivity());
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!StringUtil.isEmpty(s.toString())) {
                    btnClear.setVisibility(View.VISIBLE);
                } else {
                    btnClear.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnClear.setOnClickListener(this);
        ArrayList<String> strings = new ArrayList<>();
        strings.add("举办高校");
        strings.add("举办日期");
        ArrayList<View> views = new ArrayList<>();
        views.add(schoolInfo);
        views.add(dateInfo);
        ArrayList<Integer> integers = new ArrayList<Integer>();
        int i = DensityUtil.dip2px(getActivity(), 45);
        integers.add(8 * i);
        integers.add(9 * i);
        etv_career.setValue(strings, views, integers);

        schoolInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                schoolType = selectedKey;
                mAdapter.clear();
                startRefresh();
                etv_career.setTitle(showString, 0);
            }
        });
        dateInfo.setOnSelectListener(new SingleLevelMenuView.OnSelectListener() {
            @Override
            public void onSelected(String selectedKey, String showString) {
                runType = selectedKey;
                mAdapter.clear();
                startRefresh();
                etv_career.setTitle(showString, 1);
            }
        });
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("jt", "careertalk");
        params.put("schoolType", schoolType);
        params.put("runType", runType);
        params.put("keyword", keyword);
        emptyLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        HttpUtil.post(URLS.API_JOB_Jobfairlist, params, this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (!isSuccess) {
                startRefresh();
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        if (!isSuccess) {
            initSearch(object);
        }
        mAdapter.appendToList(object.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }
        loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage") > page);
        mPtrFrameLayout.refreshComplete();
        isSuccess = true;
    }

    private void initSearch(JSONObject object) {
        JSONArray array = object.optJSONArray("schoolTypeArr");
        String key = "0";
        for (int i = 0; i < array.length(); i++) {
            schoolData.put(String.valueOf(i), array.optString(i));
            LogUtil.info(array.optString(i));
        }
        JSONArray jarray = (JSONArray) JsonMetaUtil.getObject("runtype");
        String key1 = "0";
        for (int j = 0; j < jarray.length(); j++) {
            JSONObject o = jarray.optJSONObject(j);
            if (j == 0) {
                dateData.put("0", "不限");
            }
            dateData.put(o.optString("id"), o.optString("name"));
        }
        dateInfo.setValue(dateData, key1);
        schoolInfo.setValue(schoolData, key);
    }


    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ll_search) {
            keyword = et.getText().toString();
            mAdapter.clear();
            startRefresh();
        } else if (v.getId() == R.id.ib_clear) {
            et.setText("");
        }
    }
}
