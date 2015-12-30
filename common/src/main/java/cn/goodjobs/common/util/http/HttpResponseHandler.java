package cn.goodjobs.common.util.http;

/**
 * Created by wanggang on 2015/10/10 0010.
 */
public interface HttpResponseHandler {
    // 请求失败
    public void onFailure(int statusCode, String tag);
    // 请求成功，且返回正确
    public void onSuccess(String tag, Object data);
    // 请求成功，且返回错误
    public void onError(int errorCode, String tag, String errorMessage);
    // 请求进度
    public void onProgress(String tag, int progress);
}
