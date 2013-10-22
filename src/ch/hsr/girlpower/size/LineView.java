package ch.hsr.girlpower.size;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

	public class LineView extends View {
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