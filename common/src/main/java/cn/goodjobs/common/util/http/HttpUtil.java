package cn.goodjobs.common.util.http;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import cn.goodjobs.common.GoodJobsApp;
import cn.goodjobs.common.R;
import cn.goodjobs.common.activity.resume.MyResumeActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.IntentUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.view.LoadingDialog;
import cn.goodjobs.common.view.empty.EmptyLayout;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by wanggang on 2015/10/7 0007.
 */
public class HttpUtil {
    public static final String SUCCESS = "SUCCESS";
    public static final String INFO = "INFO";
    public static final int NETERROR = 9008;

    public static final String HOST = "220.178.31.125:3721";

    //            private final static String HOST = "app.goodjobs.cn";
    public static final String BASE_URL = "http://" + HOST + "/";

    public static String APP_API_KEY = "xinANrenCaiWangIsGoodJobS";
    private final static int TIMEOUT_CONNECTION = 30000;
    private static String appUserAgent;
    private static PersistentCookieStore myCookieStore;
    private static Stack<RequstEntity> requstEntityStack;

    private static AsyncHttpClient client;
    private static SyncHttpClient syClient;

    static {
        // 设置请求头
        client = new AsyncHttpClient();
        client.addHeader("Host", HOST);
        client.addHeader("Accept", "application/json, text/plain");
        client.addHeader("Accept-Encoding", "gzip");
        client.addHeader("Accept-Charset", "utf-8");
        client.addHeader("Connection", "Keep-Alive");
        client.addHeader("User-Agent", getUserAgent());
        client.addHeader("App-Key", APP_API_KEY);
        client.setTimeout(TIMEOUT_CONNECTION);
        syClient = new SyncHttpClient();
        syClient.addHeader("Host", HOST);
        syClient.addHeader("Accept", "application/json, text/plain");
        syClient.addHeader("Accept-Encoding", "gzip");
        syClient.addHeader("Accept-Charset", "utf-8");
        syClient.addHeader("Connection", "Keep-Alive");
        syClient.addHeader("User-Agent", getUserAgent());
        syClient.addHeader("App-Key", APP_API_KEY);
        syClient.setTimeout(TIMEOUT_CONNECTION);

        saveLocCookie();
    }


    public static void cancelAllRequests() {
        client.cancelAllRequests(true);
    }

