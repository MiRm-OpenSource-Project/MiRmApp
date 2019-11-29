package jp.mirm.mirmapp.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import jp.mirm.mirmapp.setting.SettingPresenter;
import jp.mirm.mirmapp.setting.SettingContract;
import jp.mirm.mirmapp.R;

public class SettingFragment extends Fragment implements SettingContract.View {

    private SettingPresenter presenter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.presenter = new SettingPresenter(this);
        this.view = view;

        presenter.initialize();

        /*
        AdView mAdView = view.findViewById(R.id.settingAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        final Switch saveLog = view.findViewById(R.id.saveLog);
        saveLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.setSaveLog(saveLog.isChecked());
            }
        });

        view.findViewById(R.id.formulaButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openFormula();
            }
        });

        view.findViewById(R.id.deleteLog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteLog();
            }
        });

        view.findViewById(R.id.deleteServer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteServer();
            }
        });

        final SeekBar consoleBar = view.findViewById(R.id.consoleBar);
        consoleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                presenter.setLogValue(seekBar.getProgress());
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public void setSaveLog(boolean bool) {
        Switch saveLog = view.findViewById(R.id.saveLog);
        saveLog.setChecked(bool);
    }

    @Override
    public void setLogValue(int value) {
        SeekBar consoleBar = view.findViewById(R.id.consoleBar);
        consoleBar.setProgress(value);
    }

    @Override
    public void setLogValueString(String text) {
        TextView textView = view.findViewById(R.id.consoleTick);
        textView.setText(text);
    }
}
