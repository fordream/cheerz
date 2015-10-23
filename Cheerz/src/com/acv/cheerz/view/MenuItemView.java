package com.acv.cheerz.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acv.cheerz.R;

//com.acv.cheerz.view.MenuItemView
public class MenuItemView extends LinearLayout {

	public MenuItemView(Context context) {
		super(context);

		init();
	}

	public MenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.menu_left_item, this);
	}

	public void initData(int position, int resStr, int icon) {

		findViewById(R.id.menu_left_main).setBackgroundResource(resStr == R.string.edit_profile ? R.drawable.menu_left_chooser : R.drawable.transfer);
		if (position == 0 && (resStr == R.string.artists || resStr == R.string.logout)) {
			findViewById(R.id.menu_left_main).setBackgroundResource(R.drawable.menu_left_chooser);
		}

		findViewById(R.id.item1).setVisibility(resStr == R.string.edit_profile ? View.VISIBLE : View.GONE);
		findViewById(R.id.item2).setVisibility(!(resStr == R.string.edit_profile) ? View.VISIBLE : View.GONE);

		findViewById(R.id.item1).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
		findViewById(R.id.item2).setVisibility(position != 0 ? View.VISIBLE : View.GONE);

		findViewById(R.id.menu_space).setVisibility(resStr == R.string.logout || resStr == R.string.ranking ? View.VISIBLE : View.GONE);

		findViewById(R.id.menu_left_top).setVisibility((resStr == R.string.edit_profile || resStr == R.string.setting) ? View.VISIBLE : View.GONE);

		((TextView) findViewById(R.id.item1)).setText(resStr);
		((TextView) findViewById(R.id.item2)).setText(resStr);

		ImageView menu_icon = (ImageView) findViewById(R.id.menu_icon);
		menu_icon.setImageResource(icon);

	}

}