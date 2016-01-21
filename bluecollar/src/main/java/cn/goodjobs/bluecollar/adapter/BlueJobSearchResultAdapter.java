package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueJobDetailActivity;
import cn.goodjobs.bluecollar.activity.BlueJobSearchResultActivity;
import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.GeoUtils;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.UpdateDataTaskUtils;
import cn.goodjobs.common.util.ViewHolderUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.view.BabushkaText;
import cn.goodjobs.common.view.ExtendedTouchView;


/**
 * Created by yexiangyu on 15/12/28.
 */
public class BlueJobSearchResultAdapter extends JsonArrayAdapterBase<JSONObject>
{

    public static final int CURDIS = 0;
    public static final int REGIONDIS = 1;

    private BlueJobSearchResultActivity jobSearchResultActivity;
    private Context context;
    private List<Integer> jobRead;
    private final MyLocation myLocation;
    private int curType = 1;
    private final Drawable iconDis;


    public void setCurType(int curType)
    {
        this.curType = curType;
    }

    public BlueJobSearchResultAdapter(Context context, BlueJobSearchResultActivity jobSearchResultActivity)
    {
        super(context);
        this.context = context;
        iconDis = context.getResources().getDrawable(R.mipmap.icon_bluedis);
        iconDis.setBounds(0, 0, DensityUtil.dip2px(context, 18), DensityUtil.dip2px(context, 18));
        this.jobSearchResultActivity = jobSearchResultActivity;

        UpdateDataTaskUtils.getReadJob(context, new UpdateDataTaskUtils.OnGetDiscussReadJobListener()
        {
            @Override
            public void onGetDiscussReadJob(List<Integer> jobRead)
            {
                BlueJobSearchResultAdapter.this.jobRead = jobRead;
                BlueJobSearchResultAdapter.this.jobSearchResultActivity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        myLocation = GoodJobsApp.getInstance().getMyLocation();
    }

    public void setJobSearchResultActivity(BlueJobSearchResultActivity blueJobSearchResultActivity)
    {
        this.jobSearchResultActivity = blueJobSearchResultActivity;
    }

    ArrayList<Integer> checkPosition = new ArrayList<>();


    public ArrayList<Integer> getCheckPosition()
    {
        return checkPosition;
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bluejob, null);
        }
//        TextView title = ViewHolderUtil.get(convertView, R.id.item_title);
//        TextView address = ViewHolderUtil.get(convertView, R.id.item_address);
//        TextView name = ViewHolderUtil.get(convertView, R.id.item_name);
//        BabushkaText salary = ViewHolderUtil.get(convertView, R.id.item_salary);
//        TextView time = ViewHolderUtil.get(convertView, R.id.item_time);
//        final CheckBox check = ViewHolderUtil.get(convertView, R.id.item_check);

        ExtendedTouchView itemCheck = ViewHolderUtil.get(convertView, R.id.item_check);
        final CheckBox itemC = ViewHolderUtil.get(convertView, R.id.item_c);
        final TextView item_title = ViewHolderUtil.get(convertView, R.id.item_title);
        TextView item_salary = ViewHolderUtil.get(convertView, R.id.item_salary);
        TextView item_company_name = ViewHolderUtil.get(convertView, R.id.item_company_name);
        TextView item_dis = ViewHolderUtil.get(convertView, R.id.item_dis);
        LinearLayout item_treatment_box = ViewHolderUtil.get(convertView, R.id.item_treatment_box);
        ImageView item_certify = ViewHolderUtil.get(convertView, R.id.item_certify);
        ImageView item_vip = ViewHolderUtil.get(convertView, R.id.item_vip);
        TextView item_time = ViewHolderUtil.get(convertView, R.id.item_time);

        final JSONObject data = getItem(position);
        final int jobID = data.optInt("jobID");
        item_title.setText(data.optString("jobName"));
        if (jobRead != null && jobRead.contains((Integer) jobID)) {
            item_title.setTextColor(context.getResources().getColor(R.color.light_color));
        } else {
            item_title.setTextColor(context.getResources().getColor(R.color.main_color));
        }


        item_salary.setText(data.optString("salaryName"));
        item_company_name.setText(data.optString("corpName"));
        item_time.setText(data.optString("pubDate"));


        switch (curType) {
            case CURDIS:
                item_dis.setCompoundDrawables(iconDis, null, null, null);
                double distance = GeoUtils.
                        distance(GoodJobsApp.getInstance().getMyLocation().latitude, GoodJobsApp.getInstance().getMyLocation().longitude, Double.parseDouble(data.optString("mapLat")), Double.parseDouble(data.optString("mapLng")));
                if (distance > 1000) {
                    item_dis.setText(distance / 1000 + "千米");
                } else {
                    item_dis.setText(distance + "米");
                }
                break;
            case REGIONDIS:
                item_dis.setCompoundDrawables(null, null, null, null);
                item_dis.setText(data.optString("cityName"));
                break;
        }


        String certStatus = data.optString("certStatus");
        String blueFlag = data.optString("blueFlag");


        if ("1".equals(certStatus)) {
            item_certify.setImageResource(R.mipmap.icon_certify);
        } else {
            item_certify.setImageResource(R.mipmap.icon_uncertify);
        }

        if ("0".equals(certStatus)) {
            item_vip.setVisibility(View.VISIBLE);
        } else {
            item_vip.setVisibility(View.GONE);
        }


        JSONArray treatment = data.optJSONArray("treatment");
        item_treatment_box.removeAllViews();
        if (treatment != null) {
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int i1 = DensityUtil.dip2px(context, 1);
            for (int i = 0; i < (treatment.length() > 3 ? 3 : treatment.length()); i++) {

                TextView item = new TextView(context);
                item.setPadding(i1, i1, i1, i1);
                item.setBackgroundResource(R.drawable.bg_welfare);
                item.setGravity(Gravity.CENTER);
                if (i == 2) {
                    item.setText("·  ·  ·");
                } else {
                    item.setText(treatment.optString(i));
                }

                if (i == 1) {
                    p.rightMargin = p.leftMargin = DensityUtil.dip2px(context, 2);
                }

                item.setTextColor(Color.parseColor("#6bbd00"));
                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_litter));
                item_treatment_box.addView(item, p);
            }
        }
        itemC.setChecked(checkPosition.contains((Integer) position));

        itemCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemC.setChecked(!itemC.isChecked());
                if (itemC.isChecked()) {
                    checkPosition.add((Integer) position);
                } else {
                    checkPosition.remove((Integer) position);
                }
                jobSearchResultActivity.setBottomVisible(checkPosition.size() > 0);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                HashMap<String, Object> param = new HashMap<>();
                param.put("POSITION", position);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mList.size(); i++) {
                    builder.append(mList.get(i).optInt("blueJobID") + ",");
                }
                String charSequence = builder.subSequence(0, builder.length() - 1).toString();
                param.put("IDS", charSequence);
                if (!jobRead.contains((Integer) jobID))
                    UpdateDataTaskUtils.saveJobRead(context, jobID);
                item_title.setTextColor(context.getResources().getColor(R.color.light_color));
                jobRead.add(jobID);
                JumpViewUtil.openActivityAndParam(context, BlueJobDetailActivity.class, param);
            }
        });


        return convertView;
    }


}
