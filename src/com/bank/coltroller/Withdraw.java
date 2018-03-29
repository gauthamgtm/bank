package com.bank.coltroller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.bank.dao.UserDAO;
import com.bank.dto.UserDTO;

/**
 * Servlet implementation class Withdraw
 */
@WebServlet("/withdraw")
public class Withdraw extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Withdraw() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at:
		// ").append(request.getContextPath());
		response.sendRedirect("withdraw.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		double amount = Double.parseDouble(request.getParameter("amount"));
		System.out.println("Deposit.doPost()");
		
		HttpSession sess=request.getSession(false);
        JSONObject obj=(JSONObject)sess.getAttribute("userdetails"); 
        UserDTO user=(UserDTO) obj.get("user"); 
        
        long id=user.getId();
        double balance=(double)obj.get("balance");
        System.out.println("Id :"+id);
        System.out.println("Balance = "+balance);
		int x = 0;
		if (amount > 0) {
			UserDAO dao = new UserDAO();
			x = dao.withdraw(id, amount, balance);
			double newBalance=balance - amount;
			obj.put("balance", newBalance);
		}
		if (x > 0)
			doGet(request, response);
	}

}
