package com.baraccasoftware.sfc.simplefacebookclient;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.baraccasoftware.sfc.simplefacebookclient.util.Util;
import com.baraccasoftware.sfc.simplefacebookclient.view.SFCWebView;

import java.net.CookieManager;


public class MainActivity extends Activity {
    private MainFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragment = new MainFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }

        if(Util.versionIsOlderThanLollipop()){
            //avvio il controllo dei cookie
            startSyncCookies();
        }

        if(!Util.isConnectingToInternet(this)){
            Toast.makeText(this,R.string.no_connection,Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reload) {
            if(fragment!= null){
                fragment.reloadWebView();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(Util.versionIsOlderThanLollipop()){
            CookieSyncManager.getInstance().sync();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.versionIsOlderThanLollipop()){
            CookieSyncManager.getInstance().stopSync();
        }
    }

    @Override
    public void onBackPressed() {
        if(!fragment.webViewback()) {
            super.onBackPressed();
        }
    }

    private void startSyncCookies(){
        CookieSyncManager.createInstance(this);
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MainFragment extends Fragment implements SFCWebView.OnScrollChangedCallback {

        private SFCWebView webView;
        private String url = "https://www.facebook.com";
        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            webView = (SFCWebView) rootView.findViewById(R.id.fwebView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true); //forse potremmo metterlo nelle impostazioni
            webView.setWebViewClient(new SFCWebViewClient());
            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setAllowFileAccess(true);
//            webView.getSettings().setAllowFileAccessFromFileURLs(true);
//            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            String cachePath = getActivity().getCacheDir().getAbsolutePath();
            webView.getSettings().setAppCachePath(cachePath);
            webView.setOnScrollChangedCallback(this);
            webView.loadUrl(url);//carico fb


            return rootView;
        }



        public void reloadWebView(){
            if(webView!=null)webView.reload();
        }

        public boolean webViewback(){
            if(webView.canGoBack()){
                webView.goBack();
                return true;
            }else {
                return false;
            }


        }

        @Override
        public void onScroll(int l, int t,int oldl,int oldt) {
            int d = t-oldt;
            if(Math.abs(d)>15) {
                if (d > 0) {
                    getActivity().getActionBar().hide();
                } else {
                    getActivity().getActionBar().show();
                }
            }
        }


        class SFCWebViewClient extends WebViewClient {
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                //checkMenu();
                //setRefreshActionButtonState(true);

            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                //checkMenu();
                //setRefreshActionButtonState(false);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                if (errorCode == ERROR_TIMEOUT) {
                    view.stopLoading();  // may not be needed
                    //view.loadData(timeoutMessageHtml, "text/html", "utf-8");
                }
            }
        }
    }


}
