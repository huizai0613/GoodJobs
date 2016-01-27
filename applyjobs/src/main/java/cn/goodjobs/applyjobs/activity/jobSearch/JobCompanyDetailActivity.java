package cn.goodjobs.applyjobs.activity.jobSearch;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.common.activity.ImagePreviewActivity;
import cn.goodjobs.common.activity.LsMapActivity;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.PhoneUtils;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by yexiangyu on 15/12/28.
 */
public class JobCompanyDetailActivity extends BaseActivity
{

    private View jobSimilarBox;
    private View companyImg;
    private LinearLayout mCompanyImgBox;
    private TextView comName;
    private TextView comNature;
    private TextView comNum;
    private TextView comIndustry;
    private TextView comAdd;
    private TextView comMap;
    private View comPhoneBox;
    private TextView comPhone;
    private TextView comContent;
    private ImageView comUpdown;
    private int corpID;
    private EmptyLayout error_layout;
    private JSONObject corpData;
    private String loc;
    private View com_nature_box;
    private View com_num_box;
    private View com_industry_box;
    private View com_add_box;
    private View com_phone_box;
    private View comContentBox;

    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_jobcompany_detail;
    }

    @Override
    protected void initWeightClick()
    {

        comUpdown.setOnClickListener(this);
        comPhone.setOnClickListener(this);
        comMap.setOnClickListener(this);
        jobSimilarBox.setOnClickListener(this);

    }

    @Override
    protected void initWeight()
    {
        setTopTitle("公司介绍");
        comName = (TextView) findViewById(R.id.com_name);
        error_layout = (EmptyLayout) findViewById(R.id.error_layout);
        comNature = (TextView) findViewById(R.id.com_nature);
        comNum = (TextView) findViewById(R.id.com_num);
        comIndustry = (TextView) findViewById(R.id.com_industry);
        comAdd = (TextView) findViewById(R.id.com_add);
        comMap = (TextView) findViewById(R.id.com_map);
        comPhone = (TextView) findViewById(R.id.com_phone);
        comPhoneBox = (View) findViewById(R.id.com_phone_box);
        comContentBox = (View) findViewById(R.id.com_content_box);
        comContent = (TextView) findViewById(R.id.com_content);
        comUpdown = (ImageView) findViewById(R.id.com_updown);
        jobSimilarBox = findViewById(R.id.job_similar_box);
        mCompanyImgBox = (LinearLayout) findViewById(R.id.company_img_box);
        companyImg = findViewById(R.id.company_img);


        com_nature_box = findViewById(R.id.com_nature_box);
        com_num_box = findViewById(R.id.com_num_box);
        com_industry_box = findViewById(R.id.com_industry_box);
        com_add_box = findViewById(R.id.com_add_box);
        com_phone_box = findViewById(R.id.com_phone_box);


        Drawable iconPhone = getResources().getDrawable(R.drawable.phone);
        iconPhone.setBounds(0, 0, DensityUtil.dip2px(mcontext, 15), DensityUtil.dip2px(mcontext, 15));
        comPhone.setCompoundDrawables(null, null, iconPhone, null);

        Drawable iconMap = getResources().getDrawable(R.drawable.mapm);
        iconMap.setBounds(0, 0, DensityUtil.dip2px(mcontext, 15), DensityUtil.dip2px(mcontext, 15));
        comMap.setCompoundDrawables(iconMap, null, null, null);

        error_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
        error_layout.setOnLayoutClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HashMap<String, Object> param = new HashMap<>();
                param.put("corpID", corpID);
                HttpUtil.post(URLS.API_JOB_Corpshow, param, JobCompanyDetailActivity.this);
            }
        });
        HashMap<String, Object> param = new HashMap<>();
        param.put("corpID", corpID);
        HttpUtil.post(URLS.API_JOB_Corpshow, param, this);
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        int curCorpID = intent.getIntExtra("corpID", 0);
        if (this.corpID != curCorpID) {
            error_layout.setErrorType(EmptyLayout.NETWORK_LOADING);
            HashMap<String, Object> param = new HashMap<>();
            this.corpID = curCorpID;
            param.put("corpID", curCorpID);
            HttpUtil.post(URLS.API_JOB_Corpshow, param, this);
        }
    }

    @Override
    protected void initData()
    {
        corpID = getIntent().getIntExtra("corpID", 0);
    }


    private void setStrng2Bab(TextView tv, View box, String content)
    {

        if (StringUtil.isEmpty(content)) {
            box.setVisibility(View.GONE);
            return;
        }

        tv.setText(content);
    }

    private void setData()
    {
        comName.setText(corpData.optString("corpName"));
        setStrng2Bab(comNature, com_nature_box, corpData.optString("corpkind"));
        setStrng2Bab(comNum, com_num_box, corpData.optString("corpsize"));
        setStrng2Bab(comIndustry, com_industry_box, corpData.optString("industry"));
        setStrng2Bab(comAdd, com_add_box, corpData.optString("address"));


        String phone = corpData.optString("phone");

        if (StringUtil.isEmpty(phone) || !"0".equals(corpData.optString("hidePhone"))) {
            comPhoneBox.setVisibility(View.GONE);
        } else {
            comPhone.setText(phone + " ");
        }

        String intro = corpData.optString("intro");

        if (StringUtil.isEmpty(intro)) {
            comContentBox.setVisibility(View.GONE);
        } else {
            comContent.setText(intro);
        }


        loc = corpData.optString("loc");
        if (StringUtil.isEmpty(loc)) {
            comMap.setVisibility(View.GONE);
        } else {
            comMap.setVisibility(View.VISIBLE);
        }

        JSONArray corpPic = corpData.optJSONArray("corpPic");

        ArrayList<String> strings = new ArrayList<>();
        if (corpPic != null && corpPic.length() > 0) {
            for (int i = 0; i < corpPic.length(); i++) {
                JSONObject jsonObject = corpPic.optJSONObject(i);
                if (jsonObject != null && !StringUtil.isEmpty(jsonObject.optString("url"))) {
                    strings.add(jsonObject.optString("url"));
                }
            }
        }
        setPhotos(strings);
    }

    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        error_layout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        corpData = (JSONObject) data;
        setData();
    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);
        error_layout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        int i = v.getId();
        if (i == R.id.com_updown) {
            if (Math.floor(comContent.getHeight() / comContent.getLineHeight()) <= 5) {
                comContent.setMaxLines(100);
                comUpdown.setImageResource(R.drawable.companyarticle_up);
            } else {
                comUpdown.setImageResource(R.drawable.companyarticle_down);
                comContent.setMaxLines(5);
            }
        } else if (i == R.id.com_phone) {
            PhoneUtils.makeCall(comPhone.getText().toString(), mcontext);
        } else if (i == R.id.com_map) {
            LsMapActivity.openMap(mcontext, Double.parseDouble(loc.split(",")[0]), Double.parseDouble(loc.split(",")[1]),
                    corpData.optString("corpName"), corpData.optString("address"));
        } else if (i == R.id.job_similar_box) {//跳转相似职位列表

            HashMap<String, Object> param = new HashMap<>();
            param.put("corpID", corpID);
            JumpViewUtil.openActivityAndParam(mcontext, JobSimilarActivity.class, param);
        }

    }


    public void setPhotos(final ArrayList<String> photos)
    {
        int dip2pxBig = DensityUtil.dip2px(mcontext, 15);
        int dip2pxSmall = DensityUtil.dip2px(mcontext, 5);
        int itemW = 0;
        if (photos.size() <= 0) {
            companyImg.setVisibility(View.GONE);
//            itemW = (DensityUtil.getScreenW(mcontext) - 2 * dip2pxBig - 3 * dip2pxSmall) / 5;
//            itemW += (itemW / 5);
//            mCompanyImgBox.removeAllViews();
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemW, itemW);
//            SimpleDraweeView item = (SimpleDraweeView) LayoutInflater.from(mcontext).inflate(cn.goodjobs.common.R.layout.simpledraweeview, null);
//            params.leftMargin = dip2pxBig;
//            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            mCompanyImgBox.addView(item, params);
        } else {
            companyImg.setVisibility(View.VISIBLE);


            if (photos.size() > 4) {
                itemW = (companyImg.getWidth() - 4 * dip2pxSmall - 2 * companyImg.getPaddingLeft()) / 5;
                itemW += (itemW / 5);
            } else {
                itemW = (companyImg.getWidth() - 3 * dip2pxSmall - 2 * companyImg.getPaddingLeft()) / 4;
            }
            mCompanyImgBox.removeAllViews();
            for (int i = 0; i < (photos.size() > 5 ? 5 : photos.size()); i++) {
                final int j = i;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemW, itemW);
                SimpleDraweeView item = (SimpleDraweeView) LayoutInflater.from(mcontext).inflate(R.layout.simpledraweeview, null);
                if (i == 0) {
                    params.leftMargin = 0;
                } else if (i == (photos.size() > 5 ? 5 : photos.size()) - 1) {
                    params.leftMargin = dip2pxSmall;
                    params.rightMargin = 0;
                } else {
                    params.leftMargin = dip2pxSmall;
                }
                item.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Uri uri = Uri.parse(photos.get(i));
                if (photos.get(i).endsWith(".gif") || photos.get(i).endsWith(".GIF")) {
                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setAutoPlayAnimations(true)
                            .setUri(uri)//设置uri
                            .build();
                    item.setController(draweeController);
                } else {
                    item.setImageURI(uri);
                }
                item.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ImagePreviewActivity.showImagePrivew(mcontext, j, false, photos);
                    }
                });
                mCompanyImgBox.addView(item, params);
            }
        }
    }
}
