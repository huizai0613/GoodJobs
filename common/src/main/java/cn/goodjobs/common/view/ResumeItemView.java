package cn.goodjobs.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.entity.LoginInfo;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.view.searchItem.CombinedBaseView;

/**
 * Created by wanggang on 2015/12/23.
 */
public class ResumeItemView extends RelativeLayout {
    TextView tvTitle;
    TextView tvContent;

    public ResumeItemView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.list_resume_view, null);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvContent = (TextView) view.findViewById(R.id.tv_content);
        addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setContent(String content) {
        tvContent.setText(content);
    }
}
