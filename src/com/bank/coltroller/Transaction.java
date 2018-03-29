package com.bank.coltroller;

import java.io.IOException;  
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.bank.dao.UserDAO;
import com.bank.dto.TransactionDTO;
import com.bank.dto.UserDTO;

/**
 * Servlet implementation class TransactionController
 */
@WebServlet("/transaction")
public class Transaction extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		
		UserDAO dao=new UserDAO();
		HttpSession sess=request.getSession(false);
        JSONObject obj=(JSONObject)sess.getAttribute("userdetails"); 
        UserDTO user=(UserDTO) obj.get("user"); 
        
        long id=user.getId();
		ArrayList<TransactionDTO> transactions=dao.getAllTransactionDetails(id);
		
		request.setAttribute("transactions", transactions);
		RequestDispatcher rd=request.getRequestDispatcher("transaction.jsp");
		rd.forward(request, response);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		UserDAO dao=new UserDAO();
		int id=Integer.parseInt(req.getParameter("id"));
		double transaction_amount=Double.parseDouble(req.getParameter("transaction_amount"));
		String transcation_type=req.getParameter("transcation_type");
		double amount=Double.parseDouble(req.getParameter("amount"));
		
		
		
		if(transcation_type==null){
			ArrayList<TransactionDTO> transactions=dao.getAllTransactionDetails(id);
		}
		else{
			dao.transaction(id,transaction_amount,transcation_type,amount,0);
		}
		

	}

}
