package com.similan.domain.util;

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;

public class GeometryUtils {

  public static final double EARTH_CIRCUNFERENCE_MILES = 24901.50D;

  public static final double MILES_PER_DEGREE_AT_EQUATOR = EARTH_CIRCUNFERENCE_MILES / 360D;

  public static final double DEGREES_PER_MILE_AT_EQUATOR = 1D / MILES_PER_DEGREE_AT_EQUATOR;

  private static  PositionFactoryImpl positionFactory = new PositionFactoryImpl(
      DefaultGeographicCRS.WGS84);
  private static final GeometryFactoryImpl geometryFactory = new GeometryFactoryImpl(
      positionFactory.getCoordinateReferenceSystem(), positionFactory);

  public static  Position toPositon(double longitude, double latitude) {
    Position position = geometryFactory.createPosition(new double[] {
        longitude, latitude });
    return position;
  }

  public static  double[] toCoordinates(Position position) {
    DirectPosition directPosition = position.getDirectPosition();
    if (!directPosition.getCoordinateReferenceSystem().equals(
        positionFactory.getCoordinateReferenceSystem())) {
      throw new IllegalArgumentException(
          "Invalid number of coordinate reference system: "
              + directPosition.getCoordinateReferenceSystem());
    }
    double[] coordinates = position.getDirectPosition().getCoordinate();
    if (coordinates.length != 2) {
      throw new IllegalArgumentException(
          "Invalid number of Position coordinates: " + coordinates.length);
    }
    return coordinates;
  }

  public static double milesToLongitudeGrades(double latitude, double miles) {
    return miles * DEGREES_PER_MILE_AT_EQUATOR
        / Math.cos(Math.toRadians(latitude));
  }

  public static double milesToLatitudeGrades(double miles) {
    return miles * DEGREES_PER_MILE_AT_EQUATOR;
  }

}
