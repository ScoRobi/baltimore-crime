package com.baltimorecrime.core.resource;

import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.service.MapService;
import com.baltimorecrime.core.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by scrobinson41 on 7/12/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapResourceTest {

  @Mock
  MapService mapService;

  @InjectMocks
  MapResource mapResource;

  @Before
  public void setup() {
    // Setup Mocks
    when(mapService.readUnfilteredData()).thenReturn(TestUtils.getUnfilteredCrimePoints());
    when(mapService.readFilteredData(null)).thenReturn(TestUtils.getUnfilteredCrimePoints());
    when(mapService.readFilteredData(any(FilterAttributes.class))).thenReturn(TestUtils.getFilteredCrimePoints());
  }

  @Test
  public void testRequestDataPostNoFilter() {
    assertEquals(TestUtils.getUnfilteredCrimePoints(), mapResource.requestData(null).getEntity());
  }

  @Test
  public void testRequestDataPostWithFilter() {
    assertEquals(TestUtils.getFilteredCrimePoints(), mapResource.requestData(new FilterAttributes()).getEntity());
  }

  @Test
  public void testRequestDataGet() {
    assertEquals(TestUtils.getFilteredCrimePoints(), mapResource.requestData(
        "districts", "weapons", "locations", "1", "neighborhoods", "crimeCode", null,
        null, null, null, null, null, null, null).getEntity());
  }

  @Test
  public void testPing() {
    Response expectedResponse = Response.ok("Ready for service!").build();
    Response actualResponse = mapResource.ping();
    assertEquals(expectedResponse.getStatus(), actualResponse.getStatus());
    assertEquals(expectedResponse.getLength(), actualResponse.getLength());
    assertEquals(expectedResponse.hasEntity(), actualResponse.hasEntity());
    assertEquals(expectedResponse.getEntity(), actualResponse.getEntity());
  }
}
