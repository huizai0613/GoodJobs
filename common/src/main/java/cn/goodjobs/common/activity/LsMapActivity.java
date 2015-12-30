package cn.goodjobs.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.StringUtil;
import cn.goodjobs.common.util.TipsUtil;


/**
 * Created by YeXiangYu on 15-10-10.
 */
public class LsMapActivity extends BaseActivity
{

    private MapView map;
    private BaiduMap aMap;
    private double lat;
    private double lng;
    private String content;
    private String title;
    boolean isShow;


//    @Override
//    protected void onResume() {
//        MobclickAgent.onPageStart(getClass().getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
//        super.onResume();
//        map.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        MobclickAgent.onPageEnd(getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息
//        super.onPause();
//        map.onPause();
//    }


    public static void openMap(Context mContext, double lat, double lng, String title, String content)
    {
        Intent intent = new Intent();
        intent.putExtra("LAT", lat);
        intent.putExtra("LNG", lng);
        intent.putExtra("CONTENT", content);
        intent.putExtra("TITLE", title);
        intent.setClass(mContext, LsMapActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
    }


    @Override
    protected void onResume()
    {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        map.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    protected int getLayoutID()
    {
        return R.layout.activity_map;
    }

    @Override
    protected void initWeightClick()
    {

    }

    @Override
    protected void initWeight()
    {
        setTopTitle("地图");
        map = (MapView) findViewById(R.id.map);
        aMap = this.map.getMap();

        //定义Maker坐标点
        LatLng point = new LatLng(lng, lat);
//构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.map);
//构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//在地图上添加Marker，并显示

//定义用于显示该InfoWindow的坐标点

        LinearLayout box = new LinearLayout(mcontext);

        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundResource(R.color.white);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView title = new TextView(mcontext);
        View line = new View(mcontext);
        line.setBackgroundResource(R.color.line_color);
        TextView address = new TextView(mcontext);
        title.setText(this.title);
        title.setPadding(20, 20, 20, 20);
        address.setPadding(20, 20, 20, 20);
        title.setBackgroundResource(R.color.window_background);
        address.setText(this.content);
        box.addView(title, params);
        box.addView(line, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        box.addView(address, params);
//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        final InfoWindow mInfoWindow = new InfoWindow(box, point, -90);
//显示InfoWindow


        aMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {

                if (isShow) {
                    isShow = false;
                    aMap.hideInfoWindow();
                } else {
                    isShow = true;
                    aMap.showInfoWindow(mInfoWindow);
                }
                return false;
            }
        });

        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLngZoom(point, 15);
        aMap.animateMapStatus(mapStatusUpdate);
        aMap.addOverlay(option);

    }

    @Override
    public void initData()
    {
        lat = getIntent().getDoubleExtra("LAT", 0);
        lng = getIntent().getDoubleExtra("LNG", 0);
        content = getIntent().getStringExtra("CONTENT");
        title = getIntent().getStringExtra("TITLE");
    }


}
