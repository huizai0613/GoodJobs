package cn.goodjobs.campusjobs.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.UpdateDataTaskUtils;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

/**
 * Created by zhuli on 2015/12/30.
 */
public class CampusSearchActivity extends BaseActivity {

    private Map<Long, Map<String, String>> history;
    private SelectorItemView itemAddress;
    private SelectorItemView itemJobfunc;
    private SelectorItemView itemIndtype;
    private ImageButton btnClear;
    private EditText etSearch;
    private String add;
    private String job;
    private String ind;
    private String searchKeyWorld;
    private Button btnSearch;
    private TextView tvClear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_campussearch;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateDataTaskUtils.getHistory(this, UpdateDataTaskUtils.CAMPUSJOB, new UpdateDataTaskUtils.OnGetDiscussHistoryListener() {
            @Override
            public void onGetDiscussHistory(Map<Long, Map<String, String>> history) {
                CampusSearchActivity.this.history = history;

                List<Map.Entry<Long, Map<String, String>>> infoIds =
                        new ArrayList<Map.Entry<Long, Map<String, String>>>(history.entrySet());
                Collections.sort(infoIds, new Comparator<Map.Entry<Long, Map<String, String>>>() {
                    @Override
                    public int compare(Map.Entry<Long, Map<String, String>> lhs, Map.Entry<Long, Map<String, String>> rhs) {
                        return (rhs.getKey()).compareTo(lhs.getKey());
                    }
                });
                Message message = new Message();
                message.obj = infoIds;
                handler.sendMessage(message);
            }
        });
    }

    @Override
    protected void initWeight() {
        setTopTitle("职位搜索");
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        tvClear.setOnClickListener(this);
        searchHeistory = (LinearLayout) findViewById(R.id.search_heistory);
        itemAddress = (SelectorItemView) findViewById(R.id.item_address);
        itemJobfunc = (SelectorItemView) findViewById(R.id.item_jobfunc);
        itemIndtype = (SelectorItemView) findViewById(R.id.item_indtype);
        btnClear = (ImageButton) findViewById(R.id.ib_clear);
        etSearch = (EditText) findViewById(R.id.et_campussearch);
        etSearch.addTextChangedListener(new TextWatcher() {
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
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.ib_clear) {
            etSearch.setText("");
        } else if (i == R.id.btnSearch) {
            if (history == null) {
                history = new HashMap<>();
            }
            add = itemAddress.getText();
            if (StringUtil.isEmpty(add)) {
                TipsUtil.show(this, "请选择工作地点");
                return;
            }
            LinkedHashMap searchHashMap = getSearchHashMap();
            saveSearchLock(history, searchHashMap);
            JumpViewUtil.openActivityAndParam(this, CampusSearchResultActivity.class, searchHashMap);
        } else if (i == R.id.tv_clear) {
            tvClear.setVisibility(View.GONE);
            history.clear();
            UpdateDataTaskUtils.cleanHistory(this, UpdateDataTaskUtils.CAMPUSJOB);
            disPlayerSearchUI(null);
        }
    }


    public void saveSearchLock(Map<Long, Map<String, String>> saveData, Map<String, String> put) {
        Set<Map.Entry<Long, Map<String, String>>> entries = saveData.entrySet();

        ArrayList<Long> keys = new ArrayList<>();
        for (Map.Entry<Long, Map<String, String>> en : entries) {
            boolean isEqu = true;
            Map<String, String> value = en.getValue();
            Set<Map.Entry<String, String>> entries1 = value.entrySet();
            for (Map.Entry<String, String> e : entries1) {
                if (!e.getValue().equals(put.get(e.getKey())) || entries1.size() != put.size()) {
                    isEqu = false;
                    break;
                }
            }
            if (isEqu) {
                keys.add(en.getKey());
            }
        }
        for (Long key : keys) {
            saveData.remove(key);
        }

        saveData.put(System.currentTimeMillis(), put);
        UpdateDataTaskUtils.updateHistory(mcontext, saveData, UpdateDataTaskUtils.CAMPUSJOB);
    }


    private LinkedHashMap getSearchHashMap() {
        searchKeyWorld = etSearch.getText().toString();
        add = itemAddress.getText();
        job = itemJobfunc.getText();
        ind = itemIndtype.getText();

        LinkedHashMap hashMap = new LinkedHashMap();
        if (!StringUtil.isEmpty(searchKeyWorld)) {
            hashMap.put("searchKeyWorld", searchKeyWorld);
        }
        if (!StringUtil.isEmpty(add)) {
            hashMap.put("itemAddress", add);
        }
        if (!StringUtil.isEmpty(job)) {
            hashMap.put("itemJobfunc", job);
        }
        if (!StringUtil.isEmpty(ind)) {
            hashMap.put("itemIndtype", ind);
        }
        hashMap.putAll(getSearchHashMapID());
        return hashMap;
    }

    private LinkedHashMap getSearchHashMapID() {
        String addId = (String) itemAddress.getTag();
        String jobId = (String) itemJobfunc.getTag();
        String indId = (String) itemIndtype.getTag();

        LinkedHashMap hashMap = new LinkedHashMap();
        if (!StringUtil.isEmpty(addId)) {
            hashMap.put("itemAddressId", addId);
        }
        if (!StringUtil.isEmpty(jobId)) {
            if (jobId.startsWith("-1")) {
                jobId = jobId.split("#")[1];
            }
            hashMap.put("itemJobfuncId", jobId);
        }
        if (!StringUtil.isEmpty(indId)) {
            hashMap.put("itemIndtypeId", indId);
        }

        return hashMap;
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disPlayerSearchUI((List<Map.Entry<Long, Map<String, String>>>) msg.obj);

        }
    };

    private LinearLayout searchHeistory;

    //近期搜索记录展示
    private void disPlayerSearchUI(List<Map.Entry<Long, Map<String, String>>> obj) {
        searchHeistory.removeAllViews();
        if (obj != null) {
            tvClear.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(DensityUtil.dip2px(this, 10), 0, DensityUtil.dip2px(this, 10), 0);
            LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<Long, Map<String, String>> longMapEntry : obj) {
                final Map<String, String> value = longMapEntry.getValue();
                TextView view = new TextView(this);

                builder.delete(0, builder.length());

                builder.append(StringUtil.isEmpty(value.get("searchKeyWorld")) ? "" : value.get("searchKeyWorld") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemAddress")) ? "" : value.get("itemAddress") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemJobfunc")) ? "" : value.get("itemJobfunc") + " + ");
                builder.append(StringUtil.isEmpty(value.get("itemIndtype")) ? "" : value.get("itemIndtype") + " + ");

                if (builder.length() == 0) {
                    continue;
                }
                CharSequence charSequence = builder.subSequence(0, builder.length() - 3);
                view.setPadding(0, 20, 0, 20);
                view.setSingleLine();
                view.setEllipsize(TextUtils.TruncateAt.END);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                view.setText(charSequence);
                view.setBackgroundResource(R.drawable.list_item_bg);
                view.setGravity(Gravity.CENTER_VERTICAL);
                searchHeistory.addView(view, layoutParams);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //填充数据
                        String itemAddressId = value.get("itemAddressId");
                        String itemAddressS = value.get("itemAddress");
                        String searchKeyWorld = value.get("searchKeyWorld");

                        String itemJobfuncId = value.get("itemJobfuncId");
                        String itemIndtypeId = value.get("itemIndtypeId");


                        String itemJobfuncStr = value.get("itemJobfunc");
                        String itemIndtypeStr = value.get("itemIndtype");

                        if (!StringUtil.isEmpty(itemAddressId)) {
                            itemAddress.setSelectorIds(itemAddressId);
                            itemAddress.setText(itemAddressS);
                        } else {
                            itemAddress.setSelectorIds("");
                            itemAddress.setText(itemAddressS);
                        }

                        if (!StringUtil.isEmpty(itemJobfuncStr)) {
                            itemJobfunc.setSelectorIds(itemJobfuncId);
                            itemJobfunc.setText(itemJobfuncStr);
                        } else {
                            itemJobfunc.setSelectorIds("");
                            itemJobfunc.setText("");
                        }

                        if (!StringUtil.isEmpty(itemIndtypeStr)) {
                            itemIndtype.setSelectorIds(itemIndtypeId);
                            itemIndtype.setText(itemIndtypeStr);
                        } else {
                            itemIndtype.setSelectorIds("");
                            itemIndtype.setText("");
                        }


                        if (!StringUtil.isEmpty(searchKeyWorld)) {
                            etSearch.setText(searchKeyWorld);
                        } else {
                            etSearch.setText("");
                        }
                    }
                });
                View line = new View(this);
                line.setBackgroundResource(R.color.line_color);
                searchHeistory.addView(line, lineParams);
            }
        } else {
            tvClear.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(DensityUtil.dip2px(this, 10), 0, DensityUtil.dip2px(this, 10), 0);
            TextView view = new TextView(this);
            view.setPadding(0, 20, 0, 20);
            view.setSingleLine();
            view.setEllipsize(TextUtils.TruncateAt.END);
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            view.setText("暂无搜索记录");
            view.setBackgroundResource(R.drawable.list_item_bg);
            view.setGravity(Gravity.CENTER_VERTICAL);
            searchHeistory.addView(view, layoutParams);
        }

    }

}
