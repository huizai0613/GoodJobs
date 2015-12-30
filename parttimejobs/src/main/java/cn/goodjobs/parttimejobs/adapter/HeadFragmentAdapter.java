package cn.goodjobs.parttimejobs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.parttimejobs.R;
import cn.goodjobs.headhuntingjob.activity.HeadDetailsActivity;

/**
 * Created by zhuli on 2015/12/24.
 */
public class HeadFragmentAdapter extends JsonArrayAdapterBase<JSONObject>
{

    private int type;


    public HeadFragmentAdapter(Context context, int type)
    {
        super(context);
        this.type = type;
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (type == 0 || type == 2) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_headhunting, null);
                holder.jobName = (TextView) convertView.findViewById(R.id.tv_jobName);
                holder.salary = (TextView) convertView.findViewById(R.id.tv_salary);
                holder.cityName = (TextView) convertView.findViewById(R.id.tv_place);
                holder.pubDate = (TextView) convertView.findViewById(R.id.tv_date);

            } else if (type == 1) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_reward, null);
                holder.jobName = (TextView) convertView.findViewById(R.id.tv_jobName);
                holder.salary = (TextView) convertView.findViewById(R.id.tv_salary);
                holder.cityName = (TextView) convertView.findViewById(R.id.tv_cityName);
                holder.jobBonus = (TextView) convertView.findViewById(R.id.tv_jobBonus);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        if (type == 0 || type == 2) {
            holder.jobName.setText(jsonObject.optString("jobName"));
            holder.salary.setText(jsonObject.optString("salary"));
            holder.cityName.setText(jsonObject.optString("cityName"));
            holder.pubDate.setText(jsonObject.optString("pubDate"));
            if (type == 0) {
                convertView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(context, HeadDetailsActivity.class);
                        context.startActivity(intent);
                    }
                });
            }
        } else if (type == 1) {
            holder.jobName.setText(jsonObject.optString("jobName"));
            holder.salary.setText(jsonObject.optString("salary"));
            holder.cityName.setText(jsonObject.optString("cityName"));
            holder.jobBonus.setText(jsonObject.optString("jobBonus"));
        }

        return convertView;
    }

    public class ViewHolder
    {
        private TextView jobName, salary, cityName, pubDate, jobBonus;
    }
}

