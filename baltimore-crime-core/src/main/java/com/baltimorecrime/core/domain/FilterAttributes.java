package com.baltimorecrime.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by Scott Robinson on 6/28/16.
 */
@Getter @Setter
public class FilterAttributes {

  Date startDate;
  Date endDate;

  List<TimeRange> timeRanges;

  List<String> crimeCodes;
  List<String> locations;
  List<String> weapons;
  List<Integer> posts;
  List<String> districts;
  List<String> neighborhoods;

  Double startLongitude;
  Double endLongitude;

  Double startLatitude;
  Double endLatitude;

  // Note: Workaround to allow MyBatis mapping
  public FilterAttributes getFilterAttributes() { return this; }

}
