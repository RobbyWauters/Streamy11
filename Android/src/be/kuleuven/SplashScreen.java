package be.kuleuven;

/**
 * @author Koen Boncquet (@Snakeflash)
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
 
public class SplashScreen extends Activity {
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      setContentView(R.layout.splash);
      Thread splashThread = new Thread() {
         @Override
         public void run() {
        	int wait = 1000;
        	 
            try {
               int waited = 0;
               while (waited < wait) {
                  sleep(100);
                  waited += 100;
               }
            } catch (InterruptedException e) {
               // do nothing
            } finally {
               finish();
               Intent streamIntent = new Intent(SplashScreen.this, Main.class);
               SplashScreen.this.startActivity(streamIntent);
            }
         }
      };
      splashThread.start();
   }
}
