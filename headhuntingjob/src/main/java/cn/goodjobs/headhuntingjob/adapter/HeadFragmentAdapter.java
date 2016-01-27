package cn.goodjobs.headhuntingjob.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.headhuntingjob.R;
import cn.goodjobs.headhuntingjob.activity.HeadDetailsActivity;

/**
 * Created by zhuli on 2015/12/24.
 */
public class HeadFragmentAdapter extends JsonArrayAdapterBase<JSONObject> {

    private int type;
    private List<Integer> data = new ArrayList<Integer>();


    public HeadFragmentAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            if (type == 0 || type == 2) {
                convertView =   LayoutInflater.from(context).inflate(R.layout.item_headhunting, null);
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
        if (type == 0) {
            holder.jobName.setText(jsonObject.optString("jobName"));
            holder.salary.setText(jsonObject.optString("salary"));
            holder.cityName.setText(jsonObject.optString("cityName"));
            holder.pubDate.setText(jsonObject.optString("pubDate"));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.clear();
                    for (int i = 0; i < mList.size(); i++) {
                        data.add(mList.get(i).optInt("jobID"));
                    }
                    Intent intent = new Intent(context, HeadDetailsActivity.class);
                    intent.putExtra("item", position);
                    intent.putExtra("type", type);
                    intent.putIntegerArrayListExtra("idList", (ArrayList<Integer>) data);
                    context.startActivity(intent);
                }
            });
        } else if (type == 1) {
            holder.jobName.setText(jsonObject.optString("jobName"));
            holder.salary.setText(jsonObject.optString("salary"));
            holder.cityName.setText(jsonObject.optString("cityName"));
            holder.jobBonus.setText(jsonObject.optString("jobBonus") + "元");
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    data.clear();
                    for (int i = 0; i < mList.size(); i++) {
                        data.add(mList.get(i).optInt("jobID"));
                    }
                    Intent intent = new Intent(context, HeadDetailsActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("item", position);
                    intent.putIntegerArrayListExtra("idList", (ArrayList<Integer>) data);
                    context.startActivity(intent);
                }
            });
        } else if (type == 2) {
            holder.jobName.setText(jsonObject.optString("jobName"));
            holder.salary.setText(jsonObject.optString("salary"));
            holder.cityName.setText(jsonObject.optString("cityName"));
            holder.pubDate.setText(jsonObject.optString("pubDate"));
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object> param = new HashMap<>();
                    param.put("POSITION", position);
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < mList.size(); i++) {
                        builder.append(mList.get(i).optInt("jobID") + ",");
                    }
                    String charSequence = builder.subSequence(0, builder.length() - 1).toString();
                    param.put("IDS", charSequence);
                    Intent intent = new Intent();
                    Set<Map.Entry<String, Object>> entries = param.entrySet();
                    for (Map.Entry<String, Object> entry : entries) {
                        if (entry.getValue() instanceof Integer) {
                            intent.putExtra(entry.getKey(), (int) entry.getValue());
                        } else if (entry.getValue() instanceof Float) {
                            intent.putExtra(entry.getKey(), (float) entry.getValue());
                        } else if (entry.getValue() instanceof Double) {
                            intent.putExtra(entry.getKey(), (double) entry.getValue());
                        } else if (entry.getValue() instanceof String) {
                            intent.putExtra(entry.getKey(), (String) entry.getValue());
                        } else if (entry.getValue() instanceof Serializable) {
                            intent.putExtra(entry.getKey(), (Serializable) entry.getValue());
                        } else if (entry.getValue() instanceof Long) {
                            intent.putExtra(entry.getKey(), (long) entry.getValue());
                        }
                    }
                    intent.setClassName(context, "cn.goodjobs.applyjobs.activity.jobSearch.JobDetailActivity");
                    context.startActivity(intent);
                }
            });
        }

        return convertView;
    }

    public class ViewHolder {
        private TextView jobName, salary, cityName, pubDate, jobBonus;
    }
}

