package jp.mirm.mirmapp.web;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import jp.mirm.mirmapp.R;

public class WebActivity extends AppCompatActivity implements WebContract.View {

    private WebPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Bundle bundle = getIntent().getExtras();
        String url = getIntent().getExtras().getString("url");

        this.presenter = new WebPresenter(this);

        final WebView web = findViewById(R.id.web_view);

        findViewById(R.id.web_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goBack();
            }
        });

        findViewById(R.id.web_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goForward();
            }
        });

        findViewById(R.id.web_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.finish();
            }
        });

        findViewById(R.id.web_browser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openWithBrowser(web.getUrl());
            }
        });

        web.getSettings().setUserAgentString("MiRmApp");
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(url);
        web.setWebViewClient(new WebViewClient(this, bundle.getString("endPoint")));
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public WebView getWebView() {
        return (WebView) findViewById(R.id.web_view);
    }
}
