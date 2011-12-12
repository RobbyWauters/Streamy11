<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>

<%@page import="org.joda.time.format.DateTimeFormatterBuilder"%>
<%@page import="org.joda.time.format.DateTimeFormatter"%>
<%@page import="com.google.appengine.api.datastore.Query"%>
<%@page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@page import="org.ariadne_eu.hcifetcher.util.DateManager"%>
<%@page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@page import="org.joda.time.format.DateTimeFormat"%>
<%@page import="com.google.appengine.api.datastore.Entity"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Iterator"%><html>

<head>

<title>Rss Feeds Harvester</title>
<%
String resultStr = "";
String feed = request.getParameter("feed");
String set = request.getParameter("set");
String type = request.getParameter("type");
if(feed == null) feed = "";
if (!feed.isEmpty()){

	try{

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("RssFeed")
			.addFilter("feed", Query.FilterOperator.EQUAL, feed)
			.addFilter("set", Query.FilterOperator.EQUAL, set)
			.addFilter("type", Query.FilterOperator.EQUAL, type);
		Entity result = datastore.prepare(q).asSingleEntity();
		if(result != null){
			resultStr = "Feed " + feed + " already exists for set " + set + " and type " + type + ".";
		}else{
			Entity newfeed = new Entity("RssFeed");
			newfeed.setProperty("feed",feed);
			newfeed.setProperty("set",set);
			newfeed.setProperty("type",type);
			datastore.put(newfeed);
			resultStr = "Successfully added feed " + feed + " for set " + set + " and type " + type + ".";
		}

	}catch(Exception e){
		resultStr = e.toString();
	}
}
%>
<br>
<center>
<form method="post" action="RssFeeds.jsp">
Set : <select name="set" id="set">
	<%
	String[] values = {"mume11","thesis11"};
	Iterator<String> valuesIter = Arrays.asList(values).iterator();
	while(valuesIter.hasNext()){
		String value = valuesIter.next();
		if(set != null && set.equals(value)){
			out.println("<option selected value="+value+">"+value+"</option>");
		}
		else{
			out.println("<option value="+value+">"+value+"</option>");
		}
		
	}
		%>
</select><br>
Type : <select name="type" id="type">
	<%
	
	values = new String[]{"Comment","Blog"};
	valuesIter = Arrays.asList(values).iterator();
	while(valuesIter.hasNext()){
		String value = valuesIter.next();
		if(type != null && type.equals(value)){
			out.println("<option selected value="+value+">"+value+"</option>");
		}
		else{
			out.println("<option value="+value+">"+value+"</option>");
		}
		
	}
		%>
</select><br>
<br>
<table border="1" bgcolor="lightgrey" >
	<tr>
		<TD>
		<input type="text" id="feed" style="width: 200px" value="<%=feed%>" name="feed"></input>
		<br>
		<br>
		<input type="submit" value="Add Feed">
	</tr>
</table>
</form>
<br><br>
<%=resultStr%>
</center>



</body>
</html>
