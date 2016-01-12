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

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueJobSearchResultActivity;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.ViewHolderUtil;
import cn.goodjobs.common.view.BabushkaText;
import cn.goodjobs.common.view.ExtendedTouchView;


/**
 * Created by yexiangyu on 15/12/28.
 */
public class BlueJobSearchResultAdapter extends JsonArrayAdapterBase<JSONObject>
{
    private BlueJobSearchResultActivity jobSearchResultActivity;
    private Context context;

    public BlueJobSearchResultAdapter(Context context)
    {
        super(context);
        this.context = context;
    }

    public void setJobSearchResultActivity(BlueJobSearchResultActivity blueJobSearchResultActivity)
    {
        this.jobSearchResultActivity = jobSearchResultActivity;
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
        TextView item_title = ViewHolderUtil.get(convertView, R.id.item_title);
        TextView item_salary = ViewHolderUtil.get(convertView, R.id.item_salary);
        TextView item_company_name = ViewHolderUtil.get(convertView, R.id.item_company_name);
        TextView item_dis = ViewHolderUtil.get(convertView, R.id.item_dis);
        LinearLayout item_treatment_box = ViewHolderUtil.get(convertView, R.id.item_treatment_box);
        ImageView item_certify = ViewHolderUtil.get(convertView, R.id.item_certify);
        ImageView item_vip = ViewHolderUtil.get(convertView, R.id.item_vip);
        TextView item_time = ViewHolderUtil.get(convertView, R.id.item_time);

        JSONObject data = getItem(position);

        item_title.setText(data.optString("jobName"));
        item_salary.setText(data.optString("salaryName"));
        item_company_name.setText(data.optString("corpName"));
        item_time.setText(data.optString("pubDate"));

        String distance = data.optString("distance");

        if (StringUtil.isEmpty(distance)) {

        } else {
            Drawable iconDis = context.getResources().getDrawable(R.mipmap.icon_bluedis);
            iconDis.setBounds(0, 0, DensityUtil.dip2px(context, 20), DensityUtil.dip2px(context, 20));
            item_dis.setCompoundDrawables(iconDis, null, null, null);
            item_dis.setText(" " + distance);
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
            int i1 = DensityUtil.dip2px(context, 2);
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
                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_small));
                item_treatment_box.addView(item, p);
            }
        }


        itemCheck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                itemC.setChecked(!itemC.isChecked());
                if (itemC.isChecked()) {
//                    selectJobIds.add((Integer) data.optInt("id"));
                } else {
//                    selectJobIds.remove((Integer) data.optInt("id"));
                }
            }
        });


        return convertView;
    }


}
