package be.kuleuven;

/**
 * @author Koen Boncquet (@Snakeflash)
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class StreamItem implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//TODO: kijken of dit(id) goed is
	private Long id;
	private String name;
    private String subText;
    private String content;
	private Category category;
    private Date date;
    private ArrayList<Comment> comments; 
    private ArrayList<String>  breadcrumbs; 
	private boolean read = true;
    private boolean visible = false;
    
    public StreamItem() {
    	breadcrumbs = new ArrayList<String>();
	}
    
    public StreamItem(String name, String subText, String content,
			Category category, Date date, ArrayList<Comment> comments,
			ArrayList<String> breadcrumbs, boolean read, boolean visible) {
		super();
		this.name = name;
		this.subText = subText;
		this.content = content;
		this.category = category;
		this.date = date;
		this.comments = comments;
		this.breadcrumbs = breadcrumbs;
		this.read = read;
		this.visible = visible;
		breadcrumbs = new ArrayList<String>();
	}
    
    public StreamItem(Long id, String name, String subText, String content,
			Category category, Date date, ArrayList<Comment> comments,
			ArrayList<String> breadcrumbs, boolean read, boolean visible) {
		super();
		this.id = id;
		this.name = name;
		this.subText = subText;
		this.content = content;
		this.category = category;
		this.date = date;
		this.comments = comments;
		this.breadcrumbs = breadcrumbs;
		this.read = read;
		this.visible = visible;
		breadcrumbs = new ArrayList<String>();
	}    

    public Long getId() {
    	return this.id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }
    
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getNumComments() {
		if (comments == null) {
			return 0;
		} else {
			return comments.size();
		}
	}
	
	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	
	public void addComment(Comment comment) {
		this.comments.add(comment);
	}
	
	public ArrayList<String> getBreadcrumbs() {
		return breadcrumbs;
	}

	public void setBreadcrumbs(ArrayList<String> breadcrumbs) {
		this.breadcrumbs = breadcrumbs;
	}
	
	public void addBreadcrumb(String breadcrumb) {
		breadcrumbs.add(breadcrumb);
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSubText(String subText) {
		this.subText = subText;
	}

    public String getSubText() {
		return subText;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}
}
