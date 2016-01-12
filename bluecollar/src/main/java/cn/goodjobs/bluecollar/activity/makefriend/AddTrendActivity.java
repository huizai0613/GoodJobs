package cn.goodjobs.bluecollar.activity.makefriend;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import java.io.File;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.view.upload.UploadImageAdapter;
import cn.goodjobs.bluecollar.view.upload.UploadImageView;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.baseclass.BaseImageUploadActivity;
import cn.goodjobs.common.baseclass.choosepic.ImgFileListActivity;
import cn.goodjobs.common.util.ImageUtil;

public class AddTrendActivity extends BaseImageUploadActivity {

    EditText etContent;
    RecyclerView recyclerView;

    private UploadImageAdapter uploadImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_add_trend;
    }

    @Override
    protected void initWeight() {
        super.initWeight();
        setTopTitle("添加动态");
        etContent = (EditText) findViewById(R.id.etContent);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // 线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        uploadImageAdapter = new UploadImageAdapter(this);
        recyclerView.setAdapter(uploadImageAdapter);
        isModify = false;
    }

    public void showBottomMenu(UploadImageView uploadImageView) {
        switch (uploadImageView.uploadImaggeData.status) {
            case 0:
                super.showBottomBtns();
                break;
            case 1:
                uploadImageAdapter.removeItem(uploadImageView.uploadImaggeData);
                break;
            case 3:
                // 重新上传
                uploadImageView.upload();
                break;
        }
    }

    /**
     * 拍照
     *
     * @param v
     */
    public void capturePhoto(View v) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                ImageUtil.getPicFromCaptureOrLocal(this, Uri.parse(savePath), true,
                        FLAG_CHOOSE_PHONE);
                window.dismiss();
            } catch (ActivityNotFoundException e) {
            }
        }
    }

    /**
     * 从本地获取图片
     *
     * @param v
     */
    public void pickPhotoFromLocal(View v) {
        Intent intent = new Intent(this, ImgFileListActivity.class);
        intent.putExtra("imageCount", uploadImageAdapter.getItemCount() - 1);
        startActivityForResult(intent, FLAG_CHOOSE_IMG);
        window.dismiss();
    }

    @Override
    protected void onImageFinish(File file) {
        UploadImageAdapter.UploadImaggeData uploadImaggeData;
        uploadImaggeData = new UploadImageAdapter.UploadImaggeData();
        uploadImaggeData.addTrendActivity = this;
        uploadImaggeData.status = 1;
        uploadImaggeData.file = ImageUtil.scal(Uri.fromFile(file));
        uploadImageAdapter.uploadImaggeDatas.add(uploadImaggeData);
        uploadImageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onImageFinish(String filesStr) {
        UploadImageAdapter.UploadImaggeData uploadImaggeData;
        String[] files = filesStr.split(";");
        for (String file : files) {
            uploadImaggeData = new UploadImageAdapter.UploadImaggeData();
            uploadImaggeData.addTrendActivity = this;
            uploadImaggeData.file = ImageUtil.scal(Uri.parse(file));
            uploadImaggeData.status = 1;
            uploadImageAdapter.uploadImaggeDatas.add(uploadImageAdapter.uploadImaggeDatas.size() - 1, uploadImaggeData);
        }
        uploadImageAdapter.notifyDataSetChanged();
    }
}
