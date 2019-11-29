package jp.mirm.mirmapp.data.task;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import jp.mirm.mirmapp.MainApplication;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.utils.URLHolder;

public class GoServerTask extends AsyncTask<Void, Void, Integer> {

    @Override
    protected Integer doInBackground(Void... args) {
        return MiRmAPI.getPort();
    }

    @Override
    protected void onPostExecute(Integer port) {
        try {
            String shareURL = "minecraft://?addExternalServer=" + URLHolder.serverPublicURL + ":" + port;
            MainApplication.getInstance().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shareURL)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainApplication.getInstance().getApplicationContext(), R.string.toast_failedto_open_minecraft, Toast.LENGTH_LONG).show();
        }
    }

}
