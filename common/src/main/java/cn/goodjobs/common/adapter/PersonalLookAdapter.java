package cn.goodjobs.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by 王刚 on 2015/12/23 0023.
 */
public class PersonalLookAdapter extends JsonArrayAdapterBase<JSONObject> {

    public PersonalLookAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_personal_look, null);
            viewHolder = new ViewHolder();
            viewHolder.btnCheck = (RelativeLayout) convertView.findViewById(R.id.btnCheck);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        viewHolder.tvTitle.setText(jsonObject.optString("corpName"));
        if (jsonObject.has("sendTime")) {
            viewHolder.tvTime.setText(jsonObject.optString("sendTime"));
        } else {
            viewHolder.tvTime.setText(jsonObject.optString("logDate"));
        }
        viewHolder.btnCheck.setSelected(jsonObject.optBoolean("isCheck"));
        return convertView;
    }

    static class ViewHolder {
        RelativeLayout btnCheck;
        TextView tvTitle, tvTime;
    }
}
