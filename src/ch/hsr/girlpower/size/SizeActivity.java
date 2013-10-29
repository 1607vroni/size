package ch.hsr.girlpower.size;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class SizeActivity extends Activity implements  SurfaceHolder.Callback , Camera.PictureCallback , OnClickListener{
		
    private final String TAG = "carpelibrum";
    private Camera camera;
    private Camera.PictureCallback cameraCallbackVorschau;
    private Camera.ShutterCallback cameraCallbackVerschluss;
    private SurfaceHolder cameraViewHolder;
	private int width;
	private int height;
    
    /**
     * Layout aufrufen
     */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_size);       
	}
	
	public void onClick(View v) {
		   Intent intent = new Intent(this, DistanceActivity.class);
			this.startActivity(intent);
	   }
        
	/**
	 * Menü aufrufen
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.size, menu);
		return true;
	}
	
	
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			                   int height) {

        camera.stopPreview();
        camera.setDisplayOrientation(90);
        Camera.Parameters params = camera.getParameters();
        Camera.Size vorschauGroesse = params.getPreviewSize();
        camera.setParameters(params);
        
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }
     
        camera.startPreview();	
		Toast.makeText(this ,"Unterkante bestimmen",Toast.LENGTH_SHORT).show();
	}


	public void surfaceDestroyed(SurfaceHolder holder) {
	}


	public void onPictureTaken(byte[] data, Camera camera) {
	}


	@Override
	protected void onPause() {
		super.onPause();			
		freeCamera();
	}

	private void freeCamera(){
		if(camera !=null)
		{
			camera.stopPreview();
			camera.release();
			}
		}
	
	@Override
	protected void onResume() {
		super.onResume();
        SurfaceView cameraView   = (SurfaceView) findViewById(R.id.camera_view);
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

	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){

			Toast.makeText(this ,"Oberkante bestimmen",Toast.LENGTH_SHORT).show();
			   return true; 
		}
		else {
				Intent intent = new Intent(this, DistanceActivity.class);
				startActivity(intent);
				return super.onTouchEvent(event);
	       }
	}
}
	
