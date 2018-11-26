package com.singularities.extractor;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

import java.util.Properties;

/**
 * A set of parameters used for querying and partitioning JDBC data using
 * Apache Spark.
 */
@SuppressWarnings("WeakerAccess")
public final class JdbcQuery {
  private final Properties connectionProperties;
  private final String table;
  private final String columnName;
  private final long lowerBound;
  private final long upperBound;
  private final int numPartitions;
  private final long fetchSize;
  private final String host;
  private final int port;
  private final String provider;

  JdbcQuery(JdbcQueryBuilder builder) {
    connectionProperties = Preconditions.checkNotNull(
        builder.connectionProperties, "ConnectionProperties");
    host = Preconditions.checkNotNull(builder.host, "Host");
    port = builder.port;
    provider = Preconditions.checkNotNull(builder.provider, "Provider");
    table = Preconditions.checkNotNull(
        builder.table, "Table");
    columnName = Preconditions.checkNotNull(
        builder.columnName, "ColumnName");
    lowerBound = builder.lowerBound;
    upperBound = builder.upperBound;
    numPartitions = builder.numPartitions;
    fetchSize = builder.fetchSize;
    validate();
  }

  public static JdbcQueryBuilder newBuilder() {
    return new JdbcQueryBuilder();
  }

  private void validate() {
    Preconditions.checkArgument(!host.isEmpty(), "empty Host");
    Preconditions.checkArgument(0 < port && port <= 65535,
        "expected 0 <= Port <= 65535, got Port={}",
        port);
    Preconditions.checkArgument(!table.isEmpty(), "empty Table");
    Preconditions.checkArgument(!columnName.isEmpty(), "empty ColumnName");
    Preconditions.checkArgument(lowerBound < upperBound,
        "expected LowerBound < UpperBound, got {} < {}",
        lowerBound, upperBound);
    Preconditions.checkArgument(0 < numPartitions,
        "expected 0 < NumPartitions, got NumPartitions={}",
        numPartitions);
    Preconditions.checkArgument(0 <= fetchSize,
        "expected 0 < FetchSize, got FetchSize={}",
        fetchSize);
  }

  public long getFetchSize() {
    return fetchSize;
  }

  public Properties getConnectionProperties() {
    return connectionProperties;
  }

  public String getConnectionUrl() {
    return String.format("jdbc:%s://%s:%d", provider, host, port);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    JdbcQuery jdbcQuery = (JdbcQuery) o;
    return lowerBound == jdbcQuery.lowerBound &&
        upperBound == jdbcQuery.upperBound &&
        numPartitions == jdbcQuery.numPartitions &&
        fetchSize == jdbcQuery.fetchSize &&
        port == jdbcQuery.port &&
        Objects.equal(connectionProperties, jdbcQuery.connectionProperties) &&
        Objects.equal(table, jdbcQuery.table) &&
        Objects.equal(columnName, jdbcQuery.columnName) &&
        Objects.equal(host, jdbcQuery.host) &&
        Objects.equal(provider, jdbcQuery.provider);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(connectionProperties, table, columnName,
        lowerBound, upperBound, numPartitions, fetchSize, host, port, provider);
  }
}
