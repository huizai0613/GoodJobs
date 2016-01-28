package cn.goodjobs.applyjobs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.activity.HeaderDetailsActivity;
import cn.goodjobs.applyjobs.activity.JobNewsDetailsActivity;
import cn.goodjobs.applyjobs.bean.JobFairChild;
import cn.goodjobs.applyjobs.bean.JobfairGroup;
import cn.goodjobs.common.util.TipsUtil;

/**
 * Created by zhuli on 2015/12/17.
 */
public class JobFairRecyclerAdapter extends BaseAdapter {

    private int HEADER_VIEW = 1;
    private int CHILD_VIEW = 0;
    private List<String> headerPosition = new ArrayList<String>();
    private List<String> content = new ArrayList<String>();
    private List<String> id = new ArrayList<String>();
    private List<JobfairGroup> group_list;
    private List<JobFairChild> child_list;
    private Context context;
    private int type;
    private boolean isHeader = false;

    public JobFairRecyclerAdapter(Context context, List<JobfairGroup> group_list, List<JobFairChild> child_list, int type) {
        headerPosition.add("0");
        this.group_list = group_list;
        this.child_list = child_list;
        this.context = context;
        this.type = type;
        initValue();
    }

    private void initValue() {
        int count = 0;
        for (int i = 0; i < group_list.size(); i++) {
            count = count + isHeader(i);
            headerPosition.add(String.valueOf(count));
            content.add(group_list.get(i).getName());
            id.add(group_list.get(i).getCatalogID());
            for (int j = 0; j < group_list.get(i).getList().size(); j++) {
                content.add(group_list.get(i).getList().get(j).getNewstitle());
                id.add(group_list.get(i).getList().get(j).getNewsID());
            }
        }
    }

    //判断header的位置
    public int isHeader(int i) {
        List<JobFairChild> list = group_list.get(i).getList();
        return list.size() + 1;
    }

    @Override
    public int getCount() {
        return group_list.size() + child_list.size();
    }

    @Override
    public Object getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        int count = 0;
        if (position == 0) {
            initValue();
        }
        holder = new MyViewHolder();
        for (int i = 0; i < headerPosition.size(); i++) {
            if (position == Integer.parseInt(headerPosition.get(i))) {
                count = 1;
            }
        }
        if (count == 1) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_fair_title, null);
        } else if (count == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_jobfair_content, null);
        }

        holder.tv = (TextView) convertView.findViewById(R.id.tv_jobfair_title);
        convertView.setTag(holder);
        holder.tv.setText(content.get(position));
        isHeader = false;
        for (int i = 0; i < headerPosition.size(); i++) {
            if (position == Integer.parseInt(headerPosition.get(i))) {
                isHeader = true;
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, HeaderDetailsActivity.class);
                        intent.putExtra("catalogID", Integer.parseInt(id.get(position)));
                        if (type == 0) {
                            intent.putExtra("type", content.get(position));
                            intent.putExtra("typeID", 0);
                        } else if (type == 1) {
                            intent.putExtra("type", content.get(position));
                            intent.putExtra("typeID", 1);
                        }
                        context.startActivity(intent);
                    }
                });
            }
        }
        if (!isHeader) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobNewsDetailsActivity.class);
                    intent.putExtra("newsid", Integer.parseInt(id.get(position)));
                    intent.putExtra("type", "html");
                    context.startActivity(intent);

                }
            });
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        for (int i = 0; i < headerPosition.size(); i++) {
            if (position == Integer.parseInt(headerPosition.get(i))) {
                return HEADER_VIEW;
            }
        }
        return CHILD_VIEW;
    }

    public class MyViewHolder {

        private TextView tv;
    }

}

