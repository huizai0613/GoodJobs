package cn.goodjobs.applyjobs.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.goodjobs.applyjobs.R;
import cn.goodjobs.applyjobs.adapter.DividerItemDecoration;
import cn.goodjobs.applyjobs.adapter.TrainClassAdapter;
import cn.goodjobs.common.baseclass.BaseFragment;
import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.MyRecycler.TrainDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.common.view.refresh.PullRefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;

/**
 * Created by zhuli on 2015/12/17.
 */
public class TrainClassFragment extends BaseListFragment implements View.OnClickListener {

    private EmptyLayout emptyLayout;
    private boolean isSuccess = false;
    private Button btn_order, btn_message;

    public TrainClassFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainclass, container, false);
        initView(view);
        mAdapter = new TrainClassAdapter(getActivity());
        initList(view);
        return view;
    }

    private void initView(View view) {
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        btn_order = (Button) view.findViewById(R.id.btn_order);
        btn_message = (Button) view.findViewById(R.id.btn_message);
        btn_order.setOnClickListener(this);
        btn_message.setOnClickListener(this);
    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        HttpUtil.post(URLS.API_JOB_Learning, params, this);
    }

    // 当fragment可见时调用
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("JobSearchFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isSuccess) {
                startRefresh();
            }
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.API_JOB_Learning)) {
            isSuccess = true;
            JSONObject object = (JSONObject) data;
            mAdapter.appendToList(object.optJSONArray("list"));
            if (mAdapter.getCount() == 0) {
                emptyLayout.setErrorType(EmptyLayout.NODATA);
            }

            loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage")>page);
            mPtrFrameLayout.refreshComplete();
        } else if (tag.equals(URLS.API_JOB_consult)) {
            Map<String, String> phone = new HashMap<String, String>();
            JSONObject jsonObject = (JSONObject) data;
            Iterator<String> i = jsonObject.keys();
            while (i.hasNext()) {
                String key = i.next();
                String value = jsonObject.optString(key);
                phone.put(key, value);
            }
            showConsultDialog(phone);
        } else if (tag.equals(URLS.API_JOB_appoint)) {
            String appoint = (String) data;
            TipsUtil.show(getActivity(), appoint);
        }
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_order) {
            showAlertDialog();
        }
        if (id == R.id.btn_message) {
            HttpUtil.post(URLS.API_JOB_consult, this);
            LoadingDialog.showDialog(getActivity());
        }
    }


    //显示预约的dialog
    public void showAlertDialog() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);

        final TrainDialog.Builder builder = new TrainDialog.Builder(getActivity());
        builder.setMessage("");
        builder.setTitle("预约单");
        builder.setPositiveButton("预约", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                LoadingDialog.showDialog(getActivity());
                Map<String, Object> params = builder.getParams();
                HttpUtil.post(URLS.API_JOB_appoint, params, TrainClassFragment.this);
                LoadingDialog.showDialog(getActivity());
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("暂时不", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    //显示咨询的dialog
    private void showConsultDialog(Map<String, String> phone) {
        CharSequence[] items = new CharSequence[phone.size()];
        final CharSequence[] items1 = new CharSequence[phone.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : phone.entrySet()) {
            String name = entry.getKey();
            String tel = entry.getValue();
            items[i] = tel + "(" + name + ")";
            items1[i] = tel;
            i++;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("拨打电话");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + items1[item]));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
