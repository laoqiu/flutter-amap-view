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
  static Future<dynamic> route({
    @required LatLng start,
    @required LatLng end,
    List<LatLng> wayPoints,
    RouteType routeType,
  }) async {
    return await _channel.invokeMethod('search#route', {
      "start": start.toMap(),
      "naviType": routeType.index,
      "end": end.toMap(),
      "wayPoints": wayPoints?.map((i) => i.toMap())?.toList(),
    });
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
  ReGeocodeParams(
      {this.latLntType = LatLntType.amap, @required this.point, this.radius})
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

enum RouteType {
  /// 驾车
  driver,

  /// 步行
  walk,

  /// 公交
  bus,

  /// 骑行
  ride,

  /// 公交
  truck,

  /// 未来行
  plan,
}