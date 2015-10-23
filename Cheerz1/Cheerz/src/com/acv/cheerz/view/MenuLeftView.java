package com.acv.cheerz.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.util.CheerzUtils;

//com.acv.cheerz.view.MenuLeftView
public class MenuLeftView extends LinearLayout {
	private ListView menuleftlist;
	private View menu_left_header_register;
	private View menu_left_header_infor;

	public MenuLeftView(Context context) {
		super(context);

		init();
	}

	public MenuLeftView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.menu_left, this);
		icon = (CustomImageView) findViewById(R.id.icon);
		menuleftlist = (ListView) findViewById(R.id.menuleftlist);
		menu_left_header_infor = findViewById(R.id.menu_left_header_infor);
		menu_left_header_register = findViewById(R.id.menu_left_header_register);
	}

	private CustomImageView icon;
	private Integer[] resStr = new Integer[] {};
	private Integer[] resIconStr = new Integer[] {};

	public void initData() {
		resStr = new Integer[] {//
		R.string.edit_profile,//
				R.string.activities_log,//
				R.string.earn_points,//
				R.string.favorite,//
				R.string.logout,//
				R.string.artists,//
				R.string.ranking,//
				R.string.setting };//

		resIconStr = new Integer[] {//
		R.drawable.m1,//
				R.drawable.m2,//
				R.drawable.m3,//
				R.drawable.menu_left_favorite,//
				R.drawable.m4,//
				R.drawable.m5,//
				R.drawable.m6,//
				R.drawable.m7 };//
		if (!CheerzUtils.isLogin(getContext())) {
			resStr = new Integer[] {//
			R.string.artists,//
					R.string.ranking,//
					R.string.setting };//

			resIconStr = new Integer[] {//
			R.drawable.m5,//
					R.drawable.m6,//
					R.drawable.m7 };//
		}

		menu_left_header_infor.setVisibility(CheerzUtils.isLogin(getContext()) ? View.VISIBLE : View.GONE);
		menu_left_header_register.setVisibility(!CheerzUtils.isLogin(getContext()) ? View.VISIBLE : View.GONE);
		icon.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.avatar_example));

		menuleftlist.setAdapter(new ArrayAdapter<Integer>(getContext(), 0, resStr) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					convertView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.menu_left_item, null);
				}

				int resStr = getItem(position);
				// convertView.findViewById(R.id.menu_left_main).setBackgroundColor(resStr
				// == R.string.edit_profile ?
				// getResources().getColor(R.color.ebe7ef) : Color.TRANSPARENT);

				convertView.findViewById(R.id.menu_left_main).setBackgroundResource(resStr == R.string.edit_profile ? R.drawable.menu_left_chooser : R.drawable.transfer);
				if (position == 0 && resStr == R.string.artists) {
					convertView.findViewById(R.id.menu_left_main).setBackgroundResource(R.drawable.menu_left_chooser);
				}

				convertView.findViewById(R.id.item1).setVisibility(resStr == R.string.edit_profile ? View.VISIBLE : View.GONE);
				convertView.findViewById(R.id.item2).setVisibility(!(resStr == R.string.edit_profile) ? View.VISIBLE : View.GONE);

				convertView.findViewById(R.id.item1).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
				convertView.findViewById(R.id.item2).setVisibility(position != 0 ? View.VISIBLE : View.GONE);

				convertView.findViewById(R.id.menu_space).setVisibility(resStr == R.string.logout || resStr == R.string.ranking ? View.VISIBLE : View.GONE);

				convertView.findViewById(R.id.menu_left_top).setVisibility((resStr == R.string.edit_profile || resStr == R.string.setting) ? View.VISIBLE : View.GONE);

				((TextView) convertView.findViewById(R.id.item1)).setText(resStr);
				((TextView) convertView.findViewById(R.id.item2)).setText(resStr);

				ImageView menu_icon = (ImageView) convertView.findViewById(R.id.menu_icon);
				menu_icon.setImageResource(resIconStr[position]);
				
				convertView.findViewById(R.id.item1).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						iMenuLeftOnClick.onClickItem(v.getId());
						
					}
				});

				return convertView;
			}
		});

		menu_left_header_register.findViewById(R.id.login).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				iMenuLeftOnClick.onClickItem(v.getId());
			}
		});

		menu_left_header_register.findViewById(R.id.register).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				iMenuLeftOnClick.onClickItem(v.getId());

			}
		});
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		menuleftlist.setOnItemClickListener(onItemClickListener);
	}

	private IMenuLeftOnClick iMenuLeftOnClick;

	public void setiMenuLeftOnClick(IMenuLeftOnClick iMenuLeftOnClick) {
		this.iMenuLeftOnClick = iMenuLeftOnClick;
	}

	public interface IMenuLeftOnClick {
		public void onClickItem(int resId);
	}
}