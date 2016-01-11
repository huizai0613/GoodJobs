package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.view.TrendItemView;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by 王刚 on 2015/12/21.
 * 动态列表
 */
public class TrendAdapter extends JsonArrayAdapterBase<JSONObject> {


    public TrendAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trend, null);
            holder.headPhoto= (SimpleDraweeView) convertView.findViewById(R.id.headPhoto);
            holder.tvName=(TextView)convertView.findViewById(R.id.tvName);
            holder.tvDistance=(TextView)convertView.findViewById(R.id.tvDistance);
            holder.tvAge=(TextView)convertView.findViewById(R.id.tvAge);
            holder.viewTrend= (TrendItemView) convertView.findViewById(R.id.viewTrend);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        return convertView;
    }

    public class ViewHolder {
        SimpleDraweeView headPhoto;
        TextView tvName, tvDistance, tvAge;
        TrendItemView viewTrend;
    }
}
