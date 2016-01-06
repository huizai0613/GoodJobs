package cn.goodjobs.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by 王刚 on 2016/1/5 0005.
 */
public class PersonalListAdapter extends JsonArrayAdapterBase<JSONObject> {

    public int adapterRes;
    public int[] resIDs;
    public String[] keys;
    private HashMap<String, String> strMap; // 状态转换文字
    public TextStatus textStatus;
    public String idKey; // id对应的key
    public boolean hasCheck = true; // 是否包含选择框

    public PersonalListAdapter(Context context) {
        super(context);
    }

    public void setStrMap(HashMap<String, String> strMap) {
        this.strMap = strMap;
    }

    @Override
    protected View getExView(int position, View convertView, ViewGroup parent) {
        ViewHorder viewHorder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(adapterRes, null);
            viewHorder = new ViewHorder();
            viewHorder.textViews = new TextView[resIDs.length];
            for (int i=0;i<resIDs.length;i++) {
                viewHorder.textViews[i] = (TextView) convertView.findViewById(resIDs[i]);
            }
            if (hasCheck) {
                viewHorder.btnCheck = (RelativeLayout) convertView.findViewById(R.id.btnCheck);
            }
            convertView.setTag(viewHorder);
        } else {
            viewHorder = (ViewHorder) convertView.getTag();
        }
        final JSONObject jsonObject = getItem(position);
        for (int i=0;i<resIDs.length;++i) {
            if (keys[i].startsWith("@")) {
                // 需要再转义一次
                viewHorder.textViews[i].setText(strMap.get(jsonObject.optString(keys[i].replaceAll("@", ""))));
            } else {
                viewHorder.textViews[i].setText(jsonObject.optString(keys[i]));
            }
        }
        if (textStatus != null) {
            for (int i=0;i<textStatus.key.length;++i) {
                viewHorder.textViews[textStatus.index[i]].setSelected(textStatus.selectedStatus[i].equals(jsonObject.optString(textStatus.key[i])));
            }
        }
        if (hasCheck) {
            viewHorder.btnCheck.setSelected(jsonObject.optBoolean("isCheck"));
            viewHorder.btnCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        jsonObject.put("isCheck", !jsonObject.optBoolean("isCheck"));
                        notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return convertView;
    }

    static class ViewHorder {
        TextView[] textViews;
        RelativeLayout btnCheck;
    }

    public static class TextStatus {
        String[] key;
        int[] index;
        String[] selectedStatus;

        public TextStatus(String[] key, int[] index, String[] selectedStatus) {
            this.key = key;
            this.index = index;
            this.selectedStatus = selectedStatus;
        }
    }

    public String getSelectedIDs() {
        StringBuilder sb = new StringBuilder();
        for (JSONObject jsonObj:mList) {
            if (jsonObj.optBoolean("isCheck")) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(jsonObj.optString(idKey));
            }
        }
        return sb.toString();
    }
}
