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
 * 好友列表
 */
public class FriendsAdapter extends JsonArrayAdapterBase<JSONObject> {


    public FriendsAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friends, null);
            holder.headPhoto = (SimpleDraweeView) convertView.findViewById(R.id.headPhoto);
            holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            holder.btnFans = (TextView)convertView.findViewById(R.id.btnFans);
            holder.btnMsg = (TextView)convertView.findViewById(R.id.btnMsg);
            holder.tvAddress = (TextView)convertView.findViewById(R.id.tvAddress);
            holder.tvAge = (TextView)convertView.findViewById(R.id.tvAge);
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
        holder.tvAge.setText(jsonObject.optString("age"));
        holder.tvAddress.setText(jsonObject.optString("cityName"));
        if ("女".equals(jsonObject.optString("sexName"))) {
            ImageUtil.setDrawable(context, holder.tvAge, R.mipmap.img_female, 1);
            holder.tvAge.setBackgroundResource(R.drawable.small_button_pink);
        } else {
            ImageUtil.setDrawable(context, holder.tvAge, R.mipmap.img_mail, 1);
            holder.tvAge.setBackgroundResource(R.drawable.small_button_green);
        }
        holder.btnFans.setVisibility(View.VISIBLE);
        if ("yes".equals(jsonObject.optString("followHas"))) {
            holder.btnFans.setText("取消关注");
            holder.btnFans.setBackgroundResource(R.drawable.small_button_grey);
        } else if ("no".equals(jsonObject.optString("followHas"))) {
            holder.btnFans.setText("    关注    ");
            holder.btnFans.setBackgroundResource(R.drawable.small_button_green);
        } else {
            holder.btnFans.setVisibility(View.INVISIBLE);
        }
        if ("1".equals(jsonObject.optString("SmsHas"))) {
            holder.btnMsg.setVisibility(View.VISIBLE);
        } else {
            holder.btnMsg.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        SimpleDraweeView headPhoto;
        TextView tvName, btnFans, tvAge, btnMsg, tvAddress;
    }
}
