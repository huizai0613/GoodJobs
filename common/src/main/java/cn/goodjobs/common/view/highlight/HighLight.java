package cn.goodjobs.common.view.highlight;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.view.highlight.util.ViewUtils;
import cn.goodjobs.common.view.highlight.view.HightLightView;


/**
 * Created by zhy on 15/10/8.
 */
public class HighLight
{
    public static class ViewPosInfo
    {
        public int layoutId = -1;
        public RectF rectF;
        public MarginInfo marginInfo;
        public View view;
        public OnPosCallback onPosCallback;
        public boolean isLayout;
    }

    public static class MarginInfo
    {
        public float topMargin;
        public float leftMargin;
        public float rightMargin;
        public float bottomMargin;

    }

    public static interface OnPosCallback
    {
        void getPos(float rightMargin, float bottomMargin, RectF rectF, MarginInfo marginInfo);
    }


    private View mAnchor;
    private List<ViewPosInfo> mViewRects;
    private Context mContext;
    private HightLightView mHightLightView;

    private boolean intercept = true;
    private boolean shadow = true;
    private int maskColor = 0xCC000000;

    public HighLight(Context context)
    {
        mContext = context;
        mViewRects = new ArrayList<ViewPosInfo>();
        mAnchor = ((Activity) mContext).findViewById(android.R.id.content);
    }

    public HighLight anchor(View anchor)
    {
        mAnchor = anchor;
        return this;
    }

    public HighLight intercept(boolean intercept)
    {
        this.intercept = intercept;
        return this;
    }

    public HighLight shadow(boolean shadow)
    {
        this.shadow = shadow;
        return this;
    }

    public HighLight maskColor(int maskColor)
    {
        this.maskColor = maskColor;
        return this;
    }


    public HighLight addHighLight(int viewId, int decorLayoutId, OnPosCallback onPosCallback)
    {
        ViewGroup parent = (ViewGroup) mAnchor;
        View view = parent.findViewById(viewId);
        addHighLight(view, decorLayoutId, onPosCallback);
        return this;
    }

    public void updateInfo()
    {
        ViewGroup parent = (ViewGroup) mAnchor;
        for (ViewPosInfo viewPosInfo : mViewRects) {

            RectF rect = new RectF(ViewUtils.getLocationInView(parent, viewPosInfo.view));
//            if (!rect.equals(viewPosInfo.rectF))//TODO bug dismissed...fc...
            {
                viewPosInfo.rectF = rect;
                float r = viewPosInfo.rectF.right + DensityUtil.dip2px(parent.getContext(), 10);
                float l = viewPosInfo.rectF.left - DensityUtil.dip2px(parent.getContext(), 10);
                float t = viewPosInfo.rectF.top - DensityUtil.dip2px(parent.getContext(), 10);
                float b = viewPosInfo.rectF.bottom + DensityUtil.dip2px(parent.getContext(), 10);


                float h = b - t;
                float w = r - l;

                if (w > h) {
                    float v = w - h;
                    viewPosInfo.rectF.left = l;
                    viewPosInfo.rectF.right = r;
                    viewPosInfo.rectF.top = t - v / 2;
                    viewPosInfo.rectF.bottom = b + v / 2;
                } else if (w < h) {
                    float v = h - w;
                    viewPosInfo.rectF.top = t;
                    viewPosInfo.rectF.bottom = b;
                    viewPosInfo.rectF.left = l - v / 2;
                    viewPosInfo.rectF.right = r + v / 2;
                } else {
                    viewPosInfo.rectF.top = t;
                    viewPosInfo.rectF.bottom = b;
                    viewPosInfo.rectF.left = l;
                    viewPosInfo.rectF.right = r;
                }


                viewPosInfo.onPosCallback.getPos(parent.getWidth() - viewPosInfo.rectF.right, parent.getHeight() - viewPosInfo.rectF.bottom, viewPosInfo.rectF, viewPosInfo.marginInfo);
            }
        }

    }


    public HighLight addHighLight(View view, int decorLayoutId, OnPosCallback onPosCallback)
    {
        ViewGroup parent = (ViewGroup) mAnchor;
        RectF rect = new RectF(ViewUtils.getLocationInView(parent, view));

        ViewPosInfo viewPosInfo = new ViewPosInfo();
        viewPosInfo.layoutId = decorLayoutId;
        viewPosInfo.rectF = rect;
        viewPosInfo.view = view;
        if (onPosCallback == null && decorLayoutId != -1) {
            throw new IllegalArgumentException("onPosCallback can not be null.");
        }
        MarginInfo marginInfo = new MarginInfo();
        onPosCallback.getPos(parent.getWidth() - rect.right, parent.getHeight() - rect.bottom, rect, marginInfo);
        viewPosInfo.marginInfo = marginInfo;
        viewPosInfo.onPosCallback = onPosCallback;
        mViewRects.add(viewPosInfo);

        return this;
    }


    public void show()
    {

        if (mHightLightView != null) return;

        HightLightView hightLightView = new HightLightView(mContext, this, maskColor, shadow, mViewRects);
        if (mAnchor.getClass().getSimpleName().equals("FrameLayout")) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) mAnchor).addView(hightLightView, ((ViewGroup) mAnchor).getChildCount(), lp);

        } else {
            FrameLayout frameLayout = new FrameLayout(mContext);
            ViewGroup parent = (ViewGroup) mAnchor.getParent();
            parent.removeView(mAnchor);
            parent.addView(frameLayout, mAnchor.getLayoutParams());
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(mAnchor, lp);

            frameLayout.addView(hightLightView);
        }

        if (intercept) {
            hightLightView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    remove();
                }
            });
        }

        mHightLightView = hightLightView;
    }

    public void addClickListener(View.OnClickListener listener)
    {
        if (mHightLightView != null) {
            mHightLightView.setOnClickListener(listener);
        }
    }

    public void remove()
    {
        if (mHightLightView == null) return;
        ViewGroup parent = (ViewGroup) mHightLightView.getParent();
        if (parent instanceof RelativeLayout || parent instanceof FrameLayout) {
            parent.removeView(mHightLightView);
        } else {
            parent.removeView(mHightLightView);
            View origin = parent.getChildAt(0);
            ViewGroup graParent = (ViewGroup) parent.getParent();
            graParent.removeView(parent);
            graParent.addView(origin, parent.getLayoutParams());
        }
        mHightLightView = null;
    }


}
