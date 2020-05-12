package com.laoqiu.amap_view

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.amap.api.maps.AMap
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviListener
import com.amap.api.navi.*
import com.amap.api.navi.AMapNaviViewListener
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.*
import com.autonavi.tbt.TrafficFacilityInfo


class AmapNaviActivity: Activity(), AMapNaviListener, AMapNaviViewListener {
    private var mAMapNaviView: AMapNaviView? = null
    private var mAMapNavi: AMapNavi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("调试信息", "当前为导航页面");
        mAMapNaviView = AMapNaviView(this)
        mAMapNaviView?.onCreate(savedInstanceState)
        mAMapNaviView?.setAMapNaviViewListener(this)
        mAMapNavi = AMapNavi.getInstance(applicationContext)
        mAMapNavi?.setUseInnerVoice(true)
        mAMapNavi?.addAMapNaviListener(this)
    }
    override fun onResume() {
        super.onResume()
        mAMapNaviView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mAMapNaviView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mAMapNaviView?.onDestroy()
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi?.stopNavi()
        mAMapNavi?.destroy()
    }

    override fun onNaviInfoUpdate(p0: NaviInfo?) {
        TODO("Not yet implemented")
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
        TODO("Not yet implemented")
    }

    override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
        Log.e("调试信息", "路径计算完成");
        mAMapNavi?.startNavi(NaviType.EMULATOR);
    }

    override fun onCalculateRouteFailure(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {
        TODO("Not yet implemented")
    }

    override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {
        TODO("Not yet implemented")
    }

    override fun onEndEmulatorNavi() {
        TODO("Not yet implemented")
    }

    override fun onArrivedWayPoint(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onArriveDestination() {
        TODO("Not yet implemented")
    }

    override fun onPlayRing(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onTrafficStatusUpdate() {
        TODO("Not yet implemented")
    }

    override fun onGpsOpenStatus(p0: Boolean) {
        TODO("Not yet implemented")
    }

    override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {
        TODO("Not yet implemented")
    }

    override fun showCross(p0: AMapNaviCross?) {
        TODO("Not yet implemented")
    }

    override fun onGetNavigationText(p0: Int, p1: String?) {
        TODO("Not yet implemented")
    }

    override fun onGetNavigationText(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {
        TODO("Not yet implemented")
    }

    override fun hideCross() {
        TODO("Not yet implemented")
    }

    override fun onInitNaviFailure() {
        TODO("Not yet implemented")
    }

    override fun onInitNaviSuccess() {
        Log.e("调试信息", "初始化完成");
        Log.e("调试信息", "计算导航路线");
//        if (naviType == 1) {
          mAMapNavi?.calculateRideRoute(NaviLatLng(30.674966,104.071571));
//        } else {
//           calculateWalkRoute(NaviLatLng(30.674966,104.071571));
//        }
    }

    override fun onReCalculateRouteForTrafficJam() {
    }

    override fun updateIntervalCameraInfo(p0: AMapNaviCameraInfo?, p1: AMapNaviCameraInfo?, p2: Int) {
    }

    override fun hideLaneInfo() {
    }

    override fun onNaviInfoUpdated(p0: AMapNaviInfo?) {
    }

    override fun showModeCross(p0: AMapModelCross?) {
    }

    override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {
    }

    override fun hideModeCross() {
    }

    override fun onLocationChange(p0: AMapNaviLocation?) {
    }

    override fun onReCalculateRouteForYaw() {
    }

    override fun onStartNavi(p0: Int) {
    }

    override fun notifyParallelRoad(p0: Int) {
    }

    override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {
    }

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {
    }

    override fun OnUpdateTrafficFacility(p0: TrafficFacilityInfo?) {
    }

    override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {
    }

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {
    }

    override fun showLaneInfo(p0: AMapLaneInfo?) {
    }

    override fun onNaviTurnClick() {
    }

    override fun onScanViewButtonClick() {
    }

    override fun onLockMap(p0: Boolean) {
    }

    override fun onMapTypeChanged(p0: Int) {
    }

    override fun onNaviCancel() {
    }

    override fun onNaviViewLoaded() {
    }

    override fun onNaviBackClick(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onNaviMapMode(p0: Int) {
    }

    override fun onNextRoadClick() {
    }

    override fun onNaviViewShowMode(p0: Int) {
    }

    override fun onNaviSetting() {
        TODO("Not yet implemented")
    }
}