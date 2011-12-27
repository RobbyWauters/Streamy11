
package be.kuleuven;

/**
 * @author Koen Boncquet (@Snakeflash)
 */

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DetailView extends ListActivity {
	
	private CommentAdapter m_adapter;
	
	private ArrayList<Comment> comments;
	private StreamItem item; // The item that is used to fill this page
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.detail);
	    getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
		getListView().setDivider( null );
	    
	    Bundle bundle = this.getIntent().getExtras();
	    item = (StreamItem) bundle.getSerializable("item");
	    drawUI();
	}
	
	/**
	 * 
	 */
	private void drawUI(){
		// draw Stroke 
		View stroke = (View) findViewById(R.id.stroke);
		stroke.setBackgroundDrawable(new ColorDrawable(CategoryColors.getColorFromCategory(item.getCategory())));
		
		// set title
		TextView title = (TextView) findViewById(R.id.title);
		title.setText(item.getSubText());
		
		setContent();
		initEditText();
		initComments();
	}
	
	/**
	 * Set content according to type of StreamItem
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void setContent(){
		
		TextView content = (TextView) findViewById(R.id.detail_content);
		content.setText(item.getContent());
		
		switch (item.getCategory()) {
			case FILE: // file hierarchy & text
				LinearLayout container = (LinearLayout) findViewById(R.id.breadcrumbcontainer);
				
				LinearLayout breadcrumbs = new LinearLayout(this);
				breadcrumbs.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,222));
				breadcrumbs.setPadding(0, 26, 0, 0);
				breadcrumbs.setOrientation(LinearLayout.VERTICAL);
				breadcrumbs.setBackgroundDrawable(getResources().getDrawable(R.drawable.detail_content));
				
				for (int i = 0; i < 3; i++) {
					TextView tv = new TextView(this);
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					lp.setMargins((i+1)*30, 0, 0, 10);
					tv.setLayoutParams(lp);
					tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.breadcrumb));
					tv.setTextColor(Color.parseColor("#464646"));
					tv.setTextSize(16);
					tv.setPadding(30, 5, 5, 5);
					tv.setText(item.getBreadcrumbs().get(i));
					
					breadcrumbs.addView(tv);
				}
				
		        container.addView(breadcrumbs);
				break;
			case TWEET: // tweet in contentview
				content.setText(item.getContent());
				break;
			case CALENDAR: // contentview
				content.setText(item.getContent());
				break;
			case COMMENT: // TODO: should open other view where comment was made
				content.setText(item.getContent());
				break;
			case RSS: // (part of) blog post in content view
				content.setText(item.getContent());
				break;
			default:
				break;
		}
	}
	
	public void initEditText() {
		final EditText postComment = (EditText) findViewById(R.id.newcomment);
		postComment.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN)
    	        {
    	            switch (keyCode)
    	            {
    	                case KeyEvent.KEYCODE_DPAD_CENTER:
    	                case KeyEvent.KEYCODE_ENTER:
    	                	//Create a new Comment with the appropriate parameters
    	                	String commentText = postComment.getText().toString();
    	                	String commentAuthor = ((StreamyApplication)getApplication()).owner;
    	                	Comment comment = new Comment(commentText, commentAuthor);
    	                	//Add the comment to the streamItem in the stream of this application
    	                	((StreamyApplication) getApplication()).getStream().getStreamItemById(item.getId()).addComment(comment);
    	                	
    	                	//TODO: eigenlijk stom dat dat zowel lokaal als op applicatie-niveau wordt geset...
    	                	//Add the comment to the local copy of the item
    	                	item.addComment(comment);
    	                	//Make the comment visible
    	                	updateComments();
    	                	//Hide the keyboard automatically
    	                	InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    	                	imm.hideSoftInputFromWindow(postComment.getWindowToken(), 0);   
    	                	//Empty the EditText
    	                	postComment.setText("");
    	                	return true;
    	                default:
    	                    break;
    	            }
    	        }
				return false;
			}
		});
	}
	
	
	
	/**
	 * Initialize comment stream
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void initComments(){
		comments = new ArrayList<Comment>();
		this.m_adapter = new CommentAdapter(this, R.layout.comment, comments);
		setListAdapter(this.m_adapter);
		
		comments = item.getComments();
		
		updateComments();
	}
	
	/**
	 * Populate adapter with comments data
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void updateComments(){
		if(comments != null && comments.size() > 0){
			m_adapter.clear();
			m_adapter.notifyDataSetChanged();
			
			for(int i=0; i<comments.size(); i++){
			 	m_adapter.add(comments.get(i));
			}
		}
	}
	
	/**
	 * The CommentAdapter links the data with the actual view elements
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private class CommentAdapter extends ArrayAdapter<Comment> {

		private ArrayList<Comment> items;

		public CommentAdapter(Context context, int textViewResourceId, ArrayList<Comment> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.comment, null);
			}
			
			Comment o = items.get(position);
			if (o != null) {
				TextView at = (TextView) v.findViewById(R.id.author);
				at.setText(o.getAuthor());
				
				TextView ct = (TextView) v.findViewById(R.id.content);
				ct.setText(o.getContent());
			}
			return v;
		}
	}
}
