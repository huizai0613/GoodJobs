package cn.goodjobs.applyjobs.activity;

/**
 * Created by zhuli on 2015/12/21.
 */


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.adapter.HeaderDetailsAdapter;
import cn.goodjobs.common.baseclass.BaseListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;


public class HeaderDetailsActivity extends BaseListActivity {

    private EmptyLayout emptyLayout;
    private String type;
    private EditText et;
    private ImageButton btnClear;
    private LinearLayout search, llTop;
    private int typeID;
    private int catalogID;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        if (typeID == 0) {
            mAdapter = new HeaderDetailsAdapter(this, 0);
        } else if (typeID == 1) {
            mAdapter = new HeaderDetailsAdapter(this, 1);
        }
        initList();
        startRefresh();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_headerdetails;
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        if (typeID == 0) {
            params.put("type", "jobfair");
            params.put("catalogID", catalogID);
            params.put("page", page);

        } else if (typeID == 1) {
            params.put("type", "cvjob");
            params.put("catalogID", catalogID);
            if (!StringUtil.isEmpty(keyword))
                params.put("keyword", keyword);
            params.put("page", page);
        }
        HttpUtil.post(URLS.API_JOB_FairList, params, this);
    }


    @Override
    protected void initData() {
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        typeID = intent.getIntExtra("typeID", -1);
        catalogID = intent.getIntExtra("catalogID", 0);
        setTopTitle(type);
        emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
        llTop = (LinearLayout) findViewById(R.id.ll_top);
        search = (LinearLayout) findViewById(R.id.ll_search);
        et = (EditText) findViewById(R.id.et_career);
        btnClear = (ImageButton) findViewById(R.id.ib_clear);
        if (typeID == 0) {
            llTop.setVisibility(View.GONE);
        } else {
            llTop.setVisibility(View.VISIBLE);
        }
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
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = et.getText().toString();
                mAdapter.clear();
                startRefresh();
            }
        });
    }


    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        JSONObject object = (JSONObject) data;
        try {
            mAdapter.appendToList(object.getJSONArray("list"));
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NODATA);
            }

            loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage") > page);
            mPtrFrameLayout.refreshComplete();
        } catch (JSONException e) {
            e.printStackTrace();
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

