package jp.mirm.mirmapp.serverlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import jp.mirm.mirmapp.R;

import java.util.List;

public class ListActivity extends AppCompatActivity implements ListContract.View, ActivityCompat.OnRequestPermissionsResultCallback {

    private ListPresenter presenter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        presenter = new ListPresenter(this);
        presenter.processFirstRun();
        presenter.initialize();

        /*
        AdView mAdView = findViewById(R.id.listAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        findViewById(R.id.listAboutMiRmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openAboutMiRm();
            }
        });

        findViewById(R.id.howToUseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openHowToUse();
            }
        });

        findViewById(R.id.newServerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.createServer();
            }
        });

        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            presenter.createServerFromWeb(intent);
        }
    }

    @Override
    public void reloadList() {
        presenter.initList();
    }

    @Override
    public void setListItems(List<ListCellItem> items) {
        ListCellAdapter adapter = new ListCellAdapter(presenter, this, R.layout.cell_list, items);
        ((ListView) findViewById(R.id.serverListView)).setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getResources().getString(R.string.activity_list_dialog_wait));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ListPresenter.REQUEST_CODE_1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.dialog_list_message_nopermission)
                            .setPositiveButton(R.string.static_button_next, null)
                            .show();
                }
            }
        }
    }
}
