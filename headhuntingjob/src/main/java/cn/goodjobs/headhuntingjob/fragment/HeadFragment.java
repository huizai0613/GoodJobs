package cn.goodjobs.headhuntingjob.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.goodjobs.common.baseclass.BaseListFragment;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cn.goodjobs.headhuntingjob.R;
import cn.goodjobs.headhuntingjob.adapter.HeadFragmentAdapter;

/**
 * Created by zhuli on 2015/12/24.
 */
public class HeadFragment extends BaseListFragment implements SpinnerDialog.SpinnerChooseListener {

    private Button btnChoice;
    private SpinnerDialog dialog;
    private ArrayList<String> list;
    private boolean isSuccess;
    private EmptyLayout emptyLayout;
    private View mView;
    private int type;    //0是猎头，1是悬赏，2是代理招聘

    public HeadFragment() {
    }

    public HeadFragment(int type) {
        this.type = type;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_head, container, false);
        mView = view;
        initView(view);
        mAdapter = new HeadFragmentAdapter(getActivity(), type);
        initList(view);
        if (type == 0) {
            startRefresh();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.info("HeadFragment----setUserVisibleHint:" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isSuccess && type != 0) {
                startRefresh();
            }
        }
    }

    private void initView(View view) {
        btnChoice = (Button) view.findViewById(R.id.btnChoice);
        emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
        if (type == 0) {
            btnChoice.setVisibility(View.VISIBLE);
            dialog = new SpinnerDialog(1);
            dialog.strs = new String[]{"全部行业", "IT电子/网络", "建筑/房产开发", "汽车零部件", "制造业", "商业零售", "快速消费品", "其他行业"};
            dialog.chooseIndex = 0;
            dialog.title = "行业筛选";
            dialog.listener = this;
        }
        btnChoice.setOnClickListener(this);


    }

    @Override
    protected void getDataFronServer() {
        super.getDataFronServer();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        if (type == 0) {
            HttpUtil.post(URLS.API_JOB_HeadHunt, params, this);
        } else if (type == 1) {
            HttpUtil.post(URLS.API_JOB_BonusList, params, this);
        } else if (type == 2) {
            HttpUtil.post(URLS.API_JOB_AgentList, params, this);
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        isSuccess = true;
        JSONObject object = (JSONObject) data;
        mAdapter.appendToList(object.optJSONArray("list"));
        if (mAdapter.getCount() == 0) {
            emptyLayout.setErrorType(EmptyLayout.NODATA);
        }

        loadMoreListViewContainer.loadMoreFinish(false, object.optInt("maxPage") > page);
        mPtrFrameLayout.refreshComplete();
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        super.onFailure(statusCode, tag);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        super.onError(errorCode, tag, errorMessage);
        emptyLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnChoice) {
            dialog.showSpinner(getActivity());
        }
    }


    @Override
    public void choose(int position, int id) {
        TipsUtil.show(getActivity(), "选择为：" + dialog.strs[position]);
    }
}
