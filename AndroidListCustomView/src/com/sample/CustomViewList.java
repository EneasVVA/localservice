package com.sample;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.TextView;

/*This project demonstrate creation of custom list view, and Image download with Threading model and without Asynchronous Task 
 * */
public class CustomViewList extends Activity {
	/** Called when the activity is first created. */
	TextView selection;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ListView list = (ListView) findViewById(R.id.ListView01);
		List<ImageAndText> imageAndTexts = new ArrayList<ImageAndText>(2);
		// Here you can provide any image and give a name to it
		imageAndTexts
				.add(new ImageAndText(
						"http://a0.twimg.com/profile_images/212170920/NW_new_icon_normal.png",
						"Image 1 from Twitter"));
		imageAndTexts
				.add(new ImageAndText(
						"http://a3.twimg.com/profile_images/67244783/icon.andersoncooper_bigger.png",
						"Image 2 from Twitter"));
		imageAndTexts
				.add(new ImageAndText(
						"http://a0.twimg.com/profile_images/212170920/NW_new_icon_normal.png",
						"Image 3 from Twitter"));
		imageAndTexts
				.add(new ImageAndText(
						"http://a2.twimg.com/profile_images/701971734/avatar_bigger.png",
						"Image 4 from Twitter"));

		list.setAdapter(new ImageAndTextListAdapter(this, imageAndTexts, list));
	}
}