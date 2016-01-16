package cn.goodjobs.bluecollar.view.upload;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by wanggang  on 2016/1/15 0015.
 */
public class CustomerListView extends ListView implements AbsListView.OnScrollListener {
    private MyScrollListener myScrollListener;

    public void setMyScrollListener(MyScrollListener myScrollListener) {
        this.myScrollListener = myScrollListener;
    }

    public CustomerListView(Context context) {
        super(context);
        this.setOnScrollListener(this);
    }

    public CustomerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnScrollListener(this);
    }

    public CustomerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 当不滚动时
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            // 判断是否滚动到底部
            if (view.getLastVisiblePosition() == view.getCount() - 1) {
                //加载更多功能的代码
                myScrollListener.toFoot();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    public interface MyScrollListener {
        public void toFoot();
    }
}
