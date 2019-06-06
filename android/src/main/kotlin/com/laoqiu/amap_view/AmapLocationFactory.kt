package com.laoqiu.amap_view

import android.Manifest
import android.util.Log
import androidx.core.app.ActivityCompat
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
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
                val interval: Long = call.argument<Long>("interval") ?: 2000
                // 申请权限
                ActivityCompat.requestPermissions(registrar.activity(),
                        arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION),
                        321
                )

                // 配置参数启动定位
                var options = AMapLocationClientOption()
                options.setInterval(interval)
                locationClient.setLocationOption(options)
                locationClient.startLocation()

                result.success(null)
            }
            "location#stop" -> {
                // Log.d("location", "stop")
                locationClient.stopLocation()
                result.success(null)
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
