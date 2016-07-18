package com.baltimorecrime.core.utils;

import com.baltimorecrime.core.domain.TimeRange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Time;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by scrobinson41 on 7/18/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class FilterAttributesUtilTest {

  @Test
  public void buildTimeRangeWithWrappedTimes() {

    Time startTime = Time.valueOf("20:00:00");
    Time endTime = Time.valueOf("04:00:00");

    List<TimeRange> timeRanges =  FilterAttributesUtil.buildTimeRange(startTime, endTime);

    assertEquals(2, timeRanges.size());
    assertEquals(startTime, timeRanges.get(0).getStartTime());
    assertEquals(Time.valueOf("23:59:59"), timeRanges.get(0).getEndTime());
    assertEquals(Time.valueOf("00:00:00"), timeRanges.get(1).getStartTime());
    assertEquals(endTime, timeRanges.get(1).getEndTime());
  }

}
