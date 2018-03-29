package com.bank.coltroller;

import java.io.IOException; 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.dao.UserDAO;

@WebServlet("/account")
public class Account extends HttpServlet {
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
				UserDAO dao=new UserDAO();
				System.out.println(req.getParameter("id"));
				System.out.println(req.getParameter("amount"));
				System.out.println(req.getParameter("account_type"));
				
				int id=Integer.parseInt(req.getParameter("id"));
				double amount=Double.parseDouble(req.getParameter("amount"));
				String account_type=req.getParameter("account_type");
				dao.account(id, amount, account_type);
				dao.transaction(id,amount,"credited",amount,1);
				System.out.println("Account.doPost()");
				resp.sendRedirect("home.jsp");
	}

}
