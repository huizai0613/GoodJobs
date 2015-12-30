package cn.goodjobs.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by 王刚 on 2015/12/23 0023.
 */
public class PersonalApplyAdapter extends JsonArrayAdapterBase<JSONObject> {

    public PersonalApplyAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_personal_apply, null);
            viewHolder = new ViewHolder();
            viewHolder.btnCheck = (RelativeLayout) convertView.findViewById(R.id.btnCheck);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.tvCompany = (TextView) convertView.findViewById(R.id.tvCompany);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        if ("2".equals(jsonObject.optString("jobStatus"))) {
            viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.main_color));
            viewHolder.tvStatus.setText("");
        } else {
            viewHolder.tvTitle.setTextColor(context.getResources().getColor(R.color.light_color));
            viewHolder.tvStatus.setText("已过期");
        }
        viewHolder.tvTitle.setText(jsonObject.optString("jobName"));
        if (jsonObject.has("saveTime")) {
            viewHolder.tvTime.setText(jsonObject.optString("saveTime"));
        } else {
            viewHolder.tvTime.setText(jsonObject.optString("PubTime"));
        }
        viewHolder.tvCompany.setText(jsonObject.optString("corpName"));
        viewHolder.btnCheck.setSelected(jsonObject.optBoolean("isCheck"));
        return convertView;
    }

    static class ViewHolder {
        RelativeLayout btnCheck;
        TextView tvTitle, tvTime, tvStatus, tvCompany;
    }
}
