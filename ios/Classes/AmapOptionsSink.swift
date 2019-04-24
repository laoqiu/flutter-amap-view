//
//  AmapOptionsSink.swift
//  amap_view
//
//  Created by QiuLiang Wang on 2019/4/23.
//

protocol AmapOptionsSink {
    func setCamera(camera: CameraPosition)
    func setCompassEnabled(compassEnabled: Bool)
}
