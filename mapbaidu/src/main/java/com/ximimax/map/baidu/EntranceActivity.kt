package com.ximimax.map.baidu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.baidu.location.BDLocation
import com.baidu.location.BDLocationListener
import com.baidu.location.LocationClient
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.BaiduMap
import kotlinx.android.synthetic.main.ac_entrance.*
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.map.MapStatusUpdateFactory
import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.map.MapStatusUpdate




/**
 * <p>Project:Map</p>
 * <p>Package:com.ximimax.map.baidu</p>
 * <p>Description:Baidu map entrance interface</p>
 * <p>Company:ximimax</p>
 *
 * @author ryandu
 * @date 2018/7/21
 */
class EntranceActivity : AppCompatActivity(), BDLocationListener {


    private var mBaiduMap: BaiduMap? = null
    private var mLocationClient: LocationClient? = null
    private var locationCityName: String = "上海"
    private var latLng: LatLng? = null
    private var isFirstLoc = true // 是否首次定位

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, EntranceActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SDKInitializer.initialize(applicationContext)
        setContentView(R.layout.ac_entrance)
        setTitle(R.string.baidu_map)
        initMap()
        initLocationClient()
    }

    private fun initMap() {
        //获取地图控件引用
        mBaiduMap = map.map
        //普通地图
        mBaiduMap!!.mapType = BaiduMap.MAP_TYPE_NORMAL
        // 开启定位图层
        mBaiduMap!!.isMyLocationEnabled = true

        //默认显示普通地图
        mBaiduMap!!.mapType = BaiduMap.MAP_TYPE_NORMAL
        //开启交通图
//        mBaiduMap!!.isTrafficEnabled = true
        //开启热力图
//        mBaiduMap!!.isBaiduHeatMapEnabled = true
    }


    private fun initLocationClient() {
        mLocationClient = LocationClient(applicationContext, locationOption())
        mLocationClient!!.registerLocationListener(this)
        //开启定位
        mLocationClient!!.start()
        //图片点击事件，回到定位点
        mLocationClient!!.requestLocation()
    }

    private fun location() {
        //把定位点再次显现出来
        val mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng)
        mBaiduMap!!.animateMapStatus(mapStatusUpdate)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        map.onDestroy()
        mLocationClient!!.stop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_entrance, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.action_search -> SearchActivity.start(this, locationCityName)
            R.id.action_location -> location()
            R.id.action_type_normal ->mBaiduMap!!.mapType = BaiduMap.MAP_TYPE_NORMAL
            R.id.action_type_satellite ->mBaiduMap!!.mapType = BaiduMap.MAP_TYPE_SATELLITE
            R.id.action_type_none ->mBaiduMap!!.mapType = BaiduMap.MAP_TYPE_NONE
        }
        return true
    }

    private fun locationOption(): LocationClientOption {
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll")//可选，默认gcj02，设置返回的定位结果坐标系
        val span = 1000
        option.setScanSpan(span)//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true)//可选，设置是否需要地址信息，默认不需要
        option.isOpenGps = true//可选，默认false,设置是否使用gps
        option.isLocationNotify = true//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true)//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true)//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false)
        option.isOpenGps = true // 打开gps

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false)//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false)//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        return option
    }

    override fun onReceiveLocation(location: BDLocation?) {
        locationCityName = location!!.city
        resetLocation(location)
    }

    private fun resetLocation(location: BDLocation) {

        latLng = LatLng(location.latitude, location.longitude)
        // 构造定位数据
        val locData = MyLocationData.Builder()
                .accuracy(location.radius)
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100f).latitude(location.latitude)
                .longitude(location.longitude).build()
        // 设置定位数据
        mBaiduMap!!.setMyLocationData(locData)
        // 当不需要定位图层时关闭定位图层
        //mBaiduMap.setMyLocationEnabled(false);
        if (isFirstLoc) {
            isFirstLoc = false
            val ll = LatLng(location.latitude,
                    location.longitude)
            val builder = MapStatus.Builder()
            builder.target(ll).zoom(18.0f)
            mBaiduMap!!.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))

            val locInfo: String = when {
                location.locType == BDLocation.TypeGpsLocation -> // GPS定位结果
                    location.addrStr
                location.locType == BDLocation.TypeNetWorkLocation -> // 网络定位结果
                    location.addrStr
                location.locType == BDLocation.TypeOffLineLocation -> // 离线定位结果
                    location.addrStr
                location.locType == BDLocation.TypeServerError -> "服务器错误，请检查"
                location.locType == BDLocation.TypeNetWorkException -> "网络错误，请检查"
                location.locType == BDLocation.TypeCriteriaException -> "手机模式错误，请检查是否飞行"
                else -> "normal"
            }

            Toast.makeText(this, locInfo, Toast.LENGTH_SHORT).show()
        }
    }

}