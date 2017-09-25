package vun.zisplay.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import vun.zisplay.R;

public class PaymentView extends ZisplayBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_view);
        analytics(this.getLocalClassName());
        WebView webview=(WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.addJavascriptInterface(new javaScriptInterface(this), "Android");
        webview.setWebViewClient(new chromeClient());
        Intent i=getIntent();
        String url=i.getStringExtra("url");
        webview.loadUrl(url);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment_view, menu);
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

    class chromeClient extends WebViewClient
    {

        public void onProgressChanged(WebView view, int progress) {
            // Activities and WebViews measure progress with different scales.
            // The progress meter will automatically disappear when we reach 100%
            PaymentView.this.setProgress(progress * 1000);
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(PaymentView.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
        }
//        @Override
//        public boolean  shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//            String url2="http://www.playbuzz.org/";
//            // all links  with in ur site will be open inside the webview
//            //links that start ur domain example(http://www.example.com/)
//            if (url != null && url.startsWith(url2)){
//                return false;
//            }
//            // all links that points outside the site will be open in a normal android browser
//            else  {
//                view.getContext().startActivity(
//                        new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                return true;
//            }


    }

    public class javaScriptInterface {
        Context mContext;

        javaScriptInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void showToast(String toast){
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
        @JavascriptInterface
        public void openAndroidDialog(String title,String message){
            AlertDialog.Builder myDialog
                    = new AlertDialog.Builder(mContext);
            myDialog.setTitle(title);
            myDialog.setMessage(message);
            myDialog.setPositiveButton("Ok", null);
            myDialog.show();
        }
        @JavascriptInterface
        public void goBack() {
            PaymentView.this.runOnUiThread(new Runnable() {
                public void run() {
                    onBackPressed();
                }
            });

        }

    }
}
