package jp.mirm.mirmapp.progress;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.*;
import android.widget.TextView;
import jp.mirm.mirmapp.R;

import java.lang.ref.WeakReference;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ProgressDialog implements ProgressContract.View {

    private final WeakReference<Activity> activity;
    private AlertDialog dialog;
    private View layout;

    public ProgressDialog(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public ProgressDialog create() {
        LayoutInflater inflater = (LayoutInflater) activity.get().getSystemService(LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.dialog_progress, (ViewGroup) activity.get().findViewById(R.id.progressLayout));

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity.get());
        dialog.setView(layout);
        dialog.setCancelable(false);

        this.dialog = dialog.create();
        return this;
    }

    @Override
    public ProgressDialog show() {
        if (dialog != null) dialog.show();
        return this;
    }

    @Override
    public void dismiss() {
        if (dialog != null) dialog.dismiss();
    }

    @Override
    public ProgressDialog setText(String text) {
        if (layout != null) {
            TextView view = layout.findViewById(R.id.progressText);
            view.setText(text);
        }
        return this;
    }

    @Override
    public ProgressDialog setText(int resId) {
        if (layout != null) {
            TextView view = layout.findViewById(R.id.progressText);
            view.setText(resId);
        }
        return this;
    }
}
