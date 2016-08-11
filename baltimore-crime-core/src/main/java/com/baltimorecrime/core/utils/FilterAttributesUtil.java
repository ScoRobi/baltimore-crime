package com.baltimorecrime.core.utils;

import com.baltimorecrime.core.domain.DateRange;
import com.baltimorecrime.core.domain.District;
import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.domain.LatitudeRange;
import com.baltimorecrime.core.domain.LongitudeRange;
import com.baltimorecrime.core.domain.TimeRange;
import com.baltimorecrime.core.domain.Weapon;
import com.mysql.jdbc.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Scott Robinson on 7/12/16.
 */
public class FilterAttributesUtil {

  /**
   * Maps a list of String attributes to a FilterAttributes object.
   *
   * @param filterDistricts
   * @param filterWeapons
   * @param filterLocations
   * @param filterPosts
   * @param filterNeighborhoods
   * @param filterCrimeCodes
   * @param filterStartDate
   * @param filterEndDate
   * @param filterStartLatitude
   * @param filterEndLatitude
   * @param filterStartLongitude
   * @param filterEndLongitude
   * @param filterStartTime
   * @param filterEndTime
   * @return FilterAttributes
   */
  public static FilterAttributes mapToFilterAttributes(
      String filterDistricts,
      String filterWeapons,
      String filterLocations,
      String filterPosts,
      String filterNeighborhoods,
      String filterCrimeCodes,
      String filterStartDate,
      String filterEndDate,
      String filterStartLatitude,
      String filterEndLatitude,
      String filterStartLongitude,
      String filterEndLongitude,
      String filterStartTime,
      String filterEndTime
  ) {
    try {
      FilterAttributes filterAttributes = new FilterAttributes();
      if(!StringUtils.isNullOrEmpty(filterDistricts)) {
        filterAttributes.setDistricts(Arrays.asList(filterDistricts.split(",")));
      }
      if(!StringUtils.isNullOrEmpty(filterWeapons)) {
        filterAttributes.setWeapons(Arrays.asList(filterWeapons.split(",")));
      }
      if(!StringUtils.isNullOrEmpty(filterLocations)) {
        filterAttributes.setLocations(Arrays.asList(filterLocations.split(",")));
      }
      if(!StringUtils.isNullOrEmpty(filterPosts)) {
        filterAttributes.setPosts(Arrays.asList(filterPosts.split(",")).stream()
            .map(post -> Integer.valueOf(post)).collect(Collectors.toList()));
      }
      if(!StringUtils.isNullOrEmpty(filterNeighborhoods)) {
        filterAttributes.setNeighborhoods(Arrays.asList(filterNeighborhoods.split(",")));
      }
      if(!StringUtils.isNullOrEmpty(filterCrimeCodes)) {
        filterAttributes.setCrimeCodes(Arrays.asList(filterCrimeCodes.split(",")));
      }
      if(!StringUtils.isNullOrEmpty(filterStartDate) && !StringUtils.isNullOrEmpty(filterEndDate)) {
        DateRange dateRange = new DateRange();
        dateRange.setStartDate(Date.valueOf(filterStartDate));
        dateRange.setEndDate(Date.valueOf(filterEndDate));
        filterAttributes.setDateRanges(Arrays.asList(dateRange));
      }
      if(!StringUtils.isNullOrEmpty(filterStartLatitude) && !StringUtils.isNullOrEmpty(filterEndLatitude)) {
        LatitudeRange latitudeRange = new LatitudeRange();
        latitudeRange.setStartLatitude(Double.valueOf(filterStartLatitude));
        latitudeRange.setEndLatitude(Double.valueOf(filterEndLatitude));
        filterAttributes.setLatitudeRanges(Arrays.asList(latitudeRange));
      }
      if(!StringUtils.isNullOrEmpty(filterStartLongitude) && !StringUtils.isNullOrEmpty(filterEndLongitude)) {
        LongitudeRange longitudeRange = new LongitudeRange();
        longitudeRange.setStartLongitude(Double.valueOf(filterStartLongitude));
        longitudeRange.setEndLongitude(Double.valueOf(filterEndLongitude));
        filterAttributes.setLongitudeRanges(Arrays.asList(longitudeRange));
      }
      if(!StringUtils.isNullOrEmpty(filterStartTime) && !StringUtils.isNullOrEmpty(filterEndTime)) {
        filterAttributes.setTimeRanges(buildTimeRange(Time.valueOf(filterStartTime), Time.valueOf(filterEndTime)));
      }
      return filterAttributes;
    } catch (Exception e) {
      //TODO: add better exception handling
      throw e;
    }
  }

  /**
   * Basic validation for a FilterAttributes object.
   * @param filterAttributes
   */
  public static void validateFilterAttributes(FilterAttributes filterAttributes) {

    if (Objects.isNull(filterAttributes)) {
      throw new NotFoundException();
    }

    // Map to keep track of any invalid arguments
    Map<String, List> invalidArguments = new HashMap<>();

    // Validate provided Districts
    if (!CollectionUtils.isEmpty(filterAttributes.getDistricts())) {
      List validDistricts = Arrays.asList(District.values());
      List invalidDistricts = filterAttributes.getDistricts().stream()
          .filter(district -> validDistricts.contains(district))
          .collect(Collectors.toList());

      if (!CollectionUtils.isEmpty(invalidDistricts)) {
        invalidArguments.put("Districts", invalidDistricts);
      }
    }

    // Validate provided Weapons
    if (!CollectionUtils.isEmpty(filterAttributes.getWeapons())) {
      List validWeapons = Arrays.asList(Weapon.values());
      List invalidWeapons = filterAttributes.getWeapons().stream()
          .filter(weapon -> validWeapons.contains(weapon))
          .collect(Collectors.toList());

      if (!CollectionUtils.isEmpty(invalidWeapons)) {
        invalidArguments.put("Weapons", invalidWeapons);
      }
    }

    //TODO: Add remaining filtering fields

    // Throw detailed error message if errors found
    if (!invalidArguments.isEmpty()) {
      String errorMessage = "The following arguments were invalid:\n";
      errorMessage += invalidArguments.keySet().stream().map(key ->
        new String(key + ":[" + invalidArguments.get(key) + "]\n")
      ).collect(Collectors.toList()).toString();

      throw new BadRequestException(errorMessage);
    }
  }

  public static List<TimeRange> buildTimeRange(Time startTime, Time endTime) {

    List<TimeRange> timeRanges = new ArrayList<>();

    // Handle the case where the start time is logically after the end time
    if (startTime.toLocalTime().toSecondOfDay() > endTime.toLocalTime().toSecondOfDay()) {
      TimeRange startTimeRange = new TimeRange();
      startTimeRange.setStartTime(startTime);
      startTimeRange.setEndTime(Time.valueOf("23:59:59"));

      TimeRange endTimeRange = new TimeRange();
      endTimeRange.setStartTime(Time.valueOf("00:00:00"));
      endTimeRange.setEndTime(endTime);

      timeRanges.add(startTimeRange);
      timeRanges.add(endTimeRange);
    } else {
      TimeRange timeRange = new TimeRange();
      timeRange.setStartTime(startTime);
      timeRange.setEndTime(endTime);
      timeRanges.add(timeRange);
    }

    return timeRanges;
  }

}
