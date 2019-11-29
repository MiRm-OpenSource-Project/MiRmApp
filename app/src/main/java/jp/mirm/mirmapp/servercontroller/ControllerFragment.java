package jp.mirm.mirmapp.servercontroller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import jp.mirm.mirmapp.R;

public class ControllerFragment extends Fragment implements ControllerContract.View {

    private ControllerPresenter presenter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_controller, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        AdView mAdView = view.findViewById(R.id.controllerAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        this.presenter = new ControllerPresenter(this);
        this.view = view;

        presenter.initialize();

        view.findViewById(R.id.serverStartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startServer();
            }
        });

        view.findViewById(R.id.serverStopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.stopServer();
            }
        });

        view.findViewById(R.id.serverForceStopButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.forceStopServer();
            }
        });

        view.findViewById(R.id.reloadExpireButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.reloadExpireTime();
            }
        });

        view.findViewById(R.id.expireButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.openExpireTime();
            }
        });

        view.findViewById(R.id.managePluginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openPluginManager();
            }
        });

        view.findViewById(R.id.crashDumpButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openCrushDump();
            }
        });
    }

    @Override
    public void setProgressValue(int value) {
        ProgressBar bar = view.findViewById(R.id.expireProgressBar);
        bar.setProgress(value);
    }

    @Override
    public void setProgressMaxValue(int value) {
        ProgressBar bar = view.findViewById(R.id.expireProgressBar);
        bar.setMax(value);
    }

    @Override
    public void setExpireTimeString(String text) {
        TextView textView = view.findViewById(R.id.expireTextView);
        textView.setText(text);
    }

    @Override
    public void setServerName(String name) {
        TextView textView = view.findViewById(R.id.serverNameTextView);
        textView.setText(name);
    }

    @Override
    public void setStatus(int id) {
        TextView textView = view.findViewById(R.id.statusTextView);
        textView.setText(id);
    }

    @Override
    public void setInDeterminateMode(boolean bool) {
        ProgressBar bar = view.findViewById(R.id.expireProgressBar);
        bar.setIndeterminate(bool);
    }

    @Override
    public void showConfirmExpireDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity())
                .setTitle(R.string.fragment_controller_message_expiretime_title);

        if (presenter.getExpireTime() > 500) {
            builder.setPositiveButton(R.string.static_button_back, null);
            builder.setMessage(R.string.fragment_controller_message_expiretime_error);

        } else {
            builder.setMessage(R.string.fragment_controller_message_expiretime_confirm);
            builder.setNegativeButton(R.string.static_button_back, null);
            builder.setPositiveButton(R.string.static_button_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    presenter.openExpireTime();
                }
            });
        }

        builder.show();
    }

    @Override
    public void showExpireStatusToast(int id) {
        Toast toast = Toast.makeText(this.getActivity(), id, Toast.LENGTH_LONG);
        toast.show();
    }
}