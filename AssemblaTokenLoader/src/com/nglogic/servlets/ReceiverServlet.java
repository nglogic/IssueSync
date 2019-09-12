package com.nglogic.servlets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Encoder;

@WebServlet("/ReceiverServlet")
public class ReceiverServlet extends HttpServlet{
	


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String userLogin = (String)req.getSession().getAttribute("user_login");
		if(userLogin!=null){
			String authorizationCode = req.getParameter("code");
			String infoResponse = "";
			try {
				String clientId = (String) ( new InitialContext()).lookup("java:comp/env/clientId");
				String clientSecret = (String) ( new InitialContext()).lookup("java:comp/env/clientSecret");
				if(getAssemblaTokens(clientId, clientSecret, authorizationCode, userLogin)==true)
					infoResponse = "Twój token zosta³ wygenerowany i zapisany do pliku.";
				else
					infoResponse = "Wyst¹pi³ b³¹d.";
				req.setAttribute("infoResponse", infoResponse);
				RequestDispatcher view = req.getRequestDispatcher("Goodbye.jsp");
				view.forward(req, resp);
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IOException(e);
			}
			
		}else{
			req.setAttribute("info", "Przed autoryzacj¹ w Assembla musisz podaæ swój login.");
			RequestDispatcher view = req.getRequestDispatcher("Login.jsp");
			view.forward(req, resp);
		}
	}
	
	boolean getAssemblaTokens(String clientID, String clientSecret, String authorizationCode, String userLogin){
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	    	
	    	
	    	String auth = clientID + ":" + clientSecret;
		      BASE64Encoder encoder = new BASE64Encoder();
		        // byte[] encodedAuth = 
		        		// Base64.encodeBase64(
		        	String encodedAuth	= encoder.encode(
		            auth.getBytes() );
		         String authHeader = "Basic " +  encodedAuth;
	      //Create connection
	    	
		  String data = "grant_type=authorization_code&code="+authorizationCode;
	      url = new URL("https://api.assembla.com/token");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");

	      connection.setRequestProperty("Content-Length", Integer.toString(data.length()));
	      connection.setRequestProperty("Authorization", authHeader);  
	      
	      connection.setUseCaches (false);
	      connection.setDoOutput(true);
	      connection.setDoInput(true);

	      OutputStream os = connection.getOutputStream();
	      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
	      bw.write(data);
	      bw.flush();
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      System.out.println(response.toString());
	      HashMap<String, String> loginToLineFromFileMap = new HashMap<String,String>();
	      String fileName = (String) ( new InitialContext()).lookup("java:comp/env/tokensFile");
	      FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			String s;
			while((s = br.readLine()) != null){
				String[] lineItems = s.split(" ");
				loginToLineFromFileMap.put(lineItems[0], s);
			}
	      
	      String [] tokens = response.toString().split(",");
	      PrintWriter printWriter = new PrintWriter(fileName,"utf-8");
	      String newLine = userLogin;
	      tokens[0]=tokens[0].substring(1);
	      newLine+=" "+tokens[0].replaceAll("\"", "").replaceAll("access_token:", "");
	      newLine+=" "+tokens[1].replaceAll("\"", "").replaceAll("refresh_token:", "");
	      loginToLineFromFileMap.put(userLogin, newLine);
	      for(String fileLine:loginToLineFromFileMap.values() ){
	    	  printWriter.println(fileLine);
	      }
	      printWriter.close();
	      return true;
	     // return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return false;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}

}
