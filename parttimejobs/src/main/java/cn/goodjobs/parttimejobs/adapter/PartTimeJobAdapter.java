package cn.goodjobs.parttimejobs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.parttimejobs.R;
import cn.goodjobs.parttimejobs.activity.PartJobDetailsActivity;

/**
 * Created by zhuli on 2015/12/23.
 */
public class PartTimeJobAdapter extends JsonArrayAdapterBase<JSONObject> {

    private List<Integer> data = new ArrayList<Integer>();

    public PartTimeJobAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_parttime, null);
            holder.tvtitle = (TextView) convertView.findViewById(R.id.tv_partJob);
            holder.tvcontent = (TextView) convertView.findViewById(R.id.tv_partContent);
            holder.tvdate = (TextView) convertView.findViewById(R.id.tv_partDate);
            holder.tvsalsry = (TextView) convertView.findViewById(R.id.tv_partSalary);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final JSONObject jsonObject = getItem(position);

        holder.tvtitle.setText(jsonObject.optString("corpName"));
        holder.tvcontent.setText(jsonObject.optString("jobName"));
        holder.tvsalsry.setText(jsonObject.optString("jobSalary"));
        holder.tvdate.setText(jsonObject.optString("pubDate"));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                for (int i = 0; i < mList.size(); i++) {
                    data.add(mList.get(i).optInt("jobID"));
                }
                Intent intent = new Intent(context, PartJobDetailsActivity.class);
                intent.putExtra("item", position);
                intent.putIntegerArrayListExtra("idList", (ArrayList<Integer>) data);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView tvtitle, tvcontent, tvdate, tvsalsry;
    }
}
