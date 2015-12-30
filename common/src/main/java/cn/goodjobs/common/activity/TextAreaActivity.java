package cn.goodjobs.common.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;

public class TextAreaActivity extends BaseActivity {

    EditText editText;
    Button btnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_text_area;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle(getIntent().getStringExtra("title"));
        editText = (EditText) findViewById(R.id.editText);
        btnSure = (Button) findViewById(R.id.btnSure);
        editText.setText(getIntent().getStringExtra("content"));
        editText.setHint("请填写" + getIntent().getStringExtra("title"));
        btnSure.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = new Intent();
        intent.putExtra("content", editText.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
