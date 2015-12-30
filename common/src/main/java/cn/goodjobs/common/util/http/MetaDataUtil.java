package cn.goodjobs.common.util.http;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import cn.goodjobs.common.R;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.ScreenManager;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;

/**
 * Created by 王刚 on 2015/12/11 0011.
 * 获取公共数据
 */
public class MetaDataUtil implements HttpResponseHandler{

    public JSONObject metaJson; // mota.json
    private static MetaDataUtil instanse;
    private MetaDataDownloadListener listener;
    private MetaDataUtil() {}

    public static MetaDataUtil getInstanse() {
        if (instanse == null) {
            instanse = new MetaDataUtil();
        }
        return instanse;
    }

    public JSONObject getMetaJson() {
        if (metaJson == null) {
            metaJson = SharedPrefUtil.getJSONObject("meta.json", "meta");
        }
        return metaJson;
    }

    /**
     * 检测公共数据是否下载
     * */
    public void checkMetaJson() {

    }

    public void initMetaData(MetaDataDownloadListener listener) {
        this.listener = listener;
        InputStream is = null;
        try {
            is = ScreenManager.getScreenManager().currentActivity().getResources().openRawResource(R.raw.job_meta);
            byte[] data = readStream(is); // 把输入流转换成字符数组
            String json = new String(data); // 把字符数组转换成字符串
            metaJson = new JSONObject(json);
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("ver", metaJson.optString("version"));
        HttpUtil.post(URLS.API_COMMON_META, paramMap, this);
    }

    /**
     * 把输入流转换成字符数组
     * @param inputStream 输入流
     * @return 字符数组
     * @throws Exception
     */
    private byte[] readStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            bout.write(buffer, 0, len);
        }
        bout.close();
        inputStream.close();
        return bout.toByteArray();
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        listener.failure();
    }

    @Override
    public void onSuccess(String tag, Object data) {
        metaJson = (JSONObject) data;
        SharedPrefUtil.saveDataToLoacl("meta.json", "meta", data);
        listener.success();
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        listener.failure();
    }

    @Override
    public void onProgress(String tag, int progress) {

    }

    public interface MetaDataDownloadListener {
        public void success();
        public void failure();
    }
}
