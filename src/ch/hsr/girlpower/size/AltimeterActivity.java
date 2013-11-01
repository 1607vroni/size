package ch.hsr.girlpower.size;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AltimeterActivity extends Activity implements OnClickListener{
	private float winkela;
	private float winkelb;
	private float gamma;
	private TextView alpha;
	private TextView beta;
	private TextView b;
	private long result;
	private Double abstand;
	private EditText a;
	private Button KameraButton;

	
	public void onClick(View v) {
		
		if(v == KameraButton) {
			Intent intent = new Intent(this, AngleActivity.class);
			startActivity(intent);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.size, menu);
		return true;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_altimeter);  	
	
        KameraButton = (Button) this.findViewById(R.id.btn_Kamera);
        KameraButton.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		winkela = extras.getFloat("winkel1");
		winkelb = extras.getFloat("winkel2");
		gamma = 180 - (winkela+winkelb);
		
		alpha = (TextView)findViewById(R.id.et_alpha);
		alpha.setText(""+(int)winkela);
		beta = (TextView)findViewById(R.id.et_beta);
		beta.setText(""+(int)winkelb);
		b = (TextView)findViewById(R.id.et_hoehe);
		a = (EditText)findViewById(R.id.et_abstand);
		a.addTextChangedListener(new TextWatcher()
		{
		
		@Override
		public void afterTextChanged(Editable arg0) {
			abstand = Double.parseDouble(a.getText().toString());
			result = Math.round((abstand/Math.tan(Math.toRadians((double)winkela)))+(abstand/Math.tan(Math.toRadians((double)gamma))));
			b.setText(""+result);
		}
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1,
				int arg2, int arg3) {
		}
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}
		});
	
	}
	
}
