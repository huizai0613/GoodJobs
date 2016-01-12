package cn.goodjobs.common.view.searchItem;

import org.json.JSONObject;

import cn.goodjobs.common.util.http.MetaDataUtil;

/**
 * Created by wanggang on 2015/10/18 0018.
 * 客户端原始json数据解析
 */
public class JsonMetaUtil
{

    public final static String JOBFUNCL1 = "jobfunc_l1"; // 工作性质（1级）
    public final static String JOBFUNCL2 = "jobfunc_l2"; // 工作性质（2级）
    public final static String JOBLOCPRO = "jobloc_pro"; // 省份列表
    public final static String JOBLOCCITY = "jobloc_city"; // 城市列表
    public final static String JOBLOCDISTRICT = "jobloc_district"; // 区列表
    public final static String SALARY = "salary_expected"; // 月薪
    public final static String WORKTIME = "worktime"; // 工作年限
    public final static String WELFARE = "welfare"; // 工作年限
    public final static String DEGREE = "degree"; // 学历
    public final static String SEX = "sex"; // 性别
    public final static String JOBTYPE = "jobtype"; // 工作性质
    public final static String POLITIC = "politic_status"; // 政治面貌
    public final static String MAJOR1 = "major_l1"; // 专业大类
    public final static String MAJOR2 = "major_l2"; // 专业小类
    public final static String CORPKIND = "corpkind"; // 公司性质
    public final static String CORPTYPE = "corptype"; // 公司类型
    public final static String INDTYPE = "indtype"; // 所属行业
    public final static String LANGUAGE = "language"; // 外语要求
    public final static String AGE = "language"; // 年龄

    public final static String JSONMATA_FILENAME = "jsonmata.txt";
    public final static String JSONCORPMATA = "jsoncorp";


    public final static String JOBREAD = "jobread"; // 阅读过的职位

    /**
     * 根据key获取到json对象
     */
    public static Object getObject(String key)
    {
        return MetaDataUtil.getInstanse().getMetaJson().opt(key);
    }

}
