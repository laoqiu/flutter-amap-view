import 'package:flutter/material.dart';
import 'dart:async';
import 'dart:math';

import 'package:amap_view/amap_view.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Location _location = null;

  AMapController mapController;
  Map<MarkerId, Marker> markers = <MarkerId, Marker>{};
  Map<PolylineId, Polyline> polylines = <PolylineId, Polyline>{};
  int _markerIdCounter = 1;
  ImageConfiguration imageConfiguration;
  LatLng center = LatLng(30.337875, 120.111339);
  MarkerId centerMarkerId = MarkerId("marker_center");

  @override
  void initState() {
    super.initState();
    imageConfiguration = createLocalImageConfiguration(context);
    setState(() {
      markers[centerMarkerId] = Marker(markerId: centerMarkerId, position: center, infoWindow: InfoWindow(title: "中心"));
    });

    initPlatformState();
  }

  Future<void> initPlatformState() async {
    Location location = await AmapLocation.fetchLocation();
    setState(() {
      print(location.address);
      _location = location;
    });
  }

  void _addMarker() async {
    final String markerIdVal = 'marker_$_markerIdCounter';
    _markerIdCounter++;
    final MarkerId markerId = MarkerId(markerIdVal);
    var markerIcon = await BitmapDescriptor.fromAssetImageWithText(
      imageConfiguration,
      "assets/map-point.png",
      Label(text: "$_markerIdCounter", size: 48, color: Colors.red, offset: Offset(-1, 10)),
    );
    print(markerIcon.toMap());
    setState(() {
      markers[markerId] = Marker(
          markerId: markerId,
          icon: markerIcon,
          position:
              LatLng(center.latitude + sin(_markerIdCounter * pi / 6.0) / 20.0, center.longitude + sin(_markerIdCounter * pi / 6.0) / 20.0),
          infoWindow: InfoWindow(title: 'test', snippet: "hahahkwg"));
    });
  }

  void _addPolyline() async {
    final PolylineId polylineId = PolylineId('polyline_01');
    setState(() {
      polylines[polylineId] =
          Polyline(polylineId: polylineId, points: <LatLng>[LatLng(30.330511, 120.122398), LatLng(30.352437, 120.212005)]);
    });
  }

  void _clear() {
    setState(() {
      markers = {};
      polylines = {};
    });
  }

  Future<void> _geocode() async {
    var result = await AmapSearch.geocode(GeocodeParams(address: "北京市海淀区北京大学口腔医院"));
    print(result);
  }

  Future<void> _searchRoute(LatLng start, LatLng end) async {
    var result = await AmapSearch.route(
      start: start,
      end: end,
    );
    // print(result);
    var routes = result[0]["steps"].map((i) => i["polyline"].map((p) => LatLng(p["latitude"], p["longitude"])).toList()).toList();
    setState(() {
      polylines = {};
      for (var i = 0; i < routes.length; i++) {
        var polylineId = PolylineId('polyline_$i');
        polylines[polylineId] = Polyline(
          polylineId: polylineId,
          width: 20,
          points: List<LatLng>.from(routes[i]),
        );
      }
    });
  }

  // Future<void> cameraMove(LatLng loc) async {
  //   await mapController.animateCamera(CameraUpdate.newLatLng(loc));
  // }

  // Future<dynamic> inputTips(String keyword, String city) async {
  //   var result = await AmapSearch.inputTips(keyword, city);
  //   print("inputTips-> $result");
  //   return result;
  // }

  void _onMapCreated(AMapController controller) {
    mapController = controller;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: <Widget>[
            SizedBox(
              width: double.infinity,
              height: 200,
              child: AmapView(
                initialCameraPosition: CameraPosition(target: center, zoom: 13),
                //myLocationEnabled: true,
                scaleControlsEnabled: false,
                markers: Set<Marker>.of(markers.values),
                polylines: Set<Polyline>.of(polylines.values),
                setMyLocationButtonEnabled: true,
                onCameraMove: (pos) {
                  print("onCameraMove ${pos.target}");
                  setState(() {
                    markers[centerMarkerId] = markers[centerMarkerId].copyWith(positionParam: pos.target);
                  });
                },
                onCameraIdle: (pos) {
                  //var result = await mapController.reGeocodeSearch(ReGeocodeParams(point: pos.target));
                  //print("onCameraIdle reGeocodeSearch: $result");
                  print("onCameraIdle =====> $pos");
                },
                onTap: (pos) {
                  print("onTap====> $pos");
                },
                onMapCreated: _onMapCreated,
              ),
            ),
            Text("当前位置: ${_location?.address}"),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                RaisedButton(
                  child: Text("添加"),
                  onPressed: () {
                    _addMarker();
                  },
                ),
                // RaisedButton(
                //   child: Text("清除"),
                //   onPressed: () {
                //     _clear();
                //   },
                // ),
                RaisedButton(
                  child: Text("定位"),
                  onPressed: () async {
                    Location location = await AmapLocation.fetchLocation();
                    print(location.toJson());
                  },
                ),
                RaisedButton(
                  child: Text("导航"),
                  onPressed: () async {
                    await AmapNavi.showRoute(
                      naviType: NaviType.ride,
                      // start: Poi("", LatLng(30.649863, 104.066851), ""),
                      end: Poi("下一站", LatLng(30.659019, 104.057066), ""),
                    );
                  },
                ),
                RaisedButton(
                  child: Text("路径规划"),
                  onPressed: () async {
                    dynamic data = await AmapSearch.route(
                      start: LatLng(30.649863, 104.066851),
                      end: LatLng(30.659019, 104.057066),
                      routeType: RouteType.ride,
                    );
                    print(data);
                  },
                ),
                // RaisedButton(
                //   child: Text("跳转"),
                //   onPressed: () {
                //     //  cameraMove(LatLng(30.330511, 120.122398));
                //   },
                // ),
                // RaisedButton(
                //   child: Text("搜索"),
                //   onPressed: () {
                //     //  inputTips("医院", "杭州");
                //   },
                // ),
//                RaisedButton(
//                  child: Text("行车路径规则1"),
//                  onPressed: () {
//                    _searchRoute(LatLng(30.330511, 120.122398), LatLng(30.352437, 120.212005));
//                  },
//                ),
//                RaisedButton(
//                  child: Text("行车路径规则2"),
//                  onPressed: () {
//                    _searchRoute(LatLng(30.328881, 120.12993), LatLng(30.340067, 120.121518));
//                  },
//                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
