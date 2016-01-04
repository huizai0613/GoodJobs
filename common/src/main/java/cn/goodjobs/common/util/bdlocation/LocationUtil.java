package cn.goodjobs.common.util.bdlocation;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONObject;

import java.util.List;

import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.util.UpdateDataTaskUtils;

/**
 * Created by 王刚 on 2015/12/16 0016.
 */
public class LocationUtil
{

    private static LocationUtil instance;
    private static Context mContext;

    public static LocationUtil newInstance(Context context)
    {
        LogUtil.info("instance" + instance);
        if (instance == null) {
            instance = new LocationUtil();
            mContext = context;
            instance.mLocationClient = new LocationClient(context);     //声明LocationClient类
            instance.mLocationClient.registerLocationListener(instance.bdLocationListener);    //注册监听函数
            instance.initLocation();
        }
        return instance;
    }

    private LocationClient mLocationClient = null;
    private MyLocationListener myLocationListener;

    private BDLocationListener bdLocationListener = new BDLocationListener()
    {
        @Override
        public void onReceiveLocation(final BDLocation bdLocation)
        {
            LogUtil.info("onReceiveLocation:" + bdLocation);
            if (bdLocation != null && myLocationListener != null&&bdLocation.getCity()!=null) {
                UpdateDataTaskUtils.selectCityInfo(mContext, bdLocation.getCity(), new UpdateDataTaskUtils.OnGetDiscussCityInfoListener()
                {
                    @Override
                    public void onGetDiscussCityInfo(List<JSONObject> cityData, int CityId)
                    {
                        myLocationListener.loaction(new MyLocation(bdLocation.getLatitude(), bdLocation.getLongitude(), bdLocation.getAddrStr(), bdLocation.getCity(), CityId + ""));
                        stopLoction();
                    }
                });

            }
        }
    };

    public LocationUtil initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        return this;
    }

    public void startLoction(MyLocationListener myLocationListener)
    {
        this.myLocationListener = myLocationListener;
        mLocationClient.start();
    }

    public void stopLoction()
    {
        mLocationClient.stop();
    }


}
