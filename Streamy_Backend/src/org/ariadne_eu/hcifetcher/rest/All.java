package org.ariadne_eu.hcifetcher.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;


@Path("/entries/all")
public class All {

	@Path("/")
	@GET
	@Produces("application/json")
	public String getBlogsBySet(@QueryParam("set") String set, @QueryParam("author") String author) {
		
		int total_results = 0;
		String tweets = RestUtils.getInstance().getEntries("Tweet",set,author);
		String start = tweets.substring(0, 12);
		String end = tweets.substring(tweets.lastIndexOf(","), tweets.lastIndexOf(":")+1);
		total_results += Integer.parseInt(tweets.substring(tweets.lastIndexOf(":")+1, tweets.length()-1));
		tweets = tweets.substring(12, tweets.lastIndexOf(",")-1);
		
		String comments = RestUtils.getInstance().getEntries("Comment",set,author);
		total_results += Integer.parseInt(comments.substring(comments.lastIndexOf(":")+1, comments.length()-1));
		comments = comments.substring(12, comments.lastIndexOf(",")-1);
		
		String blogs = RestUtils.getInstance().getEntries("Blog",set,author);
		total_results += Integer.parseInt(blogs.substring(blogs.lastIndexOf(":")+1, blogs.length()-1));
		blogs = blogs.substring(12, blogs.lastIndexOf(",")-1);
		
		String files = RestUtils.getInstance().getEntries("File",set,author);
		total_results += Integer.parseInt(files.substring(files.lastIndexOf(":")+1, files.length()-1));
		files = files.substring(12, files.lastIndexOf(",")-1);
		
		String schedules = RestUtils.getInstance().getEntries("Schedule",set,author);
		total_results += Integer.parseInt(schedules.substring(schedules.lastIndexOf(":")+1, schedules.length()-1));
		schedules = schedules.substring(12, schedules.lastIndexOf(",")-1);
		
		end = "]" + end +total_results+"}";
		
		String total = start+tweets+","+comments+","+blogs+","+files+","+schedules+end;
		/*System.out.println("start: "+start);
		System.out.println("end: "+end);
		System.out.println("results: "+ total_results);*/
		return total;

	}	
}
