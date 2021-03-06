package com.singularities.extractor;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class JdbcTest {
  private static Connection connection;
  private static String user;
  private static String password;
  private static String host;
  private static int port;
  private static String connectionUrl;

  static String getHost() {
    return host;
  }

  static int getPort() {
    return port;
  }

  static String getUser() {
    return user;
  }

  static String getPassword() {
    return password;
  }

  @BeforeAll
  static void setUpAll() throws SQLException {
    host = Preconditions.checkNotNull(
        System.getenv("DATA_EXTRACTOR_MSSQL_HOST"),
        "DATA_EXTRACTOR_MSSQL_HOST");
    port = Integer.valueOf(Preconditions.checkNotNull(
        System.getenv("DATA_EXTRACTOR_MSSQL_PORT"),
        "DATA_EXTRACTOR_MSSQL_PORT"));
    user = Preconditions.checkNotNull(
        System.getenv("DATA_EXTRACTOR_MSSQL_USER"),
        "DATA_EXTRACTOR_MSSQL_USER");
    password = Preconditions.checkNotNull(
        System.getenv("DATA_EXTRACTOR_MSSQL_PASSWORD"),
        "DATA_EXTRACTOR_MSSQL_PASSWORD");
    connectionUrl = String.format(
        "jdbc:sqlserver://%s:%s;"
            + "user=%s;"
            + "password=%s;",
        host,
        port,
        user,
        password);
    connection = DriverManager.getConnection(connectionUrl);
    connection.createStatement()
        .executeUpdate("CREATE DATABASE TestDB");
    connection.close();
    connection = DriverManager.getConnection(connectionUrl + "database=TestDB;");
  }

  @AfterAll
  static void tearDownAll() throws SQLException {
    connection.close();
    connection = DriverManager.getConnection(connectionUrl);
    connection.createStatement().executeUpdate("DROP DATABASE TestDB");
    connection.close();
  }

  void executeSql(String sql) throws SQLException {
    Statement statement = connection.createStatement();
    statement.executeUpdate(sql);
  }
}
