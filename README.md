# amap_view

高德地图插件

## Android

在`AndroidManifest.xml`添加如下代码
`
 <meta-data android:name="com.amap.api.v2.apikey" android:value="你的key" />
`

## IOS

在`Info.plist`添加如下代码

```
    // 默认
    <key>NSLocationWhenInUseUsageDescription</key>
    <string>App需要您的同意,才能访问位置</string>
    <key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
    <string>App需要您的同意,才能访问位置</string>
    <key>amap_key</key>
	<string>  你的key  </string>

    // 导航
    <key>UIBackgroundModes</key> 
    <array> 
        <string>location</string> 
    </array>
```

#### 持续定位

```
    await AmapLocation.start();
    AmapLocation.listen((dynamic location) { print(location);});
```

#### 单次定位

```
    Location location = await AmapLocation.fetchLocation();
    print(location.toJson());
```

#### 路径规划 `目前只支持android`

```
    dynamic data = await AmapSearch.route(
        start: LatLng(30.649863, 104.066851),
        end: LatLng(30.659019, 104.057066),
        routeType: RouteType.ride,
    );
    print(data);
```

#### 使用导航 `骑行、步行目前只支持android`

```
    await AmapNavi.showRoute(
        // driver 汽车导航 默认使用官方组件 ride 骑行 walk 步行 【骑行和步行为自定义SDK 发起直接进入导航】
        naviType: NaviType.driver, 
        end: Poi("下一站", LatLng(30.659314, 104.056294), ""),
    );
```