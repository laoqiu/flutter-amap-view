//
//  AmapLocationFactory.swift
//  amap_view
//
//  Created by QiuLiang Wang on 2019/4/25.
//

import Foundation
import AMapFoundationKit


class AmapLocationFactory: NSObject {
    var messenger: FlutterBinaryMessenger
    
    init(withMessenger messenger: FlutterBinaryMessenger) {
        self.messenger = messenger
        super.init()
    }
    
    func register() {
        let channel = FlutterMethodChannel(name: "plugins.laoqiu.com/amap_view_location", binaryMessenger:messenger)
        channel.setMethodCallHandler(onMethodCall)
    }
    
    func onMethodCall(methodCall: FlutterMethodCall, result: @escaping FlutterResult) {
        switch methodCall.method {
        case "location#convert":
            if let args = methodCall.arguments as? [String: Any] {
                let coordType = getCoordType(t: args["coordType"] as? Int)
                let coord = AMapCoordinateConvert(
                    CLLocationCoordinate2D(latitude: (args["latitude"] as? Double)!, longitude: (args["longitude"] as? Double)!), coordType)
                let coordMap: Dictionary<String, Double> = ["latitude": coord.latitude, "longitude": coord.longitude]
                result(coordMap)
            } else {
                result(nil)
            }
        default:
            result(FlutterMethodNotImplemented)
        }
        
    }
    func getCoordType(t: Int?) -> AMapCoordinateType {
        var coordType: AMapCoordinateType
        switch t {
        case 1:
            coordType = AMapCoordinateType.mapBar
        case 2:
            coordType = AMapCoordinateType.mapABC
        case 3:
            coordType = AMapCoordinateType.soSoMap
        case 4:
            coordType = AMapCoordinateType.aliYun
        case 5:
            coordType = AMapCoordinateType.google
        case 6:
            coordType = AMapCoordinateType.GPS
        default:
            coordType = AMapCoordinateType.baidu
        }
        return coordType
    }
}
