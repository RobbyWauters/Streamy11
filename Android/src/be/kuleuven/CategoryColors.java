package be.kuleuven;

/**
 * @author Koen Boncquet (@Snakeflash)
 */

import android.graphics.Color;

public class CategoryColors {
	
	private static int tweet = Color.parseColor("#498ac2");
	private static int file = Color.parseColor("#b64653");
	private static int comment = Color.parseColor("#6ba635");
	private static int calendar = Color.parseColor("#ebce30");
	private static int rss = Color.parseColor("#eba40c");
	
	public static int getColorFromCategory(Category cat){
		int color = 0;
		
		switch (cat) {
			case TWEET:
				color = tweet;
				break;
			case FILE:
				color = file;
				break;
			case CALENDAR:
				color = calendar;
				break;
			case COMMENT:
				color = comment;
				break;
			case RSS:
				color = rss;
				break;
			default:
				break;
		}
		
		return color;
	}
}
