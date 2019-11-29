package jp.mirm.mirmapp.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.data.source.remote.PublicAPI;
import jp.mirm.mirmapp.utils.Property;
import jp.mirm.mirmapp.utils.URLHolder;
import jp.mirm.mirmapp.utils.Utils;

public class SettingPresenter implements SettingContract.Presenter {

    private final SettingContract.View view;
    private Property property;

    public SettingPresenter(SettingContract.View view) {
        this.view = view;
    }

    @Override
    public void initialize() {
        property = new Property(MiRmAPI.getServerId());
        view.setLogValue(property.getConsoleTick() / 50);
        view.setLogValueString(property.getConsoleTick() + view.getActivity().getResources().getString(R.string.fragment_setting_message_console_unit));
        view.setSaveLog(property.getSaveLog());
    }

    @Override
    public void setSaveLog(boolean bool) {
        property.setSaveLog(bool);
    }

    @Override
    public void setLogValue(int value) {
        int tick = (value + 1) * 50;
        property.setConsoleTick(tick);
        view.setLogValueString(tick + view.getActivity().getResources().getString(R.string.fragment_setting_message_console_unit));
    }

    @Override
    public void deleteLog() {
        Utils.writeFile(Environment.getExternalStorageDirectory().getPath() + "/servers/" + MiRmAPI.getServerId() + "/console.log", "");
    }

    @Override
    public void deleteServer() {
        Uri uri = Uri.parse(URLHolder.URL_DELETE + "?srvid=" + MiRmAPI.getServerId() + "&dodel=false");
        view.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    public void openFormula() {
        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_FORMULA, "");
    }
}
