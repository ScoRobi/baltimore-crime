package com.baltimorecrime.core.dao;

import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.FilterAttributes;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Scott Robinson on 6/28/16.
 */
@Component
public class MapDao {

  @Autowired
  private SqlSession sqlSession;

  /**
   * Reads all crime points.
   * @return List
   */
  public List<CrimePoint> readUnfilteredData() {
    return this.sqlSession.selectList("selectAll");
  }

  /**
   * Reads all crime points within the provided filter parameters.
   * @param filterAttributes
   * @return List
   */
  public List<CrimePoint> readFilteredData(FilterAttributes filterAttributes) {
    return this.sqlSession.selectList("selectWithFilter", filterAttributes);
  }

  /**
   * Reads all crime points for a provided day.
   * @param crimeDate
   * @return List
   */
  public List<CrimePoint> readDataByDay(String crimeDate) {
    return this.sqlSession.selectList("selectByDay", crimeDate);
  }

}
