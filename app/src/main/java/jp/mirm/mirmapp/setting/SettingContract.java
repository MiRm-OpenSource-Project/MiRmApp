package jp.mirm.mirmapp.setting;

import android.app.Activity;

public interface SettingContract {

    interface View {
        void setSaveLog(boolean bool);
        void setLogValue(int value);
        void setLogValueString(String text);
        Activity getActivity();
    }

    interface Presenter {
        void initialize();
        void setSaveLog(boolean bool);
        void setLogValue(int value);
        void deleteLog();
        void deleteServer();
        void openFormula();
    }

}

