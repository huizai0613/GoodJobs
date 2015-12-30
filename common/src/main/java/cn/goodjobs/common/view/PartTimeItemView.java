package cn.goodjobs.common.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.view.searchItem.CombinedBaseView;

/**
 * Created by zhuli on 2015/12/23.
 */
public class PartTimeItemView extends CombinedBaseView {

    private String title, content;
    private boolean isCall;
    private TextView tvContent;

    public PartTimeItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public PartTimeItemView(Context context) {
        super(context);
    }

    @Override
    protected int layoutResource() {
        return R.layout.list_parttime_item;
    }


    protected void initView(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray a = getContext().obtainStyledAttributes(attrs, cn.goodjobs.common.R.styleable.listitem, 0, 0);
        title = a.getString(cn.goodjobs.common.R.styleable.listitem_tvtitle);
        isCall = a.getBoolean(cn.goodjobs.common.R.styleable.listitem_isCall, false);
        a.recycle();
    }

    @Override
    protected void onCreate(final Context context) {
        super.onCreate(context);
        tvContent = (TextView) findViewById(R.id.tv_content);
        TextView tvTitle = (TextView) findViewById(R.id.tv_parttitle);
        ImageView ivCall = (ImageView) findViewById(R.id.iv_call);

        if (isCall) {
            ivCall.setEnabled(true);
            ivCall.setVisibility(View.VISIBLE);
        } else {
            ivCall.setVisibility(View.GONE);
            ivCall.setEnabled(false);
        }
        tvTitle.setText(title);

        ivCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getText()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public void setText(String t) {
        tvContent.setText(t);
    }

    public String getText() {
        return tvContent.getText().toString();
    }


}
