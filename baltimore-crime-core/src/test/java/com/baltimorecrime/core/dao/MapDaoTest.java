package com.baltimorecrime.core.dao;

import com.baltimorecrime.core.domain.FilterAttributes;
import com.baltimorecrime.core.utils.TestUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by Scott Robinson on 6/28/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class MapDaoTest {

  @Mock SqlSession sqlSession;

  @InjectMocks
  MapDao mapDao = new MapDao();

  @Before
  public void setup() {
    // Setup Mocks
    when(sqlSession.selectList(eq("selectAll"))).thenReturn((List) TestUtils.getUnfilteredCrimePoints());
    when(sqlSession.selectList(eq("selectWithFilter"), any(FilterAttributes.class))).thenReturn((List) TestUtils.getFilteredCrimePoints());
    when(sqlSession.selectList(eq("selectByDay"), any(String.class))).thenReturn((List) TestUtils.getSingleDayCrimePoints());
  }

  @Test
  public void testReadUnfilteredData() {
    assertEquals(TestUtils.getUnfilteredCrimePoints(), mapDao.readUnfilteredData());
  }

  @Test
  public void testReadFilteredData() {
    assertEquals(TestUtils.getFilteredCrimePoints(), mapDao.readFilteredData(new FilterAttributes()));
  }

  @Test
  public void testReadDataByDay() {
    assertEquals(TestUtils.getSingleDayCrimePoints(), mapDao.readDataByDay("test"));
  }

}
