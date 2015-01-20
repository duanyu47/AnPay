package com.zplay.android.sdk.pay;

import android.app.Activity;
import pay.PL;
import pay.Payment;


public class ZplayPay {


    public static void pay(Activity paramActivity, String paramString, ZplayPayCallback paramZplayPayCallback)
    {
        Payment.doPayment(100, paramActivity, new PL(paramActivity,  paramString,  paramZplayPayCallback));
    }

    public static  void paySuccess(Activity paramActivity, String paramString, ZplayPayCallback paramZplayPayCallback)
    {

    }
}

