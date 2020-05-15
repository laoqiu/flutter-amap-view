//
//  AmapNavFactory.swift
//  amap_view
//
//  Created by QiuLiang Wang on 2019/9/20.
//

import Foundation
import AMapNaviKit
import UIKit

class AmapNavFactory: NSObject, AMapNaviCompositeManagerDelegate {
    private var messenger: FlutterBinaryMessenger
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
//
//             self.compositeManager = AMapNaviCompositeManager.init()
//             self.compositeManager.delegate = self
//             let config = AMapNaviCompositeUserConfig.init()
//             if let args = methodCall.arguments as? [String: Any] {
//                 if let start = args["start"] as? [String: Any] {
//                     if let point = start["target"] as? [String: Double] {
//                         config.setRoutePlanPOIType(
//                             AMapNaviRoutePlanPOIType.start,
//                             location: AMapNaviPoint.location(withLatitude: CGFloat(point["latitude"]!), longitude: CGFloat(point["longitude"]!)),
//                             name: start["name"] as? String,
//                             poiId: nil)
//                     }
//                 }
//                 if let end = args["end"] as? [String: Any] {
//                     if let point = end["target"] as? [String: Double] {
//                         config.setRoutePlanPOIType(
//                             AMapNaviRoutePlanPOIType.end,
//                             location: AMapNaviPoint.location(withLatitude: CGFloat(point["latitude"]!), longitude: CGFloat(point["longitude"]!)),
//                             name: end["name"] as? String,
//                             poiId: nil)
//                     }
//                 }
//                 if let wayList = args["wayList"] as? [[String: Double]] {
//                     var i = 0;
//                     for w in wayList {
//                         config.setRoutePlanPOIType(
//                             AMapNaviRoutePlanPOIType.way,
//                             location: AMapNaviPoint.location(withLatitude: CGFloat(w["latitude"]!), longitude: CGFloat(w["longitude"]!)),
//                             name: String(i),
//                             poiId: nil)
//                         i += 1
//                     }
//                 }
//             }
//            self.compositeManager.presentRoutePlanViewController(withOptions: config)
            let mview = UIView(frame: CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height))
            let delegate  = UIApplication.shared.delegate as! AppDelegate
            mview.backgroundColor = UIColor(white: 0.5, alpha: 0.8)
            //添加tag
            mview.tag = 1
            //添加视图
            delegate.window?.addSubview(mview)
            //通过tag 从window移除视图
            delegate.window?.viewWithTag(1)?.removeFromSuperview()
            let amapNaviViewController = AmapNaviViewController()
            let viewController = UIApplication.shared.keyWindow?.rootViewController
            if viewController != nil {
                viewController?.navigationController?.pushViewController(amapNaviViewController, animated: false)
            }
            result(nil)
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
	
