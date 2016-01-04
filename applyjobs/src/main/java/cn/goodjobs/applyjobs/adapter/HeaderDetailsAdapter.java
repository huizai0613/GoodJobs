package cn.goodjobs.applyjobs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.activity.JobNewsDetailsActivity;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.baseclass.RecyclerViewBaseAdapter;
import cn.goodjobs.common.baseclass.RecyclerViewHolderBase;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.MyRecycler.XFooterView;

/**
 * Created by zhuli on 2015/12/21.
 */
public class HeaderDetailsAdapter extends JsonArrayAdapterBase<JSONObject> {

    public HeaderDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_headdetails, null);
            holder.tvname = (TextView) convertView.findViewById(R.id.tv_jobfair_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final JSONObject jsonObject = getItem(position);
        holder.tvname.setText(jsonObject.optString("newstitle"));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JobNewsDetailsActivity.class);
                intent.putExtra("newsid", jsonObject.optInt("newsID"));
                intent.putExtra("type", "html");
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView tvname;
    }


}
