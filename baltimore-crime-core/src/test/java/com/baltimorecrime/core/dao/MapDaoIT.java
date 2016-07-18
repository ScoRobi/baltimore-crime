package com.baltimorecrime.core.dao;

import com.baltimorecrime.core.BaltimoreCrimeCoreApplication;
import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.District;
import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.domain.TimeRange;
import com.baltimorecrime.core.utils.FilterAttributesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Scott Robinson on 7/7/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BaltimoreCrimeCoreApplication.class)
public class MapDaoIT {

  @Autowired
  MapDao mapDao;

  @Test
  public void testReadUnfilteredData() {
    List<CrimePoint> crimePoints = mapDao.readUnfilteredData();
    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);
  }

  @Test
  public void testReadDataByDay() {
    String crimeDate = "2016-06-18";
    List<CrimePoint> crimePoints = mapDao.readDataByDay(crimeDate);
    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() == 80); // Manually checked to be 80 on 2016-06-18
  }

  @Test
  public void testReadFilteredDataByCrimeDate() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setStartDate(Date.valueOf("2016-06-10"));
    filterAttributes.setEndDate(Date.valueOf("2016-06-18"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartDate().compareTo(crimePoint.getCrimeDate()) <= 0);
      assertTrue(filterAttributes.getEndDate().compareTo(crimePoint.getCrimeDate()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByCrimeDateSameDate() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setStartDate(Date.valueOf("2016-06-18"));
    filterAttributes.setEndDate(Date.valueOf("2016-06-18"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() == 80); // Manually checked to be 80 on 2016-06-18

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartDate().compareTo(crimePoint.getCrimeDate()) <= 0);
      assertTrue(filterAttributes.getEndDate().compareTo(crimePoint.getCrimeDate()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByCrimeTime() {
    TimeRange timeRange = new TimeRange();
    timeRange.setStartTime(Time.valueOf("00:00:00"));
    timeRange.setEndTime(Time.valueOf("06:00:00"));

    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setTimeRanges(Arrays.asList(timeRange));

    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getTimeRanges().get(0).getStartTime().compareTo(crimePoint.getCrimeTime()) <= 0);
      assertTrue(filterAttributes.getTimeRanges().get(0).getEndTime().compareTo(crimePoint.getCrimeTime()) >= 0);
    });

  }

  @Test
  public void testReadFilteredDataByWrappedCrimeTime() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setTimeRanges(FilterAttributesUtil.buildTimeRange(Time.valueOf("20:00:00"), Time.valueOf("04:00:00")));

    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(
          (filterAttributes.getTimeRanges().get(0).getStartTime().compareTo(crimePoint.getCrimeTime()) <= 0 &&
            Time.valueOf("23:59:59").compareTo(crimePoint.getCrimeTime()) >= 0) ||
          (filterAttributes.getTimeRanges().get(0).getEndTime().compareTo(crimePoint.getCrimeTime()) >= 0 &&
            Time.valueOf("00:00:00").compareTo(crimePoint.getCrimeTime()) <= 0));
    });
  }

  @Test
  public void testReadFilteredDataByCrimeTimeAllTimes() {
    TimeRange timeRange = new TimeRange();
    timeRange.setStartTime(Time.valueOf("00:00:00"));
    timeRange.setEndTime(Time.valueOf("23:59:59"));

    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setTimeRanges(Arrays.asList(timeRange));

    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getTimeRanges().get(0).getStartTime().compareTo(crimePoint.getCrimeTime()) <= 0);
      assertTrue(filterAttributes.getTimeRanges().get(0).getEndTime().compareTo(crimePoint.getCrimeTime()) >= 0);
    });

    List<CrimePoint> allCrimePoints = mapDao.readUnfilteredData();
    assertNotNull(allCrimePoints);

    assertEquals(allCrimePoints.size(), crimePoints.size());
  }

  @Test
  public void testReadFilteredDataByCrimeCodes() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setCrimeCodes(Arrays.asList("4C", "6C"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getCrimeCodes().contains(crimePoint.getCrimeCode())));
  }

  @Test
  public void testReadFilteredDataByLocations() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setLocations(Arrays.asList("2700 CHESLEY AVE", "2700 FAIT AVE"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getLocations().contains(crimePoint.getLocation())));
  }

  @Test
  public void testReadFilteredDataByWeapons() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setWeapons(Arrays.asList("FIREARM", "KNIFE"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getWeapons().contains(crimePoint.getWeapon())));
  }

  @Test
  public void testReadFilteredDataByPosts() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setPosts(Arrays.asList(421, 221, 321, 111));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getPosts().contains(crimePoint.getPost())));
  }

  @Test
  public void testReadFilteredDataByDistricts() {
    List<District> districts = Arrays.asList(District.NORTHEASTERN, District.SOUTHEASTERN);
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setDistricts(districts.stream().map(district -> district.name()).collect(
        Collectors.toList()));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(districts.contains(crimePoint.getDistrict())));
  }

  @Test
  public void testReadFilteredDataByNeighborhoods() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setNeighborhoods(Arrays.asList("Canton", "Fells Point"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getNeighborhoods().contains(crimePoint.getNeighborhood())));
  }

  @Test
  public void testReadFilteredDataByLongitude() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setStartLongitude(-76.75);
    filterAttributes.setEndLongitude(-76.60);
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartLongitude().compareTo(crimePoint.getLongitude()) <= 0);
      assertTrue(filterAttributes.getEndLongitude().compareTo(crimePoint.getLongitude()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByLatitude() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setStartLatitude(39.30);
    filterAttributes.setEndLatitude(39.36);
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartLatitude().compareTo(crimePoint.getLatitude()) <= 0);
      assertTrue(filterAttributes.getEndLatitude().compareTo(crimePoint.getLatitude()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByMultipleFilters() {
    TimeRange timeRange = new TimeRange();
    timeRange.setStartTime(Time.valueOf("00:00:00"));
    timeRange.setEndTime(Time.valueOf("06:00:00"));

    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setTimeRanges(Arrays.asList(timeRange));
    filterAttributes.setStartDate(Date.valueOf("2016-06-05"));
    filterAttributes.setEndDate(Date.valueOf("2016-06-18"));
    filterAttributes.setWeapons(Arrays.asList("FIREARM", "KNIFE"));
    filterAttributes.setNeighborhoods(Arrays.asList("Canton", "Fells Point"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartDate().compareTo(crimePoint.getCrimeDate()) <= 0);
      assertTrue(filterAttributes.getEndDate().compareTo(crimePoint.getCrimeDate()) >= 0);
      assertTrue(filterAttributes.getTimeRanges().get(0).getStartTime().compareTo(crimePoint.getCrimeTime()) <= 0);
      assertTrue(filterAttributes.getTimeRanges().get(0).getEndTime().compareTo(crimePoint.getCrimeTime()) >= 0);
      assertTrue(filterAttributes.getWeapons().contains(crimePoint.getWeapon()));
      assertTrue(filterAttributes.getNeighborhoods().contains(crimePoint.getNeighborhood()));
    });
  }

  @Test
  public void testReadFilteredDataByInvalidData() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setNeighborhoods(Arrays.asList("Invalid"));
    List<CrimePoint> crimePoints = mapDao.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() == 0);
  }

  @Test
  public void testReadFilteredDataWithEmptyFilter() {
    List<CrimePoint> crimePoints1 = mapDao.readFilteredData(new FilterAttributes());
    assertNotNull(crimePoints1);
    assertTrue(crimePoints1.size() > 0);

    List<CrimePoint> crimePoints2 = mapDao.readUnfilteredData();
    assertEquals(crimePoints2.size(), crimePoints1.size());
  }

}
