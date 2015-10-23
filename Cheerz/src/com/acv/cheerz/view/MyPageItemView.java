package com.acv.cheerz.view;

import org.json.JSONObject;

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
public class MyPageItemView extends LinearLayout {
	private ImageView imageView;
	private TextView tv1, tv2, tv3, tv4;

	public MyPageItemView(Context context) {
		super(context);
		init();
	}

	public MyPageItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.mypage_item, this);
		imageView = (ImageView) findViewById(R.id.imageView1);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		tv4 = (TextView) findViewById(R.id.tv4);

	}

	public void setData(JSONObject object) {
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
		final String url = CheerzUtils.getString(jsonObject, "img");
	}
}