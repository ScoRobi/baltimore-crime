package com.baltimorecrime.core.domain;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

/**
 * Created by scrobinson41 on 7/18/16.
 */
@Getter @Setter
public class TimeRange {

  Time startTime;
  Time endTime;

}
