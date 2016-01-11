package cn.goodjobs.bluecollar.view.upload;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.goodjobs.bluecollar.activity.makefriend.AddTrendActivity;
import cn.goodjobs.common.util.DensityUtil;

/**
 * Created by wanggang on 2015/12/3 0003.
 */
public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.MyViewHolder> {

    public List<UploadImaggeData> uploadImaggeDatas;
    AddTrendActivity addTrendActivity;

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
        public String id;
        public AddTrendActivity addTrendActivity;

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

    public void uploadImage(String id) {
        int uploadCount = 0;
        for (UploadImaggeData uploadImaggeData: uploadImaggeDatas) {
            if (uploadImaggeData.status == 1 || uploadImaggeData.status == 3) {
                uploadImaggeData.status = 2;
                uploadImaggeData.id = id;
                uploadCount ++;
            }
        }
        addTrendActivity.setUploadCount(uploadCount);
        notifyDataSetChanged();
    }
}
