package be.kuleuven;

import android.app.Application;
import android.widget.TextView;
import android.widget.Toast;

public class StreamyApplication extends Application {
	
	private Stream stream = null;
	
	//TODO: aanpasbaar maken
	public String owner = "HeRoeland";
	
	private Runnable dataRunnable;
	public Boolean dataLoaded = false;
	private MeterPanel meter;
	private TextView meterText;
	private String meterMessage;
	
	 public void onCreate() {
		super.onCreate();
		this.setStream(new Stream());
		
		dataRunnable = new Runnable(){
			@Override
			public void run() {
				getStream().loadItems();
				getStream().sortItems();
				dataLoaded = true;
			} 
		};
		Thread dataThread = new Thread(dataRunnable, "dataCollector");
		dataThread.setPriority(7);
		dataThread.start();
	}
	
	public Stream getStream() {
		return stream;
	}

	private void setStream(Stream stream) {
		this.stream = stream;
	}
	
	public String getMeterMessage() {
		return this.meterMessage;
	}
	
	void updateMeterMessage() {
		//TODO: mss nog in functie setMeterText steken
		float progress = this.getStream().getProgress();
		TextView textview = this.getMeterText();
		if(progress < 0.1) {
			this.meterMessage = "You are hopelessly neglecting schoolwork";
		} else if (progress >=0.1 && progress < 0.2) {
			this.meterMessage = "Start spending more time on school now";
		} else if (progress >=0.2 && progress < 0.3) {
			this.meterMessage = "It's bad, try to get some work done fast!";
		} else if (progress >=0.3 && progress < 0.4) {
			this.meterMessage = "it's getting worse, try to focus";
		} else if (progress >=0.4 && progress < 0.5) {
			this.meterMessage = "Below average, you can do better!";
		} else if (progress >=0.5 && progress < 0.6) {
			this.meterMessage = "Average performance, could be much better";
		} else if (progress >=0.6 && progress < 0.7) {
			this.meterMessage = "You are doing fine";
		} else if (progress >=0.7 && progress < 0.8) {
			this.meterMessage = "Great work";
		} else if (progress >=0.8 && progress < 0.9) {
			this.meterMessage = "Doing great, perfection is around the corner";
		} else if (progress >= 0.9) {
			this.meterMessage = "Perfect score! Keep up the good work";
		}
	}
	
	public TextView getMeterText() {
		return this.meterText;
	}
	
	void setMeterText(TextView textview) {
		this.meterText = textview;
		this.updateMeterText();
	}
	
	void updateMeterText() {
		this.updateMeterMessage();
		this.meterText.setText(this.getMeterMessage());
	}
	
	public MeterPanel getMeterPanel() {
		return this.meter;
	}
	
	void setMeterPanel(MeterPanel meterpanel) {
		this.meter = meterpanel;
	}
	
	void updateMeterPanel() {
		float[] progress = new float[1];
		getStream().updateProgress();
		progress[0] = (getStream().getProgress())*100;
		this.getMeterPanel().onTweenUpdated(MeterPanel.ROT, progress);
		this.updateMeterText();
		Toast.makeText(this, Float.toString(getStream().getProgress()), Toast.LENGTH_LONG).show();
	}
}
