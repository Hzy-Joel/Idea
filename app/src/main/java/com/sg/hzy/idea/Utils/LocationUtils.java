package com.sg.hzy.idea.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by 胡泽宇 on 2018/11/19.
 */

public class LocationUtils {
    static String TAG = "LocationUtils";
    static LocationClient locationClient;

    public interface GetLocationLinster {
        //经纬度
        public void getLocation(double Longitude, double Latitude);

        public void fail();

        public void getCity(String addresscity);
    }

    public interface GetLocationName
    {
        public void getCity (String city);
        public void fail();
    }

    public static void GetLocation(Context context, GetLocationLinster getLocationLinster) {
//定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动

        locationClient = new LocationClient(context.getApplicationContext());

//声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        LocationListener myLocationListener = new LocationListener();

        myLocationListener.SetReturn(getLocationLinster);
//注册监听函数

        locationClient.registerLocationListener(myLocationListener);
//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("db0911");
//可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(0);
//可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
//可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
//可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
//可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
//可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
//可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
//设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//开始定位
        locationClient.setLocOption(locationOption);
        locationClient.start();
    }

    /**
     * 实现定位回调
     */
    static class LocationListener implements BDLocationListener {
        GetLocationLinster locationLinster;

        public void SetReturn(GetLocationLinster getLocationLinster) {
            this.locationLinster = getLocationLinster;
        }

        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (location.getLocType() == 61 || location.getLocType() == 161&&location!=null) {
                //获取纬度信息
                double latitude = location.getLatitude();
                //获取经度信息
                double longitude = location.getLongitude();
                //获取定位精度，默认值为0.0f
                float radius = location.getRadius();
                //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
                String coorType = location.getCoorType();
                //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
                int errorCode = location.getLocType();
                if (locationLinster != null) {
                    locationLinster.getLocation(longitude, latitude);
                    locationLinster.getCity(location.getCity());
                }
                locationClient.stop();
            }else{
                locationLinster.fail();
            }
        }
    }

    /***
     * 通过经纬度获取到地址
     */
    public static void GetLocationName(Context context,double lo,double la,GetLocationName getLocationName) {
        List<Address> locationList = null;
        // 经纬度解码实例
        Geocoder gc = new Geocoder(context, Locale.getDefault());
        try {
            // 获取Address列表
            locationList = gc.getFromLocation(la, lo, 1);
            // 获取Address实例
            if(locationList.size()==0){
                getLocationName.fail();
            }else {
                Address address = locationList.get(0);
                if (getLocationName != null)
                    getLocationName.getCity(address.getLocality());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}

