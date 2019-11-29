package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.servercontroller.ControllerContract;
import jp.mirm.mirmapp.utils.URLHolder;

public class ServerManagementTask extends AsyncTask<Void, Boolean, Boolean> {

    public static final String TYPE_START = "load";
    public static final String TYPE_STOP = "stop";
    public static final String TYPE_FORCESTOP = "forceStop";

    private final ControllerContract.View view;
    private final String type;

    public ServerManagementTask(ControllerContract.View view, String type) {
        this.view = view;
        this.type = type;
    }

    @Override
    protected Boolean doInBackground(Void... commands) {
        switch (type) {
            case TYPE_START:
                MiRmAPI.startServer(URLHolder.URL_PANEL);
                break;
            case TYPE_STOP:
                MiRmAPI.stopServer(URLHolder.URL_PANEL);
                break;
            case TYPE_FORCESTOP:
                MiRmAPI.forceStop(URLHolder.URL_PANEL);
                break;
        }
        return MiRmAPI.isServerRunning(URLHolder.URL_STATUS);
    }

    @Override
    protected void onPostExecute(Boolean isRunning) {
        if (isRunning) {
            view.setStatus(R.string.fragment_controller_message_status_running);
        } else {
            view.setStatus(R.string.fragment_controller_message_status_stopped);
        }
    }

}