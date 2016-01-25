package cn.goodjobs.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.CoordinateConverter;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.StringUtil;

/**
 * Created by YeXiangYu on 15-10-10.
 */
public class LsMapActivity extends BaseActivity
{

    private MapView map;
    private AMap aMap;
    private double lat;
    private double lng;
    private String content;
    private String title;


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

        CoordinateConverter coordinateConverter = new CoordinateConverter();

        coordinateConverter.from(CoordinateConverter.CoordType.BAIDU);

        coordinateConverter.coord(new LatLng(lng, lat));

        LatLng convert = coordinateConverter.convert();


        intent.putExtra("LAT", convert.latitude);
        intent.putExtra("LNG", convert.longitude);
        intent.putExtra("CONTENT", content);
        intent.putExtra("TITLE", title);
        intent.setClass(mContext, LsMapActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        map.onCreate(savedInstanceState);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        map.onSaveInstanceState(outState);
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
        LatLng latLng = new LatLng(lat, lng);
        CameraPosition cameraPosition = new CameraPosition(latLng, 20, 0, 0);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        MarkerOptions markerOptions = new MarkerOptions();

//        position(Required) 在地图上标记位置的经纬度值。参数不能为空。
//        title 当用户点击标记，在信息窗口上显示的字符串。
//        snippet 附加文本，显示在标题下方。
//        draggable 如果您允许用户可以自由移动标记，设置为“ true ”。默认情况下为“ false ”。
//        visible 设置“ false ”，标记不可见。默认情况下为“ true ”。
//        anchor图标摆放在地图上的基准点。默认情况下，锚点是从图片下沿的中间处。
//        perspective设置 true，标记有近大远小效果。默认情况下为 false。
//        可以通过Marker.setRotateAngle() 方法设置标记的旋转角度，从正北开始，逆时针计算。如设置旋转90度，Marker.setRotateAngle(90)，效果如下图所示：

        if (!StringUtil.isEmpty(content)) {
            markerOptions.title(title);
            markerOptions.snippet(content);
        }
        markerOptions.position(latLng);
        markerOptions.draggable(false);
        markerOptions.draggable(true);
        Marker marker = aMap.addMarker(markerOptions);
        aMap.moveCamera(cameraUpdate);

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return false;
            }
        });

        aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {
                    marker.showInfoWindow();
                }

            }
        });

        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(Marker marker)
            {

                LinearLayout box = new LinearLayout(mcontext);
                box.setOrientation(LinearLayout.VERTICAL);
                box.setBackgroundResource(R.color.white);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView titleView = new TextView(mcontext);
                View line = new View(mcontext);
                line.setBackgroundResource(R.color.line_color);
                TextView address = new TextView(mcontext);
                titleView.setText(title);
                titleView.setPadding(20, 20, 20, 20);
                address.setPadding(20, 20, 20, 20);
                titleView.setBackgroundResource(R.color.window_background);
                address.setText(content);
                box.addView(titleView, params);
                box.addView(line, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                box.addView(address, params);

                return box;
            }

            @Override
            public View getInfoContents(Marker marker)
            {
                return null;
            }
        });

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
