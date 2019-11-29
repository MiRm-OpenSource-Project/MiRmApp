package jp.mirm.mirmapp.web;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import java.lang.ref.WeakReference;

final class WebViewClient extends android.webkit.WebViewClient {

    private final WeakReference<WebActivity> weakReference;
    private final String endPoint;

    WebViewClient(WebActivity activity, String endPoint) {
        this.weakReference = new WeakReference<>(activity);
        this.endPoint = endPoint;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (url.equals(endPoint)) {
            weakReference.get().finish();

        } else if (url.startsWith("mirm://")) {
            weakReference.get().finish();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            weakReference.get().startActivity(intent);
        }
    }
}