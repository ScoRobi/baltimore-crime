package com.baltimorecrime.core.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by scrobinson41 on 8/11/16.
 */
@Getter @Setter
public class DataRanges {

  String minDate;
  String maxDate;

  String minTime;
  String maxTime;

  String minLongitude;
  String maxLongitude;

  String minLatitude;
  String maxLatitude;

  Long crimeCount;

}
