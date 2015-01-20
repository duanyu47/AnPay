package pay;

import android.app.Activity;

import com.zplay.android.sdk.pay.ZplayPayCallback;
import com.zplay.android.sdk.pay.ZplayPay;

//import org.cocos2dx.cpp.IAPJni;

public class PL implements PaymentListener{
	 private Activity paramActivity;
	 private String paramString;
	 private ZplayPayCallback paramZplayPayCallback;
	 public PL(Activity paramActivity, String paramString, ZplayPayCallback paramZplayPayCallback) {
		this.paramActivity = paramActivity;
		this.paramString = paramString;
		this.paramZplayPayCallback = paramZplayPayCallback;

	}
	@Override
	public void onResult(String status, int requestFee, int payFee) {

            if(status==Payment.STATUS_SUCCESSED)
            {paramZplayPayCallback.callback(1,requestFee+"","支付成功");}
            if (status==Payment.STATUS_USER_CANCEL)
            {paramZplayPayCallback.callback(0,requestFee+"","支付取消");}
            if(status==Payment.STATUS_FAILED)
            {paramZplayPayCallback.callback(2,requestFee+"","支付失败");}



	}

}
