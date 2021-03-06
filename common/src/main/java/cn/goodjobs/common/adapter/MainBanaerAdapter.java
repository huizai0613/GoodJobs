package cn.goodjobs.common.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by wanggang on 2015/10/14 0014.
 * 首页顶部banaer栏
 */
public class MainBanaerAdapter extends PagerAdapter {
    private Context context;
    private List<SimpleDraweeView> imageList;

    public MainBanaerAdapter(Context context, List<SimpleDraweeView> imageList) {
        this.context = context;
        this.imageList=imageList;
    }

    @Override
    public int getCount() {
        if (imageList == null) {
            return 0;
        } else if (imageList.size() == 1) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView(imageList.get(position % imageList.size()));
    }

    // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageview = null;
        imageview = imageList.get(position % imageList.size());
        if (imageview.getParent() != null) {
            ((ViewGroup) imageview.getParent()).removeView(imageview);
        }
        view.addView(imageview, 0);
        return imageview;
    }
}
