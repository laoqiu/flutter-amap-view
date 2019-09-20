//
//  AmapSearchFactory.swift
//  amap_view
//
//  Created by QiuLiang Wang on 2019/9/20.
//

import Foundation

class AmapSearchFactory: NSObject {
    var messenger: FlutterBinaryMessenger
    
    init(withMessenger messenger: FlutterBinaryMessenger) {
        self.messenger = messenger
        super.init()
    }
    
    func register() {
        let channel = FlutterMethodChannel(name: "plugins.laoqiu.com/amap_view_search", binaryMessenger:messenger)
        channel.setMethodCallHandler(onMethodCall)
    }
    
    func onMethodCall(methodCall: FlutterMethodCall, result: @escaping FlutterResult) {
        switch methodCall.method {
        case "search#geocode":
            result(nil)
        default:
            result(FlutterMethodNotImplemented)
        }
    }
}
