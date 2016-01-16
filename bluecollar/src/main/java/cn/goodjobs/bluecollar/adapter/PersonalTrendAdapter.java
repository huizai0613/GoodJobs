package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.view.TrendItemView;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by 王刚 on 2015/12/21.
 * 动态列表
 */
public class PersonalTrendAdapter extends JsonArrayAdapterBase<JSONObject> {

    public boolean showLoading;

    @Override
    public int getCount() {
        if (showLoading) {
            return 1;
        }
        return super.getCount();
    }

    public PersonalTrendAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        if (showLoading) {
            return LayoutInflater.from(context).inflate(R.layout.item_loading, null);
        }
        ViewHolder holder;
        if (convertView == null || convertView instanceof RelativeLayout) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trend_personal, null);
            holder.viewTrend = (TrendItemView) convertView.findViewById(R.id.viewTrend);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        holder.viewTrend.showView(jsonObject);
        return convertView;
    }

    public class ViewHolder {
        TrendItemView viewTrend;
    }
}
