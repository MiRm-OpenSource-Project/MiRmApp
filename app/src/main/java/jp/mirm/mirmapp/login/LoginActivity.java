package jp.mirm.mirmapp.login;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.progress.ProgressDialog;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private static LoginActivity instance;
    private ProgressDialog progressDialog;
    private LoginPresenter presenter;

    private boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instance = this;
        presenter = new LoginPresenter(this, getIntent());
        presenter.initialize();

        /*
        AdView mAdView = findViewById(R.id.loginAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        findViewById(R.id.createServerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.activity_login_snackbar_message_createserver, Snackbar.LENGTH_LONG)
                        .setAction(R.string.activity_login_snackbar_button_create, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                presenter.openCreateServer();
                            }
                        }).show();
            }
        });

        findViewById(R.id.aboutMiRmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openAboutMiRm();
            }
        });

        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText id = findViewById(R.id.loginServerId);
                EditText pass = findViewById(R.id.loginPassword);
                presenter.login(id.getText().toString(), pass.getText().toString(), isChecked);
            }
        });

        Switch save = findViewById(R.id.saveLoginData);
        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isChecked = b;
            }
        });

    }

    @Override
    public void showProgress() {
        progressDialog = new ProgressDialog(this)
                .create()
                .setText(R.string.activity_login_dialog_message_login)
                .show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    @Override
    public void showMissingLoginAlert() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.static_message_error)
                .setMessage(R.string.activity_login_dialog_message_login_error)
                .setPositiveButton(R.string.static_button_back, null)
                .show();
    }

    @Override
    public void showBannedAlert() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.static_message_error)
                .setMessage(R.string.activity_login_dialog_message_login_error)
                .setPositiveButton(R.string.static_button_back, null)
                .show();
    }

    @Override
    public LoginActivity getActivity() {
        return this;
    }

    public static LoginActivity getInstance() {
        return instance;
    }

}
