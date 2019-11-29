package jp.mirm.mirmapp.web;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

public class WebPresenter implements WebContract.Presenter {

    private WebContract.View view;

    WebPresenter(WebContract.View view) {
        this.view = view;
    }

    @Override
    public void finish() {
        view.getActivity().finish();
    }

    @Override
    public void goBack() {
        if (view.getWebView().canGoBack()) view.getWebView().goBack();
    }

    @Override
    public void goForward() {
        if (view.getWebView().canGoForward()) view.getWebView().goForward();
    }

    @Override
    public void openWithBrowser(String uri) {
        Uri uri1 = Uri.parse(uri);
        view.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri1));
    }

}
