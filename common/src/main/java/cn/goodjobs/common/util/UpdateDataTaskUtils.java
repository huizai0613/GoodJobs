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


    private static ExecutorService mBackgroundThreadPool;

    public static ExecutorService getBackgroundThreadPool()
    {

        if (mBackgroundThreadPool == null) {
            mBackgroundThreadPool = Executors.newFixedThreadPool(3);
        }

        return mBackgroundThreadPool;
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


    public static void updateSearchHistory(final Context context, final Map<Long, Map<String, String>> data)
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
                    LsSimpleCache.get(context).put("searchjob", ja);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public static void getSearchHistory(final Context context, final OnGetDiscussSearchHistoryListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
                                          {
                                              @Override
                                              public void run()
                                              {
                                                  JSONObject history = LsSimpleCache.get(context).getAsJSONObject("searchjob");
                                                  if (history != null) {
                                                      try {
                                                          Map<Long, Map<String, String>> result = new HashMap<Long, Map<String, String>>();
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
                                                              listener.onGetDiscussSearchHistory(result);
                                                          }
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }

                                                  }
                                              }
                                          }

        );
    }


    public static void updateCampusSearchHistory(final Context context, final Map<Long, Map<String, String>> data)
    {
        getBackgroundThreadPool().execute(new Runnable()
        {
            @Override
            public void run()
            {
                while (data.size() > 10) {
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
                    LsSimpleCache.get(context).put("campusJob", ja);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public static void getCampusSearchHistory(final Context context, final OnGetDiscussSearchHistoryListener listener)
    {
        getBackgroundThreadPool().execute(new Runnable()
                                          {
                                              @Override
                                              public void run()
                                              {
                                                  JSONObject history = LsSimpleCache.get(context).getAsJSONObject("campusJob");
                                                  if (history != null) {
                                                      try {
                                                          Map<Long, Map<String, String>> result = new HashMap<Long, Map<String, String>>();
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
                                                              listener.onGetDiscussSearchHistory(result);
                                                          }
                                                      } catch (JSONException e) {
                                                          e.printStackTrace();
                                                      }

                                                  }
                                              }
                                          }

        );
    }


    public interface OnGetDiscussCampusSearchHistoryListener
    {
        void onGetDiscussSearchHistory(Map<Long, Map<String, String>> history);
    }

    public interface OnGetDiscussSearchHistoryListener
    {
        void onGetDiscussSearchHistory(Map<Long, Map<String, String>> history);
    }

    public interface OnGetDiscussCityInfoListener
    {
        void onGetDiscussCityInfo(List<JSONObject> cityData, int CityId);
    }

    public interface OnGetDiscussSalaryInfoListener
    {
        void onGetDiscussSalaryInfo(List<JSONObject> salaryData);
    }

    public interface OnGetDiscussMoreInfoListener
    {
        void onGetDiscussMoreInfo(Map<String, List<JSONObject>> MoreData);
    }
}
