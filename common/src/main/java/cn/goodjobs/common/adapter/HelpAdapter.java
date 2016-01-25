package cn.goodjobs.common.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import cn.goodjobs.common.R;

/**
 * Created by wanggang on 2015/11/24 0024.
 */
public class HelpAdapter extends BaseExpandableListAdapter {

    public JSONArray jsonArray;
    private Context context;

    public HelpAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        if (jsonArray != null) {
            return jsonArray.length();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return jsonArray.optJSONObject(groupPosition).optString("title");
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return jsonArray.optJSONObject(groupPosition).optString("content");
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.expand_title, null);
            groupHolder = new GroupHolder();
            groupHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            groupHolder.imageView = (ImageView) convertView.findViewById(R.id.imageview);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.tvTitle.setText(getGroup(groupPosition));
        if (isExpanded) {
            groupHolder.imageView.setImageResource(R.drawable.icon_expand_up);
        } else {
            groupHolder.imageView.setImageResource(R.drawable.icon_expand_down);
        }
        return convertView;
    }

    static class GroupHolder {
        TextView tvTitle;
        ImageView imageView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView tvContent = (TextView) LayoutInflater.from(context).inflate(R.layout.expand_content, null);
        tvContent.setText(Html.fromHtml(getChild(groupPosition, childPosition)));
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        return tvContent;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
