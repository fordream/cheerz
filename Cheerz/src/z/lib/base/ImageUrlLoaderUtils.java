package z.lib.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageUrlLoaderUtils {
	private ImageUrlLoader imageLoader;
	public static ImageUrlLoaderUtils instance;

	public static ImageUrlLoaderUtils getInstance(Context context) {
		return (instance == null ? (instance = new ImageUrlLoaderUtils()) : instance).init(context);
	}

	private ImageUrlLoaderUtils init(Context context) {
		if (imageLoader == null)
			imageLoader = new ImageUrlLoader(context);
		imageLoader.updateContext(context);
		return this;
	}

	public void clear() {
		imageLoader.clearCache();
	}

	private ImageUrlLoaderUtils() {

	}

	public void DisplayImage(String url, ImageUrlLoader.OnImageUrlCallBack imageView) {
		imageLoader.DisplayImage(url, imageView);
	}
}