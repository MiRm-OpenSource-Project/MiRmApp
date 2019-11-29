package jp.mirm.mirmapp.console;

import android.app.Activity;
import android.os.IBinder;
import android.view.KeyEvent;

public interface ConsoleContract {

    interface View {
        void initConsole(String text);
        void append(CharSequence text);
        void clearConsole();
        void clearCommandArea();
        void scrollToEnd();
        IBinder getWindowToken();
        Activity getActivity();
    }

    interface Presenter {
        boolean sendCommand(String command);
        void onClickConsole();
        void onDestroyFragment();
        void initialize();
        void addCommandQueue(String command);
        String pollCommand();
    }

}
