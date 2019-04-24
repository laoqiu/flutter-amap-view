part of amap_view;

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
