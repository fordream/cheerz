package com.acv.cheerz.dialog;

import z.lib.base.LibsBaseAdialog;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.acv.cheerz.R;
import com.acv.cheerz.view.HeaderView;
import com.acv.cheerz.view.HeaderView.IOnClickHeader;

public class WebDialog extends LibsBaseAdialog {
	public WebDialog(Context context) {
		super(context);
	}

	private String title, url;

	public void setData(String title, String url) {
		this.title = title;
		this.url = url;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WebView web = (WebView) findViewById(R.id.web);
		web.setWebViewClient(new WebViewClient());
		web.setWebChromeClient(new WebChromeClient());
		web.loadUrl(url);
		
		findViewById(R.id.main).startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.scale));

		HeaderView headerView = (HeaderView) findViewById(R.id.headerView1);
		headerView.visibivityRight(false, R.drawable.transfer);
		headerView.visibivityLeft(true, R.drawable.btn_back);
		headerView.setTextHeader(title);
		headerView.setiOnClickHeader(new IOnClickHeader() {
			@Override
			public void onClickRight() {

			}

			@Override
			public void onClickLeft() {
				Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_to_center);
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						dismiss();
					}
				});
				findViewById(R.id.main).startAnimation(animation);

			}
		});

	}

	@Override
	public int getLayout() {
		return R.layout.web;
	}

}
