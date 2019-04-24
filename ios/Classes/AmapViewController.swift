//
//  AmapViewController.swift
//  amap_view
//
//  TODO 仍有一个错误未解决:
//  libMobileGestalt MobileGestalt.c:890: MGIsDeviceOneOfType is not supported on this platform.
//
//  Created by QiuLiang Wang on 2019/4/23.
//
import MAMapKit

class AmapViewController: NSObject, FlutterPlatformView, MAMapViewDelegate, AmapOptionsSink {
    
    private var _frame: CGRect
    private var mapView: MAMapView
    private var mapReadyResult: FlutterResult?
    
    private var initialTilt: CGFloat?
    private var cameraTargetBounds: MACoordinateBounds?
    private var trackCameraPosition = false
    private var myLocationEnabled = false
    
    init(withFrame frame: CGRect, viewIdentifier viewId: Int64, arguments args: Any?, binaryMessenger messenger: FlutterBinaryMessenger) {
        
        // 解决 Failed to bind EAGLDrawable 错误
        // 如果frame是zeroed，则初始化一个宽高
        if (frame.width == 0 || frame.height == 0) {
            _frame = CGRect(x: 0, y: 0, width: 100, height: 100)
        } else {
            _frame = frame
        }
        
        mapView = MAMapView(frame: _frame)
        // 自动调整宽高
        mapView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        
        super.init()
        
        let channel = FlutterMethodChannel(name: "plugins.laoqiu.com/amap_view_\(viewId)", binaryMessenger: messenger)
        channel.setMethodCallHandler(onMethodCall)
        
        mapView.delegate = self
        
        // 处理参数
        if let args = args as? [String: Any] {
            Convert.interpretMapOptions(options: args["options"], delegate: self)
            updateInitialMarkers(options: args["markersToAdd"])
        }
    }
    
    func view() -> UIView {
        return mapView
    }
    
    func onMethodCall(methodCall: FlutterMethodCall, result: @escaping FlutterResult) {
        switch(methodCall.method) {
        case "map#waitForMap":
            mapReadyResult = result
        case "map#update":
            let arguments = methodCall.arguments as? [String: Any]
            if (arguments != nil) {
                Convert.interpretMapOptions(options: arguments!["options"], delegate: self)
            }
            result(nil)
        case "markers#update":
            let arguments = methodCall.arguments as? [String: Any]
            if (arguments != nil) {
//                let markersToAdd = arguments!["markersToAdd"] as? [Any]
//                let markersToChange = arguments!["markersToChange"] as? [Any]
//                let markerIdsToRemove = arguments!["markerIdsToRemove"] as? [Any]
            }
            result(nil)
        case "camera#update":
            result(nil)
        case "map#navi":
            result(nil)
        default:
            result(FlutterMethodNotImplemented)
        }
    }
    
    func updateInitialMarkers(options: Any?) {
        
    }
    
    // MAMapViewDelegate
    func mapInitComplete(_ mapView: MAMapView!) {
        mapReadyResult?(nil)
    }
    
    // AMapOptionsSink
    func setCamera(camera: CameraPosition) {
        mapView.setCenter(CLLocationCoordinate2D(latitude: camera.target.latitude, longitude:  camera.target.longitude), animated: true)
        mapView.setZoomLevel(CGFloat(camera.zoom), animated: true)
    }
    
    func setCompassEnabled(compassEnabled: Bool) {
        mapView.showsCompass = compassEnabled
    }
}
