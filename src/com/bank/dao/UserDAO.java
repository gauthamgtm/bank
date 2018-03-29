package com.bank.dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.bank.coltroller.Login;
import com.bank.dto.TransactionDTO;
import com.bank.dto.UserDTO;
import com.bank.services.Mailer;
import com.bank.services.SingleTon;

public class UserDAO {
	private static org.apache.log4j.Logger log = Logger.getLogger(UserDAO.class);
	Connection con = SingleTon.getSingleTonObject().getConnection();

	public JSONObject login(String email, String pwd) {
		long id = 0;
		PreparedStatement pstmt;
		JSONObject obj = new JSONObject();
		UserDTO userDTO = new UserDTO();
		
		try {
			pstmt = con.prepareStatement("select * from users where  email=? AND password=?");
			pstmt.setString(1, email);
			pstmt.setString(2, pwd);
			
			ResultSet rs = pstmt.executeQuery();
			log.info("select * from users where  email=? AND password=?");
			if (rs.isBeforeFirst()) {
				if (rs.next()) {
					id = rs.getInt("id");
					userDTO.setId(rs.getInt("id"));
					userDTO.setFirstName(rs.getString("first_name"));
					userDTO.setEmail(rs.getString("email"));
					userDTO.setActive(rs.getInt("active"));
					userDTO.setLastLogin(rs.getString("last_login"));
					String lastLogin = LocalDateTime.now().toString();
					pstmt = con.prepareStatement("update users set last_login=? where id=?");
					pstmt.setString(1, lastLogin);
					pstmt.setLong(2, id);
					pstmt.executeUpdate();
					log.info("update users set last_login=? where id=?");
					obj.put("user", userDTO);
				} else {
					return null;
				} 
			}else{
				return null;
			}
			if (id > 0) {
				pstmt = con.prepareStatement("select * from account where id=?");
				pstmt.setLong(1, id);
				rs=pstmt.executeQuery();
				if (rs.next()) {
					obj.put("balance", rs.getDouble("AMOUNT"));
					obj.put("type", rs.getString("account_type"));
				}
			}

			/*
			 * for (Object key : obj.keySet()) { //based on you key types String
			 * keyStr = (String)key; Object keyvalue = obj.get(keyStr);
			 * 
			 * //Print key and value System.out.println("key: "+ keyStr +
			 * " value: " + keyvalue);
			 * 
			 * //for nested objects iteration if required if (keyvalue
			 * instanceof UserDTO){ UserDTO newDto=(UserDTO) keyvalue;
			 * System.out.println("Inside instanceOf");
			 * System.out.println(newDto); }else{
			 * System.out.println("Try something else idiot"); } }
			 */

			return obj;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean register(UserDTO userDTO) {
		Statement stmt = null;
		try {

			PreparedStatement pstmt;
			String query = "insert into users(id,first_name,email,password,mobile) values (Seq_user_id.nextval,?,?,?,?)";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, userDTO.getFirstName());
			pstmt.setString(2, userDTO.getEmail());
			pstmt.setString(3, userDTO.getPassword());
			pstmt.setString(4, userDTO.getMobile());
			// boolean val = pstmt.execute();
			int val = pstmt.executeUpdate();

			System.out.println("value= " + val);
			if (val >= 1) {
				String to = userDTO.getEmail();
				String subject = "Your Registration Successful";
				String message = "Greeting.\n\n \t Thank you for registering to the World Bank.\n \n regards\nTeam World Bank";

				// Mailer.send(to, subject, message);
				return true;
			}

			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void account(int id, double amount, String account_type) {
		String sql = "insert into account(id,amount,account_type) values(?,?,?)";
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setDouble(2, amount);
			pstmt.setString(3, account_type);
			int a = pstmt.executeUpdate();
			System.out.println(a);
			if (a > 0) {
				String sql1 = "Update users set active=1 where id=" + id;
				PreparedStatement pstmt1 = con.prepareStatement(sql1);
				pstmt1.executeUpdate();
			} else {
				System.out.println("No records inserted...");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<TransactionDTO> getAllTransactionDetails(long id) {
		
		String sql = "select * from transaction where id=? order by t_id desc";
		ArrayList<TransactionDTO> transactions=new ArrayList<>();
		
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				
				TransactionDTO tranaction=new TransactionDTO();
				
				tranaction.setTransactionId(rs.getLong("t_id"));
				tranaction.setDate(rs.getString("CREATED_ON"));
				tranaction.setTranscactionAmount(rs.getDouble("TRANSACTION_AMOUNT"));
				tranaction.setTranscationType(rs.getString("TRANSACTION_TYPE"));
				tranaction.setBalance(rs.getDouble("BALANCE"));
				
				transactions.add(tranaction);
			}
			return transactions;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public int transaction(int id, double transaction_amount, String transaction_type, double amount, int flag) {
		String sql = "insert into transaction(t_id,id,transaction_amount,balance,transaction_type) values(Seq_transaction.nextval,?,?,?,?)";
		try {
			double balance = 0.0;
			if (flag == 1) { // For first transaction
				balance = amount;
			} else {
				if (transaction_type.equalsIgnoreCase("credited")) {
					balance = amount + transaction_amount;
				} else {
					balance = amount - transaction_amount;
				}
			}

			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, id);
			pstmt.setDouble(2, transaction_amount);
			pstmt.setDouble(3, balance);
			pstmt.setString(4, transaction_type);

			int a = pstmt.executeUpdate();
			System.out.println(a);
			return a;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int deposit(long id, double amount, double balance) {
		
		int x = transaction((int)id, amount, "credited", balance, 0);
		
		String sql="";
		PreparedStatement pstmt;
		double newBalance=balance+amount;
		try {
//			sql = "insert into transaction(id,transaction_amount,balance,transaction_type) values(?,?,?,?)";
//			pstmt = con.prepareStatement(sql);
//			newBalance=balance+amount;
//			pstmt.setLong(1, id);
//			pstmt.setDouble(2, amount);
//			pstmt.setDouble(3, newBalance);
//			pstmt.setString(4, "credited");
//			x=pstmt.executeUpdate();
			if(x>0){
				sql="update account set amount=? where id=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setDouble(1, newBalance);
				pstmt.setDouble(2, id);
				pstmt.executeUpdate();
			}
			return x;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int withdraw(long id, double amount, double balance) {
		PreparedStatement pstmt;
		double newBalance=balance-amount;
		int x=transaction((int)id, amount, "debited", balance, 0);
		try {
			String sql = "insert into transaction(id,transaction_amount,balance,transaction_type) values(?,?,?,?)";
//			pstmt = con.prepareStatement(sql);
//			
//			pstmt.setLong(1, id);
//			pstmt.setDouble(2, amount);
//			pstmt.setDouble(3, newBalance);
//			pstmt.setString(4, "debited");
//			int x=pstmt.executeUpdate();
			if(x>0){
				sql="update account set amount=? where id=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setDouble(1, newBalance);
				pstmt.setDouble(2, id);
				pstmt.executeUpdate();

			}
			return x;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
}
