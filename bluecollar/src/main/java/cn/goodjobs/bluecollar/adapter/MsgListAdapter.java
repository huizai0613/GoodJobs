package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.view.TrendItemView;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.StringUtil;

/**
 * Created by 王刚 on 2015/12/21.
 * 动态列表
 */
public class MsgListAdapter extends JsonArrayAdapterBase<JSONObject> {

    public MsgListAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView instanceof RelativeLayout) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_msglist, null);
            holder.headPhoto = (SimpleDraweeView) convertView.findViewById(R.id.headPhoto);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
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
        holder.tvTime.setText(jsonObject.optString("sendDate"));
        holder.tvContent.setText(jsonObject.optString("content"));
        holder.tvStatus.setText(jsonObject.optString("readsName"));
        return convertView;
    }

    public class ViewHolder {
        SimpleDraweeView headPhoto;
        TextView tvName, tvTime, tvContent, tvStatus;
    }
}
