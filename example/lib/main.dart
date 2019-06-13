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
  String _platformVersion = 'Unknown';

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
      markers[centerMarkerId] =
          Marker(markerId: centerMarkerId, position: center);
    });
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    await AmapLocation.start();
    AmapLocation.listen((event) {
      print(event);
    });
  }

  void _addMarker() async {
    final String markerIdVal = 'marker_$_markerIdCounter';
    _markerIdCounter++;
    final MarkerId markerId = MarkerId(markerIdVal);
    var markerIcon = await BitmapDescriptor.fromAvatarWithAssetImage(
      imageConfiguration,
      "assets/map-point.png",
      Avatar(
          url:
              "https://img5.duitang.com/uploads/item/201512/18/20151218165511_AQW4B.jpeg",
          size: Size(112, 112),
          offset: Offset(4, 4),
          radius: 56),
    );
    setState(() {
      markers[markerId] = Marker(
        markerId: markerId,
        icon: markerIcon,
        position: LatLng(
            center.latitude + sin(_markerIdCounter * pi / 6.0) / 20.0,
            center.longitude + sin(_markerIdCounter * pi / 6.0) / 20.0),
      );
    });
  }

  void _addPolyline() async {
    final PolylineId polylineId = PolylineId('polyline_01');
    setState(() {
      polylines[polylineId] = Polyline(
          polylineId: polylineId,
          points: <LatLng>[
            LatLng(30.330511, 120.122398),
            LatLng(30.352437, 120.212005)
          ]
      );
    });
  }

  void _clearMarker() {
    setState(() {
      markers = {};
    });
  }

  Future<void> _routeNavi() async {
    await AmapNavi.showRoute(
        RouteNavi(end: Poi("下一站", LatLng(30.426789, 120.264577), "")));
  }
  
  Future<void> _searchRoute() async {
    var result = await AmapSearch.route(RouteParams(
        start: LatLng(30.330511, 120.122398),
        end: LatLng(30.352437, 120.212005)
    ));
    // print(result);
    var routes = result[0]["steps"].map((i)=> i["polyline"].map((p)=> LatLng(p["latitude"], p["longitude"]) ).toList()).toList();
    setState(() {
      for (var i=0; i<routes.length; i++) {
        var polylineId = PolylineId('polyline_$i');
        polylines[polylineId] = Polyline(
          polylineId: polylineId,
          width: 20,
          points: List<LatLng>.from(routes[i]),
        );
      }
    });

  }

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
                //markers: Set<Marker>.of(markers.values),
                polylines: Set<Polyline>.of(polylines.values),
                onCameraMove: (pos) {
                  print("onCameraMove ${pos.target}");
                  setState(() {
                    markers[centerMarkerId] = markers[centerMarkerId]
                        .copyWith(positionParam: pos.target);
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
            Text("版本号 $_platformVersion"),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceAround,
              children: <Widget>[
                RaisedButton(
                  child: Text("添加"),
                  onPressed: () {
                    _addPolyline();
                  },
                ),
//                RaisedButton(
//                  child: Text("清除所有"),
//                  onPressed: () {
//                    _clearMarker();
//                  },
//                ),
                RaisedButton(
                  child: Text("导航"),
                  onPressed: () {
                    _routeNavi();
                  },
                ),
                RaisedButton(
                  child: Text("行车路径规则"),
                  onPressed: () {
                    _searchRoute();
                  },
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
