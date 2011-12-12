package org.ariadne_eu.hcifetcher.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("/entries/file")
public class File {

	@Path("/")
	@GET
	@Produces("application/json")
	public String getBlogsBySet(@QueryParam("set") String set, @QueryParam("author") String author) {
		
		return RestUtils.getInstance().getEntries("File",set,author);

	}
	
}