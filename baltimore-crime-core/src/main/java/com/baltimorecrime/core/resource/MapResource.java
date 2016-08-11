package com.baltimorecrime.core.resource;

import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.DataRanges;
import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.service.MapService;
import com.baltimorecrime.core.utils.FilterAttributesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

/**
 * Created by Scott Robinson on 6/28/16.
 */
@Component
@Path("/map")
public class MapResource {

  @Autowired
  MapService mapService;

  /**
   * Returns either filtered or unfiltered crime data. Preferred endpoint to use when client is NOT
   * running in the same domain.
   *
   * @param filterAttributes
   * @return Response
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/data")
  public Response requestData(final FilterAttributes filterAttributes) {

    List<CrimePoint> crimePoints;
    if (Objects.isNull(filterAttributes)) {
      crimePoints = mapService.readUnfilteredData();
    } else {
      crimePoints = mapService.readFilteredData(filterAttributes);
    }

    return Response.ok(crimePoints)
        .header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Allow-Methods", "*")
        .header("Access-Control-Max-Age", "100000")
        .build();
  }

  /**
   * Returns either filtered or unfiltered crime data. Preferred endpoint to use when client IS
   * running in the same domain.
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
   * @return Response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/data")
  public Response requestData(
      @QueryParam("districts") String filterDistricts,
      @QueryParam("weapons") String filterWeapons,
      @QueryParam("locations") String filterLocations,
      @QueryParam("posts") String filterPosts,
      @QueryParam("neighborhoods") String filterNeighborhoods,
      @QueryParam("crimeCodes") String filterCrimeCodes,
      @QueryParam("startDate") String filterStartDate,
      @QueryParam("endDate") String filterEndDate,
      @QueryParam("startLatitude") String filterStartLatitude,
      @QueryParam("endLatitude") String filterEndLatitude,
      @QueryParam("startLongitude") String filterStartLongitude,
      @QueryParam("endLongitude") String filterEndLongitude,
      @QueryParam("startTime") String filterStartTime,
      @QueryParam("endTime") String filterEndTime) {

    FilterAttributes filterAttributes = FilterAttributesUtil.mapToFilterAttributes(
        filterDistricts, filterWeapons, filterLocations, filterPosts, filterNeighborhoods,
        filterCrimeCodes, filterStartDate, filterEndDate, filterStartLatitude, filterEndLatitude,
        filterStartLongitude, filterEndLongitude, filterStartTime, filterEndTime);

    return Response.ok(mapService.readFilteredData(filterAttributes))
        .header("Access-Control-Allow-Origin", "*")
        .header("Access-Control-Max-Age", "100000")
        .build();
  }

  /**
   * Reads all data bounds based on the data.
   *
   * @return Response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("/data/range")
  public Response readDataRanges() {
    return Response.ok(mapService.readDataRanges()).build();
  }

  /**
   * Ping endpoint used to ensure the service is available.
   *
   * @return Response
   */
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response ping() {
    return Response.ok("Ready for service!").build();
  }
}
