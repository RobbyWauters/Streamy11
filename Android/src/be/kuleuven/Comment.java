package be.kuleuven;

/**
 * @author Koen Boncquet (@Snakeflash)
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public class Comment implements Serializable{
	
	String content;
	String author;
	
	public Comment() {
	}
	
	public Comment(String content, String author) {
		this.content = content;
		this.author = author;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}

	@Override
	public String toString() {
		return "Comment [content=" + content + ", author=" + author + "]";
	}
	
}
