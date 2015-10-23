package z.lib.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

public abstract class LibsBaseAdialog extends Dialog {

	public LibsBaseAdialog(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setCancelable(false);
		if (getLayout() != 0)
			setContentView(getLayout());
	}

	public abstract int getLayout();

	public void showDialogMessage(String message) {
		CommonAndroid.showDialog(getContext(), message, null);
	}
}