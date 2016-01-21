package cn.goodjobs.common.activity.resume;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.goodjobs.common.R;
import cn.goodjobs.common.adapter.ResumePreviewAdapter;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.entity.ResumePreviewInfo;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.view.LoadingDialog;

/**
 * 简历预览
 * */

public class MyResumePreviewActivity extends BaseActivity {

    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_my_resume_preview;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("简历预览");

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        LoadingDialog.showDialog(this);
        HttpUtil.post(URLS.API_CV_SHOW, this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        setDataToView((JSONObject) data);
    }

    private void setDataToView(JSONObject jsonObject) {
        List<ResumePreviewInfo> mapList = new ArrayList<ResumePreviewInfo>();
        ResumePreviewInfo groupInfo = null;
        ResumePreviewInfo childInfo = null;

        // 基本信息
        groupInfo = new ResumePreviewInfo();
        groupInfo.title = "基本信息";
        groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

        addChildInfo(groupInfo.resumePreviewInfoList, "姓        名：", jsonObject.optString("realName"));
        addChildInfo(groupInfo.resumePreviewInfoList, "性        别：", jsonObject.optString("sex"));
        addChildInfo(groupInfo.resumePreviewInfoList, "民        族：", jsonObject.optString("nation"));
        addChildInfo(groupInfo.resumePreviewInfoList, "出生年月：", jsonObject.optString("birthday"));
        addChildInfo(groupInfo.resumePreviewInfoList, "户        口：", jsonObject.optString("nregion"));
        addChildInfo(groupInfo.resumePreviewInfoList, "居  住  地：", jsonObject.optString("district"));
        addChildInfo(groupInfo.resumePreviewInfoList, "身  份  证：", jsonObject.optString("cardNumber"));
        addChildInfo(groupInfo.resumePreviewInfoList, "婚姻状况：", jsonObject.optString("marriage"));
        addChildInfo(groupInfo.resumePreviewInfoList, "政治面貌：", jsonObject.optString("politicalStatus"));
        mapList.add(groupInfo);

        // 基本信息
        groupInfo = new ResumePreviewInfo();
        groupInfo.title = "个人概况";
        groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

        addChildInfo(groupInfo.resumePreviewInfoList, "身        高：", jsonObject.optString("stature"), "厘米");
        addChildInfo(groupInfo.resumePreviewInfoList, "体        重：", jsonObject.optString("weight"), "公斤");
        addChildInfo(groupInfo.resumePreviewInfoList, "视力状况：", jsonObject.optString("eyesight"));
        addChildInfo(groupInfo.resumePreviewInfoList, "工作年限：", jsonObject.optString("workTime"));
        addChildInfo(groupInfo.resumePreviewInfoList, "目前单位：", jsonObject.optString("workPlace"));
        addChildInfo(groupInfo.resumePreviewInfoList, "行        业：", jsonObject.optString("workIndustry"));
        addChildInfo(groupInfo.resumePreviewInfoList, "目前职位：", jsonObject.optString("jobName"));
        addChildInfo(groupInfo.resumePreviewInfoList, "职        称：", jsonObject.optString("techPost"));
        addChildInfo(groupInfo.resumePreviewInfoList, "目前月薪：", jsonObject.optString("currentSalary"));
        addChildInfo(groupInfo.resumePreviewInfoList, "毕业院校：", jsonObject.optString("school"));
        addChildInfo(groupInfo.resumePreviewInfoList, "毕业年份：", jsonObject.optString("graduateTime"));
        addChildInfo(groupInfo.resumePreviewInfoList, "最终学历：", jsonObject.optString("degree"));
        addChildInfo(groupInfo.resumePreviewInfoList, "专业类别：", jsonObject.optString("majorCategory"));
        addChildInfo(groupInfo.resumePreviewInfoList, "专业名称：", jsonObject.optString("major"));
        mapList.add(groupInfo);

        // 联系方式
        groupInfo = new ResumePreviewInfo();
        groupInfo.title = "联系方式";
        groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

        addChildInfo(groupInfo.resumePreviewInfoList, "手        机：", jsonObject.optString("mobile"));
        addChildInfo(groupInfo.resumePreviewInfoList, "电子邮箱：", jsonObject.optString("email"));
        mapList.add(groupInfo);

        // 求职意向
        groupInfo = new ResumePreviewInfo();
        groupInfo.title = "求职意向";
        groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

        addChildInfo(groupInfo.resumePreviewInfoList, "求职性质：", jsonObject.optString("wishJobType"));
        addChildInfo(groupInfo.resumePreviewInfoList, "薪金要求：", jsonObject.optString("wishSalary"));
        addChildInfo(groupInfo.resumePreviewInfoList, "行业要求：", jsonObject.optString("wishIndustry"));
        addChildInfo(groupInfo.resumePreviewInfoList, "职能职位：", jsonObject.optString("wishPosition"));
        addChildInfo(groupInfo.resumePreviewInfoList, "公司性质：", jsonObject.optString("wishCompany"));
        addChildInfo(groupInfo.resumePreviewInfoList, "工作地点：", jsonObject.optString("wishPlace"));
        addChildInfo(groupInfo.resumePreviewInfoList, "提供住房：", jsonObject.optString("wishHouse"));
        addChildInfo(groupInfo.resumePreviewInfoList, "离职时间：", jsonObject.optString("wishTime"));
        mapList.add(groupInfo);

        // 工作经历
        if (jsonObject.optJSONArray("workexplist") != null && jsonObject.optJSONArray("workexplist").length() > 0) {
            groupInfo = new ResumePreviewInfo();
            groupInfo.title = "工作经历";
            groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

            JSONArray workList = jsonObject.optJSONArray("workexplist");
            int len = workList.length();
            for (int i=0; i<len ; ++i) {
                if (groupInfo.resumePreviewInfoList.size() > 0) {
                    childInfo = new ResumePreviewInfo();
                    childInfo.line = new View(this);
                    AbsListView.LayoutParams lineParams =
                            new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 0.5f));
                    childInfo.line.setLayoutParams(lineParams);
                    childInfo.line.setBackgroundColor(getResources().getColor(R.color.line_color));
                    groupInfo.resumePreviewInfoList.add(childInfo);
                }
                JSONObject workObj = workList.optJSONObject(i);
                addChildInfo(groupInfo.resumePreviewInfoList, "时        间：", workObj.optString("workexpdate"));
                addChildInfo(groupInfo.resumePreviewInfoList, "所在公司：", workObj.optString("company"));
                addChildInfo(groupInfo.resumePreviewInfoList, "所属行业：", workObj.optString("industry"));
                addChildInfo(groupInfo.resumePreviewInfoList, "公司性质：", workObj.optString("companyType"));
                addChildInfo(groupInfo.resumePreviewInfoList, "所在部门：", workObj.optString("department"));
                addChildInfo(groupInfo.resumePreviewInfoList, "职能职位：", workObj.optString("jobName"));
                addChildInfo(groupInfo.resumePreviewInfoList, "工作描述：", workObj.optString("jobDescription"));
            }
            mapList.add(groupInfo);
        }

        // 教育经历
        if (jsonObject.optJSONArray("edulist") != null && jsonObject.optJSONArray("edulist").length() > 0) {
            groupInfo = new ResumePreviewInfo();
            groupInfo.title = "教育经历";
            groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

            JSONArray workList = jsonObject.optJSONArray("edulist");
            int len = workList.length();
            for (int i=0; i<len ; ++i) {
                if (groupInfo.resumePreviewInfoList.size() > 0) {
                    childInfo = new ResumePreviewInfo();
                    childInfo.line = new View(this);
                    AbsListView.LayoutParams lineParams =
                            new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 0.5f));
                    childInfo.line.setLayoutParams(lineParams);
                    childInfo.line.setBackgroundColor(getResources().getColor(R.color.line_color));
                    groupInfo.resumePreviewInfoList.add(childInfo);
                }
                JSONObject workObj = workList.optJSONObject(i);
                addChildInfo(groupInfo.resumePreviewInfoList, "时        间：", workObj.optString("edudate"));
                addChildInfo(groupInfo.resumePreviewInfoList, "学校名称：", workObj.optString("school"));
                addChildInfo(groupInfo.resumePreviewInfoList, "学        历：", workObj.optString("degree"));
            }
            mapList.add(groupInfo);
        }

        // 培训经历
        if (jsonObject.optJSONArray("trainlist") != null && jsonObject.optJSONArray("trainlist").length() > 0) {
            groupInfo = new ResumePreviewInfo();
            groupInfo.title = "培训经历";
            groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

            JSONArray workList = jsonObject.optJSONArray("trainlist");
            int len = workList.length();
            for (int i=0; i<len ; ++i) {
                if (groupInfo.resumePreviewInfoList.size() > 0) {
                    childInfo = new ResumePreviewInfo();
                    childInfo.line = new View(this);
                    AbsListView.LayoutParams lineParams =
                            new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 0.5f));
                    childInfo.line.setLayoutParams(lineParams);
                    childInfo.line.setBackgroundColor(getResources().getColor(R.color.line_color));
                    groupInfo.resumePreviewInfoList.add(childInfo);
                }
                JSONObject workObj = workList.optJSONObject(i);
                addChildInfo(groupInfo.resumePreviewInfoList, "时        间：", workObj.optString("traindate"));
                addChildInfo(groupInfo.resumePreviewInfoList, "培训单位：", workObj.optString("tdepartment"));
                addChildInfo(groupInfo.resumePreviewInfoList, "培训课程：", workObj.optString("tcourse"));
                addChildInfo(groupInfo.resumePreviewInfoList, "证书名称：", workObj.optString("tcert"));
            }
            mapList.add(groupInfo);
        }

        // 语言能力
        if (jsonObject.optJSONArray("langlist") != null && jsonObject.optJSONArray("langlist").length() > 0) {
            groupInfo = new ResumePreviewInfo();
            groupInfo.title = "语言能力";
            groupInfo.resumePreviewInfoList = new ArrayList<ResumePreviewInfo>();

            JSONArray workList = jsonObject.optJSONArray("langlist");
            int len = workList.length();
            for (int i=0; i<len ; ++i) {
                if (groupInfo.resumePreviewInfoList.size() > 0) {
                    childInfo = new ResumePreviewInfo();
                    childInfo.line = new View(this);
                    AbsListView.LayoutParams lineParams =
                            new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 0.5f));
                    childInfo.line.setLayoutParams(lineParams);
                    childInfo.line.setBackgroundColor(getResources().getColor(R.color.line_color));
                    groupInfo.resumePreviewInfoList.add(childInfo);
                }
                JSONObject workObj = workList.optJSONObject(i);
                addChildInfo(groupInfo.resumePreviewInfoList, workObj.optString("language") + "：", workObj.optString("level"));
            }
            mapList.add(groupInfo);
        }

        ResumePreviewAdapter resumePreviewAdapter = new ResumePreviewAdapter(this);
        resumePreviewAdapter.mapList = mapList;
        expandableListView.setAdapter(resumePreviewAdapter);

        for(int i = 0; i < resumePreviewAdapter.getGroupCount(); i++){
            expandableListView.expandGroup(i);
        }
    }

    private void addChildInfo(List<ResumePreviewInfo> resumePreviewInfoList, String title, String content) {
        if (!StringUtil.isEmpty(content)) {
            ResumePreviewInfo childInfo = new ResumePreviewInfo();
            childInfo.title = title;
            childInfo.content = content;
            resumePreviewInfoList.add(childInfo);
        }
    }

    private void addChildInfo(List<ResumePreviewInfo> resumePreviewInfoList, String title, String content, String dw) {
        if (!StringUtil.isEmpty(content)) {
            ResumePreviewInfo childInfo = new ResumePreviewInfo();
            childInfo.title = title;
            childInfo.content = content + dw;
            resumePreviewInfoList.add(childInfo);
        }
    }
}
