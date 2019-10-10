package com.laoqiu.amap_view

import com.amap.api.services.geocoder.*
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Tip
import com.amap.api.services.route.*
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
                                    result.success(Convert.addressToJson(geocodeResult.geocodeAddressList))
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
            "search#route" -> {
                var start = Convert.toLatLng(call.argument("start"))
                var end = Convert.toLatLng(call.argument("end"))
                val drivingMode = call.argument<Int>("drivingMode") ?: 0
                var wayPoints = Convert.toWayList(call.argument("wayPoints")) // 途经点

                if (start != null && end != null) {
                    var fromAndTo = RouteSearch.FromAndTo(Convert.toLatLntPoint(start), Convert.toLatLntPoint(end))
                    var query = RouteSearch.DriveRouteQuery(fromAndTo, drivingMode, wayPoints, null, "")
                    RouteSearch(registrar.activity()).run {
                        setRouteSearchListener(object: RouteSearch.OnRouteSearchListener{
                            override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {
                            }
                            override fun onDriveRouteSearched(routeResult: DriveRouteResult?, code: Int) {
                                // 这里返回驾车路径规划结果
                                if (routeResult != null) {
                                    result.success(Convert.pathToJson(routeResult.paths))
                                } else {
                                    result.success(null)
                                }

                            }
                            override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {
                            }
                            override fun onWalkRouteSearched(p0: WalkRouteResult?, p1: Int) {
                            }
                        })

                        calculateDriveRouteAsyn(query)
                    }
                }

            }
            "search#inputtips" -> {
                var keyword = Convert.toString(call.argument("keyword")!!)
                var city = Convert.toString(call.argument("city")!!)
                var query: InputtipsQuery = InputtipsQuery(keyword, city);
                Inputtips(registrar.activity(), query).run {
                    setInputtipsListener(object: Inputtips.InputtipsListener{
                        override fun onGetInputtips(tipList: MutableList<Tip>?, code: Int) {
                            if (code == 0) { // 正确返回
                                result.success(Convert.tipsToJson(tipList))
                            } else {
                                result.success(null)
                            }
                        }
                    })
                    // 异步执行
                    requestInputtipsAsyn()
                }
            }
            else -> result.notImplemented()
        }
    }

}