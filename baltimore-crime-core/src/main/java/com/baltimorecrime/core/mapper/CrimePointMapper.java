package com.baltimorecrime.core.mapper;

import com.baltimorecrime.core.domain.CrimePoint;
import com.baltimorecrime.core.domain.FilterAttributes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Scott Robinson on 7/7/16.
 */
@Mapper
public interface CrimePointMapper {

  /**
   * MyBatis stub to read all crime data. See CrimePointMapper.xml for SQL statement.
   */
  List<CrimePoint> selectAll();

  /**
   * MyBatis stub to read all crime data for a given day. See CrimePointMapper.xml for SQL statement.
   */
  List<CrimePoint> selectByDay(@Param("crimeData") String crimeDate);

  /**
   * MyBatis stub to read all crime data within a provided filter. See CrimePointMapper.xml for SQL statement.
   */
  List<CrimePoint> selectWithFilter(@Param("filterAttributes") FilterAttributes filterAttributes);
}
