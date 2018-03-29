package com.bank.services;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingleTon {
	private static final SingleTon SINGLE;
	Connection con=null;
	
	static{
		SINGLE = new SingleTon();
	}
	
	private SingleTon() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","root");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static SingleTon getSingleTonObject(){
		return SINGLE;
	}
	
	public Connection getConnection(){
		return con;
	}
	
	@Override
	protected void finalize() throws Throwable {
		con.close();
	}
}
