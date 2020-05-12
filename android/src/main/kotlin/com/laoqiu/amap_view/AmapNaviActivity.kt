package com.laoqiu.amap_view

import android.os.Bundle
import android.util.Log
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviView
import io.flutter.app.FlutterActivity


class AmapNaviActivity: FlutterActivity() {
    private var mAMapNaviView: AMapNaviView? = null
    private var mAMapNavi: AMapNavi? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("调试信息", "当前为导航页面");
        mAMapNaviView = AMapNaviView(this)
        mAMapNavi = AMapNavi.getInstance(this.applicationContext)
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
}