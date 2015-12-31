package cn.goodjobs.campusjobs.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.StringUtil;

/**
 * Created by zhuli on 2015/12/30.
 */
public class CampusSearchActivity extends BaseActivity {

    private ImageButton btnClear;
    private EditText et;

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
    protected void initWeight() {
        btnClear = (ImageButton) findViewById(R.id.ib_clear);
        et = (EditText) findViewById(R.id.et_campussearch);
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
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.ib_clear) {
            et.setText("");
        }
    }
}
