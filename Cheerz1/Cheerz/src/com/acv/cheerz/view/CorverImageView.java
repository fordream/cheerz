package com.acv.cheerz.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

// com.acv.cheerz.view.CorverImageView
public class CorverImageView extends ImageView {

	public CorverImageView(Context context) {
		super(context);
	}

	public CorverImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CorverImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	//
	// Bitmap rounder = Bitmap.createBitmap(getWidth(), getHeight(),
	// Bitmap.Config.ARGB_8888);
	// Canvas canvasRound = new Canvas(rounder);
	//
	// Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// xferPaint.setColor(Color.BLACK);
	//
	// final int rx = this.getWidth(); // our x radius
	// final int ry = this.getHeight(); // our y radius
	//
	// canvasRound.drawRoundRect(new RectF(0, 0, rx, ry), rx, ry, xferPaint);
	//
	// xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
	//
	// canvas.drawBitmap(rounder, 0, 0, xferPaint);
	// super.onDraw(canvas);
	// invalidate();
	// }

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		// BitmapDrawable d = new BitmapDrawable(getResources(), bitmap);
		// BitmapDrawable bitmapDrawable = CheerzUtils.roundCornered(d, 45);
		// Bitmap b = CheerzUtils.drawableToBitmap(bitmapDrawable);
		super.setImageBitmap(bitmap);
	}
}
