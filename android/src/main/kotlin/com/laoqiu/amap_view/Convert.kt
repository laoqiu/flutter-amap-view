package com.laoqiu.amap_view

import android.location.Location
import com.amap.api.location.AMapLocation
import com.amap.api.maps.model.*
import com.amap.api.services.core.LatLonPoint
import com.amap.api.services.geocoder.RegeocodeAddress
import com.google.gson.Gson
import io.flutter.view.FlutterMain
import android.graphics.*
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.net.URL


// Gson扩展方法
inline fun <reified T> Gson.fromJson(json: String) = fromJson(json, T::class.java)


class AsyncTaskLoadImage(
        private val marker: Marker,
        private val visible: Boolean,
        private val width: Int = 120,
        private val height: Int = 120,
        private val left: Float = 0f,
        private val top: Float = 0f,
        private val background: Bitmap?) : AsyncTask<String, String, Bitmap>() {

    override fun doInBackground(vararg params: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val url = URL(params[0])
            bitmap = BitmapFactory.decodeStream(url.content as InputStream)
        } catch (e: IOException) {
            Log.d("AsyncTaskLoadImage", e.message)
        }
        return bitmap
    }

    override fun onPostExecute(bitmap: Bitmap) {
        var photo = getCroppedBitmap(
                Bitmap.createScaledBitmap(bitmap, width, height, true), Math.min(width, height) / 2)
        if (background != null) photo = mergeBitmap(background, photo, left, top)
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(photo))
        marker.setVisible(visible)
    }

    fun mergeBitmap(b1: Bitmap, b2: Bitmap, left: Float, top: Float): Bitmap {
        var bitmap = Bitmap.createBitmap(b1.width, b1.height, b1.config)
        var canvas = Canvas(bitmap)
        canvas.drawBitmap(b1, Matrix(), null)
        canvas.drawBitmap(b2, left, top, null)
        return bitmap
    }

}


fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap {
    val sbmp: Bitmap

    if (bmp.width !== radius || bmp.height !== radius) {
        val smallest = Math.min(bmp.width, bmp.height)
        val factor = smallest / radius
        sbmp = Bitmap.createScaledBitmap(bmp,
                bmp.width / factor,
                bmp.height / factor, false)
    } else {
        sbmp = bmp
    }

    val output = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)

    val paint = Paint()
    val rect = Rect(0, 0, radius, radius)

    paint.setAntiAlias(true)
    paint.setFilterBitmap(true)
    paint.setDither(true)
    canvas.drawARGB(0, 0, 0, 0)
    paint.setColor(Color.parseColor("#BAB399"))
    canvas.drawCircle(radius / 2 + 0.7f, radius / 2 + 0.7f,
            radius / 2 + 0.1f, paint)
    paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
    canvas.drawBitmap(sbmp, rect, rect, paint)
    return output
}


