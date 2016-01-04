package cn.goodjobs.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by 王刚 on 2015/12/23 0023.
 */
public class OpenSetAdapter extends JsonArrayAdapterBase<JSONObject> {

    public OpenSetAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_openset, null);
            viewHolder = new ViewHolder();
            viewHolder.btnDel = (ImageButton) convertView.findViewById(R.id.btnDel);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);
        viewHolder.btnDel.setTag(position);
        viewHolder.tvTitle.setText(jsonObject.optString("corpName"));
        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        ImageButton btnDel;
    }
}
