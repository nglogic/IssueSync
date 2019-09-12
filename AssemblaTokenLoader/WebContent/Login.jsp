<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Assembla Token Loader</title>
</head>
<body>
<h2>${info}</h2>
<form method="POST" action='RedirectServlet' name="loginForm">
<table>
  <tr>
    <td>Podaj swój login do progamu Assembla:</td>
    <td><input type="text" name="login"></td>
  </tr>
  <tr>
    <td><input type="submit" value="save"></td>
   </tr>
 </table>
 </form>  

</body>
</html>