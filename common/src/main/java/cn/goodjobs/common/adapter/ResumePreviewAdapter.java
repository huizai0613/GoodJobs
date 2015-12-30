package cn.goodjobs.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.goodjobs.common.R;
import cn.goodjobs.common.entity.ResumePreviewInfo;
import cn.goodjobs.common.view.ResumeItemView;

/**
 * Created by 王刚 on 2015/12/25 0025.
 */
public class ResumePreviewAdapter extends BaseExpandableListAdapter {

    public List<ResumePreviewInfo> mapList;
    private Context context;

    public ResumePreviewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return mapList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ResumePreviewInfo> childList = getGroup(groupPosition).resumePreviewInfoList;
        return childList.size();
    }

    @Override
    public ResumePreviewInfo getGroup(int groupPosition) {
        return mapList.get(groupPosition);
    }

    @Override
    public ResumePreviewInfo getChild(int groupPosition, int childPosition) {
        ResumePreviewInfo groupInfo = getGroup(groupPosition);
        return groupInfo.resumePreviewInfoList.get(childPosition);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_resume_preview_group, null);
            groupHolder = new GroupHolder();
            groupHolder.imgExpand = (ImageView) convertView.findViewById(R.id.imgExpand);
            groupHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        ResumePreviewInfo resumePreviewInfo = getGroup(groupPosition);
        groupHolder.tvTitle.setText(resumePreviewInfo.title);
        if (isExpanded) {
            groupHolder.imgExpand.setImageResource(R.drawable.job_expand_up);
        } else {
            groupHolder.imgExpand.setImageResource(R.drawable.job_expand_down);
        }
        return convertView;
    }

    static class GroupHolder {
        TextView tvTitle;
        ImageView imgExpand;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ResumePreviewInfo resumePreviewInfo = getChild(groupPosition, childPosition);
        if (resumePreviewInfo.line != null) {
            return resumePreviewInfo.line;
        } else {
            ResumeItemView resumeItemView = null;
            if (convertView != null && convertView instanceof ResumeItemView) {
                resumeItemView = (ResumeItemView) convertView;
            } else {
                resumeItemView = new ResumeItemView(context);
            }
            resumeItemView.setTitle(resumePreviewInfo.title);
            resumeItemView.setContent(resumePreviewInfo.content);
            return resumeItemView;
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
