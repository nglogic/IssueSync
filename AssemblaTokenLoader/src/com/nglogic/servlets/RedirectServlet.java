package com.nglogic.servlets;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RedirectServlet")
public class RedirectServlet extends HttpServlet{
	 
	 private static String LOGIN_JSP = "/Login.jsp";
	 private ArrayList<String> userList = new ArrayList<>();
	 public RedirectServlet() throws NamingException, IOException {
		 super();
		// wczytaj listê userow z pliku
		 FileReader fr;
		 
		try {
			String fileName = (String) ( new InitialContext()).lookup("java:comp/env/usersFile");
			//fr = new FileReader("C:\\Documents and Settings\\socki\\workspace\\nglogic\\AssemblaTokenLoader\\users.txt");
			fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null) 
				userList.add(s);
			fr.close(); 
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		 
	}
	 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession();
		String login = req.getParameter("login");
		if(userList.contains(req.getParameter("login"))){
			session.setAttribute("user_login", req.getParameter("login"));
			try {
				String clientId = (String) ( new InitialContext()).lookup("java:comp/env/clientId");
				resp.sendRedirect("https://api.assembla.com/authorization?client_id="+clientId+"&response_type=code");
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IOException(e);
			}
			
		}else{
			 req.setAttribute("info", "Poda³eœ login spoza listy cz³onków zespo³u.");
			 RequestDispatcher view = req.getRequestDispatcher("Login.jsp");
			 view.forward(req, resp);
		}
		
	}
	 
	 

}
