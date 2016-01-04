package cn.goodjobs.common.activity.personalcenter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.AddOpenSetAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyListView;

public class AddOpenSettingActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    EditText editText;
    Button btnSearch, btnSave;
    LinearLayout hideLayout;
    TextView tvTips;
    ScrollView resultScroll;
    MyListView myListView;
    AddOpenSetAdapter addOpenSetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_open_setting;
    }

    @Override
    protected void initWeightClick() {
        btnSearch.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        myListView.setOnItemClickListener(this);
    }

    @Override
    protected void initWeight() {
        setTopTitle("对部分企业屏蔽");
        editText = (EditText) findViewById(R.id.editText);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSave = (Button) findViewById(R.id.btnSave);
        hideLayout = (LinearLayout) findViewById(R.id.hideLayout);
        tvTips = (TextView) findViewById(R.id.tvTips);
        resultScroll = (ScrollView) findViewById(R.id.resultScroll);
        myListView = (MyListView) findViewById(R.id.myListView);

        addOpenSetAdapter = new AddOpenSetAdapter(this);
        myListView.setAdapter(addOpenSetAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSearch) {
            if (StringUtil.isEmpty(editText.getText().toString().trim())) {
                TipsUtil.show(this, "请输入公司名称");
            } else {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("keyword", editText.getText().toString().trim());
                LoadingDialog.showDialog(this);
                HttpUtil.post(URLS.API_USER_OPENSEARCH, params, this);
            }
        } else if (v.getId() == R.id.btnSave) {
            if (StringUtil.isEmpty(addOpenSetAdapter.getCheckIds())) {
                TipsUtil.show(this, "请至少选择一个公司添加");
            } else {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("corpID", addOpenSetAdapter.getCheckIds());
                LoadingDialog.showDialog(this);
                HttpUtil.post(URLS.API_USER_OPENADD, params, this);
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_USER_OPENSEARCH)) {
            hideLayout.setVisibility(View.VISIBLE);
            tvTips.setText("搜索结果如下，请选择符合条件的公司进行添加，超过最大限额（20）的记录将被忽略");
            resultScroll.setVisibility(View.VISIBLE);
            addOpenSetAdapter.appendToList((JSONArray) data);
        } else if (tag.equals(URLS.API_USER_OPENADD)) {
            TipsUtil.show(this, data.toString());
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        hideLayout.setVisibility(View.VISIBLE);
        tvTips.setText(errorMessage);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JSONObject jsonObject = addOpenSetAdapter.getItem(position);
        try {
            if (jsonObject.optBoolean("check")) {
                jsonObject.put("check", false);
            } else {
                jsonObject.put("check", true);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        addOpenSetAdapter.notifyDataSetChanged();
    }
}
