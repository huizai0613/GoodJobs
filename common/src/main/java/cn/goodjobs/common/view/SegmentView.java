package cn.goodjobs.common.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import cn.goodjobs.common.R;


/**
 * Created by YeXiangYu on 15-8-20.
 */
public class SegmentView extends LinearLayout
{

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private onSegmentViewClickListener listener;

    private int textRes;
    private int bgRes;

    public void setTextRes(int textRes)
    {
        this.textRes = textRes;
        XmlPullParser xrp = getResources().getXml(textRes);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
            textView1.setTextColor(csl);
            textView2.setTextColor(csl);
            textView3.setTextColor(csl);
        } catch (Exception e) {
        }
    }

    public void setBgRes(int bgRes)
    {
        this.bgRes = bgRes;
        textView1.setBackgroundResource(bgRes);
        textView2.setBackgroundResource(bgRes);
        textView3.setBackgroundResource(bgRes);
    }

    public SegmentView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public SegmentView(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
//		this.setLayoutParams(new LinearLayout.LayoutParams(dp2Px(getContext(), 60), LinearLayout.LayoutParams.WRAP_CONTENT));
        textView1 = new TextView(getContext());
        textView2 = new TextView(getContext());
        textView3 = new TextView(getContext());
        textView1.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
        textView2.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
        textView3.setLayoutParams(new LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
        textView1.setText("SEG1");
        textView2.setText("SEG2");
        textView3.setText("SEG2");
        XmlPullParser xrp = getResources().getXml(R.drawable.seg_text_color_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(), xrp);
            textView1.setTextColor(csl);
            textView2.setTextColor(csl);
            textView3.setTextColor(csl);
        } catch (Exception e) {
        }
        textView1.setGravity(Gravity.CENTER);
        textView2.setGravity(Gravity.CENTER);
        textView3.setGravity(Gravity.CENTER);
        textView1.setPadding(3, 6, 3, 6);
        textView2.setPadding(3, 6, 3, 6);
        textView3.setPadding(3, 6, 3, 6);
        setSegmentTextSize(14);
        textView1.setBackgroundResource(R.drawable.seg_right);
        textView2.setBackgroundResource(R.drawable.seg_right);
        textView3.setBackgroundResource(R.drawable.seg_right);
        this.removeAllViews();
        this.addView(textView1);
        this.addView(textView2);
        this.addView(textView3);
        this.invalidate();

        textView1.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (textView1.isSelected()) {
                    return;
                }
                textView1.setSelected(true);
                textView2.setSelected(false);
                textView3.setSelected(false);
                if (listener != null) {
                    listener.onSegmentViewClick(textView1, 0);
                }
            }
        });
        textView2.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (textView2.isSelected()) {
                    return;
                }
                textView2.setSelected(true);
                textView1.setSelected(false);
                textView3.setSelected(false);
                if (listener != null) {
                    listener.onSegmentViewClick(textView2, 1);
                }
            }
        });
        textView3.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (textView3.isSelected()) {
                    return;
                }
                textView3.setSelected(true);
                textView1.setSelected(false);
                textView2.setSelected(false);
                if (listener != null) {
                    listener.onSegmentViewClick(textView3, 2);
                }
            }
        });
    }

    /**
     * 设置字体大小 单位dip
     * <p>2014年7月18日</p>
     *
     * @param sp
     * @author YEXIANGYU
     */
    public void setSegmentTextSize(int sp)
    {
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    /**
     * 设置选中
     *
     * @param position
     */
    public void perClick(int position)
    {
        switch (position) {
            case 0:
                textView1.performClick();
                break;
            case 1:
                textView2.performClick();
                break;
            case 2:
                textView3.performClick();
                break;
        }
    }

    /**
     * 设置选中
     *
     * @param isMultiType
     */
    public void multiType(boolean isMultiType)
    {
        textView3.setVisibility(isMultiType ? View.VISIBLE : View.GONE);
    }


    private static int dp2Px(Context context, float dp)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public void setOnSegmentViewClickListener(onSegmentViewClickListener listener)
    {
        this.listener = listener;
    }


    /**
     * 设置文字
     * <p>2014年7月18日</p>
     *
     * @param text
     * @param position
     * @author YEXIANGYU
     */
    public void setSegmentText(CharSequence text, int position)
    {
        if (position == 0) {
            textView1.setText(text);
        }
        if (position == 1) {
            textView2.setText(text);
        }
        if (position == 2) {
            textView3.setText(text);
        }
    }

    public interface onSegmentViewClickListener
    {
        /**
         * <p>2014年7月18日</p>
         *
         * @param v
         * @param position 0-左边 1-右边
         * @author YEXIANGYU
         */
        void onSegmentViewClick(View v, int position);
    }
}

