package cn.goodjobs.bluecollar.view;

import android.content.Context;
import android.util.AttributeSet;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.view.searchItem.CombinedBaseView;

/**
 * Created by 王刚 on 2016/1/7 0007.
 * 动态
 */
public class TrendItemView extends CombinedBaseView {

    public TrendItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int layoutResource() {
        return R.layout.view_item_trend;
    }
}
