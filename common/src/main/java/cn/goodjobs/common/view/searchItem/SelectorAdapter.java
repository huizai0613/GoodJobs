package cn.goodjobs.common.view.searchItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.AdapterBase;


/**
 * Created by wanggang on 2015/10/18 0018.
 */
public class SelectorAdapter extends AdapterBase<SelectorEntity> {

    public SelectorAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || "tips".equals(convertView.getTag().toString())) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.imgSelected = (ImageView) convertView.findViewById(R.id.imgSelected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SelectorEntity selectorEntity = getItem(position);
        viewHolder.tvTitle.setText(selectorEntity.name);
        if (selectorEntity.isSelected) {
            viewHolder.imgSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgSelected.setVisibility(View.INVISIBLE);
        }
        return convertView;
}

    static class ViewHolder {
        TextView tvTitle;
        ImageView imgSelected;
    }
}
