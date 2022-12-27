package com.example.lostie;

import com.yandex.mapkit.geometry.Point;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import java.util.Date;

@ProvidedTypeConverter
public class Converters {

    @TypeConverter
    public static Double PointToDouble (Point location) {
        if (location == null) return null;
        Double[] value = new Double[2];
        value[0] = location.getLatitude();
        value[1] = location.getLongitude();
        return value[0];
    }

    @TypeConverter
    public static Point DoubleToPoint (Double value) {
        if (value == null) return null;
        Point location = new Point(value, value);
        return location;
    }

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }


}
