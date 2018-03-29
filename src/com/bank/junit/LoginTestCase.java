package com.bank.junit;

import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

import com.bank.dao.UserDAO;

import junit.framework.TestCase;


public class LoginTestCase {

	/*@Test
	public void test() {
		fail("Not yet implemented");
	}*/
	
		@Test
	    public void testLogin() throws Exception {
		 String email="gauthamgtm@hotmail.com";
		 String password="123";
		 UserDAO dao=new UserDAO();
		 JSONObject obj= dao.login(email, password);
//		 if(obj!=null){
//			 assertTrue("This will succeed.", true);	 
//		 }else{
//			 assertTrue("This will fail!", false);
//		 }
		 //assertEquals("123", password);
		 
		 assertNotNull("null", obj);
		 //assertNull(obj);
		 //assertEquals("null", obj);
	    }
		
		

}
