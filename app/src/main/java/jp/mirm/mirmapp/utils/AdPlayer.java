package jp.mirm.mirmapp.utils;

import android.app.Activity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.progress.ProgressDialog;

public class AdPlayer {

    private String id;
    private boolean showDialog;
    private ProgressDialog dialog;

    public AdPlayer(String id) {
        this(id, true);
    }

    public AdPlayer(String id, boolean showDialog) {
        this.id = id;
        this.showDialog = showDialog;
    }

    public AdPlayer initialize(Activity activity) {
        if (showDialog) {
            dialog = new ProgressDialog(activity)
                    .create()
                    .setText(R.string.dialog_progress_message)
                    .show();
        }

        AdHolder.getRewardedVideoAd().setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                if (AdHolder.getRewardedVideoAd().isLoaded()) {
                    if (showDialog) dialog.dismiss();
                    AdHolder.getRewardedVideoAd().show();
                }
            }

            @Override public void onRewarded(RewardItem reward) {onAdRewarded(reward);}
            @Override public void onRewardedVideoAdOpened() {onAdRewardedVideoAdOpened();}
            @Override public void onRewardedVideoStarted() {onAdRewardedVideoStarted();}
            @Override public void onRewardedVideoCompleted() {onAdRewardedVideoCompleted();}
            @Override public void onRewardedVideoAdFailedToLoad(int errorCode) {onAdRewardedVideoAdFailedToLoad(errorCode);}
            @Override public void onRewardedVideoAdClosed() {onAdRewardedVideoAdClosed();}
            @Override public void onRewardedVideoAdLeftApplication() {onAdRewardedVideoAdLeftApplication();}
        });

        return this;
    }

    public void load() {
        AdHolder.getRewardedVideoAd().loadAd(id, new AdRequest.Builder().build());
    }

    public void onAdRewarded(RewardItem reward) {}
    public void onAdRewardedVideoAdOpened() {}
    public void onAdRewardedVideoStarted() {}
    public void onAdRewardedVideoCompleted() {}
    public void onAdRewardedVideoAdFailedToLoad(int errorCode) {}
    public void onAdRewardedVideoAdClosed() {}
    public void onAdRewardedVideoAdLeftApplication() {}
}
