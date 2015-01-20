package pay;

import android.app.Activity;


public class Payment {
	
	public static final String STATUS_SUCCESSED = "PAY.SUCCESSED";
	
	public static final String STATUS_FAILED = "PAY.FAILED";
	
	public static final String STATUS_NO_SERVICE = "PAY.NO_SERVICE";
	
	public static final String STATUS_USER_CANCEL = "PAY.USER_CANCEL";
	
	public static PaymentListener listener;
	
	public static int fee;
	
	public static void doPayment(final int fee, final Activity activity, final PaymentListener listener) {
		activity.runOnUiThread( new Runnable() {
			@Override
			public void run() {
				Payment.fee = fee;
				Payment.listener = listener;
				Billing.doPay(activity);
			}
		});
	}

	
	public static void init(Activity activity) {
	}
}
