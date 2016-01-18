package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.StringUtil;

/**
 * Created by 王刚 on 2015/12/21.
 * 动态列表
 */
public class MsgDetailAdapter extends JsonArrayAdapterBase<JSONObject> {

    public String myUserPhoto;
    public String friendUserPhoto;


    public MsgDetailAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null || convertView instanceof RelativeLayout) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_msg_detail, null);
            holder.headPhoto1 = (SimpleDraweeView) convertView.findViewById(R.id.headPhoto1);
            holder.headPhoto2 = (SimpleDraweeView) convertView.findViewById(R.id.headPhoto2);
            holder.tvContent1 = (TextView) convertView.findViewById(R.id.tvContent1);
            holder.tvContent2 = (TextView) convertView.findViewById(R.id.tvContent2);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.imgReload = (ImageView) convertView.findViewById(R.id.imgReload);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            if (!StringUtil.isEmpty(myUserPhoto)) {
                Uri uri = Uri.parse(myUserPhoto);
                holder.headPhoto2.setImageURI(uri);
            }
            if (!StringUtil.isEmpty(friendUserPhoto)) {
                Uri uri = Uri.parse(friendUserPhoto);
                holder.headPhoto1.setImageURI(uri);
            }
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        if (StringUtil.isEmpty(jsonObject.optString("sendDate"))) {
            holder.tvTime.setVisibility(View.GONE);
        } else {
            holder.tvTime.setVisibility(View.VISIBLE);
            holder.tvTime.setText(jsonObject.optString("sendDate"));
        }
        holder.progressBar.setVisibility(View.GONE);
        holder.imgReload.setVisibility(View.GONE);
        if ("0".equals(jsonObject.optString("myHas"))) {
            holder.headPhoto1.setVisibility(View.VISIBLE);
            holder.tvContent1.setVisibility(View.VISIBLE);
            holder.headPhoto2.setVisibility(View.INVISIBLE);
            holder.tvContent2.setVisibility(View.INVISIBLE);

            holder.tvContent1.setText(jsonObject.optString("content"));
        } else {
            holder.headPhoto1.setVisibility(View.INVISIBLE);
            holder.tvContent1.setVisibility(View.INVISIBLE);
            holder.headPhoto2.setVisibility(View.VISIBLE);
            holder.tvContent2.setVisibility(View.VISIBLE);

            holder.tvContent2.setText(jsonObject.optString("content"));
            if ("1".equals(jsonObject.optString("status"))) {
                // 正在发送消息
                holder.progressBar.setVisibility(View.VISIBLE);
            } else if ("2".equals(jsonObject.optString("status"))) {
                // 发动消息失败
                holder.imgReload.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    public class ViewHolder {
        SimpleDraweeView headPhoto1, headPhoto2;
        TextView tvContent1, tvContent2, tvTime;
        ProgressBar progressBar;
        ImageView imgReload;
    }

}
