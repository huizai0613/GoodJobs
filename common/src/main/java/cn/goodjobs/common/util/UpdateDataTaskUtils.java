package cn.goodjobs.common.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.goodjobs.common.view.searchItem.JsonMetaUtil;

/**
 * Created by yexiangyu on 15/12/23.
 */
public class UpdateDataTaskUtils
{
    public static final int CITYDATA = 0;
    public static final int SALARYDATA = 1;
    public static final int MOREDATA = 2;
    public static final int COMPANYDATA = 3;
    public static final int CAMPUSMOREDATA = 4;
    public static final int JOBTYPEDATA = 5;


    public static final String SEARCHJOB = "searchjob";
    public static final String CAMPUSJOB = "campusJob";
    public static final String BLUEJOB = "blueJob";


    private static ExecutorService mBackgroundThreadPool;

    public static ExecutorService getBackgroundThreadPool()
    {

        if (mBackgroundThreadPool == null) {
            mBackgroundThreadPool = Executors.newFixedThreadPool(3);
        }

        return mBackgroundThreadPool;
    }


    /**
     * 获取职位分类和子分类
     *
     * @param context
     * @param listener
     */
    public static void selectJobFun(final Context context, final OnGetDiscussJobFunListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                LinkedHashMap<JSONObject, List<JSONObject>> linkedHashMap = new LinkedHashMap<JSONObject, List<JSONObject>>();

                JSONArray jobFun1Array = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.JOBFUNCL1);
                JSONObject jobFun2Object = (JSONObject) JsonMetaUtil.getObject(JsonMetaUtil.JOBFUNCL2);


