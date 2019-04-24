part of amap_view;


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


