package cn.goodjobs.campusjobs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.ViewHolderUtil;

/**
 * Created by zhuli on 2015/12/30.
 */
public class CampusAdapter extends JsonArrayAdapterBase<JSONObject> {

    public CampusAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_campus, null);
            holder.corpName = (TextView) convertView.findViewById(R.id.tv_corpName);
            holder.pubDate = (TextView) convertView.findViewById(R.id.tv_pubDate);
            holder.jobName = (TextView) convertView.findViewById(R.id.tv_jobName);
            holder.salaryName = (TextView) convertView.findViewById(R.id.tv_salaryName);
            holder.treatment1 = (TextView) convertView.findViewById(R.id.tv_ftreatment);
            holder.treatment2 = (TextView) convertView.findViewById(R.id.tv_streatment);
            holder.treatment3 = (TextView) convertView.findViewById(R.id.tv_ttreatment3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        String treat = jsonObject.optString("treatment");

        holder.treatment1.setText(" ");
        holder.treatment2.setText(" ");
        holder.treatment3.setText(" ");

        if (!StringUtil.isEmpty(treat)) {
            String[] sp = treat.split(",");
            for (int i = 0; i < sp.length; i++) {
                if (i == 0) {
                    holder.treatment1.setText(sp[0]);
                } else if (i == 1) {
                    holder.treatment2.setText(sp[1]);
                } else if (i == 2) {
                    holder.treatment3.setText(sp[2]);
                }
            }
        }

        holder.corpName.setText(jsonObject.optString("corpName"));
        holder.pubDate.setText(jsonObject.optString("pubDate"));
        holder.jobName.setText(jsonObject.optString("jobName"));
        holder.salaryName.setText(jsonObject.optString("salaryName"));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView pubDate, jobName, corpName, salaryName, treatment1, treatment2, treatment3;
    }
}