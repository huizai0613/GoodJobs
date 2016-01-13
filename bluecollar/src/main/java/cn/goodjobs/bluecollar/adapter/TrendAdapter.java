package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.view.TrendItemView;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.StringUtil;

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
            holder.headPhoto = (SimpleDraweeView) convertView.findViewById(R.id.headPhoto);
            holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            holder.tvDistance = (TextView)convertView.findViewById(R.id.tvDistance);
            holder.tvAge = (TextView)convertView.findViewById(R.id.tvAge);
            holder.viewTrend = (TrendItemView) convertView.findViewById(R.id.viewTrend);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        if (!StringUtil.isEmpty(jsonObject.optString("userPhoto"))) {
            Uri uri = Uri.parse(jsonObject.optString("userPhoto"));
            holder.headPhoto.setImageURI(uri);
        }
        holder.tvName.setText(jsonObject.optString("nickName"));
        holder.tvDistance.setText(jsonObject.optString("distance"));
        holder.tvAge.setText(jsonObject.optString("ageName"));
        if ("女".equals(jsonObject.optString("ageName"))) {
            ImageUtil.setDrawable(context, holder.tvAge, R.mipmap.img_female, 1);
            holder.tvAge.setBackgroundColor(context.getResources().getColor(R.color.pink));
        } else {
            ImageUtil.setDrawable(context, holder.tvAge, R.mipmap.img_mail, 1);
            holder.tvAge.setBackgroundColor(context.getResources().getColor(R.color.green));
        }
        holder.viewTrend.showView(jsonObject);
        return convertView;
    }

    public class ViewHolder {
        SimpleDraweeView headPhoto;
        TextView tvName, tvDistance, tvAge;
        TrendItemView viewTrend;
    }
}