                for (int i = 0; i < jobFun1Array.length(); i++) {
                    JSONObject jsonObject = jobFun1Array.optJSONObject(i);
                    ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();

                    JSONArray is = jobFun2Object.optJSONArray(jsonObject.optInt("id") + "");

                    for (int i1 = 0; i1 < is.length(); i1++) {
                        JSONObject jsonObject1 = is.optJSONObject(i1);
                        if (jsonObject1.optString("name").equals("不限") || jsonObject1.optString("name").equals("全部")) {
                            continue;
                        }
                        jsonObjects.add(jsonObject1);
                    }
                    JSONObject jb = new JSONObject();
                    try {
                        jb.put("name", "不限");
                        jb.put("id", 0);
                        jsonObjects.add(0, jb);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    linkedHashMap.put(jsonObject, jsonObjects);
                }

                if (listener != null) {
                    listener.onGetDiscussJobFunInfo(linkedHashMap);
                }

            }
        });

    }


    /**
     * 获取省中市资源
     *
     * @param context
     * @param pro
     * @param listener
     */
    public static void selectProInfo(final Context context, final String pro, final OnGetDiscussCityInfoListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
                                          {
                                              @Override
                                              public void run()
                                              {
                                                  JSONArray cityObject = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.JOBLOCPRO);
                                                  JSONObject object = (JSONObject) JsonMetaUtil.getObject(JsonMetaUtil.JOBLOCCITY);
                                                  int cityId = 0;
                                                  ArrayList<JSONObject> jsonObjects = new ArrayList<>();

                                                  JSONObject jsonObject = new JSONObject();
                                                  for (int i = 0; i < cityObject.length(); i++) {
                                                      String name = cityObject.optJSONObject(i).optString("name");
                                                      if (pro.equals(name)) {
                                                          cityId = cityObject.optJSONObject(i).optInt("id");
                                                          break;
                                                      }

                                                      if (cityId != 0)
                                                          break;
                                                  }
                                                  try {
                                                      JSONArray jsonArray = object.getJSONArray(cityId + "");
                                                      if (jsonArray != null) {
                                                          for (int i = 0; i < jsonArray.length(); i++) {
                                                              if (jsonArray.getJSONObject(i).getString("name").equals("不限") || jsonArray.getJSONObject(i).getString("name").equals("全部")) {
                                                                  continue;
                                                              }
                                                              jsonObjects.add(jsonArray.getJSONObject(i));
                                                          }
                                                      }
                                                      try {
                                                          jsonObject.put("name", "不限");
                                                          jsonObject.put("id", 0);
                                                          jsonObjects.add(0, jsonObject);
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }

                                                      if (listener != null)
                                                          listener.onGetDiscussCityInfo(jsonObjects, cityId);
                                                  } catch (
                                                          JSONException e
                                                          )

                                                  {
                                                      e.printStackTrace();
                                                      if (listener != null)
                                                          try {
                                                              JSONObject temp = new JSONObject();
                                                              temp.put("name", pro);
                                                              temp.put("id", cityId);
                                                              jsonObjects.add(0, temp);
                                                              listener.onGetDiscussCityInfo(jsonObjects, cityId);
                                                          } catch (JSONException e1) {
                                                              e1.printStackTrace();
                                                          }

                                                  }
                                              }
                                          }

        );

    }


    /**
     * 获取城市中区资源
     *
     * @param context
     * @param city
     * @param listener
     */
    public static void selectCityInfo(final Context context, final String city, final OnGetDiscussCityInfoListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                JSONObject cityObject = (JSONObject) JsonMetaUtil.getObject(JsonMetaUtil.JOBLOCCITY);
                JSONObject object = (JSONObject) JsonMetaUtil.getObject(JsonMetaUtil.JOBLOCDISTRICT);
                int cityId = 0;
                Iterator<String> keys = cityObject.keys();
                ArrayList<JSONObject> jsonObjects = new ArrayList<>();

                JSONObject jsonObject = new JSONObject();
                while (keys.hasNext()) {
                    String next = keys.next();
                    try {
                        JSONArray jsonArray = cityObject.getJSONArray(next);
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String name = jsonArray.getJSONObject(i).getString("name");
                                if (city.equals(name)) {
                                    cityId = jsonArray.getJSONObject(i).getInt("id");
                                    break;
                                }
                            }
                        }
                        if (cityId != 0)
                            break;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    JSONArray jsonArray = object.getJSONArray(cityId + "");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i).getString("name").equals("不限") || jsonArray.getJSONObject(i).getString("name").equals("全部")) {
                                continue;
                            }
                            jsonObjects.add(jsonArray.getJSONObject(i));
                        }
                    }
                    try {
                        jsonObject.put("name", "不限");
                        jsonObject.put("id", 0);
                        jsonObjects.add(0, jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (listener != null)
                        listener.onGetDiscussCityInfo(jsonObjects, cityId);
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (listener != null)
                        try {
                            JSONObject temp = new JSONObject();
                            temp.put("name", city);
                            temp.put("id", cityId);
                            jsonObjects.add(0, temp);
                            listener.onGetDiscussCityInfo(jsonObjects, cityId);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                }
            }
        });

    }


    public static void selectMoreInfo(final Context context, final OnGetDiscussMoreInfoListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    JSONArray workArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.WORKTIME);
                    JSONArray jobArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.JOBTYPE);
                    JSONArray corptypeArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.CORPKIND);
                    JSONArray degreeArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.DEGREE);
                    ArrayList<JSONObject> workObjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < workArray.length(); i++) {
                        if (workArray.optJSONObject(i).optString("name").equals("不限") || workArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        workObjects.add(workArray.optJSONObject(i));
                    }
                    JSONObject jsonWorkObject = new JSONObject();
                    jsonWorkObject.put("name", "不限");
                    jsonWorkObject.put("id", 0);
                    workObjects.add(0, jsonWorkObject);

                    ArrayList<JSONObject> corpkindbjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < jobArray.length(); i++) {
                        if (jobArray.optJSONObject(i).optString("name").equals("不限") || jobArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        corpkindbjects.add(jobArray.optJSONObject(i));
                    }
                    JSONObject jsonCroObject = new JSONObject();
                    jsonCroObject.put("name", "不限");
                    jsonCroObject.put("id", 0);
                    corpkindbjects.add(0, jsonCroObject);

                    ArrayList<JSONObject> corptypebjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < corptypeArray.length(); i++) {
                        if (corptypeArray.optJSONObject(i).optString("name").equals("不限") || corptypeArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        corptypebjects.add(corptypeArray.optJSONObject(i));
                    }
                    JSONObject jsoncorptypeObject = new JSONObject();
                    jsoncorptypeObject.put("name", "不限");
                    jsoncorptypeObject.put("id", 0);
                    corptypebjects.add(0, jsoncorptypeObject);

                    ArrayList<JSONObject> degreebjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < degreeArray.length(); i++) {
                        if (degreeArray.optJSONObject(i).optString("name").equals("不限") || degreeArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        degreebjects.add(degreeArray.optJSONObject(i));
                    }
                    JSONObject jsondegreeObject = new JSONObject();
                    jsondegreeObject.put("name", "不限");
                    jsondegreeObject.put("id", 0);
                    degreebjects.add(0, jsoncorptypeObject);


                    LinkedHashMap<String, List<JSONObject>> linkedHashMap = new LinkedHashMap();


                    linkedHashMap.put("学历要求", degreebjects);
                    linkedHashMap.put("工作经验", workObjects);
                    linkedHashMap.put("工作性质", corpkindbjects);
                    linkedHashMap.put("企业性质", corptypebjects);

                    if (listener != null)
                        listener.onGetDiscussMoreInfo(linkedHashMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void selecBluetMoreInfo(final Context context, final OnGetDiscussMoreInfoListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    JSONArray workArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.WELFARE);
                    JSONArray jobArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.INDTYPE);
                    ArrayList<JSONObject> workObjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < workArray.length(); i++) {
                        if (workArray.optJSONObject(i).optString("name").equals("不限") || workArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        workObjects.add(workArray.optJSONObject(i));
                    }
                    JSONObject jsonWorkObject = new JSONObject();
                    jsonWorkObject.put("name", "不限");
                    jsonWorkObject.put("id", 0);
                    workObjects.add(0, jsonWorkObject);

                    ArrayList<JSONObject> corpkindbjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < jobArray.length(); i++) {
                        if (jobArray.optJSONObject(i).optString("name").equals("不限") || jobArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        corpkindbjects.add(jobArray.optJSONObject(i));
                    }
                    JSONObject jsonCroObject = new JSONObject();
                    jsonCroObject.put("name", "不限");
                    jsonCroObject.put("id", 0);
                    corpkindbjects.add(0, jsonCroObject);


                    LinkedHashMap<String, List<JSONObject>> linkedHashMap = new LinkedHashMap();


                    linkedHashMap.put("福利", workObjects);
                    linkedHashMap.put("行业", corpkindbjects);

                    if (listener != null)
                        listener.onGetDiscussMoreInfo(linkedHashMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void selectSalaryInfo(final Context context, final OnGetDiscussSalaryInfoListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    JSONArray salaryArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.SALARY);
                    ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < salaryArray.length(); i++) {
                        if (salaryArray.optJSONObject(i).optString("name").equals("不限") || salaryArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        jsonObjects.add(salaryArray.optJSONObject(i));
                    }
                    JSONObject jsonSalaryObject = new JSONObject();
                    jsonSalaryObject.put("name", "不限");

                    jsonSalaryObject.put("id", 0);

                    jsonObjects.add(0, jsonSalaryObject);

                    if (listener != null)
                        listener.onGetDiscussSalaryInfo(jsonObjects);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //岗位小类
    public static void selectJobType(final Context context, final String cateStr, final OnGetDiscussJobTypeListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    JSONObject salaryObject = (JSONObject) JsonMetaUtil.getObject(JsonMetaUtil.JOBFUNCL2);
                    JSONArray jobTypeArray = salaryObject.optJSONArray(cateStr);
                    ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < jobTypeArray.length(); i++) {
                        if (jobTypeArray.optJSONObject(i).optString("name").equals("不限") || jobTypeArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        jsonObjects.add(jobTypeArray.optJSONObject(i));
                    }
                    JSONObject jsonSalaryObject = new JSONObject();
                    jsonSalaryObject.put("name", "不限");

                    jsonSalaryObject.put("id", 0);

                    jsonObjects.add(0, jsonSalaryObject);

                    if (listener != null)
                        listener.onGetDiscussJobTypeo(jsonObjects);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static void cleanHistory(Context context, String key)
    {
        LsSimpleCache.get(context).remove(key);
    }


    public static void updateHistory(final Context context, final Map<Long, Map<String, String>> data, final String key)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                while (data.size() > 5) {
                    long lru_key = 0;
                    long lru_time = Long.MAX_VALUE;
                    for (Long key : data.keySet()) {
                        if (key < lru_time) {
                            lru_key = key;
                            lru_time = key;
                        }
                    }
                    data.remove(lru_key);
                }
                try {
                    JSONObject ja = new JSONObject();
                    int index = 0;
                    JSONObject jb;
                    for (Long key : data.keySet()) {
                        jb = new JSONObject();
                        for (Map.Entry<String, String> entries : data.get(key).entrySet()) {
                            jb.put(entries.getKey(), entries.getValue());
                        }
                        ja.put(key + "", jb);
                    }
                    LsSimpleCache.get(context).put(key, ja);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public static void getHistory(final Context context, final String key, final OnGetDiscussHistoryListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
                                          {
                                              @Override
                                              public void run()
                                              {
                                                  JSONObject history = LsSimpleCache.get(context).getAsJSONObject(key);
                                                  Map<Long, Map<String, String>> result = new HashMap<Long, Map<String, String>>();
                                                  if (history != null) {
                                                      try {

                                                          Iterator<String> keys = history.keys();
                                                          while (keys.hasNext()) {
                                                              String next = keys.next();
                                                              JSONObject o = history.getJSONObject(next);
                                                              Iterator it = o.keys();
                                                              Map<String, String> r = new HashMap<String, String>();
                                                              while (it.hasNext()) {
                                                                  String k = (String) it.next();
                                                                  r.put(k, o.getString(k));
                                                              }
                                                              result.put(StringUtil.toLong(next), r);
                                                          }
                                                          if (listener != null) {
                                                              listener.onGetDiscussHistory(result);
                                                          }
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }

                                                  } else {
                                                      if (listener != null) {
                                                          listener.onGetDiscussHistory(result);
                                                      }
                                                  }
                                              }
                                          }

        );
    }


    public static void selectCompanyData(final Context context, final OnGetCompanyInfoListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    JSONArray corptypeArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.CORPKIND);

                    ArrayList<JSONObject> corptypebjects = new ArrayList<JSONObject>();
                    for (int i = 0; i < corptypeArray.length(); i++) {
                        if (corptypeArray.optJSONObject(i).optString("name").equals("不限") || corptypeArray.optJSONObject(i).optString("name").equals("全部")) {
                            continue;
                        }
                        corptypebjects.add(corptypeArray.optJSONObject(i));
                    }
                    JSONObject jsoncorptypeObject = new JSONObject();
                    jsoncorptypeObject.put("name", "不限");
                    jsoncorptypeObject.put("id", 0);
                    corptypebjects.add(0, jsoncorptypeObject);

                    if (listener != null)
                        listener.onGetCompanyInfo(corptypebjects);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static void selectCompusInfo(final Context context, final boolean isPro, final String pro, final OnGetDiscussMoreInfoListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
                                          {
                                              @Override
                                              public void run()
                                              {
                                                  try {
                                                      JSONArray jobArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.JOBTYPE);
                                                      JSONArray degreeArray = (JSONArray) JsonMetaUtil.getObject(JsonMetaUtil.DEGREE);

                                                      ArrayList<JSONObject> corpkindbjects = new ArrayList<JSONObject>();
                                                      for (int i = 0; i < jobArray.length(); i++) {
                                                          if (jobArray.optJSONObject(i).optString("name").equals("不限") || jobArray.optJSONObject(i).optString("name").equals("全部")) {
                                                              continue;
                                                          }
                                                          corpkindbjects.add(jobArray.optJSONObject(i));
                                                      }
                                                      JSONObject jsonCroObject = new JSONObject();
                                                      jsonCroObject.put("name", "不限");
                                                      jsonCroObject.put("id", 0);
                                                      corpkindbjects.add(0, jsonCroObject);


                                                      ArrayList<JSONObject> degreebjects = new ArrayList<JSONObject>();
                                                      for (int i = 0; i < degreeArray.length(); i++) {
                                                          if (degreeArray.optJSONObject(i).optString("name").equals("不限") || degreeArray.optJSONObject(i).optString("name").equals("全部")) {
                                                              continue;
                                                          }
                                                          degreebjects.add(degreeArray.optJSONObject(i));
                                                      }
                                                      JSONObject jsondegreeObject = new JSONObject();
                                                      jsondegreeObject.put("name", "不限");
                                                      jsondegreeObject.put("id", 0);
                                                      degreebjects.add(0, jsondegreeObject);


                                                      LinkedHashMap<String, List<JSONObject>> linkedHashMap = new LinkedHashMap();


                                                      ArrayList<JSONObject> jsonObjects = new ArrayList<>();

                                                      if (isPro) {

                                                          JSONObject object = (JSONObject) JsonMetaUtil.getObject(JsonMetaUtil.JOBLOCCITY);

                                                          JSONObject jsonObject = new JSONObject();
                                                          JSONArray jsonArray = object.getJSONArray(pro.split("#")[1]);
                                                          if (jsonArray != null) {
                                                              for (int i = 0; i < jsonArray.length(); i++) {
                                                                  if (jsonArray.getJSONObject(i).getString("name").equals("不限") || jsonArray.getJSONObject(i).getString("name").equals("全部")) {
                                                                      continue;
                                                                  }
                                                                  jsonObjects.add(jsonArray.getJSONObject(i));
                                                              }
                                                          }
                                                          try {
                                                              jsonObject.put("name", "不限");
                                                              jsonObject.put("id", 0);
                                                              jsonObjects.add(0, jsonObject);
                                                          } catch (JSONException e) {
                                                              e.printStackTrace();
                                                          }
                                                      } else {
                                                          JSONObject object = (JSONObject) JsonMetaUtil.getObject(JsonMetaUtil.JOBLOCDISTRICT);

                                                          JSONObject jsonObject = new JSONObject();
                                                          JSONArray jsonArray = object.getJSONArray(pro);
                                                          if (jsonArray != null) {
                                                              for (int i = 0; i < jsonArray.length(); i++) {
                                                                  if (jsonArray.getJSONObject(i).getString("name").equals("不限") || jsonArray.getJSONObject(i).getString("name").equals("全部")) {
                                                                      continue;
                                                                  }
                                                                  jsonObjects.add(jsonArray.getJSONObject(i));
                                                              }
                                                          }
                                                          try {
                                                              jsonObject.put("name", "不限");
                                                              jsonObject.put("id", 0);
                                                              jsonObjects.add(0, jsonObject);
                                                          } catch (JSONException e) {
                                                              e.printStackTrace();
                                                          }

                                                      }


                                                      linkedHashMap.put("学历要求", degreebjects);
                                                      linkedHashMap.put("工作性质", corpkindbjects);
                                                      linkedHashMap.put("区域筛选", jsonObjects);

                                                      if (listener != null)
                                                          listener.onGetDiscussMoreInfo(linkedHashMap);
                                                  } catch (
                                                          JSONException e
                                                          )

                                                  {
                                                      e.printStackTrace();
                                                  }
                                              }
                                          }

        );

    }

    public interface OnGetDiscussHistoryListener
    {
        void onGetDiscussHistory(Map<Long, Map<String, String>> history);
    }

    public interface OnGetDiscussCityInfoListener
    {
        void onGetDiscussCityInfo(List<JSONObject> cityData, int CityId);
    }

    public interface OnGetDiscussSalaryInfoListener
    {
        void onGetDiscussSalaryInfo(List<JSONObject> salaryData);
    }

    public interface OnGetDiscussJobTypeListener
    {
        void onGetDiscussJobTypeo(List<JSONObject> jobTypeyData);
    }

    public interface OnGetDiscussJobFunListener
    {
        void onGetDiscussJobFunInfo(Map<JSONObject, List<JSONObject>> JobFunData);
    }

    public interface OnGetCompanyInfoListener
    {
        void onGetCompanyInfo(List<JSONObject> CompanyData);
    }

    public interface OnGetDiscussMoreInfoListener
    {
        void onGetDiscussMoreInfo(Map<String, List<JSONObject>> MoreData);
    }


}
