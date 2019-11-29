package jp.mirm.mirmapp.data.task;

import android.os.AsyncTask;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import jp.mirm.mirmapp.MainApplication;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.utils.AdHolder;

public class LoadAdTask extends AsyncTask<Void, Void, RewardedVideoAd> {

    @Override
    protected RewardedVideoAd doInBackground(Void... ids) {
        AdHolder.initialize(false);

        MobileAds.initialize(MainApplication.getInstance(), MainApplication.getInstance().getString(R.string.admob_appid));
        MobileAds.initialize(MainApplication.getInstance(), AdHolder.rewardRewardId);
        MobileAds.initialize(MainApplication.getInstance(), AdHolder.backupRewardId);
        MobileAds.initialize(MainApplication.getInstance(), AdHolder.expireRewardId);

        return MobileAds.getRewardedVideoAdInstance(MainApplication.getInstance());
    }

    @Override
    protected void onPostExecute(RewardedVideoAd ads) {
        AdHolder.setRewardAd(ads);
        AdHolder.setLoaded();
    }

}