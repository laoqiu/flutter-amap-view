//
//  Convert.swift
//  amap_view
//
//  Created by QiuLiang Wang on 2019/4/23.
//

import MAMapKit

class Convert {
    class func interpretMapOptions(options: Any?, delegate: AmapOptionsSink) {
        // 从json解析到model
        let jsonDecoder = JSONDecoder()
        let jsonData: Data = (options as! String).data(using: .utf8)!
        let options = try? jsonDecoder.decode(UnifiedMapOptions.self, from: jsonData)
        // 调用map方法
        delegate.setCamera(camera: options!.camera)
        delegate.setCompassEnabled(compassEnabled: options!.compassEnabled)
    }
}

