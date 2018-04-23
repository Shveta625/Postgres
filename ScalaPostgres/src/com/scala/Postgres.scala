package com.scala
import java.sql.{ Connection, DriverManager, ResultSet };

object Postgres {
  
  def main(args: Array[String]) {
    
    val conn_str = "jdbc:postgresql://localhost/test?user=postgres&password=passw0rd"
    val conn = DriverManager.getConnection(conn_str)
    try {
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

      val rs = statement.executeQuery("SELECT * FROM EMPLOYEES")

      while (rs.next) {
        println(rs.getString("name"))
      }
    } finally {
      conn.close
    }
  }
  
}