package be.kuleuven;

/**
 * @author Koen Boncquet (@Snakeflash)
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class StreamView extends ListActivity{

	private ProgressDialog m_ProgressDialog = null; 
	//TODO: kan deze var uiteindelijk niet verangen worden door stream-klasse?
	private ArrayList<StreamItem> items = null;
	private StreamAdapter m_adapter;
	private Runnable viewStream;
	
	private boolean twitterEnabled;
	private boolean filesEnabled;
	private boolean calendarEnabled;
	private boolean commentsEnabled;
	private boolean rssEnabled;
	
	/**
	 * Main method
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.streamview);
		getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
		getListView().setDivider( null );
		
		initItems();
		initVisibility();
		updateItems();
		initMenuListeners();
		initListListeners();
	}
	
	/**
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void initListListeners(){
		getListView().setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		    	Bundle bundle = new Bundle();
		    	StreamItem item = (StreamItem)parent.getItemAtPosition(position);
		    	item.setRead(true);
		    	//TODO: update row...
		    	bundle.putSerializable("item", item);
		    	
		    	Intent detailIntent = new Intent(StreamView.this, DetailView.class);
		    	detailIntent.putExtras(bundle);
		    	StreamView.this.startActivity(detailIntent);
		    	m_adapter.notifyDataSetChanged();
		    }
		  });
	}
	
	/**
	 * @author Koen Boncquet (@Snakeflash)
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		((StreamyApplication) this.getApplication()).updateMeterPanel();
	}
	
	/**
	 * initializes visibility with parameter passed from main activity
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void initVisibility(){
		Bundle bundle = this.getIntent().getExtras();
		twitterEnabled = bundle.getBoolean("twitter");
		calendarEnabled = bundle.getBoolean("calendar");
		commentsEnabled = bundle.getBoolean("comments");
		filesEnabled = bundle.getBoolean("files");
		rssEnabled = bundle.getBoolean("rss");
	}
	
	/**
	 * onClicklisteners for each menuitem
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void initMenuListeners(){
		ImageButton twitter = (ImageButton) findViewById(R.id.twitterButton);
		twitter.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   twitterEnabled = !twitterEnabled;
				   updateItems();
			   }
		});
		
		ImageButton files = (ImageButton) findViewById(R.id.filesButton);
		files.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   filesEnabled = !filesEnabled;
				   updateItems();
			   }
		});
		
		ImageButton calendar = (ImageButton) findViewById(R.id.calendarButton);
		calendar.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   calendarEnabled = !calendarEnabled;
				   updateItems();
			   }
		});
		
		ImageButton comments = (ImageButton) findViewById(R.id.commentsButton);
		comments.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   commentsEnabled = !commentsEnabled;
				   updateItems();
			   }
		});
		
		ImageButton rss = (ImageButton) findViewById(R.id.rssButton);
		rss.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   rssEnabled = !rssEnabled;
				   updateItems();
			   }
		});
	}
	
	/**
	 * Initialize the items in a seperate thread
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void initItems(){
		items = new ArrayList<StreamItem>();
		this.m_adapter = new StreamAdapter(this, R.layout.row, items);
		setListAdapter(this.m_adapter);
		
		viewStream = new Runnable(){
			@Override
			public void run() {
				getItems();
			}
		};
		
		Thread thread =  new Thread(null, viewStream, "MagentoBackground");
		thread.start();
		m_ProgressDialog = ProgressDialog.show(StreamView.this, "Please wait...", "Retrieving data ...", true);
	}
	
	/**
	 * Initialize data in adapter
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	/**
	 * Currently contains only test data
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void getItems(){
		try{
		//	items = new ArrayList<StreamItem>();
			
			items =((StreamyApplication) this.getApplication()).getStream().getStreamItems();
			
			ArrayList<Comment> comments = new ArrayList<Comment>();
			
//			sortItems();
			
			// dummy comments, for now all updates have same comments
			//TODO: naar testcomments kijken
			for (int i = 0; i < 4; i++) {
				Comment o1 = new Comment();
				o1.setContent("Bedankt voor de snelle update! Ik begin nog deze avond te leren voor dit vak.");
				o1.setAuthor("Koen Boncquet");
				
				Comment o2 = new Comment();
				o2.setContent("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s");
				o2.setAuthor("Robby Wauters");
				
				comments.add(o1);
				comments.add(o2);
			}
			
			// Testing load screen for items
//			Thread.sleep(5000);
			Log.i("ARRAY", ""+ items.size());
		} catch (Exception e) { 
			//TODO: zoek de fout
	//		Log.e("BACKGROUND_PROC", e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	/**
	 * Sorts the items (currently by date)
	 * TODO: if there is time left, try to make more advanced priorities
	 */
	private void sortItems(){
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
	
	/**
	 * Updates the currently visible categories
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void updateItems(){
		//set menu images
		updateMenuImages();
		
		//set items visibility
		for (int i = 0; i < items.size(); i++) {
			
			StreamItem item = items.get(i);
			
			switch (item.getCategory()) {
				case TWEET:
					item.setVisible(twitterEnabled);
					break;
				case FILE:
					item.setVisible(filesEnabled);
					break;
				case CALENDAR:
					item.setVisible(calendarEnabled);
					break;
				case COMMENT:
					item.setVisible(commentsEnabled);
					break;
				case RSS:
					item.setVisible(rssEnabled);
					break;
				default:
					break;
			}
		}
		
		if(items != null && items.size() > 0){
			m_adapter.clear();
			m_adapter.notifyDataSetChanged();
			
			for(int i=0; i<items.size(); i++)
			 	if (items.get(i).isVisible()) m_adapter.add(items.get(i));
		}
	}
	
	/**
	 * Update menu images according to booleans
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void updateMenuImages(){
		ImageButton twitter = (ImageButton) findViewById(R.id.twitterButton);
		if (twitterEnabled){
			twitter.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_01));
		}else{
			twitter.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_off_01));
		}

		ImageButton files = (ImageButton) findViewById(R.id.filesButton);
		if (filesEnabled){
			files.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_02));
		}else{
			files.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_off_02));
		}

		ImageButton calendar = (ImageButton) findViewById(R.id.calendarButton);
		if (calendarEnabled){
			calendar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_03));
		}else{
			calendar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_off_03));
		}

		ImageButton comments = (ImageButton) findViewById(R.id.commentsButton);
		if (commentsEnabled){
			comments.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_04));
		}else{
			comments.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_off_04));
		}

		ImageButton rss = (ImageButton) findViewById(R.id.rssButton);
		if (rssEnabled){
			rss.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_05));
		}else{
			rss.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_item_off_05));
		}
	}

	
	/**
	 * The StreamAdapter links the data with the actual view elements
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private class StreamAdapter extends ArrayAdapter<StreamItem> {

		private ArrayList<StreamItem> items;

		public StreamAdapter(Context context, int textViewResourceId, ArrayList<StreamItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			
			StreamItem o = items.get(position);
			if (o != null) {
				ImageView bg = (ImageView) v.findViewById(R.id.bar);
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				TextView dt = (TextView) v.findViewById(R.id.date);
				TextView ct = (TextView) v.findViewById(R.id.comments);
				
				
				if(o.isRead()){
					tt.setTypeface(null, Typeface.NORMAL);
					bt.setTypeface(null, Typeface.NORMAL);
				}else{
					tt.setTypeface(null, Typeface.BOLD);
					bt.setTypeface(null, Typeface.BOLD);
				}
				
				if(bg != null){
					int d = 0;
					
					switch (o.getCategory()) {
						case TWEET:
							d = R.drawable.bar_blue;
							break;
						case FILE:
							d = R.drawable.bar_red;
							break;
						case CALENDAR:
							d = R.drawable.bar_yellow;
							break;
						case COMMENT:
							d = R.drawable.bar_green;
							break;
						case RSS:
							d = R.drawable.bar_orange;
							break;
						default:
							break;
					}
					
					bg.setBackgroundDrawable(getResources().getDrawable(d));
				}
				
				if(tt != null) {
					tt.setText(o.getName());                            
				}
				
				if(bt != null){
					bt.setText(o.getSubText());
				}
				
				if(dt != null){
					DateFormat formatter = new SimpleDateFormat("HH:mm");
					dt.setText(formatter.format(o.getDate()));
				}
				
				if(ct != null){
					ct.setText(o.getNumComments() + "");
				}
			}
			return v;
		}
	}
}