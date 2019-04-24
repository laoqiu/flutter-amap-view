import Flutter
import UIKit
import AMapFoundationKit

public class SwiftAmapViewPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    
    AMapServices.shared().apiKey = "891b31d10a27751bbd0c046e1213e0f6"
    AMapServices.shared().enableHTTPS = true

    let instance = AmapViewFactory(withMessenger: registrar.messenger())
    registrar.register(instance, withId: "plugins.laoqiu.com/amap_view")
  }

}
