package org.ariadne_eu.hcifetcher.rest;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.ariadne_eu.hcifetcher.util.DateManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;

@Path("/entry")
public class Entry {

	private static final Logger log = Logger.getLogger(Entry.class.getName());

	@GET
	@Path ("/addLike")
	@Produces ("application/json")
	public String addLike(@QueryParam("userid") String userID,@QueryParam("likeid") String likeID){
		// http://localhost:8888/api/entry/addLike?userid=myuserID&likeid=theblogid
		JSONObject result = new JSONObject();

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {

			Entity likeEntity = new Entity("Like");
			likeEntity.setProperty("userid", userID);
			likeEntity.setProperty("likeid", likeID);
			datastore.put(likeEntity);
			result.append("result", "Like "+likeID+" from user "+userID + " succefully added.");
			return result.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toString();
	}

	@GET
	@Path ("/addComment")
	@Produces ("application/json")
	public String addComment(@QueryParam("author") String author,@QueryParam("text") String text,@QueryParam("category") String category,@QueryParam("notId") String notId){
		// http://localhost:8888/api/entry/addComment?author=ik&text=wadde&category=File&notId=1
		JSONObject result = new JSONObject();

		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		try {

			Entity StreamyCommentEntity = new Entity("StreamyComment");
			StreamyCommentEntity.setProperty("author", author);
			StreamyCommentEntity.setProperty("text", text);
			StreamyCommentEntity.setProperty("category", category);
			StreamyCommentEntity.setProperty("notId", notId);
			datastore.put(StreamyCommentEntity);
			result.append("result", "Comment "+text+" from user "+author + " succefully added.");
			return result.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result.toString();
	}


	@POST
	@Path ("/addLike")
	@Produces ("application/json")
	public String addUser(@FormParam("like") String likeJson){
		JSONObject result = new JSONObject();
		try {
			if(likeJson != null) {
				JSONObject like = new JSONObject(likeJson);

				DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

				try {

					Entity likeEntity = new Entity("Like");
					likeEntity.setProperty("userid", like.get("userid"));
					likeEntity.setProperty("likeid", like.get("likeid"));
					datastore.put(likeEntity);
					result.append("result", "Like "+like.get("likeid")+" from user "+like.get("userid") + " succefully added.");

				} catch (JSONException e) {
					log.log(Level.SEVERE,"JSONException",e);
				}
			}
			else {
				result.put("result", "Like was empty, no User added...");
			}
		} catch (JSONException e) {

			try {
				result.put("result", "Json could not be parsed, error was : " + e.getMessage());
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// TODO Auto-generated catch block
			e.printStackTrace();

		}

		return result.toString();

	}

}
