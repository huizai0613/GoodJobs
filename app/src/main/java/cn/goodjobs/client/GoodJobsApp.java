package cn.goodjobs.client;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by 王刚 on 2015/12/14 0014.
 */
public class GoodJobsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }


}
