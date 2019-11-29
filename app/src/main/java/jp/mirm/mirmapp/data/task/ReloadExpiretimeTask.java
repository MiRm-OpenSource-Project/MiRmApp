package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.servercontroller.ControllerContract;
import jp.mirm.mirmapp.servercontroller.ControllerPresenter;
import jp.mirm.mirmapp.utils.URLHolder;

public class ReloadExpiretimeTask extends AsyncTask<Void, Void, Integer> {

    private final ControllerContract.View view;
    private final ControllerContract.Presenter presenter;

    public ReloadExpiretimeTask(ControllerContract.View view, ControllerContract.Presenter presenter) {
        this.view = view;
        this.presenter = presenter;
    }

    @Override
    public void onPreExecute() {
        view.setExpireTimeString(view.getActivity().getResources().getString(R.string.fragment_controller_message_refreshing));
        view.setInDeterminateMode(true);
    }

    @Override
    protected Integer doInBackground(Void... args) {
        return MiRmAPI.getExpiredTime(URLHolder.URL_EXPIRE_TIME);
    }

    @Override
    protected void onPostExecute(Integer value) {
        view.setInDeterminateMode(false);

        if (value < 0) {
            view.setProgressMaxValue(1440);
            view.setProgressValue(0);
            view.setExpireTimeString(view.getActivity().getResources().getString(R.string.fragment_controller_message_needexpire));

        } else {
            view.setProgressMaxValue(1440);
            view.setProgressValue(value);

            int hour = value / 60;
            int min = value - 60 * hour;

            view.setExpireTimeString(view.getActivity().getResources().getString(R.string.fragment_controller_message_time, hour, min));
        }

        presenter.setExpireTime(value);
    }

}
