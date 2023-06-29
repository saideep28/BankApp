package com.sai4.bankapp;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class BankApplication {
    Scanner s ;
	public static void main(String[] args) {
		BankApplication b = new BankApplication();
		b.startBankApp();
	}
 
	void startBankApp()
	{
		System.out.println("\n================================");
		System.out.println("1. Add Account");
		System.out.println("2. Fund Transfer");
		System.out.println("3. Mini Statement");
		System.out.println("4. Exit");
		
		System.out.println("\nSelect Any One Option");
		System.out.println("================================");
		getUserInput();
	}
      void getUserInput ()
      {
    	  s = new Scanner(System.in);
    	  System.out.println("Enter the number as per your requirement ");
    	  int userinput = s.nextInt();
    	  if(userinput==1)
    	  {
    		  addAccount();
    	  }
    	  else if (userinput==2)
    	  {
    		  fundTransfer();
    	  }
    	  else if (userinput==3)
    	  {
    		  miniStatement();
    	  }
    	  else 
    	  {
    		  System.out.println("Bank App CLosed");
  			   System.exit(0);
    	  }
      }
      
      void addAccount ()
      {
    	  System.out.println("Enter id :");
    	  int id = s.nextInt ();
    	  
    	  System.out.println("Enter name : ");
    	  String  name = s.next();
    	  
    	  System.out.println("Enter email :");
    	  String email = s.next ();
    	  
    	  System.out.println("enter phone_no");
    	  String phno = s.next();
    	  
    	  System.out.println("enter account no :");
    	  int accno = s.nextInt ();
    	  Connection con = null ; 
    	  try 
    	  {
    		    
    		  con=DbConnection.getConnect();
  			
  			con.setAutoCommit(false);
  			//-----------creating account----------------
  			PreparedStatement ps1= con.prepareStatement("insert into users values(?,?,?,?,?)");
  			
  			ps1.setInt(1, id);
  			ps1.setString(2, name);
  			ps1.setString(3, email);
  			ps1.setString(4, phno);
  			ps1.setInt(5, accno);
  			
  			int rowCount1=ps1.executeUpdate();
  			//-------------------------------------------
  			
  			//----------------deposit money in account----------------
  			PreparedStatement ps2=con.prepareStatement("insert into total_amount values(?,?,?)");
  			
  			ps2.setInt(1, id);
  			ps2.setInt(2, accno);
  			ps2.setInt(3, 50000);
  			
  			int rowCount2=ps2.executeUpdate();
  			//--------------------------------------------------------
  			
  			if(rowCount1>0 && rowCount2>0)
  			{
  				con.commit();
  				System.out.println("Account created successfully");
  			}
  			else
  			{
  				con.rollback();
  				System.out.println("Account creation failed due to some error");
  			}
  		}
  		catch(Exception e)
  		{
  			try
  			{
  				con.rollback();
  			}
  			catch(Exception ee)
  			{
  				System.out.println(ee);
  			}
  			System.out.println(e);
  		}
    	  
    	  startBankApp();
    	  
    	  
      }
      
		void fundTransfer()
		{
			System.out.println("Enter from account number ");
		    int from_accno = s.nextInt();
		    System.out.println("Enter to account number");
		    int to_accno = s.nextInt ();
		    System.out.println("Enter deposit amount ");
		    int deposit_amount = s.nextInt();
            int from_bal = 0 , to_bal = 0;		    
		    Connection con = null ; 
		    
		    try
			{
				con=DbConnection.getConnect();
				
				con.setAutoCommit(false);
				
				PreparedStatement ps=con.prepareStatement("select balance from total_amount where account_no=?");
				ps.setInt(1, from_accno);
				
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					from_bal=rs.getInt(1);
				}
				
				PreparedStatement ps1=con.prepareStatement("select balance from total_amount where account_no=?");
				ps1.setInt(1, to_accno);
				
				ResultSet rs1=ps1.executeQuery();
				while(rs1.next())
				{
					to_bal=rs1.getInt(1);
				}
				
				int new_from_bal=from_bal-deposit_amount;
				int new_to_bal=to_bal+deposit_amount;
				
				PreparedStatement ps2=con.prepareStatement("update total_amount set balance=? where account_no=?");
				ps2.setInt(1, new_from_bal);
				ps2.setInt(2, from_accno);
				int rowCount1=ps2.executeUpdate();
				
				PreparedStatement ps3=con.prepareStatement("update total_amount set balance=? where account_no=?");
				ps3.setInt(1, new_to_bal);
				ps3.setInt(2, to_accno);
				int rowCount2=ps3.executeUpdate();
				
				//--------get system current date & time-------------
				Date d=new Date();
				
				SimpleDateFormat sdf1=new SimpleDateFormat("dd/MM/yyyy");
				String date1=sdf1.format(d);
				
				SimpleDateFormat sdf2=new SimpleDateFormat("HH:mm:ss");
				String time1=sdf2.format(d);
				//--------------------------------------------------
				
				PreparedStatement ps4=con.prepareStatement("insert into min_statement values(?,?,?,?,?)");
				ps4.setInt(1, from_accno);
				ps4.setInt(2, deposit_amount);
				ps4.setString(3, "d");
				ps4.setString(4, date1);
				ps4.setString(5, time1);
				int rowCount3=ps4.executeUpdate();
				
				PreparedStatement ps5=con.prepareStatement("insert into min_statement values(?,?,?,?,?)");
				ps5.setInt(1, to_accno);
				ps5.setInt(2, deposit_amount);
				ps5.setString(3, "c");
				ps5.setString(4, date1);
				ps5.setString(5, time1);
				int rowCount4=ps5.executeUpdate();
				
				if(rowCount1>0 && rowCount2>0 && rowCount3>0 && rowCount4>0)
				{
					con.commit();
					System.out.println("Amount deposit successfully");
				}
				else
				{
					con.rollback();
					System.out.println("Transaction failed");
				}
			}
			catch(Exception e)
			{
				try
				{
					con.rollback();
				}
				catch(Exception ee)
				{
					System.out.println(ee);
				}
				System.out.println(e);
			}
			
			startBankApp();
		}
		void miniStatement() 
		{   
			StringBuffer mini_details = new StringBuffer() ;
		    
			Connection con = null ; 
			System.out.println("Enter the account no ");
			int acc_no = s.nextInt();
			mini_details.append("Below is the transaction details for '"+acc_no+"' account no\n\n");
			try
			{
				con = DbConnection.getConnect();
				PreparedStatement ps =con. prepareStatement("Select * from min_statement where account_no=?");
			    ps.setInt(1, acc_no);
			    ResultSet rs   =ps.executeQuery();
			    while (rs.next())
			    {
			       System.out.print(rs.getString(2));
			       mini_details.append(rs.getInt(2));
			       
			       System.out.print("  " + rs.getString(3));
			       mini_details.append(rs.getString(3));
			       
			       System.out.print("  " + rs.getString(4));
			       mini_details.append(rs.getString(4));
			       
			       System.out.print("  " + rs.getString(5));
			       mini_details.append(rs.getString(5));
			       
			       System.out.println();
			       mini_details.append("/n");
			       
			       generateMiniStatementFile(mini_details, acc_no);
			    }
			}
			catch(Exception e )
			{
				System.out.println(e);
			}
		}
		public void generateMiniStatementFile(StringBuffer details, int accountno)
		{
			String st = details.toString();
			
			try
			{
				FileOutputStream fos=new FileOutputStream("c:/"+accountno+".txt");
				fos.write(st.getBytes());
				fos.close();
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
		}
}