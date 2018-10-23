package com.singularities.extractor;

import com.google.common.base.Preconditions;

import java.util.Properties;

/**
 * A set of parameters used for querying and partitioning JDBC data using
 * Apache Spark.
 */
@SuppressWarnings("WeakerAccess")
public final class JdbcQuery {
  private final Properties connectionProperties;
  private final String connectionUrl;
  private final String table;
  private final String columnName;
  private final long lowerBound;
  private final long upperBound;
  private final int numPartitions;
  private final long fetchSize;

  JdbcQuery(JdbcQueryBuilder builder) {
    connectionProperties = Preconditions.checkNotNull(
        builder.connectionProperties, "ConnectionProperties");
    connectionUrl = Preconditions.checkNotNull(
        builder.connectionUrl, "ConnectionUrl");
    table = Preconditions.checkNotNull(
        builder.table, "Table");
    columnName = Preconditions.checkNotNull(
        builder.columnName, "ColumnName");
    lowerBound = builder.lowerBound;
    upperBound = builder.upperBound;
    numPartitions = builder.numPartitions;
    fetchSize = builder.fetchSize;
  }

  public static JdbcQueryBuilder newBuilder() {
    return new JdbcQueryBuilder();
  }

  public long getFetchSize() {
    return fetchSize;
  }

  public Properties getConnectionProperties() {
    return connectionProperties;
  }

  public String getConnectionUrl() {
    return connectionUrl;
  }

  public String getTable() {
    return table;
  }

  public String getColumnName() {
    return columnName;
  }

  public long getLowerBound() {
    return lowerBound;
  }

  public long getUpperBound() {
    return upperBound;
  }

  public int getNumPartitions() {
    return numPartitions;
  }
}
