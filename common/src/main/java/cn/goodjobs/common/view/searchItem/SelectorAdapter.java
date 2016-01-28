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
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tvCount);
            viewHolder.imgSelected = (ImageView) convertView.findViewById(R.id.imgSelected);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SelectorEntity selectorEntity = getItem(position);
        viewHolder.tvTitle.setText(selectorEntity.name);
        if (selectorEntity.array != null) {
            viewHolder.imgSelected.setImageResource(R.drawable.icon_arrow);
            if (selectorEntity.selectedNum > 0) {
                viewHolder.tvCount.setVisibility(View.VISIBLE);
                viewHolder.tvCount.setText("已选择"+selectorEntity.selectedNum+"项");
            } else {
                viewHolder.tvCount.setVisibility(View.INVISIBLE);
            }
        } else {
            if (selectorEntity.isSelected) {
                viewHolder.imgSelected.setVisibility(View.VISIBLE);
            } else {
                viewHolder.imgSelected.setVisibility(View.INVISIBLE);
            }
        }
        return convertView;
}

    static class ViewHolder {
        TextView tvTitle, tvCount;
        ImageView imgSelected;
    }
}
