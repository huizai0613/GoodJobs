package cn.goodjobs.campusjobs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.activity.CampusDetailsActivity;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.util.JumpViewUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.ViewHolderUtil;
import cn.goodjobs.common.view.BabushkaText;

/**
 * Created by zhuli on 2015/12/30.
 */
public class CampusAdapter extends JsonArrayAdapterBase<JSONObject> {

    public CampusAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_campus, null);
        }
        TextView title = ViewHolderUtil.get(convertView, R.id.item_title);
        TextView address = ViewHolderUtil.get(convertView, R.id.item_address);
        TextView name = ViewHolderUtil.get(convertView, R.id.item_name);
        BabushkaText salary = ViewHolderUtil.get(convertView, R.id.item_salary);
        TextView time = ViewHolderUtil.get(convertView, R.id.item_time);

        final JSONObject item = getItem(position);

        title.setText(item.optString("jobName"));
        address.setText(item.optString("jobCityName"));
        name.setText(item.optString("corpName"));
        salary.setText(item.optString("jobName"));
        time.setText(item.optString("pubDate"));
        salary.reset();
        salary.addPiece(new BabushkaText.Piece.Builder("月薪: ")
                .textColor(Color.parseColor("#999999"))
                .build());

        salary.addPiece(new BabushkaText.Piece.Builder(item.optString("salaryName"))
                .textColor(Color.parseColor("#ff0000"))
                .textSizeRelative(1.0f)
                .build());

        salary.display();


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> param = new HashMap<>();
                param.put("POSITION", position);
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mList.size(); i++) {
                    builder.append(mList.get(i).optInt("jobID") + ",");
                }
                String charSequence = builder.subSequence(0, builder.length() - 1).toString();
                param.put("IDS", charSequence);
                JumpViewUtil.openActivityAndParam(context, CampusDetailsActivity.class, param);
            }
        });

        return convertView;
    }

}