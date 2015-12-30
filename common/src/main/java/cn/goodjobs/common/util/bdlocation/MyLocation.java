package cn.goodjobs.common.util.bdlocation;

import java.io.Serializable;

/**
 * Created by 王刚 on 2015/12/16 0016.
 */
public class MyLocation implements Serializable
{

    public double latitude;
    public double longitude;
    public String address;
    public String city;
    public String cityID;

    public MyLocation(double latitude, double longitude, String address, String city, String cityID)
    {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.cityID = cityID;
    }

    @Override
    public String toString()
    {
        return "latitude:" + latitude + "|" + "latitude:" + longitude + "|address:" + address + "|city:" + city;
    }
}
