package cn.goodjobs.common.view.searchItem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.view.MyListView;

public class SelectorActivity extends BaseActivity implements AdapterView.OnItemClickListener
{

    public static SelectorItemView selectorItemView;

    private Button btnRight;
    private MyListView myListView;
    private TextView tvSelected;
    private ImageView imgExpand;
    private LinearLayout btnExpand;
    private MyListView listView;
    SelectorAdapter selectorAdapter;
    SelectedAdapter selectedAdapter; // 已选中的列表

    Handler handler;

    TranslateAnimation mShowAction;
    TranslateAnimation mHiddenAction;
    int level;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_selector;
    }

    @Override
    protected void initWeightClick()
    {

    }

    @Override
    protected void initWeight()
    {

    }


    private void initView()
    {
        selectorItemView.init();
        setTopTitle(selectorItemView.title);

        level = getIntent().getIntExtra("level", 0);
        myListView = (MyListView) findViewById(R.id.myListView);
        tvSelected = (TextView) findViewById(R.id.tvSelected);
        imgExpand = (ImageView) findViewById(R.id.imgExpand);
        btnExpand = (LinearLayout) findViewById(R.id.btnExpand);
        btnRight = (Button) findViewById(R.id.btn_right);
        listView = (MyListView) findViewById(R.id.listView);

        selectorAdapter = new SelectorAdapter(this);
        listView.setAdapter(selectorAdapter);
        listView.setOnItemClickListener(this);
        selectorAdapter.appendToList(selectorItemView.selectorEntityStack.lastElement());

        selectedAdapter = new SelectedAdapter(this);
        myListView.setAdapter(selectedAdapter);
        selectedAdapter.appendToList(selectorItemView.selectedItems);
        initTopSelectedText();
        initAnim();
        handler = new Handler();
        initRightBtn();
    }

    @Override
    protected void initData()
    {

    }


    /**
     * 初始化右上角的消息按钮
     */
    protected void initRightBtn()
    {
        if (selectorItemView.singleSelected) {
            btnRight.setVisibility(View.INVISIBLE);
        } else {
            btnRight.setOnClickListener(this);
            myListView.setOnItemClickListener(this);
            btnExpand.setOnClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (parent.getId() == R.id.listView) {
            SelectorEntity selectorEntity = null;
            selectorEntity = selectorAdapter.getItem(position);
            selectorItemView.curSelectedItems[level] = selectorEntity;
            if (selectorEntity.array != null) {
                // 包含下级列表
                Intent intent = new Intent(this, SelectorActivity.class);
                intent.putExtra("title", selectorEntity.name);
                intent.putExtra("level", level+1);
                selectorItemView.selectorEntityStack.add(selectorEntity.array);
                startActivity(intent);
            } else {
                if (selectorEntity.isSelected && !selectorItemView.singleSelected) {
                    selectorItemView.removeSelectedItem(selectorEntity);
                } else {
                    if (selectorItemView.singleSelected) {
                        // 单选
                        if (selectorItemView.selectedItems.size() > 0) {
                            removePre();
                        }
                    } else {
                        if (selectorEntity.id.startsWith(SelectorItemView.allId)) {
                            // 选择不限
//                            selectedAll();
                            for (SelectorEntity entity : selectorItemView.selectorEntityStack.lastElement()) {
                                selectorItemView.removeSelectedItem(entity);
                            }
                        } else {
                            SelectorEntity entity = selectorItemView.selectorEntityStack.lastElement().get(0);
                            if (entity.id.startsWith(SelectorItemView.allId)) {
                                if (entity.isSelected) {
                                    selectorItemView.removeSelectedItem(entity);
                                }
                            }
                        }
                    }
                    if (selectorItemView.selectedItems.size() >= selectorItemView.maxSelected) {
                        TipsUtil.show(this, "您最多只能选择" + selectorItemView.maxSelected + "项");
                        return;
                    }
                    doSelected(true);
                    selectorEntity.isSelected = true;
                    addSelectedItem(selectorEntity);
                }
                refreshUI();
                if (selectorItemView.singleSelected) {
                    sure();
                }
            }
        } else if (parent.getId() == R.id.myListView) {
            SelectorEntity[] selectorEntitys = selectedAdapter.getItem(position);
            selectorItemView.removeSelectedItem(selectorEntitys[0]);
            refreshUI();
        }
    }

    private void doSelected(boolean isAdd) {
        int size = selectorItemView.curSelectedItems.length;
        if (size > 0) {
            for (int i=0;i<size;++i) {
                if (selectorItemView.curSelectedItems[i] != null) {
                    if (i == size - 1) {
                        selectorItemView.curSelectedItems[0].isSelected = isAdd;
                    } else {
                        if (isAdd) {
                            selectorItemView.curSelectedItems[i].selectedNum++;
                        } else {
                            selectorItemView.curSelectedItems[i].selectedNum--;
                        }
                    }
                }
            }
        }


    }

    public void addSelectedItem(SelectorEntity selectorEntity) {
        SelectorEntity[] selectorEntities = new SelectorEntity[selectorItemView.getLevelCount()];
        selectorEntities[0] = selectorEntity;
        int size = selectorItemView.curSelectedItems.length;
        if (size == 2) {
            selectorEntities[1] = selectorItemView.curSelectedItems[0];
        }
        if (size == 3) {
            selectorEntities[2] = selectorItemView.curSelectedItems[0];
            selectorEntities[1] = selectorItemView.curSelectedItems[1];
        }
        selectorItemView.selectedItems.add(selectorEntities);
    }

    // 单选的时候 先移出上一个
    private void removePre() {
        int size = selectorItemView.selectedItems.get(0).length;
        if (size > 0) {
            for (int i=0;i<size;++i) {
                if (selectorItemView.selectedItems.get(0)[i] != null) {
                    if (i == 0) {
                        selectorItemView.selectedItems.get(0)[i].isSelected = false;
                    } else {
                        selectorItemView.selectedItems.get(0)[i].selectedNum--;
                    }
                }
            }
        }
        selectorItemView.selectedItems.remove(0);
    }

    // 选择不限
    private void selectedAll() {
        int size = selectorItemView.curSelectedItems.length;
        if (size == 2) {
            selectorItemView.curSelectedItems[0].selectedNum = 0;
        }
        if (size == 3) {
            selectorItemView.curSelectedItems[0].selectedNum -= selectorItemView.curSelectedItems[1].selectedNum;
            selectorItemView.curSelectedItems[1].selectedNum = 0;
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.btnExpand) {
            int visible = myListView.getVisibility();
            if (visible == View.VISIBLE) {
                imgExpand.setImageResource(R.drawable.icon_expand_up);
                myListView.startAnimation(mHiddenAction);
                handler.postDelayed(myListViewGone, 200);
            } else {
                imgExpand.setImageResource(R.drawable.icon_expand_down);
                myListView.startAnimation(mShowAction);
                myListView.setVisibility(View.VISIBLE);
            }
        } else if (v.getId() == R.id.btn_right) {
            sure();
        } else if (v.getId() == R.id.imgExpand) {
            SelectorEntity[] selectorEntitys = selectorItemView.selectedItems.get(0);
            selectorItemView.removeSelectedItem(selectorEntitys[0]);
            refreshUI();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        refreshUI();
    }

    private void refreshUI()
    {
        selectedAdapter.clear();
        selectedAdapter.appendToList(selectorItemView.selectedItems);
        initTopSelectedText();
        selectorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void back()
    {
        if (selectorItemView.selectorEntityStack.size() == 1) {
            sure();
        } else {
            selectorItemView.selectorEntityStack.pop();
            super.back();
        }
    }

    /**
     * 确定选择
     */
    private void sure()
    {
        selectorItemView.getSelectorName();
        while (selectorItemView.selectorEntityStack.size() != 1) {
            selectorItemView.selectorEntityStack.pop();
        }
        if (selectorItemView.selectedItems != null) {
            ScreenManager.getScreenManager().popActivityByClass(this.getClass(), true);
        } else {
            ScreenManager.getScreenManager().popActivityByClass(this.getClass(), false);
        }
    }

    private void initAnim()
    {
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(200);

        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f);
        mHiddenAction.setDuration(200);
    }

    private Runnable myListViewGone = new Runnable()
    {
        @Override
        public void run()
        {
            myListView.setVisibility(View.GONE);
        }
    };

    private void initTopSelectedText()
    {
        if (selectorItemView.singleSelected) {
            if (selectorItemView.selectedItems != null && selectorItemView.selectedItems.size() > 0) {
                tvSelected.setText(selectorItemView.selectedItems.get(0)[0].getAllName());
                imgExpand.setImageResource(R.drawable.selector_delete);
                imgExpand.setOnClickListener(this);
            } else {
                tvSelected.setText("未选择");
                imgExpand.setVisibility(View.GONE);
            }
        } else {
            if (selectorItemView.selectedItems != null) {
                tvSelected.setText(selectorItemView.selectedItems.size() + "/" + selectorItemView.maxSelected);
            } else {
                tvSelected.setText("0/" + selectorItemView.maxSelected);
            }
        }
    }
}