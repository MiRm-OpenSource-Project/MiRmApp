package jp.mirm.mirmapp.backup;

import android.app.Activity;

import java.io.File;

public interface BackupContract {

    interface View {
        Activity getActivity();
        void setBackupPath(String path);
        void showFileChooser();
        void showNewFileDialog(File selectedFile);
        void showCouldNotMakeDirectoryToast();
        void showIsNotDirectoryToast();
        void showMadeDirectoryExistsToast();
        void showBackupFinishedNotification(String name);
        void setStartButtonEnabled(boolean bool);
        void refreshFileChooser(File file);
    }

    interface Presenter {
        void startBackup(String path);
        void onFileSelected(File selectedFile);
        void onMakeFolder(File selectedFile);
        void onMakeFolderComplete(File madeFile);
        void onFinishBackup(String name);
    }

}
