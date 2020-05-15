//
//  AmapNaviViewController.swift
//  amap_view
//
//  Created by QiuLiang Wang on 2020/5/13.
//
import UIKit
import AMapNaviKit

protocol DriveNaviViewControllerDelegate: NSObjectProtocol {
    func driveNaviViewCloseButtonClicked()
}

class AmapNaviViewController: UIViewController, MAMapViewDelegate, AMapNaviRideManagerDelegate, AMapNaviRideViewDelegate {
    
    var mapView: MAMapView!
    var rideView: AMapNaviRideView!
    var rideManager: AMapNaviRideManager!
    
    let startPoint = AMapNaviPoint.location(withLatitude: 30.649863, longitude: 104.066851)!
    let endPoint = AMapNaviPoint.location(withLatitude: 30.659019, longitude: 104.057066)!
    
    override func viewDidLoad() {
        super.viewDidLoad()
//        NSLog("初始化地图")
        initMapView()
        initRideView()
//        initDriveManager()
        initRideManager()
        rideManager.calculateRideRoute(withStart: startPoint, end: endPoint)
    }
    
    func initMapView() {
       mapView = MAMapView(frame: CGRect(x: 0, y: 0, width: view.bounds.width, height: view.bounds.height))
       mapView.delegate = self
       view.addSubview(mapView)
    }
    
    func initRideView() {
        rideView = AMapNaviRideView(frame: view.bounds)
        rideView.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        rideView.delegate = self
        view.addSubview(rideView)
     }
    
    func initRideManager() {
//       AMapNaviRideManager.sharedInstance().delegate = self
//
//       AMapNaviRideManager.sharedInstance().allowsBackgroundLocationUpdates = true
//       AMapNaviRideManager.sharedInstance().pausesLocationUpdatesAutomatically = false
//
//       //将driveView添加为导航数据的Representative，使其可以接收到导航诱导数据
//       AMapNaviRideManager.sharedInstance().addDataRepresentative(rideView)
        rideManager = AMapNaviRideManager.sharedInstance()
        rideManager.allowsBackgroundLocationUpdates = true
        rideManager.pausesLocationUpdatesAutomatically = false
        rideManager.delegate = self
        rideManager.addDataRepresentative(rideView)
    }
    
    
    func rideManager(onCalculateRouteSuccess rideManager: AMapNaviRideManager) {
       NSLog("CalculateRouteSuccess")
       
       //算路成功后开始GPS导航
       AMapNaviRideManager.sharedInstance().startEmulatorNavi()
    }
    
    func rideManager(_ rideManager: AMapNaviRideManager, playNaviSound soundString: String, soundStringType: AMapNaviSoundType) {
        SpeechSynthesizer.Shared.speak(soundString)
    }
   
    func rideViewCloseButtonClicked(_ rideView: AMapNaviRideView) {
       self.dismiss(animated: true, completion: nil)
    }
    
    
    deinit {
        AMapNaviRideManager.sharedInstance().stopNavi()
        AMapNaviRideManager.sharedInstance().removeDataRepresentative(rideView)
        AMapNaviRideManager.sharedInstance().delegate = nil
            
        let success = AMapNaviRideManager.destroyInstance()
        NSLog("单例是否销毁成功 : \(success)")
            
    }
}
