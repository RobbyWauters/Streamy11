/**
 * 
 */
package org.ariadne_eu.hcifetcher.util.twitter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ariadne_eu.hcifetcher.rest.RestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author gonzalo
 *
 */
public class TwitterSearch {
	
	private static final Logger log = Logger.getLogger(TwitterSearch.class
			.getName());
	
	private static final String API_URL = "http://search.twitter.com/search.json?";
	private static final String RPP = "100";
	protected static final Charset UTF_8_CHAR_SET = Charset.forName("UTF-8");
	
	public static void main(String[] args) {
		testGetTweets();
	}
	
	private static void testGetTweets() {
		String since_id = "119719744281640960";
		JSONArray result = new TwitterSearch().getTweetsFromHashtag("mume11",since_id);
		int i = 0;
	}

	public JSONArray getTweetsFromHashtag(String hashtag, String since_id) {
		log.log(Level.INFO, "getTweetsFromHashtag");
		
		URL url;
		String result = null;
		//TODO: not implemented to fetch more than 100 tweets!
		try {
			url = new URL(API_URL + "q=%23" + hashtag +"&rpp=" + RPP + "&since_id=" + since_id + "&include_entities=true");

			result = RestUtils.getInstance().getUrlAsString(url,UTF_8_CHAR_SET);
			JSONObject response = new JSONObject(result);
			if (response.has("next_page"))
				log.log(Level.WARNING, "Has second page=" + response.get("next_page"));
			return (response.getJSONArray("results"));			
		} catch (MalformedURLException e) {
			log.log(Level.SEVERE, "Wrong URL=" + API_URL + "q=%23" + hashtag +"&rpp=" + RPP, e);
		} catch (JSONException e) {
			log.log(Level.SEVERE, "Json parse exception. Json=" + result, e);
		}
		return new JSONArray();
        
	}

}
