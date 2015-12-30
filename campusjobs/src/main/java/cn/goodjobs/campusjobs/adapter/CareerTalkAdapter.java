package cn.goodjobs.campusjobs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.TipsUtil;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CareerTalkAdapter extends JsonArrayAdapterBase<JSONObject> {


    public CareerTalkAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_jobfair, null);
            holder.corpName = (TextView) convertView.findViewById(R.id.tv_corpName);
            holder.pubDate = (TextView) convertView.findViewById(R.id.tv_pubDate);
            holder.schoolAddress = (TextView) convertView.findViewById(R.id.tv_schoolAddress);
            holder.originName = (TextView) convertView.findViewById(R.id.tv_originName);
            holder.fairWeek = (TextView) convertView.findViewById(R.id.tv_fairWeek);
            holder.fairNum = (TextView) convertView.findViewById(R.id.tv_fairNum);
            holder.fairClick = (TextView) convertView.findViewById(R.id.tv_fairClick);
            holder.followHas = (TextView) convertView.findViewById(R.id.tv_followHas);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);

        holder.corpName.setText(jsonObject.optString("corpName"));
        holder.pubDate.setText(jsonObject.optString("pubDate"));
        holder.schoolAddress.setText(jsonObject.optString("schoolAddress"));
        holder.originName.setText(jsonObject.optString("originName"));
        holder.fairWeek.setText(jsonObject.optString("fairWeek"));
        holder.fairNum.setText("已有" + jsonObject.optString("fairNum") + "人关注");
        holder.fairClick.setText(jsonObject.optString("fairClick"));
        if (jsonObject.optString("followHas").equals("1")) {
            holder.followHas.setBackgroundColor(context.getResources().getColor(R.color.bg_color));
        } else if (jsonObject.optString("followHas").equals("0")) {
            holder.followHas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TipsUtil.show(context, "关注");
                }
            });
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TipsUtil.show(context, "点击");
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView corpName, pubDate, schoolAddress, originName, fairWeek, fairNum, fairClick, followHas;
    }
}