    public static void get(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void syGet(String url, RequestParams params, TextHttpResponseHandler responseHandler) {
        syClient.get(getAbsoluteUrl(url), params, responseHandler);
    }


    public static void post(String url, HttpResponseHandler responseHandler) {
        post(url, url, null, responseHandler);
    }

    public static void post(String url, Map<String, Object> paramsMap, HttpResponseHandler responseHandler) {
        post(url, url, paramsMap, responseHandler);
    }

    public static void postList(String url, Map<String, Object> paramsMap, final HttpResponseHandler responseHandler, EmptyLayout layout) {
        if (!ConnecStatus.isNetworkAvailable(ScreenManager.getScreenManager().currentActivity())) {
            layout.setErrorType(EmptyLayout.NETWORK_ERROR);
            return;
        }
        post(url, url, paramsMap, responseHandler);
    }

    public static void syPost(String url, final HttpResponseHandler responseHandler) {
        syPost(url, url, null, responseHandler);
    }

    public static void syPost(String url, Map<String, Object> paramsMap, final HttpResponseHandler responseHandler) {
        syPost(url, url, paramsMap, responseHandler);
    }


    /**
     * post请求
     *
     * @param url             请求的url
     * @param tag             请求标志，默认为当前url
     * @param paramsMap       请求参数
     * @param responseHandler 请求回调
     */
    public static void syPost(String url, final String tag, Map<String, Object> paramsMap, final HttpResponseHandler responseHandler) {
        // 首先检测网络连接
        if (!ConnecStatus.isNetworkAvailable(ScreenManager.getScreenManager().currentActivity())) {
            AlertDialogUtil.show(ScreenManager.getScreenManager().currentActivity(), R.string.app_name, "检测到您未连接网络，请检测您的网络连接！", true,
                    "去检测", "退出APP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            ScreenManager.getScreenManager().currentActivity().startActivity(intent);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 退出应用
                            ScreenManager.getScreenManager().popAllActivityExceptOne(ScreenManager.getScreenManager().currentActivity());
                            ScreenManager.getScreenManager().currentActivity().finish();
                        }
                    });
            return;
        }
        try {
            RequstEntity requstEntity = new RequstEntity(url, tag, paramsMap, responseHandler);
            if (requstEntityStack == null) {
                // 每次请求都将请求参数信息保存起来，请求结束后再移出
                requstEntityStack = new Stack<RequstEntity>();
            }
            requstEntityStack.add(requstEntity);
            if (paramsMap == null) {
                paramsMap = new HashMap<String, Object>();
            }
            //加入时间因素, 确保相同的参数列表不会有相同的签名(防重放攻击)
            paramsMap.put("timeline", System.currentTimeMillis());

            addSign(paramsMap); // 添加sign
            String cookie = getLocCookie();
            LogUtil.info("cookie:" + cookie);
            if (!StringUtil.isEmpty(cookie)) {
                syClient.addHeader("Cookie", cookie);
            } else {
                syClient.removeHeader("Cookie");
            }
            RequestParams requestParams = new RequestParams();
            for (String key : paramsMap.keySet()) {
                requestParams.put(key, paramsMap.get(key));
            }

            TextHttpResponseHandler textHttpResponseHandler = new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    LogUtil.info("onFailure" + responseString);
                    LogUtil.info("throwable" + throwable.getMessage());
                    if (requstEntityStack != null) {
                        // 移除请求
                        requstEntityStack.pop();
                    }
                    LoadingDialog.hide();
                    responseHandler.onFailure(statusCode, tag);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    LogUtil.info("responseString:" + responseString);
                    LoadingDialog.hide();
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (SUCCESS.equals(jsonObject.getString("level"))) {
                            if (requstEntityStack != null) {
                                // 移除请求
                                requstEntityStack.pop();
                            }
                            responseHandler.onSuccess(tag, jsonObject.get("data"));
                        } else {
                            int errorCode = jsonObject.getInt("errorCode");
                            requstEntityStack.pop();
                            TipsUtil.show(ScreenManager.getScreenManager().currentActivity(), jsonObject.getString("errorMessage"));
                            responseHandler.onError(errorCode, tag, jsonObject.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    responseHandler.onProgress(tag, (int) (bytesWritten / totalSize) * 100);
                }
            };
            LogUtil.logUrl(getAbsoluteUrl(url), paramsMap);
            syClient.post(getAbsoluteUrl(url), requestParams, textHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * post请求
     *
     * @param url             请求的url
     * @param tag             请求标志，默认为当前url
     * @param paramsMap       请求参数
     * @param noTipsCode      请求参数,过滤此状态码提示信息
     * @param responseHandler 请求回调
     */

    public static void post(String url, final String tag, Map<String, Object> paramsMap,
                            final HttpResponseHandler responseHandler, final int... noTipsCode) {
        // 首先检测网络连接
        if (!ConnecStatus.isNetworkAvailable(ScreenManager.getScreenManager().currentActivity())) {
            responseHandler.onFailure(NETERROR, tag);
            LoadingDialog.hide();
            AlertDialogUtil.show(ScreenManager.getScreenManager().currentActivity(), R.string.app_name, "检测到您未连接网络，请检测您的网络连接！", true,
                    "去检测", "退出APP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            ScreenManager.getScreenManager().currentActivity().startActivity(intent);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 退出应用
                            ScreenManager.getScreenManager().popAllActivityExceptOne(ScreenManager.getScreenManager().currentActivity());
                            ScreenManager.getScreenManager().currentActivity().finish();
                        }
                    });
            return;
        }
        try {
            RequstEntity requstEntity = new RequstEntity(url, tag, paramsMap, responseHandler);
            if (requstEntityStack == null) {
                // 每次请求都将请求参数信息保存起来，请求结束后再移出
                requstEntityStack = new Stack<RequstEntity>();
            }
            requstEntityStack.add(requstEntity);
            if (paramsMap == null) {
                paramsMap = new HashMap<String, Object>();
            }
            //加入时间因素, 确保相同的参数列表不会有相同的签名(防重放攻击)
            paramsMap.put("timeline", System.currentTimeMillis());

            addSign(paramsMap); // 添加sign
            String cookie = getLocCookie();
            LogUtil.info("cookie:" + cookie);
            if (!StringUtil.isEmpty(cookie)) {
                client.addHeader("Cookie", cookie);
            } else {
                client.removeHeader("Cookie");
            }
            RequestParams requestParams = new RequestParams();
            for (String key : paramsMap.keySet()) {
                requestParams.put(key, paramsMap.get(key));
            }

            TextHttpResponseHandler textHttpResponseHandler = new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    LogUtil.info("onFailure" + responseString);
                    LogUtil.info("throwable" + throwable.getMessage());
                    if (requstEntityStack != null) {
                        // 移除请求
                        requstEntityStack.pop();
                    }
                    LoadingDialog.hide();
                    responseHandler.onFailure(statusCode, tag);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    LogUtil.info("responseString:" + responseString);
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (SUCCESS.equals(jsonObject.getString("level"))) {
                            LoadingDialog.hide();
                            if (requstEntityStack != null) {
                                // 移除请求
                                requstEntityStack.pop();
                            }
                            responseHandler.onSuccess(tag, jsonObject.get("data"));
                        } else {
                            final int errorCode = jsonObject.getInt("errorCode");
                            if (errorCode == 404) {
                                // 用户服务端掉线
                                if (GoodJobsApp.getInstance().isLogin()) {
                                    // 用户客户端已经登录
                                    // 再次执行登录操作
                                    doLogin();
                                } else {
                                    LoadingDialog.hide();
                                    requstEntityStack.pop();
                                    AlertDialogUtil.show(ScreenManager.getScreenManager().currentActivity(), R.string.app_name, "您尚未登录", true, "去登录", "先看看", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            IntentUtil.toLoginActivity();
                                        }
                                    }, null);
                                }
                                return;
                            } else if (errorCode == 20002) {
                                // 蓝领交友信息不完整
                                IntentUtil.toLanlingPersonalActivity(ScreenManager.getScreenManager().currentActivity());
                            } else if (errorCode == 10016 || errorCode == 10059) {
                                // 蓝领、全职简历不完善
                                LoadingDialog.hide();
                                requstEntityStack.pop();
                                AlertDialogUtil.show(ScreenManager.getScreenManager().currentActivity(), R.string.app_name, jsonObject.getString("errorMessage"), true, "去完善", "先看看", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        if (errorCode == 10016) {
                                            intent.setClass(ScreenManager.getScreenManager().currentActivity(), MyResumeActivity.class);
                                        } else {
                                            intent.setClassName(ScreenManager.getScreenManager().currentActivity(), "cn.goodjobs.bluecollar.activity.InfoCenter.ItemResumeActivity");
                                        }
                                        ScreenManager.getScreenManager().currentActivity().startActivity(intent);
                                    }
                                }, null);
                                return;
                            }
                            LoadingDialog.hide();
                            requstEntityStack.pop();
                            if (!checkErrorCode(errorCode, noTipsCode)) {
                                TipsUtil.show(ScreenManager.getScreenManager().currentActivity(), jsonObject.getString("errorMessage"));
                            }
                            responseHandler.onError(errorCode, tag, jsonObject.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    responseHandler.onProgress(tag, (int) (bytesWritten / totalSize) * 100);
                }
            };
            LogUtil.logUrl(getAbsoluteUrl(url), paramsMap);
            client.post(getAbsoluteUrl(url), requestParams, textHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkErrorCode(int errorCode, int... noTipsCode) {
        if (noTipsCode != null) {
            for (int eCode : noTipsCode) {
                if (eCode == errorCode) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 给请求参数添加sign
     */
    private static void addSign(Map<String, Object> paramsMap) {
        List<String> keys = new ArrayList<String>(paramsMap.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            String v = paramsMap.get(key).toString();
            if (StringUtil.isEmpty(v)) continue;
            sb.append("&");
            sb.append(key);
            sb.append("=");
            try {
                sb.append(URLEncoder.encode(v, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(v);
            }
        }
        if (sb.length() > 0) {
            String sortedParams = sb.substring(1);
            String sign = CryptUtils.Base64Encode(CryptUtils.RSASignature(sortedParams));
            client.addHeader("App-Sign", sign);
        } else {
            String sign = CryptUtils.Base64Encode(CryptUtils.RSASignature(""));
            client.addHeader("App-Sign", sign);
        }
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    /**
     * 获取手机UA
     */
    public static String getUserAgent() {
        if (appUserAgent == null || appUserAgent == "") {
            TelephonyManager tm = ((TelephonyManager) ScreenManager.getScreenManager().currentActivity().getSystemService(Context.TELEPHONY_SERVICE));
            String androidId = Settings.Secure.ANDROID_ID;
            String androidDeviceId = tm.getDeviceId();
            String deviceId = CryptUtils.MD5Encode16(androidId + androidDeviceId);

            String display = getDisplayMetrics().widthPixels + "x" + getDisplayMetrics().heightPixels + "@" + getDisplayMetrics().densityDpi;
            StringBuilder ua = new StringBuilder("Goodjobs Client");
            ua.append('/' + getPackageInfo().versionName + '_' + getPackageInfo().versionCode);//App版本
            ua.append("/Android(" + android.os.Build.VERSION.RELEASE + "|" + android.os.Build.VERSION.SDK_INT + "|" + android.os.Build.MODEL + ")");//手机系统平台
            ua.append("/" + deviceId + "(" + androidDeviceId + "|" + androidId + ")");//客户端唯一标识
            ua.append("/" + display); //屏幕分辨率信息
            appUserAgent = ua.toString();
        }
        return appUserAgent;
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrix = new DisplayMetrics();
        WindowManager wm = ((WindowManager) GoodJobsApp.getInstance().getSystemService(Context.WINDOW_SERVICE));
        wm.getDefaultDisplay().getMetrics(displayMetrix);
        return displayMetrix;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public static PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = ScreenManager.getScreenManager().currentActivity().getPackageManager().getPackageInfo(ScreenManager.getScreenManager().currentActivity().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    static class RequstEntity {
        String url;
        String tag;
        Map<String, Object> param;
        HttpResponseHandler httpResponseHandler;

        public RequstEntity(String url, String tag, Map<String, Object> param, HttpResponseHandler httpResponseHandler) {
            this.url = url;
            this.tag = tag;
            this.param = param;
            this.httpResponseHandler = httpResponseHandler;
        }

        public void request() {
            HttpUtil.post(url, tag, param, httpResponseHandler);
        }
    }

    /**
     * 给用户执行强行登录或者自动登录
     */
    private static void doLogin() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", GoodJobsApp.getInstance().getLoginInfo().userName);
        params.put("password", CryptUtils.Base64Encode(CryptUtils.RSAEncode(GoodJobsApp.getInstance().getLoginInfo().passWord)));
        params.put("isremember", GoodJobsApp.getInstance().getLoginInfo().isAutoLogin ? 2 : 0);
        HttpUtil.post(URLS.API_PERSON_LOGIN, params, new HttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, String tag) {
                IntentUtil.toLoginActivity();
            }

            @Override
            public void onSuccess(String tag, Object data) {
                if (requstEntityStack != null && requstEntityStack.size() > 0) {
                    RequstEntity requstEntity = requstEntityStack.lastElement();
                    requstEntityStack.pop();
                    requstEntity.request();
                }
            }

            @Override
            public void onError(int errorCode, String tag, String errorMessage) {
                IntentUtil.toLoginActivity();
            }

            @Override
            public void onProgress(String tag, int progress) {

            }
        });
    }

    public static void uploadFile(String url, final String tag, RequestParams requestParams, final HttpResponseHandler responseHandler) {
        // 首先检测网络连接
        if (!ConnecStatus.isNetworkAvailable(ScreenManager.getScreenManager().currentActivity())) {
            AlertDialogUtil.show(ScreenManager.getScreenManager().currentActivity(), R.string.app_name, "检测到您未连接网络，请检测您的网络连接！", true,
                    "去检测", "退出APP", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 跳转到系统的网络设置界面
                            Intent intent = null;
                            // 先判断当前系统版本
                            if (android.os.Build.VERSION.SDK_INT > 10) {  // 3.0以上
                                intent = new Intent(Settings.ACTION_SETTINGS);
                            } else {
                                intent = new Intent();
                                intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                            }
                            ScreenManager.getScreenManager().currentActivity().startActivity(intent);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 退出应用
                            ScreenManager.getScreenManager().popAllActivityExceptOne(ScreenManager.getScreenManager().currentActivity());
                            ScreenManager.getScreenManager().currentActivity().finish();
                        }
                    });
            return;
        }
        try {

            String cookie = getLocCookie();
            LogUtil.info("cookie:" + cookie);
            if (!StringUtil.isEmpty(cookie)) {
                client.addHeader("Cookie", cookie);
            } else {
                client.removeHeader("Cookie");
            }
            requestParams.put("timeline", System.currentTimeMillis());
            client.removeHeader("App-Sign");
            TextHttpResponseHandler textHttpResponseHandler = new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    LogUtil.info("onFailure" + responseString);
                    LogUtil.info("throwable" + throwable.getMessage());
                    LoadingDialog.hide();
                    responseHandler.onFailure(statusCode, tag);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    LogUtil.info("responseString:" + responseString);
                    LoadingDialog.hide();
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        if (SUCCESS.equals(jsonObject.getString("level"))) {
                            responseHandler.onSuccess(tag, jsonObject.get("data"));
                        } else {
                            int errorCode = jsonObject.getInt("errorCode");
                            responseHandler.onError(errorCode, tag, jsonObject.getString("errorMessage"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                    responseHandler.onProgress(tag, (int) (bytesWritten * 100 / totalSize));
                }
            };
            client.post(getAbsoluteUrl(url), requestParams, textHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveLocCookie() {
        //保存cookie，自动保存到了shareprefercece
        PersistentCookieStore cookieStore = new PersistentCookieStore(ScreenManager.getScreenManager().currentActivity());
        client.setCookieStore(cookieStore);
        syClient.setCookieStore(cookieStore);
    }

    public static String getLocCookie() {
        PersistentCookieStore cookieStore = new PersistentCookieStore(ScreenManager.getScreenManager().currentActivity());
        if (cookieStore != null) {
            if (cookieStore.getCookies().size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Cookie cookie : cookieStore.getCookies()) {
                    if (cookie.isExpired(new Date(System.currentTimeMillis()))) continue;
                    if (cookie.isPersistent() || "PHPSESSID".equals(cookie.getName())) {
                        sb.append(";").append(cookie.getName() + "=" + cookie.getValue());
                    }
                }
                return sb.toString().substring(1);
            }
        }
        return "";
    }

    public static void cancelRequest() {
        if (client != null) {
            client.cancelAllRequests(true);
        }
    }
}
