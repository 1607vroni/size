package ch.hsr.girlpower.size;

import ch.hsr.girlpower.size.R;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class DistanceActivity extends Activity implements OnClickListener, SensorEventListener {

	private Button zurückButton;
    private SensorManager SensorManager;
    private Sensor Accelerometer;
	
	private final float[] magneticFieldData = new float[3];
	private final float[] accelerationData = new float[3];
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distance);
        
        zurückButton = (Button) this.findViewById(R.id.button1);
        zurückButton.setOnClickListener(this);
     
    	 SensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    	 Accelerometer = SensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD); 
    }

    protected void onResume() {
        super.onResume();
        SensorManager.registerListener(this, Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        SensorManager.unregisterListener(this);
    }
    
	public void onClick(View v) {
		
		if(v == zurückButton) {
			Intent intent = new Intent(this, SizeActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			System.arraycopy(event.values, 0, accelerationData, 0, 3);
		}

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			System.arraycopy(event.values, 0, magneticFieldData, 0, 3);
		}

		double currentRotationValue = getCurrentRotationValue();
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

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {	
	}
}




