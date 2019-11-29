package jp.mirm.mirmapp.serverlist;

import android.app.Activity;
import android.content.Intent;

import java.util.List;

public interface ListContract {

    interface View {
        void reloadList();
        void showProgress();
        void hideProgress();
        void setListItems(List<ListCellItem> items);
        Activity getActivity();
    }

    interface Presenter {
        void openAboutMiRm();
        void openHowToUse();
        void createServer();
        void createServerFromWeb(Intent intent);
        void startManagement(String serverId);
        void disconnectServer(String serverId);
        void setState(int state);
        void processFirstRun();
        void initialize();
        void initList();
        int getState();
    }
}
