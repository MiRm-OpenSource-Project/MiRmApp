package jp.mirm.mirmapp.utils;

import com.google.android.gms.ads.reward.RewardedVideoAd;

public class AdHolder {

    private static boolean loaded;

    private static RewardedVideoAd rewardAd;

    public static String testRewardId = "ca-app-pub-3940256099942544/5224354917";

    public static String backupRewardId;
    public static String expireRewardId;
    public static String rewardRewardId;

    public static void initialize(boolean debug) {
        if (debug) {
            backupRewardId = testRewardId;
            expireRewardId = testRewardId;
            rewardRewardId = testRewardId;
        } else {
            backupRewardId = "ca-app-pub-1788933592781069/2517632143";
            expireRewardId = "ca-app-pub-1788933592781069/4645102030";
            rewardRewardId = "ca-app-pub-1788933592781069/7785312836";
        }
    }

    public static void setLoaded() {
        if (!loaded) loaded = true;
    }

    public static boolean isLoaded() {
        return loaded;
    }

    public static RewardedVideoAd getRewardedVideoAd() {
        return rewardAd;
    }

    public static void setRewardAd(RewardedVideoAd rewardAd) {
        AdHolder.rewardAd = rewardAd;
    }
}
