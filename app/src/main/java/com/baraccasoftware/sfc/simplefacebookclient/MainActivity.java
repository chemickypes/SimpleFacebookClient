package com.baraccasoftware.sfc.simplefacebookclient;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baraccasoftware.sfc.simplefacebookclient.util.Util;

import java.net.CookieManager;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        if(Util.versionIsOlderThanLollipop()){
            //avvio il controllo dei cookie
            startSyncCookies();
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
        if (id == R.id.action_settings) {
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

    private void startSyncCookies(){
        CookieSyncManager.createInstance(this);
        android.webkit.CookieManager.getInstance().setAcceptCookie(true);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private WebView webView;
        private String url = "https://www.facebook.com";
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            webView = (WebView) rootView.findViewById(R.id.fwebView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setLoadsImagesAutomatically(true); //forse potremmo metterlo nelle impostazioni
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);//carico fb


            return rootView;
        }
    }
}
