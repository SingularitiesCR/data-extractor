package com.singularities.extractor;

import com.google.common.base.Preconditions;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.Properties;

/**
 * A extractor of data stored on a JDBC supported source.
 */
@SuppressWarnings("WeakerAccess")
public final class JdbcExtractor {
  static final String JDBC_FETCH_SIZE = "fetchsize";
  private final DataFrameReader dataFrameReader;

  public JdbcExtractor(DataFrameReader dataFrameReader) {
    this.dataFrameReader = Preconditions.checkNotNull(
        dataFrameReader, "DataFrameReader");
  }

  /**
   * Persists the resulting query data into an Apache Parquet file.
   *
   * @param query      the JDBC query.
   * @param parquetUrl the URL of the Parquet file.
   */
  public void extractIntoParquet(JdbcQuery query, String parquetUrl) {
    final Properties connectionProperties = query.getConnectionProperties();
    connectionProperties.put(JDBC_FETCH_SIZE, String.valueOf(query.getFetchSize()));
    final Dataset<Row> rows = dataFrameReader.jdbc(
        query.getConnectionUrl(),
        query.getTable(),
        query.getColumnName(),
        query.getLowerBound(),
        query.getUpperBound(),
        query.getNumPartitions(),
        connectionProperties);
    rows.write().parquet(parquetUrl);
  }
}
