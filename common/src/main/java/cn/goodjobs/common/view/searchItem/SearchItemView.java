package cn.goodjobs.common.view.searchItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;

/**
 * Created by zhuli on 2015/12/14.
 */
public class SearchItemView extends CombinedBaseView{
    public String title;
    private String result;
    private String hint;
    private boolean showImgArrow; // 默认显示右边的小箭头
    private boolean showLine; // 默认显示底部分割线
    private String necessity;
    private boolean showHLine; // 竖直的分割线
    private Drawable imgRes;

    public SearchItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public SearchItemView(Context context){
        super(context);
    }


    @Override
    protected int layoutResource() {
        return R.layout.item_search;
    }

    private void initView(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.listitem, 0, 0);
        title = a.getString(R.styleable.listitem_tvtitle);
        result = a.getString(R.styleable.listitem_text);
        hint = a.getString(R.styleable.listitem_hint);
        showImgArrow = a.getBoolean(R.styleable.listitem_showArrow, true);
        showLine = a.getBoolean(R.styleable.listitem_showLine, true);
        showHLine = a.getBoolean(R.styleable.listitem_showHLine, true);
        imgRes = a.getDrawable(R.styleable.listitem_image);
        necessity = a.getString(R.styleable.listitem_important);
        a.recycle();
    }


    @Override
    protected void onCreate(Context context) {
        super.onCreate(context);

        TextView tvTitle= (TextView) findViewById(R.id.tv_searchtitle);
        TextView tvResult= (TextView) findViewById(R.id.tv_searchresult);
        TextView tvNecessity= (TextView) findViewById(R.id.tvNecessity);
        ImageView imgArrow = (ImageView) findViewById(R.id.imgArrow);
        ImageView imageView = (ImageView) findViewById(R.id.imageview);

        View line = findViewById(R.id.line);
        View hline = findViewById(R.id.hline);
        tvTitle.setText(title);
        tvResult.setText(result);
        tvResult.setHint(hint);
        if (!showImgArrow) {
            imgArrow.setVisibility(View.INVISIBLE);
        }
        if (!showLine) {
            line.setVisibility(View.INVISIBLE);
        }
        if (imgRes != null) {
            imageView.setImageDrawable(imgRes);
            imageView.setVisibility(View.VISIBLE);
        }
        if (!showHLine) {
            hline.setVisibility(View.INVISIBLE);
        }
        if (StringUtil.isEmpty(necessity) || "gone".equals(necessity)) {
            tvNecessity.setVisibility(View.GONE);
        } else if ("visible".equals(necessity)) {
            tvNecessity.setVisibility(View.VISIBLE);
        } else if ("invisible".equals(necessity)) {
            tvNecessity.setVisibility(View.INVISIBLE);
        } else {
            tvNecessity.setVisibility(View.GONE);
        }
    }


    public void setText(String t) {
        TextView textview = (TextView) findViewById(R.id.tv_searchresult);
        textview.setText(t);
    }

    public void setHint(String t) {
        TextView textview = (TextView) findViewById(R.id.tv_searchresult);
        textview.setHint(t);
    }

    public String getText() {
        TextView textview = (TextView) findViewById(R.id.tv_searchresult);
        return textview.getText().toString().trim();
    }

    public boolean isEmpty() {
        if (StringUtil.isEmpty(getText())) {
            TipsUtil.show(getContext(), title.replaceAll(" +","") + "不能为空");
        }
        return StringUtil.isEmpty(getText());
    }
}
