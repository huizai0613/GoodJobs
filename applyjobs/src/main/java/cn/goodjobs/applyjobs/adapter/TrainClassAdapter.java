package cn.goodjobs.applyjobs.adapter;

import android.content.Context;
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
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.view.MyRecycler.XFooterView;
import cn.goodjobs.common.baseclass.RecyclerViewBaseAdapter;
import cn.goodjobs.common.baseclass.RecyclerViewHolderBase;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;

/**
 * Created by zhuli on 2015/12/21.
 */
public class TrainClassAdapter extends JsonArrayAdapterBase<JSONObject> {


    public TrainClassAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trainclass, null);
            holder.tvname=(TextView)convertView.findViewById(R.id.tv_classname);
            holder.tvplace=(TextView)convertView.findViewById(R.id.tv_classplace);
            holder.tvupdate=(TextView)convertView.findViewById(R.id.tv_classupdate);
            holder.tvtime=(TextView)convertView.findViewById(R.id.tv_classtime);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        JSONObject jsonObject = getItem(position);

        holder.tvname.setText(jsonObject.optString("title"));
        holder.tvplace.setText(jsonObject.optString("classtype"));
        holder.tvupdate.setText(jsonObject.optString("examdate"));
        holder.tvtime.setText(jsonObject.optString("classdate"));
        return convertView;
    }

    public class ViewHolder {
        private TextView tvname, tvplace, tvupdate, tvtime;
    }
}
