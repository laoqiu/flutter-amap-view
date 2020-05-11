# amap_view

高德地图插件

## Android

在`AndroidManifest.xml`添加如下代码
`
 <meta-data android:name="com.amap.api.v2.apikey" android:value="你的key" />
`

## IOS

在`Info.plist`添加如下代码

`
	<key>NSLocationWhenInUseUsageDescription</key>
    <string>App需要您的同意,才能访问位置</string>
	<key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
	<string>App需要您的同意,才能访问位置</string>
`

#### 持续定位

`
    await AmapLocation.start();
    AmapLocation.listen((dynamic location) { print(location);});
   
`

#### 使用导航

`
    await AmapNavi.showRoute(
        RouteNavi(
            naviType: NaviType.walk, // 当前为步行 [骑行和步行 导航组件官方未提供 目前有问题]
            end: Poi("下一站", LatLng(30.659314, 104.056294), ""),
        ),
    );
`