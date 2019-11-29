package jp.mirm.mirmapp.login;

import android.content.Intent;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.PublicAPI;
import jp.mirm.mirmapp.data.task.LoadAdTask;
import jp.mirm.mirmapp.data.task.LoginTask;
import jp.mirm.mirmapp.utils.AdHolder;
import jp.mirm.mirmapp.utils.URLHolder;
import jp.mirm.mirmapp.utils.Utils;

public class LoginPresenter implements LoginContract.Presenter {

    private final LoginContract.View view;
    private final Intent intent;

    LoginPresenter(LoginContract.View view, Intent intent) {
        this.view = view;
        this.intent = intent;
    }

    @Override
    public void initialize() {
        if (!AdHolder.isLoaded()) {
            LoadAdTask task = new LoadAdTask();
            task.execute();
        }

        if (intent.getBooleanExtra("isLogin", false)) {
            EditText id = view.getActivity().findViewById(R.id.loginServerId);
            id.setText(intent.getStringExtra("serverId"), TextView.BufferType.SPANNABLE);
            id.setFocusable(false);

            Switch autoLogin = view.getActivity().findViewById(R.id.saveLoginData);
            autoLogin.setChecked(intent.getBooleanExtra("autoLogin", false));

            if (intent.getBooleanExtra("autoLogin", false)) {
                EditText password = view.getActivity().findViewById(R.id.loginPassword);
                password.setText(Utils.decrypt(intent.getStringExtra("password"), view.getActivity().getResources().getString(R.string.data_salt)));
                login(intent.getStringExtra("serverId"), Utils.decrypt(intent.getStringExtra("password"), view.getActivity().getResources().getString(R.string.data_salt)), true);
            }
        }

        if (intent.getBooleanExtra("fromWeb", false)) {
            EditText id = view.getActivity().findViewById(R.id.loginServerId);
            id.setText(intent.getStringExtra("serverId"), TextView.BufferType.SPANNABLE);

            if (intent.getStringExtra("password") != null) {
                EditText password = view.getActivity().findViewById(R.id.loginPassword);
                password.setText(intent.getStringExtra("password"), TextView.BufferType.SPANNABLE);
            }
        }
    }

    @Override
    public void login(String serverId, String password, boolean save) {
        LoginTask task = new LoginTask(view);
        task.execute(serverId, password, String.valueOf(save));
    }

    @Override
    public void openAboutMiRm() {
        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_MAINPAGE, "");
    }

    @Override
    public void openCreateServer() {
        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_CREATE_SERVER, "");
    }

}
