package com.baltimorecrime.core.service;

import com.baltimorecrime.core.dao.MapDao;
import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.utils.FilterAttributesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Scott Robinson on 6/28/16.
 */
@Service
public class MapService {

  @Autowired
  MapDao mapDao;

  /**
   * Reads all crime points.
   * @return List
   */
  public List<CrimePoint> readUnfilteredData() {

    // Read data from file or db
    List<CrimePoint> crimePoints = mapDao.readUnfilteredData();

    // Return only relevant CrimePoints
    return crimePoints;
  }

  /**
   * Reads all crime points within the provided filter parameters.
   * @param filterAttributes
   * @return List
   */
  public List<CrimePoint> readFilteredData(FilterAttributes filterAttributes) {

    // Validate the provided filter
    FilterAttributesUtil.validateFilterAttributes(filterAttributes);

    // Read data from file or db
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    // Return only relevant CrimePoints
    return crimePoints;
  }

}
