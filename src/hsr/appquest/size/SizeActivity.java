package hsr.appquest.size;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hsr.appquest.size.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


public class SizeActivity extends Activity implements  
											SurfaceHolder.Callback ,
											Camera.PictureCallback{
		
    private final String TAG = "carpelibrum";
    private Camera camera;
    private Camera.PictureCallback cameraCallbackVorschau;
    private Camera.ShutterCallback cameraCallbackVerschluss;
    private SurfaceHolder cameraViewHolder;
    private LineView lineview;
	private int width;
	private int height;
    
    
    //Layout aufrufen
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_size);
		
         //Zur LinieView verweisen
        lineview = new LineView(this); 


        FrameLayout fl = (FrameLayout) findViewById(R.id.FrameLayout1);
        fl.addView(lineview);
        	

        }
	
	public void onClick(View v) {
	       Button button = (Button) this.findViewById(R.id.button1);
	        //button.onClick(this);
	        
	 	   Toast einToast = Toast.makeText(this, "Gedrückt: " + ((Button) v).getText(), Toast.LENGTH_SHORT);
		   einToast.show();
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
        //params.setPreviewSize(vorschauGroesse.width, vorschauGroesse.height);
        
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


	public void onPictureTaken(byte[] data, Camera camera) {

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

	
	//Linie
	public class LineView extends View {
		
	    private float xpos = -1;
		private float ypos = -1;
		
		private Paint linePaint;
		private float startX;
		private float startY;
		private float stopX;
		private float stopY;
		
	
	public LineView(Context c) {
			super(c);
	
			linePaint = new Paint();
			linePaint.setColor(Color.GREEN);
			linePaint.setStrokeWidth(10);

			
			
		setFocusable(true);
	}
	
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawLine(startX, startY, stopX, stopY, linePaint);
		
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startX = event.getX();
				startY = event.getY();
				return true;
			
			case MotionEvent.ACTION_MOVE:
				stopX = event.getX();
				stopY = event.getY();
				break;
			case MotionEvent.ACTION_UP:    
				stopX = event.getX();
				stopY = event.getY();
				break;
			default:
				return false;
			}
			invalidate();
			return true;
	}
	
}
}
	
