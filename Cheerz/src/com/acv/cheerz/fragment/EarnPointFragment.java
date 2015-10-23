package com.acv.cheerz.fragment;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acv.cheerz.R;
import com.acv.cheerz.billing.IabHelper;
import com.acv.cheerz.billing.IabResult;
import com.acv.cheerz.billing.Inventory;
import com.acv.cheerz.billing.Purchase;
import com.acv.cheerz.util.CheerzUtils;

public class EarnPointFragment  extends Fragment implements OnClickListener{

	private String TAG = "EarnPointFragment";
	private Button btn100;
	private Button btn250;
	private Button btn500;
	private Button btn1000;
	private Button btn2500;
	private Button btn4900;
	private TextView txtPolicy;
	private ImageButton btnBack;
	private ImageButton btnHome;
	private TextView txtTitle;
	
	private IabHelper iabHelper;
	
	static final String SKU_BLOCK2 = "block2";
	static final String SKU_INFINITE_BLOCK2 = "block2";
	private boolean isSKUBlock = false;
	static final int RC_REQUEST = 10001;
	
	private String[] arrBlock = {
			"block1","block2","block3","block4","block5","block6"
	};
	
	public EarnPointFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		View earnView = inflater.inflate(R.layout.earn_point_layout, null);
		btn100 = (Button)earnView.findViewById(R.id.earn_100_id);
		btn250 = (Button)earnView.findViewById(R.id.earn_250_id);
		btn500 = (Button)earnView.findViewById(R.id.earn_500_id);
		btn1000 = (Button)earnView.findViewById(R.id.earn_1000_id);
		btn2500 = (Button)earnView.findViewById(R.id.earn_2500_id);
		btn4900 = (Button)earnView.findViewById(R.id.earn_4900_id);
		txtPolicy = (TextView)earnView.findViewById(R.id.earn_txtpolicy_id);
		txtPolicy.setPaintFlags(txtPolicy.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
		
		btnBack = (ImageButton)earnView.findViewById(R.id.header_btn_left);
		btnHome = (ImageButton)earnView.findViewById(R.id.header_btn_right);
		txtTitle = (TextView)earnView.findViewById(R.id.header_title);
		txtTitle.setText("BUY POINT");
		txtTitle.setTextColor(getResources().getColor(R.color.ebe7ef));
		
		btnHome.setVisibility(View.INVISIBLE);
		btnBack.setImageResource(R.drawable.btn_back);
		
		btnBack.setOnClickListener(this);
		btn100.setOnClickListener(this);
		btn250.setOnClickListener(this);
		btn500.setOnClickListener(this);
		btn1000.setOnClickListener(this);
		btn2500.setOnClickListener(this);
		btn4900.setOnClickListener(this);
		txtPolicy.setOnClickListener(this);
		
		// implement in-app billing
		//iabHelper.enableDebugLogging(true);
		
		iabHelper = new IabHelper(getActivity(), CheerzUtils.PUBLIC_KEY);
		iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			
			@Override
			public void onIabSetupFinished(IabResult result) {
				if (result.isSuccess()) {
					Log.i(TAG, "billing success : " + result);
				}else{
					Log.i(TAG, "billing fail : " + result);
				}
				
				if(iabHelper == null) return;
				iabHelper.queryInventoryAsync(inventoryFinishedListener);
			}
		});
		
		
		
		
		
		return earnView;
	}
	
	 /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        Log.i(TAG, "payload = " + payload);
        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.earn_100_id:
			String payload1 = "";
			iabHelper.launchPurchaseFlow(getActivity(), arrBlock[0], RC_REQUEST, mPurchaseFinishedListener, payload1);
			break;
		case R.id.earn_250_id:
			String payload2 = "";
			iabHelper.launchPurchaseFlow(getActivity(), arrBlock[1], RC_REQUEST, mPurchaseFinishedListener, payload2);
			break;
		case R.id.earn_500_id:
			String payload3 = "";
			iabHelper.launchPurchaseFlow(getActivity(), arrBlock[2], RC_REQUEST, mPurchaseFinishedListener, payload3);
			break;
		case R.id.earn_1000_id:
			String payload4 = "";
			iabHelper.launchPurchaseFlow(getActivity(), arrBlock[3], RC_REQUEST, mPurchaseFinishedListener, payload4);
			break;
		case R.id.earn_2500_id:
			String payload5 = "";
			iabHelper.launchPurchaseFlow(getActivity(), arrBlock[4], RC_REQUEST, mPurchaseFinishedListener, payload5);
			break;
		case R.id.earn_4900_id:
			String payload6 = "";
			iabHelper.launchPurchaseFlow(getActivity(), arrBlock[5], RC_REQUEST, mPurchaseFinishedListener, payload6);
			break;
		case R.id.header_btn_left:
			getActivity().finish();
			break;
		
		default:
			break;
		}
		
	}
	// Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (iabHelper == null) return;

            if (result.isFailure()) {
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(SKU_BLOCK2)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                iabHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }else{
            	
            }
            
        }
    };
	
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (iabHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                
            }
            else {
            	Log.i(TAG,"Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };
	
	IabHelper.QueryInventoryFinishedListener inventoryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
		
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {
			if (iabHelper == null) {
				return;
			}
			
			if (result.isFailure()) {
				Log.i(TAG, "result fail : " + result);
				return;
			}
			
			// purchase
			Purchase block1Purchase = inv.getPurchase(SKU_BLOCK2);
			isSKUBlock = (block1Purchase != null && verifyDeveloperPayload(block1Purchase));
			Log.d(TAG, "User is " + (isSKUBlock ? "Block1" : "NOT Block2"));
			
		}
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (iabHelper != null) {
			iabHelper.dispose();
			iabHelper = null;
		}
	}
}
