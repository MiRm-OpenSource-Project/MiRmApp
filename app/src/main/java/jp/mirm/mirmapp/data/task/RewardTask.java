package jp.mirm.mirmapp.data.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.utils.Property;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RewardTask extends AsyncTask<Void, String, String> {

    private WeakReference<Activity> weakReference;

    public RewardTask(Activity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    @Override
    protected String doInBackground(Void... commands) {
        return MiRmAPI.reward();
    }

    @Override
    protected void onPostExecute(String status) {
        super.onPostExecute(status);

        int id = 0;
        String text = "";

        if (status.contains("tooearly")) {
            id = R.string.toast_reward_tooearly;

        } else if (status.contains("false") || status.contains("E: Login Failed") || status.contains("E: Server is not found") || status.contains("E: Posted Data is Null.")) {
            id = R.string.toast_reward_failure;

        } else if (status.contains("true:")) {
            id = R.string.toast_reward_complete_1;

        } else {
            try {
                long unixTime = Long.valueOf(status);

                Property property = new Property(MiRmAPI.getServerId());
                property.setNextReward(unixTime * 1000);
                property.save();

                Date time = new Date(unixTime * 1000);
                SimpleDateFormat format = new SimpleDateFormat("yyyy MM/dd HH:mm");
                text = weakReference.get().getResources().getString(R.string.toast_reward_complete_2) + format.format(time);
            } catch (Exception e) {
                id = -1;
            }
        }

        if (text.equals("")) {
            if (id != -1) {
                text = weakReference.get().getResources().getString(id);

            } else {
                Date date;
                try {
                    date = new Date(Long.parseLong(status));
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
                    text = weakReference.get().getResources().getString(R.string.toast_reward_complete_2) + format.format(date);

                } catch (NumberFormatException e) {
                    text = weakReference.get().getResources().getString(R.string.toast_reward_complete_1);
                }
            }
        }

        Toast.makeText(weakReference.get(), text, Toast.LENGTH_SHORT).show();
    }

}