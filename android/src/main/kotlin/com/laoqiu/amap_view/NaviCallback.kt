package com.laoqiu.amap_view

import android.view.View
import com.amap.api.navi.INaviInfoCallback
import com.amap.api.navi.model.AMapNaviLocation

class NaviCallback: INaviInfoCallback {
    override fun getCustomNaviBottomView(): View? {
        return null
    }

    override fun getCustomNaviView(): View? {
        return null
    }

    override fun onArriveDestination(p0: Boolean) {
    }

    override fun onArrivedWayPoint(p0: Int) {
    }

    override fun onCalculateRouteFailure(p0: Int) {
    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {
    }

    override fun onExitPage(p0: Int) {
    }

    override fun onGetNavigationText(p0: String?) {
    }

    override fun onInitNaviFailure() {
    }

    override fun onLocationChange(p0: AMapNaviLocation?) {
    }

    override fun onReCalculateRoute(p0: Int) {
    }

    override fun onStartNavi(p0: Int) {
    }

    override fun onStopSpeaking() {
    }

    override fun onStrategyChanged(p0: Int) {
    }
}