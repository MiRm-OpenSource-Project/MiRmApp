package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.console.ConsoleContract;
import jp.mirm.mirmapp.utils.*;

import java.util.Timer;
import java.util.TimerTask;

public class ConsoleTask extends AsyncTask<Void, Void, Void> {

    private final int TIMER_PERIOD;

    private final ConsoleContract.View view;
    private final Timer timer;
    private final Handler handler;
    private final boolean saveLog;

    public ConsoleTask(ConsoleContract.View view) {
        this.view = view;
        this.timer = new Timer(false);
        this.handler =  new Handler();

        Property property = new Property(MiRmAPI.getServerId());
        this.TIMER_PERIOD = property.getConsoleTick();
        this.saveLog = property.getSaveLog();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    final String result = MiRmAPI.getConsoleText(URLHolder.URL_INFO);
                    final String formattedText = ConsoleTextFormatter.format(result);

                    if (saveLog) {
                        Utils.appendFile(Environment.getExternalStorageDirectory().getPath() + "/servers/" + MiRmAPI.getServerId() + "/console.log", formattedText);
                    }

                    if (result != null && !result.isEmpty()) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                view.append(ConsoleTextFormatter.formatToSpanned(formattedText));
                                view.scrollToEnd();
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        }, 1000, TIMER_PERIOD);
        return null;
    }

}
