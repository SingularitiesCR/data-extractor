package com.singularities.extractor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertThrows;

class JdbcQueryTest {

  private JdbcQueryBuilder query;

  @BeforeEach
  void setUp() {
    query = JdbcQuery.newBuilder()
        .setConnectionProperties(new Properties())
        .setProvider("mssql")
        .setHost("host")
        .setPort(8080)
        .setTable("Table")
        .setColumnName("ColumnName")
        .setLowerBound(0L)
        .setUpperBound(1000L)
        .setNumPartitions(2)
        .setFetchSize(500);
  }

  @Test
  void host_null() {
    query.setHost(null);
    assertThrows(NullPointerException.class, () -> query.build());
  }

  @Test
  void host_empty() {
    query.setHost("");
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }

  @Test
  void port_greaterThan65535() {
    query.setPort(66000);
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }

  @Test
  void port_zeroOrLess() {
    query.setPort(0);
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }

  @Test
  void table_null() {
    query.setTable(null);
    assertThrows(NullPointerException.class, () -> query.build());
  }

  @Test
  void table_empty() {
    query.setTable("");
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }

  @Test
  void columnName_null() {
    query.setColumnName(null);
    assertThrows(NullPointerException.class, () -> query.build());
  }

  @Test
  void columnName_empty() {
    query.setColumnName("");
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }

  @Test
  void lowerBound_greaterThanUpperBound() {
    query.setLowerBound(2);
    query.setUpperBound(1);
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }

  @Test
  void numPartitions_zeroOrLess() {
    query.setNumPartitions(0);
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }

  @Test
  void fetchSize_negative() {
    query.setFetchSize(-1);
    assertThrows(IllegalArgumentException.class, () -> query.build());
  }
}