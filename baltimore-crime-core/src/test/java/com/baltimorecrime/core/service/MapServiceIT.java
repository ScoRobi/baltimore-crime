package com.baltimorecrime.core.service;

import com.baltimorecrime.core.BaltimoreCrimeCoreApplication;
import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.District;
import com.baltimorecrime.core.domain.FilterAttributes;
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
    filterAttributes.setStartDate(Date.valueOf("2016-06-10"));
    filterAttributes.setEndDate(Date.valueOf("2016-06-18"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartDate().compareTo(crimePoint.getCrimeDate()) <= 0);
      assertTrue(filterAttributes.getEndDate().compareTo(crimePoint.getCrimeDate()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByCrimeTime() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setStartTime(Time.valueOf("00:00:00"));
    filterAttributes.setEndTime(Time.valueOf("06:00:00"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartTime().compareTo(crimePoint.getCrimeTime()) <= 0);
      assertTrue(filterAttributes.getEndTime().compareTo(crimePoint.getCrimeTime()) >= 0);
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
    filterAttributes.setStartLongitude(-76.75);
    filterAttributes.setEndLongitude(-76.60);
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

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
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartLatitude().compareTo(crimePoint.getLatitude()) <= 0);
      assertTrue(filterAttributes.getEndLatitude().compareTo(crimePoint.getLatitude()) >= 0);
    });
  }

  @Test
  public void testReadFilteredDataByMultipleFilters() {
    FilterAttributes filterAttributes = new FilterAttributes();
    filterAttributes.setStartDate(Date.valueOf("2016-06-05"));
    filterAttributes.setEndDate(Date.valueOf("2016-06-18"));
    filterAttributes.setStartTime(Time.valueOf("12:00:00"));
    filterAttributes.setEndTime(Time.valueOf("23:59:59"));
    filterAttributes.setWeapons(Arrays.asList("FIREARM", "KNIFE"));
    filterAttributes.setNeighborhoods(Arrays.asList("Canton", "Fells Point"));
    List<CrimePoint> crimePoints = mapService.readFilteredData(filterAttributes);

    assertNotNull(crimePoints);
    assertTrue(crimePoints.size() > 0);

    crimePoints.stream().forEach(crimePoint -> {
      assertTrue(filterAttributes.getStartDate().compareTo(crimePoint.getCrimeDate()) <= 0);
      assertTrue(filterAttributes.getEndDate().compareTo(crimePoint.getCrimeDate()) >= 0);
      assertTrue(filterAttributes.getStartTime().compareTo(crimePoint.getCrimeTime()) <= 0);
      assertTrue(filterAttributes.getEndTime().compareTo(crimePoint.getCrimeTime()) >= 0);
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
