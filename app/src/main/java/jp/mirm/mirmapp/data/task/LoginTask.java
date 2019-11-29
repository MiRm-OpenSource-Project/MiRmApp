package jp.mirm.mirmapp.data.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.servermain.MainActivity;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.login.LoginContract;
import jp.mirm.mirmapp.utils.Property;
import jp.mirm.mirmapp.utils.Utils;

import java.io.File;

public class LoginTask extends AsyncTask<String, Void, Integer> {

    private final LoginContract.View view;

    public LoginTask(LoginContract.View view) {
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        view.showProgress();
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String serverId = strings[0];
        String password = strings[1];
        boolean save = Boolean.parseBoolean(strings[2]);

        int result = MiRmAPI.login(serverId, password);

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/servers/" + serverId + "/");
        File prop = new File(Environment.getExternalStorageDirectory().getPath() + "/servers/" + serverId + "/setting.properties");
        File log = new File(Environment.getExternalStorageDirectory().getPath() + "/servers/" + serverId + "/console.log");

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (!log.exists()) {
            Utils.writeFile(Environment.getExternalStorageDirectory().getPath() + "/servers/" + serverId + "/console.log", "");
        }

        Property property = new Property(serverId);
        property.setAutoLogin(save);
        property.setServerId(serverId);

        if (!prop.exists()) {
            property.setPassword(Utils.encrypt(password, view.getActivity().getResources().getString(R.string.data_salt)));
            property.setNextReward(0);
            property.setConsoleTick(50);
            property.setSaveLog(true);
        }

        property.save();

        String fcmToken = Utils.readFile("FCMToken.dat");
        if (fcmToken != null) {
            MiRmAPI.sendToken(fcmToken);
        }

        return result;
    }

    @Override
    protected void onPostExecute(Integer status) {
        view.hideProgress();

        switch (status) {
            case MiRmAPI.LOGIN_SUCCESS: {
                Intent intent = new Intent(view.getActivity().getApplication(), MainActivity.class);
                view.getActivity().startActivity(intent);
                view.getActivity().finish();
                break;
            }

            case MiRmAPI.LOGIN_BANNED: {
                view.showBannedAlert();
                break;
            }

            case MiRmAPI.LOGIN_ERROR: {
                view.showMissingLoginAlert();
                break;
            }
        }

    }

}
