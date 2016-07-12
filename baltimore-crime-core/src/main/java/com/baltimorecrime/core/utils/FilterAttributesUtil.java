package com.baltimorecrime.core.utils;

import com.baltimorecrime.core.domain.District;
import com.baltimorecrime.core.domain.FilterAttributes;
import com.mysql.jdbc.StringUtils;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.NotFoundException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
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
      if(!StringUtils.isNullOrEmpty(filterStartDate)) {
        filterAttributes.setStartDate(Date.valueOf(filterStartDate));
      }
      if(!StringUtils.isNullOrEmpty(filterEndDate)) {
        filterAttributes.setEndDate(Date.valueOf(filterEndDate));
      }
      if(!StringUtils.isNullOrEmpty(filterStartLatitude)) {
        filterAttributes.setStartLatitude(Double.valueOf(filterStartLatitude));
      }
      if(!StringUtils.isNullOrEmpty(filterEndLatitude)) {
        filterAttributes.setEndLatitude(Double.valueOf(filterEndLatitude));
      }
      if(!StringUtils.isNullOrEmpty(filterStartLongitude)) {
        filterAttributes.setStartLongitude(Double.valueOf(filterStartLongitude));
      }
      if(!StringUtils.isNullOrEmpty(filterEndLongitude)) {
        filterAttributes.setEndLongitude(Double.valueOf(filterEndLongitude));
      }
      if(!StringUtils.isNullOrEmpty(filterStartTime)) {
        filterAttributes.setStartTime(Time.valueOf(filterStartTime));
      }
      if(!StringUtils.isNullOrEmpty(filterEndTime)) {
        filterAttributes.setEndTime(Time.valueOf(filterEndTime));
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
      throw new NotFoundException(); //TODO: Improve exception handling
    }

    // Validate provided Districts
    if (!CollectionUtils.isEmpty(filterAttributes.getDistricts())) {
      List validDistricts = Arrays.asList(District.values());
      List invalidDistricts = filterAttributes.getDistricts().stream()
          .filter(district -> validDistricts.contains(district))
          .collect(Collectors.toList());

      if (!CollectionUtils.isEmpty(invalidDistricts)) {
        //TODO: throw error with each invalid district
      }
    }

    //TODO: Validate provided Date and Time
  }

}
