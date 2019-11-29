package jp.mirm.mirmapp.servercontroller;

import com.google.android.gms.ads.reward.RewardItem;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.data.source.remote.PublicAPI;
import jp.mirm.mirmapp.data.task.ExpireTask;
import jp.mirm.mirmapp.data.task.CheckServerStatusTask;
import jp.mirm.mirmapp.data.task.ReloadExpiretimeTask;
import jp.mirm.mirmapp.data.task.ServerManagementTask;
import jp.mirm.mirmapp.utils.AdHolder;
import jp.mirm.mirmapp.utils.AdPlayer;
import jp.mirm.mirmapp.utils.URLHolder;

public class ControllerPresenter implements ControllerContract.Presenter {

    private final ControllerContract.View view;
    private long expireTime;

    ControllerPresenter(ControllerContract.View view) {
        this.view = view;
    }

    @Override
    public void initialize() {
        view.setServerName(MiRmAPI.getServerId());
        reloadExpireTime();

        CheckServerStatusTask task = new CheckServerStatusTask(view);
        task.execute();
    }

    @Override
    public void startServer() {
        ServerManagementTask task = new ServerManagementTask(view, ServerManagementTask.TYPE_START);
        task.execute();
    }

    @Override
    public void stopServer() {
        ServerManagementTask task = new ServerManagementTask(view, ServerManagementTask.TYPE_STOP);
        task.execute();
    }

    @Override
    public void forceStopServer() {
        ServerManagementTask task = new ServerManagementTask(view, ServerManagementTask.TYPE_FORCESTOP);
        task.execute();
    }

    @Override
    public void openPluginManager() {
        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_PLUGIN_MANAGER + "?srvid=" + MiRmAPI.getServerId(), "");
    }

    @Override
    public void openCrushDump() {
        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_CRASH_DUMP + "?srvid=" + MiRmAPI.getServerId(), "");
    }

    @Override
    public void openExpireTime() {
        /*
        AdPlayer player = new AdPlayer(AdHolder.backupRewardId, true) {
            @Override
            public void onAdRewarded(RewardItem item) {
                ExpireTask task = new ExpireTask(ControllerPresenter.this);
                task.execute();
            }
        }.initialize(view.getActivity());
        player.load();
        ExpireTask task = new ExpireTask(ControllerPresenter.this);
        task.execute();*/
        PublicAPI.openBrowser(view.getActivity(), "https://c.mirm.jp/expire?srvid=" + MiRmAPI.getServerId() + "&dodel=false", null);
    }

    @Override
    public void reloadExpireTime() {
        ReloadExpiretimeTask task = new ReloadExpiretimeTask(view, this);
        task.execute();
    }

    @Override
    public void onExpireComplete(String data) {
        int id;

        if (data.equals("true")) {
            id = R.string.toast_expire_success;
        } else {
            id = R.string.toast_expire_failure;
        }

        view.showExpireStatusToast(id);
    }

    @Override
    public long getExpireTime() {
        return expireTime;
    }

    @Override
    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
