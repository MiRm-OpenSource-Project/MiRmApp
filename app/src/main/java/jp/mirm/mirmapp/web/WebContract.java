package jp.mirm.mirmapp.web;

import android.app.Activity;
import android.webkit.WebView;

public interface WebContract {

    interface View {
        Activity getActivity();
        WebView getWebView();
    }

    interface Presenter {
        void finish();
        void goBack();
        void goForward();
        void openWithBrowser(String uri);
    }

}
