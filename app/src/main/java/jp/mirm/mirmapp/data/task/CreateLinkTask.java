package jp.mirm.mirmapp.data.task;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import jp.mirm.mirmapp.MainApplication;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.utils.URLHolder;

public class CreateLinkTask extends AsyncTask<Void, Void, Integer> {

    @Override
    protected Integer doInBackground(Void... args) {
        return MiRmAPI.getPort();
    }

    @Override
    protected void onPostExecute(Integer port) {
        String shareURL = "minecraft://?addExternalServer=" + URLHolder.serverPublicURL + ":" + port;
        ClipboardManager clipboardManager = (ClipboardManager) MainApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager == null) return;
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", shareURL));
        Toast.makeText(MainApplication.getInstance().getApplicationContext(), R.string.toast_copied_link, Toast.LENGTH_LONG).show();
    }

}