class Convert {
    companion object {
        fun updateMapOptions(options: Any?, sink: AmapOptionsSink) {
            val opts = options as Map<String, Any>
            val compassEnabled = opts.get("compassEnabled")
            if (compassEnabled != null) {
                sink.setCompassEnabled(compassEnabled as Boolean)
            }
            val mapType = opts.get("mapType")
            if (mapType != null) {
                sink.setMapType(mapType as Int)
            }
            val myLocationEnabled = opts.get("myLocationEnabled")
            if (myLocationEnabled != null) {
                sink.setMyLocationEnabled(myLocationEnabled as Boolean)
            }
            val scaleControlsEnabled = opts.get("scaleControlsEnabled")
            if (scaleControlsEnabled != null) {
                sink.setScaleEnabled(scaleControlsEnabled as Boolean)
            }
            val zoomControlsEnabled = opts.get("zoomControlsEnabled")
            if (zoomControlsEnabled != null) {
                sink.setZoomControlsEnabled(zoomControlsEnabled as Boolean)
            }
            val scrollGesturesEnabled = opts.get("scrollGesturesEnabled")
            if (scrollGesturesEnabled != null) {
                sink.setScrollGesturesEnabled(scrollGesturesEnabled as Boolean)
            }
        }

        fun interpretMarkerOptions(marker: Marker, new: UnifiedMarkerOptions, old: UnifiedMarkerOptions?) {
            if (old == null) {
                interpretMarkerIcon(new.icon, marker)
            } else {
                if (old.visible != new.visible) {
                    marker.setVisible(new.visible)
                }
                if (old.flat != new.flat) {
                    marker.setFlat(new.flat)
                }
                if (old.alpha != new.alpha) {
                    marker.setAlpha(new.alpha)
                }
                if (old.infoWindow != new.infoWindow) {
                    if (new.infoWindow["snippet"] != null) {
                        marker.setSnippet(new.infoWindow["snippet"] as String)
                    }
                    if (new.infoWindow["title"] != null) {
                        marker.setTitle(new.infoWindow["title"] as String)
                    }
                }
                if (old.draggable != new.draggable) {
                    marker.setDraggable(new.draggable)
                }
                if (old.anchor != new.anchor) {
                    marker.setAnchor(new.anchor[0], new.anchor[1])
                }
                if (old.position != new.position) {
                    marker.setPosition(new.position)
                }
                if (old.rotation != new.rotation) {
                    marker.setRotateAngle(new.rotation)
                }
                if (old.zIndex != new.zIndex) {
                    marker.setZIndex(new.zIndex)
                }
                if (old.icon != new.icon) {
                    interpretMarkerIcon(new.icon, marker)
                }
            }

        }

        fun interpretMarkerIcon(o: Any?, marker: Marker) {
            var data = o as List<Any>
            var icon: BitmapDescriptor
            when (data[0].toString()) {
                "defaultMarker" -> {
                    if (data.size == 1) {
                        icon = BitmapDescriptorFactory.defaultMarker()
                    } else {
                        icon = BitmapDescriptorFactory.defaultMarker(toFloat(data[1]))
                    }
                    marker.setIcon(icon)
                }
                "fromAssetImage" -> {
                    if (data.size == 3) {
                        icon = BitmapDescriptorFactory.fromAsset(
                                FlutterMain.getLookupKeyForAsset(toString(data[1])))
                        marker.setIcon(icon)
                    } else {
                        throw IllegalArgumentException(
                                "'fromAssetImage' Expected exactly 3 arguments, got: " + data.size);
                    }
                }
                "fromAvatarWithAssetImage" -> {
                    if (data.size == 4) {
                        // 网络图片的marker，等图片加载完成才显示
                        var visible: Boolean = marker.isVisible
                        if (visible) marker.setVisible(false)
                        var icon = BitmapDescriptorFactory.fromAsset(
                                FlutterMain.getLookupKeyForAsset(toString(data[1])))
                        var avatar = data[3] as Map<String, Any>
                        var url = avatar.get("url") as String
                        var size = avatar.get("size") as List<Int>
                        var offset = avatar.get("offset") as List<Float>
                        AsyncTaskLoadImage(marker, visible, size[0], size[1], offset[0], offset[1], icon.bitmap).execute(url)
                    } else {
                        throw IllegalArgumentException(
                                "'fromAvatarWithAssetImage' Expected exactly 4 arguments, got: " + data.size);
                    }
                }
                else -> throw IllegalArgumentException("Cannot interpret " + o + " as BitmapDescriptor")
            }
        }

        fun toUnifiedMapOptions(options: Any?): UnifiedMapOptions {
            var data = options as Map<String, Any>
            return Gson().fromJson<UnifiedMapOptions>(Gson().toJson(data))
        }

        fun toUnifiedMarkerOptions(options: Any): UnifiedMarkerOptions {
            var data = options as Map<String, Any>
            return Gson().fromJson<UnifiedMarkerOptions>(Gson().toJson(data))
        }

        fun markerIdToJson(markerId: String): Any {
            var data = hashMapOf<String, Any>(
                    "markerId" to markerId
            )
            return data
        }

        fun toLatLng(o: Any?): LatLng? {
            if (o == null) {
                return null
            }
            var data = toMap(o)
            var latitude = data.get("latitude")
            var longitude = data.get("longitude")
            if (latitude != null && longitude != null) {
                return LatLng(toDouble(latitude), toDouble(longitude))
            }
            return null
        }

        fun toPoi(o: Any?): Poi? {
            if (o == null) {
                return null
            }
            var data = toMap(o)
            var name = data.get("name")
            var target = toLatLng(data.get("target"))
            var s1 = data.get("s1") as String
            if (name != null && target != null) {
                return Poi(toString(name), target, s1)
            }
            return null
        }

        fun toLatLntPoint(latLng: LatLng): LatLonPoint {
            return LatLonPoint(latLng.latitude, latLng.longitude)
        }

        fun toJson(latLng: LatLng): Any {
            // return Arrays.asList(latLng.latitude, latLng.longitude)
            val data = HashMap<String, Any>()
            data.put("latitude", latLng.latitude)
            data.put("longitude", latLng.longitude)
            return data
        }

        fun toJson(location: Location): Any {
            val data = HashMap<String, Any>()
            data.put("latitude", location.latitude)
            data.put("longitude", location.longitude)
            data.put("accuracy", location.accuracy)
            data.put("speed", location.speed)
            data.put("time", location.time)
            return data
        }

        fun toJson(position: CameraPosition): Any {
            val data = HashMap<String, Any>()
            data.put("bearing", position.bearing)
            data.put("target", toJson(position.target))
            data.put("tilt", position.tilt)
            data.put("zoom", position.zoom)
            return data
        }

        fun toJson(location: AMapLocation): Any {
            val data = HashMap<String, Any>()
            data.put("latitude", location.latitude)
            data.put("longitude", location.longitude)
            data.put("accuracy", location.accuracy)
            data.put("speed", location.speed)
            data.put("time", location.time)
            // 以下是定位sdk返回的逆地理信息
            data.put("coordType", location.coordType)
            data.put("country", location.country)
            data.put("city", location.city)
            data.put("district", location.district)
            data.put("street", location.street)
            data.put("address", location.address)
            return data
        }

        fun toJson(address: RegeocodeAddress): Any {
            val data = HashMap<String, Any>()
            data.put("country", address.country)
            data.put("province", address.province)
            data.put("city", address.city)
            data.put("district", address.district)
            data.put("township", address.township)
            data.put("street", address.streetNumber.street + address.streetNumber.number)
            data.put("building", address.building)
            data.put("formatted_address", address.formatAddress)
            return data
        }

        fun toDouble(o: Any): Double {
            return o as Double
        }

        fun toFloat(o: Any): Float {
            return o as Float
        }

        fun toString(o: Any): String {
            return o as String
        }

        fun toMap(o: Any): HashMap<String, Any> {
            return o as HashMap<String, Any>
        }

    }

}


