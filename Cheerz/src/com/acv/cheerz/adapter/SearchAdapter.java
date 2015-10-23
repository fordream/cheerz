package com.acv.cheerz.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.acv.cheerz.R;
import com.acv.cheerz.util.CheerzUtils;
import com.acv.cheerz.view.HomeItemBotomView;

public class SearchAdapter extends CursorAdapter {

	public SearchAdapter(Context context, Cursor c) {
		super(context, c);
	}

	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// if (convertView == null) {
	// convertView = new HomeItemBotomView(parent.getContext());
	// }
	//
	// ((HomeItemBotomView) convertView).setData(getItem(position));
	// ((HomeItemBotomView) convertView).loadUrl(getItem(position));
	//
	// final String url = CheerzUtils.getString(getItem(position), "img");
	//
	// ImageView imageView = (ImageView)
	// convertView.findViewById(R.id.imageView1);
	// final String artist_id = CheerzUtils.getString(getItem(position),
	// "artist_id");
	// convertView.findViewById(R.id.like).setOnClickListener(new
	// LikeArtist(artist_id));
	// return convertView;
	// }

	private class LikeArtist implements View.OnClickListener {
		private String artist_id;

		public LikeArtist(String artist_id) {
			this.artist_id = artist_id;
		}

		@Override
		public void onClick(View v) {
			likeArtist(artist_id);
		}
	}

	public void likeArtist(String artist_id) {

	}

	@Override
	public void bindView(View concertView, Context arg1, Cursor cursor) {
		if (concertView == null) {
			concertView = new HomeItemBotomView(arg1);
		}

		((HomeItemBotomView) concertView).setData(cursor, new LikeArtist(CheerzUtils.getString(cursor, "artist_id")));
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		return new HomeItemBotomView(arg0);
	}
}
