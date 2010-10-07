package com.sample;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

/*Image download without Asynchronous Task but with Threading model*/
public class AsyncImageLoader {
	private HashMap<String, SoftReference<Drawable>> imageCache;

	public AsyncImageLoader() {
		imageCache = new HashMap<String, SoftReference<Drawable>>();
	}

	// Call loadDrawable(imageUrl, imageCallback) providing an anonymous
	// implementation of the ImageCallback interface
	Drawable loadDrawable(final String imageUrl,
			final ImageCallback imageCallback) {
		// If the image DOES exist in the cache, it is immediately returned and
		// the ImageCallback is never called.
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softref = imageCache.get(imageUrl);
			Drawable drawable = softref.get();
			return drawable;
		}
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				imageCallback.imageLoaded((Drawable) message.obj, imageUrl);
			}
		};

		// If the image doesn’t exist in the cache yet, the image is downloaded
		// in a separate thread and the ImageCallback is called as soon as the
		// download is complete.
		new Thread() {
			@Override
			public void run() {
				Drawable drawable = loadImageFromUrl(imageUrl);
				imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
				Message message = handler.obtainMessage(0, drawable);
				handler.sendMessage(message);
			}
		}.start();
		return null;

	}

	private Drawable loadImageFromUrl(String imageUrl) {
		InputStream inputStream;
		try {
			inputStream = new URL(imageUrl).openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return Drawable.createFromStream(inputStream, "src");

	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable, String imageUrl);
	}
}
