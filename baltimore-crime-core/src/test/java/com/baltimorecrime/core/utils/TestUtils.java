package com.baltimorecrime.core.utils;

import com.baltimorecrime.core.domain.CrimePoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by scrobinson41 on 7/12/16.
 */
public class TestUtils {

  private static boolean initialized = false;

  private static List<CrimePoint> unfilteredCrimePoints = new ArrayList<>();
  private static List<CrimePoint> filteredCrimePoints = new ArrayList<>();
  private static List<CrimePoint> singleDayCrimePoints = new ArrayList<>();


  private static CrimePoint crimePoint1 = new CrimePoint();
  private static CrimePoint crimePoint2 = new CrimePoint();
  private static CrimePoint crimePoint3 = new CrimePoint();

  public static List<CrimePoint> getUnfilteredCrimePoints() {
    if (!initialized) {
      setup();
    }
    return unfilteredCrimePoints;
  }

  public static List<CrimePoint> getFilteredCrimePoints() {
    if (!initialized) {
      setup();
    }
    return filteredCrimePoints;
  }

  public static List<CrimePoint> getSingleDayCrimePoints() {
    if (!initialized) {
      setup();
    }
    return singleDayCrimePoints;
  }

  private static void setup() {
    crimePoint1.setCrimeCode("1A");
    crimePoint2.setCrimeCode("2B");
    crimePoint3.setCrimeCode("3C");

    unfilteredCrimePoints.addAll(Arrays.asList(crimePoint1, crimePoint2, crimePoint3));
    filteredCrimePoints = unfilteredCrimePoints.subList(0,1);
    singleDayCrimePoints.add(crimePoint3);

    initialized = true;
  }

}
