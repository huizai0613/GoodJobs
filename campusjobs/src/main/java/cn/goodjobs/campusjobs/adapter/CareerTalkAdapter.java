package cn.goodjobs.campusjobs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.goodjobs.campusjobs.R;
import cn.goodjobs.campusjobs.activity.CareerTalkDetailsActivity;
import cn.goodjobs.common.baseclass.JsonArrayAdapterBase;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

/**
 * Created by zhuli on 2015/12/29.
 */
public class CareerTalkAdapter extends JsonArrayAdapterBase<JSONObject> {

    private List<Integer> data = new ArrayList<Integer>();

    public CareerTalkAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getExView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_jobfair, null);
            holder.corpName = (TextView) convertView.findViewById(R.id.tv_corpName);
            holder.pubDate = (TextView) convertView.findViewById(R.id.tv_pubDate);
            holder.schoolAddress = (TextView) convertView.findViewById(R.id.tv_schoolAddress);
            holder.originName = (TextView) convertView.findViewById(R.id.tv_originName);
            holder.fairWeek = (TextView) convertView.findViewById(R.id.tv_fairWeek);
            holder.fairNum = (TextView) convertView.findViewById(R.id.tv_fairNum);
            holder.fairClick = (TextView) convertView.findViewById(R.id.tv_fairClick);
            holder.followHas = (TextView) convertView.findViewById(R.id.tv_followHas);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final JSONObject jsonObject = getItem(position);

        holder.corpName.setText(jsonObject.optString("corpName"));
        holder.pubDate.setText(jsonObject.optString("pubDate"));
        holder.schoolAddress.setText(jsonObject.optString("schoolAddress"));
        holder.originName.setText(jsonObject.optString("originName"));
        holder.fairWeek.setText(jsonObject.optString("fairWeek"));
        holder.fairNum.setText("已有" + jsonObject.optString("fairNum") + "人关注");
        holder.fairClick.setText(jsonObject.optString("fairClick"));
        if (jsonObject.optString("followHas").equals("2")) {
            holder.followHas.setBackgroundColor(context.getResources().getColor(R.color.bg_color));
        } else if (jsonObject.optString("followHas").equals("0")) {
            holder.followHas.setBackgroundColor(context.getResources().getColor(R.color.yellow));
        }

        holder.followHas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("fairID", jsonObject.optInt("fairID"));
                HttpUtil.post(URLS.API_JOB_Jobfairfollow, params, new HttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String tag) {

                    }

                    @Override
                    public void onSuccess(String tag, Object data) {
                        int fairNum = jsonObject.optInt("fairNum");
                        if (jsonObject.optString("followHas").equals("2")) {
                            try {
                                jsonObject.put("followHas", "0");
                                jsonObject.put("fairNum", --fairNum);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (jsonObject.optString("followHas").equals("0")) {
                            try {
                                jsonObject.put("followHas", "2");
                                jsonObject.put("fairNum", ++fairNum);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        notifyDataSetChanged();
                        TipsUtil.show(context, ((JSONObject) data).optString("message"));

                    }

                    @Override
                    public void onError(int errorCode, String tag, String errorMessage) {

                    }

                    @Override
                    public void onProgress(String tag, int progress) {

                    }
                });
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                for (int i = 0; i < mList.size(); i++) {
                    data.add(mList.get(i).optInt("fairID"));
                }
                Intent intent = new Intent(context, CareerTalkDetailsActivity.class);
                intent.putExtra("item", position);
                intent.putIntegerArrayListExtra("idList", (ArrayList<Integer>) data);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView corpName, pubDate, schoolAddress, originName, fairWeek, fairNum, fairClick, followHas;
    }
}

