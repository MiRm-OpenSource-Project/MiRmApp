package jp.mirm.mirmapp.login;

import android.app.Activity;

public interface LoginContract {

    interface View {
        void showProgress();
        void hideProgress();
        void showMissingLoginAlert();
        void showBannedAlert();
        Activity getActivity();
    }

    interface Presenter {
        void initialize();
        void login(String serverId, String password, boolean save);
        void openAboutMiRm();
        void openCreateServer();
    }

}
