package cn.goodjobs.bluecollar.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.DatePickerUtil;
import cn.goodjobs.common.view.searchItem.InputItemView;
import cn.goodjobs.common.view.searchItem.SearchItemView;
import cn.goodjobs.common.view.searchItem.SelectorItemView;

/**
 * Created by zhuli on 2016/1/7.
 */
public class ItemResumeActivity extends BaseActivity {

    private LinearLayout llBottom;
    private SelectorItemView itemWant, itemAddress;
    private SearchItemView itemBirthday;
    private InputItemView itemName;
    private ImageButton btnRight;
    private Button btnSave;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_resume;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我的简历");
        btnRight = (ImageButton) findViewById(R.id.btn_right);
        btnRight.setImageResource(R.drawable.icon_view);
        btnRight.setVisibility(View.VISIBLE);

        btnSave = (Button) findViewById(R.id.btn_save);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        itemWant = (SelectorItemView) findViewById(R.id.itemWant);
        itemAddress = (SelectorItemView) findViewById(R.id.itemAddress);
        itemBirthday = (SearchItemView) findViewById(R.id.itemBirthday);
        itemName = (InputItemView) findViewById(R.id.itemName);

        new DatePickerUtil(this, itemBirthday, "yyyy-MM-dd", null);

        llBottom.setVisibility(View.GONE);

        btnRight.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    public void doSave(View v) {
        Intent intent = new Intent(this, ResumeMoreActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_right) {

        }
    }
}
