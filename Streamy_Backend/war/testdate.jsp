<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="org.joda.time.format.DateTimeFormatterBuilder"%>
<%@page import="org.joda.time.format.DateTimeFormatter"%>
<%@page import="com.google.appengine.api.datastore.Query"%>
<%@page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@page import="org.ariadne_eu.hcifetcher.util.DateManager"%>
<%@page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@page import="org.joda.time.format.DateTimeFormat"%><html>

<head>

<title>Date Tester</title>
<%
	String metadataDefault = "<lom>\n...</lom>";

String result = "";

String input = "";


String newdate = request.getParameter("newdate");

String date = request.getParameter("date");
if(date == null) date = "2011-10-20";

String type = request.getParameter("type");

if (newdate != null){

	try{

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateManager.getInstance().updateTo(fmt.parseDateTime(newdate),type);
		
		out.println( type + "date updated to " + newdate);
		out.println();
	}catch(Exception e){}
}

if (type != null){

	try{

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
		result = String.valueOf(DateManager.getInstance().isMoreRecent(fmt.parseDateTime(date),type));
		
		out.println("Result : " + result);
		out.println();
	}catch(Exception e){}
}
%>
<br>
<center>
<form method="post" action="testdate.jsp">
Type : <select name="type" id="type">
	<%

	out.println("<option selected value=\"blog\">blog</option>");
	out.println("<option value=\"comment\">comment</option>");
	out.println("<option value=\"tweet\">tweet</option>");
		%>
</select><br>
<br>
<table border="1" bgcolor="lightgrey" >
	<tr>
		<TD>
		<input type="text" id="date" style="width: 200px" value="<%=date%>" name="date"></input>
		<br>
		<br>
		<input type="submit" value="Test !">
	</tr>
</table>

</form>
</center>

<br><br>
<center>
<form method="post" action="testdate.jsp">
Type : <select name="type" id="type">
	<%

	out.println("<option selected value=\"blog\">blog</option>");
	out.println("<option value=\"comment\">comment</option>");
	out.println("<option value=\"tweet\">tweet</option>");
		%>
</select><br>
<br>
<table border="1" bgcolor="lightgrey" >
	<tr>
		<TD>
		<input type="text" id="newdate" style="width: 200px" value="<%=newdate%>" name="newdate"></input>
		<br>
		<br>
		<input type="submit" value="Push Date">
	</tr>
</table>

</form>
</center>

</body>
</html>
