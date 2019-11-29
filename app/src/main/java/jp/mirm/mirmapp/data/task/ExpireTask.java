package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import jp.mirm.mirmapp.MainApplication;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.servercontroller.ControllerPresenter;
import jp.mirm.mirmapp.utils.URLHolder;

public class ExpireTask extends AsyncTask<Void, Void, String> {

    private final ControllerPresenter presenter;

    public ExpireTask(ControllerPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected String doInBackground(final Void... args) {
        return MiRmAPI.post(URLHolder.URL_EXPIRE, "srvid=" + MiRmAPI.getServerId() + "&app_token=" + MainApplication.getInstance().getResources().getString(R.string.app_token));
    }

    @Override
    protected void onPostExecute(String data) {
        presenter.onExpireComplete(data);
    }

}