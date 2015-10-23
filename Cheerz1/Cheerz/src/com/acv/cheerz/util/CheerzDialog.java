package com.acv.cheerz.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;
import android.widget.TextView;

import com.acv.cheerz.R;

public class CheerzDialog {

	private Context mContext;
	private Dialog mDialog;
	private TextView txtMsg;
	private Button btnOk;
	public CheerzDialog(Context ctx){
		mContext = ctx;
		mDialog = new Dialog(ctx);
	}
	
	public void showDialog(){
		if (mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
		}
	}
	
	public void initDialogConfirm(){
		mDialog.setContentView(R.layout.custom_dialog_confirm);
		txtMsg = (TextView)mDialog.findViewById(R.id.textView1);
		btnOk = (Button)mDialog.findViewById(R.id.button1);
	}
	
	public void setMsgDialog(String msg){
		txtMsg.setText(msg);
	}
	
	public void initDialogBuilder(){
//		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		
	}
	
	/**
	 * show dialog 
	 * @param title
	 * @param msg
	 */
	public void showDialog(String title,String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// code
				
			}
		});
		
		Dialog dialog = builder.create();
		dialog.show();
	}
	
}
