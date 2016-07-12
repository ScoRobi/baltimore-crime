package com.baltimorecrime.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by Scott Robinson on 6/28/16.
 */
@Getter @Setter
public class CrimePoint {

  Date crimeDate;
  Time crimeTime;
  String crimeCode;
  String location;
  String weapon;
  Integer post;
  District district;
  String neighborhood;
  Double longitude;
  Double latitude;

}
