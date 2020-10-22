package com.student_crm.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.student_crm.dao.UserDao;
import com.student_crm.dao.UserDaoImpl;
import com.student_crm.entity.User;
import com.student_crm.service.UserService;
import org.springframework.stereotype.Component;


//@WebServlet("/TestDBServlet")
@Component
public class TestDBServlet extends HttpServlet {
	
    private static final String userName = "springstudent";   
    private static final String password = "SpringStudent@1";
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&serverTimezone=UTC";
    private static final String driver = "com.mysql.jdbc.Driver"; //For new version com.mysql.cj.jdbc.Driver
    
    @Autowired
	private UserService userService;
	
	Logger logger = Logger.getLogger(getClass().getName());
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			out.println("Establishing The Connection To Database "+jdbcUrl);
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(jdbcUrl, userName, password);
			out.println("Connection Established Sucesssfully");
			
			UserDao   userDaos = new UserDaoImpl();
			//calling method of the bean
			out.println(userDaos.findUserByUserName("parth"));
			out.println("Data Fetched");
			logger.info(">>>> Check user in db");
			User user = (User) userService.findUserByUserName("parth");
			logger.info(">>>> add user to session");
			out.println("user "+user);
			out.println("Data Fetched");
			
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
