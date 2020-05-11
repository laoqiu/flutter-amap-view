package com.laoqiu.amap_view

import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AmapNaviType
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class AmapNaviFactory(private val registrar: PluginRegistry.Registrar) :
        MethodChannel.MethodCallHandler {
    init {

        // 通信控制器
        val methodChannel = MethodChannel(registrar.messenger(), "plugins.laoqiu.com/amap_view_navi")
        methodChannel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "navi#showRoute" -> {
                val naviType = call.argument<Int>("naviType") ?: AmapNaviType.DRIVER
                var start = Convert.toPoi(call.argument("start"))
                var end = Convert.toPoi(call.argument("end"))
                // TODO: wayList参数未处理
                var wayList = null
                if (end != null) {
                    AmapNaviPage.getInstance().showRouteActivity(
                            registrar.activity(),
                            AmapNaviParams(start, wayList, end, when (naviType) {
                                1 -> AmapNaviType.WALK
                                2 -> AmapNaviType.RIDE
                                else -> AmapNaviType.DRIVER
                            }),
                            null
                    )
//                    AMapNavi(registrar.activity()).run {
//                        addAMapNaviListener(object: AMapNaviListener{
//                            override fun onCalculateRouteSuccess(AMapCalcRouteResult routeResult) {
//
//                            }
//                            override func onInitNaviSuccess() {
//
//                            }
//                        })
//                    }
                }
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

}