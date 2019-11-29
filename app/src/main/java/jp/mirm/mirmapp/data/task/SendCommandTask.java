package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import jp.mirm.mirmapp.console.ConsolePresenter;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.utils.URLHolder;

import java.util.Timer;
import java.util.TimerTask;

public class SendCommandTask extends AsyncTask<Void, Void, Void> {

    private final ConsolePresenter presenter;
    private final Timer timer;

    public SendCommandTask(ConsolePresenter presenter) {
        this.presenter = presenter;
        this.timer = new Timer(false);
    }

    @Override
    protected Void doInBackground(Void... args) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                String command = presenter.pollCommand();
                if (command != null) MiRmAPI.sendCommand(URLHolder.URL_PANEL, command);
            }
        }, 0, 100);

        return null;
    }

}
