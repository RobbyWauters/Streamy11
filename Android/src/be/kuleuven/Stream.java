package be.kuleuven;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Stream {

	private ArrayList<StreamItem> items = new ArrayList<StreamItem>();
	private float progress;
	
	public Stream(){

	}
		
	public StreamItem getStreamItem(int index) throws IndexOutOfBoundsException {
		StreamItem item = null;
		if(index <= this.getNbOfItems()) {
			item = this.items.get(index);
		} else {
			throw new IndexOutOfBoundsException();
		}
		return item;
	}
	
	public StreamItem getStreamItemById(Long id){
		StreamItem streamitem = null;
		for(int i=0; i<this.getNbOfItems(); i++) {
			if (id == items.get(i).getId()) {
				streamitem = items.get(i);
			}
		}
		return streamitem;
	}
	
	public ArrayList<StreamItem> getStreamItems() {
		return this.items;
	}
	
	private void addItem(StreamItem item) {
		this.items.add(item);
	}
	
	private void addItems(ArrayList<StreamItem> items) {
		this.items.addAll(items);
	}
	
	public float getProgress() {
		return this.progress;
	}
	
	public void setProgress(float progress) throws IllegalArgumentException{
		if(isValidProgress(progress)) {
			this.progress = progress;
		} else {
			throw new IllegalArgumentException();
		}
	}
		
	public Boolean isValidProgress(float progress) {
		return (progress>=0 && progress<=1);
	}

	public void updateProgress() {
		float progress = 0;
		if(this.getNbOfItems() != 0) {
			float teller = Integer.valueOf(this.getNbOfReadItems()).floatValue();
			float noemer = Integer.valueOf(this.getNbOfItems()).floatValue();
			progress = (teller/noemer);
		}
		this.setProgress(progress);
	}
	
	void loadItems() {
		try {
			//String streamItemsString = getJSONString("http://streamy11.appspot.com/api/entity/");
			String tweetString = this.getJSONString("http://streamy11.appspot.com/api/entries/tweet");
			String scheduleString = this.getJSONString("http://streamy11.appspot.com/api/entries/schedule");
			String commentString = this.getJSONString("http://streamy11.appspot.com/api/entries/comment");
			String filesString = this.getJSONString("http://streamy11.appspot.com/api/entries/file");
			String rssString = this.getJSONString("http://streamy11.appspot.com/api/entries/blog");
			String sCommentsString = this.getJSONString("http://streamy11.appspot.com/api/entries/scomment");
			
			//this.parseJSON(streamItemsString);
			this.parseJSON(tweetString);
			this.parseJSON(commentString);
			this.parseJSON(rssString);
			
			this.parseJSON(sCommentsString);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
   /**
    * Gets the JSON-data from the given url and returns it as a String
    * 
    * @author Roeland Herrebosch
    */
   public String getJSONString(String url) {
   	String jString = "";
		try {
			BufferedReader reader = read(url);
			String line = reader.readLine();
			jString = line;
			while (line != null) {
				line = reader.readLine();
				if(line != null) {
				//	Log.i("problem", line);
					jString = jString+line;
				//	Log.i("problem", Integer.toString(jString.length()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	return jString;
   }
   

   
   public void parseJSON(String jString) {
	   try {
	   		// Create a new JSONObject
	   		JSONObject jObject = new JSONObject(jString);
	   				
	   		//Get the results-array
	   		JSONArray jArray = jObject.getJSONArray("results");
	   				
	   		// Get the results, create StreamItems and put them in the ArrayList items
	   		for(int i=0; i<jArray.length(); i++) {
	   			String categoryString = jArray.getJSONObject(i).getString("type");
	   			if(categoryString.equals("Tweet")) {
	   				this.parseJSONStreamItem(jArray.getJSONObject(i), Category.TWEET);
	   			} else if (categoryString.equals("Schedule")) {
	   				this.parseJSONStreamItem(jArray.getJSONObject(i), Category.CALENDAR);
	   			} else if (categoryString.equals("Comment")) {
	   				this.parseJSONStreamItem(jArray.getJSONObject(i), Category.COMMENT);
	   			} else if (categoryString.equals("Files")) {
	   				this.parseJSONStreamItem(jArray.getJSONObject(i), Category.FILE);
	   			} else if (categoryString.equals("Blog")) {
	   				this.parseJSONStreamItem(jArray.getJSONObject(i), Category.RSS);
	   			} else if (categoryString.equals("StreamyComment")) {
	   				this.parseJSONComment(jArray.getJSONObject(i));
	   			} else {
	   				Log.i("problem", "Category wordt niet correct gededecteerd");
	   			}
	   		}
	   	} catch (JSONException e) {
				e.printStackTrace();
			}
   }
   
   /**
    * Parses the JSON-string that is inputted to an ArrayList of StreamItems
    * 
    * @author Roeland Herrebosch
    */
   public void parseJSONStreamItem(JSONObject jObject, Category cat) {
   	try {
   			String idString = jObject.getString("id");
   			Long id = Long.valueOf(idString);

   			String name = jObject.getString("author");
   			String subText = jObject.getString("tags");
   			String content = jObject.getString("text");
   		
   			String dateString = jObject.getString("datestamp");
   			dateString = dateString.substring(0, 20);
   			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); 
   			Date date = dateFormat.parse(dateString); 
   		
   			ArrayList<Comment> comments = new ArrayList<Comment>();
//   			String breadcrumbs = null;
   			ArrayList<String> breadcrumbs = null;
   		
   			Boolean read = false;
   			Boolean visible = true;
   					
   			StreamItem item = new StreamItem(id, name, subText, content, cat, date, comments,
   					breadcrumbs, read, visible);
   					
   			this.addItem(item);
   	} catch (JSONException e) {
			e.printStackTrace();
	} catch (ParseException e) {
			e.printStackTrace();
	} 
   }
   
   public void parseJSONComment(JSONObject jObject) {
	   try {	
			// Get the results, create StreamItems and put them in the ArrayList items

				String name = jObject.getString("author");
	   			String content = jObject.getString("text");
				
	   			Comment comment = new Comment(content, name);
	   			
	   			String notIdString = jObject.getString("id");
	   			Long notId = Long.valueOf(notIdString);
	   			
	   			if (this.getStreamItemById(notId) != null) {
	   				this.getStreamItemById(notId).addComment(comment);
	   			
	   				String dateString = jObject.getString("datestamp");
	   				dateString = dateString.substring(0, 20);
	   				SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss"); 
	   				Date date = dateFormat.parse(dateString);
	   			
	   				this.getStreamItemById(notId).setDate(date);
	   			} else {
	   				Log.i("commentproblem", "comment could not be loaded, because its parent-notification could not be found");
	   			}
  			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
   }
   
   /**
   * @author Koen Boncquet (@Snakeflash)
   */
  public static BufferedReader read(String url) throws Exception{
  	return new BufferedReader(new InputStreamReader(new URL(url).openStream()));
  }
  
	/**
	 * Sorts the items (by date)
	 * 
	 * @author Roeland Herrebosch
	 */
	void sortItems(){
		if(getNbOfItems() != 0) {
			ArrayList<StreamItem> sortedList = new ArrayList<StreamItem>();
			sortedList.add(items.get(0));
			for(int i = 1; i < items.size(); i++) {
				int j = 0;
				while(j < sortedList.size() && sortedList.get(j).getDate().after(items.get(i).getDate())){
					j++;
				}
				sortedList.add(j, items.get(i));
				}
			items = sortedList;
		}
	}
	
	public int getNbOfItems() {
		return this.items.size();
	}
	
	public int getNbOfReadItems() {
		int read = 0;
		if (this.getNbOfItems() != 0) {
			for(int i=0; i<getNbOfItems(); i++) {
				if(this.getStreamItem(i).isRead()) {
					read++;
				}
			}
		}
		return read;
	}
	
}
