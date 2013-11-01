package ch.hsr.girlpower.size;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


public class AngleActivity extends Activity implements  SurfaceHolder.Callback , Camera.PictureCallback , SensorEventListener {

		private Camera camera;
	    private SurfaceHolder cameraViewHolder;
	    private SensorManager SensorManager;
	    private Sensor Accelerometer;
		private float[] messwert;
		private boolean erstens;
		private float winkela;
		private float winkelb;

		//private final float[] magneticFieldData = new float[3];
		//private final float[] accelerationData = new float[3];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
	        SensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	        Accelerometer = SensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);    
    
        if(Accelerometer == null) {
        Log.d("carpelibrum", "Kein Lagesensor vorhanden");
        } 
	}  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.size, menu);
		return true;
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        camera.stopPreview();
        camera.setDisplayOrientation(90); 
        try {
            camera.setPreviewDisplay(holder);
        	} catch (IOException e) {
            Log.d("", e.getMessage());
        	}
        camera.startPreview();	
		Toast.makeText(this ,"Unterkante bestimmen",Toast.LENGTH_SHORT).show();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		messwert = event.values;
	}
	
	/*@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			System.arraycopy(event.values, 0, accelerationData, 0, 3);
		}

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			System.arraycopy(event.values, 0, magneticFieldData, 0, 3);
		}

		double currentRotationValue = getCurrentRotationValue();
		
		messwert = event.values;
	}
	
	private double getCurrentRotationValue() {
		float[] rotationMatrix = new float[16];

		if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerationData, magneticFieldData)) {

			float[] orientation = new float[4];
			SensorManager.getOrientation(rotationMatrix, orientation);

			double neigung = Math.toDegrees(orientation[2]);

			return Math.abs(neigung);
		}
		return 0;
	}
	*/
	
	@Override
	protected void onPause(){
		super.onPause();
		
		if(camera != null){
			camera.stopPreview();
			camera.release();
		}
    	SensorManager.unregisterListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        SurfaceView cameraView   = (SurfaceView) findViewById(R.id.camera_view);
        cameraViewHolder         = cameraView.getHolder();
        cameraViewHolder.addCallback(this);      
        SensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        erstens = true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(erstens){
				erstens = false;
				winkela = (float) Math.sqrt(messwert[1] * messwert[1]);
				Toast.makeText(this ,"Oberkante bestimmen",Toast.LENGTH_SHORT).show();
			} 
			else {
				winkelb = (float) (Math.sqrt(messwert[1] * messwert[1]))-winkela;
				Intent intent = new Intent(this, AltimeterActivity.class);
				intent.putExtra("winkel1", winkela);
				intent.putExtra("winkel2", winkelb);
				startActivity(intent);
			}
		}
		return super.onTouchEvent(event);
	}	
}
