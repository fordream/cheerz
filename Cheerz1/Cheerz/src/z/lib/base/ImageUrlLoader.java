package z.lib.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;

public class ImageUrlLoader {
	public interface OnImageUrlCallBack {
		public void setImageBitmap(Bitmap bitmap);
	}

	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	private Map<OnImageUrlCallBack, String> imageViews = Collections.synchronizedMap(new WeakHashMap<OnImageUrlCallBack, String>());
	private ExecutorService executorService;
	private Handler handler = new Handler();
	private Context context;

	public ImageUrlLoader(Context context) {
		this.context = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	int isLoadImg = 1;

	public void DisplayImage(String url, OnImageUrlCallBack imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			isLoadImg = 1;
			queuePhoto(url, imageView);
		}
	}

	public static Bitmap getRoundedCornerImage(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		int width2 = (int) 300;
		int height2 = width2 * h / w;

		Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
		Bitmap output = Bitmap.createBitmap(resizedbitmap.getWidth(), resizedbitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, resizedbitmap.getWidth(), resizedbitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 5;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(resizedbitmap, rect, rect, paint);

		return output;

	}

	private void queuePhoto(String url, OnImageUrlCallBack imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			if (url.startsWith("http:")) {
				Bitmap bitmap = null;
				URL imageUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
				conn.setConnectTimeout(30000);
				conn.setReadTimeout(30000);
				conn.setInstanceFollowRedirects(true);
				InputStream is = conn.getInputStream();
				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				is.close();
				os.close();
				bitmap = decodeFile(f);
				return bitmap;
			} else if (url.startsWith("file:///android_asset/")) {
				AssetManager mgr = context.getAssets();
				String filepath = url.replace("file:///android_asset/", "");
				InputStream is = (InputStream) mgr.open(filepath);

				OutputStream os = new FileOutputStream(f);
				CopyStream(is, os);
				is.close();
				os.close();
				return decodeFile(f);
			} else {

				try {
					int res = Integer.parseInt(url);
					BitmapFactory.Options o = new BitmapFactory.Options();
					o.inJustDecodeBounds = true;
					BitmapFactory.decodeResource(context.getResources(), res, o);
					// Find the correct scale value. It should be the power of
					// 2.
					final int REQUIRED_SIZE = 480;
					int width_tmp = o.outWidth, height_tmp = o.outHeight;
					int scale = 1;
					while (true) {
						if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
							break;
						width_tmp /= 2;
						height_tmp /= 2;
						scale *= 2;

					}

					BitmapFactory.Options o2 = new BitmapFactory.Options();
					o2.inSampleSize = scale;

					return BitmapFactory.decodeResource(context.getResources(), res, o2);
				} catch (Exception ex) {

					InputStream in1 = null;
					try {
						File tmp = new File(url);
						in1 = new FileInputStream(tmp);
						if (url.endsWith(".nomedia")) {
							in1.read(tmp.getName().replace(".nomedia", "").getBytes());
						}
					} catch (final Exception e) {
					}

					OutputStream os = new FileOutputStream(f);
					CopyStream(in1, os);
					in1.close();
					os.close();
					Bitmap bitmap = decodeFile(f);
					return bitmap;
					//
					// BitmapFactory.Options options = new
					// BitmapFactory.Options();
					// options.inScaled = false;
					// options.inDither = false;
					// options.inDensity = 7;
					// options.inTargetDensity = 1;
					// options.inPreferredConfig = Bitmap.Config.ARGB_8888;
					//
					// return BitmapFactory.decodeStream(in1, null, options);
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(FileInputStream stream1) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 480;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;// default == 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE | height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeStream(stream1, null, o2);
			stream1.close();
			return bitmap;
		} catch (Exception e) {
		}
		return null;
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 480;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;// default == 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE | height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			// Log.e("scale", "width_tmp2==" + bitmap.getWidth() + ":scale:" +
			// scale);
			stream2.close();
			return bitmap;
		} catch (Exception e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public OnImageUrlCallBack imageView;

		public PhotoToLoad(String u, OnImageUrlCallBack i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			try {
//				if (imageViewReused(photoToLoad))
//					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				if (isLoadImg == 2) {
					bmp = getRoundedCornerImage(bmp);// test
				}
				memoryCache.put(photoToLoad.url, bmp);
//				if (imageViewReused(photoToLoad))
//					return;
				BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
				handler.post(bd);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

//	boolean imageViewReused(PhotoToLoad photoToLoad) {
//		String tag = imageViews.get(photoToLoad.imageView);
//		if (tag == null || !tag.equals(photoToLoad.url))
//			return true;
//		return false;
//	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
//			if (imageViewReused(photoToLoad))
//				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

	private void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	// ---------------------------------------------------------
	// CLASS
	// ---------------------------------------------------------

	public class FileCache {

		private File cacheDir;

		public FileCache(Context context) {
			// Find the dir to save cached images
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
				cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "LazyList");
			else
				cacheDir = context.getCacheDir();
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		}

		public File getFile(String url) {
			// I identify images by hashcode. Not a perfect solution, good for
			// the demo.
			String filename = String.valueOf(url.hashCode());
			File f = new File(cacheDir, filename);
			return f;

		}

		public void clear() {
			File[] files = cacheDir.listFiles();
			for (File f : files)
				f.delete();
		}

	}

	public class MemoryCache {
		private HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();

		public Bitmap get(String id) {
			if (!cache.containsKey(id))
				return null;
			SoftReference<Bitmap> ref = cache.get(id);
			return ref.get();
		}

		public void put(String id, Bitmap bitmap) {
			cache.put(id, new SoftReference<Bitmap>(bitmap));
		}

		public void clear() {
			cache.clear();
		}
	}

	public void updateContext(Context context2) {
		if (context == null) {
			context = context2;
		}
	}
}