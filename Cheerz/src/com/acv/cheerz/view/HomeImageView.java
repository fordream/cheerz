package com.acv.cheerz.view;

import z.lib.base.LogUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//com.acv.cheerz.view.HomeImageView
public class HomeImageView extends ImageView {

	public static float radius = 90f;

	public HomeImageView(Context context) {
		super(context);
	}

	public HomeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HomeImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		if (!(drawable instanceof AnimationDrawable)) {
			// ((AnimationDrawable)drawable).start();
			new RunHeight(drawable).run();
		}

		super.setImageDrawable(drawable);
		// setImageBitmap(drawableToBitmap(drawable));
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		if (bm != null) {
			// post(new RunHeight(bm));
			new RunHeight(bm).run();
		}
	}

	// public static Bitmap drawableToBitmap(Drawable drawable) {
	// try {
	// if (drawable instanceof BitmapDrawable) {
	// return ((BitmapDrawable) drawable).getBitmap();
	// }
	//
	// Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
	// drawable.getIntrinsicHeight(), Config.ARGB_8888);
	// Canvas canvas = new Canvas(bitmap);
	// drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	// drawable.draw(canvas);
	//
	// return bitmap;
	// } catch (Exception exception) {
	// return null;
	// }
	// }

	private class RunHeight implements Runnable {
		int width = 0;
		int height = 0;
		private boolean isBitmap;

		public RunHeight(Object o) {

			if (o != null && o instanceof Bitmap) {
				this.width = ((Bitmap) o).getWidth();
				this.height = ((Bitmap) o).getHeight();
				isBitmap = true;
			}
			if (o != null && o instanceof Drawable) {
				isBitmap = false;
				Rect rect = ((Drawable) o).getBounds();
				if (rect != null) {
					width = rect.width();
					height = rect.height();
				}
			}
		}

		@Override
		public void run() {
			int mWight = getWidth();
			LogUtils.e("AAAAAAAA", String.format("%s %s %s %s", isBitmap, width, height, mWight));
			if (width > 0 && height > 0 && mWight > 0) {
				int mHeight = height * mWight / width;
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
				layoutParams.height = mHeight;
				setLayoutParams(layoutParams);
			}
		}
	}

}
