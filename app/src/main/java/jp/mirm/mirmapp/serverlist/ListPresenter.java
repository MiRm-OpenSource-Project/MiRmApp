package jp.mirm.mirmapp.serverlist;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.login.LoginActivity;
import jp.mirm.mirmapp.data.source.remote.PublicAPI;
import jp.mirm.mirmapp.utils.Property;
import jp.mirm.mirmapp.utils.URLHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListPresenter implements ListContract.Presenter {

    private final ListContract.View view;

    private static final int PREFERENCE_INIT = 0;
    private static final int PREFERENCE_BOOTED = 1;

    public static final int REQUEST_CODE_1 = 1;

    ListPresenter(ListContract.View view) {
        this.view = view;
    }

    @Override
    public void initialize() {
        initList();
        checkPermission();

        File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/servers/");
        if (!dir.exists()) dir.mkdirs();
    }

    @Override
    public void initList() {
        List<ListCellItem> items = new ArrayList<>();
        File[] properties = new File(Environment.getExternalStorageDirectory().getPath() + "/servers/").listFiles();
        if (properties != null) {
            for (File dir : properties) {
                if (dir.isDirectory() && new File(dir.getAbsolutePath() + "/setting.properties").exists()) {
                    Property prop = new Property(dir.getName());
                    ListCellItem item = new ListCellItem(prop.getServerId(), prop.getDate());
                    items.add(item);
                }
            }
        }

        view.setListItems(items);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this.view.getActivity(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this.view.getActivity(), Manifest.permission.INTERNET)) {
                ActivityCompat.requestPermissions(this.view.getActivity(), new String[]{Manifest.permission.INTERNET}, REQUEST_CODE_1);
            }
        }

        if (ContextCompat.checkSelfPermission(this.view.getActivity(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this.view.getActivity(), Manifest.permission.ACCESS_NETWORK_STATE)) {
                ActivityCompat.requestPermissions(this.view.getActivity(), new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_CODE_1);
            }
        }

        if (ContextCompat.checkSelfPermission(this.view.getActivity(), Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this.view.getActivity(), Manifest.permission.VIBRATE)) {
                ActivityCompat.requestPermissions(this.view.getActivity(), new String[]{Manifest.permission.VIBRATE}, REQUEST_CODE_1);
            }
        }

        if (ContextCompat.checkSelfPermission(this.view.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this.view.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this.view.getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_1);
            }
        }
    }

    @Override
    public void openAboutMiRm() {
        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_ABOUT, "");
    }

    @Override
    public void openHowToUse() {
        PublicAPI.openBrowser(view.getActivity(), URLHolder.URL_HOWTOUSE, "");
    }

    @Override
    public void createServer() {
        Intent intent = new Intent(view.getActivity().getApplication(), LoginActivity.class);
        intent.putExtra("isLogin", false);
        view.getActivity().startActivity(intent);
    }

    @Override
    public void startManagement(String serverId) {
        Intent intent = new Intent(view.getActivity(), LoginActivity.class);

        Property property = new Property(serverId);
        intent.putExtra("isLogin", true);
        intent.putExtra("autoLogin", property.getAutoLogin());
        intent.putExtra("password", property.getPassword());
        intent.putExtra("serverId", property.getServerId());

        view.getActivity().startActivity(intent);
        view.getActivity().finish();
    }

    @Override
    public void disconnectServer(String serverId) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/servers/" + serverId + "/");
        deleteFile(file);
        String text;
        if (file.delete()) {
            text = view.getActivity().getResources().getString(R.string.toast_list_deleteserver_success, serverId);
        } else {
            text = view.getActivity().getResources().getString(R.string.toast_list_deleteserver_failure, serverId);
        }

        Toast.makeText(view.getActivity(), text, Toast.LENGTH_LONG).show();
        view.reloadList();
    }

    private void deleteFile(File file) {
        for (File f : file.listFiles()) {
            if (f.isDirectory()) deleteFile(f);
            else f.delete();
        }
    }

    @Override
    public void setState(int state) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivity());
        sp.edit().putInt("InitState", state).apply();
    }

    @Override
    public void processFirstRun() {
        if (getState() == PREFERENCE_INIT) {
            FirebaseMessaging.getInstance().subscribeToTopic("user")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Log.e("registerTopic", "failed to register FirebaseMessaging topic.");
                            }
                        }
                    });

            setState(PREFERENCE_BOOTED);
        }
    }

    @Override
    public int getState() {
        int state;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(view.getActivity());
        state = sp.getInt("InitState", PREFERENCE_INIT);
        return state;
    }

    @Override
    public void createServerFromWeb(Intent data) {
        try {
            if (MiRmAPI.isLoggedIn()) {
                MiRmAPI.logout();
            }

            Intent intent = new Intent(view.getActivity().getApplication(), LoginActivity.class);
            intent.putExtra("isLogin", false);
            intent.putExtra("fromWeb", true);
            intent.putExtra("serverId", data.getData().getQueryParameter("id"));
            if (data.getData().getQueryParameter("password") != null) intent.putExtra("password", data.getData().getQueryParameter("password"));
            view.getActivity().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
