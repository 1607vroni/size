package hsr.appquest.size;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hsr.appquest.size.R;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.content.ContentValues;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SizeActivity extends Activity implements  
											SurfaceHolder.Callback ,
											Camera.PictureCallback{
	
    private final String TAG = "carpelibrum";
    private Camera camera;
    private Camera.PictureCallback cameraCallbackVorschau;
    private Camera.ShutterCallback cameraCallbackVerschluss;
    private SurfaceHolder cameraViewHolder;

    
    //Layout aufrufen
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_size);
	}

	
	//Menü aufrufen
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.size, menu);
		return true;
	}
	
	
	//Kamera öffnen
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}
	
	
	//Kamera vorbereiten
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			                   int height) {

        camera.stopPreview();
        // Kamera-Vorschau in "Portrait-Modus" drehen
        camera.setDisplayOrientation(90);
        
        Camera.Parameters params = camera.getParameters();
        Camera.Size vorschauGroesse = params.getPreviewSize();
        params.setPreviewSize(vorschauGroesse.width, vorschauGroesse.height);
   
        
        camera.setParameters(params);
        
        
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
        
        camera.startPreview();	
       	
	}


	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	//Bild aufnehmen
	public boolean onTouchEvent(MotionEvent event) {

       if(event.getAction() == MotionEvent.ACTION_UP) {
		   camera.takePicture(this.cameraCallbackVerschluss, this.cameraCallbackVorschau, this); 
		   return true; 
		}
       else {
		   return super.onTouchEvent(event);
       }

	}


	// Bild speichern
	public void onPictureTaken(byte[] data, Camera camera) {
		
        try {
           SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
           String name     = "foto_" + df.format(new Date());
           ContentValues werte = new ContentValues();
           werte.put(MediaColumns.TITLE, name);
           werte.put(ImageColumns.DESCRIPTION, "Aufgenommen mit CameraDemo");
           Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, werte);
           
           OutputStream ausgabe =  getContentResolver().openOutputStream(uri);            	   
           ausgabe.write(data);
           ausgabe.close();
           camera.startPreview();
          
     } catch (Exception ex) {
    	 Log.d(TAG, ex.getMessage());
     }

	}

	
	// Kamera wieder freigeben
	@Override
	protected void onPause() {
		super.onPause();
		
						
		if(camera != null) {
		   camera.stopPreview();
		   camera.release();
		}
	}

	
	// Kamera registrieren
	@Override
	protected void onResume() {
		super.onResume();
		

        SurfaceView cameraView   = (SurfaceView) findViewById(R.id.surfaceview1);
        cameraViewHolder         = cameraView.getHolder();
        cameraViewHolder.addCallback(this);
        
     	        
        cameraCallbackVorschau = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera c) {
            }
        };

        cameraCallbackVerschluss = new Camera.ShutterCallback() {
            public void onShutter() {
            }
        };

		
	}

}
