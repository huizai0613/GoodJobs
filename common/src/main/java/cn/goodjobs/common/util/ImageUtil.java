package cn.goodjobs.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

public class ImageUtil
{
    public static Drawable getDrawbleFromAssets(Context context, String path)
    {
        AssetManager assetManager = context.getAssets();
        InputStream ims;
        Drawable d;
        try {
            ims = assetManager.open("images/" + path);
            d = Drawable.createFromStream(ims, null);
            return d;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void moveFrontBg(View v, int startX, int toX, int startY,
                                   int toY)
    {
        TranslateAnimation anim = new TranslateAnimation(startX, toX, startY,
                toY);
        anim.setDuration(200);
        anim.setFillAfter(true);
        v.startAnimation(anim);
    }

    public static void getPicFromCaptureOrLocal(Activity context,
                                                Uri uri, boolean isCapture, int reuqestCode)
    {
        Intent intent = null;
        if (isCapture) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // 下面这句指定调用相机拍照后的照片存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent = new Intent(Intent.ACTION_PICK, null);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    "image/*");
        }
        context.startActivityForResult(intent, reuqestCode);
    }

    public static void getPicFromCaptureOrLocal(Fragment fragment,
                                                Uri uri, boolean isCapture, int reuqestCode)
    {
        Intent intent = null;
        if (isCapture) {
            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // 下面这句指定调用相机拍照后的照片存储的路径
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        } else {
            intent = new Intent(Intent.ACTION_PICK, null);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                    "image/*");
        }
        fragment.startActivityForResult(intent, reuqestCode);
    }


    public static void startPhotoZoom(Activity context, Uri uri, Uri urito, int aspectX,
                                      int aspectY, int requestCode)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, urito);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        context.startActivityForResult(intent, requestCode);
    }

    public static void startPhotoZoom(Fragment fragment, Uri uri, Uri urito, int aspectX,
                                      int aspectY, int requestCode)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, urito);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void saveBitmap(File file, Bitmap bitmap)
    {
//		try {
//			file.createNewFile();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap compressBmpFromBmp(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        image.recycle();
        return bitmap;
    }

    public static Bitmap compressImageFromFile(String srcPath, int targetWidth, int targetHeight)
    {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && w > targetWidth) {
            be = (int) (newOpts.outWidth / targetWidth);
        } else if (w < h && h > targetHeight) {
            be = (int) (newOpts.outHeight / targetHeight);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率
        newOpts.inJustDecodeBounds = false;
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//		return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩
        //其实是无效的,大家尽管尝试
        return bitmap;
    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri)
    {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static void setDrawable(Context context, TextView textView, int resid, int position)
    {
        Drawable drawable = context.getResources().getDrawable(resid);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        switch (position) {
            case 1:
                textView.setCompoundDrawables(drawable, null, null, null);
                break;
            case 2:
                textView.setCompoundDrawables(null, drawable, null, null);
                break;
            case 3:
                textView.setCompoundDrawables(null, null, drawable, null);
                break;
            case 4:
                textView.setCompoundDrawables(null, null, null, drawable);
                break;

            default:
                break;
        }
    }

    public static File scal(Uri fileUri)
    {
        final long fileMaxSize = 150 * 1024; // 图片大小控制在150以下
        return scal(fileUri, fileMaxSize);
    }

    public static File scal(Uri fileUri, long fileMaxSize)
    {
        List<String> pathSegments = fileUri.getPathSegments();
        String s = pathSegments.get(pathSegments.size() - 1);
        s = s.replaceAll("/", "%2F");
        s = s.replaceAll(":", "%3A");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < pathSegments.size() - 1; i++) {
            builder.append(pathSegments.get(i) + "/");
        }
        builder.append(s);
        String path = builder.toString();
        File outputFile = new File(path);
        long fileSize = outputFile.length();
        LogUtil.info("filesize:" + fileSize);
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int height = options.outHeight;
            int width = options.outWidth;
            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            outputFile = new File(createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
                outputFile = new File(createImageFile().getPath());
                copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;
    }

    public static Uri createImageFile()
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = FileUtils.checkFolder("goodjobsPics");
        File image = null;
        try {
            image = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
        return Uri.fromFile(image);
    }

    public static void copyFileUsingFileChannels(File source, File dest)
    {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            try {
                inputChannel = new FileInputStream(source).getChannel();
                outputChannel = new FileOutputStream(dest).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } finally {
            try {
                inputChannel.close();
                outputChannel.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
