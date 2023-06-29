package com.sai4.bankapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnection {

	
	public  static Connection con;
	static
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bankapp","root","Sai@1128");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public static Connection getConnect()
	{
		return con;
	}

	
}
