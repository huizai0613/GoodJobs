package cn.goodjobs.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by 王刚 on 2015/12/23 0023.
 */
public class AddOpenSetAdapter extends JsonArrayAdapterBase<JSONObject> {

    public AddOpenSetAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_addopenset, null);
            viewHolder = new ViewHolder();
            viewHolder.btnCheck = (ImageView) convertView.findViewById(R.id.btnCheck);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        viewHolder.tvTitle.setText(jsonObject.optString("corpName"));
        viewHolder.btnCheck.setSelected(jsonObject.optBoolean("check"));
        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        ImageView btnCheck;
    }

    public String getCheckIds() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<getCount(); ++i) {
            JSONObject jsonObject = getItem(i);
            if (jsonObject.optBoolean("check")) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(jsonObject.optString("memCorpID"));
            }
        }
        return sb.toString();
    }
}
