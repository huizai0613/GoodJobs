package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseActivity;
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

        btnSave = (Button) findViewById(R.id.btn_save);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);
        itemWant = (SelectorItemView) findViewById(R.id.itemWant);
        itemAddress = (SelectorItemView) findViewById(R.id.itemAddress);
        itemBirthday = (SearchItemView) findViewById(R.id.itemBirthday);
        itemName = (InputItemView) findViewById(R.id.itemName);

        new DatePickerUtil(this, itemBirthday, "yyyy-MM-dd", null);

        llBottom.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }

    public void doSave(View v) {
        Intent intent = new Intent(this, ResumeMoreActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
    }
}
