package jp.mirm.mirmapp.servercontroller;

import android.app.Activity;

public interface ControllerContract {

    interface View {
        void setProgressValue(int value);
        void setProgressMaxValue(int value);
        void setExpireTimeString(String text);
        void setServerName(String name);
        void setStatus(int id);
        void setInDeterminateMode(boolean bool);
        void showConfirmExpireDialog();
        void showExpireStatusToast(int id);

        Activity getActivity();
    }

    interface Presenter {
        void initialize();
        void startServer();
        void stopServer();
        void forceStopServer();
        void openPluginManager();
        void openCrushDump();
        void openExpireTime();
        void reloadExpireTime();
        void onExpireComplete(String status);
        void setExpireTime(long time);
        long getExpireTime();
    }

}
