package cn.goodjobs.bluecollar.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

/**
 * Created by 王刚 on 2015/12/21.
 * 好友列表
 */
public class LookAdapter extends JsonArrayAdapterBase<JSONObject> implements View.OnClickListener, HttpResponseHandler {

    JSONObject itemObject;
    public LookListener lookListener;
    public LookAdapter(Context context) {
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
            holder.btnFans.setOnClickListener(this);
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
        holder.btnFans.setTag(position);
        if ("yes".equals(jsonObject.optString("followHas")) || "all".equals(jsonObject.optString("followHas"))) {
            holder.btnFans.setText("取消关注");
            holder.btnFans.setBackgroundResource(R.drawable.small_button_grey);
            holder.btnFans.setTextColor(context.getResources().getColor(R.color.main_color));
        }
        if ("1".equals(jsonObject.optString("smsHas"))) {
            holder.btnMsg.setVisibility(View.VISIBLE);
        } else {
            holder.btnMsg.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnFans) {
            itemObject = getItem((int) v.getTag());
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("friendID", itemObject.optString("friendID"));
            if ("yes".equals(itemObject.optString("followHas")) || "all".equals(itemObject.optString("followHas"))) {
                params.put("type", 1);
            } else if ("no".equals(itemObject.optString("followHas"))) {
                params.put("type", 2);
            } else {
                TipsUtil.show(context, "登录后才能关注TA");
                return;
            }
            LoadingDialog.showDialog((Activity) context);
            HttpUtil.post(URLS.MAKEFRIEND_FOLLOW, params, this);
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        if (tag.equals(URLS.MAKEFRIEND_FOLLOW)) {
            lookListener.remove();
        }
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {

    }

    @Override
    public void onProgress(String tag, int progress) {

    }

    public class ViewHolder {
        SimpleDraweeView headPhoto;
        TextView tvName, btnFans, tvAge, btnMsg, tvAddress;
    }

    public interface LookListener {
        public void remove();
    }
}
