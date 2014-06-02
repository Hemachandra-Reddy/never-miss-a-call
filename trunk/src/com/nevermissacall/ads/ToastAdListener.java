

package com.nevermissacall.ads;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.nevermissacall.utils.ShowToasts;

/**
 * An ad listener that toasts all ad events.
 */
public class ToastAdListener extends AdListener {
    private Context mContext;

    public ToastAdListener(Context context) {
        this.mContext = context;        
    }

    @Override
    public void onAdLoaded() {
    	ShowToasts.show(mContext, "onAdLoaded()");        
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        String errorReason = "";
        switch(errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorReason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorReason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorReason = "Network Error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorReason = "No fill";
                break;
        }        
        ShowToasts.show(mContext, String.format("onAdFailedToLoad(%s)", errorReason));
    }

    @Override
    public void onAdOpened() {        
        ShowToasts.show(mContext, "onAdOpened()");
    }

    @Override
    public void onAdClosed() {        
        ShowToasts.show(mContext, "onAdClosed()");        
    }

    @Override
    public void onAdLeftApplication() {
    	ShowToasts.show(mContext, "onAdLeftApplication()");
    }
}
