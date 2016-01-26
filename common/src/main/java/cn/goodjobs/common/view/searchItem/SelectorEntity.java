package cn.goodjobs.common.view.searchItem;

import java.util.List;

import cn.goodjobs.common.util.StringUtil;

/**
 * Created by 王刚 on 2015/12/18 0018.
 * 选择器实体类
 */
public class SelectorEntity {
    public String id;
    public String name;
    public String parentName; // 父节点名称
    public String parentId; // 父节点名称
    public boolean isSelected; // 是否被选中
    public List<SelectorEntity> array; // 下级列表

    public SelectorEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SelectorEntity(String id, String name, String parentId, String parentName) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.parentName = parentName;
    }

    public String getAllName() {
        // 全名
        if (StringUtil.isEmpty(parentName)) {
            return name;
        } else {
            return "[" + parentName + "] " + name;
        }
    }
}
