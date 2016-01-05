package cn.goodjobs.common.view.searchItem;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;

/**
 * Created by wanggang on 2015/12/26.
 */
public class InputItemView extends CombinedBaseView{
    public String title;
    private String result;
    private String hint;
    private boolean showLine; // 默认显示底部分割线
    private String necessity;
    private boolean showHLine; // 竖直的分割线
    private String inputType;

    public InputItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public InputItemView(Context context){
        super(context);
    }


    @Override
    protected int layoutResource() {
        return R.layout.item_input;
    }

    private void initView(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.listitem, 0, 0);
        title = a.getString(R.styleable.listitem_tvtitle);
        result = a.getString(R.styleable.listitem_text);
        hint = a.getString(R.styleable.listitem_hint);
        showLine = a.getBoolean(R.styleable.listitem_showLine, true);
        showHLine = a.getBoolean(R.styleable.listitem_showHLine, true);
        necessity = a.getString(R.styleable.listitem_important);
        inputType = a.getString(R.styleable.listitem_inputtype);
        a.recycle();
    }


    @Override
    protected void onCreate(Context context) {
        super.onCreate(context);

        TextView tvTitle= (TextView) findViewById(R.id.tv_searchtitle);
        TextView tvNecessity= (TextView) findViewById(R.id.tvNecessity);
        ImageView imgArrow = (ImageView) findViewById(R.id.imgArrow);
        ImageView imageView = (ImageView) findViewById(R.id.imageview);
        final EditText editText = (EditText) findViewById(R.id.editText);
        final ImageButton btnClear = (ImageButton) findViewById(R.id.btnClear);

        View line = findViewById(R.id.line);
        View hline = findViewById(R.id.hline);
        tvTitle.setText(title);
        editText.setText(result);
        editText.setHint(hint);
        if (!showLine) {
            line.setVisibility(View.INVISIBLE);
        }
        if (!showHLine) {
            hline.setVisibility(View.INVISIBLE);
        }
        if ("number".equals(inputType)) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if ("email".equals(inputType)) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        } else if ("password".equals(inputType)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
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

        btnClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                btnClear.setVisibility(View.INVISIBLE);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEnabled() && !StringUtil.isEmpty(s.toString())) {
                    btnClear.setVisibility(View.VISIBLE);
                } else {
                    btnClear.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public void setText(String t) {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText(t);
        if (!StringUtil.isEmpty(t)) {
            ImageButton btnClear = (ImageButton) findViewById(R.id.btnClear);
            btnClear.setVisibility(View.VISIBLE);
        }
    }

    public void setHint(String t) {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setHint(t);
    }

    public String getText() {
        EditText editText = (EditText) findViewById(R.id.editText);
        return editText.getText().toString().trim();
    }

    public boolean isEmpty() {
        if (StringUtil.isEmpty(getText())) {
            TipsUtil.show(getContext(), title + "不能为空");
        }
        return StringUtil.isEmpty(getText());
    }

    public void setEditable(boolean editable) {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setEnabled(editable);
    }

    public void setEditGravityLeft() {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }
}
