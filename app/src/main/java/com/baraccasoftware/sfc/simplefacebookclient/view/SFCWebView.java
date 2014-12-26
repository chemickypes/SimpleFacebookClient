package com.baraccasoftware.sfc.simplefacebookclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by angelomoroni on 27/12/14.
 */
public class SFCWebView extends WebView {

    private OnScrollChangedCallback mOnScrollChangedCallback;

    public SFCWebView(Context context) {
        super(context);
    }

    public SFCWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SFCWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt)
    {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mOnScrollChangedCallback != null) mOnScrollChangedCallback.onScroll(l, t,oldl,oldt);
    }

    public OnScrollChangedCallback getOnScrollChangedCallback()
    {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback)
    {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public static interface OnScrollChangedCallback
    {
        public void onScroll(int l, int t,int oldl, int oldt);
    }


}
