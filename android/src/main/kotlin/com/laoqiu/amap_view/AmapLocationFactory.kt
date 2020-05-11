package com.laoqiu.amap_view

import android.Manifest
import android.util.Log
import androidx.core.app.ActivityCompat
import com.amap.api.location.*
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry

class AmapLocationFactory(private val registrar: PluginRegistry.Registrar) :
        AMapLocationListener,
        MethodCallHandler {

    private var eventSink: EventChannel.EventSink? = null
    private var locationClient: AMapLocationClient

    init {

        // 通信控制器
        val methodChannel = MethodChannel(registrar.messenger(), "plugins.laoqiu.com/amap_view_location")
        methodChannel.setMethodCallHandler(this)

        // 数据流通道
        var eventChannel = EventChannel(registrar.messenger(), "plugins.laoqiu.com/amap_view_location_event")
        eventChannel.setStreamHandler(object: EventChannel.StreamHandler{
            override fun onListen(p0: Any?, sink: EventChannel.EventSink?) {
                // Log.d("location", "onListen")
                eventSink = sink
            }

            override fun onCancel(p0: Any?) {
                // Log.d("location", "onCancel")
            }
        })

        // 初始化定位
        locationClient = AMapLocationClient(registrar.activity())
        locationClient.setLocationListener(this)
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "location#start" -> {
                // Log.d("location", "start")
                val interval: Int = call.argument<Int>("interval") ?: 2000
                val once: Boolean = call.argument<Boolean>("once") ?: false
                // 申请权限
                ActivityCompat.requestPermissions(registrar.activity(),
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION),
                        321
                )

                // 配置参数启动定位
                var options = AMapLocationClientOption()
                options.setOnceLocation(once)
                if (once) {
                    options.setOnceLocationLatest(true)
                } else {
                    options.setInterval(interval.toLong())
                }
                locationClient.setLocationOption(options)
                locationClient.startLocation()

                result.success(null)
            }
            "location#stop" -> {
                // Log.d("location", "stop")
                locationClient.stopLocation()
                result.success(null)
            }
            "location#convert" -> {
                var lat = call.argument<Double>("latitude")
                var lng = call.argument<Double>("longitude")
                var coordType = call.argument<Int>("coordType") ?: 0
                if (lat != null && lng != null) {
                    var point = CoordinateConverter(registrar.activity()).from(when(coordType){
                        1 -> CoordinateConverter.CoordType.BAIDU
                        2 -> CoordinateConverter.CoordType.GOOGLE
                        else -> CoordinateConverter.CoordType.GPS
                    }).coord(DPoint(lat, lng)).convert()
                    result.success(Convert.toJson(point))
                } else {
                    result.success(null)
                }

            }
            else -> result.notImplemented()
        }
    }

    override fun onLocationChanged(location: AMapLocation?) {
        // Log.d("location", location.toString())
        if (location != null) {
            if (location.errorCode == 0) {
                eventSink?.success(Convert.toJson(location))
            } else {
                Log.e("AmapError", "onLocationChanged Error: ${location.errorInfo}")
            }
        }
    }
}
