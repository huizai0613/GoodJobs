package cn.goodjobs.common.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.view.imagemenu.MyImageView;

/**
 * Created by zhuli on 2016/1/21.
 */
public class HelpActivity extends BaseActivity {

    private ImageButton btnBottom;
    private ImageView[] image;
    private ViewPager help;
    private List<TextView> list = new ArrayList<TextView>();
    private LinearLayout llPoint;
    private String[] names = {"zt1.jpg", "zt2.jpg", "zt3.jpg"};

    @Override
    protected int getLayoutID() {
        return R.layout.activity_help;
    }

    @Override
    protected void initWeightClick() {
        help.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeBg(position);
                if (position == names.length - 1) {
                    btnBottom.setVisibility(View.VISIBLE);
                } else {
                    btnBottom.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void changeBg(int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setBackgroundColor(getResources().getColor(R.color.topbar_bg));
            } else {
                list.get(i).setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }


    @Override
    protected void initWeight() {
        btnBottom = (ImageButton) findViewById(R.id.btn_bottom);
        help = (ViewPager) findViewById(R.id.vp_help);
        llPoint = (LinearLayout) findViewById(R.id.ll_point);
        image = new ImageView[names.length];
        for (int i = 0; i < names.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageDrawable(getDrawableFromAssets(this, names[i]));
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
            image[i] = iv;
            TextView tv = new TextView(this);
            if (i == 0) {
                tv.setBackgroundColor(getResources().getColor(R.color.topbar_bg));
            } else {
                tv.setBackgroundColor(getResources().getColor(R.color.white));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 20), DensityUtil.dip2px(this, 3));
            params.setMargins((int) getResources().getDimension(R.dimen.padding_small), 0, 0, 0);
            tv.setLayoutParams(params);
            llPoint.addView(tv);
            list.add(tv);
        }

        help.setAdapter(new MyAdapter(this));
        help.setCurrentItem(0);
    }

    public static Drawable getDrawableFromAssets(Context context, String fileName) {
        try {
            return BitmapDrawable.createFromStream(context.getResources().getAssets().open(fileName), fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void initData() {

    }

    class MyAdapter extends PagerAdapter {

        Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return image.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(image[position]);
            return image[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(image[position]);
        }
    }

    public void click(View v) {
        Intent intent = new Intent();
        intent.setClassName(this, "cn.goodjobs.client.activity.MainActivity");
        startActivity(intent);
    }

}
