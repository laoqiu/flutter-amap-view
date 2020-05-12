package com.laoqiu.amap_view

import android.annotation.SuppressLint
import android.content.Intent
import com.amap.api.navi.AmapNaviPage
import com.amap.api.navi.AmapNaviParams
import com.amap.api.navi.AmapNaviType
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry


class AmapNaviFactory(private val registrar: PluginRegistry.Registrar) :
        MethodChannel.MethodCallHandler  {
    init {

        // 通信控制器
        val methodChannel = MethodChannel(registrar.messenger(), "plugins.laoqiu.com/amap_view_navi")
        methodChannel.setMethodCallHandler(this)
    }

    @SuppressLint("WrongConstant")
    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "navi#showRoute" -> {
                val naviType = call.argument<Int>("naviType")
                var start = Convert.toPoi(call.argument("start"))
                var end = Convert.toPoi(call.argument("end"))
                // TODO: wayList参数未处理
                var wayList = null
                if (end != null) {
                   when(naviType) {
                       1 , 2 -> {
                           var intent = Intent(registrar.activity(), AmapNaviActivity::class.java).addFlags(268435456)
                           registrar.activity().startActivity(intent)

//                           object : PlatformViewFactory(StandardMessageCodec()) {
//                               override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
//                                   return object : PlatformView {
//                                       override fun getView(): View? {
//                                           var aMapNaviView = AMapNaviView(registrar.activity())
//                                           AMapNavi.getInstance(registrar.activity().applicationContext).run {
//                                               addAMapNaviListener(object: AMapNaviListener {
//                                                   override fun onNaviInfoUpdate(p0: NaviInfo?) {
//                                                   }
//
//                                                   override fun onCalculateRouteSuccess(p0: IntArray?) {
//
//                                                   }
//
//                                                   override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
//                                                       Log.e("调试信息", "路径计算完成");
//                                                   }
//
//                                                   override fun onCalculateRouteFailure(p0: Int) {
//                                                   }
//
//                                                   override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {
//                                                   }
//
//                                                   override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {
//                                                   }
//
//                                                   override fun onEndEmulatorNavi() {
//                                                   }
//
//                                                   override fun onArrivedWayPoint(p0: Int) {
//                                                   }
//
//                                                   override fun onArriveDestination() {
//                                                   }
//
//                                                   override fun onPlayRing(p0: Int) {
//                                                   }
//
//                                                   override fun onTrafficStatusUpdate() {
//                                                   }
//
//                                                   override fun onGpsOpenStatus(p0: Boolean) {
//                                                   }
//
//                                                   override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {
//                                                   }
//
//                                                   override fun showCross(p0: AMapNaviCross?) {
//                                                   }
//
//                                                   override fun onGetNavigationText(p0: Int, p1: String?) {
//                                                   }
//
//                                                   override fun onGetNavigationText(p0: String?) {
//                                                   }
//
//                                                   override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {
//                                                   }
//
//                                                   override fun hideCross() {
//                                                   }
//
//                                                   override fun onInitNaviFailure() {
//                                                   }
//
//                                                   override fun onInitNaviSuccess() {
//                                                       Log.e("调试信息", "初始化完成");
//                                                       Log.e("调试信息", "计算导航路线");
//                                                       if (naviType == 1) {
//                                                           calculateRideRoute(NaviLatLng(30.674966,104.071571));
//                                                       } else {
//                                                           calculateWalkRoute(NaviLatLng(30.674966,104.071571));
//                                                       }
//                                                   }
//
//                                                   override fun onReCalculateRouteForTrafficJam() {
//                                                   }
//
//                                                   override fun updateIntervalCameraInfo(p0: AMapNaviCameraInfo?, p1: AMapNaviCameraInfo?, p2: Int) {
//                                                   }
//
//                                                   override fun hideLaneInfo() {
//                                                   }
//
//                                                   override fun onNaviInfoUpdated(p0: AMapNaviInfo?) {
//                                                   }
//
//                                                   override fun showModeCross(p0: AMapModelCross?) {
//                                                   }
//
//                                                   override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {
//                                                   }
//
//                                                   override fun hideModeCross() {
//                                                   }
//
//                                                   override fun onLocationChange(p0: AMapNaviLocation?) {
//                                                   }
//
//                                                   override fun onReCalculateRouteForYaw() {
//                                                   }
//
//                                                   override fun onStartNavi(p0: Int) {
//                                                   }
//
//                                                   override fun notifyParallelRoad(p0: Int) {
//                                                   }
//
//                                                   override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {
//                                                   }
//
//                                                   override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {
//                                                   }
//
//                                                   override fun OnUpdateTrafficFacility(p0: TrafficFacilityInfo?) {
//                                                   }
//
//                                                   override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {
//                                                   }
//
//                                                   override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {
//                                                   }
//
//                                                   override fun showLaneInfo(p0: AMapLaneInfo?) {
//                                                   }
//
//                                               })
//                                           }
//                                           return aMapNaviView
//                                       }
//
//                                       override fun dispose() {
//
//                                       }
//                                   }
//                               }
//                           }
                       }
                       else -> {
                           AmapNaviPage.getInstance().showRouteActivity(
                                   registrar.activity(),
    //                               AmapNaviParams(start, wayList, end, when (naviType) {
    //                                   1 -> AmapNaviType.WALK
    //                                   2 -> AmapNaviType.RIDE
    //                                   else -> AmapNaviType.DRIVER
    //                               }),
                                   AmapNaviParams(start, wayList, end, AmapNaviType.DRIVER),
                                   null
                           )
                       }
                   }
                }
                result.success(null)
            }
            else -> result.notImplemented()
        }
    }

}