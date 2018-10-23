package com.singularities.extractor;

import java.util.Properties;

@SuppressWarnings("WeakerAccess")
public final class JdbcQueryBuilder {
  public long fetchSize;
  Properties connectionProperties;
  String connectionUrl;
  String table;
  String columnName;
  long lowerBound;
  long upperBound;
  int numPartitions;

  public JdbcQueryBuilder setFetchSize(long fetchSize) {
    this.fetchSize = fetchSize;
    return this;
  }

  public JdbcQueryBuilder setConnectionUrl(String connectionUrl) {
    this.connectionUrl = connectionUrl;
    return this;
  }

  public JdbcQueryBuilder setTable(String table) {
    this.table = table;
    return this;
  }

  public JdbcQueryBuilder setColumnName(String columnName) {
    this.columnName = columnName;
    return this;
  }

  public JdbcQueryBuilder setLowerBound(long lowerBound) {
    this.lowerBound = lowerBound;
    return this;
  }


  public JdbcQueryBuilder setUpperBound(long upperBound) {
    this.upperBound = upperBound;
    return this;
  }

  public JdbcQueryBuilder setNumPartitions(int numPartitions) {
    this.numPartitions = numPartitions;
    return this;
  }

  public JdbcQueryBuilder setConnectionProperties(
      Properties connectionProperties
  ) {
    this.connectionProperties = connectionProperties;
    return this;
  }

  public JdbcQuery build() {
    return new JdbcQuery(this);
  }
}
