package com.acv.cheerz.view;

import org.json.JSONObject;

import z.lib.base.ImageLoaderUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.util.CheerzUtils;

//com.acv.cheerz.view.HeaderView
public class HomeItemBotomView extends LinearLayout {
	private ImageView imageView;
	private TextView tv1, tv2, tv3, tv4;

	public HomeItemBotomView(Context context) {
		super(context);
		init();
	}

	public HomeItemBotomView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.home_item_bot, this);
		imageView = (ImageView) findViewById(R.id.imageView1);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);

	}

	public void setData(JSONObject object) {

		// "artist_id":"0233281",
		// "image_id":"0231",
		// "artist_name":"name artist",
		// "link_img":"http://192.168.2.5/anhlx/cheerz/uploads/images/01/images-01.jpg",
		// "status":"text","date":"20/01/2014",
		// "status_vote":"1",
		// "content":"id video hoặc image",
		// "total_vote":"500"

		// imageView.setBackgroundResource(new Random().nextBoolean() ?
		// R.drawable.example_1 : R.drawable.example_2);
		tv1.setText(CheerzUtils.getString(object, "artist_name"));
		tv2.setText(CheerzUtils.getString(object, "status_vote"));
		tv3.setText(CheerzUtils.getString(object, "date"));
		tv4.setText(CheerzUtils.getString(object, "content"));
	}

	public void setDataBitmap(Bitmap bm) {

		if (bm == null) {
			bm = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
		}
		imageView.setImageBitmap(bm);
	}

	public void loadUrl(JSONObject jsonObject) {
		final String url = CheerzUtils.getString(jsonObject, "link_img");
		ImageLoaderUtils.getInstance(getContext()).DisplayImageRound(url, imageView, BitmapFactory.decodeResource(getResources(), R.drawable.noimage));
	}
}