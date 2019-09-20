//
//  AmapNavFactory.swift
//  amap_view
//
//  Created by QiuLiang Wang on 2019/9/20.
//

import Foundation
import AMapNaviKit


class AmapNavFactory: NSObject, AMapNaviCompositeManagerDelegate {
    var messenger: FlutterBinaryMessenger
    
    private var compositeManager: AMapNaviCompositeManager!
    
    init(withMessenger messenger: FlutterBinaryMessenger) {
        self.messenger = messenger
        super.init()
    }
    
    func register() {
        let channel = FlutterMethodChannel(name: "plugins.laoqiu.com/amap_view_navi", binaryMessenger:messenger)
        channel.setMethodCallHandler(onMethodCall)
    }
    
    func onMethodCall(methodCall: FlutterMethodCall, result: @escaping FlutterResult) {
        switch (methodCall.method) {
        case "navi#showRoute":
            self.compositeManager = AMapNaviCompositeManager.init()
            self.compositeManager.delegate = self
            let config = AMapNaviCompositeUserConfig.init()
            if let args = methodCall.arguments as? [String: Any] {
                if let start = args["start"] as? [String: Any] {
                    if let point = start["target"] as? [String: Double] {
                        config.setRoutePlanPOIType(
                            AMapNaviRoutePlanPOIType.start,
                            location: AMapNaviPoint.location(withLatitude: CGFloat(point["latitude"]!), longitude: CGFloat(point["longitude"]!)),
                            name: start["name"] as? String,
                            poiId: nil)
                    }
                }
                if let end = args["end"] as? [String: Any] {
                    if let point = end["target"] as? [String: Double] {
                        config.setRoutePlanPOIType(
                            AMapNaviRoutePlanPOIType.end,
                            location: AMapNaviPoint.location(withLatitude: CGFloat(point["latitude"]!), longitude: CGFloat(point["longitude"]!)),
                            name: end["name"] as? String,
                            poiId: nil)
                    }
                }
                if let wayList = args["wayList"] as? [[String: Double]] {
                    var i = 0;
                    for w in wayList {
                        config.setRoutePlanPOIType(
                            AMapNaviRoutePlanPOIType.way,
                            location: AMapNaviPoint.location(withLatitude: CGFloat(w["latitude"]!), longitude: CGFloat(w["longitude"]!)),
                            name: String(i),
                            poiId: nil)
                        i += 1
                    }
                }
            }
            self.compositeManager.presentRoutePlanViewController(withOptions: config)
            result(nil)
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
