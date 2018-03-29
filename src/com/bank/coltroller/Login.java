package com.bank.coltroller;

import org.apache.log4j.Logger;


import java.io.IOException;    
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class Login
 */
@WebServlet("/login")
public class Login extends HttpServlet {
	 private static org.apache.log4j.Logger log = Logger.getLogger(Login.class);
	private static final long serialVersionUID = 1L;
	Connection con;
	PreparedStatement pstmt;
    /**
     * Default constructor. 
     */
    public Login() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email=req.getParameter("email");
		String pwd=req.getParameter("password");
		
		System.out.println("email: "+email);
		System.out.println("password: "+pwd);
		UserDAO dao=new UserDAO();
		JSONObject obj= dao.login(email, pwd);
		
		System.out.println(obj);
		
		if(obj!=null){
			UserDTO dto=(UserDTO) obj.get("user");
			System.out.println("Name: "+dto.getFirstName());
			HttpSession userSession=req.getSession();
			userSession.setAttribute("userdetails", obj);
			userSession.setMaxInactiveInterval(120);
			log.info(email+" Logged in");
			resp.sendRedirect("home.jsp");
		}else{
			log.warn("Inavlid credientials!");
			RequestDispatcher rd=req.getRequestDispatcher("login.jsp");
			req.setAttribute("msg", "Invalid username or password");
			rd.forward(req, resp);
		}
		 
	}

}
