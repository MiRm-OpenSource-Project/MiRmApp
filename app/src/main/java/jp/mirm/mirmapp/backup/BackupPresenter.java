package jp.mirm.mirmapp.backup;

import android.os.Handler;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.data.task.BackupTask;
import jp.mirm.mirmapp.progress.ProgressDialog;
import jp.mirm.mirmapp.utils.Property;

import java.io.File;

public class BackupPresenter implements BackupContract.Presenter {

    private final BackupContract.View view;
    private final Handler handler;

    private ProgressDialog dialog;

    BackupPresenter(BackupContract.View view) {
        this.view = view;
        this.handler = new Handler();

        Property property = new Property(MiRmAPI.getServerId());
        if (property.getBackupDirectory() != null) {
            view.setBackupPath(property.getBackupDirectory());
            view.setStartButtonEnabled(true);
        } else {
            view.setStartButtonEnabled(false);
        }
    }

    @Override
    public void startBackup(final String path) {
        /*
        AdPlayer player = new AdPlayer(AdHolder.backupRewardId, true) {
            @Override
            public void onAdRewarded(RewardItem reward) {
                BackupTask task = new BackupTask(BackupPresenter.this);
                task.execute(path);
            }
        }.initialize(view.getActivity());
        player.load();*/
        if (dialog == null) {
            dialog = new ProgressDialog(view.getActivity())
                    .create()
                    .setText(R.string.fragment_backup_dialog_backupping)
                    .show();
        }

        BackupTask task = new BackupTask(BackupPresenter.this);
        task.execute(path);
    }

    @Override
    public void onFileSelected(File selectedFile) {
        if (!selectedFile.isDirectory()) {
            view.showIsNotDirectoryToast();
        } else {
            Property property = new Property(MiRmAPI.getServerId());
            property.setBackupDirectory(selectedFile.getAbsolutePath());
            property.save();

            view.setBackupPath(selectedFile.getAbsolutePath());
            view.setStartButtonEnabled(true);
        }
    }

    @Override
    public void onMakeFolder(File selectedFile) {
        view.showNewFileDialog(selectedFile);
    }

    @Override
    public void onMakeFolderComplete(File madeFile) {
        if (madeFile.exists()) {
            view.showMadeDirectoryExistsToast();
            return;
        }

        if (!madeFile.mkdirs()) {
            view.showCouldNotMakeDirectoryToast();
            return;
        }

        view.refreshFileChooser(madeFile);
        view.showFileChooser();
    }

    @Override
    public void onFinishBackup(final String name) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) dialog.dismiss();
                view.showBackupFinishedNotification(name);
            }
        });
    }

}
