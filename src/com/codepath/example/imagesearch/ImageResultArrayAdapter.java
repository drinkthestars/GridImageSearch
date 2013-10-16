package com.codepath.example.imagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {
	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, R.layout.item_image_result, images);
		
	}
	
	//translates java item and transforms it into a view
	//position - index of item in array
	//convertView - if a view has already been initialized, can reuse them
	//parent - giving u access to the parent GridView itself
	@Override	
	public View getView(int position, View convertView, ViewGroup parent) {
		//grab the image data
		ImageResult imageInfo = this.getItem(position);
		//need access to SmartImageView lib
		SmartImageView ivImage;
		
		//need to inflate our subview of each image
		//but first check to see if convertView exists, and if it does
		//we can reuse the view (don't have to inflate again)
		if(convertView == null) {
			//inflate out custom xml using layout inflater
			LayoutInflater inflater = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflater.inflate(R.layout.item_image_result, parent, false);
		} else {
			ivImage = (SmartImageView) convertView;
			//clear existing data in view
			ivImage.setImageResource(android.R.color.transparent);
		}
		//asynchronously load image from url
		ivImage.setImageUrl(imageInfo.getThumbUrl());
		return ivImage;
	}
}
