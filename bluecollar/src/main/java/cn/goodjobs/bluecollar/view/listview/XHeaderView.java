package cn.goodjobs.bluecollar.view.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.util.DensityUtil;


/**
 * @author Maxwin
 * @file XHeaderView.java
 * @create Apr 18, 2012 5:22:27 PM
 * @description XListView's header
 */
public class XHeaderView extends LinearLayout {
    private LinearLayout mContainer;
    private ImageView mArrowImageView;
    private ProgressBar mProgressBar;
    private TextView mHintTextView;
    public TextView header_hint_time ;
    public RelativeLayout header_content ;
    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private boolean mIsFirst;
    private boolean isFirst = true;

    public XHeaderView(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @SuppressLint("NewApi")
    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mContainer = new LinearLayout(context) ;
        mContainer.setClickable(true);
        mContainer.setLayoutParams(lp) ;
        mContainer.setGravity(Gravity.BOTTOM) ;

        header_content = new RelativeLayout(context) ;
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 50)) ;
        header_content.setLayoutParams(headerParams) ;
        LinearLayout header_text_layout = new LinearLayout(context) ;
        RelativeLayout.LayoutParams headerTextParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        headerTextParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE) ;
        header_text_layout.setLayoutParams(headerTextParams) ;
        header_text_layout.setGravity(Gravity.CENTER) ;
        header_text_layout.setOrientation(LinearLayout.VERTICAL) ;
        header_text_layout.setId(android.R.id.text1);

        mHintTextView = new TextView(context) ;
        header_hint_time = new TextView(context) ;
        mHintTextView.setTextSize(15);
        header_hint_time.setTextSize(12);

        LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        mHintTextView.setLayoutParams(textParams) ;
        header_hint_time.setLayoutParams(textParams) ;
        mHintTextView.setTextColor(Color.parseColor("#ff999999"));
        mHintTextView.setText(MyStrings.first_loading) ;
        header_hint_time.setTextColor(Color.parseColor("#ffc9c9c9"));
        header_hint_time.setText(MyStrings.pre_refresh_time);
        header_text_layout.addView(mHintTextView) ;
//        header_text_layout.addView(header_hint_time) ;
        header_content.addView(header_text_layout) ;

        mProgressBar = new ProgressBar(context) ;
        RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 20),
                DensityUtil.dip2px(context, 20)) ;
        progressParams.rightMargin = DensityUtil.dip2px(context, 16) ;
        progressParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE) ;
        progressParams.addRule(RelativeLayout.LEFT_OF, android.R.id.text1);
        mProgressBar.setLayoutParams(progressParams) ;
        mProgressBar.setVisibility(View.INVISIBLE) ;
        mProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.iamge_progress));
        header_content.addView(mProgressBar) ;

        mArrowImageView = new ImageView(context) ;
        mArrowImageView.setLayoutParams(progressParams) ;
        mArrowImageView.setScaleType(ScaleType.FIT_CENTER) ;
        mArrowImageView.setImageResource(R.mipmap.arrow_down);
        header_content.addView(mArrowImageView);

        mContainer.addView(header_content) ;
        this.addView(mContainer) ;
        setGravity(Gravity.BOTTOM);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == mState && mIsFirst) {
            mIsFirst = true;
            return;
        }

        if (state == STATE_REFRESHING) { // 显示进度
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else { // 显示箭头图片
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }
                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }
                mHintTextView.setText(MyStrings.refresh_normal);
                break;

            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(MyStrings.refresh_ready);
                }
                break;

            case STATE_REFRESHING:
                if (isFirst) {
                    mHintTextView.setText(MyStrings.first_loading);
                    isFirst = false;
                } else {
                    mHintTextView.setText(MyStrings.refresh_loading);
                }

                break;
            default:
        }

        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getHeight();
    }

}
