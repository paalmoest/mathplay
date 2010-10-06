/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathplay;

import java.sql.*;
public class Cleanup {
    /** Closes a result statement */
    public static void closeResultStatement(ResultSet result) {
    try {
      if (result != null /*!res.isClosed()*/) result.close();
    } catch (SQLException e) {
      printMessage(e, "closeResultStatement");
    }
  }

  /** Closes a statement */
  public static void closeStatement(Statement statement) {
    try {
      if (statement != null && !statement.isClosed()) statement.close();
    } catch (SQLException e) {
      printMessage(e, "closeStatement()");
    }
  }

  /** Clean database connection closure */
  public static void closeConnection(Connection connection) {
    try {
      if (connection != null && !connection.isClosed()) connection.close();
    } catch (SQLException e) {
      printMessage(e, "closeConnection()");
    }
  }

  /** Roll back database changes */
  public static void rollBack(Connection connection) {
    try {
      if (connection != null && !connection.getAutoCommit()) connection.rollback();
    } catch (SQLException e) {
      printMessage(e, "rollBack()");
    }
  }

  /** Used to set auto commit */
  public static void setAutoCommit(Connection connection) {
    try {
      if (connection != null && !connection.getAutoCommit()) connection.setAutoCommit(true);
    } catch (SQLException e) {
      printMessage(e, "setAutoCommit()");
    }
  }

  /** Print messages in case of exceptions */
  public static void printMessage(Exception e, String message) {
    System.err.println("*** Error accured: " + message + ". ***");
    e.printStackTrace(System.err);
  }
}