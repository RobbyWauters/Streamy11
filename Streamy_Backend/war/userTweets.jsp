<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="org.joda.time.format.DateTimeFormatterBuilder"%>
<%@page import="org.joda.time.format.DateTimeFormatter"%>
<%@page import="com.google.appengine.api.datastore.Query"%>
<%@page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@page import="org.ariadne_eu.hcifetcher.util.DateManager"%>
<%@page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@page import="org.joda.time.format.DateTimeFormat"%>
<%@page import="com.google.appengine.api.datastore.Entity"%><html>

<head>

<title>User Tweets Harvester</title>
<%
String resultStr = "";
String user = request.getParameter("user");
String set = request.getParameter("set");
if(user == null) user = "";
if (!user.isEmpty()){

	try{

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("TwitterUser").addFilter("user", Query.FilterOperator.EQUAL, user).addFilter("set", Query.FilterOperator.EQUAL, set);
		Entity result = datastore.prepare(q).asSingleEntity();
		if(result != null){
			resultStr = "User " + user + " already exists for set " + set + ".";
		}else{
			Entity newUser = new Entity("TwitterUser");
			newUser.setProperty("user",user);
			newUser.setProperty("set",set);
			datastore.put(newUser);
			resultStr = "Successfully added user " + user + " for set " + set + ".";
		}

	}catch(Exception e){
		resultStr = e.toString();
	}
}
%>
<br>
<center>
<form method="post" action="userTweets.jsp">
Set : <select name="set" id="set">
	<%
	out.println("<option selected value=\"mume11\">mume11</option>");
		%>
</select><br>
<br>
<table border="1" bgcolor="lightgrey" >
	<tr>
		<TD>
		<input type="text" id="user" style="width: 200px" value="<%=user%>" name="user"></input>
		<br>
		<br>
		<input type="submit" value="Add User">
	</tr>
</table>
</form>
<br><br>
<%=resultStr%>
</center>



</body>
</html>
