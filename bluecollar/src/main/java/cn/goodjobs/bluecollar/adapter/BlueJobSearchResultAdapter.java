package cn.goodjobs.bluecollar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.activity.BlueJobSearchResultActivity;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;

/**
 * Created by yexiangyu on 15/12/28.
 */
public class BlueJobSearchResultAdapter extends JsonArrayAdapterBase<JSONObject>
{
    private BlueJobSearchResultActivity jobSearchResultActivity;

    public BlueJobSearchResultAdapter(Context context)
    {
        super(context);
    }

    public void setJobSearchResultActivity(BlueJobSearchResultActivity blueJobSearchResultActivity)
    {
        this.jobSearchResultActivity = jobSearchResultActivity;
    }

    ArrayList<Integer> checkPosition = new ArrayList<>();


    public ArrayList<Integer> getCheckPosition()
    {
        return checkPosition;
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bluejob, null);
        }
//        TextView title = ViewHolderUtil.get(convertView, R.id.item_title);
//        TextView address = ViewHolderUtil.get(convertView, R.id.item_address);
//        TextView name = ViewHolderUtil.get(convertView, R.id.item_name);
//        BabushkaText salary = ViewHolderUtil.get(convertView, R.id.item_salary);
//        TextView time = ViewHolderUtil.get(convertView, R.id.item_time);
//        final CheckBox check = ViewHolderUtil.get(convertView, R.id.item_check);
//
//        final JSONObject item = getItem(position);
//
//        title.setText(item.optString("jobName"));
//        address.setText(item.optString("jobCity"));
//        name.setText(item.optString("corpName"));
//        salary.setText(item.optString("jobName"));
//        time.setText(item.optString("pubDate"));
//        salary.reset();
//        salary.addPiece(new BabushkaText.Piece.Builder("月薪: ")
//                .textColor(Color.parseColor("#999999"))
//                .build());
//
//        // Add the second piece "1.2 mi"
//        salary.addPiece(new BabushkaText.Piece.Builder(item.optString("salary"))
//                .textColor(Color.parseColor("#ff0000"))
//                .textSizeRelative(1.0f)
//                .build());
//
//        salary.display();
//
//        check.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                if (checkPosition.contains((Integer) position)) {
//                    checkPosition.remove((Integer) position);
//                    check.setSelected(false);
//                } else {
//                    check.setSelected(true);
//                    checkPosition.add((Integer) position);
//                }
////                jobSearchResultActivity.setBottomVisible(checkPosition.size() > 0);
//            }
//        });
//
//        convertView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//
//                HashMap<String, Object> param = new HashMap<>();
//                param.put("POSITION", position);
//                StringBuilder builder = new StringBuilder();
//                for (int i = 0; i < mList.size(); i++) {
//                    builder.append(mList.get(i).optInt("jobID") + ",");
//                }
//                String charSequence = builder.subSequence(0, builder.length() - 1).toString();
//                param.put("IDS", charSequence);
////                JumpViewUtil.openActivityAndParam(context, JobDetailActivity.class, param);
//            }
//        });

        return convertView;
    }


}
