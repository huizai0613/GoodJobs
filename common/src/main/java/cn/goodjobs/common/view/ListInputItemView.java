package cn.goodjobs.common.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
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
import cn.goodjobs.common.view.searchItem.CombinedBaseView;

/**
 * Created by wanggang on 2015/10/8 0008.
 * 带输入框的item
 */
public class ListInputItemView extends CombinedBaseView {

    private String title;
    private String text;
    private String hint;
    private boolean necessity; // 是否带*号
    private String inputType;

    public ListInputItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }



    public ListInputItemView(Context context) {
        super(context);
    }

    @Override
    protected int layoutResource() {
        return R.layout.list_jobinput_item_view;
    }

    protected void initView(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.listitem, 0, 0);
        title = a.getString(R.styleable.listitem_tvtitle);
        text = a.getString(R.styleable.listitem_text);
        hint = a.getString(R.styleable.listitem_hint);
        inputType = a.getString(R.styleable.listitem_inputtype);
        necessity = a.getBoolean(R.styleable.listitem_necessity, false);
        a.recycle();
    }

    @Override
    protected void onCreate(Context context) {
        super.onCreate(context);
        //把initView里面取到的数据显示到界面上

        final ImageButton btnClear = (ImageButton) findViewById(R.id.btnClear);
        TextView tvNecessity = (TextView) findViewById(R.id.tvNecessity);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        final EditText editText = (EditText) findViewById(R.id.editText);

        if ("number".equals(inputType)) {
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if ("email".equals(inputType)) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS);
        } else if ("password".equals(inputType)) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        if (necessity) {
            tvNecessity.setVisibility(View.VISIBLE);
        } else {
            tvNecessity.setVisibility(View.INVISIBLE);
        }
        tvTitle.setText(title);
        editText.setHint(hint);
        if (StringUtil.isEmpty(text)) {
            btnClear.setVisibility(View.INVISIBLE);
        } else {
            editText.setText(text);
            btnClear.setVisibility(View.VISIBLE);
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
        ImageButton btnClear = (ImageButton) findViewById(R.id.btnClear);
        if (StringUtil.isEmpty(t)) {
            btnClear.setVisibility(View.INVISIBLE);
        } else {
            editText.setText(t);
            if (isEnabled()) {
                btnClear.setVisibility(View.VISIBLE);
            } else {
                btnClear.setVisibility(View.INVISIBLE);
            }
        }
    }

    public String getText() {
        EditText editText = (EditText) findViewById(R.id.editText);
        return editText.getText().toString().trim();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (!enabled) {
            ImageButton btnClear = (ImageButton) findViewById(R.id.btnClear);
            EditText editText = (EditText) findViewById(R.id.editText);
            editText.setHint("");
            editText.setEnabled(false);
            btnClear.setVisibility(View.INVISIBLE);
            btnClear.setEnabled(false);
        }
    }
}
