package cn.goodjobs.bluecollar.fragment.BlueJob;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueJobSearchResultActivity;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.JumpViewUtil;


public class Pro_type_adapter extends BaseAdapter
{
    // 定义Context
    private LayoutInflater mInflater;
    private ArrayList<Type> list;
    private Context context;
    private Type type;
    private final AbsListView.LayoutParams layoutParams;

    public Pro_type_adapter(Context context, ArrayList<Type> list)
    {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        layoutParams = new AbsListView.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 41));

    }

    @Override
    public int getCount()
    {
        if (list != null && list.size() > 0)
            return list.size();
        else
            return 0;
    }

    @Override
    public Type getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final MyView view;
        if (convertView == null) {
            view = new MyView();
            convertView = mInflater.inflate(R.layout.list_pro_type_item, null);
            view.name = (TextView) convertView.findViewById(R.id.typename);
            convertView.setLayoutParams(layoutParams);
            convertView.setTag(view);
        } else {
            view = (MyView) convertView.getTag();
        }
        if (list != null && list.size() > 0) {
            type = list.get(position);
            view.name.setText(type.getTypename());
        }

        convertView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                HashMap<String, Object> param = new HashMap<String, Object>();
                Type item = getItem(position);
                param.put("itemAddress", "合肥市");
                param.put("itemAddressId", "1043");
                param.put("itemJobfunc", item.getTypename());
                param.put("itemJobfuncId", item.getId() + "");
                param.put("jobpraentId", item.getPranetId() + "");
                JumpViewUtil.openActivityAndParam(context, BlueJobSearchResultActivity.class, param);


            }
        });

        return convertView;
    }


    private class MyView
    {
        private ImageView icon;
        private TextView name;
    }

}
