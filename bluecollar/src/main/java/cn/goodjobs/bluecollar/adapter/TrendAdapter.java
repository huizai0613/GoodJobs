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
import cn.goodjobs.common.util.GeoUtils;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

/**
 * Created by 王刚 on 2015/12/21.
 * 动态列表
 */
public class TrendAdapter extends JsonArrayAdapterBase<JSONObject> {

    MyLocation myLocation;

    public TrendAdapter(Context context) {
        super(context);
        myLocation = (MyLocation) SharedPrefUtil.getObject("location");
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
        } else {
            holder.headPhoto.setImageResource(R.drawable.img_personal_default);
        }
        holder.tvName.setText(jsonObject.optString("nickName"));
        if (myLocation != null && jsonObject.optDouble("fdLng") != 0 && jsonObject.optDouble("fdLat") != 0) {
            holder.tvDistance.setVisibility(View.VISIBLE);
            holder.tvDistance.setText(GeoUtils.friendlyDistance(GeoUtils.distance(myLocation.latitude, myLocation.longitude, jsonObject.optDouble("fdLat"), jsonObject.optDouble("fdLng"))));
        } else {
            holder.tvDistance.setVisibility(View.GONE);
        }
        holder.tvAge.setText(jsonObject.optString("ageName"));
        if ("女".equals(jsonObject.optString("sexName"))) {
            ImageUtil.setDrawable(context, holder.tvAge, R.mipmap.img_female, 1);
            holder.tvAge.setBackgroundResource(R.drawable.small_button_pink);
        } else {
            ImageUtil.setDrawable(context, holder.tvAge, R.mipmap.img_mail, 1);
            holder.tvAge.setBackgroundResource(R.drawable.small_button_green);
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
