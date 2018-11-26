package com.singularities.extractor;

import com.google.common.io.Files;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.DataFrameReader;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

class JdbcExtractorMsSqlIT extends JdbcTest {
  private static final int MAX_INSERT_ROWS = 1000;
  private DataFrameReader mDataFrameReader;
  private SQLContext sqlContext;

  @BeforeEach
  void setUp() {
    // Spark
    SparkConf sparkConf = new SparkConf(true)
        .setMaster("local[*]")
        .setAppName(JdbcExtractorMsSqlIT.class.getSimpleName());
    SparkContext sc = SparkContext.getOrCreate(sparkConf);
    sqlContext = SQLContext.getOrCreate(sc);
    mDataFrameReader = spy(sqlContext.read());
  }

  @Test
  void extractData() throws SQLException {
    // Params
    final Properties connectionProperties = new Properties();
    connectionProperties.put("databaseName", "TestDB");
    connectionProperties.put("user", getUser());
    connectionProperties.put("password", getPassword());
    connectionProperties.put("queryTimeout", "120");
    connectionProperties.put("socketTimeout", "60000");
    connectionProperties.put("lockTimeout", "60000");
    JdbcQuery pJdbcQuery = JdbcQuery.newBuilder()
        .setHost(getHost())
        .setPort(getPort())
        .setProvider("sqlserver")
        .setTable("Person")
        .setColumnName("ID")
        .setLowerBound(0L)
        .setUpperBound(999999L)
        .setNumPartitions(4)
        .setConnectionProperties(connectionProperties)
        .setFetchSize(1000L)
        .build();
    String pParquetUrl = String.format("file://%s/Person.parquet",
        Files.createTempDir().getPath());
    // Expected
    final Properties eConnectionProperties =
        pJdbcQuery.getConnectionProperties();
    eConnectionProperties.put(JdbcExtractor.JDBC_FETCH_SIZE, 1000);
    long eTotalRows = MAX_INSERT_ROWS * 1000;
    String[] eColumns = new String[]{"ID", "FirstName", "LastName", "Age"};
    String eRow = "FirstName,LastName,18";
    // Server
    executeSql("CREATE TABLE Person " +
        "(ID INTEGER NOT NULL IDENTITY(1,1) PRIMARY KEY, " +
        " FirstName VARCHAR(255), " +
        " LastName VARCHAR(255), " +
        " Age INTEGER, " +
        " )");
    List<String> persons = new ArrayList<>(MAX_INSERT_ROWS);
    for (int i = 0; i < eTotalRows; i += MAX_INSERT_ROWS) {
      for (int j = 0; j < MAX_INSERT_ROWS; j++) {
        persons.add("('FirstName', 'LastName', '18')");
      }
      executeSql("INSERT INTO Person (FirstName, LastName, Age) VALUES " +
          String.join(",", persons) + ";");
      persons.clear();
    }
    // Instance
    final JdbcExtractor extractor = new JdbcExtractor(mDataFrameReader);
    // Call
    final Dataset<Row> aParquet = extractor.extractIntoParquet(pJdbcQuery, pParquetUrl);
    final Dataset<Row> eParquet = sqlContext.read().parquet(pParquetUrl);
    // Assertions
    verify(mDataFrameReader).jdbc(pJdbcQuery.getConnectionUrl(),
        pJdbcQuery.getTable(), pJdbcQuery.getColumnName(),
        pJdbcQuery.getLowerBound(), pJdbcQuery.getUpperBound(),
        pJdbcQuery.getNumPartitions(), eConnectionProperties);
    assertEquals(eParquet, aParquet);
    assertEquals(eTotalRows, aParquet.count());
    assertArrayEquals(eColumns, aParquet.columns());
    assertEquals(eRow, aParquet.select("FirstName", "LastName", "Age")
        .toJavaRDD().first().mkString(","));
    verifyNoMoreInteractions(mDataFrameReader);
  }
}
