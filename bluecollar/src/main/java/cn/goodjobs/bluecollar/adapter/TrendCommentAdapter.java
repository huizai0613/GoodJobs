package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.StringUtil;

/**
 * Created by 王刚 on 2015/12/21.
 * 动态评论列表
 */
public class TrendCommentAdapter extends JsonArrayAdapterBase<JSONObject> {

    public boolean showLoading;

    @Override
    public int getCount() {
        if (showLoading) {
            return 1;
        }
        return super.getCount();
    }

    public TrendCommentAdapter(Context context) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trend_comment, null);
            holder.headPhoto = (SimpleDraweeView) convertView.findViewById(R.id.headPhoto);
            holder.tvName = (TextView)convertView.findViewById(R.id.tvName);
            holder.tvTime = (TextView)convertView.findViewById(R.id.tvTime);
            holder.tvContent = (TextView)convertView.findViewById(R.id.tvContent);
            holder.tvReplyContent = (TextView)convertView.findViewById(R.id.tvReplyContent);
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
        holder.tvTime.setText(jsonObject.optString("saveDate"));
        holder.tvContent.setText(jsonObject.optString("content"));

        JSONArray relpyArray = jsonObject.optJSONArray("replyArr");
        if (relpyArray != null && relpyArray.length() > 0) {
            holder.tvReplyContent.setVisibility(View.VISIBLE);
            JSONObject replyObject = relpyArray.optJSONObject(0);
            String html = "<font color='#3492e9'>" + replyObject.optString("nickName") + "</font> 回复 "
                    + "<font color='#3492e9'>" + jsonObject.optString("nickName") + "</font>："
                    + replyObject.optString("content");
            holder.tvReplyContent.setText(Html.fromHtml(html));
        } else {
            holder.tvReplyContent.setVisibility(View.GONE);
        }
        return convertView;
    }

    public class ViewHolder {
        SimpleDraweeView headPhoto;
        TextView tvName, tvTime, tvContent, tvReplyContent;
    }
}
