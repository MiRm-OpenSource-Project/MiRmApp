package jp.mirm.mirmapp.backup;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.mirm.mirmapp.MainApplication;
import jp.mirm.mirmapp.data.source.remote.MiRmAPI;
import jp.mirm.mirmapp.R;
import jp.mirm.mirmapp.utils.FileChooserDialog;

public class BackupFragment extends Fragment implements BackupContract.View {

    private View view;
    private BackupPresenter presenter;

    private FileChooserDialog fc;

    public static final int NOTIFICATION_ID = 1347839;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_backup, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        AdView mAdView = view.findViewById(R.id.backupAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        this.view = view;
        this.presenter = new BackupPresenter(this);

        view.findViewById(R.id.backupChoose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        view.findViewById(R.id.backupStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView textView = BackupFragment.this.view.findViewById(R.id.backupPath);
                presenter.startBackup(textView.getText().toString() + "/" + MiRmAPI.getServerId() + "_backup_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".zip");
            }
        });

        fc = new FileChooserDialog(this.getActivity()) {
            @Override
            public void onSelected(File selectedFile) {
                presenter.onFileSelected(selectedFile);
            }

            @Override
            public void onMakeFolder(File selectedFile) {
                presenter.onMakeFolder(selectedFile);
            }
        };

    }

    @Override
    public void setBackupPath(String path) {
        TextView textView = view.findViewById(R.id.backupPath);
        textView.setText(path);
    }

    @Override
    public void showFileChooser() {
        fc.showDialog();
    }

    @Override
    public void showCouldNotMakeDirectoryToast() {
        Toast ts = Toast.makeText(this.getActivity(), R.string.toast_backup_failure_create, Toast.LENGTH_LONG);
        ts.show();
        showFileChooser();
    }

    @Override
    public void setStartButtonEnabled(boolean bool) {
        Button button = view.findViewById(R.id.backupStart);
        button.setEnabled(bool);
    }

    @Override
    public void showIsNotDirectoryToast() {
        Toast ts = Toast.makeText(this.getActivity(), R.string.toast_backup_choose, Toast.LENGTH_LONG);
        ts.show();
        showFileChooser();
    }

    @Override
    public void showMadeDirectoryExistsToast() {
        Toast ts = Toast.makeText(this.getActivity(), R.string.toast_backup_exists, Toast.LENGTH_LONG);
        ts.show();
        showFileChooser();
    }

    @Override
    public void showBackupFinishedNotification(String name) {

        /*
        Notification notification = new Notification();
        notification.icon = R.drawable.logo;
        notification.tickerText = MainApplication.getInstance().getResources().getString(R.string.toast_backup_complete, name);
        ((NotificationManager) MainApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setSmallIcon(R.mipmap.icon);
        builder.setContentText(this.getActivity().getResources().getString(R.string.toast_backup_complete, name));
        NotificationManagerCompat.from(getActivity()).notify(NOTIFICATION_ID, builder.build());*/

        String text;
        if (name != null) {
            text = this.getActivity().getResources().getString(R.string.toast_backup_complete, name);
        } else {
            text = this.getActivity().getResources().getString(R.string.toast_backup_error);
        }

        Toast ts = Toast.makeText(this.getActivity(), text, Toast.LENGTH_LONG);
        ts.show();
    }

    @Override
    public void showNewFileDialog(final File selectedFile) {
        final EditText editView = new EditText(this.getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.fragment_backup_dialog_message_title);
        builder.setView(new EditText(this.getActivity()));
        builder.setPositiveButton(R.string.fragment_backup_dialog_button_create, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                presenter.onMakeFolderComplete(new File(selectedFile + "/" + editView.getText() + "/"));
            }
        });
        builder.setNegativeButton(R.string.static_button_back, null);
        builder.show();
    }

    @Override
    public void refreshFileChooser(File file) {
        fc.refresh(file);
    }
}
