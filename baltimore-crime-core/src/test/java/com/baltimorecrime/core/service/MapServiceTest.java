package com.baltimorecrime.core.service;

import com.baltimorecrime.core.dao.MapDao;
import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.NotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by scrobinson41 on 6/28/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapServiceTest {

  @Mock
  MapDao mapDao;

  @InjectMocks
  MapService mapService = new MapService();

  @Before
  public void setup() {
    // Setup Mocks
    when(mapDao.readUnfilteredData()).thenReturn(TestUtils.getUnfilteredCrimePoints());
    when(mapDao.readFilteredData(any(FilterAttributes.class))).thenReturn(TestUtils.getFilteredCrimePoints());
  }

  @Test
  public void shouldReadUnfilteredData() {
    List<CrimePoint> crimePoints = mapService.readUnfilteredData();
    assertNotNull(crimePoints);
    assertEquals(TestUtils.getUnfilteredCrimePoints(), crimePoints);
  }

  @Test
  public void shouldReadFilteredData() {
    List<CrimePoint> crimePoints = mapService.readFilteredData(new FilterAttributes());
    assertNotNull(crimePoints);
    assertEquals(TestUtils.getFilteredCrimePoints(), crimePoints);
  }

  @Test(expected = NotFoundException.class)
  public void shouldFailReadFilteredDataWithNullFilter() {
    mapService.readFilteredData(null);
  }

}
