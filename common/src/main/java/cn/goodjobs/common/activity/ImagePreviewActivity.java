package cn.goodjobs.common.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.RecyclingPagerAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.view.HackyViewPager;
import cn.goodjobs.common.view.photoview.PhotoView;
import cn.goodjobs.common.view.photoview.PhotoViewAttacher;


public class ImagePreviewActivity extends BaseActivity implements
        OnPageChangeListener
{
    public static final String BUNDLE_KEY_IMAGES = "bundle_key_images";
    public static final String BUNDLE_KEY_PHOTOS = "bundle_key_photos";
    public static final String BUNDLE_KEY_DELETE = "bundle_key_delete";
    private static final String BUNDLE_KEY_INDEX = "bundle_key_index";
    private int mIndex = 0;
    private HackyViewPager mViewPager;
    private ImagePageAdapter mAdapter;
    private TextView mTvImgIndex;
    private int mCurrentPostion = 0;
    private boolean mShowDelete = false;//是否支持删除
    private boolean isDelete;

    private Handler mHandler = new Handler(

    );
    private ArrayList<String> data;


    public static void showImagePrivew(Context context, int index, boolean mShowDelete,
                                       ArrayList<String> images)
    {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(BUNDLE_KEY_PHOTOS, images);
        intent.putExtra(BUNDLE_KEY_INDEX, index);
        context.startActivity(intent);
    }

    public void singleClick()
    {
        finish();
        this.overridePendingTransition(R.anim.in_from_nochange,
                R.anim.in_from_big2small);
    }


    @Override
    public void initData()
    {
        data = (ArrayList<String>) getIntent().getSerializableExtra(BUNDLE_KEY_PHOTOS);
        mIndex = getIntent().getIntExtra(BUNDLE_KEY_INDEX, 0);
    }


    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_image_preview;
    }

    @Override
    protected void initWeightClick()
    {

    }

    @Override
    protected void initWeight()
    {
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mTvImgIndex = (TextView) findViewById(R.id.tv_img_index);

        mAdapter = new ImagePageAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setCurrentItem(mIndex);
        onPageSelected(mIndex);
    }

    @Override
    public void onPageScrollStateChanged(int arg0)
    {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2)
    {

    }

    @Override
    public void onPageSelected(int idx)
    {
        mCurrentPostion = idx;
        if (data != null && data.size() > 1) {
            if (mTvImgIndex != null) {
                mTvImgIndex.setText((mCurrentPostion + 1) + "/"
                        + data.size());
            }
        }
    }

    static class ViewHolder
    {
        PhotoView image;
        ProgressBar progress;

        ViewHolder(View view)
        {
            image = (PhotoView) view.findViewById(R.id.scaleimageview);
            progress = (ProgressBar) view.findViewById(R.id.progress);
        }
    }

    class ImagePageAdapter extends RecyclingPagerAdapter
    {


        public String getItem(int position)
        {
            return data.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container)
        {
            ViewHolder vh = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(container.getContext())
                        .inflate(R.layout.v_image_preview_item, null);
                vh = new ViewHolder(convertView);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }
            vh.image.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener()
            {

                @Override
                public void onViewTap(View view, float x, float y)
                {
                    singleClick();
                }
            });
            final ProgressBar bar = vh.progress;


            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setControllerListener(new ControllerListener<ImageInfo>()
                    {

                        @Override
                        public void onSubmit(String id, Object callerContext)
                        {
                            bar.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable)
                        {
                            bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onIntermediateImageSet(String id, ImageInfo imageInfo)
                        {

                        }

                        @Override
                        public void onIntermediateImageFailed(String id, Throwable throwable)
                        {
                            bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(String id, Throwable throwable)
                        {
                            bar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onRelease(String id)
                        {

                        }
                    })
                    .setUri(Uri.parse(getItem(position)))
                            // other setters
                    .build();
            vh.image.setController(controller);

            return convertView;
        }

        @Override
        public int getCount()
        {
            return data.size();
        }

    }

}
