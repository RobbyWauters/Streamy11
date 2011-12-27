package be.kuleuven;

/**
 * @author Koen Boncquet (@Snakeflash)
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	
	private MeterPanel meter;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    // set 32 bit window (draw correctly transparent images)
	    getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;

	    initDashListeners();
	    
	    // example of notification
//	    Toast.makeText(Main.this, ((StreamyApplication)this.getApplication()).testString, Toast.LENGTH_LONG).show();
    
	    // save a MeterPanel reference 
	    StreamyApplication app = (StreamyApplication) this.getApplication();
	    app.setMeterPanel((MeterPanel) findViewById(R.id.dashboardmeter));
	    app.setMeterText((TextView) findViewById(R.id.meter_info));

	    // use this to set the rotation
	    //TODO: nog uit commentaar halen
	    //app.updateMeterPanel();
	    this.setProgress();
	    //((StreamyApplication) this.getApplication()).getMeterPanel().setRotation((float) 95);
    }

    private void setProgress() {
    	Stream stream = ((StreamyApplication) this.getApplication()).getStream();
    	stream.updateProgress();
    	
    	((StreamyApplication) this.getApplication()).getMeterPanel().setRotation(stream.getProgress());
	}

	/**
     * @author Koen Boncquet (@Snakeflash)
     */
    private void setMeterInfo(String info){
    	TextView v = (TextView) findViewById(R.id.meter_info);
    	v.setText(info);
    }
    
    /**
     * Initialize dash buttons
     * 
     * @author Koen Boncquet (@Snakeflash)
     */
    private void initDashListeners(){
		ImageButton twitter = (ImageButton) findViewById(R.id.dash_twitter);
		twitter.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   startStreamIntent(buildBundle(true,false,false,false,false));
				   //TEMP: testing meter tween
//				   meter.setRotation((int) (Math.random() * 100));
			   }
		});
		
		ImageButton calendar = (ImageButton) findViewById(R.id.dash_calendar);
		calendar.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   startStreamIntent(buildBundle(false,true,false,false,false));
			   }
		});
		
		ImageButton comments = (ImageButton) findViewById(R.id.dash_comments);
		comments.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   startStreamIntent(buildBundle(false,false,true,false,false));
			   }
		});
		
		ImageButton files = (ImageButton) findViewById(R.id.dash_files);
		files.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   startStreamIntent(buildBundle(false,false,false,true,false));
			   }
		});
		
		ImageButton rss = (ImageButton) findViewById(R.id.dash_rss);
		rss.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   startStreamIntent(buildBundle(false,false,false,false,true));
			   }
		});
		
		ImageButton all = (ImageButton) findViewById(R.id.dash_all);
		all.setOnClickListener(new OnClickListener() {
			   public void onClick(View v) {
				   startStreamIntent(buildBundle(true,true,true,true,true));
			   }
		});
	}
    
    /**
     * Open StreamView
     * 
     * @author Koen Boncquet (@Snakeflash)
     */
    private void startStreamIntent(Bundle bundle){
    	Intent streamIntent = new Intent(Main.this, StreamView.class);
  	    streamIntent.putExtras(bundle);
        Main.this.startActivity(streamIntent);
    }
    
    /**
     * Builds the parameters to pass to the StreamView
     * 
     * @author Koen Boncquet (@Snakeflash)
     */
    private Bundle buildBundle(boolean twitter, boolean calendar, boolean comments, boolean files, boolean rss){
    	Bundle bundle = new Bundle();
    	
    	bundle.putBoolean("twitter", twitter);
    	bundle.putBoolean("calendar", calendar);
    	bundle.putBoolean("comments", comments);
    	bundle.putBoolean("files", files);
    	bundle.putBoolean("rss", rss);
    	
    	return bundle;
    }
    
    /**
     * @author Koen Boncquet (@Snakeflash)
     */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
}