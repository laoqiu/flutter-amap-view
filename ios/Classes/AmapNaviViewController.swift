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

class AmapNaviViewController: UIViewController, MAMapViewDelegate, AMapNaviRideManagerDelegate, UICollectionViewDelegate,  UICollectionViewDelegateFlowLayout {
    
    let routePlanInfoViewHeight: CGFloat = 130.0
    let routeIndicatorViewHeight: CGFloat = 64.0
    
    var mapView: MAMapView!
    var rideManager: AMapNaviRideManager!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor.white
        initMapView()
    }
    
    func initMapView() {
       mapView = MAMapView(frame: CGRect(x: 0, y: routePlanInfoViewHeight, width: view.bounds.width, height: view.bounds.height - routePlanInfoViewHeight))
       mapView.delegate = self
       view.addSubview(mapView)
    }
    
    func initRideManager() {
        rideManager = AMapNaviRideManager.sharedInstance()
        rideManager.delegate = self
    }
    
}
