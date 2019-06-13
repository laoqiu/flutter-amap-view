part of amap_view;

class AmapSearch {
  static const MethodChannel _channel =
  const MethodChannel('plugins.laoqiu.com/amap_view_search');

  /// 逆地理编码转换
  static Future<dynamic> reGeocode(ReGeocodeParams params) async {
    assert(params != null);
    return await _channel.invokeMethod('search#reGeocode', params.toMap());
  }

  /// 地理编码转换
  static Future<dynamic> geocode(GeocodeParams params) async {
    assert(params != null);
    return await _channel.invokeMethod('search#geocode', params.toMap());
  }

  /// 行车路径规划
  static Future<dynamic> route(RouteParams params) async {
    assert(params != null);
    return await _channel.invokeMethod('search#route', params.toMap());
  }

}

class GeocodeParams {
  GeocodeParams({@required this.address, this.city});

  final String address;
  final String city;

  Map<String, dynamic> toMap() {
    return {
      "address": address,
      "city": city,
    };
  }

  @override
  String toString() => '$runtimeType($address, $city)';
}

class ReGeocodeParams {
  ReGeocodeParams({this.latLntType = LatLntType.amap, @required this.point, this.radius})
      : assert(point != null);

  final LatLntType latLntType;
  final LatLng point;
  final double radius;

  Map<String, dynamic> toMap() {
    return {
      "latLntType": latLntType.index,
      "point": point?.toMap(),
      "radius": radius,
    };
  }

  @override
  String toString() => '$runtimeType($latLntType, $point, $radius)';
}


class RouteParams {
  RouteParams({@required this.start, @required this.end, this.wayPoints});
  final LatLng start;
  final LatLng end;
  final List<LatLng> wayPoints;

  Map<String, dynamic> toMap() {
    return {
      "start": start.toMap(),
      "end": end.toMap(),
      "wayPoints": wayPoints?.map((i)=> i.toMap())?.toList(),
    };
  }

  @override
  String toString() => '$runtimeType($start, $end, $wayPoints)';
}