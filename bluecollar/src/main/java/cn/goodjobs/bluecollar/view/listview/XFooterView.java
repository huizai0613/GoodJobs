package cn.goodjobs.bluecollar.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.util.DensityUtil;

/**
 * @author Maxwin
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @description XListView's footer
 */
public class XFooterView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_LOADING = 2;

    private View mLayout;
    private View mProgressBar;
    private TextView mHintView;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private final int ROTATE_ANIM_DURATION = 180;
    private int mState = STATE_NORMAL;
    boolean isHideText;

    public XFooterView(Context context) {
        super(context);
        initView(context);
    }

    public XFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void setState(int state) {
        if (state == mState) return;

        if (state == STATE_LOADING) {
//            mHintImage.clearAnimation();
//            mHintImage.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mHintView.setVisibility(View.INVISIBLE);
        } else {
            if (!isHideText) {
                mHintView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
//            mHintImage.setVisibility(View.VISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
//                if (mState == STATE_READY) {
//                    mHintImage.startAnimation(mRotateDownAnim);
//                }
//                if (mState == STATE_LOADING) {
//                    mHintImage.clearAnimation();
//                }
                mProgressBar.setVisibility(View.INVISIBLE);
                mHintView.setText(MyStrings.load_more) ;
                break;

            case STATE_READY:
                if (mState != STATE_READY) {
//                    mHintImage.clearAnimation();
//                    mHintImage.startAnimation(mRotateUpAnim);
                    mHintView.setText(MyStrings.load_ready);
                }
                break;

            case STATE_LOADING:
                break;
        }
        mState = state;
    }

    public void setBottomMargin(int height) {
        if (height < 0) return;
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.bottomMargin = height;
        mLayout.setLayoutParams(lp);
    }

    public int getBottomMargin() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        return lp.bottomMargin;
    }

    /**
     * normal status
     */
    public void normal() {
        if (!isHideText) {
            mHintView.setVisibility(View.VISIBLE);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * loading status
     */
    public void loading() {
        mHintView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    public void hide() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = 0;
        mLayout.setLayoutParams(lp);
    }

    /**
     * show footer
     */
    public void show() {
        LayoutParams lp = (LayoutParams) mLayout.getLayoutParams();
        lp.height = LayoutParams.WRAP_CONTENT;
        mLayout.setLayoutParams(lp);
    }

    private void initView(Context context) {
        mLayout = new RelativeLayout(context) ;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(context, 50)) ;
        mLayout.setLayoutParams(layoutParams) ;

        ProgressBar temp = new ProgressBar(context) ;
        RelativeLayout.LayoutParams tempParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 50),
                DensityUtil.dip2px(context, 50)) ;
        tempParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE) ;
        temp.setLayoutParams(tempParams) ;
        temp.setVisibility(View.INVISIBLE) ;
        temp.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.iamge_progress));
        ((ViewGroup) mLayout).addView(temp) ;

        mProgressBar = new ProgressBar(context) ;
        RelativeLayout.LayoutParams progressParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(context, 20),
                DensityUtil.dip2px(context, 20)) ;
        progressParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE) ;
        mProgressBar.setLayoutParams(progressParams) ;
        ((ProgressBar) mProgressBar).setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.iamge_progress));
        mProgressBar.setVisibility(View.INVISIBLE) ;
        ((ViewGroup) mLayout).addView(mProgressBar) ;

        mHintView = new TextView(context) ;
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT) ;
        textParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE) ;
        mHintView.setLayoutParams(textParams) ;
        mHintView.setTextSize(17) ;
        mHintView.setTextColor(0xff4cb5e4) ;
        mHintView.setText(MyStrings.load_more) ;
        ((ViewGroup) mLayout).addView(mHintView) ;

        addView(mLayout);
        mRotateUpAnim = new RotateAnimation(0.0f, 180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void hideText() {
        isHideText = true;
        mHintView.setVisibility(View.INVISIBLE);
    }

}