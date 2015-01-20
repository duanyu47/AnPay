package pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Looper;

public class Billing {
	static Pay pay = new Pay();
	public static void onOk() {
		Payment.listener.onResult(Payment.STATUS_SUCCESSED, Payment.fee, Payment.fee );
	}
	
	public static void onCancel() {
		Payment.listener.onResult(Payment.STATUS_USER_CANCEL, Payment.fee, Payment.fee );
	}
	
	public static void onFailed() {
		Payment.listener.onResult(Payment.STATUS_FAILED, Payment.fee, Payment.fee );
	}
	
	public static void doPay(final Activity context) {
		
		
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("确认进行支付吗?");
		builder.setTitle("支付确认");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				pay.doPay(context);
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				onCancel();
			}
		});
		
		Dialog dialogConfirm = builder.create();
		dialogConfirm.show();
	}
	
	private static ProgressDialog dialog;
	
}
