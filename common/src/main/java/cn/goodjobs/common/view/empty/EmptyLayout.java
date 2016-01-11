package cn.goodjobs.common.view.empty;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.http.ConnecStatus;


public class EmptyLayout extends LinearLayout implements
        android.view.View.OnClickListener
{// , ISkinUIObserver {

    public static final int HIDE_LAYOUT = 4;
    public static final int NETWORK_ERROR = 1;
    public static final int NETWORK_LOADING = 2;
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;
    public static final int NO_LOGIN = 6;


    private int errorImgResoure_notNet = R.drawable.icon_null_8;
    private int errorImgResoure_loadError = R.drawable.icon_nothing;
    private int notDataImgResoure = R.drawable.page_icon_empty;

    private final Context context;
    public ImageView errorImg;
    private ProgressBar animProgress;
    private boolean clickEnable = true;
    private android.view.View.OnClickListener listener;
    private int mErrorState;
    private RelativeLayout mLayout;
    private String strNoDataContent = "";
    private TextView tv;
    private TextView tv_but;
    private View loadBox;
    private TextView tipTextView;
    private ImageView loadImg;

    public EmptyLayout(Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        init();
    }

    /**
     * 设置没有网络,图片
     *
     * @param errorImgResoure_notNet
     */
    public void setErrorImgResoure_notNet(int errorImgResoure_notNet)
    {
        this.errorImgResoure_notNet = errorImgResoure_notNet;
    }

    /**
     * 设置加载失败,图片
     *
     * @param errorImgResoure_loadError
     */
    public void setErrorImgResoure_loadError(int errorImgResoure_loadError)
    {
        this.errorImgResoure_loadError = errorImgResoure_loadError;
    }

    /**
     * 设置没有数据,图片
     *
     * @param notDataImgResoure
     */
    public void setNotDataImgResoure(int notDataImgResoure)
    {
        this.notDataImgResoure = notDataImgResoure;
    }

    private void init()
    {
        View view = View.inflate(context, R.layout.view_error_layout, null);
        errorImg = (ImageView) view.findViewById(R.id.img_error_layout);
        loadImg = (ImageView) view.findViewById(R.id.load_img);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        tipTextView = (TextView) view.findViewById(R.id.tipTextView);
        mLayout = (RelativeLayout) view.findViewById(R.id.pageerrLayout);
        animProgress = (ProgressBar) view.findViewById(R.id.animProgress);
        loadBox = view.findViewById(R.id.load_box);
        setBackgroundColor(-1);
        setOnClickListener(this);
        errorImg.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if (clickEnable) {
                    // setErrorType(NETWORK_LOADING);
                    if (listener != null)
                        listener.onClick(v);
                }
            }
        });
        addView(view);
        changeErrorLayoutBgMode(context);
    }

    public void changeErrorLayoutBgMode(Context context1)
    {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss()
    {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState()
    {
        return mErrorState;
    }

    public boolean isLoadError()
    {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading()
    {
        return mErrorState == NETWORK_LOADING;
    }

    @Override
    public void onClick(View v)
    {
        if (clickEnable) {
            // setErrorType(NETWORK_LOADING);
            if (listener != null)
                listener.onClick(v);
        }
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        // MyApplication.getInstance().getAtSkinObserable().registered(this);
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        // MyApplication.getInstance().getAtSkinObserable().unregistered(this);
    }

    public void setErrorButContent(String content)
    {
        tv_but.setText(content);
        tv_but.setVisibility(View.VISIBLE);
    }

    public void setErrorButClickListener(View.OnClickListener clickListener)
    {
        tv_but.setOnClickListener(clickListener);
    }

    public void onSkinChanged()
    {
        // mLayout.setBackgroundColor(SkinsUtil
        // .getColor(getContext(), "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(getContext(), "textcolor05"));
    }

    public void setDayNight(boolean flag)
    {
    }

    public void setErrorMessage(String msg)
    {
        tv.setText(msg);
    }

    /**
     * 新添设置背景
     *
     * @author 火蚁 2015-1-27 下午2:14:00
     */
    public void setErrorImag(int imgResource)
    {
        try {
            errorImg.setImageResource(imgResource);
        } catch (Exception e) {
        }
    }

    public void setErrorType(int i)
    {
        loadBox.setBackgroundResource(android.R.color.transparent);
        tipTextView.setVisibility(View.GONE);
        loadImg.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"pagefailed_bg"));
                if (ConnecStatus.isNetworkAvailable(getContext())) {
                    tv.setText(R.string.error_view_load_error_click_to_refresh);
                    errorImg.setBackgroundResource(errorImgResoure_loadError);
                } else {
                    tv.setText(R.string.error_view_network_error_click_to_refresh);
                    errorImg.setBackgroundResource(errorImgResoure_notNet);
                }
                errorImg.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                clickEnable = true;
                break;
            case NETWORK_LOADING:
                tipTextView.setVisibility(View.VISIBLE);
                loadImg.setVisibility(View.VISIBLE);
                loadBox.setBackgroundResource(R.drawable.loading_dialog_bg);
                mErrorState = NETWORK_LOADING;
                // animProgress.setBackgroundDrawable(SkinsUtil.getDrawable(context,"loadingpage_bg"));
                animProgress.setVisibility(View.VISIBLE);
                errorImg.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                clickEnable = false;
                break;
            case NODATA:
                mErrorState = NODATA;
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                errorImg.setBackgroundResource(notDataImgResoure);
                errorImg.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState = NODATA_ENABLE_CLICK;
                errorImg.setBackgroundResource(notDataImgResoure);
                // img.setBackgroundDrawable(SkinsUtil.getDrawable(context,"page_icon_empty"));
                errorImg.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
                clickEnable = true;
                break;
            default:
                break;
        }
    }

    public void setNoDataContent(String noDataContent)
    {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(View.OnClickListener listener)
    {
        this.listener = listener;
    }

    public void setTvNoDataContent()
    {
        tv.setVisibility(View.VISIBLE);
        if (!strNoDataContent.equals(""))
            tv.setText(strNoDataContent);
        else
            tv.setText(R.string.error_view_no_data);
    }

    @Override
    public void setVisibility(int visibility)
    {
        if (visibility == View.GONE)
            mErrorState = HIDE_LAYOUT;
        super.setVisibility(visibility);
    }
}
