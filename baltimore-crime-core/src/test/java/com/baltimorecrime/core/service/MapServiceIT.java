package com.baltimorecrime.core.service;

import com.baltimorecrime.core.BaltimoreCrimeCoreApplication;
import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.DateRange;
import com.baltimorecrime.core.domain.District;
import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.domain.LatitudeRange;
import com.baltimorecrime.core.domain.LongitudeRange;
import com.baltimorecrime.core.domain.TimeRange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.NotFoundException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Scott Robinson on 7/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BaltimoreCrimeCoreApplication.class)
public class MapServiceIT {

  @Autowired
  MapService mapService;

  @Test
  public void testReadUnfilteredData() {
    List<CrimePoint> crimePoints = mapService.readUnfilteredData();
    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);
  }

  @Test
  public void testReadFilteredDataByCrimeDate() {
    FilterAttributes filterAttributes = new FilterAttributes();
    DateRange dateRange = new DateRange();
    dateRange.setStartDate(Date.valueOf("2016-06-10"));
    dateRange.setEndDate(Date.valueOf("2016-06-18"));
    filterAttributes.setDateRanges(Arrays.asList(dateRange));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getDateRanges().get(0).getStartDate().compareTo(crimePoint.getCrimeDate()) <= 0);
      assertTrue(filterAttributes.getDateRanges().get(0).getEndDate().compareTo(crimePoint.getCrimeDate()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByCrimeTime() {
    TimeRange timeRange = new TimeRange();
    timeRange.setStartTime(Time.valueOf("00:00:00"));
    timeRange.setEndTime(Time.valueOf("06:00:00"));

    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setTimeRanges(Arrays.asList(timeRange));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getTimeRanges().get(0).getStartTime().compareTo(crimePoint.getCrimeTime()) <= 0);
      assertTrue(filterAttributes.getTimeRanges().get(0).getEndTime().compareTo(crimePoint.getCrimeTime()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByCrimeCodes() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setCrimeCodes(Arrays.asList("4C", "6C"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getCrimeCodes().contains(crimePoint.getCrimeCode())));
  }

  @Test
  public void testReadFilteredDataByLocations() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setLocations(Arrays.asList("2700 CHESLEY AVE", "2700 FAIT AVE"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getLocations().contains(crimePoint.getLocation())));
  }

  @Test
  public void testReadFilteredDataByWeapons() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setWeapons(Arrays.asList("FIREARM", "KNIFE"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getWeapons().contains(crimePoint.getWeapon())));
  }

  @Test
  public void testReadFilteredDataByPosts() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setPosts(Arrays.asList(421, 221, 321, 111));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

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
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(districts.contains(crimePoint.getDistrict())));
  }

  @Test
  public void testReadFilteredDataByNeighborhoods() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setNeighborhoods(Arrays.asList("Canton", "Fells Point"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint ->
        assertTrue(filterAttributes.getNeighborhoods().contains(crimePoint.getNeighborhood())));
  }

  @Test
  public void testReadFilteredDataByLongitude() {
    FilterAttributes filterAttributes = new FilterAttributes();
    LongitudeRange longitudeRange = new LongitudeRange();
    longitudeRange.setStartLongitude(-76.75);
    longitudeRange.setEndLongitude(-76.60);
    filterAttributes.setLongitudeRanges(Arrays.asList(longitudeRange));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getLongitudeRanges().get(0).getStartLongitude().compareTo(crimePoint.getLongitude()) <= 0);
      assertTrue(filterAttributes.getLongitudeRanges().get(0).getEndLongitude().compareTo(crimePoint.getLongitude()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByLatitude() {
    FilterAttributes filterAttributes = new FilterAttributes();
    LatitudeRange latitudeRange = new LatitudeRange();
    latitudeRange.setStartLatitude(39.30);
    latitudeRange.setEndLatitude(39.36);
    filterAttributes.setLatitudeRanges(Arrays.asList(latitudeRange));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getLatitudeRanges().get(0).getStartLatitude().compareTo(crimePoint.getLatitude()) <= 0);
      assertTrue(filterAttributes.getLatitudeRanges().get(0).getEndLatitude().compareTo(crimePoint.getLatitude()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByMultipleFilters() {
    TimeRange timeRange = new TimeRange();
    timeRange.setStartTime(Time.valueOf("12:00:00"));
    timeRange.setEndTime(Time.valueOf("23:59:59"));

    DateRange dateRange = new DateRange();
    dateRange.setStartDate(Date.valueOf("2016-06-05"));
    dateRange.setEndDate(Date.valueOf("2016-06-18"));

    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setTimeRanges(Arrays.asList(timeRange));
    filterAttributes.setDateRanges(Arrays.asList(dateRange));
    filterAttributes.setWeapons(Arrays.asList("FIREARM", "KNIFE"));
    filterAttributes.setNeighborhoods(Arrays.asList("Canton", "Fells Point"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getDateRanges().get(0).getStartDate().compareTo(crimePoint.getCrimeDate()) <= 0);
      assertTrue(filterAttributes.getDateRanges().get(0).getEndDate().compareTo(crimePoint.getCrimeDate()) >= 0);
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
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() == 0);
  }

  @Test
  public void testReadFilteredDataWithEmptyFilter() {
    List<CrimePoint> crimePoints1 = mapService.readFilteredData(new FilterAttributes());
    assertNotNull(crimePoints1);
    assertTrue(crimePoints1.size() > 0);

    List<CrimePoint> crimePoints2 = mapService.readUnfilteredData();
    assertEquals(crimePoints2.size(), crimePoints1.size());
  }

  @Test(expected = NotFoundException.class)
  public void testReadFilteredDataWithNullFilter() {
    mapService.readFilteredData(null);
  }

}
