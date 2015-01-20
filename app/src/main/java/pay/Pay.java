package pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;


public class Pay {

	private Thread thread;
	
	private Handler handler;
	
	private Activity activity;
	
	public void doPay(Activity activity) {
		this.activity = activity;
		handler = new Handler();
		thread = new Thread( new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				if(!isSimReady()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							showLoadFailedDialog("SIM卡状态有误，请检测SIM卡！");
						}
					});
					return;
				}
				if(!isReady()) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							showLoadFailedDialog("网络不可用，请检测网络设置");
						}
					});
					return;
				}
				
				TelephonyManager telManager = (TelephonyManager) Pay.this.activity.getSystemService(Context.TELEPHONY_SERVICE);
				String operator = telManager.getNetworkOperator();
		    	if(operator == null) {
		    		operator = "";
		    	}
		    	String imei = telManager.getDeviceId();
		    	if(imei == null) {
		    		imei = "";
		    	}
		    	String imsi = telManager.getSubscriberId();
		    	if(imsi == null) {
		    		imsi = "";
		    	}  
		    	handler.post(new Runnable() {
					@Override
					public void run() {
						showLoadingDialog();
					}
		    	});
				if(new Item().doPay(imei, imsi, getNetType() == 0)) {
					if(dialog != null && dialog.isShowing()) {
						try{
							dialog.dismiss();
						} catch (Exception e){}
					}
					Billing.onOk();
				} else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							if(dialog != null && dialog.isShowing()) {
								try{
									dialog.dismiss();
								} catch (Exception e){}
							}
							showLoadFailedDialog("支付失败，未知错误");
						}
					});
					
				}
			}
		});
		thread.start();
	}
	
	private ProgressDialog dialog;
	public void showLoadingDialog() {
		dialog = new ProgressDialog(activity);
		((ProgressDialog)dialog).setMessage("正在支付，请保持网络畅通....");
		dialog.setCancelable(false);
		((ProgressDialog)dialog).setIndeterminate(false);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface iDialog1, int keyCode,
					KeyEvent event) {
				return true;	
			}
		});
		dialog.show();
	}
	
	
	public boolean isSimReady() {
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        if(TelephonyManager.SIM_STATE_READY !=tm.getSimState()) {
        	return false;
        }
        String imsi = tm.getSubscriberId();
       	return imsi != null && imsi.length() == 15;
	}
	
	
	public void showLoadFailedDialog(String info) {
		AlertDialog.Builder builder = new Builder(activity); 
		builder.setMessage(info);
		builder.setTitle("支付失败");
		builder.setCancelable(false);
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Billing.onFailed();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
	}
	
	public int getNetType() {
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo info = cm.getActiveNetworkInfo(); 
		if(info == null || !info.isAvailable()) {
			return -1;
		}
		if(info.getType() == ConnectivityManager.TYPE_MOBILE) {
			if(info.getExtraInfo().toLowerCase().indexOf("wap") != -1) {
				return 0;
			}
			return 1;
		}
		return 2;
	}
	
	public boolean isAvalible() {
		return getNetType() >= 0; 
	}
	
	public boolean isReady() {
		return isAvalible();
	}
}
