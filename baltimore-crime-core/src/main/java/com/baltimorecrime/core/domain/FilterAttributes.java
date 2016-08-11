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

  List<DateRange> dateRanges;
  List<TimeRange> timeRanges;

  List<String> crimeCodes;
  List<String> locations;
  List<String> weapons;
  List<Integer> posts;
  List<String> districts;
  List<String> neighborhoods;

  List<LatitudeRange> latitudeRanges;
  List<LongitudeRange> longitudeRanges;

  // Note: Workaround to allow MyBatis mapping
  public FilterAttributes getFilterAttributes() { return this; }

}
