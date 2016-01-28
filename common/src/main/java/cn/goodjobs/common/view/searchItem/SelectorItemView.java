package cn.goodjobs.common.view.searchItem;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.goodjobs.common.R;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;

/**
 * Created by 王刚 on 2015/12/17 0017.
 * 公共数据选择
 */
public class SelectorItemView extends SearchItemView implements View.OnClickListener
{

    public String sourceStr; // 数据源，多个数据源以|分隔
    public String selectorIds; //选中ids
    private String sID;
    private String spID;
    public boolean containAll; // 下级列表是否包含全部
    public boolean singleSelected; // 是否单选
    public int maxSelected = 5; // 默认最多选择5项
    private String[] keys;

    public Stack<List<SelectorEntity>> selectorEntityStack; // 保存要显示的数据
    public List<SelectorEntity[]> selectedItems; // 已经选中的数据
    public SelectorEntity[] curSelectedItems; //当前正在选择列表
    public static String spitStr = ";";
    public static String parentSpitStr = "#";
    public boolean isInit; //是否初始化
    public static String allId = "-1"; // 默认不限的id

    public SelectorItemView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initView(attrs);
        this.setOnClickListener(this);
    }

    public SelectorItemView(Context context)
    {
        super(context);
        this.setOnClickListener(this);
    }

    private void initView(AttributeSet attrs)
    {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.listitem, 0, 0);
        sourceStr = a.getString(R.styleable.listitem_source);
        keys = sourceStr.split(";");
        selectorIds = a.getString(R.styleable.listitem_selectorIds);
        containAll = a.getBoolean(R.styleable.listitem_containAll, false);
        singleSelected = a.getBoolean(R.styleable.listitem_singleSelected, true);
        maxSelected = a.getInt(R.styleable.listitem_maxSelected, 5);
        if (!StringUtil.isEmpty(selectorIds)) {
            if (!selectorIds.startsWith(spitStr)) {
                selectorIds = spitStr + selectorIds;
            }
            if (!selectorIds.endsWith(spitStr)) {
                selectorIds = selectorIds + spitStr;
            }
        } else {
            selectorIds = "";
        }
        a.recycle();
    }

    public int getLevelCount() {
        // 获取选择等级
        return keys.length;
    }

    @Override
    public void onClick(View v)
    {
        SelectorActivity.selectorItemView = this;
        Intent intent = new Intent(getContext(), SelectorActivity.class);
        getContext().startActivity(intent);
    }

    public void setKeys(String... keys)
    {
        this.keys = keys;
    }

    /**
     * 公共数据初始化
     */
    public void init()
    {
        if (!isInit) {
            selectorEntityStack = new Stack<List<SelectorEntity>>();
            selectedItems = new ArrayList<SelectorEntity[]>();
            curSelectedItems = new SelectorEntity[keys.length];
            JSONArray jsonArray = (JSONArray) JsonMetaUtil.getObject(keys[0]);
            selectorEntityStack.add(initWithJSONOArray(null, allId, "", jsonArray, 1));
            if (keys.length > 1 && !singleSelected) {
                checkSelectedData();
            }
            isInit = true;
        }
    }

    private List<SelectorEntity> initWithJSONOArray(SelectorEntity parantEntity, String parentId, String parantName, JSONArray jsonArray, int keyIndex)
    {
        JSONObject jsonObject = null;
        if (keyIndex < keys.length) {
            jsonObject = (JSONObject) JsonMetaUtil.getObject(keys[keyIndex]);
        }
        List<SelectorEntity> selectorEntityList = new ArrayList<SelectorEntity>();
        if (containAll && keyIndex == keys.length) {
            SelectorEntity selectorEntity2 = new SelectorEntity(parentId, "不限", parentId, parantName);
            if (selectorIds.contains(spitStr + parentId.replaceAll(allId + parentSpitStr, "") + spitStr)) {
                selectorEntity2.isSelected = true;
                SelectorEntity[] selectorEntitys = new SelectorEntity[keys.length];
                selectorEntitys[0] = selectorEntity2;
                selectedItems.add(selectorEntitys); // 不限被选中
            }
            selectorEntityList.add(selectorEntity2);
        }
        int len1 = jsonArray.length();
        int count = 0;
        for (int i = 0; i < len1; ++i) {
            JSONObject jsonObject1 = jsonArray.optJSONObject(i);
            String id1 = jsonObject1.optString("id");
            SelectorEntity selectorEntity = new SelectorEntity(id1, jsonObject1.optString("name"), parentId, parantName);
            if (jsonObject != null && jsonObject.has(id1)) {
                // 该列表包含下级选择项
                JSONArray jsonArray1 = jsonObject.optJSONArray(id1);
                String parantName1;
                if (!StringUtil.isEmpty(parantName)) {
                    parantName1 = parantName + " " + jsonObject1.optString("name");
                } else {
                    parantName1 = jsonObject1.optString("name");
                }
                selectorEntity.array = initWithJSONOArray(selectorEntity,allId + parentSpitStr + id1, parantName1, jsonArray1, keyIndex + 1);
                count += selectorEntity.selectedNum;
            } else {
                if (selectorIds.contains(spitStr + id1 + spitStr)) {
                    selectorEntity.isSelected = true;
                    SelectorEntity[] selectorEntitys = new SelectorEntity[keys.length];
                    selectorEntitys[0] = selectorEntity;
                    selectedItems.add(selectorEntitys);
                    count++;
                }
            }
            selectorEntityList.add(selectorEntity);
        }
        if (parantEntity != null && count > 0) {
            parantEntity.selectedNum = count;
            LogUtil.info("selectedItems.size()-1:=" + (selectedItems.size() - 1));
            LogUtil.info("keys.length - keyIndex:=" + (keys.length - keyIndex));
            selectedItems.get(selectedItems.size()-1)[keys.length - keyIndex + 1] = parantEntity;
        }
        return selectorEntityList;
    }

    // 检测已经选择的数据是否正确
    private void checkSelectedData() {
        int size = selectedItems.size();
        SelectorEntity[] selectorEntities = null;
        if (size > 0) {
            // 有被选中的项
            for (int i=size-1;i>=0;--i) {
                SelectorEntity[] temp = selectedItems.get(i);
                for (int j=0;j<keys.length;++j) {
                    if (temp[j] == null) {
                        temp[j] = selectorEntities[j];
                    }
                }
                selectorEntities = temp;
            }
        }
    }

    public void setSelectorIds(String selectorIds)
    {
        setTag(selectorIds);
        clear();
        if (!StringUtil.isEmpty(selectorIds)) {
            sID = selectorIds;
            if (!selectorIds.startsWith(spitStr)) {
                selectorIds = spitStr + selectorIds;
            }
            if (!selectorIds.endsWith(spitStr)) {
                selectorIds = selectorIds + spitStr;
            }
        } else {
            selectorIds = "";
            sID = "";
        }
        this.selectorIds = selectorIds;
    }

    /**
     * 清空选择
     */
    public void clear()
    {
        isInit = false;
        if (selectedItems == null || selectedItems.size() == 0) {
            return;
        }
        for (SelectorEntity[] selectorEntitys : selectedItems) {
            for (SelectorEntity selectorEntity : selectorEntitys) {
                selectorEntity.isSelected = false;
                selectorEntity.selectedNum = 0;
            }
        }
        selectedItems.clear();
        setText("");
    }

    public String getSelectorIds()
    {
        if (!isInit && !StringUtil.isEmpty(sID)) {
            return sID;
        }
        if (selectedItems == null || selectedItems.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (SelectorEntity[] selectorEntitys : selectedItems) {
            if (sb.length() > 0) {
                sb.append(spitStr);
            }
            if (selectorEntitys[0].id.startsWith(allId)) {
                sb.append(selectorEntitys[0].parentId);
            } else {
                sb.append(selectorEntitys[0].id);
            }
        }
        return sb.toString();
    }


    public void setSpID(String spID)
    {
        this.spID = spID;
    }

    public String getSelectorPraentIds()
    {
        if (!isInit && !StringUtil.isEmpty(spID)) {
            return spID;
        }
        if (selectedItems == null || selectedItems.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (SelectorEntity[] selectorEntitys: selectedItems) {
            if (sb.length() > 0) {
                sb.append(spitStr);
            }
            sb.append(selectorEntitys[0].parentId.split(parentSpitStr)[1]);
        }
        return sb.toString();
    }

    public String getSelectorName()
    {
        if (selectedItems == null || selectedItems.size() == 0) {
            setTag("");
            setText("");
            return "";
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder id = new StringBuilder();
        for (SelectorEntity[] selectorEntitys: selectedItems) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            if (id.length() > 0) {
                id.append(spitStr);
            }
            if (selectorEntitys[0].id.startsWith(allId)) {
                if (StringUtil.isEmpty(selectorEntitys[0].parentName)) {
                    sb.append(selectorEntitys[0].name);
                } else {
                    sb.append(selectorEntitys[0].parentName);
                }
            } else {
                sb.append(selectorEntitys[0].name);
            }
            id.append(selectorEntitys[0].id);
        }
        setTag(id.toString());
        setText(sb.toString());
        return sb.toString();
    }

    public void removeSelectedItem(SelectorEntity selectorEntity) {
        for (SelectorEntity[] selectorEntitys: selectedItems) {
            if (selectorEntitys[0] == selectorEntity) {
                int size = selectorEntitys.length;
                for (int i=0;i<size;++i) {
                    if (i == 0) {
                        selectorEntitys[0].isSelected = false;
                    } else {
                        selectorEntitys[i].selectedNum--;
                    }
                }
                selectedItems.remove(selectorEntitys);
                return;
            }
        }
    }
}
