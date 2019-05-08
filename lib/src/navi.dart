part of amap_view;

class AmapNavi {
  static const MethodChannel _channel =
  const MethodChannel('plugins.laoqiu.com/amap_view_navi');

  /// 导航
  static Future<void> showRoute(RouteNavi navi) async {
    assert(navi != null);
    await _channel.invokeMethod('navi#showRoute', navi.toMap());
  }

}

class RouteNavi {
  RouteNavi({this.naviType = NaviType.driver, this.start, @required this.end, this.wayList})
      : assert(end != null),
        assert(wayList == null || wayList.length <= 3);

  final NaviType naviType;
  final Poi start;
  final Poi end;

  // 途径点目前最多支持3个
  final List<LatLng> wayList;

  Map<String, dynamic> toMap() {
    return {
      "naviType": naviType.index,
      "start": start?.toMap(),
      "end": end?.toMap(),
      "wayList": wayList?.map((i) => i.toMap()),
    };
  }

  @override
  String toString() => '$runtimeType($naviType, $start, $end, $wayList)';
}


