
package be.kuleuven;

/**
 * Class representing the dashboard meter
 * 
 * @author Koen Boncquet (@Snakeflash)
 */

import be.kuleuven.R;
import be.kuleuven.R.drawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.Tweenable;
import aurelienribon.tweenengine.equations.Elastic;
import aurelienribon.tweenengine.equations.Expo;

public class MeterPanel extends SurfaceView implements SurfaceHolder.Callback, Tweenable {

	public static final int ROT = 1;
	public static TweenManager tm = new TweenManager();
	
	private Canvas canvas;
	private Bitmap bmp;
	private Bitmap bg;
	// scale: 0 - 100 (0 = left, 100 = right)
	private float rotation = 0;
	private int width;
	private int height;
	private Matrix matrix;
	private CanvasThread canvasthread;

	/**
	 * Default Constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public MeterPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		
		setFocusable(true);
		
		// load BitMaps
        bmp = BitmapFactory.decodeResource(getResources(),
               R.drawable.pointer);
        bg = BitmapFactory.decodeResource(getResources(),
                R.drawable.streamy_meter_a);
        width = bmp.getWidth();
    	height = bmp.getHeight();
    	matrix = new Matrix();
    	
//    	setRotation(95);
	}
	
	/**
	 * Sets the rotation of the pointer in percentages
	 * 
	 * @param percent 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	public void setRotation(float percent){
		Tween.to(this, ROT, 3000, Expo.INOUT)
        .target(percent)
        .delay(2)
        .addToManager(tm);
	}

	/**
	 * Update method for drawing
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	@Override
	public void onDraw(Canvas canvas) {
		this.canvas = canvas;
        updateRotation();
	}
	
	/**
	 * Update the rotation of the pointer on the dashboard
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
	private void updateRotation(){
        // rotate the Bitmap
        matrix.setTranslate(0,0);
        
        // compute rotation angle in degrees
        float rotationPercent = -90 + 180 * (rotation/100);
        matrix.postRotate(rotationPercent,width/2,height/2);
        
        // redraw canvas
        canvas.drawColor(Color.GRAY);
        canvas.drawBitmap(bg,0,0,null);
		canvas.drawBitmap(bmp, matrix, null);  
	}
	
	/**
	 * Getter for tween animation
	 * 
	 * @author Koen Boncquet (@Snakeflash)
	 */
    @Override
    public int getTweenValues(int tweenType, float[] returnValues) {
        switch (tweenType) {
                case ROT: returnValues[0] = rotation;
                          return 1;
                default: assert false; return 0;
        }
    }

    /**
     * Setter for tween animation
     * 
     * @author Koen Boncquet (@Snakeflash)
     */
    @Override
    public void onTweenUpdated(int tweenType, float[] newValues) {
        switch (tweenType) {
                case ROT: 
                	rotation = newValues[0];
                	updateRotation();
                break;
                default: assert false; break;
        }
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
	
	/**
	 * Starts a new thread that will handle the animation
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		canvasthread = new CanvasThread(getHolder(), this);	
		canvasthread.setRunning(true);
		canvasthread.start();
	}
	
	/**
	 * Destroys the thread
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		canvasthread.setRunning(false);
		while (retry) {
			try {
				canvasthread.join();
				retry = false;
			} catch (InterruptedException e) {
				// we will try it again and again...
			}
		}

	}
}
