package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.servercontroller.ControllerContract;
import jp.mirm.mirmapp.utils.URLHolder;

public class CheckServerStatusTask extends AsyncTask<Void, Boolean, Boolean> {

    private final ControllerContract.View view;

    public CheckServerStatusTask(ControllerContract.View view) {
        this.view = view;
    }

    @Override
    protected Boolean doInBackground(Void... commands) {
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