package com.postgres.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author postgresqltutorial.com
 */
public class PostgresJDBC {

	private final String url = "jdbc:postgresql://localhost/test";
	private final String user = "postgres";
	private final String password = "passw0rd";

	/**
	 * Connect to the PostgreSQL database
	 *
	 * @return a Connection object
	 */
	public Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}

	public void executeQuery(List<Department> depts, List<Employee> emps) {
		Connection conn = connect();
		Statement st;
		try {
			st = conn.createStatement();
			String qsDept = "CREATE TABLE IF NOT EXISTS DEPARTMENTS(ID INT PRIMARY KEY, DEPT CHAR(50) NOT NULL)";
			String qsEmp = "CREATE TABLE IF NOT EXISTS EMPLOYEES(ID INT PRIMARY KEY, NAME VARCHAR(50), DEPT INT NOT NULL)";
			String sqlDept = "INSERT INTO DEPARTMENTS(ID,DEPT) " + "VALUES(?,?)";
			String sqlEmp = "INSERT INTO EMPLOYEES(ID,NAME,DEPT) " + "VALUES(?,?,?)";

			st.execute(qsDept);
			st.execute(qsEmp);
			PreparedStatement statementDept = conn.prepareStatement(sqlDept);
			PreparedStatement statementEmp = conn.prepareStatement(sqlEmp);

			for (Department dept : depts) {
				statementDept.setInt(1, dept.getId());
				statementDept.setString(2, dept.getName());

				statementDept.addBatch();
				statementDept.executeBatch();
			}
			for (Employee emp : emps) {
				statementEmp.setInt(1, emp.getId());
				statementEmp.setString(2, emp.getName());
				statementEmp.setInt(3, emp.getDepartment().getId());

				statementEmp.addBatch();
				statementEmp.executeBatch();
			}
			
			ResultSet rsDepts = st.executeQuery("SELECT * FROM DEPARTMENTS	");
			while (rsDepts.next()) {
				System.out.print("Column 1 returned ");
				System.out.println(rsDepts.getString(2));
			}
			rsDepts.close();
			
			ResultSet rsEmps = st.executeQuery("SELECT * FROM EMPLOYEES	");
			while (rsEmps.next()) {
				System.out.print("Column 1 returned ");
				System.out.println(rsEmps.getString(2));
			}
			rsEmps.close();
			st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
		PostgresJDBC app = new PostgresJDBC();
		
		List<Department> departments=new ArrayList<Department>();
		Department dep1=new Department(101, "Teaching");
		Department dep2=new Department(102, "Non-Teaching");
		departments.add(dep1);
		departments.add(dep2);
		
		List<Employee> employees=new ArrayList<Employee>();
		employees.add(new Employee(10001, "Sia", dep1));
		employees.add(new Employee(10002, "Shveta", dep2));
		
		app.executeQuery(departments,employees);
	}
}
