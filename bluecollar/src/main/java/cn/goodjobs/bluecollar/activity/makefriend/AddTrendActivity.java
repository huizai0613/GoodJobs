package cn.goodjobs.bluecollar.activity.makefriend;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.bluecollar.fragment.makefriend.MakeFriendsNearFragment;
import cn.goodjobs.bluecollar.view.upload.UploadImageAdapter;
import cn.goodjobs.bluecollar.view.upload.UploadImageView;
import cn.goodjobs.common.baseclass.BaseImageUploadActivity;
import cn.goodjobs.common.baseclass.choosepic.ImgFileListActivity;
import cn.goodjobs.common.constants.URLS;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.ImageUtil;
import cn.goodjobs.common.util.KeyBoardUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;
import cn.goodjobs.common.util.bdlocation.LocationUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;
import cn.goodjobs.common.util.bdlocation.MyLocationListener;
import cn.goodjobs.common.util.http.HttpUtil;
import cn.goodjobs.common.util.sharedpreferences.SharedPrefUtil;
import cn.goodjobs.common.view.LoadingDialog;

public class AddTrendActivity extends BaseImageUploadActivity {

    EditText etContent;
    TextView tvCount;
    RecyclerView recyclerView;
    MyLocation myLocation;
    private UploadImageAdapter uploadImageAdapter;
    String uploadID;
    protected int uploadCount; // 上传照片的数量
    protected int uploadFinishCount; // 上传照片完成的数量

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
        tvCount = (TextView) findViewById(R.id.tvCount);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // 线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        uploadImageAdapter = new UploadImageAdapter(this);
        recyclerView.setAdapter(uploadImageAdapter);
        isModify = false;

        LocationUtil.newInstance(this).startLoction(new MyLocationListener() {
            @Override
            public void loaction(MyLocation location) {
                SharedPrefUtil.saveObjectToLoacl("location", location);
                myLocation = location;
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvCount.setText("还能再输入"+(200-s.length()+"个字"));
            }
        });
    }

    public void showBottomMenu(UploadImageView uploadImageView) {
        KeyBoardUtil.hide(this); // 隐藏软键盘
        switch (uploadImageView.uploadImaggeData.status) {
            case 0:
                super.showBottomBtns();
                break;
            case 1:
                uploadImageAdapter.removeItem(uploadImageView.uploadImaggeData);
                break;
            case 3:
                // 重新上传
                uploadImageView.uploadImage(uploadID, myLocation, null);
                break;
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

    protected void picSelect() {
        window.dismiss();
        Intent intent = new Intent(this, ImgFileListActivity.class);
        intent.putExtra("imageCount", uploadImageAdapter.getItemCount() - 1);
        startActivityForResult(intent, FLAG_CHOOSE_IMG);
    }

    @Override
    protected void onImageFinish(Uri fileUri) {
        UploadImageAdapter.UploadImaggeData uploadImaggeData;
        uploadImaggeData = new UploadImageAdapter.UploadImaggeData();
        uploadImaggeData.addTrendActivity = this;
        uploadImaggeData.status = 1;
        uploadImaggeData.file = ImageUtil.scal(fileUri);
        uploadImageAdapter.uploadImaggeDatas.add(uploadImageAdapter.uploadImaggeDatas.size() - 1, uploadImaggeData);
        uploadImageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onImageFinish(String filesStr) {
        UploadImageAdapter.UploadImaggeData uploadImaggeData;
        String[] files = filesStr.split(";");
        for (String file : files) {
            LogUtil.info("filename:"+file);
            uploadImaggeData = new UploadImageAdapter.UploadImaggeData();
            uploadImaggeData.addTrendActivity = this;
            uploadImaggeData.file = ImageUtil.scal(Uri.parse(file));
            LogUtil.info("filename:"+file);
            uploadImaggeData.status = 1;
            uploadImageAdapter.uploadImaggeDatas.add(uploadImageAdapter.uploadImaggeDatas.size() - 1, uploadImaggeData);
        }
        uploadImageAdapter.notifyDataSetChanged();
    }

    // 添加动态
    public void doAdd(View view) {
        if (myLocation == null) {
            myLocation = (MyLocation) SharedPrefUtil.getObject("location");
        }
        uploadCount = uploadImageAdapter.uploadImaggeDatas.size()-1;
        if (!StringUtil.isEmpty(etContent.getText()) && StringUtil.isEmpty(uploadID)) {
            HashMap<String, Object> params = new HashMap<String, Object>();
            if (myLocation != null) {
                params.put("lng", myLocation.longitude);
                params.put("lat", myLocation.latitude);
            }
            params.put("content", etContent.getText());
            LoadingDialog.showDialog(this);
            HttpUtil.post(URLS.MAKEFRIEND_ADDTREND, params, this);
            return;
        }
        if (uploadImageAdapter.getItemCount() > 1) {
            uploadImageAdapter.uploadImage(uploadID, myLocation);
            return;
        }
        TipsUtil.show(this, "请输入文字或者选择图片上传");
    }

    @Override
    public void onSuccess(String tag, Object data) {
        super.onSuccess(tag, data);
        if (tag.equals(URLS.MAKEFRIEND_ADDTREND)) {
            JSONObject jsonObject = (JSONObject) data;
            if (uploadImageAdapter.getItemCount() > 1) {
                uploadImageAdapter.uploadImage(jsonObject.optString("dynamicID"), myLocation);
            } else {
                TipsUtil.show(this, jsonObject.optString("message"));
                MakeFriendsNearFragment.needRefresh = true;
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    public void addUploadCount() {
        uploadFinishCount ++;
        if (uploadFinishCount == uploadCount) {
            TipsUtil.show(this, "您的动态发布成功");
            setResult(RESULT_OK);
            MakeFriendsNearFragment.needRefresh = true;
            finish();
        }
    }

    @Override
    protected void back() {
        if (uploadCount > uploadFinishCount) {
            AlertDialogUtil.show(this, R.string.app_name, "您还有" + (uploadCount - uploadFinishCount) + "没有上传成功，是否放弃该照片?", true, "再传一次", "放弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    uploadImageAdapter.uploadImage(uploadID, myLocation);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MakeFriendsNearFragment.needRefresh = true;
                    setResult(RESULT_OK);
                    finish();
                }
            });
        } else {
            super.back();
        }
    }
}
