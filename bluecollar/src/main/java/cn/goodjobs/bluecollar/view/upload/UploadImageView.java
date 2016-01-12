package cn.goodjobs.bluecollar.view.upload;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.FileNotFoundException;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.http.HttpResponseHandler;
import cn.goodjobs.common.util.http.HttpUtil;

/**
 * Created by Administrator on 2015/12/3 0003.
 */
public class UploadImageView extends RelativeLayout implements HttpResponseHandler {

    SimpleDraweeView imageview;
    private ImageView imgReload;
    private ImageButton btnClear;
    private Shadow bgView;

    public UploadImageAdapter.UploadImaggeData uploadImaggeData;
    UploadListener uploadListener;
//    public int status; // 0:默认状态，只显示一张图片，可以点击； 1：选择状态，显示图片和删除按钮，可以点击；2：上传状态：显示图片和阴影，不可以点击 3：上传失败：显示图片和阴影，点击重传, 4:上传成功

    public UploadImageView(Context context) {
        super(context);
        addView(LayoutInflater.from(context).inflate(R.layout.item_upload_image, null));

        imageview = (SimpleDraweeView) findViewById(R.id.imageview);
        imgReload = (ImageView) findViewById(R.id.imgReload);
        btnClear = (ImageButton) findViewById(R.id.btnClear);
        bgView = (Shadow) findViewById(R.id.bgView);
    }

    public void setImageView(int resId) {
        imageview.setImageResource(resId);
    }

    public void setStatus(UploadImageAdapter.UploadImaggeData uploadImaggeData) {
        this.uploadImaggeData = uploadImaggeData;
        this.uploadImaggeData.uploadImageView = this;
        fleshView();
    }

    private void fleshView() {
        switch (uploadImaggeData.status) {
            case 0:
                imgReload.setVisibility(View.INVISIBLE);
                btnClear.setVisibility(View.INVISIBLE);
                bgView.setVisibility(View.INVISIBLE);
                imageview.setImageResource(R.mipmap.add_image);
                break;
            case 1:
                imgReload.setVisibility(View.INVISIBLE);
                btnClear.setVisibility(View.VISIBLE);
                bgView.setVisibility(View.INVISIBLE);
                imageview.setImageURI(Uri.fromFile(uploadImaggeData.file));
                break;
            case 2:
                imgReload.setVisibility(View.INVISIBLE);
                btnClear.setVisibility(View.INVISIBLE);
                bgView.setVisibility(View.VISIBLE);
                bgView.setProgress(0);
                imageview.setImageURI(Uri.fromFile(uploadImaggeData.file));
//                upload();
                break;
            case 3:
                imgReload.setVisibility(View.VISIBLE);
                imgReload.setImageResource(R.mipmap.img_reload);
                bgView.setVisibility(View.VISIBLE);
                bgView.setProgress(0);
                btnClear.setVisibility(View.INVISIBLE);
                imageview.setImageURI(Uri.fromFile(uploadImaggeData.file));
                break;
            case 4:
                imgReload.setVisibility(View.VISIBLE);
                imgReload.setImageResource(R.mipmap.img_success);
                bgView.setVisibility(View.VISIBLE);
                bgView.setProgress(0);
                btnClear.setVisibility(View.INVISIBLE);
                imageview.setImageURI(Uri.fromFile(uploadImaggeData.file));
                break;
        }
    }

    public void uploadImage(String id, MyLocation myLocation, UploadListener uploadListener) {
        uploadImaggeData.status = 2;
        fleshView();
        upload(id, myLocation, uploadListener);
    }

    public void upload(String id, MyLocation myLocation, UploadListener uploadListener) {
        this.uploadListener = uploadListener;
        RequestParams requestParams = new RequestParams();
        if (!StringUtil.isEmpty(id)) {
            requestParams.put("dynamicID", id);
        }
        if (myLocation != null) {
            requestParams.put("lng", myLocation.longitude);
            requestParams.put("lat", myLocation.latitude);
        }
        try {
            requestParams.put("pics", uploadImaggeData.file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        HttpUtil.uploadFile(URLS.MAKEFRIEND_ADDPIC, URLS.MAKEFRIEND_ADDPIC, requestParams, this);
    }

    @Override
    public void onFailure(int statusCode, String tag) {
        uploadImaggeData.status = 3;
        fleshView();
        if (uploadListener != null) {
            uploadListener.failure();
        }
    }

    @Override
    public void onSuccess(String tag, Object data) {
        uploadImaggeData.status = 4;
        fleshView();
        if (uploadListener != null) {
            JSONObject jsonObject = (JSONObject) data;
            uploadListener.finish(jsonObject.optString("dynamicID"));
        }
        uploadImaggeData.addTrendActivity.addUploadCount();
    }

    @Override
    public void onError(int errorCode, String tag, String errorMessage) {
        uploadImaggeData.status = 3;
        fleshView();
        if (uploadListener != null) {
            uploadListener.failure();
        }
    }

    @Override
    public void onProgress(String tag, int progress) {
        LogUtil.info("progress:" + progress);
        uploadImaggeData.progress = progress;
        bgView.setProgress(uploadImaggeData.progress);
    }

    public interface UploadListener {
        public void finish(String id);
        public void failure();
    }
}
