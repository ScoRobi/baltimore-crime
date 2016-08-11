package com.baltimorecrime.core.resource;

import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by scrobinson41 on 7/12/16.
 */
public class MapResourceIT {

  @Test
  public void requestDataGetWithParams() {
    given().contentType("application/json").with()
        .params("weapons", "knife").params("startDate", "2016-6-1").params("endDate", "2016-6-1")
        .expect().statusCode(200)
        .body(equalTo("[{\"crimeDate\":\"2016-06-01\",\"crimeTime\":\"01:55:00\",\"crimeCode\":\"4B\",\"location\":\"400 E 28TH ST\",\"weapon\":\"KNIFE\",\"post\":513,\"district\":\"NORTHERN\",\"neighborhood\":\"Harwood\",\"longitude\":-76.61064,\"latitude\":39.32218},{\"crimeDate\":\"2016-06-01\",\"crimeTime\":\"18:24:00\",\"crimeCode\":\"3AK\",\"location\":\"3400 GOUGH ST\",\"weapon\":\"KNIFE\",\"post\":231,\"district\":\"SOUTHEASTERN\",\"neighborhood\":\"Highlandtown\",\"longitude\":-76.56862,\"latitude\":39.2886},{\"crimeDate\":\"2016-06-01\",\"crimeTime\":\"15:10:00\",\"crimeCode\":\"4B\",\"location\":\"400 S CONKLING ST\",\"weapon\":\"KNIFE\",\"post\":231,\"district\":\"SOUTHEASTERN\",\"neighborhood\":\"Highlandtown\",\"longitude\":-76.56714,\"latitude\":39.28702},{\"crimeDate\":\"2016-06-01\",\"crimeTime\":\"20:28:00\",\"crimeCode\":\"3AK\",\"location\":\"CALVERT ST & E BIDDLE ST\",\"weapon\":\"KNIFE\",\"post\":141,\"district\":\"CENTRAL\",\"neighborhood\":\"Mid-Town Belvedere\",\"longitude\":-76.61328,\"latitude\":39.30355}]"))
        .when().get("/map/data");
  }

  @Test
  public void requestDataGetInvalidParam() {
    given().contentType("application/json").with().params("weapons", "invalid")
        .expect().statusCode(200).body(equalTo("[]")).when().get("/map/data");
  }

  @Test
  public void requestDataPostWithParams() {
    given().contentType("application/json").with().body("{\n"
        + "  \"locations\":[\"2700 CHESLEY AVE\"],\n"
        + "  \"posts\":[424],\n"
        + "  \"weapons\":[\"HANDS\",\"FIREARM\"]\n"
        + "}")
        .expect().statusCode(200).body(equalTo("[{\"crimeDate\":\"2016-06-18\",\"crimeTime\":\"00:33:00\",\"crimeCode\":\"4E\",\"location\":\"2700 CHESLEY AVE\",\"weapon\":\"HANDS\",\"post\":424,\"district\":\"NORTHEASTERN\",\"neighborhood\":\"North Harford Road\",\"longitude\":-76.55559,\"latitude\":39.3679}]")).when().post("/map/data");
  }

  @Test
  public void requestDataPostInvalidParam() {
    given().contentType("application/json").with().body("{\n"
        + "  \"weapons\":[\"INVALID\"]\n"
        + "}")
        .expect().statusCode(200).body(equalTo("[]")).when().post("/map/data");
  }

  @Test
  public void testReadDataRanges() {
    when().get("/map/data/range").then().statusCode(200).body(
        equalTo("{\"minDate\":\"2016-06-01\",\"maxDate\":\"2016-06-18\"," +
        "\"minTime\":\"00:00:00\",\"maxTime\":\"23:59:00\"," +
        "\"minLongitude\":\"-76.7108600000000000\",\"maxLongitude\":\"-76.5213600000000000\"," +
        "\"minLatitude\":\"39.2176300000000000\",\"maxLatitude\":\"41.5286100000000000\"," +
        "\"crimeCount\":2399}"));
  }

  @Test
  public void testPing() {
    when().get("/map").then().statusCode(200).body(equalTo("Ready for service!"));
  }
}
