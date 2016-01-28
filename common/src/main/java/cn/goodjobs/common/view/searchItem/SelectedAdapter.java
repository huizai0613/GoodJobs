package cn.goodjobs.common.view.searchItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.AdapterBase;

/**
 * Created by wanggang on 2015/10/19 0019.
 * 以选中的
 */
public class SelectedAdapter extends AdapterBase<SelectorEntity[]> {
    public SelectedAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_selected, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.tvTitle);
        SelectorEntity[] selectorEntitys = getItem(position);
        textView.setText(selectorEntitys[0].getAllName());
        return convertView;
    }
}
