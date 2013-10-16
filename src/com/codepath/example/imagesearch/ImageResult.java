package com.codepath.example.imagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//this class models the 'image' that we get back. it will contain two urls:
//one for the actual image, and another for a thumbnail of the image which
//is what we are actually going to display
public class ImageResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -760077580686300090L;
	private String fullUrl;
	private String thumbUrl;
	
	//Constructor accepts a JSON obj
	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch(JSONException e) {
			this.fullUrl = null;
			this.thumbUrl= null;
		}
	}
	
	public String getFullUrl(){
		return fullUrl;
	}
	
	public String getThumbUrl(){
		return thumbUrl;
	}
	
	public String toString() {
		return this.thumbUrl;
	}

	public static ArrayList<ImageResult> fromJSONArray(
			JSONArray imageJsonResults) {
		
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int i = 0; i < imageJsonResults.length(); i++) {
			try {
				results.add(new ImageResult (imageJsonResults.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return results;
	}
}
