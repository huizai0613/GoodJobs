package cn.goodjobs.bluecollar.view.upload;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.goodjobs.bluecollar.activity.makefriend.AddTrendActivity;
import cn.goodjobs.common.util.DensityUtil;
import cn.goodjobs.common.util.bdlocation.MyLocation;

/**
 * Created by wanggang on 2015/12/3 0003.
 */
public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.MyViewHolder> {

    public List<UploadImaggeData> uploadImaggeDatas;
    public Stack<UploadImageView> uploadImageViews;
    AddTrendActivity addTrendActivity;
    String id;
    MyLocation myLocation;
    private UploadImageView.UploadListener uploadListener = new UploadImageView.UploadListener() {
        @Override
        public void finish(String id) {
            UploadImageAdapter.this.id = id;
            uploadImageViews.pop();
            if (uploadImageViews.size() > 0) {
                upload();
            }
        }

        @Override
        public void failure() {
            uploadImageViews.pop();
            if (uploadImageViews.size() > 0) {
                upload();
            }
        }
    };

    private void upload() {
        uploadImageViews.lastElement().upload(id, myLocation, uploadListener);
    }

    public UploadImageAdapter(AddTrendActivity addTrendActivity) {
        this.addTrendActivity = addTrendActivity;
        uploadImaggeDatas = new ArrayList<UploadImaggeData>();
        UploadImaggeData uploadImaggeData;
        uploadImaggeData = new UploadImaggeData();
        uploadImaggeData.addTrendActivity = addTrendActivity;
        uploadImaggeDatas.add(uploadImaggeData);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UploadImageView uploadImageView = new UploadImageView(parent.getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(parent.getContext(), 80), DensityUtil.dip2px(parent.getContext(), 80));
        layoutParams.rightMargin = DensityUtil.dip2px(parent.getContext(), 10);
        uploadImageView.setLayoutParams(layoutParams);
        return new MyViewHolder(uploadImageView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.uploadImageView.setStatus(uploadImaggeDatas.get(position));
        holder.uploadImageView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return uploadImaggeDatas.size()>4?4:uploadImaggeDatas.size();

    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        UploadImageView uploadImageView;
        public MyViewHolder(View view) {
            super(view);
            uploadImageView = (UploadImageView) view;
            uploadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addTrendActivity.showBottomMenu(uploadImageView);
                }
            });
        }
    }

    public static class UploadImaggeData {
        public File file;
        public int status;
        public int progress;
        public AddTrendActivity addTrendActivity;
        public UploadImageView uploadImageView;

        @Override
        public String toString() {
            return "status="+status+"|progress="+progress;
        }
    }

    public void setStatus(int position, int status) {
        UploadImaggeData uploadImaggeData = uploadImaggeDatas.get(position);
        uploadImaggeData.status = status;
        notifyDataSetChanged();
    }

    public void removeItem(UploadImaggeData uploadImaggeData) {
        uploadImaggeDatas.remove(uploadImaggeData);
        notifyDataSetChanged();
    }

    public void uploadImage(String id, MyLocation myLocation) {
        this.id = id;
        this.myLocation = myLocation;
        int uploadCount = 0;
        int size = uploadImaggeDatas.size() - 1;
        for (int i=size;i>0;--i) {
            UploadImaggeData uploadImaggeData = uploadImaggeDatas.get(i-1);
            if (uploadImaggeData.status == 1 || uploadImaggeData.status == 3) {
                if (uploadImageViews == null) {
                    uploadImageViews = new Stack<UploadImageView>();
                }
                uploadImageViews.add(uploadImaggeData.uploadImageView);
                uploadImaggeData.status = 2;
                uploadCount ++;
            }
        }
        if (uploadCount > 0) {
            upload();
        }
        notifyDataSetChanged();
    }
}
