package com.codepath.example.imagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	EditText etQuery;
	Button btnSearch;
	GridView gvResults;
	//Stores a colln of image results
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		//create method called setupView and set the field vars with the handle
		setupViews();
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long rowId) {
				//All we need is the position
				//first arg context, second arg, name of activity to launch
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);

				//set the photo that we wanna displaym from the ArrayList
				//position is coming from the click
				ImageResult image = imageResults.get(position);

				//make a bundle: essentially a param passed along with the req that
				//you can access on both sides
				//The new activity can access the bundle param to get the data from 
				//the underlying activity that started it
				//Since we made ImageResult serializable, we can just pass it in
				i.putExtra("result", image);

				//Execute intent
				startActivity(i);	
			}

		});
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadImageData(page);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);	
	}

	public void onImageSearch(View v) {
		this.loadImageData(0);
	}

	public void loadImageData(int page) {
		//Using the async http client: Creating obj
		AsyncHttpClient client = new AsyncHttpClient();

		//Get Query
		String query = etQuery.getText().toString();

		//The url we wanna hit looks like this: 
		//https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=food

		//rsz=8 => max # of results is 8.
		Log.d("DEBUG", "BEFORE CLIENT.GET()");
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&start="
				+ 0 + "&v=1.0&q=" + Uri.encode(query),
				new JsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(JSONObject response) {
						JSONArray imageJsonResults = null;
						Log.d("DEBUG", "BEFORE PARSING RESPONSE");
						//now we need to parse that response to get the array
						try {
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							imageResults.clear();
							imageAdapter.addAll(ImageResult
									.fromJSONArray(imageJsonResults));
							Log.d("DEBUG", imageResults.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}	
					}

					@Override
					public void onFailure(Throwable arg0) {
						// TODO Auto-generated method stub
						Log.d("DEBUG", "Failed! - " + arg0.toString());
					}
				});
	}
}
