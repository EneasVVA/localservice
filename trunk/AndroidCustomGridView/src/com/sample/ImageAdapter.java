package com.sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        //return mThumbIds.length;
    	return 10;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        
       
        Drawable image = ImageOperations(uriThumbIds[position],"image"+position+".jpg");		
		imageView.setImageDrawable(image);
        return imageView;
    }

    private String[] uriThumbIds = {
    		"http://a1.twimg.com/profile_images/111624981/Photo_25_normal.jpg",
    		"http://a0.twimg.com/profile_images/966604992/IMG_43141_normal.jpg",
    		"http://a2.twimg.com/profile_images/57973202/gigaom_icon_normal.gif",
    		"http://a1.twimg.com/profile_images/918713333/reuters_twitter_avatar_normal.png",
    		"http://a0.twimg.com/profile_images/24099792/adfreakicon_normal.gif",
    		"http://a3.twimg.com/profile_images/272069999/20070131_102_350x263_1__normal.jpg",
    		"http://a2.twimg.com/profile_images/57973202/gigaom_icon_normal.gif",
    		"http://a0.twimg.com/profile_images/966604992/IMG_43141_normal.jpg",
    		"http://a2.twimg.com/profile_images/57973202/gigaom_icon_normal.gif",
    		"http://a0.twimg.com/profile_images/966604992/IMG_43141_normal.jpg",
    		"http://a2.twimg.com/profile_images/57973202/gigaom_icon_normal.gif",
    		"http://a0.twimg.com/profile_images/966604992/IMG_43141_normal.jpg",
    		"http://a2.twimg.com/profile_images/57973202/gigaom_icon_normal.gif",
    		"http://a0.twimg.com/profile_images/966604992/IMG_43141_normal.jpg",
    		"http://a2.twimg.com/profile_images/57973202/gigaom_icon_normal.gif",
    };
    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7,
            R.drawable.sample_0, R.drawable.sample_1,
            R.drawable.sample_2, R.drawable.sample_3,
            R.drawable.sample_4, R.drawable.sample_5,
            R.drawable.sample_6, R.drawable.sample_7
    };
    
	private Drawable ImageOperations(String url, String saveFilename) {
		try {			
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");						
			return d;
		} catch (MalformedURLException e) {			
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} 
	}

	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}	
}