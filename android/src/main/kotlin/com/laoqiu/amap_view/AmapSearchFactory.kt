package com.laoqiu.amap_view

import com.amap.api.services.geocoder.*
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry

class AmapSearchFactory(private val registrar: PluginRegistry.Registrar) :
        MethodChannel.MethodCallHandler {

    init {
        // 通信控制器
        val methodChannel = MethodChannel(registrar.messenger(), "plugins.laoqiu.com/amap_view_search")
        methodChannel.setMethodCallHandler(this)
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "search#reGeocode" -> {  // 逆地图编码获取地址信息
                val latLntType = call.argument<Int>("latLntType") ?: 0
                val radius = call.argument<Float>("radius") ?: 50f
                var point = Convert.toLatLng(call.argument("point"))
                if (point != null) {
                    var query = RegeocodeQuery(Convert.toLatLntPoint(point), radius, when(latLntType){
                        1 -> GeocodeSearch.GPS
                        else -> GeocodeSearch.AMAP
                    })
                    GeocodeSearch(registrar.activity()).run {
                        setOnGeocodeSearchListener(object: GeocodeSearch.OnGeocodeSearchListener{
                            override fun onGeocodeSearched(geocodeResult: GeocodeResult?, resultId: Int) {
                            }

                            override fun onRegeocodeSearched(reGeocodeResult: RegeocodeResult?, resultID: Int) {
                                if (reGeocodeResult != null) {
                                    result.success(Convert.toJson(reGeocodeResult.regeocodeAddress))
                                } else {
                                    result.success(null)
                                }
                            }
                        })

                        getFromLocationAsyn(query)
                    }
                } else {
                    result.success(null)
                }
            }
            "search#geocode" -> {
                val address = call.argument<String>("address") ?: ""
                val city = call.argument<String>("city") ?: ""

                if (address != "") {
                    var query = GeocodeQuery(address, city)
                    GeocodeSearch(registrar.activity()).run {
                        setOnGeocodeSearchListener(object: GeocodeSearch.OnGeocodeSearchListener{
                            override fun onGeocodeSearched(geocodeResult: GeocodeResult?, resultId: Int) {
                                if (geocodeResult != null) {
                                    result.success(Convert.toJson(geocodeResult.geocodeAddressList))
                                } else {
                                    result.success(null)
                                }
                            }

                            override fun onRegeocodeSearched(reGeocodeResult: RegeocodeResult?, resultID: Int) {
                            }
                        })

                        getFromLocationNameAsyn(query)
                    }
                } else {
                    result.success(null)
                }
            }
            else -> result.notImplemented()
        }
    }

}