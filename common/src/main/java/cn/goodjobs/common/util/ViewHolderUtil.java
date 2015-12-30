package cn.goodjobs.common.util;

import android.util.SparseArray;
import android.view.View;

import cn.goodjobs.common.R;


public class ViewHolderUtil
{
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag(R.string.tag);
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(R.string.tag, viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

}
