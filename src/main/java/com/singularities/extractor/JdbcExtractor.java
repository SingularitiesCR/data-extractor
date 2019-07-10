package com.singularities.extractor;

import com.google.common.base.Preconditions;
import java.util.Properties;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

/** A extractor of data stored on a JDBC supported source. */
@SuppressWarnings("WeakerAccess")
public final class JdbcExtractor {
  static final String PROPERTY_FETCH_SIZE = "fetchsize";
  static final String PROPERTY_DRIVER = "driver";
  static final String DRIVER_SQL_SERVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
  private final DataFrameReader dataFrameReader;

  public JdbcExtractor(DataFrameReader dataFrameReader) {
    this.dataFrameReader = Preconditions.checkNotNull(dataFrameReader, "DataFrameReader");
  }

  /**
   * Persists the resulting query data into an Apache Parquet file.
   *
   * @param query the JDBC query.
   * @return the extracted dataset of rows.
   */
  public Dataset<Row> extractIntoDataset(JdbcQuery query) {
    final Properties connectionProperties = query.getConnectionProperties();
    connectionProperties.put(PROPERTY_FETCH_SIZE, String.valueOf(query.getFetchSize()));
    connectionProperties.put(PROPERTY_DRIVER, DRIVER_SQL_SERVER);
    return dataFrameReader.jdbc(
        query.getConnectionUrl(),
        query.getTable(),
        query.getColumnName(),
        query.getLowerBound(),
        query.getUpperBound(),
        query.getNumPartitions(),
        connectionProperties);
  }
}
