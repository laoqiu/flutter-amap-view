part of amap_view;

class AmapLocation {
  static const _channel = MethodChannel('plugins.laoqiu.com/amap_view_location');
  static const _event = EventChannel('plugins.laoqiu.com/amap_view_location_event');

  static Future<void> start() async {
    await _channel.invokeMethod('location#start');
  }

  static Future<void> stop() async {
    await _channel.invokeMethod('location#stop');
  }

  static void listen(Function callback) {
    _event.receiveBroadcastStream().listen(callback);
  }
}

class LatLng {
  const LatLng(this.latitude, this.longitude)
      : assert(latitude != null),
        assert(longitude != null);

  final double latitude;
  final double longitude;

  Map<String, dynamic> toMap() {
    return {"latitude": latitude, "longitude": longitude};
  }

  static LatLng fromJson(dynamic json) {
    if (json == null) {
      return null;
    }
    return LatLng(json["longitude"], json["latitude"]);
  }

  @override
  String toString() => '$runtimeType($latitude, $longitude)';

  @override
  bool operator ==(Object o) {
    return o is LatLng && o.latitude == latitude && o.longitude == longitude;
  }

  @override
  int get hashCode => hashValues(latitude, longitude);
}

class LatLngBounds {
  const LatLngBounds(this.southwest, this.northeast)
      : assert(southwest != null),
        assert(northeast != null);

  final LatLng southwest;
  final LatLng northeast;

  Map<String, dynamic> toMap() {
    return {"southwest": northeast.toMap(), "northeast": northeast.toMap()};
  }

//  static LatLngBounds fromJson(dynamic json) {
//    if (json == null) {
//      return null;
//    }
//    return LatLngBounds();
//  }

  @override
  String toString() => '$runtimeType($southwest, $northeast)';

  @override
  bool operator ==(Object o) {
    return o is LatLngBounds && o.northeast == northeast && o.southwest == southwest;
  }

  @override
  int get hashCode => hashValues(northeast, southwest);
}


class Poi {
  Poi(this.name, this.target, this.s1) : assert(name != null), assert(target != null);

  final String name;
  final LatLng target;
  final String s1;

  Map<String, dynamic> toMap() {
    final Map<String, dynamic> _data = <String, dynamic>{};
    void addIfPresent(String fieldName, dynamic value) {
      if (value != null) {
        _data[fieldName] = value;
      }
    }

    addIfPresent("name", name);
    addIfPresent("target", target.toMap());
    addIfPresent("s1", s1);

    return _data;
  }

}

