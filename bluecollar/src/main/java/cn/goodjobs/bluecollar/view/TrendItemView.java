package cn.goodjobs.bluecollar.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.activity.ImagePreviewActivity;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.view.searchItem.CombinedBaseView;

/**
 * Created by 王刚 on 2016/1/7 0007.
 * 动态
 */
public class TrendItemView extends CombinedBaseView implements View.OnClickListener{

    RelativeLayout imgLayout;
    LinearLayout contentLayout;
    SimpleDraweeView myImageview;
    TextView tvMsg, tvFans, tvGuanzhu, tvTime, tvContent, tvPics;
    ArrayList<String> picList;

    public TrendItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutResource() {
        return R.layout.view_item_trend;
    }

    @Override
    protected void onCreate(Context context) {
        super.onCreate(context);
        myImageview = (SimpleDraweeView) findViewById(R.id.myImageview);
        imgLayout = (RelativeLayout) findViewById(R.id.imgLayout);
        contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvFans = (TextView) findViewById(R.id.tvFans);
        tvGuanzhu = (TextView) findViewById(R.id.tvGuanzhu);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvContent = (TextView) findViewById(R.id.tvContent);
        tvPics = (TextView) findViewById(R.id.tvPics);

        myImageview.setOnClickListener(this);
    }

    public void showView(JSONObject jsonObject) {
        JSONArray images = jsonObject.optJSONArray("picList");
        if (images != null && images.length() > 0) {
            imgLayout.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(images.optString(0));
            myImageview.setImageURI(uri);

            picList = new ArrayList<String>();
            int size = images.length();
            if (size > 1) {
                tvPics.setVisibility(View.VISIBLE);
            } else {
                tvPics.setVisibility(View.INVISIBLE);
            }
            for (int i=0;i<size;++i) {
                picList.add(images.optString(i));
            }
        } else {
            imgLayout.setVisibility(View.GONE);
        }
        if (StringUtil.isEmpty(jsonObject.optString("content"))) {
            contentLayout.setVisibility(View.GONE);
        } else {
            contentLayout.setVisibility(View.VISIBLE);
            tvContent.setText(jsonObject.optString("content"));
        }
        tvTime.setText(jsonObject.optString("saveDate"));
        tvGuanzhu.setText(jsonObject.optString("lookNum"));
        tvFans.setText(jsonObject.optString("likeNum"));
        tvMsg.setText(jsonObject.optString("replyNum"));
    }

    @Override
    public void onClick(View v) {
        ImagePreviewActivity.showImagePrivew(getContext(), 0, false, picList);
    }
}
