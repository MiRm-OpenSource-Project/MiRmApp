package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.backup.BackupPresenter;

import java.util.Timer;
import java.util.TimerTask;

public class BackupTask extends AsyncTask<String, String, Void> {

    private final BackupPresenter presenter;
    private boolean canDownload;

    public BackupTask(BackupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected Void doInBackground(final String... args) {
        final String token = MiRmAPI.sendBackupRequest();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                canDownload = MiRmAPI.canDownloadBackup();
                if (canDownload) {
                    MiRmAPI.downloadBackup(args[0], token, presenter);
                    this.cancel();
                }
            }
        }, 0, 1000);

        return null;
    }

    @Override
    protected void onPostExecute(Void param) {
        //presenter.onFinishBackup();
    }

}
