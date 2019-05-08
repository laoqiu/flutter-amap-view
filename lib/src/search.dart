part of amap_view;

class AmapSearch {
  static const MethodChannel _channel =
  const MethodChannel('plugins.laoqiu.com/amap_view_search');

  /// 逆地理编码转换
  static Future<dynamic> reGeocode(ReGeocodeParams params) async {
    assert(params != null);
    return await _channel.invokeMethod('search#reGeocode', params.toMap());
  }

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
